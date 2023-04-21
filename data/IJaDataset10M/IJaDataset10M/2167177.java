package org.koossery.adempiere.core.contract.interfaces.invoice;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.invoice.I_InvoiceCriteria;
import org.koossery.adempiere.core.contract.dto.invoice.I_InvoiceDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface II_InvoiceSVCO extends IKTADempiereServiceComposed {

    public int createI_Invoice(Properties ctx, I_InvoiceDTO i_InvoiceDTO, String trxname) throws KTAdempiereException;

    public I_InvoiceDTO findOneI_Invoice(Properties ctx, int i_InvoiceID) throws KTAdempiereException;

    public ArrayList<I_InvoiceDTO> findI_Invoice(Properties ctx, I_InvoiceCriteria i_InvoiceCriteria) throws KTAdempiereException;

    public void updateI_Invoice(Properties ctx, I_InvoiceDTO i_InvoiceDTO) throws KTAdempiereException;

    public boolean deleteI_Invoice(Properties ctx, I_InvoiceCriteria criteria) throws KTAdempiereException;
}
