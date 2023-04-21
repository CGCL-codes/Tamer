package org.ss.mobot.core.backbone;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import org.ss.mobot.core.BotConstant;
import org.ss.util.Configure;
import org.ss.util.Ini4jSimple;

@SuppressWarnings("unchecked")
public class BotBone {

    private static Logger log = Logger.getLogger(BotBone.class.getSimpleName());

    private final BlockingQueue que;

    public BotBone() {
        que = new LinkedBlockingQueue();
    }

    public void setProxy() {
        try {
            String iniFileName = Configure.getInstance().getString(BotConstant.CONFIGURE_INIFILE_KEY);
            Ini4jSimple ini4js = new Ini4jSimple(iniFileName);
            String host = ini4js.fetch(BotConstant.INISEC_PROXY, "socksProxyHost");
            int port = Integer.parseInt(ini4js.fetch(BotConstant.INISEC_PROXY, "socksProxyPort"));
            if (host != null && port > 0) {
                System.getProperties().put("socksProxyHost", host);
                System.getProperties().put("socksProxyPort", port);
                log.info("proxy has been set to " + host + ":" + port);
            }
        } catch (Exception e) {
            log.info("skip setting proxy: " + e.getMessage());
        } finally {
        }
    }

    public void begin() {
        if (que != null) log.info("Message Que has been initialized.");
        QReadThread qrt = new QReadThread(que);
        Thread t1 = new Thread(qrt);
        t1.setName("QReadThread");
        t1.start();
        QWriteThread qwt = new QWriteThread(que);
        Thread t2 = new Thread(qwt);
        t2.setName("QWriteThread");
        t2.start();
    }
}
