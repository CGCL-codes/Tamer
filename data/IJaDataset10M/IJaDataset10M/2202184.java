package org.dicom4j.toolkit.query;

import org.dicom4j.network.association.associate.AssociateSession;
import org.dicom4j.network.dimse.messages.support.DimseRequestMessage;

/**
 * 
 * 
 * @since 0.0.4
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class StoreQuery extends DicomQuery {

    public StoreQuery() throws Exception {
        super();
    }

    @Override
    protected DimseRequestMessage createsMessage(AssociateSession aSession) throws Exception {
        return null;
    }
}
