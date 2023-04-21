package org.icepdf.ri.common.annotation;

import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.actions.ActionFactory;
import org.icepdf.core.pobjects.actions.GoToAction;
import org.icepdf.core.pobjects.actions.URIAction;
import org.icepdf.core.pobjects.actions.LaunchAction;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.core.views.swing.AnnotationComponentImpl;
import org.icepdf.ri.common.SwingController;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Actions panel manages an annotations actions as annotation can have zero
 * or more annotations.  The pannel allows a user  add, edit and remove
 * actions for the selected annotation.
 *
 * @since 4.0
 */
public class ActionsPanel extends AnnotationPanelAdapter implements ListSelectionListener, ActionListener {

    private static final Logger logger = Logger.getLogger(ActionsPanel.class.toString());

    private SwingController controller;

    private ResourceBundle messageBundle;

    private AnnotationComponentImpl currentAnnotaiton;

    private DefaultListModel actionListModel;

    private JList actionList;

    private JButton addAction;

    private JButton editAction;

    private JButton removeAction;

    private String destinationLabel;

    private String uriActionLabel;

    private String goToActionLabel;

    private String launchActionLabel;

    private GoToActionDialog goToActionDialog;

    public ActionsPanel(SwingController controller) {
        super(new GridLayout(2, 1, 5, 5), true);
        this.controller = controller;
        this.messageBundle = this.controller.getMessageBundle();
        setFocusable(true);
        createGUI();
        this.setEnabled(false);
        destinationLabel = messageBundle.getString("viewer.utilityPane.action.type.destination.label");
        uriActionLabel = messageBundle.getString("viewer.utilityPane.action.type.uriAction.label");
        goToActionLabel = messageBundle.getString("viewer.utilityPane.action.type.goToAction.label");
        launchActionLabel = messageBundle.getString("viewer.utilityPane.action.type.launchAction.label");
    }

    /**
     * Sets the current annotation component.  The current annotation component
     * is used to build the associated action list and of which all action
     * edits act on.
     *
     * @param annotaiton current action, should not be null.
     */
    public void setAnnotationComponent(AnnotationComponentImpl annotaiton) {
        currentAnnotaiton = annotaiton;
        actionListModel.clear();
        if (annotaiton.getAnnotation() != null && annotaiton.getAnnotation().getAction() != null) {
            addActionToList(annotaiton.getAnnotation().getAction());
        } else if (annotaiton.getAnnotation() != null && annotaiton.getAnnotation() instanceof LinkAnnotation) {
            LinkAnnotation linkAnnotaiton = (LinkAnnotation) annotaiton.getAnnotation();
            if (linkAnnotaiton.getDestination() != null) {
                actionListModel.addElement(new ActionEntry(destinationLabel, null));
            }
        }
        refreshActionCrud();
    }

    /**
     * Handlers for add, edit and delete commands.
     *
     * @param e awt action event
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (currentAnnotaiton == null) {
            logger.warning("No annotation was selected, edit is not possible.");
            return;
        }
        if (source == addAction) {
            addAction();
        } else if (source == editAction) {
            editAction();
        } else if (source == removeAction) {
            int option = JOptionPane.showConfirmDialog(controller.getViewerFrame(), messageBundle.getString("viewer.utilityPane.action.dialog.delete.msgs"), messageBundle.getString("viewer.utilityPane.action.dialog.delete.title"), JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == option) {
                removeAction();
            }
            refreshActionCrud();
        }
    }

    /**
     * Changes events that occur whena user selects an annotation's action in
     * the list actionList.
     *
     * @param e awt list event.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (actionList.getSelectedIndex() == -1) {
                addAction.setEnabled(false);
                editAction.setEnabled(false);
                removeAction.setEnabled(false);
            } else {
                refreshActionCrud();
            }
        }
    }

    /**
     * Shows new action selection dialog and then the appropriate action type
     * dialog for creating/adding new actions to the current annotation.
     */
    private void addAction() {
        Object[] possibilities = { new ActionChoice(messageBundle.getString("viewer.utilityPane.action.type.goToAction.label"), ActionFactory.GOTO_ACTION), new ActionChoice(messageBundle.getString("viewer.utilityPane.action.type.launchAction.label"), ActionFactory.LAUNCH_ACTION), new ActionChoice(messageBundle.getString("viewer.utilityPane.action.type.uriAction.label"), ActionFactory.URI_ACTION) };
        ActionChoice actionType = (ActionChoice) JOptionPane.showInputDialog(controller.getViewerFrame(), messageBundle.getString("viewer.utilityPane.action.dialog.new.msgs"), messageBundle.getString("viewer.utilityPane.action.dialog.new.title"), JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
        if (actionType != null && actionType.getActionType() == ActionFactory.GOTO_ACTION) {
            showGoToActionDialog();
        } else if (actionType != null && actionType.getActionType() == ActionFactory.URI_ACTION) {
            String uriString = showURIActionDialog(null);
            if (uriString != null && currentAnnotaiton != null) {
                org.icepdf.core.pobjects.actions.URIAction uriAction = (URIAction) ActionFactory.buildAction(currentAnnotaiton.getAnnotation().getLibrary(), ActionFactory.URI_ACTION);
                uriAction.setURI(uriString);
                currentAnnotaiton.getAnnotation().addAction(uriAction);
                actionListModel.addElement(new ActionEntry(messageBundle.getString("viewer.utilityPane.action.type.uriAction.label"), uriAction));
            }
        } else if (actionType != null && actionType.getActionType() == ActionFactory.LAUNCH_ACTION) {
            String fileString = showLaunchActionDialog(null);
            if (fileString != null && currentAnnotaiton != null) {
                LaunchAction launchAction = (LaunchAction) ActionFactory.buildAction(currentAnnotaiton.getAnnotation().getLibrary(), ActionFactory.LAUNCH_ACTION);
                launchAction.setExternalFile(fileString);
                currentAnnotaiton.getAnnotation().addAction(launchAction);
                actionListModel.addElement(new ActionEntry(messageBundle.getString("viewer.utilityPane.action.type.launchAction.label"), launchAction));
            }
        }
    }

    /**
     * Shows the appropriate action type edit dialog.
     */
    private void editAction() {
        ActionEntry actionEntry = (ActionEntry) actionListModel.getElementAt(actionList.getSelectedIndex());
        org.icepdf.core.pobjects.actions.Action action = actionEntry.getAction();
        if (action instanceof URIAction) {
            URIAction uriAction = (URIAction) action;
            String oldURIValue = uriAction.getURI();
            String newURIValue = showURIActionDialog(oldURIValue);
            if (newURIValue != null && !oldURIValue.equals(newURIValue)) {
                uriAction.setURI(newURIValue);
                currentAnnotaiton.getAnnotation().updateAction(uriAction);
            }
        } else if (action instanceof GoToAction || action == null) {
            showGoToActionDialog();
        }
        if (action instanceof LaunchAction) {
            LaunchAction launchAction = (LaunchAction) action;
            String oldLaunchValue = launchAction.getExternalFile();
            String newLaunchValue = showLaunchActionDialog(oldLaunchValue);
            if (newLaunchValue != null && !oldLaunchValue.equals(newLaunchValue)) {
                launchAction.setExternalFile(newLaunchValue);
                currentAnnotaiton.getAnnotation().updateAction(launchAction);
            }
        }
    }

    /**
     * Shows a confirmation dialog and if confirmed the action will be removed
     * from the current annotation.
     */
    private void removeAction() {
        ActionEntry actionEntry = (ActionEntry) actionListModel.getElementAt(actionList.getSelectedIndex());
        org.icepdf.core.pobjects.actions.Action action = actionEntry.getAction();
        if (action != null) {
            boolean success = currentAnnotaiton.getAnnotation().deleteAction(action);
            if (success) {
                actionListModel.removeElementAt(actionList.getSelectedIndex());
                actionList.setSelectedIndex(-1);
            }
        } else if (currentAnnotaiton.getAnnotation() instanceof LinkAnnotation) {
            LinkAnnotation linkAnnotation = (LinkAnnotation) currentAnnotaiton.getAnnotation();
            linkAnnotation.getEntries().remove(LinkAnnotation.DESTINATION_KEY);
            updateCurrentAnnotation(linkAnnotation);
            actionListModel.removeElementAt(actionList.getSelectedIndex());
            actionList.setSelectedIndex(-1);
        }
    }

    /**
     * Utility to show the URIAction dialog for add and edits.
     *
     * @param oldURIValue default value to show in dialog text field.
     * @return new values typed by user.
     */
    private String showURIActionDialog(String oldURIValue) {
        return (String) JOptionPane.showInputDialog(controller.getViewerFrame(), messageBundle.getString("viewer.utilityPane.action.dialog.uri.msgs"), messageBundle.getString("viewer.utilityPane.action.dialog.uri.title"), JOptionPane.PLAIN_MESSAGE, null, null, oldURIValue);
    }

    /**
     * Utility to show the LaunchAction dialog for add and edits.
     *
     * @param oldLaunchValue default value to show in dialog text field.
     * @return new values typed by user.
     */
    private String showLaunchActionDialog(String oldLaunchValue) {
        return (String) JOptionPane.showInputDialog(controller.getViewerFrame(), messageBundle.getString("viewer.utilityPane.action.dialog.launch.msgs"), messageBundle.getString("viewer.utilityPane.action.dialog.launch.title"), JOptionPane.PLAIN_MESSAGE, null, null, oldLaunchValue);
    }

    private void showGoToActionDialog() {
        if (goToActionDialog != null) {
            goToActionDialog.dispose();
        }
        goToActionDialog = new GoToActionDialog(controller, this);
        goToActionDialog.setAnnotationComponent(currentAnnotaiton);
        goToActionDialog.setVisible(true);
    }

    /**
     * Refreshes add,edit and remove button state based on the number of actions
     * currently in the action list.
     */
    private void refreshActionCrud() {
        addAction.setEnabled(actionListModel.getSize() == 0);
        editAction.setEnabled(actionListModel.getSize() > 0);
        removeAction.setEnabled(actionListModel.getSize() > 0);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        actionList.setEnabled(enabled);
        actionList.setSelectedIndex(-1);
        boolean isSelectedIndex = actionList.getSelectedIndex() != -1;
        addAction.setEnabled(enabled && actionListModel.getSize() == 0);
        editAction.setEnabled(enabled && isSelectedIndex);
        removeAction.setEnabled(enabled && isSelectedIndex);
    }

    /**
     * Method to create link annotation GUI.
     */
    private void createGUI() {
        setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), messageBundle.getString("viewer.utilityPane.action.selectionTitle"), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
        actionListModel = new DefaultListModel();
        actionList = new JList(actionListModel);
        actionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        actionList.setVisibleRowCount(-1);
        actionList.addListSelectionListener(this);
        JScrollPane listScroller = new JScrollPane(actionList);
        listScroller.setPreferredSize(new Dimension(150, 50));
        add(listScroller);
        addAction = new JButton(messageBundle.getString("viewer.utilityPane.action.addAction"));
        addAction.setEnabled(false);
        addAction.addActionListener(this);
        editAction = new JButton(messageBundle.getString("viewer.utilityPane.action.editAction"));
        editAction.setEnabled(false);
        editAction.addActionListener(this);
        removeAction = new JButton(messageBundle.getString("viewer.utilityPane.action.removeAction"));
        removeAction.setEnabled(false);
        removeAction.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addAction);
        buttonPanel.add(editAction);
        buttonPanel.add(removeAction);
        add(buttonPanel);
        revalidate();
    }

    /**
     * Clear the action list of all action items.
     */
    public void clearActionList() {
        actionListModel.clear();
    }

    /**
     * Add an action to the list.
     * @param action action object to add.
     */
    public void addActionToList(org.icepdf.core.pobjects.actions.Action action) {
        if (action instanceof GoToAction) {
            actionListModel.addElement(new ActionEntry(goToActionLabel, action));
        } else if (action instanceof URIAction) {
            actionListModel.addElement(new ActionEntry(uriActionLabel, action));
        } else if (action instanceof LaunchAction) {
            actionListModel.addElement(new ActionEntry(launchActionLabel, action));
        }
    }

    /**
     * Utility to udpate the action annotation when changes have been made to
     * 'Dest' which has the same notation as 'GoTo'.  It's the pre action way
     * of doign things and is still very common of link Annotations. .
     *
     * @param annotation annotation to update/sync with parent page object.
     */
    private void updateCurrentAnnotation(Annotation annotation) {
        int pageIndex = currentAnnotaiton.getPageIndex();
        PageTree pageTree = currentAnnotaiton.getDocument().getPageTree();
        Page page = pageTree.getPage(pageIndex, this);
        page.updateAnnotation(annotation);
        pageTree.releasePage(page, this);
    }

    /**
     * Action entries used with the actionList component.
     */
    class ActionEntry {

        String title;

        org.icepdf.core.pobjects.actions.Action action;

        /**
         * Creates a new instance of a FindEntry.
         *
         * @param title of found entry
         */
        ActionEntry(String title) {
            super();
            this.title = title;
        }

        ActionEntry(String title, org.icepdf.core.pobjects.actions.Action action) {
            this.action = action;
            this.title = title;
        }

        org.icepdf.core.pobjects.actions.Action getAction() {
            return action;
        }

        public String toString() {
            return title;
        }
    }

    /**
     * An Entry objects for the different action types, used in dialog
     * for creating/adding new actions.
     */
    class ActionChoice {

        String title;

        int actionType;

        ActionChoice(String title, int actionType) {
            super();
            this.actionType = actionType;
            this.title = title;
        }

        int getActionType() {
            return actionType;
        }

        public String toString() {
            return title;
        }
    }
}
