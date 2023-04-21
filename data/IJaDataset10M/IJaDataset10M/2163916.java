package dr.evolution.sequence;

import dr.evolution.datatype.DataType;
import dr.evolution.util.Taxon;
import dr.util.Attributable;
import dr.util.Identifiable;
import java.util.Iterator;

/**
 * Class for storing a molecular sequence.
 *
 * @author Alexei Drummond
 * @author Andrew Rambaut
 * @version $Id: Sequence.java,v 1.35 2005/05/25 09:35:28 rambaut Exp $
 */
public class Sequence implements Identifiable, Attributable {

    /**
     * Empty constructor.
     */
    public Sequence() {
        sequenceString = new StringBuffer();
    }

    /**
     * Constructor with initial sequence string.
     *
     * @param sequence a string representing the sequence
     */
    public Sequence(String sequence) {
        sequenceString = new StringBuffer();
        setSequenceString(sequence);
    }

    /**
     * Clone constructor
     *
     * @param sequence the sequence to clone
     */
    public Sequence(Sequence sequence) {
        this(sequence.getTaxon(), sequence.getSequenceString());
    }

    /**
     * Constructor with taxon and sequence string.
     *
     * @param taxon    the sequence's taxon
     * @param sequence the sequence's symbol string
     */
    public Sequence(Taxon taxon, String sequence) {
        sequenceString = new StringBuffer();
        setTaxon(taxon);
        setSequenceString(sequence);
    }

    /**
     * @return the DataType of the sequences.
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * @return the length of the sequences.
     */
    public int getLength() {
        return sequenceString.length();
    }

    /**
     * @return a String containing the sequences.
     */
    public String getSequenceString() {
        return sequenceString.toString();
    }

    /**
     * @return a char containing the state at index.
     */
    public char getChar(int index) {
        return sequenceString.charAt(index);
    }

    /**
     * @return the state at site index.
     */
    public int getState(int index) {
        return dataType.getState(sequenceString.charAt(index));
    }

    /**
     */
    public final void setState(int index, int state) {
        sequenceString.setCharAt(index, dataType.getChar(state));
    }

    /**
     * Characters are copied from the sequences into the destination character array dst.
     */
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        sequenceString.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * Set the DataType of the sequences.
     */
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    /**
     * Set the DataType of the sequences.
     */
    public DataType guessDataType() {
        return DataType.guessDataType(sequenceString.toString());
    }

    /**
     * Set the sequences using a string.
     */
    public void setSequenceString(String sequence) {
        sequenceString.setLength(0);
        sequenceString.append(sequence.toUpperCase());
    }

    /**
     * Append a string to the sequences.
     */
    public void appendSequenceString(String sequence) {
        sequenceString.append(sequence);
    }

    /**
     * Insert a string into the sequences.
     */
    public void insertSequenceString(int offset, String sequence) {
        sequenceString.insert(offset, sequence);
    }

    /**
     * Sets a taxon for this sequences.
     *
     * @param taxon the taxon.
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the taxon for this sequences.
     */
    public Taxon getTaxon() {
        return taxon;
    }

    private Attributable.AttributeHelper attributes = null;

    /**
     * Sets an named attribute for this object.
     *
     * @param name  the name of the attribute.
     * @param value the new value of the attribute.
     */
    public void setAttribute(String name, Object value) {
        if (attributes == null) attributes = new Attributable.AttributeHelper();
        attributes.setAttribute(name, value);
    }

    /**
     * @param name the name of the attribute of interest.
     * @return an object representing the named attributed for this object.
     */
    public Object getAttribute(String name) {
        if (attributes == null) return null; else return attributes.getAttribute(name);
    }

    /**
     * @return an iterator of the attributes that this object has.
     */
    public Iterator<String> getAttributeNames() {
        if (attributes == null) return null; else return attributes.getAttributeNames();
    }

    protected String id = null;

    /**
     * @return the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     */
    public void setId(String id) {
        this.id = id;
    }

    protected Taxon taxon = null;

    protected StringBuffer sequenceString = null;

    protected DataType dataType = null;
}
