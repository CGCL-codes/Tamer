package org.koossery.adempiere.svco.impl.bom;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.bom.M_BOMAlternativeCriteria;
import org.koossery.adempiere.core.contract.dto.bom.M_BOMAlternativeDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.bom.IM_BOMAlternativeSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class M_BOMAlternativeSVCOImplTest extends KTADempiereBaseTest {

    private IM_BOMAlternativeSVCO mbomalternativeSVCOImpl;

    private static M_BOMAlternativeDTO newmBOMAlternativeDTO = new M_BOMAlternativeDTO();

    private static M_BOMAlternativeDTO loadmBOMAlternativeDTO = new M_BOMAlternativeDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newmBOMAlternativeDTO.setDescription("Description" + d.toString());
            newmBOMAlternativeDTO.setM_BOMAlternative_ID(1000);
            newmBOMAlternativeDTO.setM_Product_ID(1000);
            newmBOMAlternativeDTO.setName("M_BOMALTERNATIVE" + d.toString());
            newmBOMAlternativeDTO.setIsActive("Y");
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
        mbomalternativeSVCOImpl = (IM_BOMAlternativeSVCO) finder.get("SVCO/M_BOMAlternative");
    }

    @Test
    public void testCreerM_BOMAlternative() {
        try {
            int m_BOMAlternative_ID = mbomalternativeSVCOImpl.createM_BOMAlternative(ctx, newmBOMAlternativeDTO, null);
            assertNotSame("M_BOMAlternative creation failed ID=0", m_BOMAlternative_ID, 0);
            newmBOMAlternativeDTO.setM_BOMAlternative_ID(m_BOMAlternative_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerM_BOMAlternative() {
        try {
            int key = newmBOMAlternativeDTO.getM_BOMAlternative_ID();
            loadmBOMAlternativeDTO = mbomalternativeSVCOImpl.findOneM_BOMAlternative(ctx, key);
            assertEquals(loadmBOMAlternativeDTO.getDescription(), newmBOMAlternativeDTO.getDescription());
            assertEquals(loadmBOMAlternativeDTO.getM_BOMAlternative_ID(), newmBOMAlternativeDTO.getM_BOMAlternative_ID());
            assertEquals(loadmBOMAlternativeDTO.getM_Product_ID(), newmBOMAlternativeDTO.getM_Product_ID());
            assertEquals(loadmBOMAlternativeDTO.getName(), newmBOMAlternativeDTO.getName());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherM_BOMAlternative() {
        ArrayList<M_BOMAlternativeDTO> resultlist;
        try {
            M_BOMAlternativeCriteria criteria = new M_BOMAlternativeCriteria();
            resultlist = mbomalternativeSVCOImpl.findM_BOMAlternative(ctx, criteria);
            assertNotNull(resultlist);
            for (M_BOMAlternativeDTO dto : resultlist) {
                System.out.println("ID=" + dto.getM_BOMAlternative_ID() + " : Name=" + dto.getName());
                if (dto.getM_BOMAlternative_ID() == newmBOMAlternativeDTO.getM_BOMAlternative_ID()) assertEquals(dto.getName(), newmBOMAlternativeDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierM_BOMAlternative() {
        try {
            newmBOMAlternativeDTO.setName(loadmBOMAlternativeDTO.getName() + "M");
            newmBOMAlternativeDTO.setIsActive("Y");
            mbomalternativeSVCOImpl.updateM_BOMAlternative(ctx, newmBOMAlternativeDTO);
            int key = newmBOMAlternativeDTO.getM_BOMAlternative_ID();
            loadmBOMAlternativeDTO = mbomalternativeSVCOImpl.findOneM_BOMAlternative(ctx, key);
            assertEquals(loadmBOMAlternativeDTO.getDescription(), newmBOMAlternativeDTO.getDescription());
            assertEquals(loadmBOMAlternativeDTO.getName(), newmBOMAlternativeDTO.getName());
            assertEquals(loadmBOMAlternativeDTO.getM_BOMAlternative_ID(), newmBOMAlternativeDTO.getM_BOMAlternative_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerM_BOMAlternative() {
        try {
            M_BOMAlternativeCriteria criteria = new M_BOMAlternativeCriteria();
            criteria.setM_BOMAlternative_ID(newmBOMAlternativeDTO.getM_BOMAlternative_ID());
            boolean deleted = mbomalternativeSVCOImpl.deleteM_BOMAlternative(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
