package org.dicom4j.network.dimse.messages.support;

import org.dicom4j.data.CommandSet;
import org.dicom4j.data.DataElements;
import org.dicom4j.dicom.DicomException;
import org.dicom4j.dicom.DicomTags;
import org.dicom4j.dicom.network.dimse.DimsePriority;
import org.dicom4j.network.dimse.DimseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @since 
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public abstract class CompositeDimseRequestWithPriorityMessage extends CompositeDimseRequestMessage {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CompositeDimseRequestWithPriorityMessage.class);

    public CompositeDimseRequestWithPriorityMessage(CommandSet commandSet) {
        super(commandSet);
    }

    public CompositeDimseRequestWithPriorityMessage(int messageID, String affectedSOPClassUID) throws DicomException {
        super(messageID, affectedSOPClassUID);
    }

    @Override
    protected void fillCommandSet(CommandSet commandSet) throws DicomException {
        super.fillCommandSet(commandSet);
        commandSet.addElement(DataElements.newPriority());
        setPriority(DimsePriority.MEDIUM.value());
    }

    public boolean hasPriorityElement() {
        return this.getCommandSet().hasElement(DicomTags.Priority);
    }

    /**
	 * <p>return the message's priority.
	 * 
	 * if the message doesn't contains Priority element, Medium priority will be returned
	 * 
	 * @return message's priority
	 * @throws DimseException
	 */
    public int getPriority() throws DimseException {
        if (!hasPriorityElement()) {
            return DimsePriority.MEDIUM.value();
        } else {
            return this.getCommandSet().getUnsignedShort(DicomTags.Priority).getIntegerValue(0);
        }
    }

    /**
	 * set the Priority's value
	 * 
	 * @param value priority
	 * @throws DicomException
	 */
    public void setPriority(int value) throws DicomException {
        logger.info("setPriority: " + DimsePriority.toString(value));
        this.setorAddUnsignedShort(DicomTags.Priority, value);
    }

    /**
	 * return the priority for human reading
	 * 
	 * @return the priority
	 */
    public String getPriorityAsString() {
        try {
            return DimsePriority.toString(this.getPriority());
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return "";
        }
    }
}
