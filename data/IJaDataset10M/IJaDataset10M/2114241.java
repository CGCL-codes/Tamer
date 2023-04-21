package ro.masc.server.gui;

import com.ibm.aglet.AgletProxy;
import ro.masc.server.core.TextResolver;
import ro.masc.server.gui.action.*;
import ro.masc.server.mediator.Mediator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EventObject;

/**
 * Description
 *
 * @author <a href="mailto:andrei.chiritescu@gmail.com">Andrei Chiritescu</a>
 * @version $Revision: 1.3 $
 *          $Date: 2005/05/08 21:04:25 $
 */
public class MainWindow extends BaseFrame {

    public static final String TITLE = "window.main.title";

    private JTabbedPane tabbedPane = new JTabbedPane();

    private JTextArea logPanel = new JTextArea();

    private Mediator mediator;

    int currentHeader;

    public MainWindow() throws HeadlessException {
        super(TITLE);
        buildMenu();
        logPanel.setEditable(false);
        logPanel.setName("Main log");
        logPanel.setAutoscrolls(true);
        tabbedPane.add(logPanel);
        this.getContentPane().add(BorderLayout.CENTER, tabbedPane);
        setPreferredSize(new Dimension(600, 400));
        pack();
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu(resolve("window.main.menu.file"));
        JMenuItem menuItemNewList = new JMenuItem(new ActionNewList<MainWindow>(this));
        menuFile.add(menuItemNewList);
        JMenuItem menuItemOpenList = new JMenuItem(new ActionOpenList<MainWindow>(this));
        menuFile.add(menuItemOpenList);
        JMenuItem menuItemSaveList = new JMenuItem(new ActionSaveList<MainWindow>(this));
        menuFile.add(menuItemSaveList);
        JMenuItem menuItemExit = new JMenuItem(new ActionExit<MainWindow>(this));
        menuFile.add(menuItemExit);
        menuBar.add(menuFile);
        JMenu menuAction = new JMenu(resolve("window.main.menu.actions"));
        JMenuItem menuItemCreate = new JMenuItem(new ActionCreate<MainWindow>(this));
        menuAction.add(menuItemCreate);
        JMenuItem menuItemDispose = new JMenuItem(new ActionDispose<MainWindow>(this));
        menuAction.add(menuItemDispose);
        JMenuItem menuItemDispatch = new JMenuItem(new ActionDispatch<MainWindow>(this));
        menuAction.add(menuItemDispatch);
        JMenuItem menuItemCallback = new JMenuItem(new ActionCallback<MainWindow>(this));
        menuAction.add(menuItemCallback);
        JMenuItem menuItemActivate = new JMenuItem(new ActionActivate<MainWindow>(this));
        menuAction.add(menuItemActivate);
        JMenuItem menuItemDeactivate = new JMenuItem(new ActionDeactivate<MainWindow>(this));
        menuAction.add(menuItemDeactivate);
        JMenuItem menuItemClone = new JMenuItem(new ActionClone<MainWindow>(this));
        menuAction.add(menuItemClone);
        menuBar.add(menuAction);
        JMenu menuHelp = new JMenu(resolve("window.main.menu.help"));
        JMenuItem menuItemAbout = new JMenuItem(new ActionAbout<MainWindow>(this));
        menuHelp.add(menuItemAbout);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    protected void onClose(EventObject e) {
        System.out.println("exit");
        getMediator().shutdown();
        super.onClose(e);
    }

    public void handleAction(final ActionCommand actionCommand, final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    switch(actionCommand) {
                        case ACTION_CREATE:
                            {
                                final CreateAgletWindow chooser = new CreateAgletWindow(MainWindow.this);
                                chooser.setVisible(true);
                                Object sel = chooser.getSelectedValue();
                                if (sel == null) break;
                                AgletProxy proxy = getMediator().createAglet(sel.toString());
                                if (proxy == null) break;
                                InfoWindow info = new InfoWindow(proxy);
                                tabbedPane.add(info);
                                tabbedPane.setSelectedComponent(info);
                                break;
                            }
                        case ACTION_DISPOSE:
                            {
                                InfoWindow comp = getInfoWindow(tabbedPane.getSelectedIndex());
                                if (comp != null && comp.getProxy().isValid()) {
                                    comp.getProxy().dispose();
                                    tabbedPane.remove(comp);
                                }
                                break;
                            }
                        case ACTION_ACTIVATE:
                            {
                                InfoWindow comp = getInfoWindow(tabbedPane.getSelectedIndex());
                                if (comp != null && comp.getProxy().isValid()) comp.getProxy().activate();
                                break;
                            }
                        case ACTION_DEACTIVATE:
                            {
                                InfoWindow comp = getInfoWindow(tabbedPane.getSelectedIndex());
                                if (comp != null && comp.getProxy().isValid()) comp.getProxy().deactivate(1000);
                                break;
                            }
                        case ACTION_CLONE:
                            {
                                InfoWindow comp = getInfoWindow(tabbedPane.getSelectedIndex());
                                AgletProxy clone = null;
                                if (comp != null && comp.getProxy().isValid()) {
                                    clone = (AgletProxy) comp.getProxy().clone();
                                }
                                if (clone != null) {
                                    InfoWindow info = new InfoWindow(clone);
                                    tabbedPane.add(info);
                                    tabbedPane.setSelectedComponent(info);
                                }
                                break;
                            }
                        case ACTION_EXIT:
                            {
                                exit(e);
                                break;
                            }
                    }
                } catch (Exception ex) {
                    log.debug("error while handling action", ex);
                    ex.printStackTrace();
                    showErrorDlg(ex.toString());
                }
            }
        });
    }

    public void updateInfo(String string, AgletProxy agletProxy) {
        InfoWindow toUpdate = null;
        boolean found = false;
        for (int i = 0; i < tabbedPane.getComponents().length && !found && agletProxy != null; i++) {
            toUpdate = getInfoWindow(i);
            if (toUpdate != null && toUpdate.equalsTo(agletProxy)) found = true;
        }
        if (toUpdate != null && found) {
            showInfoDlg(string + agletProxy);
            toUpdate.append(string);
            toUpdate.append("\n");
        } else {
            logPanel.append(string);
            logPanel.append("\n");
        }
    }

    private InfoWindow getInfoWindow(int idx) {
        Component comp = tabbedPane.getComponentAt(idx);
        return comp instanceof InfoWindow ? (InfoWindow) comp : null;
    }

    protected TextResolver getResolver() {
        return TextResolver.getTextResolver();
    }

    public Mediator getMediator() {
        return mediator;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
    }
}
