package jmodnews.modules.midfinder;

import jmodnews.logging.ExceptionHandler;

/**
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class ArticleHolder {

    private final String messageID;

    private int users = 0;

    private String article;

    private boolean locked = false;

    /**
	 * @param messageID
	 * 
	 */
    public ArticleHolder(String messageID) {
        super();
        this.messageID = messageID;
        article = null;
    }

    public synchronized void incrementUsers() {
        users++;
    }

    public synchronized boolean lock() {
        try {
            while (locked) wait();
        } catch (InterruptedException ex) {
            ExceptionHandler.handle(ex);
        }
        if (users == 0) throw new IllegalStateException();
        users--;
        locked = true;
        return article == null;
    }

    public synchronized void unlock(String newArticle) {
        if (!locked) throw new IllegalStateException();
        if (article == null) article = newArticle;
        locked = false;
        notifyAll();
    }

    public synchronized void waitForAllTasks() {
        try {
            while (locked || users > 0) wait();
        } catch (InterruptedException ex) {
            ExceptionHandler.handle(ex);
        }
    }

    public synchronized String getArticle() {
        if (locked || users > 0) throw new IllegalStateException();
        return article;
    }

    public synchronized String getMessageID() {
        return messageID;
    }
}
