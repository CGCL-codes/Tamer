package org.koossery.adempiere.core.backend.interfaces.dao.Requisition_to_invoice.RfQ_Topic;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicSubscriberCriteria;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicSubscriberDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IC_RfQ_TopicSubscriberDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(C_RfQ_TopicSubscriberCriteria criteria) throws KTAdempiereException;

    public boolean update(C_RfQ_TopicSubscriberCriteria c_RfQ_TopicSubscriberCriteria) throws KTAdempiereException;

    public ArrayList<C_RfQ_TopicSubscriberDTO> getC_RfQ_TopicSubscriber(C_RfQ_TopicSubscriberCriteria c_RfQ_TopicSubscriberCriteria) throws KTAdempiereException;
}
