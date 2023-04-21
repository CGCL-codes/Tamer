package net.taylor.sample.listener;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.mail.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SampleMailListenerMDB {

    private static final Log log = LogFactory.getLog(SampleMailListenerMDB.class);

    @Resource
    private MessageDrivenContext ctx;

    public void onMessage(Message msg) {
        log.info(ctx);
        log.info(msg);
    }
}
