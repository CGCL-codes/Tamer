package org.koossery.adempiere.svco.impl.pa;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.pa.PA_GoalRestrictionCriteria;
import org.koossery.adempiere.core.contract.dto.pa.PA_GoalRestrictionDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.pa.IPA_GoalRestrictionSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class PA_GoalRestrictionSVCOImplTest extends KTADempiereBaseTest {

    private static IPA_GoalRestrictionSVCO pagoalrestrictionSVCOImpl = null;

    private static PA_GoalRestrictionDTO newpAGoalRestrictionDTO = new PA_GoalRestrictionDTO();

    private static PA_GoalRestrictionDTO loadpAGoalRestrictionDTO = new PA_GoalRestrictionDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            pagoalrestrictionSVCOImpl = (IPA_GoalRestrictionSVCO) finder.get("SVCO/PA_GoalRestriction");
            newpAGoalRestrictionDTO.setGoalRestrictionType("G");
            newpAGoalRestrictionDTO.setName("PA_GOALRESTRICTION" + d.toString());
            newpAGoalRestrictionDTO.setOrg_ID(11);
            newpAGoalRestrictionDTO.setPa_Goal_ID(101);
            newpAGoalRestrictionDTO.setIsActive("Y");
            newpAGoalRestrictionDTO.setC_BP_Group_ID(103);
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
    public void testcreatePA_GoalRestriction() {
        try {
            int pa_GoalRestriction_ID = pagoalrestrictionSVCOImpl.createPA_GoalRestriction(ctx, newpAGoalRestrictionDTO, null);
            assertNotSame("PA_GoalRestriction creation failed ID=0", pa_GoalRestriction_ID, 0);
            newpAGoalRestrictionDTO.setPa_GoalRestriction_ID(pa_GoalRestriction_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOnePA_GoalRestriction() {
        try {
            int key = newpAGoalRestrictionDTO.getPa_GoalRestriction_ID();
            loadpAGoalRestrictionDTO = pagoalrestrictionSVCOImpl.findOnePA_GoalRestriction(ctx, key);
            assertEquals(loadpAGoalRestrictionDTO.getC_BP_Group_ID(), newpAGoalRestrictionDTO.getC_BP_Group_ID());
            assertEquals(loadpAGoalRestrictionDTO.getC_BPartner_ID(), newpAGoalRestrictionDTO.getC_BPartner_ID());
            assertEquals(loadpAGoalRestrictionDTO.getGoalRestrictionType(), newpAGoalRestrictionDTO.getGoalRestrictionType());
            assertEquals(loadpAGoalRestrictionDTO.getM_Product_Category_ID(), newpAGoalRestrictionDTO.getM_Product_Category_ID());
            assertEquals(loadpAGoalRestrictionDTO.getM_Product_ID(), newpAGoalRestrictionDTO.getM_Product_ID());
            assertEquals(loadpAGoalRestrictionDTO.getName(), newpAGoalRestrictionDTO.getName());
            assertEquals(loadpAGoalRestrictionDTO.getOrg_ID(), newpAGoalRestrictionDTO.getOrg_ID());
            assertEquals(loadpAGoalRestrictionDTO.getPa_Goal_ID(), newpAGoalRestrictionDTO.getPa_Goal_ID());
            assertEquals(loadpAGoalRestrictionDTO.getPa_GoalRestriction_ID(), newpAGoalRestrictionDTO.getPa_GoalRestriction_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindPA_GoalRestriction() {
        ArrayList<PA_GoalRestrictionDTO> resultlist;
        try {
            PA_GoalRestrictionCriteria criteria = new PA_GoalRestrictionCriteria();
            resultlist = pagoalrestrictionSVCOImpl.findPA_GoalRestriction(ctx, criteria);
            assertNotNull(resultlist);
            for (PA_GoalRestrictionDTO dto : resultlist) {
                System.out.println("ID=" + dto.getPa_GoalRestriction_ID() + " : Name=" + dto.getName());
                if (dto.getPa_GoalRestriction_ID() == newpAGoalRestrictionDTO.getPa_GoalRestriction_ID()) assertEquals(dto.getName(), newpAGoalRestrictionDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdatePA_GoalRestriction() {
        try {
            newpAGoalRestrictionDTO.setName(loadpAGoalRestrictionDTO.getName() + "M");
            newpAGoalRestrictionDTO.setIsActive("Y");
            pagoalrestrictionSVCOImpl.updatePA_GoalRestriction(ctx, newpAGoalRestrictionDTO);
            int key = newpAGoalRestrictionDTO.getPa_GoalRestriction_ID();
            loadpAGoalRestrictionDTO = pagoalrestrictionSVCOImpl.findOnePA_GoalRestriction(ctx, key);
            assertEquals(loadpAGoalRestrictionDTO.getName() + "M", newpAGoalRestrictionDTO.getName());
            assertEquals(loadpAGoalRestrictionDTO.getPa_GoalRestriction_ID(), newpAGoalRestrictionDTO.getPa_GoalRestriction_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesactiverPA_GoalRestriction() {
        try {
            PA_GoalRestrictionCriteria criteria = new PA_GoalRestrictionCriteria();
            criteria.setPa_GoalRestriction_ID(newpAGoalRestrictionDTO.getPa_GoalRestriction_ID());
            criteria.setIsActive("N");
            pagoalrestrictionSVCOImpl.updatePA_GoalRestriction(criteria);
            int key = newpAGoalRestrictionDTO.getPa_GoalRestriction_ID();
            loadpAGoalRestrictionDTO = pagoalrestrictionSVCOImpl.findOnePA_GoalRestriction(ctx, key);
            assertEquals(loadpAGoalRestrictionDTO.getIsActive(), "N");
            assertEquals(loadpAGoalRestrictionDTO.getPa_GoalRestriction_ID(), newpAGoalRestrictionDTO.getPa_GoalRestriction_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
