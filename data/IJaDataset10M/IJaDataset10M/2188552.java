package gvs.repository.server.security.p2p.security.jaas.authorization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.BasicPermission;
import java.security.Permission;
import java.util.StringTokenizer;

/**
 * 
 */
public class CommandPermission extends BasicPermission {

    /**
     * The serial Version UID
     */
    private static final long serialVersionUID = -6765839986380959053L;

    protected int mask;

    /**
     * No actions
     */
    private static final int NONE = 0x00;

    /**
     * Add action
     */
    private static final int ADD = 0x01;

    /**
     * Commit action
     */
    private static final int COMMIT = 0x02;

    /**
     * Update action
     */
    private static final int UPDATE = 0x04;

    /**
     * Checkout action
     */
    private static final int CHECKOUT = 0x08;

    /**
     * All actions
     */
    private static final int ALL = ADD | COMMIT | UPDATE | CHECKOUT;

    /**
     * The actions string
     * 
     * @serial
     */
    private String actions;

    /**
     * Construct a CommandPermission object. Defaults to 'checkout' action
     * 
     * @param name The resource to access
     */
    public CommandPermission(String name) {
        this(name, "checkout");
    }

    /**
     * Construct a CommandPermission
     * 
     * @param name The resource to access
     * @param actions The set of actions permissible
     */
    public CommandPermission(String name, String actions) {
        super(name);
        init(getMask(actions));
    }

    /**
     * Ensures valid mask into permission
     * 
     * @param mask the actions mask to use
     * @throws IllegalArgumentException if actions mask invalid
     */
    private void init(int mask) {
        if ((mask & ALL) != mask) {
            throw new IllegalArgumentException("Invalid actions mask");
        }
        this.mask = mask;
    }

    /**
     * Checks if this CommandPermission object implies the specified permission.
     * It must be an instance of CommandPermission, names equal or implied by
     * the object's name (a.b.* implies a.b.c) and the actions are a proper
     * subset.
     * 
     * @param p the permission to check against
     * 
     * @return true if the specified permission is equal or implied by this
     *         object, false otherwise.
     */
    public boolean implies(Permission p) {
        if (!super.implies(p)) {
            return false;
        } else {
            CommandPermission that = (CommandPermission) p;
            return ((this.mask & that.mask) == that.mask);
        }
    }

    /**
     * Checks two CommandPermission objects for equality. Checks that other
     * object is a CommandPermission and has same name and set of actions.
     * 
     * @param obj the object we are testing against
     * @return true if obj is a CommandPermission and has same name and actions
     *         as this CommandPermission object, false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CommandPermission)) {
            return false;
        }
        CommandPermission that = (CommandPermission) obj;
        return ((this.mask == that.mask) && (this.getName().equals(that.getName())));
    }

    /**
     * Returns the hash code value for this object.
     * 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.getName().hashCode() + this.mask;
    }

    /**
     * Converts an actions String to an actions mask
     * 
     * @param action the action string
     * @return the actions mask
     * @throws IllegualArgumentException if invalid action in actions String
     */
    private static int getMask(String actions) {
        int mask = NONE;
        if (actions == null) {
            return mask;
        }
        actions = actions.toLowerCase();
        StringTokenizer st = new StringTokenizer(actions, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if ("add".equals(token)) {
                mask |= ADD;
            } else if ("commit".equals(token)) {
                mask |= COMMIT;
            } else if ("update".equals(token)) {
                mask |= UPDATE;
            } else if ("checkout".equals(token)) {
                mask |= CHECKOUT;
            } else {
                throw new IllegalArgumentException("Invalid permission: " + actions);
            }
        }
        return mask;
    }

    /**
     * Returns the canonical string representation of the actions. The available
     * actions are always returned in the following order: read, write
     * 
     * @return the action string
     */
    public String getActions() {
        if (actions == null) {
            actions = getActions(mask);
        }
        return actions;
    }

    private static String getActions(int mask) {
        StringBuffer sb = new StringBuffer();
        boolean comma = false;
        if ((mask & ADD) == ADD) {
            comma = true;
            sb.append("add");
        }
        if ((mask & COMMIT) == COMMIT) {
            if (comma) {
                sb.append(',');
            } else {
                comma = true;
            }
            sb.append("commit");
        }
        if ((mask & UPDATE) == UPDATE) {
            if (comma) {
                sb.append(',');
            } else {
                comma = true;
            }
            sb.append("update");
        }
        if ((mask & CHECKOUT) == CHECKOUT) {
            if (comma) {
                sb.append(',');
            } else {
                comma = true;
            }
            sb.append("checkout");
        }
        return sb.toString();
    }

    /**
     * Saves the state of the permission to a stream. The actions are
     * serialized, and the superclass takes care of the name.
     * 
     * @param s the ObjectOutputStream to write to
     * 
     * @throws IOException if I/O errors occur while writing to the underlying
     *                 OutputStream
     */
    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        if (actions == null) {
            getActions();
        }
        s.defaultWriteObject();
    }

    /**
     * Restores the state of the permission from a stream.
     * 
     * @param s the ObjectInputStream to read from
     * 
     * @throws IOException if I/O errors occur while reading from the underlying
     *                 InputStream
     * @throws ClassNotFoundException if the class of a serialized object could
     *                 not be found
     */
    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        init(getMask(actions));
    }

    /**
     * Generates string representation of class information.
     * 
     * @return String representation of state
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append("[");
        sb.append(getName());
        sb.append(",");
        sb.append(getActions());
        sb.append("]");
        return sb.toString();
    }
}
