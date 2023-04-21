package org.koossery.adempiere.svco.impl.generated;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.generated.C_Conversion_RateCriteria;
import org.koossery.adempiere.core.contract.dto.generated.C_Conversion_RateDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_Conversion_RateSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_Conversion_RateSVCOImplTest extends KTADempiereBaseTest {

    private static IC_Conversion_RateSVCO cconversionrateSVCOImpl = null;

    private static C_Conversion_RateDTO newcConversionRateDTO = new C_Conversion_RateDTO();

    private static C_Conversion_RateDTO loadcConversionRateDTO = new C_Conversion_RateDTO();

    static {
        try {
            init();
            cconversionrateSVCOImpl = (IC_Conversion_RateSVCO) finder.get("SVCO/C_Conversion_Rate");
            newcConversionRateDTO.setC_ConversionType_ID(1000000);
            newcConversionRateDTO.setC_Currency_ID(214);
            newcConversionRateDTO.setC_Currency_ID_To(1000000);
            newcConversionRateDTO.setDivideRate(new BigDecimal(0.25));
            newcConversionRateDTO.setMultiplyRate(new BigDecimal(4.00));
            newcConversionRateDTO.setValidFrom(new Timestamp(1000000));
            newcConversionRateDTO.setValidTo(new Timestamp(1000000));
            newcConversionRateDTO.setIsActive("Y");
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
    public void testcreateC_Conversion_Rate() {
        try {
            int c_Conversion_Rate_ID = cconversionrateSVCOImpl.createC_Conversion_Rate(ctx, newcConversionRateDTO, null);
            assertNotSame("C_Conversion_Rate creation failed ID=0", c_Conversion_Rate_ID, 0);
            newcConversionRateDTO.setC_Conversion_Rate_ID(c_Conversion_Rate_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneC_Conversion_Rate() {
        try {
            int key = newcConversionRateDTO.getC_Conversion_Rate_ID();
            loadcConversionRateDTO = cconversionrateSVCOImpl.findOneC_Conversion_Rate(ctx, key);
            assertEquals(loadcConversionRateDTO.getC_Conversion_Rate_ID(), newcConversionRateDTO.getC_Conversion_Rate_ID());
            assertEquals(loadcConversionRateDTO.getC_ConversionType_ID(), newcConversionRateDTO.getC_ConversionType_ID());
            assertEquals(loadcConversionRateDTO.getC_Currency_ID(), newcConversionRateDTO.getC_Currency_ID());
            assertEquals(loadcConversionRateDTO.getC_Currency_ID_To(), newcConversionRateDTO.getC_Currency_ID_To());
            assertEquals(loadcConversionRateDTO.getDivideRate(), newcConversionRateDTO.getDivideRate());
            assertEquals(loadcConversionRateDTO.getMultiplyRate(), newcConversionRateDTO.getMultiplyRate());
            assertEquals(loadcConversionRateDTO.getValidFrom().toString().substring(0, 10), newcConversionRateDTO.getValidFrom().toString().substring(0, 10));
            assertEquals(loadcConversionRateDTO.getValidTo().toString().substring(0, 10), newcConversionRateDTO.getValidTo().toString().substring(0, 10));
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindC_Conversion_Rate() {
        ArrayList<C_Conversion_RateDTO> resultlist;
        try {
            C_Conversion_RateCriteria criteria = new C_Conversion_RateCriteria();
            resultlist = cconversionrateSVCOImpl.findC_Conversion_Rate(ctx, criteria);
            assertNotNull(resultlist);
            for (C_Conversion_RateDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_Conversion_Rate_ID() + " : Rate=" + dto.getMultiplyRate());
                if (dto.getC_Conversion_Rate_ID() == newcConversionRateDTO.getC_Conversion_Rate_ID()) assertEquals(dto.getC_Currency_ID_To(), newcConversionRateDTO.getC_Currency_ID_To());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateC_Conversion_Rate() {
        try {
            newcConversionRateDTO.setMultiplyRate(new BigDecimal(5));
            newcConversionRateDTO.setIsActive("Y");
            cconversionrateSVCOImpl.updateC_Conversion_Rate(ctx, newcConversionRateDTO);
            int key = newcConversionRateDTO.getC_Conversion_Rate_ID();
            loadcConversionRateDTO = cconversionrateSVCOImpl.findOneC_Conversion_Rate(ctx, key);
            assertEquals(loadcConversionRateDTO.getMultiplyRate(), newcConversionRateDTO.getMultiplyRate());
            assertEquals(loadcConversionRateDTO.getC_Conversion_Rate_ID(), newcConversionRateDTO.getC_Conversion_Rate_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesactiverC_Conversion_Rate() {
        try {
            C_Conversion_RateCriteria criteria = new C_Conversion_RateCriteria();
            criteria.setC_Conversion_Rate_ID(newcConversionRateDTO.getC_Conversion_Rate_ID());
            criteria.setIsActive("N");
            cconversionrateSVCOImpl.updateC_Conversion_Rate(criteria);
            int key = newcConversionRateDTO.getC_Conversion_Rate_ID();
            loadcConversionRateDTO = cconversionrateSVCOImpl.findOneC_Conversion_Rate(ctx, key);
            assertEquals(loadcConversionRateDTO.getIsActive(), "N");
            assertEquals(loadcConversionRateDTO.getC_Conversion_Rate_ID(), newcConversionRateDTO.getC_Conversion_Rate_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
