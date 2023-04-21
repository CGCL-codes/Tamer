package org.dicom4j.toolkit.dimse.service;

import org.dicom4j.dicom.network.dimse.DimseStatus;
import org.dicom4j.network.dimse.messages.CStoreRequestMessage;
import org.dicom4j.network.dimse.messages.CStoreResponseMessage;
import org.dicom4j.network.dimse.messages.DimseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to implement a CStoreSCP service.
 * <p>
 * sub-class need to override "handleStoreRequest"
 * </p>
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public abstract class CStoreSCP {

    private static final Logger fLogger = LoggerFactory.getLogger(CStoreSCP.class);
}
