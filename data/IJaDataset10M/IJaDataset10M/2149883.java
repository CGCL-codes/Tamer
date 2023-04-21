package org.exist.xquery.value;

import org.exist.EXistException;
import org.exist.collections.Collection;
import org.exist.dom.DocumentSet;
import org.exist.dom.EmptyNodeSet;
import org.exist.dom.NodeSet;
import org.exist.dom.StoredNode;
import org.exist.memtree.DocumentBuilderReceiver;
import org.exist.numbering.NodeId;
import org.exist.storage.DBBroker;
import org.exist.storage.Indexable;
import org.exist.storage.ValueIndexFactory;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Constants;
import org.exist.xquery.XPathException;
import org.exist.xquery.util.ExpressionDumper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import java.text.Collator;
import java.util.Iterator;
import java.util.Properties;

/**
 * Represents an atomic value. All simple values that are not nodes extend AtomicValue.
 * As every single item is also a sequence, this class implements both: Item and Sequence.
 * 
 * @author wolf
 */
public abstract class AtomicValue implements Item, Sequence, Indexable {

    /** An empty atomic value */
    public static final AtomicValue EMPTY_VALUE = new EmptyValue();

    public int getType() {
        return Type.ATOMIC;
    }

    public abstract String getStringValue() throws XPathException;

    public abstract AtomicValue convertTo(int requiredType) throws XPathException;

    public abstract boolean compareTo(Collator collator, int operator, AtomicValue other) throws XPathException;

    public abstract int compareTo(Collator collator, AtomicValue other) throws XPathException;

    public abstract AtomicValue max(Collator collator, AtomicValue other) throws XPathException;

    public abstract AtomicValue min(Collator collator, AtomicValue other) throws XPathException;

    /**
     * Compares this atomic value to another. Returns true if the current value is of type string
     * and its value starts with the string value of the other value.
     * 
     * @param collator Collator used for string comparison.
     * @param other
     * @throws XPathException if this is not a string.
     */
    public boolean startsWith(Collator collator, AtomicValue other) throws XPathException {
        throw new XPathException("Cannot call starts-with on value of type " + Type.getTypeName(getType()));
    }

    /**
     * Compares this atomic value to another. Returns true if the current value is of type string
     * and its value ends with the string value of the other value.
     * 
     * @param collator Collator used for string comparison.
     * @param other
     * @throws XPathException if this is not a string.
     */
    public boolean endsWith(Collator collator, AtomicValue other) throws XPathException {
        throw new XPathException("Cannot call ends-with on value of type " + Type.getTypeName(getType()));
    }

    /**
     * Compares this atomic value to another. Returns true if the current value is of type string
     * and its value contains the string value of the other value.
     * 
     * @param collator Collator used for string comparison.
     * @param other
     * @throws XPathException if this is not a string.
     */
    public boolean contains(Collator collator, AtomicValue other) throws XPathException {
        throw new XPathException("Cannot call contains on value of type " + Type.getTypeName(getType()));
    }

    public int getItemCount() {
        return 1;
    }

    public int getCardinality() {
        return Cardinality.EXACTLY_ONE;
    }

    public void removeDuplicates() {
    }

    public SequenceIterator iterate() throws XPathException {
        return new SingleItemIterator(this);
    }

    public SequenceIterator unorderedIterator() throws XPathException {
        return new SingleItemIterator(this);
    }

    public int getItemType() {
        return getType();
    }

    public Item itemAt(int pos) {
        return pos > 0 ? null : this;
    }

    public Sequence toSequence() {
        return this;
    }

    public void toSAX(DBBroker broker, ContentHandler handler, Properties properties) throws SAXException {
        try {
            final String s = getStringValue();
            handler.characters(s.toCharArray(), 0, s.length());
        } catch (XPathException e) {
            throw new SAXException(e);
        }
    }

    public void copyTo(DBBroker broker, DocumentBuilderReceiver receiver) throws SAXException {
        try {
            final String s = getStringValue();
            receiver.characters(s);
        } catch (XPathException e) {
            throw new SAXException(e);
        }
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean hasOne() {
        return true;
    }

    public boolean hasMany() {
        return false;
    }

    public void add(Item item) throws XPathException {
    }

    public void addAll(Sequence other) throws XPathException {
    }

    public AtomicValue atomize() throws XPathException {
        return this;
    }

    public abstract boolean effectiveBooleanValue() throws XPathException;

    public NodeSet toNodeSet() throws XPathException {
        throw new XPathException("cannot convert " + Type.getTypeName(getType()) + "('" + getStringValue() + "')" + " to a node set");
    }

    public MemoryNodeSet toMemNodeSet() throws XPathException {
        throw new XPathException("cannot convert " + Type.getTypeName(getType()) + "('" + getStringValue() + "')" + " to a node set");
    }

    public DocumentSet getDocumentSet() {
        return DocumentSet.EMPTY_DOCUMENT_SET;
    }

    public Iterator<Collection> getCollectionIterator() {
        return EmptyNodeSet.EMPTY_COLLECTION_ITERATOR;
    }

    public AtomicValue promote(AtomicValue otherValue) throws XPathException {
        if (getType() != otherValue.getType()) {
            if (Type.subTypeOf(getType(), Type.DECIMAL) && (Type.subTypeOf(otherValue.getType(), Type.DOUBLE) || Type.subTypeOf(otherValue.getType(), Type.FLOAT))) return convertTo(otherValue.getType());
            if (Type.subTypeOf(getType(), Type.FLOAT) && Type.subTypeOf(otherValue.getType(), Type.DOUBLE)) return convertTo(Type.DOUBLE);
            if (Type.subTypeOf(getType(), Type.ANY_URI) && Type.subTypeOf(otherValue.getType(), Type.STRING)) return convertTo(Type.STRING);
        }
        return this;
    }

    /**
     * Dump a string representation of this value to the given 
     * ExpressionDumper.
     * 
	 * @param dumper
	 */
    public void dump(ExpressionDumper dumper) {
        try {
            dumper.display(getStringValue());
        } catch (XPathException e) {
        }
    }

    public int conversionPreference(Class<?> javaClass) {
        return Integer.MAX_VALUE;
    }

    public Object toJavaObject(Class<?> target) throws XPathException {
        throw new XPathException("cannot convert value of type " + Type.getTypeName(getType()) + " to Java object of type " + target.getName());
    }

    public String toString() {
        try {
            return getStringValue();
        } catch (XPathException e) {
            return super.toString();
        }
    }

    public boolean isCached() {
        return false;
    }

    public void setIsCached(boolean cached) {
    }

    public void clearContext(int contextId) throws XPathException {
    }

    public void setSelfAsContext(int contextId) throws XPathException {
    }

    public boolean isPersistentSet() {
        return false;
    }

    public void nodeMoved(NodeId oldNodeId, StoredNode newNode) {
    }

    public byte[] serializeValue(int offset) throws EXistException {
        return ValueIndexFactory.serialize(this, offset);
    }

    public int compareTo(Object other) {
        throw new IllegalArgumentException("Invalid call to compareTo by " + Type.getTypeName(this.getItemType()));
    }

    public int getState() {
        return 0;
    }

    public boolean hasChanged(int previousState) {
        return false;
    }

    public boolean isCacheable() {
        return true;
    }

    private static final class EmptyValue extends AtomicValue {

        public boolean hasOne() {
            return false;
        }

        public boolean isEmpty() {
            return true;
        }

        public String getStringValue() {
            return "";
        }

        public AtomicValue convertTo(int requiredType) throws XPathException {
            switch(requiredType) {
                case Type.ATOMIC:
                case Type.ITEM:
                case Type.STRING:
                    return StringValue.EMPTY_STRING;
                case Type.NORMALIZED_STRING:
                case Type.TOKEN:
                case Type.LANGUAGE:
                case Type.NMTOKEN:
                case Type.NAME:
                case Type.NCNAME:
                case Type.ID:
                case Type.IDREF:
                case Type.ENTITY:
                    return new StringValue("", requiredType);
                case Type.ANY_URI:
                    return AnyURIValue.EMPTY_URI;
                case Type.BOOLEAN:
                    return BooleanValue.FALSE;
                default:
                    throw new XPathException("cannot convert empty value to " + requiredType);
            }
        }

        public boolean effectiveBooleanValue() throws XPathException {
            return false;
        }

        public int compareTo(Collator collator, AtomicValue other) throws XPathException {
            if (other instanceof EmptyValue) return Constants.EQUAL; else return Constants.INFERIOR;
        }

        public boolean compareTo(Collator collator, int operator, AtomicValue other) throws XPathException {
            return false;
        }

        public Item itemAt(int pos) {
            return null;
        }

        public Sequence toSequence() {
            return this;
        }

        public AtomicValue max(Collator collator, AtomicValue other) throws XPathException {
            return this;
        }

        public void add(Item item) throws XPathException {
        }

        public AtomicValue min(Collator collator, AtomicValue other) throws XPathException {
            return this;
        }

        public int conversionPreference(Class<?> javaClass) {
            return Integer.MAX_VALUE;
        }

        public Object toJavaObject(Class<?> target) throws XPathException {
            throw new XPathException("cannot convert value of type " + Type.getTypeName(getType()) + " to Java object of type " + target.getName());
        }
    }
}
