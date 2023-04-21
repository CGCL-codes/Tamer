package dot.chimera.plugin;

import dot.exceptions.ProgrammingErrorException;
import dot.chimera.*;
import dot.chimera.registry.*;
import dot.chimera.service.*;
import java.util.*;
import java.lang.ref.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This plugin provides an base implementation of the functionality of
 * the window manager mode that should be in common to all modes.  It
 * provides an abstract base implementation of the {@link WindowMode} 
 * service for the subclass of this class to extend.
 * <p>
 * The mode is only half of the equasion as far as window management.  
 * The other half is the plugin that implements the "window manager" 
 * service, which is the "front end" that the rest of the system uses.
 * <center><img src="WindowManagerArchitecture.png"></center>
 * The devision is labor is that the this plugin responds to data written
 * into the registry.
 *  
 * @author Rob Clark
 * @version 0.1
 */
public abstract class AbstractModePlugin extends Plugin {

    /**
   * We use the registry a lot, so hang on to a copy for easy access...
   */
    protected Registry registry;

    /**
   * The main-window.  This is the window that contains the menu-bar, the 
   * tool-bars, and the JDesktopPane containing all the dialogs
   */
    protected JFrame mainWindow;

    /**
   * This panel contains all the tool-bars.
   */
    private JPanel toolBarPanel;

    /**
   * The subscriber that listens for look & feel changes
   */
    private NodeSubscriber lnfSubscriber;

    /**
   * The subscriber that listens for new dialogs, and creates a 
   * {@link DialogImplementation} for each new dialog.  The dialog
   * implementation itself handles listening for the removal of the
   * dialog, so this subscriber only needs to worry about creation.
   */
    private NodeSubscriber dialogDirectorySubscriber;

    /**
   * The subscriber that listens for new toolbars, and displays them.
   */
    private NodeSubscriber toolBarDirectorySubscriber;

    /**
   * The subscriber to the main-window size
   */
    private NodeSubscriber mainWindowBoundsSubscriber;

    /**
   * Table of DialogUtilities... need to msg the DialogUtility when
   * we leave this mode, so they can unsubscribe but not run the
   * close-runnables when the dialog itself is dispose'd!
   */
    private WeakHashMap dialogUtilityTable = new WeakHashMap();

    /**
   * Class Constructor.
   * 
   * @param main         the main application
   * @param name         the plugin name
   */
    public AbstractModePlugin(Main main, String name) {
        super(main, name);
        registry = main.getRegistry();
        try {
            registry.link(new PersistentNode(getDefaultMainWindowBounds(), new TypeNodeContract(Rectangle.class), "the bounds of the main window"), "/Window Manager/" + getName() + "/bounds");
        } catch (RegistryException e) {
            throw new ProgrammingErrorException(e);
        }
        main.atExit(new Runnable() {

            public void run() {
                if (mainWindow != null) {
                    try {
                        registry.resolve("/Window Manager/" + AbstractModePlugin.this.getName() + "/bounds").setValue(mainWindow.getBounds());
                    } catch (RegistryException e) {
                        throw new ProgrammingErrorException(e);
                    }
                }
            }
        });
    }

    protected Main getMain() {
        return main;
    }

    /**
   * Get the appropriate default main-window size for this mode
   */
    protected abstract Rectangle getDefaultMainWindowBounds();

    /**
   * Base class for service... makes use of startHook()/stopHook()/
   * createDialog() which must be implemented by derived class.
   */
    protected abstract class AbstractWindowMode extends WindowMode {

        protected AbstractWindowMode(String name) {
            super(name);
        }

        public final void start() {
            getMain().debug(1, getName() + ": begin start");
            mainWindow = new JFrame("chimera");
            mainWindow.setVisible(false);
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindowBoundsSubscriber = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(1, getName() + ": mainWindow.setBounds( " + value + " )");
                    mainWindow.setBounds((Rectangle) value);
                    mainWindow.setVisible(true);
                }
            });
            registry.subscribeToValue("/Window Manager/" + AbstractModePlugin.this.getName() + "/bounds", null, mainWindowBoundsSubscriber);
            toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            mainWindow.getContentPane().add(toolBarPanel, BorderLayout.NORTH);
            mainWindow.setJMenuBar(new dot.chimera.MenuBar(getMain(), "/MenuBar"));
            startHook();
            lnfSubscriber = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(1, getName() + ": UIManager.setLookAndFeel( " + value + " )");
                    if (value != null) {
                        try {
                            UIManager.setLookAndFeel((String) value);
                            SwingUtilities.updateComponentTreeUI(mainWindow);
                        } catch (Throwable e) {
                            throw new ProgrammingErrorException(e);
                        }
                    }
                }
            });
            registry.subscribeToValue("/Preferences/Window Manager/Look And Feel", NodeContract.STRING_CONTRACT, lnfSubscriber);
            dialogDirectorySubscriber = new NodeSubscriber() {

                private DirectoryTable lastDt;

                public void publish(Node node, Object value) {
                    DirectoryTable dt = (DirectoryTable) value;
                    for (Iterator added = dt.notIn(lastDt); added.hasNext(); ) {
                        String dialogName = (String) (added.next());
                        getMain().debug(1, getName() + ": createDialog( " + dialogName + " )");
                        createDialog(dialogName);
                    }
                    lastDt = dt;
                }
            };
            registry.subscribeToValue("/Dialogs", null, dialogDirectorySubscriber);
            toolBarDirectorySubscriber = new NodeSubscriber() {

                private DirectoryTable lastDt;

                public void publish(Node node, Object value) {
                    DirectoryTable dt = (DirectoryTable) value;
                    for (Iterator added = dt.notIn(lastDt); added.hasNext(); ) {
                        String basename = (String) (added.next());
                        getMain().debug(1, getName() + ": new tool bar: " + basename);
                        (new NodeSubscriberAdapter(registry) {

                            private JToolBar currentToolBar;

                            public void publish(Node node, Object value) {
                                if (currentToolBar != null) toolBarPanel.remove(currentToolBar);
                                currentToolBar = (JToolBar) value;
                                if (currentToolBar != null) toolBarPanel.add(currentToolBar);
                                toolBarPanel.revalidate();
                                toolBarPanel.repaint();
                            }

                            public void nodeDeleted(Node node) {
                                unsubscribe();
                                publish(node, null);
                            }
                        }).subscribe("/ToolBars/" + basename, null);
                    }
                    lastDt = dt;
                }
            };
            registry.subscribeToValue("/ToolBars", null, toolBarDirectorySubscriber);
            mainWindow.setVisible(true);
            mainWindow.repaint();
            getMain().debug(1, getName() + ": finish start");
        }

        public final void stop() {
            getMain().debug(1, getName() + ": begin stop");
            for (Iterator itr = dialogUtilityTable.keySet().iterator(); itr.hasNext(); ) ((DialogUtility) (itr.next())).stop();
            registry.unsubscribeFromValue(mainWindowBoundsSubscriber);
            registry.unsubscribeFromValue(lnfSubscriber);
            registry.unsubscribeFromValue(dialogDirectorySubscriber);
            registry.unsubscribeFromValue(toolBarDirectorySubscriber);
            mainWindow.dispose();
            mainWindow = null;
            toolBarPanel = null;
            stopHook();
            getMain().debug(1, getName() + ": finish stop");
        }

        /**
     * Called after mainWindow is created, but before subscribes in start()
     */
        protected abstract void startHook();

        /**
     * Called after unsubscribes in stop()
     */
        protected abstract void stopHook();

        /**
     * Called to realize a dialog
     */
        protected abstract void createDialog(String name);
    }

    /**
   * Iterface used by DialogUtility to manipulate the dialog in response to
   * data published by registry
   */
    protected interface DialogImplementation {

        public void setContentPane(Container contentPane);

        public void setBounds(Rectangle bounds);

        public Rectangle getBounds();

        public void setVisible(boolean visible);

        public void center();

        public void toFront();

        public void pack();

        public void dispose();

        public void addWindowListener(WindowListener l);

        public void removeWindowListener(WindowListener l);
    }

    /**
   * Handles the interface between the registry and the dialog subscriber,
   * and other features in common between any dialog implementation.  The
   * dialog implementation should call <code>dispose()</code> and 
   * <code>boundsUpdated()</code> as needed.
   */
    protected class DialogUtility {

        private DialogImplementation impl;

        private String title;

        private java.util.List closeRunnableList;

        private java.util.List windowListenerList;

        private java.util.List nodeSubscriberList = new LinkedList();

        private WeakHashMap boundsTable = new WeakHashMap();

        private WindowListener windowListener = new WindowListener() {

            /**
         * Invoked the first time a window is made visible.
         */
            public void windowOpened(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowOpened(evt);
                }
            }

            /**
         * Invoked when the user attempts to close the window
         * from the window's system menu.  If the program does not 
         * explicitly hide or dispose the window while processing 
         * this event, the window close operation will be cancelled.
         */
            public void windowClosing(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowClosing(evt);
                }
            }

            /**
         * Invoked when a window has been closed as the result
         * of calling dispose on the window.
         */
            public void windowClosed(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowClosed(evt);
                }
            }

            /**
         * Invoked when a window is changed from a normal to a
         * minimized state. For many platforms, a minimized window 
         * is displayed as the icon specified in the window's 
         * iconImage property.
         * @see java.awt.Frame#setIconImage
         */
            public void windowIconified(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowIconified(evt);
                }
            }

            /**
         * Invoked when a window is changed from a minimized
         * to a normal state.
         */
            public void windowDeiconified(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowDeiconified(evt);
                }
            }

            /**
         * Invoked when the window is set to be the user's
         * active window, which means the window (or one of its
         * subcomponents) will receive keyboard events.
         */
            public void windowActivated(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowActivated(evt);
                }
            }

            /**
         * Invoked when a window is no longer the user's active
         * window, which means that keyboard events will no longer
         * be delivered to the window or its subcomponents.
         */
            public void windowDeactivated(WindowEvent evt) {
                synchronized (impl) {
                    for (Iterator itr = windowListenerList.iterator(); itr.hasNext(); ) ((WindowListener) (itr.next())).windowDeactivated(evt);
                }
            }
        };

        /**
     * Class Constructor
     * 
     * @param impl         the dialog implementation
     * @param title        the title of the dialog
     */
        DialogUtility(DialogImplementation impl, String title) {
            this.impl = impl;
            this.title = title;
            installSubscribers();
            dialogUtilityTable.put(this, Boolean.TRUE);
        }

        private void installSubscribers() {
            getMain().debug(0, getName() + ": " + title + ": begin installSubscribers");
            Object tmp;
            registry.subscribeToValue("/Dialogs/" + title + "/contentPane", null, (NodeSubscriber) (tmp = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(0, getName() + ": " + title + ": contentPane=" + value);
                    impl.setContentPane((Container) value);
                }
            })));
            nodeSubscriberList.add(tmp);
            registry.subscribeToValue("/Dialogs/" + title + "/closeRunnableList", null, (NodeSubscriber) (tmp = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(0, getName() + ": " + title + ": closeRunnableList=" + value);
                    closeRunnableList = (java.util.List) value;
                }
            })));
            nodeSubscriberList.add(tmp);
            registry.subscribeToValue("/Dialogs/" + title + "/windowListenerList", null, (NodeSubscriber) (tmp = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    synchronized (impl) {
                        getMain().debug(0, getName() + ": " + title + ": windowListenerList=" + value);
                        windowListenerList = (java.util.List) value;
                    }
                }
            })));
            nodeSubscriberList.add(tmp);
            registry.subscribeToValue("/Dialogs/" + title + "/bounds", null, (NodeSubscriber) (tmp = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(0, getName() + ": " + title + ": bounds=" + value);
                    if (value != null) if (boundsTable.remove(value) == null) impl.setBounds((Rectangle) value);
                }
            })));
            nodeSubscriberList.add(tmp);
            registry.subscribeToValue("/Dialogs/" + title + "/visible", null, (NodeSubscriber) (tmp = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(0, getName() + ": " + title + ": visible=" + value);
                    impl.setVisible(((Boolean) value).booleanValue());
                }
            })));
            nodeSubscriberList.add(tmp);
            registry.subscribeToValue("/Dialogs/" + title + "/control", null, (NodeSubscriber) (tmp = new SwingNodeSubscriber(new NodeSubscriber() {

                public void publish(Node node, Object value) {
                    getMain().debug(0, getName() + ": " + title + ": control=" + value);
                    if (value == null) return;
                    if (value.equals("center")) impl.center(); else if (value.equals("toFront")) impl.toFront(); else if (value.equals("pack")) impl.pack(); else throw new ProgrammingErrorException("unknown control command: " + value);
                }
            })));
            nodeSubscriberList.add(tmp);
            registry.subscribeToDeletion("/Dialogs/" + title, (NodeDeletionSubscriber) (tmp = new NodeDeletionSubscriber() {

                public void nodeDeleted(Node node) {
                    impl.dispose();
                }
            }));
            nodeSubscriberList.add(tmp);
            impl.addWindowListener(windowListener);
            getMain().debug(0, getName() + ": " + title + ": finish installSubscribers");
        }

        private void removeSubscribers() {
            getMain().debug(0, getName() + ": " + title + ": begin removeSubscribers");
            impl.removeWindowListener(windowListener);
            for (Iterator itr = nodeSubscriberList.iterator(); itr.hasNext(); ) {
                Object subscriber = itr.next();
                if (subscriber instanceof NodeSubscriber) registry.unsubscribeFromValue((NodeSubscriber) subscriber); else if (subscriber instanceof NodeCreationSubscriber) registry.unsubscribeFromCreation((NodeCreationSubscriber) subscriber); else if (subscriber instanceof NodeDeletionSubscriber) registry.unsubscribeFromDeletion((NodeDeletionSubscriber) subscriber);
                itr.remove();
            }
            getMain().debug(0, getName() + ": " + title + ": finish removeSubscribers");
        }

        /**
     * To be called by dialog implementation when dialog is disposed
     */
        void dispose() {
            getMain().debug(0, getName() + ": " + title + ": dispose");
            removeSubscribers();
            if (closeRunnableList != null) {
                for (Iterator itr = closeRunnableList.iterator(); itr.hasNext(); ) {
                    ((Runnable) (itr.next())).run();
                    itr.remove();
                }
            }
        }

        /**
     * Called if we are leaving the current mode... removes subscribers
     * but does not run the close-runnables.
     */
        void stop() {
            getMain().debug(0, getName() + ": " + title + ": stop");
            closeRunnableList = null;
            removeSubscribers();
        }

        /**
     * To be called by dialog implementation when bounds changes
     */
        void boundsUpdated() {
            try {
                synchronized (registry) {
                    String path = "/Dialogs/" + title + "/bounds";
                    if (registry.exists(path)) {
                        Rectangle r = impl.getBounds();
                        boundsTable.put(r, Boolean.TRUE);
                        registry.resolve(path).setValue(r);
                    }
                }
            } catch (RegistryException e) {
                throw new ProgrammingErrorException(e);
            }
        }
    }
}
