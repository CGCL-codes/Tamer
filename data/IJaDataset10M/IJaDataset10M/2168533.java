package org.koossery.adempiere.svco.impl.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.invoice.C_InvoiceTaxCriteria;
import org.koossery.adempiere.core.contract.dto.invoice.C_InvoiceTaxDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.invoice.IC_InvoiceTaxSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_InvoiceTaxSVCOImplTest extends KTADempiereBaseTest {

    private IC_InvoiceTaxSVCO cinvoicetaxSVCOImpl;

    private static C_InvoiceTaxDTO newcInvoiceTaxDTO = new C_InvoiceTaxDTO();

    private static C_InvoiceTaxDTO loadcInvoiceTaxDTO = new C_InvoiceTaxDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newcInvoiceTaxDTO.setC_Invoice_ID(1000);
            newcInvoiceTaxDTO.setC_Tax_ID(1000);
            newcInvoiceTaxDTO.setTaxAmt(new BigDecimal(1000));
            newcInvoiceTaxDTO.setTaxBaseAmt(new BigDecimal(1000));
            newcInvoiceTaxDTO.setIsProcessed("Y");
            newcInvoiceTaxDTO.setIsTaxIncluded("Y");
            newcInvoiceTaxDTO.setIsActive("Y");
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        close();
    }

    @Before
    public void setUp() throws Exception {
        cinvoicetaxSVCOImpl = (IC_InvoiceTaxSVCO) finder.get("SVCO/C_InvoiceTax");
    }

    @Test
    public void testCreerC_InvoiceTax() {
        try {
            int c_InvoiceTax_ID = cinvoicetaxSVCOImpl.createC_InvoiceTax(ctx, newcInvoiceTaxDTO, null);
            assertNotSame("C_InvoiceTax creation failed ID=0", c_InvoiceTax_ID, 0);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerC_InvoiceTax() {
        try {
            int key1 = newcInvoiceTaxDTO.getC_Invoice_ID();
            int key2 = newcInvoiceTaxDTO.getC_Tax_ID();
            loadcInvoiceTaxDTO = cinvoicetaxSVCOImpl.findOneC_InvoiceTax(ctx, key1, key2);
            assertEquals(loadcInvoiceTaxDTO.getC_Invoice_ID(), newcInvoiceTaxDTO.getC_Invoice_ID());
            assertEquals(loadcInvoiceTaxDTO.getC_Tax_ID(), newcInvoiceTaxDTO.getC_Tax_ID());
            assertEquals(loadcInvoiceTaxDTO.getTaxAmt(), newcInvoiceTaxDTO.getTaxAmt());
            assertEquals(loadcInvoiceTaxDTO.getTaxBaseAmt(), newcInvoiceTaxDTO.getTaxBaseAmt());
            assertEquals(loadcInvoiceTaxDTO.getIsProcessed(), newcInvoiceTaxDTO.getIsProcessed());
            assertEquals(loadcInvoiceTaxDTO.getIsTaxIncluded(), newcInvoiceTaxDTO.getIsTaxIncluded());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherC_InvoiceTax() {
        ArrayList<C_InvoiceTaxDTO> resultlist;
        try {
            C_InvoiceTaxCriteria criteria = new C_InvoiceTaxCriteria();
            resultlist = cinvoicetaxSVCOImpl.findC_InvoiceTax(ctx, criteria);
            assertNotNull(resultlist);
            for (C_InvoiceTaxDTO dto : resultlist) {
                System.out.println("ID1=" + dto.getC_Invoice_ID() + " : Name1=" + dto.getIsProcessed());
                System.out.println("ID2=" + dto.getC_Tax_ID() + " : Name2=" + dto.getIsProcessed());
                if (dto.getC_Invoice_ID() == newcInvoiceTaxDTO.getC_Invoice_ID() && dto.getC_Tax_ID() == newcInvoiceTaxDTO.getC_Tax_ID()) assertEquals(dto.getIsTaxIncluded(), newcInvoiceTaxDTO.getIsTaxIncluded());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierC_InvoiceTax() {
        try {
            newcInvoiceTaxDTO.setIsActive(loadcInvoiceTaxDTO.getIsActive() + "M");
            newcInvoiceTaxDTO.setIsActive("Y");
            cinvoicetaxSVCOImpl.updateC_InvoiceTax(ctx, newcInvoiceTaxDTO);
            int key1 = newcInvoiceTaxDTO.getC_Invoice_ID();
            int key2 = newcInvoiceTaxDTO.getC_Tax_ID();
            loadcInvoiceTaxDTO = cinvoicetaxSVCOImpl.findOneC_InvoiceTax(ctx, key1, key2);
            assertEquals(loadcInvoiceTaxDTO.getIsActive(), newcInvoiceTaxDTO.getIsActive());
            assertEquals(loadcInvoiceTaxDTO.getIsProcessed(), newcInvoiceTaxDTO.getIsProcessed());
            assertEquals(loadcInvoiceTaxDTO.getC_Invoice_ID(), newcInvoiceTaxDTO.getC_Invoice_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerC_InvoiceTax() {
        try {
            C_InvoiceTaxCriteria criteria = new C_InvoiceTaxCriteria();
            criteria.setC_Invoice_ID(newcInvoiceTaxDTO.getC_Invoice_ID());
            criteria.setC_Tax_ID(newcInvoiceTaxDTO.getC_Tax_ID());
            boolean deleted = cinvoicetaxSVCOImpl.deleteC_InvoiceTax(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
