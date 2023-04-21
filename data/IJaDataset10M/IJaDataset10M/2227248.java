package org.koossery.adempiere.svco.impl.org.cash;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.org.cash.C_CashCriteria;
import org.koossery.adempiere.core.contract.dto.org.cash.C_CashDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.org.cash.IC_CashSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_CashSVCOImplTest extends KTADempiereBaseTest {

    private IC_CashSVCO ccashSVCOImpl;

    private static C_CashDTO newcCashDTO = new C_CashDTO();

    private static C_CashDTO loadcCashDTO = new C_CashDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newcCashDTO.setAd_OrgTrx_ID(1000);
            newcCashDTO.setBeginningBalance(new BigDecimal(1000));
            newcCashDTO.setC_Activity_ID(1000);
            newcCashDTO.setC_Campaign_ID(1000);
            newcCashDTO.setC_Cash_ID(1000);
            newcCashDTO.setC_CashBook_ID(1000);
            newcCashDTO.setC_Project_ID(1000);
            newcCashDTO.setDateAcct(new Timestamp(1000000));
            newcCashDTO.setDescription("Description" + d.toString());
            newcCashDTO.setDocAction("docAction");
            newcCashDTO.setDocStatus("docStatus");
            newcCashDTO.setEndingBalance(new BigDecimal(1000));
            newcCashDTO.setName("C_CASH" + d.toString());
            newcCashDTO.setStatementDate(new Timestamp(1000000));
            newcCashDTO.setStatementDifference(new BigDecimal(1000));
            newcCashDTO.setUser1_ID(1000);
            newcCashDTO.setUser2_ID(1000);
            newcCashDTO.setIsApproved("Y");
            newcCashDTO.setIsPosted("Y");
            newcCashDTO.setIsProcessed("Y");
            newcCashDTO.setIsProcessing("Y");
            newcCashDTO.setIsActive("Y");
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
        ccashSVCOImpl = (IC_CashSVCO) finder.get("SVCO/C_Cash");
    }

    @Test
    public void testCreerC_Cash() {
        try {
            int c_Cash_ID = ccashSVCOImpl.createC_Cash(ctx, newcCashDTO, null);
            assertNotSame("C_Cash creation failed ID=0", c_Cash_ID, 0);
            newcCashDTO.setC_Cash_ID(c_Cash_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerC_Cash() {
        try {
            int key = newcCashDTO.getC_Cash_ID();
            loadcCashDTO = ccashSVCOImpl.findOneC_Cash(ctx, key);
            assertEquals(loadcCashDTO.getAd_OrgTrx_ID(), newcCashDTO.getAd_OrgTrx_ID());
            assertEquals(loadcCashDTO.getBeginningBalance(), newcCashDTO.getBeginningBalance());
            assertEquals(loadcCashDTO.getC_Activity_ID(), newcCashDTO.getC_Activity_ID());
            assertEquals(loadcCashDTO.getC_Campaign_ID(), newcCashDTO.getC_Campaign_ID());
            assertEquals(loadcCashDTO.getC_Cash_ID(), newcCashDTO.getC_Cash_ID());
            assertEquals(loadcCashDTO.getC_CashBook_ID(), newcCashDTO.getC_CashBook_ID());
            assertEquals(loadcCashDTO.getC_Project_ID(), newcCashDTO.getC_Project_ID());
            assertEquals(loadcCashDTO.getDateAcct(), newcCashDTO.getDateAcct());
            assertEquals(loadcCashDTO.getDescription(), newcCashDTO.getDescription());
            assertEquals(loadcCashDTO.getDocAction(), newcCashDTO.getDocAction());
            assertEquals(loadcCashDTO.getDocStatus(), newcCashDTO.getDocStatus());
            assertEquals(loadcCashDTO.getEndingBalance(), newcCashDTO.getEndingBalance());
            assertEquals(loadcCashDTO.getName(), newcCashDTO.getName());
            assertEquals(loadcCashDTO.getStatementDate(), newcCashDTO.getStatementDate());
            assertEquals(loadcCashDTO.getStatementDifference(), newcCashDTO.getStatementDifference());
            assertEquals(loadcCashDTO.getUser1_ID(), newcCashDTO.getUser1_ID());
            assertEquals(loadcCashDTO.getUser2_ID(), newcCashDTO.getUser2_ID());
            assertEquals(loadcCashDTO.getIsApproved(), newcCashDTO.getIsApproved());
            assertEquals(loadcCashDTO.getIsPosted(), newcCashDTO.getIsPosted());
            assertEquals(loadcCashDTO.getIsProcessed(), newcCashDTO.getIsProcessed());
            assertEquals(loadcCashDTO.getIsProcessing(), newcCashDTO.getIsProcessing());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherC_Cash() {
        ArrayList<C_CashDTO> resultlist;
        try {
            C_CashCriteria criteria = new C_CashCriteria();
            resultlist = ccashSVCOImpl.findC_Cash(ctx, criteria);
            assertNotNull(resultlist);
            for (C_CashDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_Cash_ID() + " : Name=" + dto.getName());
                if (dto.getC_Cash_ID() == newcCashDTO.getC_Cash_ID()) assertEquals(dto.getName(), newcCashDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierC_Cash() {
        try {
            newcCashDTO.setName(loadcCashDTO.getName() + "M");
            newcCashDTO.setIsActive("Y");
            ccashSVCOImpl.updateC_Cash(ctx, newcCashDTO);
            int key = newcCashDTO.getC_Cash_ID();
            loadcCashDTO = ccashSVCOImpl.findOneC_Cash(ctx, key);
            assertEquals(loadcCashDTO.getDescription(), newcCashDTO.getDescription());
            assertEquals(loadcCashDTO.getName(), newcCashDTO.getName());
            assertEquals(loadcCashDTO.getC_Cash_ID(), newcCashDTO.getC_Cash_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerC_Cash() {
        try {
            C_CashCriteria criteria = new C_CashCriteria();
            criteria.setC_Cash_ID(newcCashDTO.getC_Cash_ID());
            boolean deleted = ccashSVCOImpl.deleteC_Cash(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
