package de.offis.semanticmm4u.global;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;
import component_interfaces.semanticmm4u.realization.IMetadata;
import component_interfaces.semanticmm4u.realization.IMetadataEntry;
import component_interfaces.semanticmm4u.realization.IPropertyList;

public class Metadata extends PropertyList implements IMetadata {

    public void putObject(String key, Object objectValue) {
        this.insert(key, objectValue, true);
    }

    /**
	 * Clone the object recursive.
	 * 
	 * @return a copy of the Object.
	 * @see de.offis.semanticmm4u.global.AbstractMM4UVector#recursiveClone()
	 */
    @Override
    public IPropertyList recursiveClone() {
        Metadata object = new Metadata();
        super.recursiveClone(object);
        return object;
    }

    public void put(IMetadataEntry entry) {
        String hashKey;
        if (entry.getNamespace().equals(IMetadata.DEFAULT_NAMESPACE)) hashKey = entry.getKey(); else hashKey = entry.getNamespace() + entry.getKey();
        Vector entryObjects = (Vector) this.hashtable.get(hashKey);
        if (entryObjects == null) {
            entryObjects = new Vector();
            entryObjects.add(entry);
            this.hashtable.put(hashKey, entryObjects);
        } else {
            entryObjects.add(entry);
        }
    }

    public IMetadataEntry[] get(String key, String namespace) {
        String hashKey = namespace + key;
        Vector entryObjects = (Vector) this.hashtable.get(hashKey);
        if (entryObjects == null) return null;
        IMetadataEntry[] entries = (IMetadataEntry[]) entryObjects.toArray(new IMetadataEntry[0]);
        Comparator timeComparator = new Comparator() {

            public int compare(Object arg0, Object arg1) {
                long time0 = ((IMetadataEntry) arg0).getCreationTime();
                long time1 = ((IMetadataEntry) arg1).getCreationTime();
                if (time0 < time1) return 1; else if (time0 > time1) return -1; else return 0;
            }
        };
        Arrays.sort(entries, timeComparator);
        return entries;
    }

    public void put(String key, String value, String namespace, long creationTime, float reliability, String source) {
        IMetadataEntry entry = new MetadataEntry(key, namespace, creationTime, source, reliability);
        entry.setValue(value);
        this.put(entry);
    }

    public void put(String key, Object value, String namespace, long creationTime, float reliability, String source) {
        IMetadataEntry entry = new MetadataEntry(key, namespace, creationTime, source, reliability);
        entry.setValue(value);
        this.put(entry);
    }

    public void putAll(IMetadata myMetadata) {
        Enumeration enumeration = myMetadata.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            IMetadataEntry[] entries = myMetadata.get(key, "");
            for (int i = 0; i < entries.length; i++) {
                this.put(entries[i]);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Enumeration myPropertyEnum = this.keys();
        while (myPropertyEnum.hasMoreElements()) {
            String tempKey = (String) myPropertyEnum.nextElement();
            IMetadataEntry[] tempValue = this.get(tempKey, "");
            for (int i = 0; i < tempValue.length; i++) builder.append(tempValue[i].toString());
        }
        return builder.toString();
    }

    public void clear() {
        this.hashtable.clear();
    }
}
