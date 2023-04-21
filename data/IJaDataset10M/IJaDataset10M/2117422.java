package org.dinopolis.timmon.frontend.dummy;

import org.dinopolis.timmon.node.LogEntry;
import org.dinopolis.timmon.node.TaskNode;
import org.dinopolis.timmon.TimmonId;
import org.dinopolis.timmon.TimmonBackEnd;
import org.dinopolis.timmon.TimmonBackEndException;
import org.dinopolis.timmon.node.PropertyException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * @author Christof Dallermassl
 * @version $Revision: 1.2 $
 */
public class DummyLogEntry extends LogEntry {

    /** the backend to communicate with */
    protected TimmonBackEnd backend_ = null;

    /** the timmon-id of this node */
    protected TimmonId id_ = null;

    /** has the backend already been asked for the properties of this node? */
    protected boolean properties_requested_ = false;

    /**
 * Default constructor.
 *
 */
    public DummyLogEntry() {
        super();
    }

    /**
 * Sets the backend to communicate with.
 *
 * @param backend the backend to communicate with.
 */
    protected void setBackEnd(TimmonBackEnd backend) {
        backend_ = backend;
    }

    /**
 * Sets the timmon id of the task node.
 *
 * @param id the TimmonId from the backend.
 */
    protected void setTimmonId(TimmonId id) {
        id_ = id;
    }

    /**
 * Returns the timmon id of the task node.
 *
 * @return the timmon id of the task node.
 */
    protected TimmonId getTimmonId() {
        return (id_);
    }

    /**
 * This method changes the current parent_node to the given
 * TaskNode. No listeners are informed!
 *
 * @param parent_node the new parent 
 */
    protected void setParentNode(TaskNode parent_node) {
        super.setParentNode(parent_node);
    }

    /**
 * Changes the property with given key. If there exists a property
 * with the given key, the new value replaces the old one. If the
 * value is set to <code>null</code> the property is deleted. If there
 * did not exist a property with the given key, it will be created.
 *
 * @param key the key of the property to be changed.
 * @param value the value of the property to be changed, or
 * <code>null</code> to delete the property.
 * @exception RemoteException if a network error occurres.
 * @exception SecurityException if the user does not have the required
 * access rights.
 * @exception IllegalArgumentException if the key is
 * <code>null</code>.  
 * @exception PropertyException if an error occured on the storage of
 * the property (e.g. FileAccess, Network error, Database error...).
 */
    public void changeProperty(String key, Object value) throws SecurityException, RemoteException, IllegalArgumentException, PropertyException {
        try {
            if (!properties_requested_) {
                synchronized (properties_lock_) {
                    properties_ = backend_.getProperties(id_);
                }
                properties_requested_ = true;
            }
            backend_.changeProperty(id_, key, value);
            super.changeProperty(key, value);
        } catch (TimmonBackEndException tbee) {
            throw new PropertyException(tbee.getMessage());
        }
    }

    /**
 * Changes the properties. If there exists a property with one of the
 * given keys, the new value replaces the old one. If the value is set
 * to <code>null</code> the property is deleted. If there did not
 * exist a property with one of the given keys, it will be created.
 *
 * @param properties the map containing the properties to be changed.
 * @exception RemoteException if a network error occurres.
 * @exception SecurityException if the user does not have the required
 * access rights.
 * @exception IllegalArgumentException if <code>properties</code> is
 * <code>null</code>. 
 * @exception PropertyException if an error occured on the storage of
 * the property (e.g. FileAccess, Network error, Database error...).
 */
    public void changeProperties(Map properties) throws SecurityException, RemoteException, IllegalArgumentException, PropertyException {
        try {
            if (!properties_requested_) {
                synchronized (properties_lock_) {
                    properties_ = backend_.getProperties(id_);
                }
                properties_requested_ = true;
            }
            if (property_change_support_ == null) backend_.changeProperties(id_, properties);
            super.changeProperties(properties);
        } catch (TimmonBackEndException tbee) {
            throw new PropertyException(tbee.getMessage());
        }
    }

    /**
 * Returns the value of the property with the given key or
 * <code>null</code>, if the key does not exist within the
 * properties.
 *
 * @param key the key of the property.
 * @return the value of the property, or <code>null</code>, if the
 * key does not exist within the properties.
 * @exception RemoteException if a network error occurres.
 * @exception SecurityException if the user does not have the required
 * access rights.
 * @exception IllegalArgumentException if the <code>key</code> is
 * <code>null</code>.
 * @exception PropertyException if an error occured on the storage of
 * the property (e.g. FileAccess, Network error, Database error...).
 */
    public Object getProperty(String key) throws SecurityException, RemoteException, IllegalArgumentException, PropertyException {
        try {
            if (!properties_requested_) {
                synchronized (properties_lock_) {
                    properties_ = backend_.getProperties(id_);
                    properties_requested_ = true;
                }
            }
            return (super.getProperty(key));
        } catch (TimmonBackEndException tbee) {
            throw new PropertyException(tbee.getMessage());
        }
    }

    /**
 * Returns a map holding key-value pairs of all properties or
 * <code>null</code>, if no properties exist for the given object. 
 *
 * @return a map holding key-value pairs of all properties or
 * <code>null</code> if no properties exist for the given object. 
 * @exception RemoteException if a network error occurres.
 * @exception SecurityException if the user does not have the required
 * access rights.
 * @exception PropertyException if an error occured on the storage of
 * the property (e.g. FileAccess, Network error, Database error...).
 */
    public Map getProperties() throws SecurityException, RemoteException, PropertyException {
        try {
            if (!properties_requested_) {
                synchronized (properties_lock_) {
                    properties_ = backend_.getProperties(id_);
                }
                properties_requested_ = true;
            }
            return (super.getProperties());
        } catch (TimmonBackEndException tbee) {
            throw new PropertyException(tbee.getMessage());
        }
    }
}
