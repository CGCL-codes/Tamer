package org.caleigo.core.meta;

import java.util.*;
import org.caleigo.core.*;
import org.caleigo.toolkit.log.Log;

class CustomDataSourceDescriptor extends AbstractDataSourceDescriptor {

    private static HashMap sRegisteredDescriptors = new HashMap();

    /** Returns a CustomDataSourceDescriptor with the given code name if it has
     * been created, otherwise null.
     * 
     * All CustomDataSourceDescriptor that are created are automatically registered.
     * This mehtod has package scope since it is only used to handle serialization
     * of custom descriptors.
     */
    static CustomDataSourceDescriptor getRegisteredCustomDataSourceDescriptor(String codeName) {
        return (CustomDataSourceDescriptor) sRegisteredDescriptors.get(codeName);
    }

    private Hashtable mEntityDescriptorCodeNames = new Hashtable();

    private Hashtable mEntityDescriptorSourceNames = new Hashtable();

    public CustomDataSourceDescriptor(String codeName, String sourceName, String displayName, String version, boolean readOnly) {
        super(codeName, sourceName, displayName, version, readOnly, null);
        sRegisteredDescriptors.put(codeName, this);
    }

    public int getEntityDescriptorCount() {
        return mEntityDescriptorCodeNames.size();
    }

    public IEntityDescriptor getEntityDescriptor(int index) {
        Enumeration entityDescriptors = mEntityDescriptorCodeNames.elements();
        for (int i = 0; i < index && entityDescriptors.hasMoreElements(); i++) entityDescriptors.nextElement();
        if (entityDescriptors.hasMoreElements()) return (IEntityDescriptor) entityDescriptors.nextElement(); else return null;
    }

    public IEntityDescriptor getEntityDescriptor(String codeName) {
        return (IEntityDescriptor) mEntityDescriptorCodeNames.get(codeName);
    }

    protected Object writeReplace() throws java.io.ObjectStreamException {
        return new Dezerializer(this.getCodeName());
    }

    protected void finalize() {
        sRegisteredDescriptors.remove(this.getCodeName());
    }

    protected void addEntityDescriptor(IEntityDescriptor entityDescriptor) {
        mEntityDescriptorCodeNames.put(entityDescriptor.getCodeName(), entityDescriptor);
        mEntityDescriptorSourceNames.put(entityDescriptor.getSourceName(), entityDescriptor);
    }

    protected IEntityDescriptor getEntityDescriptorBySourceName(String sourceName) {
        return (IEntityDescriptor) mEntityDescriptorSourceNames.get(sourceName);
    }

    protected static class Dezerializer implements java.io.Serializable {

        protected String mCodeName;

        public Dezerializer(String codeName) {
            mCodeName = codeName;
        }

        protected Object readResolve() throws java.io.ObjectStreamException {
            if (sRegisteredDescriptors.containsKey(mCodeName)) return sRegisteredDescriptors.get(mCodeName); else {
                Log.printError(this, "CustomDataSourceDescriptor with code name " + mCodeName + " not found");
                throw new java.io.InvalidObjectException("CustomDataSourceDescriptor with code name " + mCodeName + " not found");
            }
        }
    }
}
