package org.koossery.adempiere.svco.impl.asset;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.asset.A_AssetCriteria;
import org.koossery.adempiere.core.contract.dto.asset.A_AssetDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.asset.IA_AssetSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class A_AssetSVCOImplTest extends KTADempiereBaseTest {

    private static IA_AssetSVCO aassetSVCOImpl = null;

    private static A_AssetDTO newaAssetDTO = new A_AssetDTO();

    private static A_AssetDTO loadaAssetDTO = new A_AssetDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newaAssetDTO.setA_Asset_Group_ID(100);
            newaAssetDTO.setA_Asset_ID(1000);
            newaAssetDTO.setAd_User_ID(100);
            newaAssetDTO.setAssetDepreciationDate(new Timestamp(1000000));
            newaAssetDTO.setAssetDisposalDate(new Timestamp(1000000));
            newaAssetDTO.setAssetServiceDate(new Timestamp(1000000));
            newaAssetDTO.setC_BPartner_ID(112);
            newaAssetDTO.setC_BPartner_Location_ID(108);
            newaAssetDTO.setC_BPartnerSR_ID(1000);
            newaAssetDTO.setC_Location_ID(109);
            newaAssetDTO.setC_Project_ID(100);
            newaAssetDTO.setDescription("Description" + d.toString());
            newaAssetDTO.setGuaranteeDate(new Timestamp(1000000));
            newaAssetDTO.setHelp("help");
            newaAssetDTO.setLastMaintenanceDate(new Timestamp(1000000));
            newaAssetDTO.setLastMaintenanceNote("lastMaintenanceNote");
            newaAssetDTO.setLastMaintenanceUnit(1000);
            newaAssetDTO.setLease_BPartner_ID(1000);
            newaAssetDTO.setLeaseTerminationDate(new Timestamp(1000000));
            newaAssetDTO.setLifeUseUnits(1000);
            newaAssetDTO.setLocationComment("locationComment");
            newaAssetDTO.setLot("lot");
            newaAssetDTO.setM_AttributeSetInstance_ID(100);
            newaAssetDTO.setM_InOutLine_ID(100);
            newaAssetDTO.setM_Locator_ID(101);
            newaAssetDTO.setM_Product_ID(122);
            newaAssetDTO.setName("A_ASSET" + d.toString());
            newaAssetDTO.setNextMaintenenceDate(new Timestamp(1000000));
            newaAssetDTO.setNextMaintenenceUnit(1000);
            newaAssetDTO.setQty(new BigDecimal(1000));
            newaAssetDTO.setSerNo("serNo");
            newaAssetDTO.setUseLifeMonths(1000);
            newaAssetDTO.setUseLifeYears(1000);
            newaAssetDTO.setUseUnits(1000);
            newaAssetDTO.setValue("value");
            newaAssetDTO.setVersionNo("versionNo");
            newaAssetDTO.setIsDepreciated("Y");
            newaAssetDTO.setIsDisposed("Y");
            newaAssetDTO.setIsFullyDepreciated("Y");
            newaAssetDTO.setIsInPosession("Y");
            newaAssetDTO.setIsOwned("Y");
            newaAssetDTO.setIsActive("Y");
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
        aassetSVCOImpl = (IA_AssetSVCO) finder.get("SVCO/A_Asset");
    }

    @Test
    public void testcreateA_Asset() {
        try {
            int a_Asset_ID = aassetSVCOImpl.createA_Asset(ctx, newaAssetDTO, null);
            assertNotSame("A_Asset creation failed ID=0", a_Asset_ID, 0);
            newaAssetDTO.setA_Asset_ID(a_Asset_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneA_Asset() {
        try {
            int key = newaAssetDTO.getA_Asset_ID();
            loadaAssetDTO = aassetSVCOImpl.findOneA_Asset(ctx, key);
            assertEquals(loadaAssetDTO.getA_Asset_Group_ID(), newaAssetDTO.getA_Asset_Group_ID());
            assertEquals(loadaAssetDTO.getA_Asset_ID(), newaAssetDTO.getA_Asset_ID());
            assertEquals(loadaAssetDTO.getAd_User_ID(), newaAssetDTO.getAd_User_ID());
            assertEquals(loadaAssetDTO.getAssetDepreciationDate(), newaAssetDTO.getAssetDepreciationDate());
            assertEquals(loadaAssetDTO.getAssetDisposalDate(), newaAssetDTO.getAssetDisposalDate());
            assertEquals(loadaAssetDTO.getAssetServiceDate(), newaAssetDTO.getAssetServiceDate());
            assertEquals(loadaAssetDTO.getC_BPartner_ID(), newaAssetDTO.getC_BPartner_ID());
            assertEquals(loadaAssetDTO.getC_BPartner_Location_ID(), newaAssetDTO.getC_BPartner_Location_ID());
            assertEquals(loadaAssetDTO.getC_BPartnerSR_ID(), newaAssetDTO.getC_BPartnerSR_ID());
            assertEquals(loadaAssetDTO.getC_Location_ID(), newaAssetDTO.getC_Location_ID());
            assertEquals(loadaAssetDTO.getC_Project_ID(), newaAssetDTO.getC_Project_ID());
            assertEquals(loadaAssetDTO.getDescription(), newaAssetDTO.getDescription());
            assertEquals(loadaAssetDTO.getGuaranteeDate(), newaAssetDTO.getGuaranteeDate());
            assertEquals(loadaAssetDTO.getHelp(), newaAssetDTO.getHelp());
            assertEquals(loadaAssetDTO.getLastMaintenanceDate(), newaAssetDTO.getLastMaintenanceDate());
            assertEquals(loadaAssetDTO.getLastMaintenanceNote(), newaAssetDTO.getLastMaintenanceNote());
            assertEquals(loadaAssetDTO.getLastMaintenanceUnit(), newaAssetDTO.getLastMaintenanceUnit());
            assertEquals(loadaAssetDTO.getLease_BPartner_ID(), newaAssetDTO.getLease_BPartner_ID());
            assertEquals(loadaAssetDTO.getLeaseTerminationDate(), newaAssetDTO.getLeaseTerminationDate());
            assertEquals(loadaAssetDTO.getLifeUseUnits(), newaAssetDTO.getLifeUseUnits());
            assertEquals(loadaAssetDTO.getLocationComment(), newaAssetDTO.getLocationComment());
            assertEquals(loadaAssetDTO.getLot(), newaAssetDTO.getLot());
            assertEquals(loadaAssetDTO.getM_AttributeSetInstance_ID(), newaAssetDTO.getM_AttributeSetInstance_ID());
            assertEquals(loadaAssetDTO.getM_InOutLine_ID(), newaAssetDTO.getM_InOutLine_ID());
            assertEquals(loadaAssetDTO.getM_Locator_ID(), newaAssetDTO.getM_Locator_ID());
            assertEquals(loadaAssetDTO.getM_Product_ID(), newaAssetDTO.getM_Product_ID());
            assertEquals(loadaAssetDTO.getName(), newaAssetDTO.getName());
            assertEquals(loadaAssetDTO.getNextMaintenenceDate(), newaAssetDTO.getNextMaintenenceDate());
            assertEquals(loadaAssetDTO.getNextMaintenenceUnit(), newaAssetDTO.getNextMaintenenceUnit());
            assertEquals(loadaAssetDTO.getQty(), newaAssetDTO.getQty());
            assertEquals(loadaAssetDTO.getSerNo(), newaAssetDTO.getSerNo());
            assertEquals(loadaAssetDTO.getUseLifeMonths(), newaAssetDTO.getUseLifeMonths());
            assertEquals(loadaAssetDTO.getUseLifeYears(), newaAssetDTO.getUseLifeYears());
            assertEquals(loadaAssetDTO.getUseUnits(), newaAssetDTO.getUseUnits());
            assertEquals(loadaAssetDTO.getValue(), newaAssetDTO.getValue());
            assertEquals(loadaAssetDTO.getVersionNo(), newaAssetDTO.getVersionNo());
            assertEquals(loadaAssetDTO.getIsDepreciated(), newaAssetDTO.getIsDepreciated());
            assertEquals(loadaAssetDTO.getIsDisposed(), newaAssetDTO.getIsDisposed());
            assertEquals(loadaAssetDTO.getIsFullyDepreciated(), newaAssetDTO.getIsFullyDepreciated());
            assertEquals(loadaAssetDTO.getIsInPosession(), newaAssetDTO.getIsInPosession());
            assertEquals(loadaAssetDTO.getIsOwned(), newaAssetDTO.getIsOwned());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindA_Asset() {
        ArrayList<A_AssetDTO> resultlist;
        try {
            A_AssetCriteria criteria = new A_AssetCriteria();
            resultlist = aassetSVCOImpl.findA_Asset(ctx, criteria);
            assertNotNull(resultlist);
            for (A_AssetDTO dto : resultlist) {
                System.out.println("ID=" + dto.getA_Asset_ID() + " : Name=" + dto.getName());
                if (dto.getA_Asset_ID() == newaAssetDTO.getA_Asset_ID()) assertEquals(dto.getName(), newaAssetDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateA_Asset() {
        try {
            newaAssetDTO.setName(loadaAssetDTO.getName() + "M");
            newaAssetDTO.setIsActive("Y");
            aassetSVCOImpl.updateA_Asset(ctx, newaAssetDTO);
            int key = newaAssetDTO.getA_Asset_ID();
            loadaAssetDTO = aassetSVCOImpl.findOneA_Asset(ctx, key);
            assertEquals(loadaAssetDTO.getDescription(), newaAssetDTO.getDescription());
            assertEquals(loadaAssetDTO.getName() + "M", newaAssetDTO.getName());
            assertEquals(loadaAssetDTO.getA_Asset_ID(), newaAssetDTO.getA_Asset_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerA_Asset() {
        try {
            A_AssetCriteria criteria = new A_AssetCriteria();
            criteria.setA_Asset_ID(newaAssetDTO.getA_Asset_ID());
            boolean deleted = aassetSVCOImpl.supprimerA_Asset(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
