package org.koossery.adempiere.svco.impl.menu;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.menu.AD_MenuCriteria;
import org.koossery.adempiere.core.contract.dto.menu.AD_MenuDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.menu.IAD_MenuSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class AD_MenuSVCOImplTest extends KTADempiereBaseTest {

    private IAD_MenuSVCO admenuSVCOImpl;

    private static AD_MenuDTO newaDMenuDTO = new AD_MenuDTO();

    private static AD_MenuDTO loadaDMenuDTO = new AD_MenuDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newaDMenuDTO.setAction("W");
            newaDMenuDTO.setAd_Form_ID(100);
            newaDMenuDTO.setAd_Menu_ID(1000);
            newaDMenuDTO.setAd_Process_ID(100);
            newaDMenuDTO.setAd_Task_ID(102);
            newaDMenuDTO.setAd_Window_ID(100);
            newaDMenuDTO.setAd_Workbench_ID(100);
            newaDMenuDTO.setAd_Workflow_ID(101);
            newaDMenuDTO.setDescription("Description" + d.toString());
            newaDMenuDTO.setEntityType("entityType");
            newaDMenuDTO.setName("AD_MENU" + d.toString());
            newaDMenuDTO.setIsReadOnly("Y");
            newaDMenuDTO.setIsSOTrx("Y");
            newaDMenuDTO.setIsSummary("Y");
            newaDMenuDTO.setIsActive("Y");
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
        admenuSVCOImpl = (IAD_MenuSVCO) finder.get("SVCO/AD_Menu");
    }

    @Test
    public void testCreerAD_Menu() {
        try {
            int ad_Menu_ID = admenuSVCOImpl.createAD_Menu(ctx, newaDMenuDTO, null);
            assertNotSame("AD_Menu creation failed ID=0", ad_Menu_ID, 0);
            newaDMenuDTO.setAd_Menu_ID(ad_Menu_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerAD_Menu() {
        try {
            int key = newaDMenuDTO.getAd_Menu_ID();
            loadaDMenuDTO = admenuSVCOImpl.findOneAD_Menu(ctx, key);
            assertEquals(loadaDMenuDTO.getAction(), newaDMenuDTO.getAction());
            assertEquals(loadaDMenuDTO.getAd_Form_ID(), newaDMenuDTO.getAd_Form_ID());
            assertEquals(loadaDMenuDTO.getAd_Menu_ID(), newaDMenuDTO.getAd_Menu_ID());
            assertEquals(loadaDMenuDTO.getAd_Process_ID(), newaDMenuDTO.getAd_Process_ID());
            assertEquals(loadaDMenuDTO.getAd_Task_ID(), newaDMenuDTO.getAd_Task_ID());
            assertEquals(loadaDMenuDTO.getAd_Window_ID(), newaDMenuDTO.getAd_Window_ID());
            assertEquals(loadaDMenuDTO.getAd_Workbench_ID(), newaDMenuDTO.getAd_Workbench_ID());
            assertEquals(loadaDMenuDTO.getAd_Workflow_ID(), newaDMenuDTO.getAd_Workflow_ID());
            assertEquals(loadaDMenuDTO.getDescription(), newaDMenuDTO.getDescription());
            assertEquals(loadaDMenuDTO.getEntityType(), newaDMenuDTO.getEntityType());
            assertEquals(loadaDMenuDTO.getName(), newaDMenuDTO.getName());
            assertEquals(loadaDMenuDTO.getIsReadOnly(), newaDMenuDTO.getIsReadOnly());
            assertEquals(loadaDMenuDTO.getIsSOTrx(), newaDMenuDTO.getIsSOTrx());
            assertEquals(loadaDMenuDTO.getIsSummary(), newaDMenuDTO.getIsSummary());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherAD_Menu() {
        ArrayList<AD_MenuDTO> resultlist;
        try {
            AD_MenuCriteria criteria = new AD_MenuCriteria();
            resultlist = admenuSVCOImpl.findAD_Menu(ctx, criteria);
            assertNotNull(resultlist);
            for (AD_MenuDTO dto : resultlist) {
                System.out.println("ID=" + dto.getAd_Menu_ID() + " : Name=" + dto.getName());
                if (dto.getAd_Menu_ID() == newaDMenuDTO.getAd_Menu_ID()) assertEquals(dto.getName(), newaDMenuDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierAD_Menu() {
        try {
            newaDMenuDTO.setName(loadaDMenuDTO.getName() + "M");
            newaDMenuDTO.setIsActive("Y");
            admenuSVCOImpl.updateAD_Menu(ctx, newaDMenuDTO);
            int key = newaDMenuDTO.getAd_Menu_ID();
            loadaDMenuDTO = admenuSVCOImpl.findOneAD_Menu(ctx, key);
            assertEquals(loadaDMenuDTO.getDescription(), newaDMenuDTO.getDescription());
            assertEquals(loadaDMenuDTO.getName(), newaDMenuDTO.getName());
            assertEquals(loadaDMenuDTO.getAd_Menu_ID(), newaDMenuDTO.getAd_Menu_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerAD_Menu() {
        try {
            AD_MenuCriteria criteria = new AD_MenuCriteria();
            criteria.setAd_Menu_ID(newaDMenuDTO.getAd_Menu_ID());
            boolean deleted = admenuSVCOImpl.deleteAD_Menu(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
