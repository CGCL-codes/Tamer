package org.dicom4j.network.dimse.messages.support;

import org.dicom4j.data.CommandSet;
import org.dicom4j.dicom.DicomException;
import org.dicom4j.dicom.DicomTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for Dimse message which have an AffectedSOPClass field
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public abstract class DimseMessageWithAffectedSOPClass extends AbstractDimseMessage {

    private static final Logger logger = LoggerFactory.getLogger(DimseResponseMessage.class);

    public DimseMessageWithAffectedSOPClass(CommandSet aCommandSet) {
        super(aCommandSet);
    }

    public DimseMessageWithAffectedSOPClass() throws DicomException {
        super();
    }

    public DimseMessageWithAffectedSOPClass(int aMessageID) throws DicomException {
        super(aMessageID);
    }

    public DimseMessageWithAffectedSOPClass(int aMessageID, String aAffectedSOPClassUID) throws DicomException {
        super(aMessageID);
        this.getCommandSet().getUniqueIdentifier(DicomTags.AffectedSOPClassUID).addValue(aAffectedSOPClassUID);
    }

    public String getAffectedSOPClassName() {
        if (this.getSopClassRegistry() == null) {
            logger.warn("SopClassRegistry was no set, return the AffectedSOPClassUID");
            return this.getAffectedSOPClassUID();
        } else {
            try {
                return this.getSopClassRegistry().getName(this.getAffectedSOPClassUID());
            } catch (Exception lE) {
                return this.getAffectedSOPClassUID();
            }
        }
    }

    public String getAffectedSOPClassUID() {
        try {
            if (this.hasAffectedSOPClassUIDElement()) {
                return this.getCommandSet().getUniqueIdentifier(DicomTags.AffectedSOPClassUID).getSingleStringValue();
            } else {
                return "";
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return "";
        }
    }

    public boolean hasAffectedSOPClassUIDElement() {
        return this.getCommandSet().hasElement(DicomTags.AffectedSOPClassUID);
    }
}
