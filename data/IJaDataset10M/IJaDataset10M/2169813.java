package helen.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author brobert
 */
public class PhysicalObject extends Subject {

    Location location;

    private String name;

    private String displayName;

    private List<Verb> verbs = new ArrayList<Verb>();

    private String description;

    private String owner;

    private int id;

    public static String NO_DESCRIPTION = "You see nothing special here.";

    public String getDescription() {
        return (description == null) ? NO_DESCRIPTION : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private PhysicalObject() {
        Vault.store(this);
        allowAll();
        setFailureMessage(null, "That is not allowed here.");
    }

    public PhysicalObject(String name) {
        this();
        this.setName(name);
    }

    public void setLocation(Location l) {
        if (location != null) {
            location.removeObject(this);
        }
        location = l;
        l.addObject(this);
    }

    public Location getLocation() {
        return location;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        Location l = getLocation();
        if (l != null) {
            l.removeObject(this);
        }
        Vault.remove(this);
        this.name = name;
        Vault.store(this);
        if (l != null) {
            l.addObject(this);
        }
    }

    public List<Verb> getVerbs() {
        return verbs;
    }

    public boolean processCommand(User user, String command) throws Exception {
        for (Verb v : verbs) {
            if (v.processCommand(user, command)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription(User user) {
        return this.toString();
    }

    public void describe(User user) {
        if (user.session != null) {
            Message m = new Message(user.getName(), getDescription(user));
            m.setSecondPersonMessage(null);
            m.setThirdPersonMessage(null);
            user.session.messageQueue.offer(m);
            m.announce();
        }
    }

    @Override
    public String toString() {
        return getName() + ":\n" + getDescription();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
