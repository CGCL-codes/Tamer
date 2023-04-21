package svrtest;

import java.io.File;
import java.io.IOException;
import com.hk.bean.HkAd;
import com.hk.svr.processor.CreateHkAdResult;
import com.hk.svr.processor.HkAdProcessor;
import com.hk.svr.pub.Err;

public class HkAdServiceTest extends HkServiceTest {

    private HkAdProcessor hkAdProcessor;

    public void setHkAdProcessor(HkAdProcessor hkAdProcessor) {
        this.hkAdProcessor = hkAdProcessor;
    }

    public void testCreateImg() throws IOException {
        long userId = 1;
        String name = "测试广告";
        int totalViewCount = 10000;
        byte showflg = HkAd.SHOWFLG_IMG;
        String href = "http://www.163.com";
        File file = new File("d:/test.jpg");
        HkAd o = new HkAd();
        o.setUserId(userId);
        o.setName(name);
        o.setTotalViewCount(totalViewCount);
        o.setShowflg(showflg);
        o.setHref(href);
        CreateHkAdResult result = this.hkAdProcessor.createHkAd(o, file, null, "青岛");
        assertEquals(result.getErrorCode(), Err.SUCCESS);
        this.commit();
    }

    public void testCreateData() throws IOException {
        long userId = 1;
        String name = "测试广告";
        int totalViewCount = 10000;
        byte showflg = HkAd.SHOWFLG_CHAR;
        String href = "http://www.163.com";
        String adData = "来看看啊";
        HkAd o = new HkAd();
        o.setUserId(userId);
        o.setName(name);
        o.setTotalViewCount(totalViewCount);
        o.setShowflg(showflg);
        o.setHref(href);
        CreateHkAdResult result = this.hkAdProcessor.createHkAd(o, null, adData, null);
        assertEquals(result.getErrorCode(), Err.SUCCESS);
        this.commit();
    }
}
