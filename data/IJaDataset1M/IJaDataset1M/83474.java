package org.koossery.adempiere.core.backend.interfaces.dao.Requisition_to_invoice.RfQ;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.RfQ.C_RfQLineCriteria;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ.C_RfQLineDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IC_RfQLineDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(C_RfQLineCriteria criteria) throws KTAdempiereException;

    public boolean update(C_RfQLineCriteria c_RfQLineCriteria) throws KTAdempiereException;

    public ArrayList<C_RfQLineDTO> getC_RfQLine(C_RfQLineCriteria c_RfQLineCriteria) throws KTAdempiereException;
}
