package org.koossery.adempiere.svco.impl.hr;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.hr.HR_JobCriteria;
import org.koossery.adempiere.core.contract.dto.hr.HR_JobDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.hr.IHR_JobSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class HR_JobSVCOImplTest extends KTADempiereBaseTest {

    private static IHR_JobSVCO hrjobSVCOImpl = null;

    private static HR_JobDTO newhRJobDTO = new HR_JobDTO();

    private static HR_JobDTO loadhRJobDTO = new HR_JobDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            hrjobSVCOImpl = (IHR_JobSVCO) finder.get("SVCO/HR_Job");
            newhRJobDTO.setDescription("Description" + d.toString());
            newhRJobDTO.setHr_Department_ID(1000005);
            newhRJobDTO.setName("HR_JOB" + d.toString());
            newhRJobDTO.setValue("value");
            newhRJobDTO.setIsParent("Y");
            newhRJobDTO.setIsActive("Y");
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
    public void testcreateHR_Job() {
        try {
            int hr_Job_ID = hrjobSVCOImpl.createHR_Job(ctx, newhRJobDTO, null);
            assertNotSame("HR_Job creation failed ID=0", hr_Job_ID, 0);
            newhRJobDTO.setHr_Job_ID(hr_Job_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneHR_Job() {
        try {
            int key = newhRJobDTO.getHr_Job_ID();
            loadhRJobDTO = hrjobSVCOImpl.findOneHR_Job(ctx, key);
            assertEquals(loadhRJobDTO.getDescription(), newhRJobDTO.getDescription());
            assertEquals(loadhRJobDTO.getHr_Department_ID(), newhRJobDTO.getHr_Department_ID());
            assertEquals(loadhRJobDTO.getHr_Job_ID(), newhRJobDTO.getHr_Job_ID());
            assertEquals(loadhRJobDTO.getJobCant(), newhRJobDTO.getJobCant());
            assertEquals(loadhRJobDTO.getName(), newhRJobDTO.getName());
            assertEquals(loadhRJobDTO.getNext_Job_ID(), newhRJobDTO.getNext_Job_ID());
            assertEquals(loadhRJobDTO.getSupervisor_ID(), newhRJobDTO.getSupervisor_ID());
            assertEquals(loadhRJobDTO.getValue(), newhRJobDTO.getValue());
            assertEquals(loadhRJobDTO.getIsParent(), newhRJobDTO.getIsParent());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindHR_Job() {
        ArrayList<HR_JobDTO> resultlist;
        try {
            HR_JobCriteria criteria = new HR_JobCriteria();
            resultlist = hrjobSVCOImpl.findHR_Job(ctx, criteria);
            assertNotNull(resultlist);
            for (HR_JobDTO dto : resultlist) {
                System.out.println("ID=" + dto.getHr_Job_ID() + " : Name=" + dto.getName());
                if (dto.getHr_Job_ID() == newhRJobDTO.getHr_Job_ID()) assertEquals(dto.getName(), newhRJobDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateHR_Job() {
        try {
            newhRJobDTO.setName(loadhRJobDTO.getName() + "M");
            newhRJobDTO.setIsActive("Y");
            hrjobSVCOImpl.updateHR_Job(ctx, newhRJobDTO);
            int key = newhRJobDTO.getHr_Job_ID();
            loadhRJobDTO = hrjobSVCOImpl.findOneHR_Job(ctx, key);
            assertEquals(loadhRJobDTO.getDescription(), newhRJobDTO.getDescription());
            assertEquals(loadhRJobDTO.getName() + "M", newhRJobDTO.getName());
            assertEquals(loadhRJobDTO.getHr_Job_ID(), newhRJobDTO.getHr_Job_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDesactiverHR_Job() {
        try {
            HR_JobCriteria criteria = new HR_JobCriteria();
            criteria.setHr_Job_ID(newhRJobDTO.getHr_Job_ID());
            criteria.setIsActive("N");
            hrjobSVCOImpl.updateHR_Job(criteria);
            int key = newhRJobDTO.getHr_Job_ID();
            loadhRJobDTO = hrjobSVCOImpl.findOneHR_Job(ctx, key);
            assertEquals(loadhRJobDTO.getIsActive(), "N");
            assertEquals(loadhRJobDTO.getHr_Job_ID(), newhRJobDTO.getHr_Job_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
