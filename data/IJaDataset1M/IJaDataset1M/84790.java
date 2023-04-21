package org.koossery.adempiere.core.backend.interfaces.dao.invoice;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.invoice.C_InvoiceBatchLineCriteria;
import org.koossery.adempiere.core.contract.dto.invoice.C_InvoiceBatchLineDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IC_InvoiceBatchLineDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(C_InvoiceBatchLineCriteria criteria) throws KTAdempiereException;

    public boolean update(C_InvoiceBatchLineCriteria c_InvoiceBatchLineCriteria) throws KTAdempiereException;

    public ArrayList<C_InvoiceBatchLineDTO> getC_InvoiceBatchLine(C_InvoiceBatchLineCriteria c_InvoiceBatchLineCriteria) throws KTAdempiereException;
}
