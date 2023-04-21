package com.arsenal.session.client;

import javax.swing.*;
import java.util.Vector;
import com.arsenal.session.message.*;
import com.arsenal.log.Log;
import com.arsenal.observer.IHandler;
import com.arsenal.message.*;
import com.arsenal.session.*;
import com.arsenal.client.Client;
import com.arsenal.client.ConnectionWindow;
import com.arsenal.client.observer.*;
import com.arsenal.session.client.panels.*;
import com.arsenal.user.client.*;

public class SessionClientHandler implements IHandler, LogoutObserver, LoginObserver, NewSessionObserver, RemoveSessionObserver, MyUserInfoObserver {

    private SessionMenu sessionMenu = new SessionMenu();

    private static SessionClientHandler instance = new SessionClientHandler();

    public static SessionClientHandler getInstance() {
        if (instance == null) {
            instance = new SessionClientHandler();
        }
        return instance;
    }

    public SessionClientHandler() {
        this.instance = this;
    }

    public void init() {
        Log.info(this, "SessionClientHandler initing....");
        sessionMenu.setEnabled(false);
        Client.getInstance().addToMenuBar(sessionMenu);
        CreateEditSessionWindow.getInstance();
        Client.getInstance().addToSharedLeftSidePanel(SessionPanel.getInstance());
        registerNewSessionListener(this);
        registerRemoveSessionListener(this);
        registerLoginListener(this);
        registerLogoutListener(this);
        registerMyUserInfoListener(this);
        SessionPanel.getInstance().setRendererForTree();
    }

    public void handleMessage(IMessage message) {
        message.execute();
    }

    public void sendMessage(IMessage message) {
        Client.getInstance().sendMessage(message);
    }

    public synchronized void addNewSessions(Vector newSessions) {
        SessionList.getInstance().addNewSessions(newSessions);
    }

    public void sendInitializationMessages() {
    }

    private void sendGetAllSessionsMessage() {
        GetAllSessionsMessage message = new GetAllSessionsMessage();
        message.setHandlerName("session");
        message.setPayload(ConnectionWindow.getInstance().getUsername());
        sendMessage(message);
    }

    private void sendAllUsersInAllSessionMessage() {
    }

    public void addUserToSession(SessionBean bean) {
        SessionPanel.getInstance().addUserToSession(bean);
    }

    public void removeUserFromSession(SessionBean bean) {
        SessionPanel.getInstance().removeUserFromSession(bean);
    }

    /*********************************************************************
   *
   * action event handlers
   *
   *********************************************************************/
    public void doNewSessionAction(Object object) {
    }

    public void registerNewSessionListener(NewSessionObserver newSessionObserver) {
        Client.getInstance().registerNewSessionObserver(newSessionObserver);
    }

    public void doRemoveSessionAction(Object object) {
    }

    public void registerRemoveSessionListener(RemoveSessionObserver removeSessionObserver) {
        Client.getInstance().registerRemoveSessionObserver(removeSessionObserver);
    }

    public void doLoginAction() {
        Log.debug(this, "doLoginAction() - send init message");
        sendGetAllSessionsMessage();
        sessionMenu.setEnabled(true);
    }

    public void registerLoginListener(LoginObserver loginObserver) {
        Client.getInstance().registerLoginObserver(loginObserver);
    }

    public void doLogoutAction() {
        sessionMenu.setEnabled(false);
    }

    public void registerLogoutListener(LogoutObserver logoutObserver) {
        Client.getInstance().registerLogoutObserver(logoutObserver);
    }

    protected void informSessionAdded(SessionBean bean) {
        Client.getInstance().informNewSessionEvent(bean);
    }

    protected void informSessionRemoved(SessionBean bean) {
        Client.getInstance().informRemoveSessionEvent(bean);
    }

    public void doMyUserInfoAction(Object object) {
        if (!UserClientHandler.getInstance().isGuest()) sessionMenu.setEnabled(true); else sessionMenu.setEnabled(false);
    }

    public void registerMyUserInfoListener(MyUserInfoObserver myUserInfoObserver) {
        UserClientHandler.getInstance().registerMyUserInfoObserver(this);
    }
}
