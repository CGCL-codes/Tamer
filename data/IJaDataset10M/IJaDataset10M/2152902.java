package es.ehrflex.client.main.system.controller;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.google.gwt.user.client.Cookies;
import es.ehrflex.client.main.event.EHRflexEvent;
import es.ehrflex.client.main.event.EHRflexEventType;
import es.ehrflex.client.main.message.EHRflexMessages;
import es.ehrflex.client.main.rpc.EHRflexAsyncCallback;
import es.ehrflex.client.main.system.view.ManagementCenterView;
import es.ehrflex.client.main.system.window.MessageDialog;
import es.ehrflex.client.main.user.model.UserDataModel;
import es.ehrflex.client.service.EHRflexSecuredService;
import es.ehrflex.client.service.GWTServiceFactory;
import es.ehrflex.client.service.UserGWTServiceAsync;

/**
 * Object that takes and handles the general requests
 * 
 * @author Anton Brass
 * @version 1.0, 12.05.2009
 */
public class ManagementCenterController extends Controller {

    private EHRflexMessages messages = GWTServiceFactory.getEHRflexMessages();

    private static ManagementCenterView managementCenterView;

    private UserGWTServiceAsync userService;

    String sharedSecret;

    public ManagementCenterController() {
        registerEventTypes(EHRflexEventType.START_LAYOUT_VIEW.getEventType());
        registerEventTypes(EHRflexEventType.USER_SAVE.getEventType());
        registerEventTypes(EHRflexEventType.USER_NEW_SUBMIT.getEventType());
        registerEventTypes(EHRflexEventType.USER_EDIT_SUBMIT.getEventType());
    }

    /**
	 * Callback for logout
	 */
    private EHRflexAsyncCallback<Void> userSaveCallback = new EHRflexAsyncCallback<Void>() {

        @Override
        public void onSuccess(Void result) {
            forwardToView(managementCenterView, new EHRflexEvent(EHRflexEventType.USER_SAVE_SUCCESSFULL));
            new MessageDialog(messages.ehrflex_user_submit_successful());
        }
    };

    /**
	 * Registration of the different event types that are supported by this controller:
	 * 
	 * EHRflexEventType.START_LAYOUT_VIEW <br>
	 */
    @Override
    public void handleEvent(AppEvent pEvent) {
        EHRflexEvent event = (EHRflexEvent) pEvent;
        switch(event.mEventType) {
            case START_LAYOUT_VIEW:
                onStartCenterView(event);
                break;
            case USER_SAVE:
                onUserSave(event);
                break;
            case USER_NEW_SUBMIT:
                onUserNewSubmit(event);
                break;
            case USER_EDIT_SUBMIT:
                onUserEditSubmit(event);
                break;
        }
    }

    /**
	 * Forward to View
	 * 
	 * @param pEvent
	 *            START_LAYOUT_VIEW event
	 */
    private void onStartCenterView(EHRflexEvent pEvent) {
        forwardToView(managementCenterView, pEvent);
    }

    /**
	 * Forward to View
	 * 
	 * @param pEvent
	 *            START_LAYOUT_VIEW event
	 */
    private void onUserSave(EHRflexEvent pEvent) {
        forwardToView(managementCenterView, pEvent);
    }

    /**
	 * Sends request to save the user
	 * 
	 * @param pEvent
	 *            USER_NEW_SUBMIT event
	 */
    private void onUserNewSubmit(EHRflexEvent pEvent) {
        sharedSecret = Cookies.getCookie(EHRflexSecuredService.SHARED_SECRET_KEY);
        this.userService.createUser((UserDataModel) pEvent.getData(), sharedSecret, userSaveCallback);
    }

    /**
	 * Sends request to save the user
	 * 
	 * @param pEvent
	 *            USER_EDIT_SUBMIT event
	 */
    private void onUserEditSubmit(EHRflexEvent pEvent) {
    }

    /**
	 * Setting some connections with other objects
	 */
    @Override
    protected void initialize() {
        this.managementCenterView = new ManagementCenterView(this);
        this.userService = GWTServiceFactory.getUserGWTService();
        sharedSecret = Cookies.getCookie(EHRflexSecuredService.SHARED_SECRET_KEY);
    }
}
