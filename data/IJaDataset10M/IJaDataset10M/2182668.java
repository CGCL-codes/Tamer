package fb4java.beans;

/**
 * 
 * fb4java<br />
 * fb4java.beans
 * 
 * This is not a Thread class just like java.lang.Thread. It's wrapper bean for
 * a thread in Facebook Message.
 * 
 * @author Choongsan Ro
 * @version 1.0 2010. 5. 13.
 */
public class Thread {

    public String subject;

    public long senderUserid;

    public long recipientUserid;

    public String message;

    @Override
    public String toString() {
        return "\nSubject : " + subject + "\nSender : " + senderUserid + "\nRecipient : " + recipientUserid + "\nMessage : \n" + message;
    }
}
