package org.bungeni.editor.actions;

import org.apache.log4j.Logger;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.actions.routers.routerFactory;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.error.ErrorMessages;
import org.bungeni.extutils.MessageBox;
import org.bungeni.ooo.OOComponentHelper;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Ashok Hariharan
 */
public class EditorSelectionActionHandler implements IEditorActionEvent {

    private static org.apache.log4j.Logger log = Logger.getLogger(EditorSelectionActionHandler.class.getName());

    private ErrorMessages errorMsgObj = new ErrorMessages();

    private toolbarAction m_subAction;

    private OOComponentHelper ooDocument;

    private JFrame parentFrame;

    /** Creates a new instance of EditorSelectionActionHandler */
    public EditorSelectionActionHandler() {
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarAction action, JFrame c) {
        this.ooDocument = ooDocument;
        this.parentFrame = c;
        this.m_subAction = action;
        int nValid = -1;
        BungeniValidatorState validObj = _validateAction();
        if (validObj.state) {
            BungeniValidatorState routeObj = _routeAction();
        } else {
            String msg = validObj.msg.getMessageString();
            MessageBox.OK(null, msg);
        }
    }

    /**
     * The action routing architecture allows adding pre-validation check before routing the action.
     * Pre-validation checks can be added per action-subAction combination and allow runtime aborting of user generated actions.
     * e.g. if a user action is invalid because of some metadata in the doucment or some specific state of the document, a validation action
     * allows a pre-check of the user action.
     * By default the recommended validator to use for an action is the defaultValidator which always to 'true'
     * (Refer to the settings table sub_action_settings )
     * @return
     */
    private BungeniValidatorState _validateAction() {
        org.bungeni.editor.actions.validators.IBungeniActionValidator validatorObject = null;
        validatorObject = org.bungeni.editor.actions.validators.validatorFactory.getValidatorClass(m_subAction);
        BungeniValidatorState validState = validatorObject.check(this.m_subAction, this.ooDocument);
        return validState;
    }

    /**
     * Routes the action by instantiating the object to route to using the IBungeniActionRouter interface
     * @return
     */
    private BungeniValidatorState _routeAction() {
        log.debug("_routeAction : calling _routeAction()");
        org.bungeni.editor.actions.routers.IBungeniActionRouter routerObject = null;
        routerObject = routerFactory.getRouterClass(m_subAction);
        BungeniValidatorState validState = routerObject.route(m_subAction, parentFrame, ooDocument);
        return validState;
    }

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, JFrame parentFrame) {
    }
}
