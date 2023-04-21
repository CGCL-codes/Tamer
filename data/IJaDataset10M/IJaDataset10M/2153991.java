package net.simapro.connector.server;

/**
 * Container for a server message.
 * 
 * @author Michael Srocka
 *
 */
public class Message {

    private String title;

    private String content;

    public Message(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
