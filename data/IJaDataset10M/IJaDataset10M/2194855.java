package com.earnware.data.customermgr;

import com.earnware.xml.XmlSerializer;
import com.earnware.xml.AttributeXmlStrategy;
import com.earnware.data.logging.AbstractLogger;
import com.earnware.soap.customermgr_2_0.*;

public class CustomerMgrFacade extends AbstractLogger {

    private LoginInfoDTO loginInfo = null;

    private UtilSoap proxy = new Util().getUtilSoap();

    public CustomerMgrFacade() {
    }

    public CustomerMgrFacade(LoginInfoDTO loginInfo) {
        this.loginInfo = loginInfo;
    }

    public LoginInfoDTO login(int communityId, String username, String password) throws CustomerMgrException {
        logger.trace("login()");
        try {
            String response = proxy.login(communityId, username, password, false);
            logger.debug(response);
            XmlSerializer serializer = new XmlSerializer(new AttributeXmlStrategy());
            LoginInfoDTO loginInfo = serializer.deserialize(response, LoginInfoDTO.class);
            if (loginInfo.getSessionToken() == "") throw new LoginException("Invalid username and password!");
            return loginInfo;
        } catch (CustomerMgrException ce) {
            logger.info(ce);
            throw ce;
        } catch (Exception e) {
            logger.error(null, e);
            throw new CustomerMgrException(e);
        }
    }

    public AcctMgrDTO getAcctMgr(int acctMgrId) throws CustomerMgrException {
        logger.trace("getAcctMgr(), acctMgrId = " + acctMgrId);
        try {
            String response = proxy.getAcctMgr(loginInfo.getSessionToken(), acctMgrId);
            logger.debug(response);
            XmlSerializer serializer = new XmlSerializer(new AttributeXmlStrategy());
            return serializer.deserialize(response, AcctMgrDTO.class);
        } catch (Exception e) {
            logger.error(null, e);
            throw new CustomerMgrException(e);
        }
    }
}
