package org.koossery.adempiere.svco.impl.ad;

import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.ad.AD_AccessLogCriteria;
import org.koossery.adempiere.core.contract.dto.ad.AD_AccessLogDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.ad.IAD_AccessLogSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class AD_AccessLogSVCOImplTest extends KTADempiereBaseTest {

    private IAD_AccessLogSVCO adaccesslogSVCOImpl;

    private static AD_AccessLogDTO newaDAccessLogDTO = new AD_AccessLogDTO();

    private static AD_AccessLogDTO loadaDAccessLogDTO = new AD_AccessLogDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            newaDAccessLogDTO.setAd_AccessLog_ID(1000);
            newaDAccessLogDTO.setAd_Column_ID(100);
            newaDAccessLogDTO.setAd_Table_ID(100);
            newaDAccessLogDTO.setDescription("Description" + d.toString());
            newaDAccessLogDTO.setRecord_ID(1000);
            newaDAccessLogDTO.setRemote_Addr("remote_Addr");
            newaDAccessLogDTO.setRemote_Host("remote_Host");
            newaDAccessLogDTO.setReply("reply");
            newaDAccessLogDTO.setTextMsg("textMsg");
            newaDAccessLogDTO.setIsActive("Y");
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
        adaccesslogSVCOImpl = (IAD_AccessLogSVCO) finder.get("SVCO/AD_AccessLog");
    }

    @Test
    public void testCreerAD_AccessLog() {
        try {
            int ad_AccessLog_ID = adaccesslogSVCOImpl.createAD_AccessLog(ctx, newaDAccessLogDTO, null);
            assertNotSame("AD_AccessLog creation failed ID=0", ad_AccessLog_ID, 0);
            newaDAccessLogDTO.setAd_AccessLog_ID(ad_AccessLog_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerAD_AccessLog() {
        try {
            int key = newaDAccessLogDTO.getAd_AccessLog_ID();
            loadaDAccessLogDTO = adaccesslogSVCOImpl.findOneAD_AccessLog(ctx, key);
            assertEquals(loadaDAccessLogDTO.getAd_AccessLog_ID(), newaDAccessLogDTO.getAd_AccessLog_ID());
            assertEquals(loadaDAccessLogDTO.getAd_Column_ID(), newaDAccessLogDTO.getAd_Column_ID());
            assertEquals(loadaDAccessLogDTO.getAd_Table_ID(), newaDAccessLogDTO.getAd_Table_ID());
            assertEquals(loadaDAccessLogDTO.getDescription(), newaDAccessLogDTO.getDescription());
            assertEquals(loadaDAccessLogDTO.getRecord_ID(), newaDAccessLogDTO.getRecord_ID());
            assertEquals(loadaDAccessLogDTO.getRemote_Addr(), newaDAccessLogDTO.getRemote_Addr());
            assertEquals(loadaDAccessLogDTO.getRemote_Host(), newaDAccessLogDTO.getRemote_Host());
            assertEquals(loadaDAccessLogDTO.getReply(), newaDAccessLogDTO.getReply());
            assertEquals(loadaDAccessLogDTO.getTextMsg(), newaDAccessLogDTO.getTextMsg());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherAD_AccessLog() {
        ArrayList<AD_AccessLogDTO> resultlist;
        try {
            AD_AccessLogCriteria criteria = new AD_AccessLogCriteria();
            resultlist = adaccesslogSVCOImpl.findAD_AccessLog(ctx, criteria);
            assertNotNull(resultlist);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierAD_AccessLog() {
        try {
            newaDAccessLogDTO.setIsActive("Y");
            adaccesslogSVCOImpl.updateAD_AccessLog(ctx, newaDAccessLogDTO);
            int key = newaDAccessLogDTO.getAd_AccessLog_ID();
            loadaDAccessLogDTO = adaccesslogSVCOImpl.findOneAD_AccessLog(ctx, key);
            assertEquals(loadaDAccessLogDTO.getDescription(), newaDAccessLogDTO.getDescription());
            assertEquals(loadaDAccessLogDTO.getAd_AccessLog_ID(), newaDAccessLogDTO.getAd_AccessLog_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerAD_AccessLog() {
        try {
            AD_AccessLogCriteria criteria = new AD_AccessLogCriteria();
            criteria.setAd_AccessLog_ID(newaDAccessLogDTO.getAd_AccessLog_ID());
            boolean deleted = adaccesslogSVCOImpl.deleteAD_AccessLog(ctx, criteria);
            assertEquals(true, deleted);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
