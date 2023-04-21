package org.neurpheus.nlp.morphology.baseimpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.neurpheus.nlp.morphology.tagset.GrammaticalProperties;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;
import org.neurpheus.nlp.morphology.tagset.Tagset;

/**
 *
 * @author jstrychowski
 */
public class GrammaticalPropertiesListImpl implements GrammaticalPropertiesList {

    /** Unique identifier of this class. */
    protected static final long serialVersionUID = -770608080501101336L;

    /** Logger of this class. */
    private static Logger logger = Logger.getLogger(GrammaticalPropertiesListImpl.class.getName());

    /** Static value used for id generating. */
    private static int idGenerator = 1;

    /** Holds unique identifier of this object. */
    private int id;

    private List list;

    public GrammaticalPropertiesListImpl() {
        list = Collections.EMPTY_LIST;
        id = idGenerator++;
    }

    public GrammaticalPropertiesListImpl(String marks, Tagset tagset) {
        id = idGenerator++;
        String[] tab = marks.split("\\+");
        if (tab.length == 1) {
            list = Collections.singletonList(tagset.getGrammaticalProperties(marks));
        } else {
            list = new ArrayList(tab.length);
            for (int i = 0; i < tab.length; i++) {
                list.add(tagset.getGrammaticalProperties(tab[i]));
            }
        }
    }

    /**
     * Returns a list of grammatical properties
     * 
     * @return The list of {@link GrammaticalProperties} objects.
     */
    public List getGrammaticalProperties() {
        return list;
    }

    /**
     * Sets a list of grammatical properties
     * 
     * @param newList The list of {@link GrammaticalProperties} objects.
     */
    public void setGrammaticalProperties(List newList) {
        list = newList;
    }

    /** 
     * Returns a string representation of the grammatical properties.
     * 
     * @return The mark of the grammatical properties.
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        for (final Iterator it = list.iterator(); it.hasNext(); ) {
            if (res.length() > 0) {
                res.append(MARK_SEPARATOR);
            }
            res.append(it.next().toString());
        }
        return res.toString();
    }

    /**
     * Checks if this grammatical properties are equals to the given one.
     * 
     * @param obj The object to compare with.
     * 
     * @return <code>true</code> if grammatical properties are equal.
     */
    public boolean equals(final Object obj) {
        if (obj != null && obj instanceof GrammaticalPropertiesList) {
            return toString().equals(obj.toString());
        } else {
            return false;
        }
    }

    /**
     * Almost unique identifier of this object.
     * 
     * @return The identifier.
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Compares this grammatical properties with another one.
     * 
     * @param obj The object which have to be compared.
     * 
     * @return result of a marks comparison.
     */
    public int compareTo(final Object obj) {
        if (obj != null && obj instanceof GrammaticalPropertiesList) {
            GrammaticalPropertiesList gp = (GrammaticalPropertiesList) obj;
            return toString().compareTo(gp.toString());
        } else {
            return 1;
        }
    }

    /**
     * Merges two list of grammatical properties into one.
     * 
     * @param gpl The grammatiacal properties list which should be merged with thin one.
     */
    public void merge(GrammaticalPropertiesList gpl) {
        for (final Iterator it = gpl.getGrammaticalProperties().iterator(); it.hasNext(); ) {
            GrammaticalProperties gp = (GrammaticalProperties) it.next();
            merge(gp);
        }
    }

    public void merge(GrammaticalProperties gp) {
        if (!list.contains(gp)) {
            if (!(list instanceof ArrayList)) {
                list = new ArrayList(list);
            }
            list.add(gp);
        }
    }

    /**
     * Checks if this list of grammatical properties contains grammatical properties
     * covering the given properties.
     * 
     * 
     * @param properties Subset of tags which may be covered.
     * 
     * @return <code>true</code> if one of gramamtical properties represented by this
     *  object contain all tags from the given grammatical propertie object.
     */
    public boolean covers(GrammaticalProperties properties) {
        for (final Iterator it = getGrammaticalProperties().iterator(); it.hasNext(); ) {
            GrammaticalProperties gp = (GrammaticalProperties) it.next();
            if (gp.covers(properties)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all duplicated grammatical properties.
     */
    public void normalize(Tagset tagset) {
        GrammaticalProperties[] tab = (GrammaticalProperties[]) getGrammaticalProperties().toArray(new GrammaticalProperties[0]);
        for (int i = 0; i < tab.length; i++) {
            GrammaticalProperties gp = tab[i];
            if (gp != null) {
                for (int j = 0; j < tab.length; j++) {
                    if (i != j && tab[j] != null && gp.covers(tab[j])) {
                        tab[j] = null;
                        break;
                    }
                }
            }
        }
        int count = 0;
        for (int i = 0; i < tab.length; i++) {
            GrammaticalProperties gp = tab[i];
            if (gp != null) {
                count++;
            }
        }
        list = new ArrayList(count);
        for (int i = 0; i < tab.length; i++) {
            GrammaticalProperties gp = tab[i];
            if (gp != null) {
                list.add(tagset.getGrammaticalProperties(gp.getMark()));
            }
        }
    }

    /**
     * Returns a unique identifier of this object.
     * 
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets a unique identifier for this object.
     * 
     * @param newId The unique identifier.
     */
    public void setId(int newId) {
        id = newId;
    }

    /**
     * Writes this object into the given output stream.
     *
     * @param out   The output stream where this IPB should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeShort(id);
        out.writeByte((byte) list.size());
        for (final Iterator it = list.iterator(); it.hasNext(); ) {
            GrammaticalProperties gp = (GrammaticalProperties) it.next();
            out.writeShort(gp.getId());
        }
    }

    /**
     * Reads this object data from the given input stream.
     *
     * @param in   The input stream where this IPB is stored.
     *
     * @throws IOException if any read error occurred.
     * @throws ClassNotFoundException if this object cannot be instantied.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = in.readShort();
        if (idGenerator < id) {
            idGenerator = id + 1;
        }
        int size = in.readByte();
        list = new ArrayList(size);
        Tagset tagset = TagsetImpl.getDeserializationTagset();
        for (int i = 0; i < size; i++) {
            int gpId = in.readShort();
            GrammaticalProperties gp = tagset.getGrammaticalPropertiesById(gpId);
            if (gp == null) {
                throw new IOException("Cannot obtain grammatical properties having id " + gpId);
            }
            list.add(gp);
        }
    }

    /**
     * Writes this object into the given data output stream.
     *
     * @param out   The output stream where this tagset should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    public void write(final DataOutputStream out) throws IOException {
        TagsetStreamPacker.writeInt(id, out);
        TagsetStreamPacker.writeInt(list.size(), out);
        for (final Iterator it = list.iterator(); it.hasNext(); ) {
            GrammaticalProperties gp = (GrammaticalProperties) it.next();
            TagsetStreamPacker.writeInt(gp.getId(), out);
        }
    }

    /**
     * Reads this object from the given data input stream.
     *
     * @param in The input stream from which this object should be read.
     * @param tagset The tagset for which this category is defined.
     *
     * @throws IOException if any read error occurred.
     */
    public void read(DataInputStream in, final Tagset tagset) throws IOException {
        id = TagsetStreamPacker.readInt(in);
        int size = TagsetStreamPacker.readInt(in);
        list = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            int id = TagsetStreamPacker.readInt(in);
            GrammaticalProperties gp = tagset.getGrammaticalPropertiesById(id);
            list.add(gp);
        }
    }
}
