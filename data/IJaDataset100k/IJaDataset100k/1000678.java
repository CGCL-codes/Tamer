package org.dicom4j.data.elements;

import java.io.IOException;
import org.dicom4j.data.SpecificCharacterSet;
import org.dicom4j.data.elements.support.DataElement;
import org.dicom4j.data.elements.support.StringElement;
import org.dicom4j.dicom.DicomException;
import org.dicom4j.dicom.DicomTag;
import org.dicom4j.dicom.ValueRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Person Name (PN) {@link DataElement DataElement}
 * </p>
 * 
 * @since Alpha 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 */
public class PersonName extends StringElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonName.class);

    /**
	 * <p>
	 * Creates an empty element
	 * </p>
	 * 
	 * @param aTag
	 *          the tag of the element
	 */
    public PersonName(DicomTag aTag) {
        super(aTag);
    }

    /**
	 * <p>
	 * Construct an (empty) attribute.
	 * </p>
	 * 
	 * @param t
	 *          the tag of the attribute
	 * @param specificCharacterSet
	 *          the character set to be used for the text
	 */
    public PersonName(DicomTag t, SpecificCharacterSet specificCharacterSet) {
        super(t, specificCharacterSet);
    }

    /**
	 * Return the value representation of this data element
	 * 
	 * @return the value representation (PN)
	 */
    @Override
    public ValueRepresentation getValueRepresentation() {
        return ValueRepresentation.PersonName;
    }

    /**
	 * <p>
	 * Get the value representation of this attribute (PN).
	 * </p>
	 * 
	 * @return 'P','N' in ASCII as a two byte array; see
	 */
    public byte[] getVR() {
        return ValueRepresentation.PN;
    }

    @Override
    public void setValue(String aValue) {
        LOGGER.debug("setValue: " + aValue);
        super.setValue(aValue);
    }
}
