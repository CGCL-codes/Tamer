package org.dicom4j.data.elements;

import org.dicom4j.dicom.DicomTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementNotFoundException extends DataElementException {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ElementNotFoundException.class);

    public ElementNotFoundException(DicomTag elementTag) {
        super(elementTag.getName() + " not found");
        logger.error(getMessage());
    }
}
