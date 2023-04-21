package com.explosion.expf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.RepaintManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.expf.help.HelpPane;
import com.explosion.expf.preferences.StandardConfigurationDialog;
import com.explosion.expf.preferences.SystemPreferences;
import com.explosion.expf.preferences.Utils.PreferenceResizeRecorder;
import com.explosion.utilities.GeneralUtils;
import com.explosion.utilities.PrintUtilities;
import com.explosion.utilities.dialog.AboutDialog;
import com.explosion.utilities.dialog.SplashScreen;
import com.explosion.utilities.exception.ExceptionManagerFactory;

/**
 * @author Stephen Cowx Date created:@07-Feb-2003
 * 
 * <p>
 * Listeners in expf applications work as follows.
 * 
 * <p>
 * There is this listener, there are listeners that listen to global events,
 * listeners that listen to local events and listeners that listen to component
 * events. All events that are managed by the ApplicationFramework will come
 * through this listener. Here they will either be responded to or propagated
 * 
 * <p>
 * The difference between local and global events is that local events are
 * events that must be applied to the currently active window and global events
 * are events that apply to all windows whether they are active or not.
 * 
 * <p>
 * e.g. "File >> Open" and "File >> Close all" are global events whereas "File
 * >>" Save is a local event.
 * 
 * <p>
 * Listeners need to be registered as either local or global listeners. They
 * also need to be able to provide a list of events that they listen for i.e
 * they should implement the ExpListener interface.
 * 
 * <P>
 * For every event that occurs, the following logic is followed
 * 
 * 1) If events of this type are handled by this listener then it is handled
 * here and not passed on. <br>
 * 2) If it isn't handled by this listener then: <br>
 * 3) A local listener is sought for the currently active window (by object
 * reference) <br>
 * 4) If there is one and it listens for events of this type, then the event is
 * sent to this listener and not passed any furthur <br>
 * 5) If there isn't one (or there is and it doesn't listen for event of this
 * type) then the event is sent to each global listener that listenes for events
 * of this type <br>
 */
public class NonCompoundExpListener extends ExpListener {

    private static Logger log = LogManager.getLogger(NonCompoundExpListener.class);

    private StandardConfigurationDialog confDialog;

    private SplashScreen splashScreen;

    private boolean splashed = false;

    private List eventQueue = new ArrayList();

    private Map actionsHandledByThisListener;

    private int[] keyCodesHandledByThisListener;

    private Map globalActionListeners = new HashMap();

    private Map localActionListeners = new HashMap();

    private Map componentActionListeners = new HashMap();

    private HelpPane helpPane = null;

    private JFrame helpFrame = null;

    private JInternalFrame helpInternalFrame = null;

    private boolean busy = false;

    /**
     * Constructs this listener
     *
     */
    public NonCompoundExpListener() {
        actionsHandledByThisListener = new HashMap();
        actionsHandledByThisListener.put(ExpConstants.MENU_PROPERTIES, ExpConstants.MENU_PROPERTIES);
        actionsHandledByThisListener.put(ExpConstants.MENU_EXIT, ExpConstants.MENU_EXIT);
        actionsHandledByThisListener.put(ExpConstants.MENU_ABOUT, ExpConstants.MENU_ABOUT);
        actionsHandledByThisListener.put(ExpConstants.MENU_HELP_CONTENTS, ExpConstants.MENU_HELP_CONTENTS);
        actionsHandledByThisListener.put(ExpConstants.MENU_PRINT, ExpConstants.MENU_PRINT);
        for (Iterator it = ExpConstants.looksList.iterator(); it.hasNext(); ) {
            String command = (String) it.next();
            actionsHandledByThisListener.put(command, command);
        }
    }

    /**
     * Action performed
     * 
     * @param e
     */
    public synchronized void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        log.debug("q_execute " + command);
        if (actionsHandledByThisListener.get(command) == null) {
            log.debug("Event not handled by CompoundListener, delegating to locals and globals.");
            Object reference = Application.getLocalReference();
            boolean handled = false;
            if (reference != null) {
                if (localActionListeners != null) {
                    log.debug("Local reference found: " + reference);
                    ArrayList locals = (ArrayList) localActionListeners.get(reference);
                    if (locals != null) {
                        log.debug(locals.size() + " local listeners found for the reference, event being sent to all of them.");
                        for (int i = locals.size() - 1; i >= 0; i--) {
                            ExpActionListener localActionListener = (ExpActionListener) locals.get(i);
                            if (localActionListener != null && localActionListener.getListensFor().get(command) != null) {
                                log.debug("Local listener " + localActionListener + " is listening for this event and is being sent it.");
                                localActionListener.actionPerformed(e);
                                handled = true;
                            } else {
                                if (localActionListener == null) log.debug("Local listener " + localActionListener + " not listening for this event and is being skipped."); else log.debug("Local listener is null and is being skipped.");
                            }
                        }
                    } else {
                        log.debug("No local listeners found for the reference.");
                    }
                }
                if (componentActionListeners != null) {
                    log.debug("There are component listeners, checking for active component.");
                    Object activeComponent = Application.getActiveComponent();
                    if (activeComponent != null) {
                        log.debug("Active component found: " + activeComponent);
                        log.debug("Checking for listeners for this component.");
                        Map localComponents = (Map) componentActionListeners.get(reference);
                        if (localComponents != null) {
                            ArrayList listenersForActiveCompenent = (ArrayList) localComponents.get(activeComponent);
                            if (listenersForActiveCompenent != null) {
                                log.debug(listenersForActiveCompenent.size() + " local listeners found for active component");
                                Iterator it = listenersForActiveCompenent.iterator();
                                while (it.hasNext()) {
                                    ExpActionListener listenerForActiveComponent = (ExpActionListener) it.next();
                                    if (listenerForActiveComponent.getListensFor().get(command) != null) {
                                        log.debug("Component listener " + listenerForActiveComponent + " is listening for this event and is being sent it.");
                                        listenerForActiveComponent.actionPerformed(e);
                                        handled = true;
                                    } else {
                                        log.debug("Component listener " + listenerForActiveComponent + " not listening for this event and is being skipped.");
                                    }
                                }
                            }
                        } else {
                            log.debug("No listeners found this component.");
                        }
                    } else {
                        log.debug("No active component found.");
                    }
                }
                if (((ExpFrame) Application.getApplicationFrame()) != null) ((ExpFrame) Application.getApplicationFrame()).checkEnabled();
                if (handled) {
                    log.debug("Returning from CompoundListener actionPerformed().");
                    return;
                }
            } else {
                log.debug("Application currently has has no local references.");
            }
            if (globalActionListeners != null) {
                log.debug(globalActionListeners.size() + " global references found.");
                Iterator it = globalActionListeners.keySet().iterator();
                while (it.hasNext()) {
                    Object key = it.next();
                    ArrayList globals = (ArrayList) globalActionListeners.get(key);
                    if (globals != null) {
                        log.debug("Reference " + key + " has " + globals.size() + " global listeners.");
                        for (int i = 0; i < globals.size(); i++) {
                            ExpActionListener actionListener = (ExpActionListener) globals.get(i);
                            if (actionListener.getListensFor() != null && actionListener.getListensFor().get(command) != null) {
                                log.debug("Global listener " + actionListener + " is listening for this event and is being sent it.");
                                actionListener.actionPerformed(e);
                                ((ExpFrame) Application.getApplicationFrame()).checkEnabled();
                            } else {
                                if (actionListener != null) log.debug("Global listener " + actionListener + " not listening for this event and is being skipped."); else log.debug("Global listener is null and is being skipped.");
                            }
                        }
                    } else {
                        log.debug("Reference " + key + " has no global listeners.");
                    }
                }
                log.debug("Returning from CompoundListener actionPerformed().");
                return;
            }
        }
        if (command.equals(ExpConstants.MENU_PROPERTIES)) {
            confDialog = new StandardConfigurationDialog(Application.getApplicationFrame(), "Options");
            confDialog.loadPreferences(SystemPreferences.getPreferences());
            confDialog.setVisible(true);
        } else if (command.equals(ExpConstants.MENU_EXIT)) {
            Application.getInstance().exit();
        } else if (ExpConstants.looksMap.get(command) != null) {
            try {
                SystemPreferences.getPreference(ExpConstants.LAF).setValue(((ExpLookAndFeel) ExpConstants.looksMap.get(command)).getClassName());
                Application.getInstance().updateLookAndFeel();
            } catch (Exception ex) {
                com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while updating look and feel.");
            }
        } else if (command.equals(ExpConstants.MENU_HELP_CONTENTS)) {
            try {
                displayHelp();
            } catch (Exception ex) {
                ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception while trying to display help.");
            }
        } else if (command.equals(ExpConstants.MENU_ABOUT)) {
            String applicationName = (String) SystemPreferences.getPreference(ExpConstants.EXPF_APP_NAME).getValue();
            String applicationVersion = (String) SystemPreferences.getPreference(ExpConstants.EXPF_APP_VERSION).getValue();
            String vendor = (String) SystemPreferences.getPreference(ExpConstants.EXPF_APP_VENDOR).getValue();
            String author = (String) SystemPreferences.getPreference(ExpConstants.EXPF_APP_AUTHOR).getValue();
            String year = (String) SystemPreferences.getPreference(ExpConstants.EXPF_APP_COPYRIGHT_YEAR).getValue();
            String vendorUrl = (String) SystemPreferences.getPreference(ExpConstants.EXPF_APP_VENDOR_URL).getValue();
            Map map = new HashMap();
            map.put("Modules", getModulesModel());
            AboutDialog dialog = new AboutDialog(Application.getAboutImage(), applicationName, applicationVersion, vendor, author, year, vendorUrl, Color.white, map, Application.getApplicationFrame());
            GeneralUtils.centreWindowInParent(dialog);
            dialog.setVisible(true);
        } else if (command.equals(ExpConstants.MENU_PRINT)) {
            RepaintManager currentManager = RepaintManager.currentManager(Application.getApplicationFrame());
            currentManager.setDoubleBufferingEnabled(false);
            PrintUtilities.printComponent((Component) Application.getActiveComponent());
            currentManager.setDoubleBufferingEnabled(true);
        }
        if (((ExpFrame) Application.getApplicationFrame()) != null) ((ExpFrame) Application.getApplicationFrame()).checkEnabled();
    }

    /**
     * Returns a tableModel containing information about the modules included in this 
     * application.
     * @return
     */
    private synchronized TableModel getModulesModel() {
        Vector vector = Application.getModules();
        Vector columns = new Vector();
        columns.add("Name");
        columns.add("Description");
        columns.add("Version");
        Vector rows = new Vector();
        for (int i = 0; i < vector.size(); i++) {
            Vector row = new Vector();
            ExpModuleManager manager = (ExpModuleManager) vector.get(i);
            row.add(manager.getName());
            row.add(manager.getDescription());
            row.add(manager.getVersion());
            rows.add(row);
        }
        return new DefaultTableModel(rows, columns);
    }

    /**
     * This method adds a global ExpAction Listener to the compound listener An
     * application can have more than one listener per reference. When events
     * are handled, the CompundListener will iterate through all of the
     * listeners for that reference in the order in which they were added.
     * 
     * @param listener
     */
    public synchronized void addGlobalActionListener(ExpActionListener listener, Object reference) {
        log.debug("q_addGlobalActionListener");
        if (listener == null) return;
        if (globalActionListeners == null) {
            globalActionListeners = new HashMap();
            ArrayList listenersForReference = new ArrayList();
            listenersForReference.add(listener);
            globalActionListeners.put(reference, listenersForReference);
        } else {
            ArrayList listenersForReference = (ArrayList) globalActionListeners.get(reference);
            if (listenersForReference != null) {
                if (!listenersForReference.contains(listener)) listenersForReference.add(listener);
            } else {
                listenersForReference = new ArrayList();
                listenersForReference.add(listener);
                globalActionListeners.put(reference, listenersForReference);
            }
        }
    }

    /**
     * This method removes all of the global action listener from the compound
     * listener for the given reference
     * 
     * @param listener
     */
    public synchronized void removeGlobalActionListenersForReference(Object reference) {
        log.debug("q_removeGlobalActionListenersForReference");
        if (reference == null) return;
        if (globalActionListeners == null) return;
        globalActionListeners.remove(reference);
    }

    /**
     * This method adds a local ExpAction Listener to the compound listener
     * 
     * @param listener
     */
    public synchronized void addLocalActionListener(ExpActionListener listener, Object reference) {
        log.debug("q_addLocalActionListener");
        if (listener == null) return;
        if (localActionListeners == null) {
            localActionListeners = new HashMap();
            ArrayList listenersForReference = new ArrayList();
            listenersForReference.add(listener);
            localActionListeners.put(reference, listenersForReference);
        } else {
            ArrayList listenersForReference = (ArrayList) localActionListeners.get(reference);
            if (listenersForReference != null) {
                if (!listenersForReference.contains(listener)) listenersForReference.add(listener);
            } else {
                listenersForReference = new ArrayList();
                listenersForReference.add(listener);
                localActionListeners.put(reference, listenersForReference);
            }
        }
    }

    /**
     * This method removes the specified local action listener from the compound
     * listener
     * 
     * @param listener
     */
    public synchronized void removeLocalActionListenerForReference(ExpActionListener listener, Object reference) {
        log.debug("q_removeLocalActionListenerForReference");
        if (localActionListeners == null) return;
        localActionListeners.remove(reference);
    }

    /**
     * This method adds a component ExpAction Listener to the compound listener
     * 
     * @param listener
     */
    public synchronized void addComponentActionListener(ExpActionListener listener, Object localReference, Object componentReference) {
        log.debug("q_addComponentActionListener");
        if (listener == null) return;
        log.debug("Adding component listener " + listener + " for component  " + componentReference + " for local reference " + localReference);
        if (componentActionListeners == null) {
            log.debug("Component action listeners does not exist.");
            ArrayList listenersForComponent = new ArrayList();
            listenersForComponent.add(listener);
            HashMap listenersForReference = new HashMap();
            listenersForReference.put(componentReference, listenersForComponent);
            componentActionListeners = new HashMap();
            componentActionListeners.put(localReference, listenersForReference);
        } else {
            log.debug("Component action listeners exists.");
            HashMap listenersForReference = (HashMap) componentActionListeners.get(localReference);
            if (listenersForReference != null) {
                log.debug("There are Listeners for this reference");
                if (listenersForReference.containsKey(componentReference)) {
                    log.debug("There are listeners for this component");
                    ArrayList listenersForComponent = (ArrayList) listenersForReference.get(componentReference);
                    listenersForComponent.add(listener);
                } else {
                    log.debug("There are no listeners previous for this component.");
                    ArrayList listenersForComponent = new ArrayList();
                    listenersForComponent.add(listener);
                    listenersForReference.put(componentReference, listenersForComponent);
                }
            } else {
                log.debug("There are no previous listeners for this reference");
                ArrayList listenersForComponent = new ArrayList();
                listenersForComponent.add(listener);
                listenersForReference = new HashMap();
                listenersForReference.put(componentReference, listenersForComponent);
                componentActionListeners.put(localReference, listenersForReference);
            }
        }
    }

    /**
     * This method removes the specified component action listener from the
     * compound listener
     * 
     * @param listener
     */
    public synchronized void removeComponentActionListenerForReference(ExpActionListener listener, Object localReference) {
        log.debug("q_removeComponentActionListenerForReference");
        if (componentActionListeners == null) return;
        componentActionListeners.remove(localReference);
    }

    /**
     * Clean sup all references to any objects registered with this reference
     */
    public synchronized void cleanUpForReference(Object reference) {
        log.debug("q_cleanUpForReference started");
        if (localActionListeners != null) localActionListeners.remove(reference);
        if (globalActionListeners != null) globalActionListeners.remove(reference);
        if (componentActionListeners != null) componentActionListeners.remove(reference);
        log.debug("q_cleanUpForReference finished");
    }

    /**
     * logic for displaying thehelp screen in its correct format
     */
    private void displayHelp() throws Exception {
        int heightOfIt = ((Integer) SystemPreferences.getPreference(ExpConstants.HELP_HEIGHT).getValue()).intValue();
        int widthOfIt = ((Integer) SystemPreferences.getPreference(ExpConstants.HELP_WIDTH).getValue()).intValue();
        if (helpPane == null) helpPane = new HelpPane((String) SystemPreferences.getPreference(ExpConstants.HELP_STARTUP_ID).getValue());
        boolean helpEmbedded = ((Boolean) SystemPreferences.getPreference(ExpConstants.HELP_EMBEDDED).getValue()).booleanValue();
        if (helpEmbedded) {
            helpInternalFrame = ((ExpFrame) Application.getApplicationFrame()).getFrameWithComponent(helpPane, ExpFrame.PALETTE_LAYER.intValue());
            if (helpInternalFrame != null) {
                if (helpInternalFrame.isVisible()) return; else {
                    Application.getInstance().updateLookAndFeel(helpInternalFrame);
                    helpInternalFrame.setVisible(true);
                    return;
                }
            }
            if (helpFrame != null && helpFrame.isVisible()) helpFrame.setVisible(false);
            ExpInternalFrame frame = ((ExpFrame) Application.getApplicationFrame()).createPaletteFrame(helpPane, new Dimension(widthOfIt, heightOfIt), "Help", false);
            frame.addSizePersistence(SystemPreferences.getPreference(ExpConstants.HELP_HEIGHT), SystemPreferences.getPreference(ExpConstants.HELP_WIDTH));
            frame.centreInParent();
        } else {
            if (helpFrame == null) {
                helpFrame = new JFrame("Help");
                ImageIcon icon = ((ExpFrame) Application.getApplicationFrame()).getFrameIcon();
                if (icon != null) helpFrame.setIconImage(icon.getImage());
                helpFrame.setContentPane(helpPane);
                helpFrame.setSize(widthOfIt, heightOfIt);
                GeneralUtils.centreWindowInParent(helpFrame, Application.getApplicationFrame());
                helpFrame.addComponentListener(new PreferenceResizeRecorder(SystemPreferences.getPreference(ExpConstants.HELP_HEIGHT), SystemPreferences.getPreference(ExpConstants.HELP_WIDTH), helpFrame));
            } else if (helpFrame.isVisible()) return;
            if (helpInternalFrame != null && helpInternalFrame.isVisible()) helpInternalFrame.setVisible(false);
            helpFrame.setContentPane(helpPane);
            Application.getInstance().updateLookAndFeel(helpFrame);
            helpFrame.setVisible(true);
        }
    }

    /**
     * This method sets the look and feel of the help frame and it's children
     * This is done because it doesn't happen automatically
     */
    protected void updateLookAndFeelOfHelp() throws Exception {
        boolean helpEmbedded = ((Boolean) SystemPreferences.getPreference(ExpConstants.HELP_EMBEDDED).getValue()).booleanValue();
        boolean showing = false;
        if (helpInternalFrame != null && !helpEmbedded) {
            if (helpInternalFrame.isVisible()) showing = true;
            ((ExpFrame) Application.getApplicationFrame()).closeFrameWithComponent(helpPane, ExpFrame.PALETTE_LAYER);
        }
        if (helpFrame != null) {
            if (helpFrame.isVisible()) showing = true;
            helpFrame.dispose();
            helpPane = null;
        }
        if (showing) displayHelp();
    }

    /**
     * Responds to for F1 key code currently. It pops up a help window assuming
     * it recieves the event.
     */
    public synchronized void keyPressed(KeyEvent e) {
    }

    /**
     * Does nothing currently
     */
    public synchronized void keyReleased(KeyEvent e) {
    }

    /**
     * Does nothing currently
     */
    public synchronized void keyTyped(KeyEvent e) {
    }
}
