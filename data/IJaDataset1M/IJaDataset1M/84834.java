package org.koossery.adempiere.svco.impl.ad;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.ad.AD_ClientCriteria;
import org.koossery.adempiere.core.contract.dto.ad.AD_ClientDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.ad.IAD_ClientSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class AD_ClientSVCOImplTest extends KTADempiereBaseTest {

    private static IAD_ClientSVCO adclientSVCOImpl = null;

    private static AD_ClientDTO newaDClientDTO = new AD_ClientDTO();

    private static AD_ClientDTO loadaDClientDTO = new AD_ClientDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newaDClientDTO.setAd_Language("en_US");
            newaDClientDTO.setAutoArchive("N");
            newaDClientDTO.setDescription("Description" + d.toString());
            newaDClientDTO.setDocumentDir("documentDir");
            newaDClientDTO.setEmailTest("N");
            newaDClientDTO.setMmPolicy("L");
            newaDClientDTO.setModelValidationClasses("modelValidationClasses");
            newaDClientDTO.setName("AD_CLIENT" + d.toString());
            newaDClientDTO.setRequestEMail("requestEMail");
            newaDClientDTO.setRequestFolder("requestFolder");
            newaDClientDTO.setRequestUser("requestUser");
            newaDClientDTO.setRequestUserPW("requestUserPW");
            newaDClientDTO.setSmTPHost("smTPHost");
            newaDClientDTO.setUnixArchivePath("unixArchivePath");
            newaDClientDTO.setUnixAttachmentPath("unixAttachmentPath");
            newaDClientDTO.setValue("value");
            newaDClientDTO.setWindowsArchivePath("windowsArchivePath");
            newaDClientDTO.setWindowsAttachmentPath("windowsAttachmentPath");
            newaDClientDTO.setIsCostImmediate("Y");
            newaDClientDTO.setIsMultiLingualDocument("Y");
            newaDClientDTO.setIsPostImmediate("Y");
            newaDClientDTO.setIsServerEMail("Y");
            newaDClientDTO.setIsSmtpAuthorization("Y");
            newaDClientDTO.setIsUseASP("Y");
            newaDClientDTO.setIsUseBetaFunctions("Y");
            newaDClientDTO.setIsActive("Y");
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
        adclientSVCOImpl = (IAD_ClientSVCO) finder.get("SVCO/AD_Client");
    }

    @Test
    public void testCreerAD_Client() {
        try {
            int ad_Client_ID = adclientSVCOImpl.createAD_Client(ctx, newaDClientDTO, null);
            assertNotSame("AD_Client creation failed ID=0", ad_Client_ID, 0);
            newaDClientDTO.setAd_Client_ID(ad_Client_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerAD_Client() {
        try {
            int key = newaDClientDTO.getAd_Client_ID();
            loadaDClientDTO = adclientSVCOImpl.findOneAD_Client(ctx, key);
            assertEquals(loadaDClientDTO.getAd_Language(), newaDClientDTO.getAd_Language());
            assertEquals(loadaDClientDTO.getAutoArchive(), newaDClientDTO.getAutoArchive());
            assertEquals(loadaDClientDTO.getDescription(), newaDClientDTO.getDescription());
            assertEquals(loadaDClientDTO.getDocumentDir(), newaDClientDTO.getDocumentDir());
            assertEquals(loadaDClientDTO.getEmailTest(), newaDClientDTO.getEmailTest());
            assertEquals(loadaDClientDTO.getMmPolicy(), newaDClientDTO.getMmPolicy());
            assertEquals(loadaDClientDTO.getModelValidationClasses(), newaDClientDTO.getModelValidationClasses());
            assertEquals(loadaDClientDTO.getName(), newaDClientDTO.getName());
            assertEquals(loadaDClientDTO.getRequestEMail(), newaDClientDTO.getRequestEMail());
            assertEquals(loadaDClientDTO.getRequestFolder(), newaDClientDTO.getRequestFolder());
            assertEquals(loadaDClientDTO.getRequestUser(), newaDClientDTO.getRequestUser());
            assertEquals(loadaDClientDTO.getRequestUserPW(), newaDClientDTO.getRequestUserPW());
            assertEquals(loadaDClientDTO.getSmTPHost(), newaDClientDTO.getSmTPHost());
            assertEquals(loadaDClientDTO.getUnixArchivePath(), newaDClientDTO.getUnixArchivePath());
            assertEquals(loadaDClientDTO.getUnixAttachmentPath(), newaDClientDTO.getUnixAttachmentPath());
            assertEquals(loadaDClientDTO.getValue(), newaDClientDTO.getValue());
            assertEquals(loadaDClientDTO.getWindowsArchivePath(), newaDClientDTO.getWindowsArchivePath());
            assertEquals(loadaDClientDTO.getWindowsAttachmentPath(), newaDClientDTO.getWindowsAttachmentPath());
            assertEquals(loadaDClientDTO.getIsCostImmediate(), newaDClientDTO.getIsCostImmediate());
            assertEquals(loadaDClientDTO.getIsMultiLingualDocument(), newaDClientDTO.getIsMultiLingualDocument());
            assertEquals(loadaDClientDTO.getIsPostImmediate(), newaDClientDTO.getIsPostImmediate());
            assertEquals(loadaDClientDTO.getIsServerEMail(), newaDClientDTO.getIsServerEMail());
            assertEquals(loadaDClientDTO.getIsSmtpAuthorization(), newaDClientDTO.getIsSmtpAuthorization());
            assertEquals(loadaDClientDTO.getIsStoreArchiveOnFileSystem(), newaDClientDTO.getIsStoreArchiveOnFileSystem());
            assertEquals(loadaDClientDTO.getIsStoreAttachmentsOnFileSystem(), newaDClientDTO.getIsStoreAttachmentsOnFileSystem());
            assertEquals(loadaDClientDTO.getIsUseASP(), newaDClientDTO.getIsUseASP());
            assertEquals(loadaDClientDTO.getIsUseBetaFunctions(), newaDClientDTO.getIsUseBetaFunctions());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherAD_Client() {
        ArrayList<AD_ClientDTO> resultlist;
        try {
            AD_ClientCriteria criteria = new AD_ClientCriteria();
            criteria.setAd_Role_ID(102);
            resultlist = adclientSVCOImpl.findAD_Client(ctx, criteria);
            assertNotNull(resultlist);
            for (AD_ClientDTO dto : resultlist) {
                System.out.println("FIND ID=" + dto.getAd_Client_ID() + " : Name=" + dto.getName());
                if (dto.getAd_Client_ID() == newaDClientDTO.getAd_Client_ID()) assertEquals(dto.getName(), newaDClientDTO.getName());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierAD_Client() {
        try {
            newaDClientDTO.setName(loadaDClientDTO.getName() + "M");
            newaDClientDTO.setIsActive("Y");
            adclientSVCOImpl.updateAD_Client(ctx, newaDClientDTO);
            int key = newaDClientDTO.getAd_Client_ID();
            loadaDClientDTO = adclientSVCOImpl.findOneAD_Client(ctx, key);
            assertEquals(loadaDClientDTO.getDescription(), newaDClientDTO.getDescription());
            assertEquals(loadaDClientDTO.getName(), newaDClientDTO.getName());
            assertEquals(loadaDClientDTO.getAd_Client_ID(), newaDClientDTO.getAd_Client_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerAD_Client() {
        try {
            AD_ClientCriteria criteria = new AD_ClientCriteria();
            criteria.setAd_Client_ID(newaDClientDTO.getAd_Client_ID());
            boolean deleted = adclientSVCOImpl.deleteAD_Client(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
