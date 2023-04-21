package jk.spider.core.task.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import jk.spider.core.SpiderController;
import jk.spider.core.storage.Storage;
import jk.spider.core.task.threading.MonitorThread;
import jk.spider.model.ProxyInfo;
import jk.spider.util.SpiderUtil;
import jk.spider.util.config.ConfigConstants;
import jk.spider.util.config.PropertySet;
import org.apache.log4j.Logger;

public class ProxyValidMonitor extends MonitorThread {

    protected static final Logger log = Logger.getLogger(ProxyValidMonitor.class);

    protected static final String DEFAULT_URL = "http://www.dianping.com/shoplist/all_1_10";

    protected Storage storage;

    protected PropertySet propSet;

    protected int sCount;

    protected int pCount;

    protected String monitorUrl = "";

    public ProxyValidMonitor(SpiderController controller, int interval, int tCount) {
        super(controller, interval, tCount);
        storage = controller.getStorage();
        propSet = controller.getSpiderConfiguration().getProxyConfiguration();
        monitorUrl = propSet.getString(ConfigConstants.PROXY_MONITOR_URL, DEFAULT_URL);
    }

    @Override
    public void doMonitorTask() {
        SpiderUtil util = new SpiderUtil();
        sCount = 0;
        List<ProxyInfo> pxList = storage.getProxyDAO().getValidateProxy();
        if (pxList == null || pxList.isEmpty()) {
            try {
                TimeUnit.SECONDS.sleep(10);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (ProxyInfo proxy : pxList) {
            pCount++;
            int httpStatus = util.getHttpStatus(proxy, monitorUrl, controller.getCookie(), "www.dianping.com");
            if (httpStatus == 200) {
                storage.getProxyDAO().setMark(proxy.getPxId(), 1);
                controller.getContent().addProxyQueue(proxy);
                storage.getProxyDAO().setMark(proxy.getPxId(), 2);
                sCount++;
            }
        }
        log.info("���¼���[" + pCount + "]���Գ���Ч���� [ " + sCount + " ] ��");
    }
}
