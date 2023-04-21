package org.koossery.adempiere.svco.impl.role;

import java.util.ArrayList;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.role.AD_Role_OrgAccessCriteria;
import org.koossery.adempiere.core.contract.dto.role.AD_Role_OrgAccessDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.role.IAD_Role_OrgAccessSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class AD_Role_OrgAccessSVCOImplTest extends KTADempiereBaseTest {

    private static IAD_Role_OrgAccessSVCO adroleorgaccessSVCOImpl;

    private static AD_Role_OrgAccessDTO newaDRoleOrgAccessDTO = new AD_Role_OrgAccessDTO();

    private static AD_Role_OrgAccessDTO loadaDRoleOrgAccessDTO = new AD_Role_OrgAccessDTO();

    static {
        try {
            init();
            newaDRoleOrgAccessDTO.setAd_Role_ID(50001);
            newaDRoleOrgAccessDTO.setAd_Org_ID(11);
            newaDRoleOrgAccessDTO.setIsReadOnly("Y");
            newaDRoleOrgAccessDTO.setIsActive("Y");
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
        adroleorgaccessSVCOImpl = (IAD_Role_OrgAccessSVCO) finder.get("SVCO/AD_Role_OrgAccess");
    }

    @Test
    public void testCreerAD_Role_OrgAccess() {
        try {
            int ad_Role_OrgAccess_ID = adroleorgaccessSVCOImpl.createAD_Role_OrgAccess(ctx, newaDRoleOrgAccessDTO, null);
            assertNotSame("AD_Role_OrgAccess creation failed ID=0", ad_Role_OrgAccess_ID, 0);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerAD_Role_OrgAccess() {
        try {
            int key1 = newaDRoleOrgAccessDTO.getAd_Role_ID();
            int key2 = newaDRoleOrgAccessDTO.getAd_Org_ID();
            loadaDRoleOrgAccessDTO = adroleorgaccessSVCOImpl.findOneAD_Role_OrgAccess(ctx, key1, key2);
            assertEquals(loadaDRoleOrgAccessDTO.getAd_Role_ID(), newaDRoleOrgAccessDTO.getAd_Role_ID());
            assertEquals(loadaDRoleOrgAccessDTO.getAd_Org_ID(), newaDRoleOrgAccessDTO.getAd_Org_ID());
            assertEquals(loadaDRoleOrgAccessDTO.getIsReadOnly(), newaDRoleOrgAccessDTO.getIsReadOnly());
            assertEquals(loadaDRoleOrgAccessDTO.getIsActive(), newaDRoleOrgAccessDTO.getIsActive());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherAD_Role_OrgAccess() {
        ArrayList<AD_Role_OrgAccessDTO> resultlist;
        try {
            AD_Role_OrgAccessCriteria criteria = new AD_Role_OrgAccessCriteria();
            resultlist = adroleorgaccessSVCOImpl.findAD_Role_OrgAccess(ctx, criteria);
            assertNotNull(resultlist);
            for (AD_Role_OrgAccessDTO dto : resultlist) {
                System.out.println("ID=" + dto.getAd_Role_ID() + " : IsActive=" + dto.getIsActive());
                if ((dto.getAd_Role_ID() == newaDRoleOrgAccessDTO.getAd_Role_ID()) && (dto.getAd_Org_ID() == newaDRoleOrgAccessDTO.getAd_Org_ID())) assertEquals(dto.getIsActive(), newaDRoleOrgAccessDTO.getIsActive());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierAD_Role_OrgAccess() {
        try {
            System.out.print("Modified Properties");
            newaDRoleOrgAccessDTO.setIsActive("N");
            adroleorgaccessSVCOImpl.updateAD_Role_OrgAccess(ctx, newaDRoleOrgAccessDTO);
            int key1 = newaDRoleOrgAccessDTO.getAd_Role_ID();
            int key2 = newaDRoleOrgAccessDTO.getAd_Org_ID();
            loadaDRoleOrgAccessDTO = adroleorgaccessSVCOImpl.findOneAD_Role_OrgAccess(ctx, key1, key2);
            assertEquals(loadaDRoleOrgAccessDTO.getIsActive(), newaDRoleOrgAccessDTO.getIsActive());
            assertEquals(loadaDRoleOrgAccessDTO.getIsReadOnly(), newaDRoleOrgAccessDTO.getIsReadOnly());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerAD_Role_OrgAccess() {
        try {
            AD_Role_OrgAccessCriteria criteria = new AD_Role_OrgAccessCriteria();
            criteria.setAd_Role_ID(newaDRoleOrgAccessDTO.getAd_Role_ID());
            criteria.setAd_Org_ID(newaDRoleOrgAccessDTO.getAd_Org_ID());
            boolean deleted = adroleorgaccessSVCOImpl.deleteAD_Role_OrgAccess(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
