package org.koossery.adempiere.svco.impl.payroll;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.payroll.HR_PayrollConceptCriteria;
import org.koossery.adempiere.core.contract.dto.payroll.HR_PayrollConceptDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.payroll.IHR_PayrollConceptSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class HR_PayrollConceptSVCOImplTest extends KTADempiereBaseTest {

    private static IHR_PayrollConceptSVCO hrpayrollconceptSVCOImpl = null;

    private static Date d = new Date();

    private static HR_PayrollConceptDTO newhRPayrollConceptDTO = new HR_PayrollConceptDTO();

    private static HR_PayrollConceptDTO loadhRPayrollConceptDTO = new HR_PayrollConceptDTO();

    static {
        try {
            init();
            hrpayrollconceptSVCOImpl = (IHR_PayrollConceptSVCO) finder.get("SVCO/HR_PayrollConcept");
            newhRPayrollConceptDTO.setHr_Concept_ID(1000000);
            newhRPayrollConceptDTO.setHr_Payroll_ID(1000000);
            newhRPayrollConceptDTO.setName("HR_PAYROLLCONCEPT" + d.toString());
            newhRPayrollConceptDTO.setSeqNo(1000);
            newhRPayrollConceptDTO.setIsDisplayed("Y");
            newhRPayrollConceptDTO.setIsInclude("Y");
            newhRPayrollConceptDTO.setIsPrinted("Y");
            newhRPayrollConceptDTO.setIsActive("Y");
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
    public void testcreateHR_PayrollConcept() {
        try {
            int hr_PayrollConcept_ID = hrpayrollconceptSVCOImpl.createHR_PayrollConcept(ctx, newhRPayrollConceptDTO, null);
            assertNotSame("HR_PayrollConcept creation failed ID=0", hr_PayrollConcept_ID, 0);
            newhRPayrollConceptDTO.setHr_PayrollConcept_ID(hr_PayrollConcept_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneHR_PayrollConcept() {
        try {
            int key = newhRPayrollConceptDTO.getHr_PayrollConcept_ID();
            loadhRPayrollConceptDTO = hrpayrollconceptSVCOImpl.findOneHR_PayrollConcept(ctx, key);
            assertEquals(loadhRPayrollConceptDTO.getAd_Rule_Engine_ID(), newhRPayrollConceptDTO.getAd_Rule_Engine_ID());
            assertEquals(loadhRPayrollConceptDTO.getHr_Concept_ID(), newhRPayrollConceptDTO.getHr_Concept_ID());
            assertEquals(loadhRPayrollConceptDTO.getHr_Payroll_ID(), newhRPayrollConceptDTO.getHr_Payroll_ID());
            assertEquals(loadhRPayrollConceptDTO.getHr_PayrollConcept_ID(), newhRPayrollConceptDTO.getHr_PayrollConcept_ID());
            assertEquals(loadhRPayrollConceptDTO.getName(), newhRPayrollConceptDTO.getName());
            assertEquals(loadhRPayrollConceptDTO.getSeqNo(), newhRPayrollConceptDTO.getSeqNo());
            assertEquals(loadhRPayrollConceptDTO.getIsDisplayed(), newhRPayrollConceptDTO.getIsDisplayed());
            assertEquals(loadhRPayrollConceptDTO.getIsInclude(), newhRPayrollConceptDTO.getIsInclude());
            assertEquals(loadhRPayrollConceptDTO.getIsPrinted(), newhRPayrollConceptDTO.getIsPrinted());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindHR_PayrollConcept() {
        ArrayList<HR_PayrollConceptDTO> resultlist;
        try {
            HR_PayrollConceptCriteria criteria = new HR_PayrollConceptCriteria();
            resultlist = hrpayrollconceptSVCOImpl.findHR_PayrollConcept(ctx, criteria);
            assertNotNull(resultlist);
            for (HR_PayrollConceptDTO dto : resultlist) {
                System.out.println("ID=" + dto.getHr_PayrollConcept_ID() + " : Name=" + dto.getName());
                if (dto.getHr_PayrollConcept_ID() == newhRPayrollConceptDTO.getHr_PayrollConcept_ID()) assertEquals(dto.getName(), newhRPayrollConceptDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateHR_PayrollConcept() {
        try {
            newhRPayrollConceptDTO.setName(loadhRPayrollConceptDTO.getName() + "M");
            newhRPayrollConceptDTO.setIsActive("Y");
            hrpayrollconceptSVCOImpl.updateHR_PayrollConcept(ctx, newhRPayrollConceptDTO);
            int key = newhRPayrollConceptDTO.getHr_PayrollConcept_ID();
            loadhRPayrollConceptDTO = hrpayrollconceptSVCOImpl.findOneHR_PayrollConcept(ctx, key);
            assertEquals(loadhRPayrollConceptDTO.getHr_Concept_ID(), newhRPayrollConceptDTO.getHr_Concept_ID());
            assertEquals(loadhRPayrollConceptDTO.getName() + "M", newhRPayrollConceptDTO.getName());
            assertEquals(loadhRPayrollConceptDTO.getHr_PayrollConcept_ID(), newhRPayrollConceptDTO.getHr_PayrollConcept_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesactiverHR_PayrollConcept() {
        try {
            HR_PayrollConceptCriteria criteria = new HR_PayrollConceptCriteria();
            criteria.setHr_PayrollConcept_ID(newhRPayrollConceptDTO.getHr_PayrollConcept_ID());
            criteria.setIsActive("N");
            hrpayrollconceptSVCOImpl.updateHR_PayrollConcept(criteria);
            int key = newhRPayrollConceptDTO.getHr_PayrollConcept_ID();
            loadhRPayrollConceptDTO = hrpayrollconceptSVCOImpl.findOneHR_PayrollConcept(ctx, key);
            assertEquals(loadhRPayrollConceptDTO.getIsActive(), "N");
            assertEquals(loadhRPayrollConceptDTO.getHr_PayrollConcept_ID(), newhRPayrollConceptDTO.getHr_PayrollConcept_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
