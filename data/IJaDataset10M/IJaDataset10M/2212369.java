package org.dicom4j.network.protocoldataunit;

import java.io.IOException;
import java.util.ListIterator;
import org.dicom4j.dicom.DicomException;
import org.dicom4j.io.BinaryInputStream;
import org.dicom4j.io.BinaryOutputStream;
import org.dicom4j.network.protocoldataunit.items.PresentationContextItemRQ;
import org.dicom4j.network.protocoldataunit.support.AssociateRQACPDU;
import org.dicom4j.network.protocoldataunit.support.PresentationContextItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ASSOCIATE-RQ PDU (see DICOM PS 3.8)
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class AssociateRQPDU extends AssociateRQACPDU {

    private static Logger logger = LoggerFactory.getLogger(AssociateRQPDU.class);

    /**
	 * Create a new ASSOCIATE-RQ PDU
	 */
    public AssociateRQPDU() {
        super();
    }

    /**
	 * Create a new Associate-RQ PDU
	 * 
	 * @param aCalledAETitle
	 *          the called AET
	 * @param aCallingAETitle
	 *          the calling AET
	 */
    public AssociateRQPDU(String aCalledAETitle, String aCallingAETitle) {
        super(aCalledAETitle, aCallingAETitle);
    }

    /**
	 * Add a new PresentationContext Item
	 * 
	 * @param aItem
	 *          the item
	 */
    public void addPresentationContextItem(PresentationContextItemRQ aItem) {
        assert aItem != null;
        fPresentationContextItems.add(aItem);
    }

    @Override
    public String getName() {
        return "ASSOCIATE-RQ";
    }

    public PresentationContextItemRQ getPresentationContextItem(byte aId) {
        ListIterator<PresentationContextItem> i = fPresentationContextItems.listIterator();
        while (i.hasNext()) {
            PresentationContextItemRQ lPres = (PresentationContextItemRQ) i.next();
            if (lPres.getID() == aId) {
                return lPres;
            }
        }
        return null;
    }

    /**
	 * return a PresentationContextItemRQ depending of an abstractSyntax
	 * @param abstractSyntax the abstractSyntax
	 * @return the PresentationContextItemRQ of null if not present
	 */
    public PresentationContextItemRQ getPresentationContextItem(String abstractSyntax) {
        ListIterator i = fPresentationContextItems.listIterator();
        while (i.hasNext()) {
            PresentationContextItemRQ lPres = (PresentationContextItemRQ) i.next();
            if (lPres.getAbstractSyntax().equals(abstractSyntax.trim())) {
                return lPres;
            }
        }
        return null;
    }

    public PresentationContextItemRQ[] getPresentationContextItemRQ() {
        PresentationContextItemRQ[] Result = new PresentationContextItemRQ[fPresentationContextItems.size()];
        int lIndex = 0;
        ListIterator i = fPresentationContextItems.listIterator();
        while (i.hasNext()) {
            Result[lIndex] = (PresentationContextItemRQ) i.next();
        }
        return Result;
    }

    @Override
    public ProtocolDataUnitType getType() {
        return ProtocolDataUnitType.A_ASSOCIATE_RQ;
    }

    @Override
    public void read(BinaryInputStream aStream, int aLength) throws DicomException, IOException {
        logger.debug("we must read " + aLength + " bytes ");
        super.read(aStream, aLength);
        logger.debug("reading completed");
    }

    public void setPresentationContexts(byte aID, String aAbstractSyntax, String[] aTransfertSyntaxes) {
        PresentationContextItemRQ lItem = new PresentationContextItemRQ(aID);
        lItem.setAbstractSyntax(aAbstractSyntax);
        lItem.addTransferSyntaxes(aTransfertSyntaxes);
        addPresentationContextItem(lItem);
    }

    @Override
    public void write(BinaryOutputStream aStream) throws DicomException, IOException {
        logger.debug("Start writing");
        super.write(aStream);
        logger.debug("Stop writing");
    }
}
