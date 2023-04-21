package org.dicom4j.data.elements;

import org.dicom4j.data.elements.support.DataElement;
import org.dicom4j.data.elements.support.StringElement;
import org.dicom4j.dicom.DicomTag;
import org.dicom4j.dicom.DicomViolationException;
import org.dicom4j.dicom.ValueRepresentation;

/**
 * <p>
 * Application Entity (AE) {@link DataElement element}
 * </p>
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class ApplicationEntity extends StringElement {

    /**
	 * creates new element
	 * 
	 * @param aDataElementTag
	 *          the tag element's
	 */
    public ApplicationEntity(DicomTag aDataElementTag) {
        super(aDataElementTag);
    }

    /**
	 * <p>
	 * Creates an new DataElement
	 * </p>
	 * 
	 * @param aDataElementTag
	 *          the tag of the element
	 * @param aAET
	 *          the value for this element
	 * @throws DicomViolationException
	 *           if the value doesn't respect the DICOM standard
	 */
    public ApplicationEntity(DicomTag aDataElementTag, String aAET) throws DicomViolationException {
        this(aDataElementTag);
        this.addValue(aAET);
    }

    @Override
    public ValueRepresentation getValueRepresentation() {
        return ValueRepresentation.ApplicationEntity;
    }

    @Override
    public boolean isValid() {
        if (this.getSingleStringValue().length() > 16) {
            return false;
        }
        return true;
    }
}
