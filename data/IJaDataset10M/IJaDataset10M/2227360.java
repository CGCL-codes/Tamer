package org.xsocket.mail.pop3;

import org.xsocket.mail.pop3.spi.IPOP3AuthenticatorService;
import org.xsocket.mail.pop3.spi.IPOP3Maildrop;
import org.xsocket.mail.pop3.spi.IPOP3MaildropService;

/**
 * command context  
 * 
 * @author grro@xsocket.org
 */
interface ICommandContext {

    enum State {

        AUTHORIZATION, TRANSACTION
    }

    public State getState();

    public void setState(State state);

    public IPOP3MaildropService getMaildropService();

    public void clear();

    public String getUser();

    public void setUser(String user);

    public IPOP3AuthenticatorService getAuthenticator();

    public void setMaildrop(IPOP3Maildrop maildrop);

    public IPOP3Maildrop getMaildrop();

    public boolean addMessageNumberToDelete(Integer msgNumber);

    public boolean isMessageNumberMarkedAsDeleted(Integer msgNumber);

    public void clearMessageNumbersToDelete();

    public Integer[] getMessageNumbersToDelete();

    public String getMessageUid(Integer msgNumber);

    public Integer[] mapUidToMessageNumber(String[] uids);
}
