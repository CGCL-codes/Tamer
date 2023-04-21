package pl.edu.agh.pp.jspclient.admin;

import pl.edu.agh.pp.forum.Message;
import pl.edu.agh.pp.forum.Thread;
import java.io.Serializable;

public interface Connector extends Serializable {

    public Thread[] getThreads();

    public Thread createThread(String username, String title);

    public Message[] getMessages(int threadId);

    public Message createMessage(int threadId, String username, String text);

    public boolean modifyMessage(int messageId, String username, String text);

    public Message getMessage(int id);

    public Thread getThread(int id);
}
