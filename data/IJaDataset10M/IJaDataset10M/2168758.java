package org.koossery.adempiere.core.contract.interfaces.Requisition_to_invoice.RfQ;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.RfQ.C_RfQLineQtyCriteria;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ.C_RfQLineQtyDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IC_RfQLineQtySVCO extends IKTADempiereServiceComposed {

    public int createC_RfQLineQty(Properties ctx, C_RfQLineQtyDTO c_RfQLineQtyDTO, String trxname) throws KTAdempiereException;

    public C_RfQLineQtyDTO findOneC_RfQLineQty(Properties ctx, int c_RfQLineQtyID) throws KTAdempiereException;

    public ArrayList<C_RfQLineQtyDTO> findC_RfQLineQty(Properties ctx, C_RfQLineQtyCriteria c_RfQLineQtyCriteria) throws KTAdempiereException;

    public void updateC_RfQLineQty(Properties ctx, C_RfQLineQtyDTO c_RfQLineQtyDTO) throws KTAdempiereException;

    public boolean deleteC_RfQLineQty(Properties ctx, C_RfQLineQtyCriteria criteria) throws KTAdempiereException;
}
