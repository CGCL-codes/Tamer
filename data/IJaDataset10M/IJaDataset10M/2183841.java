package com.expantion.metier.impl;

import java.util.Locale;
import org.apache.log4j.Logger;
import com.expantion.dao.ConnectionDao;
import com.expantion.dao.ServerDaoFactoryImpl;
import com.expantion.dao.data.ServerMessage;
import com.expantion.dao.data.ServerMessageType;
import com.expantion.metier.ConnectionManager;
import com.expantion.metier.data.ConnectionStatus;
import com.expantion.model.LoginPanelData;

public class ConnectionManagerImpl implements ConnectionManager {

    private static final Logger logger = Logger.getLogger(ConnectionManager.class);

    public ConnectionManagerImpl() {
    }

    @Override
    public ConnectionStatus connect(String username, String password) {
        logger.debug("connect : " + username);
        ConnectionStatus result = ConnectionStatus.NOT_CONNECTED;
        ConnectionDao connecteur = ServerDaoFactoryImpl.getInstance().getConnectionDao();
        if (!connecteur.connect(username, password)) {
            logger.debug("echec de connection");
            if (connecteur.isUsernameValid(username)) {
                if (!connecteur.isPasswordValid(username, password)) {
                    logger.debug("mot de passe incorrect");
                    result = ConnectionStatus.PWD_ERROR;
                }
            } else {
                logger.debug("utilisateur inconnu");
                result = ConnectionStatus.LOGIN_ERROR;
            }
        } else {
            result = ConnectionStatus.CONNECTED;
        }
        return result;
    }

    @Override
    public ConnectionStatus disconnect(String username) {
        return ConnectionStatus.NOT_CONNECTED;
    }

    @Override
    public LoginPanelData getLoginMessage(Locale pLangue) {
        logger.debug("getLoginMessage : " + pLangue.getLanguage());
        LoginPanelData result = new LoginPanelData();
        ServerMessage message = ServerDaoFactoryImpl.getInstance().getMessageDao().getServerMessage(ServerMessageType.MOTD, pLangue);
        result.setBanniereUrl(message.getUrl());
        result.setMessage(message.getMessage());
        return result;
    }

    @Override
    public LoginPanelData getLoginMessageByStatus(ConnectionStatus pStatus, Locale pLangue) {
        logger.debug("getLoginMessageByStatus : " + pLangue.getLanguage());
        LoginPanelData result = new LoginPanelData();
        ServerMessage message = ServerDaoFactoryImpl.getInstance().getMessageDao().getServerMessage(ServerMessageType.getMessageType(pStatus), pLangue);
        result.setBanniereUrl(message.getUrl());
        result.setMessage(message.getMessage());
        return result;
    }
}
