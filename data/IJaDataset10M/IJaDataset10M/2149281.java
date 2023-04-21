package com.quikj.ace.web.client.presenter;

import com.google.gwt.user.client.ui.Widget;
import com.quikj.ace.messages.vo.app.Message;
import com.quikj.ace.messages.vo.app.ResponseMessage;
import com.quikj.ace.messages.vo.talk.CallPartyElement;
import com.quikj.ace.messages.vo.talk.ChangePasswordRequestMessage;
import com.quikj.ace.messages.vo.talk.DndRequestMessage;
import com.quikj.ace.web.client.ApplicationController;
import com.quikj.ace.web.client.AudioSettings;
import com.quikj.ace.web.client.ChatSettings;
import com.quikj.ace.web.client.Images;
import com.quikj.ace.web.client.SessionInfo;
import com.quikj.ace.web.client.comm.ResponseListener;
import com.quikj.ace.web.client.comm.CommunicationsFactory;
import com.quikj.ace.web.client.view.UserInfoPanel;
import com.quikj.ace.web.client.view.desktop.DesktopUserInfoPanel;

/**
 * @author beckie
 * 
 */
public class UserInfoPresenter {

    public enum UserInfoEvents {

        CHAT_DND, CHAT_AUTOANSWER, AUDIO_BUZZ, AUDIO_CHAT_NOTIFICATION, AUDIO_NEW_CHAT, AUDIO_PRESENCE_NOTIFICATION
    }

    private UserInfoPanel view;

    private static UserInfoPresenter instance;

    public UserInfoPresenter() {
        instance = this;
    }

    public static UserInfoPresenter getCurrentInstance() {
        return instance;
    }

    public void show() {
        view = createView();
        view.setPresenter(this);
        CallPartyElement userInfo = (CallPartyElement) SessionInfo.getInstance().get(SessionInfo.USER_INFO);
        setViewValues(userInfo);
        UserPanelPresenter.getCurrentInstance().addNewPanel(userInfo.getUserName(), (Widget) view);
        ChatSettings chatSettings = (ChatSettings) SessionInfo.getInstance().get(SessionInfo.CHAT_SETTINGS);
        UserPanelPresenter.getCurrentInstance().setUserStatus(!chatSettings.isDnd());
    }

    private void setViewValues(CallPartyElement userInfo) {
        AudioSettings audioSettings = (AudioSettings) SessionInfo.getInstance().get(SessionInfo.AUDIO_SETTINGS);
        if (audioSettings == null) {
            audioSettings = new AudioSettings();
            SessionInfo.getInstance().put(SessionInfo.AUDIO_SETTINGS, audioSettings);
        }
        ChatSettings chatSettings = (ChatSettings) SessionInfo.getInstance().get(SessionInfo.CHAT_SETTINGS);
        if (chatSettings == null) {
            chatSettings = new ChatSettings();
            SessionInfo.getInstance().put(SessionInfo.CHAT_SETTINGS, chatSettings);
        }
        view.setValues(userInfo, audioSettings, chatSettings);
    }

    public void dispose() {
        instance = null;
    }

    public void logOut() {
        LogoutPresenter.getCurrentInstance().processLogout();
    }

    private UserInfoPanel createView() {
        return new DesktopUserInfoPanel();
    }

    public void processPasswordChange(String oldPasswd, String newPasswd, String verifyPasswd) {
        if (newPasswd.length() == 0) {
            MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_invalidEntry(), ApplicationController.getMessages().UserInfoPresenter_passwordRequired(), Images.WARNING_MEDIUM, true);
            return;
        }
        if (!newPasswd.equals(verifyPasswd)) {
            MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_invalidEntry(), ApplicationController.getMessages().UserInfoPresenter_passwordMismatch(), Images.WARNING_MEDIUM, true);
            return;
        }
        CallPartyElement userInfo = (CallPartyElement) SessionInfo.getInstance().get(SessionInfo.USER_INFO);
        ChangePasswordRequestMessage req = new ChangePasswordRequestMessage();
        req.setUserName(userInfo.getUserName());
        req.setNewPassword(newPasswd);
        req.setOldPassword(oldPasswd);
        MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_changePassword(), ApplicationController.getMessages().UserInfoPresenter_changingPassword() + " ...", Images.LOGIN_MEDIUM, false);
        CommunicationsFactory.getServerCommunications().sendRequest(req, Message.CONTENT_TYPE_XML, false, 100000L, new ResponseListener() {

            @Override
            public void timeoutOccured(int requestId) {
                MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_changePassword(), ApplicationController.getMessages().UserInfoPresenter_noResponseFromServer(), Images.DISCONNECTED_MEDIUM, true);
            }

            @Override
            public void responseReceived(int requestId, String contentType, ResponseMessage message) {
                if (message.getStatus() == ResponseMessage.OK) {
                    MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_changePassword(), ApplicationController.getMessages().UserInfoPresenter_passwordChanged(), (String) Images.INFO_MEDIUM, true);
                    view.reset();
                } else {
                    MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_changePassword(), ApplicationController.getMessages().UserInfoPresenter_passwordFailed() + ": " + message.getReason() + " (" + message.getStatus() + ")", (String) Images.WARNING_MEDIUM, true);
                }
            }
        });
    }

    public void processEvent(UserInfoEvents event, boolean value) {
        AudioSettings audioSettings = (AudioSettings) SessionInfo.getInstance().get(SessionInfo.AUDIO_SETTINGS);
        ChatSettings chatSettings = (ChatSettings) SessionInfo.getInstance().get(SessionInfo.CHAT_SETTINGS);
        if (event == UserInfoEvents.AUDIO_BUZZ) {
            audioSettings.setBuzz(value);
        } else if (event == UserInfoEvents.AUDIO_CHAT_NOTIFICATION) {
            audioSettings.setChime(value);
        } else if (event == UserInfoEvents.AUDIO_NEW_CHAT) {
            audioSettings.setRinging(value);
        } else if (event == UserInfoEvents.AUDIO_PRESENCE_NOTIFICATION) {
            audioSettings.setPresence(value);
        } else if (event == UserInfoEvents.CHAT_AUTOANSWER) {
            chatSettings.setAutoAnswer(value);
        } else if (event == UserInfoEvents.CHAT_DND) {
            chatSettings.setDnd(value);
            UserPanelPresenter.getCurrentInstance().setUserStatus(!value);
            DndRequestMessage req = new DndRequestMessage();
            req.setEnable(value);
            CommunicationsFactory.getServerCommunications().sendRequest(req, Message.CONTENT_TYPE_XML, false, 100000L, new ResponseListener() {

                @Override
                public void timeoutOccured(int requestId) {
                    MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_modifyDnd(), ApplicationController.getMessages().UserInfoPresenter_noResponseFromServer(), Images.DISCONNECTED_MEDIUM, true);
                }

                @Override
                public void responseReceived(int requestId, String contentType, ResponseMessage message) {
                    if (message.getStatus() != ResponseMessage.OK) {
                        MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().UserInfoPresenter_modifyDnd(), ApplicationController.getMessages().UserInfoPresenter_dndFailed() + ": " + message.getReason() + " (" + message.getStatus() + ")", (String) Images.WARNING_MEDIUM, true);
                    }
                }
            });
        }
    }
}
