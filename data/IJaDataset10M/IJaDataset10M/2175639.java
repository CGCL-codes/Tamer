package hla.rti1516e.encoding;

/**
 * Interface for the HLA data type HLAinteger32LE.
 */
public interface HLAinteger32LE extends DataElement {

    /**
    * Returns the int value of this element.
    *
    * @return int value
    */
    int getValue();

    /**
    * Sets the int value of this element.
    *
    * @param value new value
    */
    void setValue(int value);
}
