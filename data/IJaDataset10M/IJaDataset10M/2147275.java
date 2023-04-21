package org.koossery.adempiere.svco.impl.bpartner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.bpartner.C_BPartnerCriteria;
import org.koossery.adempiere.core.contract.dto.bpartner.C_BPartnerDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.bpartner.IC_BPartnerSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_BPartnerSVCOImplTest extends KTADempiereBaseTest {

    private static IC_BPartnerSVCO cbpartnerSVCOImpl = null;

    private static C_BPartnerDTO newcBPartnerDTO = new C_BPartnerDTO();

    private static C_BPartnerDTO loadcBPartnerDTO = new C_BPartnerDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newcBPartnerDTO.setAcqusitionCost(new BigDecimal(1000));
            newcBPartnerDTO.setActualLifeTimeValue(new BigDecimal(1000));
            newcBPartnerDTO.setAd_Language("en_US");
            newcBPartnerDTO.setC_BP_Group_ID(103);
            newcBPartnerDTO.setDeliveryRule("R");
            newcBPartnerDTO.setDescription("Description" + d.toString());
            newcBPartnerDTO.setDocumentCopies(1000);
            newcBPartnerDTO.setDunningGrace(new Timestamp(1000000));
            newcBPartnerDTO.setFirstSale(new Timestamp(1000000));
            newcBPartnerDTO.setFlatDiscount(new BigDecimal(1000));
            newcBPartnerDTO.setFreightCostRule("I");
            newcBPartnerDTO.setInvoice_PrintFormat_ID(100);
            newcBPartnerDTO.setNaICS("naICS");
            newcBPartnerDTO.setName("C_BPARTNER" + d.toString());
            newcBPartnerDTO.setName2("name2");
            newcBPartnerDTO.setNumberEmployees(1000);
            newcBPartnerDTO.setPotentialLifeTimeValue(new BigDecimal(1000));
            newcBPartnerDTO.setRating("rating");
            newcBPartnerDTO.setReferenceNo("referenceNo");
            newcBPartnerDTO.setSalesVolume(1000);
            newcBPartnerDTO.setShareOfCustomer(1000);
            newcBPartnerDTO.setShelfLifeMinPct(1000);
            newcBPartnerDTO.setSo_CreditLimit(new BigDecimal(1000));
            newcBPartnerDTO.setSo_CreditUsed(new BigDecimal(1000));
            newcBPartnerDTO.setSo_Description("so_Description");
            newcBPartnerDTO.setSoCreditStatus("S");
            newcBPartnerDTO.setTotalOpenBalance(new BigDecimal(1000));
            newcBPartnerDTO.setUrL("urL");
            newcBPartnerDTO.setValue("value" + d);
            newcBPartnerDTO.setIsCustomer("Y");
            newcBPartnerDTO.setIsDiscountPrinted("Y");
            newcBPartnerDTO.setIsEmployee("Y");
            newcBPartnerDTO.setIsOneTime("Y");
            newcBPartnerDTO.setIsProspect("Y");
            newcBPartnerDTO.setIsSalesRep("Y");
            newcBPartnerDTO.setIsSendEMail("Y");
            newcBPartnerDTO.setIsSummary("Y");
            newcBPartnerDTO.setIsTaxExempt("Y");
            newcBPartnerDTO.setIsVendor("Y");
            newcBPartnerDTO.setIsActive("Y");
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
        cbpartnerSVCOImpl = (IC_BPartnerSVCO) finder.get("SVCO/C_BPartner");
    }

    @Test
    public void testcreateC_BPartner() {
        try {
            int c_BPartner_ID = cbpartnerSVCOImpl.createC_BPartner(ctx, newcBPartnerDTO, null);
            assertNotSame("C_BPartner creation failed ID=0", c_BPartner_ID, 0);
            newcBPartnerDTO.setC_BPartner_ID(c_BPartner_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneC_BPartner() {
        try {
            int key = newcBPartnerDTO.getC_BPartner_ID();
            loadcBPartnerDTO = cbpartnerSVCOImpl.findOneC_BPartner(ctx, key);
            assertEquals(loadcBPartnerDTO.getAcqusitionCost(), newcBPartnerDTO.getAcqusitionCost());
            assertEquals(loadcBPartnerDTO.getActualLifeTimeValue(), newcBPartnerDTO.getActualLifeTimeValue());
            assertEquals(loadcBPartnerDTO.getAd_Language(), newcBPartnerDTO.getAd_Language());
            assertEquals(loadcBPartnerDTO.getAd_OrgBP_ID(), newcBPartnerDTO.getAd_OrgBP_ID());
            assertEquals(loadcBPartnerDTO.getBpartner_Parent_ID(), newcBPartnerDTO.getBpartner_Parent_ID());
            assertEquals(loadcBPartnerDTO.getC_BP_Group_ID(), newcBPartnerDTO.getC_BP_Group_ID());
            assertEquals(loadcBPartnerDTO.getC_BPartner_ID(), newcBPartnerDTO.getC_BPartner_ID());
            assertEquals(loadcBPartnerDTO.getC_Dunning_ID(), newcBPartnerDTO.getC_Dunning_ID());
            assertEquals(loadcBPartnerDTO.getC_Greeting_ID(), newcBPartnerDTO.getC_Greeting_ID());
            assertEquals(loadcBPartnerDTO.getC_InvoiceSchedule_ID(), newcBPartnerDTO.getC_InvoiceSchedule_ID());
            assertEquals(loadcBPartnerDTO.getC_PaymentTerm_ID(), newcBPartnerDTO.getC_PaymentTerm_ID());
            assertEquals(loadcBPartnerDTO.getDeliveryRule(), newcBPartnerDTO.getDeliveryRule());
            assertEquals(loadcBPartnerDTO.getDeliveryViaRule(), newcBPartnerDTO.getDeliveryViaRule());
            assertEquals(loadcBPartnerDTO.getDescription(), newcBPartnerDTO.getDescription());
            assertEquals(loadcBPartnerDTO.getDocumentCopies(), newcBPartnerDTO.getDocumentCopies());
            assertEquals(loadcBPartnerDTO.getDunningGrace().toString().substring(0, 10), newcBPartnerDTO.getDunningGrace().toString().substring(0, 10));
            assertEquals(loadcBPartnerDTO.getDuNS(), newcBPartnerDTO.getDuNS());
            assertEquals(loadcBPartnerDTO.getFirstSale().toString().substring(0, 10), newcBPartnerDTO.getFirstSale().toString().substring(0, 10));
            assertEquals(loadcBPartnerDTO.getFlatDiscount(), newcBPartnerDTO.getFlatDiscount());
            assertEquals(loadcBPartnerDTO.getFreightCostRule(), newcBPartnerDTO.getFreightCostRule());
            assertEquals(loadcBPartnerDTO.getInvoice_PrintFormat_ID(), newcBPartnerDTO.getInvoice_PrintFormat_ID());
            assertEquals(loadcBPartnerDTO.getInvoiceRule(), newcBPartnerDTO.getInvoiceRule());
            assertEquals(loadcBPartnerDTO.getM_DiscountSchema_ID(), newcBPartnerDTO.getM_DiscountSchema_ID());
            assertEquals(loadcBPartnerDTO.getM_PriceList_ID(), newcBPartnerDTO.getM_PriceList_ID());
            assertEquals(loadcBPartnerDTO.getNaICS(), newcBPartnerDTO.getNaICS());
            assertEquals(loadcBPartnerDTO.getName(), newcBPartnerDTO.getName());
            assertEquals(loadcBPartnerDTO.getName2(), newcBPartnerDTO.getName2());
            assertEquals(loadcBPartnerDTO.getNumberEmployees(), newcBPartnerDTO.getNumberEmployees());
            assertEquals(loadcBPartnerDTO.getPaymentRule(), newcBPartnerDTO.getPaymentRule());
            assertEquals(loadcBPartnerDTO.getPaymentRulePO(), newcBPartnerDTO.getPaymentRulePO());
            assertEquals(loadcBPartnerDTO.getPo_DiscountSchema_ID(), newcBPartnerDTO.getPo_DiscountSchema_ID());
            assertEquals(loadcBPartnerDTO.getPo_PaymentTerm_ID(), newcBPartnerDTO.getPo_PaymentTerm_ID());
            assertEquals(loadcBPartnerDTO.getPo_PriceList_ID(), newcBPartnerDTO.getPo_PriceList_ID());
            assertEquals(loadcBPartnerDTO.getPoReference(), newcBPartnerDTO.getPoReference());
            assertEquals(loadcBPartnerDTO.getPotentialLifeTimeValue(), newcBPartnerDTO.getPotentialLifeTimeValue());
            assertEquals(loadcBPartnerDTO.getRating(), newcBPartnerDTO.getRating());
            assertEquals(loadcBPartnerDTO.getReferenceNo(), newcBPartnerDTO.getReferenceNo());
            assertEquals(loadcBPartnerDTO.getSalesRep_ID(), newcBPartnerDTO.getSalesRep_ID());
            assertEquals(loadcBPartnerDTO.getSalesVolume(), newcBPartnerDTO.getSalesVolume());
            assertEquals(loadcBPartnerDTO.getShareOfCustomer(), newcBPartnerDTO.getShareOfCustomer());
            assertEquals(loadcBPartnerDTO.getShelfLifeMinPct(), newcBPartnerDTO.getShelfLifeMinPct());
            assertEquals(loadcBPartnerDTO.getSo_CreditLimit(), newcBPartnerDTO.getSo_CreditLimit());
            assertEquals(loadcBPartnerDTO.getSo_CreditUsed(), newcBPartnerDTO.getSo_CreditUsed());
            assertEquals(loadcBPartnerDTO.getSo_Description(), newcBPartnerDTO.getSo_Description());
            assertEquals(loadcBPartnerDTO.getSoCreditStatus(), newcBPartnerDTO.getSoCreditStatus());
            assertEquals(loadcBPartnerDTO.getTaxID(), newcBPartnerDTO.getTaxID());
            assertEquals(loadcBPartnerDTO.getTotalOpenBalance(), newcBPartnerDTO.getTotalOpenBalance());
            assertEquals(loadcBPartnerDTO.getUrL(), newcBPartnerDTO.getUrL());
            assertEquals(loadcBPartnerDTO.getValue(), newcBPartnerDTO.getValue());
            assertEquals(loadcBPartnerDTO.getIsCustomer(), newcBPartnerDTO.getIsCustomer());
            assertEquals(loadcBPartnerDTO.getIsDiscountPrinted(), newcBPartnerDTO.getIsDiscountPrinted());
            assertEquals(loadcBPartnerDTO.getIsEmployee(), newcBPartnerDTO.getIsEmployee());
            assertEquals(loadcBPartnerDTO.getIsOneTime(), newcBPartnerDTO.getIsOneTime());
            assertEquals(loadcBPartnerDTO.getIsProspect(), newcBPartnerDTO.getIsProspect());
            assertEquals(loadcBPartnerDTO.getIsSalesRep(), newcBPartnerDTO.getIsSalesRep());
            assertEquals(loadcBPartnerDTO.getIsSendEMail(), newcBPartnerDTO.getIsSendEMail());
            assertEquals(loadcBPartnerDTO.getIsSummary(), newcBPartnerDTO.getIsSummary());
            assertEquals(loadcBPartnerDTO.getIsTaxExempt(), newcBPartnerDTO.getIsTaxExempt());
            assertEquals(loadcBPartnerDTO.getIsVendor(), newcBPartnerDTO.getIsVendor());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindC_BPartner() {
        ArrayList<C_BPartnerDTO> resultlist;
        try {
            C_BPartnerCriteria criteria = new C_BPartnerCriteria();
            resultlist = cbpartnerSVCOImpl.findC_BPartner(ctx, criteria);
            assertNotNull(resultlist);
            for (C_BPartnerDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_BPartner_ID() + " : Name=" + dto.getName());
                if (dto.getC_BPartner_ID() == newcBPartnerDTO.getC_BPartner_ID()) assertEquals(dto.getName(), newcBPartnerDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateC_BPartner() {
        try {
            newcBPartnerDTO.setName(loadcBPartnerDTO.getName() + "M");
            newcBPartnerDTO.setIsActive("Y");
            cbpartnerSVCOImpl.updateC_BPartner(ctx, newcBPartnerDTO);
            int key = newcBPartnerDTO.getC_BPartner_ID();
            loadcBPartnerDTO = cbpartnerSVCOImpl.findOneC_BPartner(ctx, key);
            assertEquals(loadcBPartnerDTO.getDescription(), newcBPartnerDTO.getDescription());
            assertEquals(loadcBPartnerDTO.getName(), newcBPartnerDTO.getName());
            assertEquals(loadcBPartnerDTO.getC_BPartner_ID(), newcBPartnerDTO.getC_BPartner_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesactiverC_BPartner() {
        try {
            C_BPartnerCriteria criteria = new C_BPartnerCriteria();
            criteria.setC_BPartner_ID(newcBPartnerDTO.getC_BPartner_ID());
            criteria.setIsActive("N");
            cbpartnerSVCOImpl.updateC_BPartner(criteria);
            int key = newcBPartnerDTO.getC_BPartner_ID();
            loadcBPartnerDTO = cbpartnerSVCOImpl.findOneC_BPartner(ctx, key);
            assertEquals(loadcBPartnerDTO.getIsActive(), "N");
            assertEquals(loadcBPartnerDTO.getC_BPartner_ID(), newcBPartnerDTO.getC_BPartner_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
