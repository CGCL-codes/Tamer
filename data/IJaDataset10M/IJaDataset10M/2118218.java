package com.earnware.data.messagesvr;

import java.util.List;
import java.util.Arrays;
import javax.xml.ws.soap.MTOMFeature;
import com.earnware.data.logging.AbstractLogger;
import com.earnware.data.customermgr.*;
import com.earnware.soap.messagesvr_2_0.*;
import com.earnware.xml.XmlSerializer;

public class MessageSvrFacade extends AbstractLogger {

    private LoginInfoDTO loginInfo;

    private UtilSoap proxy = new Util().getUtilSoap(new MTOMFeature());

    public MessageSvrFacade(LoginInfoDTO loginInfo) {
        this.loginInfo = loginInfo;
    }

    public MessageDTO[] getPop3Messages() throws MessageSvrException {
        logger.trace("getPop3Messages(), acctMgrId = " + loginInfo.getAcctMgrId());
        try {
            String response = proxy.getPop3Messages(loginInfo.getSessionToken(), loginInfo.getAcctMgrId());
            logger.debug(response);
            XmlSerializer serializer = new XmlSerializer(new MessageXmlStrategy());
            List<MessageDTO> messages = serializer.deserialize(response, "//message", MessageDTO.class);
            return messages.toArray(new MessageDTO[0]);
        } catch (Exception e) {
            logger.error(null, e);
            throw new MessageSvrException(e);
        }
    }

    public void deletePop3Messages(MessageKey[] messages) throws MessageSvrException {
        logger.trace("deletePop3Messages(), acctMgrId = " + loginInfo.getAcctMgrId() + ", messages.length = " + messages.length);
        try {
            XmlSerializer serializer = new XmlSerializer(new MessageXmlStrategy());
            String xml = serializer.serialize(Arrays.asList(messages), "messages");
            logger.debug(xml);
            proxy.deletePop3Messages(loginInfo.getSessionToken(), loginInfo.getAcctMgrId(), xml);
        } catch (Exception e) {
            logger.error(null, e);
            throw new MessageSvrException(e);
        }
    }

    public String getMimeMessage(MessageKey message) throws MessageSvrException {
        logger.trace("getMimeMessage(), messageId = " + message.getMessageId());
        try {
            return proxy.getMimeMessage(loginInfo.getSessionToken(), message.getAccountId(), message.getMessageId());
        } catch (Exception e) {
            logger.trace(null, e);
            throw new MessageSvrException(e);
        }
    }

    public byte[] getMimeMessageBytes(MessageKey message) throws MessageSvrException {
        logger.trace("getMimeMessageBytes(), messageId = " + message.getMessageId());
        try {
            return proxy.getMimeMessageBytes(loginInfo.getSessionToken(), message.getAccountId(), message.getMessageId());
        } catch (Exception e) {
            logger.trace(null, e);
            throw new MessageSvrException(e);
        }
    }
}
