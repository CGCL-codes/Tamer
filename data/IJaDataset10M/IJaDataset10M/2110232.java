package org.tm4j.topicmap.ozone;

import org.tm4j.net.Locator;
import org.tm4j.topicmap.DataObject;
import org.tm4j.topicmap.TMTypes;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapRuntimeException;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Defines an occurrence instance of a Topic.
 * The type of an Occurrence is defined either by a Topic defining the type.
 * An occurrence may be scoped and may contain either an in-line data resource (as a string) or
 * a reference to an external resource (specified as a URI).
 *
 * @author <a href="mailto:kal@techquila.com">Kal Ahmed</a>
 * @author <a href="mailto:gerd@smb-tec.com">Gerd Mueller</a>
 */
public class OzoneOccurrenceImpl extends OzoneScopedObjectImpl implements OzoneOccurrence {

    static final long serialVersionUID = 1L;

    static final long subSerialVersionUID = 1L;

    /** */
    protected Topic m_type;

    protected Topic m_parent;

    protected DataObject m_data;

    /** */
    public OzoneOccurrenceImpl() {
        m_type = null;
        m_parent = null;
        m_data = null;
    }

    private DataObject getDataObject() {
        if (m_data == null) {
            try {
                m_data = (OzoneDataObject) database().createObject(OzoneDataObjectImpl.class.getName());
            } catch (Exception ex) {
                throw new TopicMapRuntimeException("INTERNAL ERROR: cannot construct DataObject for new Occurrence. ", ex);
            }
        }
        return m_data;
    }

    /**
     * @return The Topic defining the type of this Occurrence, or null if no such Topic has been specified.
     */
    public Topic getType() {
        return m_type;
    }

    /**
     * Sets the Topic defining the type of this Occurrence.
     */
    public void setType(Topic type) throws PropertyVetoException {
        Topic oldType = m_type;
        fireVetoableChange("type", oldType, m_type);
        m_type = type;
        firePropertyChange("type", oldType, m_type);
    }

    public boolean isOfType(Topic type) {
        if (m_type == null) {
            return (type == null);
        }
        return m_type.equals(type);
    }

    public String getData() {
        return getDataObject().getData();
    }

    public void setData(String data) throws PropertyVetoException {
        String oldData = getDataObject().getData();
        fireVetoableChange("resource", oldData, data);
        getDataObject().setData(data);
        firePropertyChange("resource", oldData, data);
    }

    public Locator getDataLocator() {
        Locator loc = getDataObject().getDataLocator();
        return getDataObject().getDataLocator();
    }

    public void setDataLocator(Locator loc) throws PropertyVetoException {
        Locator oldLoc = getDataObject().getDataLocator();
        fireVetoableChange("resourceRef", oldLoc, loc);
        getDataObject().setDataLocator(loc);
        firePropertyChange("resourceRef", oldLoc, loc);
    }

    public boolean isDataInline() {
        return getDataObject().isDataInline();
    }

    public Topic getParent() {
        return m_parent;
    }

    public void setParent(Topic parent) {
        m_parent = parent;
    }

    public int getObjectType() {
        return TMTypes.TM_OCCURRENCE;
    }

    /**
     * Called by Ozone when the object is destroyed.
     */
    public void onDelete() throws Exception {
        database().deleteObject((org.tm4j.topicmap.ozone.OzoneDataObject) m_data);
    }

    /** */
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeLong(subSerialVersionUID);
        out.writeObject(m_parent);
        out.writeObject(m_type);
        out.writeObject(m_data);
    }

    /** */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        long version = in.readLong();
        m_parent = (Topic) in.readObject();
        m_type = (Topic) in.readObject();
        m_data = (DataObject) in.readObject();
    }
}
