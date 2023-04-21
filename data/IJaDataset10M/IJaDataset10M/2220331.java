package org.koossery.adempiere.core.backend.interfaces.dao.Requisition_to_invoice.RfQ_Topic;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicCriteria;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IC_RfQ_TopicDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(C_RfQ_TopicCriteria criteria) throws KTAdempiereException;

    public boolean update(C_RfQ_TopicCriteria c_RfQ_TopicCriteria) throws KTAdempiereException;

    public ArrayList<C_RfQ_TopicDTO> getC_RfQ_Topic(C_RfQ_TopicCriteria c_RfQ_TopicCriteria) throws KTAdempiereException;
}
