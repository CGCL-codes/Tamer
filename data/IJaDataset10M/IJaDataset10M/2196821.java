package co.edu.unal.ungrid.image.dicom.core;

import java.io.IOException;

/**
 * <p>
 * A concrete class specializing
 * {@link co.edu.unal.ungrid.image.dicom.core.Attribute Attribute} for Code
 * String (CS) attributes.
 * </p>
 * 
 * <p>
 * Though an instance of this class may be created using its constructors, there
 * is also a factory class,
 * {@link co.edu.unal.ungrid.image.dicom.core.AttributeFactory AttributeFactory}.
 * </p>
 * 
 * @see co.edu.unal.ungrid.image.dicom.core.Attribute
 * @see co.edu.unal.ungrid.image.dicom.core.AttributeFactory
 * @see co.edu.unal.ungrid.image.dicom.core.AttributeList
 * 
 * 
 */
public class CodeStringAttribute extends StringAttribute {

    /**
	 * <p>
	 * Construct an (empty) attribute.
	 * </p>
	 * 
	 * @param t
	 *            the tag of the attribute
	 */
    public CodeStringAttribute(AttributeTag t) {
        super(t);
    }

    /**
	 * <p>
	 * Read an attribute from an input stream.
	 * </p>
	 * 
	 * @param t
	 *            the tag of the attribute
	 * @param vl
	 *            the value length of the attribute
	 * @param i
	 *            the input stream
	 * @exception IOException
	 * @exception DicomException
	 */
    public CodeStringAttribute(AttributeTag t, long vl, DicomInputStream i) throws IOException, DicomException {
        super(t, vl, i);
    }

    /**
	 * <p>
	 * Read an attribute from an input stream.
	 * </p>
	 * 
	 * @param t
	 *            the tag of the attribute
	 * @param vl
	 *            the value length of the attribute
	 * @param i
	 *            the input stream
	 * @exception IOException
	 * @exception DicomException
	 */
    public CodeStringAttribute(AttributeTag t, Long vl, DicomInputStream i) throws IOException, DicomException {
        super(t, vl, i);
    }

    /**
	 * <p>
	 * Get the value representation of this attribute (CS).
	 * </p>
	 * 
	 * @return 'C','S' in ASCII as a two byte array; see
	 *         {@link co.edu.unal.ungrid.image.dicom.core.ValueRepresentation ValueRepresentation}
	 */
    public byte[] getValueRepresentation() {
        return ValueRepresentation.CS;
    }
}
