package org.opentrust.jsynch;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 *	Registry of Lockable objects keyed by unique name.
 *
 *	<p>The LockableRegistry defines the <i>reference</i> naming form 
 *	(see createFullName) which is used as the default form for all Lockable types.
 *	Specific implementations of Lockable may override this to provide their 
 *	own custom naming form without affecting the naming of the registry.
 *
 *	<p>Sub-classes of LockableRegistry can overide the <i>reference</i> 
 *	implementation as well(see getFullName()). 
 *	The upshot of all this is that how the registry keys objects is transparent 
 *	to the Lockable and Lockable users. 
 *	
 *	<p>Lockable objects can be registered and retrieved using one of the
 *	following forms:
 *		<ul>Register:
 *			<li>Alias		- A name form provided by the <b>registering</b> application
 *			<li>Lockable	- Name form will be generated base upon LockableRegistry</ul>
 *		<ul>Retrieve/Remove:
 *			<li>Alias		- A name form provided by the <b>registering</b> application
 *			<li>Type&Name	- Standard Lockable type (getType()) and name/id</ul>
 *
 *	<p>As was noted during the proceeding discussion the application implementation
 *	can register objects using an Alias (i.e., an arbitrary string). The application
 *	is responsible for managing DuplicateLockableException when aliases collide.
 */
public class LockableRegistry {

    public class AlreadyRegisteredException extends DuplicateLockableException {

        public AlreadyRegisteredException(Lockable l) {
            super("The lockable, " + l.getFullName() + " is already registered");
        }

        public AlreadyRegisteredException(String alias, Lockable l) {
            super("The lockable, " + l.getFullName() + " is already registered as " + alias);
        }
    }

    public class DuplicateLockableException extends Exception {

        public DuplicateLockableException(String msg) {
            super(msg);
        }

        public DuplicateLockableException(Lockable l) {
            super("A lockable with the name, " + l.getFullName() + " already exists");
        }

        public DuplicateLockableException(String alias, Lockable l) {
            super("A lockable (" + l.getFullName() + ") is already registred as " + alias);
        }
    }

    /** Default instance (a.k.a., global name space) for lockable objects */
    protected static LockableRegistry lockableRegistry = null;

    /** The registry where everything is kept
	 *
	 *	<p>Note: This is JDK 1.1 compatible.
	 */
    protected Hashtable registry = new Hashtable(11, .75f);

    /** @return The default instance or "global name space" for lockable objects
	 *
	 *	<p>Note: This is synchronized for thread-safety. Hope you don't mind the
	 *	overhead to acquire the class monitor...
	 */
    public static synchronized LockableRegistry getInstance() {
        if (lockableRegistry == null) {
            lockableRegistry = new LockableRegistry();
        }
        return lockableRegistry;
    }

    /** 
	 *	Creates a "full" name for a Lockable of <i>type</i> with <i>name</i>.
	 *
	 *	<p>This method exists in static form so that it can be a <i>reference</i>
	 *	implementation for other classes.
	 *
	 *	@return The full name for the specified type & name. This form looks
	 *	like: <i>{typename}</i> <b>::</b> <i>{name/id}</i> 
	 */
    public static String createLockableName(String type, String name) {
        return type + "::" + name;
    }

    /** 
	 *	Creates a "full" name for a Lockable of <i>type</i> with <i>name</i>.
	 *
	 *	<p>This method exists in static form so that it can be a <i>reference</i>
	 *	implementation for other classes.
	 *
	 *	@return The full name for the specified type & name. This form looks
	 *	like: <i>{typename}</i> <b>::</b> <i>{name/id}</i> 
	 */
    public static String createLockableName(Lockable l) {
        return l.getType() + "::" + ((l.getName() != null) ? l.getName() : l.getId().toString());
    }

    /** 
	 *	Creates a "full" name for a Lockable of <i>type</i> with <i>name</i>.
	 *
	 *	<p>This method exists as a class method so that a LockableRegistry can
	 *	use its own name form that is not equivalent to the <i>reference</i>
	 *	implementation.
	 *
	 *	<p>The default is to return the name in <i>reference</i> implementation
	 *	form.
	 *
	 *	@return The full name for the specified type & name. This form looks
	 *	like: <i>{typename}</i> <b>::</b> <i>{name/id}</i> 
	 */
    public String getFullName(String type, String nameOrId) {
        return createLockableName(type, nameOrId);
    }

    /** 
	 *	Creates a "full" name for a Lockable.
	 *
	 *	<p>This method exists so that a LockableRegistry can
	 *	use its own name form that is not equivalent to the <i>reference</i>
	 *	implementation or a specifc Lockable implementation.
	 *
	 *	@return The full name for the specified Lockable based on the name
	 *	generation policy of this LockableRegistry
	 */
    public String getFullName(Lockable l) {
        return getFullName(l.getType(), (l.getName() != null) ? l.getName() : l.getId().toString());
    }

    /** Create an instance of a LockableRegistry. */
    public LockableRegistry() {
    }

    /** 
	 *	Look up an object by an alias.
	 *
	 *	@return The registered Lockable corresponding to the alias, or null
	 */
    public synchronized Lockable getLockable(String alias) {
        return (Lockable) registry.get(alias);
    }

    /** 
	 *	Look up an object by type and name.
	 *
	 *	@return The registered Lockable corresponding to the name, or null
	 */
    public synchronized Lockable getLockable(String type, String name) {
        return (Lockable) registry.get(getFullName(type, name));
    }

    /** Determine if a specific Lockable is registred.
	 *
	 *	@return true if the Lockable is registered
	 */
    public synchronized boolean isRegistered(Lockable l) {
        return registry.get(getFullName(l)) != null;
    }

    /** 
	 *	Register a new Lockable object.
	 *
	 *	@param lockable The lockable to register
	 *	@exception DuplicateLockableException if an object with the same
	 *	name and type already exists.
	 *	@exception AlreadyRegisteredException if the same instance is
	 *	already registered
	 */
    public synchronized void registerLockable(Lockable lockable) throws DuplicateLockableException {
        Lockable already = null;
        if ((already = getLockable(getFullName(lockable))) != null) {
            if (already == lockable) {
                throw new AlreadyRegisteredException(lockable);
            } else {
                throw new DuplicateLockableException(lockable);
            }
        }
        registry.put(getFullName(lockable), lockable);
    }

    /** 
	 *	Register a new Lockable object using an Alias.
	 *
	 *	@param lockable The lockable to register
	 *	@exception DuplicateLockableException if an object with the same
	 *	alias already exists.
	 *	@exception AlreadyRegisteredException if the same instance is
	 *	already registered using the specified alias
	 */
    public synchronized void registerLockable(String alias, Lockable lockable) throws DuplicateLockableException {
        Lockable already = null;
        if ((already = getLockable(alias)) != null) {
            if (already == lockable) {
                throw new AlreadyRegisteredException(alias, lockable);
            } else {
                throw new DuplicateLockableException(alias, lockable);
            }
        }
        registry.put(alias, lockable);
    }

    /** 
	 *	Remove the specified lockable from the registry.
	 *
	 *	<p>The instance of the lockable in the registry and
	 *	the lockable provided must refer to the same object.
	 *	
	 *	@return The lockable object removed from the registry.
	 */
    public synchronized Lockable removeLockable(Lockable lockable) {
        Lockable result = null;
        if (lockable == null) {
            throw new IllegalArgumentException("removeLockable: lockable == null");
        }
        if (getLockable(getFullName(lockable)) == lockable) {
            result = (Lockable) registry.remove(getFullName(lockable));
        }
        return result;
    }

    /** 
	 *	Remove an object from the registry by name.
	 *
	 *	@return The lockable object removed from the registry.
	 */
    public synchronized Lockable removeLockable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("removeLockable: name == null");
        }
        return (Lockable) registry.remove(name);
    }

    /** @return an enumeration of each lockable in the registry. */
    public synchronized Enumeration getLockables() {
        return registry.elements();
    }

    /** 
	 *	@return an enumeration of each lockable in the registry 
	 *	of the specified type.
	 */
    public synchronized Enumeration getLockables(String type) {
        Enumeration e = getLockables();
        Vector v = new Vector();
        while (e.hasMoreElements()) {
            Lockable l = (Lockable) e.nextElement();
            if (l.getType().equalsIgnoreCase(type)) {
                v.addElement(l);
            }
        }
        return v.elements();
    }
}
