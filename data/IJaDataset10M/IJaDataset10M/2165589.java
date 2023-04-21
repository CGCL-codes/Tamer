package pl.edu.agh.pp.swingclient;

import java.rmi.RemoteException;
import pl.edu.agh.pp.forum.Forum;
import pl.edu.agh.pp.forum.Message;
import pl.edu.agh.pp.forum.Thread;

public final class ForumConnector {

    private Forum forumServer;

    public ForumConnector() {
    }

    public Thread[] getThreads() {
        try {
            return forumServer.getThreads();
        } catch (RemoteException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public Thread createThread(String username, String title) {
        try {
            return forumServer.createThread(username, title);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message[] getMessages(int threadId) {
        try {
            return forumServer.getMessages(threadId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message createMessage(int threadId, String username, String text) {
        try {
            return forumServer.createMessage(threadId, username, text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean modifyMessage(int messageId, String username, String text) {
        try {
            return forumServer.modifyMessage(messageId, username, text);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setForumServer(Forum forumServer) {
        this.forumServer = forumServer;
    }
}
