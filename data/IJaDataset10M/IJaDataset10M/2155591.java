package org.kineticsystem.commons.data.view;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kineticsystem.commons.data.controller.DataNavigator;
import org.kineticsystem.commons.data.controller.DataNavigatorEvent;
import org.kineticsystem.commons.data.controller.DataNavigatorListener;
import org.kineticsystem.commons.data.controller.NavigatorEvent;
import org.kineticsystem.commons.data.controller.NavigatorListener;
import org.kineticsystem.commons.data.view.actions.CancelAction;
import org.kineticsystem.commons.data.view.actions.ConfirmAction;
import org.kineticsystem.commons.data.view.actions.CreateAction;
import org.kineticsystem.commons.data.view.actions.DeleteAction;
import org.kineticsystem.commons.data.view.actions.ModifyAction;
import org.kineticsystem.commons.data.view.actions.MoveBackAction;
import org.kineticsystem.commons.data.view.actions.MoveBackMouseAction;
import org.kineticsystem.commons.data.view.actions.MoveFirstAction;
import org.kineticsystem.commons.data.view.actions.MoveForwardAction;
import org.kineticsystem.commons.data.view.actions.MoveForwardMouseAction;
import org.kineticsystem.commons.data.view.actions.MoveLastAction;
import org.kineticsystem.commons.data.view.actions.ReloadAction;
import org.kineticsystem.commons.data.view.actions.SynchronizeAction;

/**
 * <p>Questa classe rappresenta un componente per la gestione dell'inserimento,
 * modifica e cancellazione di oggetti generici in una lista. Il componente
 * utilizza un oggetto di tipo <code>JObjectNavigator</code> per navigare
 * la lista di oggetti. Utilizza inoltre un componente ausiliario, che deve
 * estendere la classe <code>JObjectEditorComponent</code>, per editare
 * l'oggetto specifico presente nella lista.</p>
 * <p>Un'altra particolarit� del componente � la capacit� di sincronizzare i
 * dati presenti nella lista con una sorgente dati tramite l'utilizzo di
 * due componenti esterni, un <code>Resolver</code> ed un <code>Provider</code>.
 * Questi due componenti vanno agganciati all'oggetto che gestisce la logica
 * dell'editor, recuperabile tramite il metodo <code>getManager</code>. Come
 * il pannello per l'editazione di un oggetto, anche il <code>Resolver</code> e
 * il <code>Provider</code> sono specifici per la lista di oggetti e vanno
 * opportunamente implementati.</p>
 * @author Giovanni Remigi
 * @version $Revision: 146 $
 */
@SuppressWarnings("serial")
public class JDataNavigator extends JPanel {

    /** Logging framework. */
    private static Log logger = LogFactory.getLog(JDataNavigator.class);

    /** Name of the action to create a new item. */
    public static final String CREATE_ACTION_NAME = CreateAction.ACTION_NAME;

    /** Name of the action to confirm the creation or the change of an item. */
    public static final String CONFIRM_ACTION_NAME = ConfirmAction.ACTION_NAME;

    /** Name of the action to modify an item. */
    public static final String MODIFY_ACTION_NAME = ModifyAction.ACTION_NAME;

    /** Name of the action to cancel the creation or the change of an item. */
    public static final String CANCEL_ACTION_NAME = CancelAction.ACTION_NAME;

    /** Name of the action to delete the selected item. */
    public static final String DELETE_ACTION_NAME = DeleteAction.ACTION_NAME;

    /** Name of the action to persist the list in the repository. */
    public static final String SYNCHRONIZE_ACTION_NAME = SynchronizeAction.ACTION_NAME;

    /** Name of the action to reload the list from the repository. */
    public static final String RELOAD_ACTION_NAME = ReloadAction.ACTION_NAME;

    /** Name of the action to jump to the first item of the list. */
    public static final String MOVE_FIRST_ACTION_NAME = MoveFirstAction.ACTION_NAME;

    /** Name of the action to move to the next element of the list. */
    public static final String MOVE_BACK_ACTION_NAME = MoveBackAction.ACTION_NAME;

    /** Name of the action to move to the previous element of the list. */
    public static final String MOVE_FORWARD_ACTION_NAME = MoveForwardAction.ACTION_NAME;

    /** Name of the action to jump to the last item of the list. */
    public static final String MOVE_LAST_ACTION_NAME = MoveLastAction.ACTION_NAME;

    /** Action used to create a new object. */
    protected Action createAction;

    /** Action used to confirm an update, insertion or removal of an object. */
    protected Action confirmAction;

    /** Action used to modify the selected object. */
    protected Action modifyAction;

    /** Action used to cancel an update, or an insertion. */
    protected Action cancelAction;

    /** Action used to delete the selected object. */
    protected Action deleteAction;

    /** Action to synchonize the modified list with the database. */
    protected Action synchronizeAction;

    /** Action to reload everithing from the database. */
    protected Action reloadAction;

    /** Action used to jump to the first element of the list. */
    protected Action moveFirstAction;

    /** Action used to move backward. */
    protected Action moveBackAction;

    /** Action used to move forward. */
    protected Action moveForwardAction;

    /** Action used to automatically move backward. */
    protected Action moveBackMouseAction;

    /** Action used to automatically move forward. */
    protected Action moveForwardMouseAction;

    /** Action used to jump to the last element of the list. */
    protected Action moveLastAction;

    /** Force <tt>CreateAction</tt> disabled/enabled. */
    private boolean createActionEnabled;

    /** Force <tt>ConfirmAction</tt> disabled/enabled. */
    private boolean confirmActionEnabled;

    /** Force <tt>ModifyAction</tt> disabled/enabled. */
    private boolean modifyActionEnabled;

    /** Force <tt>CancelAction</tt> disabled/enabled. */
    private boolean cancelActionEnabled;

    /** Force <tt>DeleteAction</tt> disabled/enabled. */
    private boolean deleteActionEnabled;

    /** Force <tt>SynchronizeAction</tt> disabled/enabled. */
    private boolean synchronizeActionEnabled;

    /** Force <tt>ReloadAction</tt> disabled/enabled. */
    private boolean reloadActionEnabled;

    /** Navigator message renderer. */
    protected JDataNavigatorRenderer renderPane;

    /** Button used for backward navigation of the list. */
    private JActionButton moveBackButton;

    /** Button used for forward navigation of the list. */
    private JActionButton moveForwardButton;

    /** Button used to jump to the first element of the list. */
    private JActionButton moveFirstButton;

    /** Button used to jump to the last element of the list. */
    private JActionButton moveLastButton;

    /** Button used to create a new element. */
    private JActionButton createButton;

    /**
     * Button used to modify a list element or to confirm the change or the
     * creation of a new element.
     */
    private JActionButton modifyConfirmButton;

    /**
     * Button used to delete a list element or to cancel the change or the
     * creation of a new element.
     */
    private JActionButton deleteCancelButton;

    /** Button used to presist the list in the default repository. */
    private JActionButton syncronizeButton;

    /** Button used to reload the list from the default repository. */
    private JActionButton reloadButton;

    /**
     * The panel containing the renderer to show information about the current
     * state of the editor.
     */
    protected JPanel infoPane;

    /** The map containing all available actions. */
    private Map<String, Action> actions;

    /** Componente di gestione dell'editor dell'oggetto. */
    private DataNavigator dataNavigator;

    /** Constructor. */
    public JDataNavigator() {
        dataNavigator = new DataNavigator();
        dataNavigator.addEditorListener(new DataNavigatorListener() {

            public void editorStateChanged(DataNavigatorEvent event) {
                update();
            }
        });
        dataNavigator.getNavigator().addNavigatorListener(new NavigatorListener() {

            public void objectSelected(NavigatorEvent e) {
                update();
            }
        });
        initActions();
        initComponents();
        dataNavigator.getNavigator().reset();
    }

    /** Action initialization. */
    private void initActions() {
        createActionEnabled = true;
        confirmActionEnabled = true;
        modifyActionEnabled = true;
        cancelActionEnabled = true;
        deleteActionEnabled = true;
        synchronizeActionEnabled = true;
        reloadActionEnabled = true;
        createAction = new CreateAction(dataNavigator);
        confirmAction = new ConfirmAction(dataNavigator);
        deleteAction = new DeleteAction(dataNavigator);
        cancelAction = new CancelAction(dataNavigator);
        modifyAction = new ModifyAction(dataNavigator);
        synchronizeAction = new SynchronizeAction(dataNavigator);
        reloadAction = new ReloadAction(dataNavigator);
        moveFirstAction = new MoveFirstAction(dataNavigator.getNavigator());
        moveBackAction = new MoveBackAction(dataNavigator.getNavigator());
        moveForwardAction = new MoveForwardAction(dataNavigator.getNavigator());
        moveLastAction = new MoveLastAction(dataNavigator.getNavigator());
        moveForwardMouseAction = new MoveForwardMouseAction(dataNavigator.getNavigator());
        moveBackMouseAction = new MoveBackMouseAction(dataNavigator.getNavigator());
        actions = new HashMap<String, Action>();
        actions.put(CreateAction.ACTION_NAME, createAction);
        actions.put(ConfirmAction.ACTION_NAME, confirmAction);
        actions.put(DeleteAction.ACTION_NAME, deleteAction);
        actions.put(CancelAction.ACTION_NAME, cancelAction);
        actions.put(ModifyAction.ACTION_NAME, modifyAction);
        actions.put(SynchronizeAction.ACTION_NAME, synchronizeAction);
        actions.put(ReloadAction.ACTION_NAME, reloadAction);
        actions.put(MoveFirstAction.ACTION_NAME, moveFirstAction);
        actions.put(MoveBackAction.ACTION_NAME, moveBackAction);
        actions.put(MoveForwardAction.ACTION_NAME, moveForwardAction);
        actions.put(MoveLastAction.ACTION_NAME, moveLastAction);
        actions.put(MoveBackMouseAction.ACTION_NAME, moveBackMouseAction);
        actions.put(MoveForwardMouseAction.ACTION_NAME, moveForwardMouseAction);
    }

    /** GUI initialization. */
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        moveBackButton = new JActionButton();
        moveForwardButton = new JActionButton();
        moveFirstButton = new JActionButton();
        moveLastButton = new JActionButton();
        createButton = new JActionButton();
        modifyConfirmButton = new JActionButton();
        deleteCancelButton = new JActionButton();
        syncronizeButton = new JActionButton();
        reloadButton = new JActionButton();
        Insets insets = new Insets(2, 2, 2, 2);
        moveBackButton.putClientProperty("hideActionText", Boolean.TRUE);
        moveForwardButton.putClientProperty("hideActionText", Boolean.TRUE);
        moveFirstButton.putClientProperty("hideActionText", Boolean.TRUE);
        moveLastButton.putClientProperty("hideActionText", Boolean.TRUE);
        createButton.putClientProperty("hideActionText", Boolean.TRUE);
        modifyConfirmButton.putClientProperty("hideActionText", Boolean.TRUE);
        deleteCancelButton.putClientProperty("hideActionText", Boolean.TRUE);
        syncronizeButton.putClientProperty("hideActionText", Boolean.TRUE);
        reloadButton.putClientProperty("hideActionText", Boolean.TRUE);
        moveBackButton.setAction(moveBackAction);
        moveForwardButton.setAction(moveForwardAction);
        moveFirstButton.setAction(moveFirstAction);
        moveLastButton.setAction(moveLastAction);
        createButton.setAction(createAction);
        modifyConfirmButton.setActions(new Action[] { modifyAction, confirmAction });
        deleteCancelButton.setActions(new Action[] { deleteAction, cancelAction });
        syncronizeButton.setAction(synchronizeAction);
        reloadButton.setAction(reloadAction);
        moveBackButton.setMargin(insets);
        moveForwardButton.setMargin(insets);
        moveFirstButton.setMargin(insets);
        moveLastButton.setMargin(insets);
        createButton.setMargin(insets);
        modifyConfirmButton.setMargin(insets);
        deleteCancelButton.setMargin(insets);
        syncronizeButton.setMargin(insets);
        reloadButton.setMargin(insets);
        renderPane = new JDataNavigatorDefaultRenderer();
        dataNavigator.addEditorListener(renderPane);
        dataNavigator.getNavigator().addNavigatorListener(renderPane);
        infoPane = new JPanel(new BorderLayout());
        infoPane.add(renderPane, BorderLayout.CENTER);
        add(moveFirstButton);
        add(moveBackButton);
        add(infoPane);
        add(moveForwardButton);
        add(moveLastButton);
        add(createButton);
        add(modifyConfirmButton);
        add(deleteCancelButton);
    }

    /**
     * Enable or disable (always) an editor action.
     * @param actionName The name of the action.
     * @param enabled True to enable the action, false to disable.
     */
    public void setActionEnabled(String actionName, boolean enabled) {
        if (actionName.equals(CreateAction.ACTION_NAME)) {
            createActionEnabled = enabled;
        } else if (actionName.equals(ConfirmAction.ACTION_NAME)) {
            confirmActionEnabled = enabled;
        } else if (actionName.equals(ModifyAction.ACTION_NAME)) {
            modifyActionEnabled = enabled;
        } else if (actionName.equals(CancelAction.ACTION_NAME)) {
            cancelActionEnabled = enabled;
        } else if (actionName.equals(DeleteAction.ACTION_NAME)) {
            deleteActionEnabled = enabled;
        } else if (actionName.equals(SynchronizeAction.ACTION_NAME)) {
            synchronizeActionEnabled = enabled;
        } else if (actionName.equals(ReloadAction.ACTION_NAME)) {
            reloadActionEnabled = enabled;
        } else {
            logger.warn("The action name \"" + actionName + "\" doesn't exist!");
        }
        update();
    }

    /**
     * Enable or disable automatic navigation. Default value is false.
     * @param value True to enable automatic navigation, false otherwise.
     */
    public void setAutoEnabled(boolean value) {
        if (value) {
            moveBackButton.setAction(moveBackMouseAction);
            moveForwardButton.setAction(moveForwardMouseAction);
        } else {
            moveBackButton.setAction(moveBackAction);
            moveForwardButton.setAction(moveForwardAction);
        }
    }

    /**
     * Enable or disable editing features. Default value is true.
     * @param value True to enable automatic navigation, false otherwise.
     */
    public void setEditingEnabled(boolean value) {
        if (value) {
            createButton.setVisible(true);
            modifyConfirmButton.setVisible(true);
            deleteCancelButton.setVisible(true);
        } else {
            createButton.setVisible(false);
            modifyConfirmButton.setVisible(false);
            deleteCancelButton.setVisible(false);
        }
    }

    /** 
     * Set the component to render infomation about the current state of the
     * editor.
     * @param renderer The editor information renderer.
     */
    public void setRenderer(JDataNavigatorRenderer renderer) {
        dataNavigator.removeEditorListener(renderPane);
        dataNavigator.getNavigator().removeNavigatorListener(renderPane);
        renderPane = renderer;
        dataNavigator.addEditorListener(renderPane);
        dataNavigator.getNavigator().addNavigatorListener(renderPane);
    }

    /**
     * Return the editor editor.
     * 
     * @return The editor editor.
     */
    public DataNavigator getDataNavigator() {
        return dataNavigator;
    }

    /** Reset all components based on the controller state. */
    private void update() {
        switch(dataNavigator.getState()) {
            case DataNavigator.EMPTY_STATE:
                {
                    modifyAction.setEnabled(false);
                    deleteAction.setEnabled(false);
                    confirmAction.setEnabled(false);
                    cancelAction.setEnabled(false);
                    createAction.setEnabled(createActionEnabled);
                    synchronizeAction.setEnabled(false);
                    reloadAction.setEnabled(false);
                    moveFirstAction.setEnabled(false);
                    moveBackAction.setEnabled(false);
                    moveForwardAction.setEnabled(false);
                    moveLastAction.setEnabled(false);
                    moveBackMouseAction.setEnabled(false);
                    moveForwardMouseAction.setEnabled(false);
                    break;
                }
            case DataNavigator.DEFAULT_STATE:
                {
                    deleteAction.setEnabled(deleteActionEnabled);
                    modifyAction.setEnabled(modifyActionEnabled);
                    confirmAction.setEnabled(false);
                    cancelAction.setEnabled(false);
                    createAction.setEnabled(createActionEnabled);
                    synchronizeAction.setEnabled(synchronizeActionEnabled);
                    reloadAction.setEnabled(reloadActionEnabled);
                    moveFirstAction.setEnabled(dataNavigator.getNavigator().isMoveFirstEnabled());
                    moveBackAction.setEnabled(dataNavigator.getNavigator().isMoveBackEnabled());
                    moveForwardAction.setEnabled(dataNavigator.getNavigator().isMoveForwardEnabled());
                    moveLastAction.setEnabled(dataNavigator.getNavigator().isMoveLastEnabled());
                    moveBackMouseAction.setEnabled(dataNavigator.getNavigator().isMoveBackEnabled());
                    moveForwardMouseAction.setEnabled(dataNavigator.getNavigator().isMoveForwardEnabled());
                    break;
                }
            case DataNavigator.CREATION_STATE:
                {
                    deleteAction.setEnabled(false);
                    modifyAction.setEnabled(false);
                    confirmAction.setEnabled(confirmActionEnabled);
                    cancelAction.setEnabled(cancelActionEnabled);
                    createAction.setEnabled(false);
                    synchronizeAction.setEnabled(false);
                    reloadAction.setEnabled(false);
                    reloadAction.setEnabled(false);
                    moveFirstAction.setEnabled(false);
                    moveBackAction.setEnabled(false);
                    moveForwardAction.setEnabled(false);
                    moveLastAction.setEnabled(false);
                    moveBackMouseAction.setEnabled(false);
                    moveForwardMouseAction.setEnabled(false);
                    break;
                }
            case DataNavigator.EDITING_STATE:
                {
                    deleteAction.setEnabled(false);
                    modifyAction.setEnabled(false);
                    confirmAction.setEnabled(confirmActionEnabled);
                    cancelAction.setEnabled(cancelActionEnabled);
                    createAction.setEnabled(false);
                    synchronizeAction.setEnabled(false);
                    reloadAction.setEnabled(false);
                    reloadAction.setEnabled(false);
                    moveFirstAction.setEnabled(false);
                    moveBackAction.setEnabled(false);
                    moveForwardAction.setEnabled(false);
                    moveLastAction.setEnabled(false);
                    moveBackMouseAction.setEnabled(false);
                    moveForwardMouseAction.setEnabled(false);
                    break;
                }
        }
    }
}
