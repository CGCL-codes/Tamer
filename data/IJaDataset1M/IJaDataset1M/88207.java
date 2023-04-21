package fr.macymed.modulo.framework.module;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import fr.macymed.commons.message.MessageFactory;
import fr.macymed.commons.message.PropertiesProvider;
import fr.macymed.commons.message.ProviderMessages;

/**
 * <p>
 * This class implements permissions to manipulate <code>Service</code>s and {@link fr.macymed.modulo.framework.module.ServiceRegistry}.
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 2.0.3
 * @since Modulo Module Framework 2.0
 */
public class ServicePermission extends Permission {

    /**
     * <p>
     * Static initializer instructions that registers commons messages.
     * </p>
     */
    static {
        final String providerId = "fr.macymed.modulo.framework.module.messages";
        if (MessageFactory.getMessageProvider(providerId) == null) {
            PropertiesProvider provider = new PropertiesProvider(providerId, Module.class.getClassLoader());
            MessageFactory.addMessageProvider(providerId, provider);
        }
    }

    /** The serial version UID. */
    private static final long serialVersionUID = -5462264896936299996L;

    /**
     * <p>
     * This is an enumeration of all the possible action for services.
     * </p>
     * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
     * @version 1.0.0
     * @since Modulo Framework 2.0
     */
    public enum ServiceAction {

        /** The <code>bind</code> action. */
        BIND("bind"), /** The <code>rebind</code> action. */
        REBIND("rebind"), /** The <code>bindPrivate</code> action. */
        BIND_PRIVATE("bindPrivate"), /** The <code>rebindPrivate</code> action. */
        REBIND_PRIVATE("rebindPrivate"), /** The <code>unbind</code> action. */
        UNBIND("unbind"), /** The <code>lookup</code> action. */
        LOOKUP("lookup"), /** The <code>list</code> action. */
        LIST("list");

        /** The name of the action. */
        private String name;

        /**
         * <p>
         * Creates a new ServiceAction.
         * </p>
         * @param _name The name of the action.
         */
        ServiceAction(String _name) {
            this.name = _name;
        }

        /**
         * <p>
         * Returns the name of the action.
         * </p>
         * @return <code>String</code> - The name of the action.
         */
        public String getName() {
            return this.name;
        }

        /**
         * <p>
         * Returns a string representation of this action.
         * </p>
         * @return <code>String</code>- A string representation of this action.
         */
        @Override
        public String toString() {
            return this.getName();
        }
    }

    /** The list of registered actions. */
    Set<ServiceAction> actionsList;

    /**
     * <p>
     * Creates a new ServicePermission.
     * </p>
     * @param _name The name of the service (or a joker).
     * @param _actions A comma-separated string list of actions.
     * @throws NullPointerException If an argument is null.
     * @throws IllegalArgumentException If an argument is no valid.
     */
    public ServicePermission(String _name, String _actions) {
        super(_name);
        if (_name == null) {
            throw new NullPointerException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permission.nullname"));
        }
        if (_name.equals("")) {
            throw new IllegalArgumentException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permission.emptyname"));
        }
        if (_actions == null) {
            throw new NullPointerException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permission.nullaction"));
        }
        if (_actions.equals("")) {
            throw new IllegalArgumentException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permission.emptyaction"));
        }
        StringTokenizer tokens = new StringTokenizer(_actions, ",", false);
        if (tokens.countTokens() < 1) {
            throw new IllegalArgumentException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permission.emptyactions", new Object[] { _actions }));
        }
        List<ServiceAction> lst = new ArrayList<ServiceAction>();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            boolean finded = false;
            for (ServiceAction act : ServiceAction.values()) {
                if (token.equals(act.getName())) {
                    lst.add(act);
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                throw new IllegalArgumentException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permission.notvalidaction", new Object[] { _actions, tokens }));
            }
        }
        Collections.sort(lst);
        this.actionsList = new HashSet<ServiceAction>();
        this.actionsList.addAll(lst);
    }

    /**
     * <p>
     * Checks if this ServicePermission object "implies" the specified permission.
     * </p>
     * <p>
     * Will return false:
     * <ul>
     * <li>
     * If the specified permission is not an instance of ServicePermission, or
     * </li>
     * <li>
     * If the specified permission's name is not implied by this permission's name, or
     * </li>
     * <li>
     * If the specified permission's actions are not a proper subset of this permission's actions.
     * </li>
     * </ul>
     * </p>
     * @param _permission The permission to check against.
     * @return <code>boolean</code> - <code>True</code> if the specified permission implies this permission, <code>false</code> otherwise.
     */
    @Override
    public boolean implies(Permission _permission) {
        if (!(_permission instanceof ServicePermission)) {
            return false;
        }
        ServicePermission permission = (ServicePermission) _permission;
        boolean joker = false;
        String localName = this.getName();
        if (localName.endsWith("*")) {
            joker = true;
            localName = localName.substring(0, localName.length() - 1);
        }
        if (joker) {
            if (!(permission.getName().startsWith(localName))) {
                return false;
            }
        } else {
            if (!(permission.getName().equals(localName))) {
                return false;
            }
        }
        for (ServiceAction permAct : permission.actionsList) {
            if (!(this.actionsList.contains(permAct))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Returns the actions as a String.
     * </p>
     * @return <code>String</code> - The actions of this Permission.
     */
    @Override
    public String getActions() {
        StringBuffer str = new StringBuffer();
        for (ServiceAction act : this.actionsList) {
            str.append(act.getName());
            str.append(',');
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * <p>
     * Returns a hash code value for the object. This method is supported for the benefit of hashtables such as those provided by java.util.Hashtable. It only uses short and long form (as the equals method).
     * </p>
     * @return <code>int</code> - A hash code value for this object.
     */
    @Override
    public final int hashCode() {
        int res = 17;
        res = 37 * res + this.getActions().hashCode();
        res += 17;
        res = 37 * res + this.getName().hashCode();
        res += 17;
        return res;
    }

    /**
     * <p>
     * Indicates whether some other object is "equal to" this one.
     * </p>
     * @param _obj The reference object with which to compare.
     * @return <code>boolean</code> - True if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public final boolean equals(Object _obj) {
        if (_obj == null) {
            return false;
        }
        if (!(_obj instanceof ServicePermission)) {
            return false;
        }
        ServicePermission perm = (ServicePermission) _obj;
        if (!(perm.getName().equals(this.getName()))) {
            return false;
        }
        if (!(perm.getActions().equals(this.getActions()))) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Returns a new PermissionCollection object for storing ServicePermission objects.
     * </p>
     * @return a new PermissionCollection object suitable for storing ServicePermissions.
     */
    @Override
    public PermissionCollection newPermissionCollection() {
        return new ServicePermissionCollection();
    }

    /**
     * <p>
     * A ServicePermissionCollection stores a set of ServicePermission permissions.
     * </p>
     * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
     * @version 1.0.0
     * @since Modulo Framework 2.0
     */
    public static final class ServicePermissionCollection extends PermissionCollection {

        /** The serial version UID. */
        private static final long serialVersionUID = -3475486221251987035L;

        /** The list of permissions. */
        private transient Set<ServicePermission> perms;

        /**
         * <p>
         * Creates a new ServicePermissionCollection. The collection will be empty.
         * </p>
         */
        public ServicePermissionCollection() {
            this.perms = new HashSet<ServicePermission>();
        }

        /**
         * <p>
         * </p>
         * @param _permission The Permission object to add.
         * @throws IllegalArgumentException If the specified permission is not a ServicePermission.
         * @throws SecurityException If this FilePermissionCollection object has been marked readonly.
         */
        @Override
        public void add(Permission _permission) {
            if (!(_permission instanceof ServicePermission)) {
                throw new IllegalArgumentException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permissions.readonly", new Object[] { _permission, "service" }));
            }
            if (isReadOnly()) {
                throw new SecurityException(new ProviderMessages("fr.macymed.modulo.framework.module.messages").getMessage("fr.macymed.modulo.framework.module.Permissions.readonly"));
            }
            synchronized (this) {
                this.perms.add((ServicePermission) _permission);
            }
        }

        /**
         * <p>
         * Returns an enumeration of all the Permission objects in this collection.
         * </p>
         * @return <code>Enumeration<Permission></code> - An enumeration of all the Permission objects.
         */
        @Override
        public Enumeration<Permission> elements() {
            synchronized (this) {
                return enumerate(this.perms);
            }
        }

        /**
         * <p>
         * Checks if this ServicePermissionCollection object "implies" the specified permission.
         * </p>
         * <p>
         * Will return false:
         * <ul>
         * <li>
         * If the specified permission is not an instance of ServicePermission, or
         * </li>
         * <li>
         * If the specified permission's name is not implied by any of this collection's permission, or
         * </li>
         * <li>
         * If the specified permission's actions are not a proper subset of this permission collection's actions.
         * </li>
         * </ul>
         * </p>
         * @param _permission The permission to check against.
         * @return <code>boolean</code> - <code>True</code> if the specified permission implies this permission, <code>false</code> otherwise.
         */
        @Override
        public boolean implies(Permission _permission) {
            if (!(_permission instanceof ServicePermission)) {
                return false;
            }
            ServicePermission permission = (ServicePermission) _permission;
            ServiceAction[] required = permission.actionsList.toArray(new ServiceAction[permission.actionsList.size()]);
            boolean[] finded = new boolean[required.length];
            for (int i = 0; i < finded.length; i++) {
                finded[i] = false;
            }
            for (ServicePermission localPermission : this.perms) {
                boolean joker = false;
                String localName = localPermission.getName();
                if (localName.endsWith("*")) {
                    joker = true;
                    localName = localName.substring(0, localName.length() - 1);
                }
                if (joker) {
                    if (permission.getName().startsWith(localName)) {
                        for (int i = 0; i < required.length; i++) {
                            if (localPermission.actionsList.contains(required[i])) {
                                finded[i] = true;
                            }
                        }
                    }
                } else {
                    if ((permission.getName().equals(localName))) {
                        for (int i = 0; i < required.length; i++) {
                            if (localPermission.actionsList.contains(required[i])) {
                                finded[i] = true;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < finded.length; i++) {
                if (!finded[i]) {
                    return finded[i];
                }
            }
            return true;
        }

        /**
         * <p>
         * Returns a string representation of this permission collection.
         * </p>
         * @return <code>String</code>- A string representation of this permission collection.
         */
        @Override
        public String toString() {
            return this.perms.toString();
        }

        /**
         * <p>
         * Returns a hash code value for the object. This method is supported for the benefit of hashtables such as those provided by java.util.Hashtable. It only uses short and long form (as the equals method).
         * </p>
         * @return <code>int</code> - A hash code value for this object.
         */
        @Override
        public final int hashCode() {
            int res = 17;
            res = 37 * res + this.perms.hashCode();
            res += 17;
            return res;
        }

        /**
         * <p>
         * Indicates whether some other object is "equal to" this one.
         * </p>
         * @param _obj The reference object with which to compare.
         * @return <code>boolean</code> - True if this object is the same as the obj argument; false otherwise.
         */
        @Override
        public final boolean equals(Object _obj) {
            if (_obj == null) {
                return false;
            }
            if (!(_obj instanceof ServicePermissionCollection)) {
                return false;
            }
            ServicePermissionCollection coll = (ServicePermissionCollection) _obj;
            if (coll.perms.size() != this.perms.size()) {
                return false;
            }
            if (!(coll.perms.containsAll(this.perms))) {
                return false;
            }
            if (!(this.perms.containsAll(coll.perms))) {
                return false;
            }
            return true;
        }
    }

    /**
     * <p>
     * Translates a collection of ServicePermission into an enumeration of Permission.
     * </p>
     * @param _coll The collection of ServicePermission.
     * @return <code>Enumeration<Permission></code> - The enumeration of Permission
     */
    static Enumeration<Permission> enumerate(final Collection<ServicePermission> _coll) {
        return new Enumeration<Permission>() {

            Iterator<ServicePermission> i = _coll.iterator();

            public boolean hasMoreElements() {
                return this.i.hasNext();
            }

            public Permission nextElement() {
                return this.i.next();
            }
        };
    }
}
