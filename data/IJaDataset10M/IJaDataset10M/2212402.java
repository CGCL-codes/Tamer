package org.koossery.adempiere.svco.impl.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.task.C_ProjectTaskCriteria;
import org.koossery.adempiere.core.contract.dto.task.C_ProjectTaskDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.task.IC_ProjectTaskSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_ProjectTaskSVCOImplTest extends KTADempiereBaseTest {

    private IC_ProjectTaskSVCO cprojecttaskSVCOImpl;

    private static C_ProjectTaskDTO newcProjectTaskDTO = new C_ProjectTaskDTO();

    private static C_ProjectTaskDTO loadcProjectTaskDTO = new C_ProjectTaskDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newcProjectTaskDTO.setC_ProjectPhase_ID(1000);
            newcProjectTaskDTO.setC_ProjectTask_ID(1000);
            newcProjectTaskDTO.setC_Task_ID(1000);
            newcProjectTaskDTO.setCommittedAmt(new BigDecimal(1000));
            newcProjectTaskDTO.setDescription("Description" + d.toString());
            newcProjectTaskDTO.setHelp("help");
            newcProjectTaskDTO.setM_Product_ID(1000);
            newcProjectTaskDTO.setName("C_PROJECTTASK" + d.toString());
            newcProjectTaskDTO.setPlannedAmt(new BigDecimal(1000));
            newcProjectTaskDTO.setProjInvoiceRule("projInvoiceRule");
            newcProjectTaskDTO.setQty(new BigDecimal(1000));
            newcProjectTaskDTO.setSeqNo(1000);
            newcProjectTaskDTO.setIsActive("Y");
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
        cprojecttaskSVCOImpl = (IC_ProjectTaskSVCO) finder.get("SVCO/C_ProjectTask");
    }

    @Test
    public void testCreerC_ProjectTask() {
        try {
            int c_ProjectTask_ID = cprojecttaskSVCOImpl.createC_ProjectTask(ctx, newcProjectTaskDTO, null);
            assertNotSame("C_ProjectTask creation failed ID=0", c_ProjectTask_ID, 0);
            newcProjectTaskDTO.setC_ProjectTask_ID(c_ProjectTask_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerC_ProjectTask() {
        try {
            int key = newcProjectTaskDTO.getC_ProjectTask_ID();
            loadcProjectTaskDTO = cprojecttaskSVCOImpl.findOneC_ProjectTask(ctx, key);
            assertEquals(loadcProjectTaskDTO.getC_ProjectPhase_ID(), newcProjectTaskDTO.getC_ProjectPhase_ID());
            assertEquals(loadcProjectTaskDTO.getC_ProjectTask_ID(), newcProjectTaskDTO.getC_ProjectTask_ID());
            assertEquals(loadcProjectTaskDTO.getC_Task_ID(), newcProjectTaskDTO.getC_Task_ID());
            assertEquals(loadcProjectTaskDTO.getCommittedAmt(), newcProjectTaskDTO.getCommittedAmt());
            assertEquals(loadcProjectTaskDTO.getDescription(), newcProjectTaskDTO.getDescription());
            assertEquals(loadcProjectTaskDTO.getHelp(), newcProjectTaskDTO.getHelp());
            assertEquals(loadcProjectTaskDTO.getM_Product_ID(), newcProjectTaskDTO.getM_Product_ID());
            assertEquals(loadcProjectTaskDTO.getName(), newcProjectTaskDTO.getName());
            assertEquals(loadcProjectTaskDTO.getPlannedAmt(), newcProjectTaskDTO.getPlannedAmt());
            assertEquals(loadcProjectTaskDTO.getProjInvoiceRule(), newcProjectTaskDTO.getProjInvoiceRule());
            assertEquals(loadcProjectTaskDTO.getQty(), newcProjectTaskDTO.getQty());
            assertEquals(loadcProjectTaskDTO.getSeqNo(), newcProjectTaskDTO.getSeqNo());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherC_ProjectTask() {
        ArrayList<C_ProjectTaskDTO> resultlist;
        try {
            C_ProjectTaskCriteria criteria = new C_ProjectTaskCriteria();
            resultlist = cprojecttaskSVCOImpl.findC_ProjectTask(ctx, criteria);
            assertNotNull(resultlist);
            for (C_ProjectTaskDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_ProjectTask_ID() + " : Name=" + dto.getName());
                if (dto.getC_ProjectTask_ID() == newcProjectTaskDTO.getC_ProjectTask_ID()) assertEquals(dto.getName(), newcProjectTaskDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierC_ProjectTask() {
        try {
            newcProjectTaskDTO.setName(loadcProjectTaskDTO.getName() + "M");
            newcProjectTaskDTO.setIsActive("Y");
            cprojecttaskSVCOImpl.updateC_ProjectTask(ctx, newcProjectTaskDTO);
            int key = newcProjectTaskDTO.getC_ProjectTask_ID();
            loadcProjectTaskDTO = cprojecttaskSVCOImpl.findOneC_ProjectTask(ctx, key);
            assertEquals(loadcProjectTaskDTO.getDescription(), newcProjectTaskDTO.getDescription());
            assertEquals(loadcProjectTaskDTO.getName(), newcProjectTaskDTO.getName());
            assertEquals(loadcProjectTaskDTO.getC_ProjectTask_ID(), newcProjectTaskDTO.getC_ProjectTask_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerC_ProjectTask() {
        try {
            C_ProjectTaskCriteria criteria = new C_ProjectTaskCriteria();
            criteria.setC_ProjectTask_ID(newcProjectTaskDTO.getC_ProjectTask_ID());
            boolean deleted = cprojecttaskSVCOImpl.deleteC_ProjectTask(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
