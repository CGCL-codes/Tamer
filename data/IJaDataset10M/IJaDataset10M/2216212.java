package dot.chimera.plugin;

import dot.exceptions.ProgrammingErrorException;
import dot.chimera.*;
import dot.chimera.registry.*;
import dot.chimera.service.*;
import java.util.*;
import java.lang.ref.*;
import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.WindowListener;

/**
 * The <code>WindowManagerPlugin</code> handles diplaying <code>View</code>s, 
 * tool-bars, and handling the menu-bar.  All the API methods to add/remove 
 * menu-bar-items/docks/toolbars are thread safe, so they can be used, without 
 * having to worry about swing's lack of thread safeness.
 * <p>
 * This plugin, and the <i>window manager</i> service that it provides is
 * only half of the equasion as far as window management.  The other half
 * is the "window manager mode" which is what actually displays the UI.
 * <center><img src="WindowManagerArchitecture.png"></center>
 * The devision is labor is that the this plugin handles writing data into
 * the registry, creating {@link Dialog} objects, etc., and the window 
 * manager implementation responds to data written into the registry in
 * order to realize the UI.  There are actually multiple implementations
 * of the window manager implementation... window mode and desktop mode.
 * 
 * @author Rob Clark
 * @version 0.1
 * @see dot.chimera.service.WindowManager dot.chimera.service.WindowManager - 
 *    the service interface implemented by this plugin
 */
public class WindowManagerPlugin extends Plugin {

    private Registry registry;

    /**
   * Counter used to generate a unique id for each toolbar, which is used
   * as part of the path for storing toolbars in the registry.
   */
    private int toolBarIdx = 0;

    /**
   * The current mode service.  Hang on to this to keep the plugin that
   * implements the mode active while it is in use.
   */
    private WindowMode windowMode;

    /**
   * Class Constructor.
   * 
   * @param main         the main application
   */
    public WindowManagerPlugin(Main main) {
        super(main, "Window Manager");
        registry = main.getRegistry();
        registry.subscribeToValue("/Preferences/Window Manager/Mode", null, new NodeSubscriber() {

            public void publish(Node node, Object value) {
                if (windowMode != null) windowMode.stop();
                windowMode = (WindowMode) (registry.getService((String) value));
                windowMode.start();
            }
        });
        final Service service = new WindowManager() {

            private Node toolBarNode = registry.mkdir("/ToolBars");

            public void setMode(WindowMode mode) {
                setValue("/Preferences/Window Manager/Mode", mode.getName());
            }

            public void setLookAndFeel(String lnfName) {
                setValue("/Preferences/Window Manager/Look And Feel", lnfName);
            }

            private void setValue(String path, Object value) {
                try {
                    registry.resolve(path).setValue(value);
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }

            public synchronized dot.chimera.Dialog getDialog(String title) {
                return new WMDialog(title);
            }

            public synchronized Dock getDock(String name) {
                Dock dock = Dock.getDock(name);
                if (dock == null) dock = new Dock(getMain(), name);
                return dock;
            }

            public void addDock(final Dock dock) {
                Dialog dialog = getDialog(dock.getName());
                dock.setDialog(dialog);
                dock.setVisible(true);
                dialog.addCloseRunnable(new Runnable() {

                    public void run() {
                        dock.close();
                    }
                });
                dialog.getContentPane().add(dock);
                if (dialog.getBounds() == null) dialog.pack();
                dialog.setVisible(true);
            }

            public void removeDock(Dock dock) {
                Dialog dialog = dock.getDialog();
                if (dialog != null) {
                    dialog.getContentPane().remove(dock);
                    dialog.dispose();
                    dock.setDialog(null);
                } else {
                    getMain().error("WindowManager", "dock not in dialog");
                }
            }

            public void dockUpdated(Dock dock) {
                Dialog dialog = dock.getDialog();
                if (dialog != null) dialog.pack(); else getMain().error("WindowManager", "dock not in dialog");
            }

            public synchronized void addToolBar(JToolBar toolBar) {
                try {
                    registry.link(new Node(toolBar, new TypeNodeContract(JToolBar.class), "a toolbar"), "/ToolBars/" + (toolBarIdx++));
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }

            public synchronized void removeToolBar(JToolBar toolBar) {
                try {
                    DirectoryTable dt = (DirectoryTable) (registry.resolve("/ToolBars").getValue());
                    for (Iterator itr = dt.getChildNames(); itr.hasNext(); ) {
                        String name = (String) (itr.next());
                        if (dt.get(name).getValue().equals(toolBar)) {
                            registry.unlink("/ToolBars/" + name);
                            return;
                        }
                    }
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }

            private int numSeparators = 0;

            public synchronized void addMenuBarItem(String path, Action a) {
                String name;
                if (a == null) name = "." + (numSeparators++); else name = (String) (a.getValue(Action.NAME));
                try {
                    String rpath = "/MenuBar/" + path + "/" + name;
                    LinkedList actionList;
                    Node node;
                    if (registry.exists(rpath)) {
                        node = registry.resolve(rpath);
                        actionList = (LinkedList) (node.getValue());
                    } else {
                        registry.link(node = new Node(actionList = new LinkedList(), new TypeNodeContract(LinkedList.class), "a list of menubar item actions"), rpath);
                    }
                    if (a != null) {
                        actionList.add(a);
                        node.setValue(actionList);
                    }
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }

            public synchronized void removeMenuBarItem(String path, Action a) {
                try {
                    String rpath = "/MenuBar/" + path + "/" + a.getValue(Action.NAME);
                    if (registry.exists(rpath)) {
                        Node node = registry.resolve(rpath);
                        LinkedList actionList = (LinkedList) (node.getValue());
                        actionList.remove(a);
                        if (actionList.size() == 0) registry.unlink(rpath); else node.setValue(actionList);
                    }
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }
        };
        registerServiceFactory(new ServiceFactory() {

            protected Service createService() {
                return service;
            }
        });
    }

    private Main getMain() {
        return main;
    }

    /**
   * The implementation of the dialog interface
   */
    private class WMDialog implements Dialog {

        /**
     * The dialog's title, must be unique, as it is used as a "key" to
     * uniquely identify a dialog.
     */
        private String title;

        private Node boundsNode = new PersistentNode(null, new TypeNodeContract(Rectangle.class), "the position of this dialog");

        private Node closeRunnableListNode = new Node(new LinkedList(), new TypeNodeContract(LinkedList.class), "the list of runnables to run when dialog is closed");

        private Node contentPaneNode = new Node(new JPanel(new BorderLayout()), new TypeNodeContract(Container.class), "the content pane");

        private Node controlNode = new Node(null, NodeContract.STRING_CONTRACT, "The control command pipe.  Write <i>center</i>, " + "<i>toFront</i>, <i>pack</i> to this pipe.");

        private Node windowListenerListNode = new Node(new LinkedList(), new TypeNodeContract(LinkedList.class), "the list of window listeners");

        private Node instanceNode = new Node(this, new TypeNodeContract(Dialog.class), "the dialog instance; this is immutable, so it is safe to getValue() instead of subscribing.");

        private Node visibleNode = new Node(Boolean.FALSE, NodeContract.BOOLEAN_CONTRACT, "is this dialog visible?");

        /**
     * Table of node paths and nodes...
     */
        private Object[][] nodeTable = new Object[][] { { "bounds", boundsNode }, { "closeRunnableList", closeRunnableListNode }, { "contentPane", contentPaneNode }, { "control", controlNode }, { "windowListenerList", windowListenerListNode }, { "instance", instanceNode }, { "visible", visibleNode } };

        /**
     * Class Constructor.
     */
        WMDialog(String title) {
            if (registry.exists("/Dialogs/" + title)) {
                int idx = 1;
                while (registry.exists("/Dialogs/" + title + " " + idx)) idx++;
                title += " " + idx;
            }
            this.title = title;
            try {
                for (int i = 0; i < nodeTable.length; i++) {
                    registry.link((Node) (nodeTable[i][1]), "/Dialogs/" + title + "/" + nodeTable[i][0]);
                }
            } catch (RegistryException e) {
                throw new ProgrammingErrorException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            addCloseRunnable(new Runnable() {

                public void run() {
                    synchronized (WMDialog.this) {
                        if (modalQueue != null) modalQueue.enqueue("done");
                    }
                }
            });
        }

        /**
     * Get the title of this dialog.
     * 
     * @return a string
     */
        public String getTitle() {
            return title;
        }

        /**
     * Called to set the visible state of this dialog.
     * 
     * @param visible      the new visible state
     */
        public void setVisible(boolean visible) {
            visibleNode.setValue(visible ? Boolean.TRUE : Boolean.FALSE);
            if (!visible && (modalQueue != null)) modalQueue.enqueue("done");
        }

        /**
     * Get the bounds of this dialog, ie the position and size of the window.
     * 
     * @return a rectangle
     */
        public Rectangle getBounds() {
            return (Rectangle) (boundsNode.getValue());
        }

        /**
     * Set the bounds of this dialog, ie. the size and position of the window.
     * 
     * @param r            the new bounds
     */
        public void setBounds(Rectangle r) {
            boundsNode.setValue(r);
        }

        /**
     * Return the content-pane.  This is the component that the contents of
     * the window are to be added to.
     * 
     * @return a container
     */
        public Container getContentPane() {
            return (Container) (contentPaneNode.getValue());
        }

        /**
     * Bring this dialog to the front.
     */
        public void toFront() {
            controlNode.setValue("toFront");
        }

        /**
     * Center this dialog on the screen.
     */
        public void center() {
            controlNode.setValue("center");
        }

        /**
     * Cause the dialog to be sized to fit the preferred size of it's sub-
     * components.
     */
        public void pack() {
            controlNode.setValue("pack");
        }

        /**
     * Dispose of this dialog.
     */
        public synchronized void dispose() {
            if (registry.exists("/Dialogs/" + title)) {
                try {
                    for (int i = 0; i < nodeTable.length; i++) registry.unlink("/Dialogs/" + title + "/" + nodeTable[i][0]);
                    registry.unlink("/Dialogs/" + title);
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }
        }

        /**
     * Show as modal dialog.  Do not return until dialog closed.
     */
        public synchronized void showModal() {
            modalQueue = new dot.swing.SwingQueue();
            setVisible(true);
            modalQueue.dequeue();
            modalQueue = null;
        }

        /**
     * When dialog is shown modally, we block on this until someone  (either
     * close-runnable or setVisible(false) writes to this.
     */
        private dot.swing.SwingQueue modalQueue;

        /**
     * Add a runnable that will be invoked when this dialog is closed.  This
     * gives the user of the dialog a way to perform cleanup when the dialog
     * is closed.
     * 
     * @param r            the runnable
     */
        public void addCloseRunnable(Runnable r) {
            ((LinkedList) (closeRunnableListNode.getValue())).add(r);
        }

        /**
     * Remove the runnable from the list of runnables that will get run when
     * this dialog is closed.
     * 
     * @param r            the runnable
     */
        public void removeCloseRunnable(Runnable r) {
            ((LinkedList) (closeRunnableListNode.getValue())).remove(r);
        }

        /**
     * Adds the specified window listener to receive window events.
     * 
     * @param l            the listener
     */
        public void addWindowListener(WindowListener l) {
            ((LinkedList) (windowListenerListNode.getValue())).add(l);
        }

        /**
     * Removes the specified window listener so that it no longer receives 
     * window events from this component
     * 
     * @param l            the listener
     */
        public void removeWindowListener(WindowListener l) {
            ((LinkedList) (windowListenerListNode.getValue())).remove(l);
        }
    }
}
