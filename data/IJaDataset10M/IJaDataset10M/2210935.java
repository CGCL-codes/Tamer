package black.sheep.wall.uam;

/**
 * basic implementation of IAlert
 * @author hni
 *
 */
public class BasicAlert implements IAlert {

    private UUID id;

    private Object content;

    private boolean unread;

    private String description;

    public BasicAlert(String description, Object content) {
        id = new UUID();
        this.content = content;
        this.description = description;
        unread = true;
    }

    public Object getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
    }

    public boolean isUnread() {
        return unread;
    }

    public void read() {
        unread = false;
    }
}
