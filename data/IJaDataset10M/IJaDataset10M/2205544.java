package org.koossery.adempiere.svco.impl.server.request;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.server.request.R_RequestProcessorCriteria;
import org.koossery.adempiere.core.contract.dto.server.request.R_RequestProcessorDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.server.request.IR_RequestProcessorSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class R_RequestProcessorSVCOImplTest extends KTADempiereBaseTest {

    private IR_RequestProcessorSVCO rrequestprocessorSVCOImpl;

    private static R_RequestProcessorDTO newrRequestProcessorDTO = new R_RequestProcessorDTO();

    private static R_RequestProcessorDTO loadrRequestProcessorDTO = new R_RequestProcessorDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newrRequestProcessorDTO.setDateLastRun(new Timestamp(1000000));
            newrRequestProcessorDTO.setDateNextRun(new Timestamp(1000000));
            newrRequestProcessorDTO.setDescription("Description" + d.toString());
            newrRequestProcessorDTO.setFrequency(1000);
            newrRequestProcessorDTO.setFrequencyType("M");
            newrRequestProcessorDTO.setInactivityAlertDays(1000);
            newrRequestProcessorDTO.setKeepLogDays(1000);
            newrRequestProcessorDTO.setName("R_REQUESTPROCESSOR" + d.toString());
            newrRequestProcessorDTO.setOverdueAlertDays(1000);
            newrRequestProcessorDTO.setOverdueAssignDays(1000);
            newrRequestProcessorDTO.setR_RequestProcessor_ID(1000);
            newrRequestProcessorDTO.setR_RequestType_ID(1000);
            newrRequestProcessorDTO.setRemindDays(1000);
            newrRequestProcessorDTO.setSupervisor_ID(101);
            newrRequestProcessorDTO.setIsProcessing("Y");
            newrRequestProcessorDTO.setIsActive("Y");
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
        rrequestprocessorSVCOImpl = (IR_RequestProcessorSVCO) finder.get("SVCO/R_RequestProcessor");
    }

    @Test
    public void testCreerR_RequestProcessor() {
        try {
            int r_RequestProcessor_ID = rrequestprocessorSVCOImpl.createR_RequestProcessor(ctx, newrRequestProcessorDTO, null);
            assertNotSame("R_RequestProcessor creation failed ID=0", r_RequestProcessor_ID, 0);
            newrRequestProcessorDTO.setR_RequestProcessor_ID(r_RequestProcessor_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerR_RequestProcessor() {
        try {
            int key = newrRequestProcessorDTO.getR_RequestProcessor_ID();
            loadrRequestProcessorDTO = rrequestprocessorSVCOImpl.findOneR_RequestProcessor(ctx, key);
            assertEquals(loadrRequestProcessorDTO.getDateLastRun(), newrRequestProcessorDTO.getDateLastRun());
            assertEquals(loadrRequestProcessorDTO.getDateNextRun(), newrRequestProcessorDTO.getDateNextRun());
            assertEquals(loadrRequestProcessorDTO.getDescription(), newrRequestProcessorDTO.getDescription());
            assertEquals(loadrRequestProcessorDTO.getFrequency(), newrRequestProcessorDTO.getFrequency());
            assertEquals(loadrRequestProcessorDTO.getFrequencyType(), newrRequestProcessorDTO.getFrequencyType());
            assertEquals(loadrRequestProcessorDTO.getInactivityAlertDays(), newrRequestProcessorDTO.getInactivityAlertDays());
            assertEquals(loadrRequestProcessorDTO.getKeepLogDays(), newrRequestProcessorDTO.getKeepLogDays());
            assertEquals(loadrRequestProcessorDTO.getName(), newrRequestProcessorDTO.getName());
            assertEquals(loadrRequestProcessorDTO.getOverdueAlertDays(), newrRequestProcessorDTO.getOverdueAlertDays());
            assertEquals(loadrRequestProcessorDTO.getOverdueAssignDays(), newrRequestProcessorDTO.getOverdueAssignDays());
            assertEquals(loadrRequestProcessorDTO.getR_RequestProcessor_ID(), newrRequestProcessorDTO.getR_RequestProcessor_ID());
            assertEquals(loadrRequestProcessorDTO.getR_RequestType_ID(), newrRequestProcessorDTO.getR_RequestType_ID());
            assertEquals(loadrRequestProcessorDTO.getRemindDays(), newrRequestProcessorDTO.getRemindDays());
            assertEquals(loadrRequestProcessorDTO.getSupervisor_ID(), newrRequestProcessorDTO.getSupervisor_ID());
            assertEquals(loadrRequestProcessorDTO.getIsProcessing(), newrRequestProcessorDTO.getIsProcessing());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherR_RequestProcessor() {
        ArrayList<R_RequestProcessorDTO> resultlist;
        try {
            R_RequestProcessorCriteria criteria = new R_RequestProcessorCriteria();
            resultlist = rrequestprocessorSVCOImpl.findR_RequestProcessor(ctx, criteria);
            assertNotNull(resultlist);
            for (R_RequestProcessorDTO dto : resultlist) {
                System.out.println("ID=" + dto.getR_RequestProcessor_ID() + " : Name=" + dto.getName());
                if (dto.getR_RequestProcessor_ID() == newrRequestProcessorDTO.getR_RequestProcessor_ID()) assertEquals(dto.getName(), newrRequestProcessorDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierR_RequestProcessor() {
        try {
            newrRequestProcessorDTO.setName(loadrRequestProcessorDTO.getName() + "M");
            newrRequestProcessorDTO.setIsActive("Y");
            rrequestprocessorSVCOImpl.updateR_RequestProcessor(ctx, newrRequestProcessorDTO);
            int key = newrRequestProcessorDTO.getR_RequestProcessor_ID();
            loadrRequestProcessorDTO = rrequestprocessorSVCOImpl.findOneR_RequestProcessor(ctx, key);
            assertEquals(loadrRequestProcessorDTO.getDescription(), newrRequestProcessorDTO.getDescription());
            assertEquals(loadrRequestProcessorDTO.getName(), newrRequestProcessorDTO.getName());
            assertEquals(loadrRequestProcessorDTO.getR_RequestProcessor_ID(), newrRequestProcessorDTO.getR_RequestProcessor_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerR_RequestProcessor() {
        try {
            R_RequestProcessorCriteria criteria = new R_RequestProcessorCriteria();
            criteria.setR_RequestProcessor_ID(newrRequestProcessorDTO.getR_RequestProcessor_ID());
            boolean deleted = rrequestprocessorSVCOImpl.deleteR_RequestProcessor(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
