package org.koossery.adempiere.svco.impl.generated;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.generated.C_AcctSchemaCriteria;
import org.koossery.adempiere.core.contract.dto.generated.C_AcctSchemaDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_AcctSchemaSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_AcctSchemaSVCOImplTest extends KTADempiereBaseTest {

    private static IC_AcctSchemaSVCO cacctschemaSVCOImpl = null;

    private static C_AcctSchemaDTO newcAcctSchemaDTO = new C_AcctSchemaDTO();

    private static C_AcctSchemaDTO loadcAcctSchemaDTO = new C_AcctSchemaDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            cacctschemaSVCOImpl = (IC_AcctSchemaSVCO) finder.get("SVCO/C_AcctSchema");
            newcAcctSchemaDTO.setC_Currency_ID(214);
            newcAcctSchemaDTO.setCommitmentType("C");
            newcAcctSchemaDTO.setCostingLevel("C");
            newcAcctSchemaDTO.setCostingMethod("S");
            newcAcctSchemaDTO.setDescription("P Comptable Test CFA");
            newcAcctSchemaDTO.setGaAP("XX");
            newcAcctSchemaDTO.setM_CostType_ID(100);
            newcAcctSchemaDTO.setName("C_ACCTSCHEMA" + d.toString());
            newcAcctSchemaDTO.setPeriod_OpenFuture(100);
            newcAcctSchemaDTO.setPeriod_OpenHistory(100);
            newcAcctSchemaDTO.setSeparator("-");
            newcAcctSchemaDTO.setTaxCorrectionType("N");
            newcAcctSchemaDTO.setIsAccrual("Y");
            newcAcctSchemaDTO.setIsAdjustCOGS("Y");
            newcAcctSchemaDTO.setIsAllowNegativePosting("Y");
            newcAcctSchemaDTO.setIsAutoPeriodControl("N");
            newcAcctSchemaDTO.setIsDiscountCorrectsTax("Y");
            newcAcctSchemaDTO.setIsExplicitCostAdjustment("N");
            newcAcctSchemaDTO.setIsHasAlias("Y");
            newcAcctSchemaDTO.setIsHasCombination("Y");
            newcAcctSchemaDTO.setIsPostIfClearingEqual("Y");
            newcAcctSchemaDTO.setIsPostServices("N");
            newcAcctSchemaDTO.setIsTradeDiscountPosted("N");
            newcAcctSchemaDTO.setIsActive("Y");
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
    }

    @Test
    public void testcreateC_AcctSchema() {
        try {
            int c_AcctSchema_ID = cacctschemaSVCOImpl.createC_AcctSchema(ctx, newcAcctSchemaDTO, null);
            assertNotSame("C_AcctSchema creation failed ID=0", c_AcctSchema_ID, 0);
            newcAcctSchemaDTO.setC_AcctSchema_ID(c_AcctSchema_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneC_AcctSchema() {
        try {
            int key = newcAcctSchemaDTO.getC_AcctSchema_ID();
            loadcAcctSchemaDTO = cacctschemaSVCOImpl.findOneC_AcctSchema(ctx, key);
            assertEquals(loadcAcctSchemaDTO.getAd_OrgOnly_ID(), newcAcctSchemaDTO.getAd_OrgOnly_ID());
            assertEquals(loadcAcctSchemaDTO.getC_AcctSchema_ID(), newcAcctSchemaDTO.getC_AcctSchema_ID());
            assertEquals(loadcAcctSchemaDTO.getC_Currency_ID(), newcAcctSchemaDTO.getC_Currency_ID());
            assertEquals(loadcAcctSchemaDTO.getC_Period_ID(), newcAcctSchemaDTO.getC_Period_ID());
            assertEquals(loadcAcctSchemaDTO.getCommitmentType(), newcAcctSchemaDTO.getCommitmentType());
            assertEquals(loadcAcctSchemaDTO.getCostingLevel(), newcAcctSchemaDTO.getCostingLevel());
            assertEquals(loadcAcctSchemaDTO.getCostingMethod(), newcAcctSchemaDTO.getCostingMethod());
            assertEquals(loadcAcctSchemaDTO.getDescription(), newcAcctSchemaDTO.getDescription());
            assertEquals(loadcAcctSchemaDTO.getGaAP(), newcAcctSchemaDTO.getGaAP());
            assertEquals(loadcAcctSchemaDTO.getM_CostType_ID(), newcAcctSchemaDTO.getM_CostType_ID());
            assertEquals(loadcAcctSchemaDTO.getName(), newcAcctSchemaDTO.getName());
            assertEquals(loadcAcctSchemaDTO.getPeriod_OpenFuture(), newcAcctSchemaDTO.getPeriod_OpenFuture());
            assertEquals(loadcAcctSchemaDTO.getPeriod_OpenHistory(), newcAcctSchemaDTO.getPeriod_OpenHistory());
            assertEquals(loadcAcctSchemaDTO.getSeparator(), newcAcctSchemaDTO.getSeparator());
            assertEquals(loadcAcctSchemaDTO.getTaxCorrectionType(), newcAcctSchemaDTO.getTaxCorrectionType());
            assertEquals(loadcAcctSchemaDTO.getIsAccrual(), newcAcctSchemaDTO.getIsAccrual());
            assertEquals(loadcAcctSchemaDTO.getIsAdjustCOGS(), newcAcctSchemaDTO.getIsAdjustCOGS());
            assertEquals(loadcAcctSchemaDTO.getIsAllowNegativePosting(), newcAcctSchemaDTO.getIsAllowNegativePosting());
            assertEquals(loadcAcctSchemaDTO.getIsDiscountCorrectsTax(), newcAcctSchemaDTO.getIsDiscountCorrectsTax());
            assertEquals(loadcAcctSchemaDTO.getIsExplicitCostAdjustment(), newcAcctSchemaDTO.getIsExplicitCostAdjustment());
            assertEquals(loadcAcctSchemaDTO.getIsPostIfClearingEqual(), newcAcctSchemaDTO.getIsPostIfClearingEqual());
            assertEquals(loadcAcctSchemaDTO.getIsPostServices(), newcAcctSchemaDTO.getIsPostServices());
            assertEquals(loadcAcctSchemaDTO.getIsTradeDiscountPosted(), newcAcctSchemaDTO.getIsTradeDiscountPosted());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindC_AcctSchema() {
        ArrayList<C_AcctSchemaDTO> resultlist;
        try {
            C_AcctSchemaCriteria criteria = new C_AcctSchemaCriteria();
            resultlist = cacctschemaSVCOImpl.findC_AcctSchema(ctx, criteria);
            assertNotNull(resultlist);
            for (C_AcctSchemaDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_AcctSchema_ID() + " : Name=" + dto.getName());
                if (dto.getC_AcctSchema_ID() == newcAcctSchemaDTO.getC_AcctSchema_ID()) assertEquals(dto.getName(), newcAcctSchemaDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateC_AcctSchema() {
        try {
            newcAcctSchemaDTO.setName(loadcAcctSchemaDTO.getName() + "M");
            newcAcctSchemaDTO.setIsActive("Y");
            cacctschemaSVCOImpl.updateC_AcctSchema(ctx, newcAcctSchemaDTO);
            int key = newcAcctSchemaDTO.getC_AcctSchema_ID();
            loadcAcctSchemaDTO = cacctschemaSVCOImpl.findOneC_AcctSchema(ctx, key);
            assertEquals(loadcAcctSchemaDTO.getDescription(), newcAcctSchemaDTO.getDescription());
            assertEquals(loadcAcctSchemaDTO.getName() + "M", newcAcctSchemaDTO.getName());
            assertEquals(loadcAcctSchemaDTO.getC_AcctSchema_ID(), newcAcctSchemaDTO.getC_AcctSchema_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesactiverC_AcctSchema() {
        try {
            C_AcctSchemaCriteria criteria = new C_AcctSchemaCriteria();
            criteria.setC_AcctSchema_ID(newcAcctSchemaDTO.getC_AcctSchema_ID());
            criteria.setIsActive("N");
            cacctschemaSVCOImpl.updateC_AcctSchema(criteria);
            int key = newcAcctSchemaDTO.getC_AcctSchema_ID();
            loadcAcctSchemaDTO = cacctschemaSVCOImpl.findOneC_AcctSchema(ctx, key);
            assertEquals(loadcAcctSchemaDTO.getIsActive(), "N");
            assertEquals(loadcAcctSchemaDTO.getC_AcctSchema_ID(), newcAcctSchemaDTO.getC_AcctSchema_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
