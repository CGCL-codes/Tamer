package org.dicom4j.network.association.listeners.defaults;

import org.dicom4j.network.association.Association;
import org.dicom4j.network.association.associate.AssociateAbort;
import org.dicom4j.network.association.associate.AssociateReject;
import org.dicom4j.network.association.associate.AssociateRelease;
import org.dicom4j.network.association.associate.AssociateSession;
import org.dicom4j.network.association.listeners.AssociationListener;
import org.dicom4j.network.dimse.messages.support.AbstractDimseMessage;

/**
 * default implementation of {@link AssociationListener AssociationListener}
 * interface
 * 
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 */
public class DefaultAssociationListener implements AssociationListener {

    public DefaultAssociationListener() {
        super();
    }

    public void associateRelease(Association aAssociation, AssociateRelease aRelease) throws Exception {
        aAssociation.sendReleaseResponse();
    }

    public void associationAborted(Association aAssociation, AssociateAbort aAbort) throws Exception {
    }

    public void associationCreated(Association aAssociation) throws Exception {
    }

    public void associationOpened(Association aAssociation, AssociateSession aAssociateSession) throws Exception {
    }

    public void associationRejected(Association aAssociation, AssociateReject aReject) throws Exception {
    }

    public void associationReleased(Association aAssociation) throws Exception {
    }

    public void exceptionCaught(Association aAssociation, Throwable cause) {
    }

    public void messageReceived(Association aAssociation, byte aPresentationContextID, AbstractDimseMessage aMessage) throws Exception {
    }

    @Override
    public String toString() {
        return "DefaultAssociationListener";
    }
}
