package org.tolk.io.extension.ipico;

import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.tolk.ApplicationContextFactory;
import org.tolk.io.impl.TextFileDataSource;
import org.tolk.ipico.util.IpicoUtil;

/**
 * Class to read IPICO strings from a text file and replay them with intervals between strings. The interval is a function of the
 * date/time separation between consecutive strings based on the IPICO Serial reader protocol's date/time stamp.
 * 
 * @author Werner van Rensburg
 * 
 */
public class TextFileReplayDataSource extends TextFileDataSource implements InitializingBean, DisposableBean {

    private IpicoUtil ipicoUtil;

    private GregorianCalendar previousTime;

    private final Logger logger = Logger.getLogger(TextFileReplayDataSource.class);

    @Override
    public void forwardMessage(String message) {
        long delay;
        logger.info(message);
        if (getIpicoUtil().isValidTag(message)) {
            if (previousTime != null) {
                delay = this.getTimeDiffMilliSeconds(previousTime, getIpicoUtil().getGregorianCalendarDate(message));
                if (delay >= 100) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        logger.info(e.getMessage());
                    }
                }
            }
            previousTime = getIpicoUtil().getGregorianCalendarDate(message);
            this.fwdMessageToAllNextNodes(message);
        }
    }

    /**
     * getTimeDiffMilliSeconds() is a function to return the difference between to times
     * 
     * @parms prev variable indicating the older time curr variable indicating the younger time
     * @return (long) diff the difference between the two times in milliseconds
     */
    public long getTimeDiffMilliSeconds(GregorianCalendar prev, GregorianCalendar curr) {
        Long diff = curr.getTimeInMillis() - prev.getTimeInMillis();
        return diff;
    }

    /**
     * see {@link InitializingBean#afterPropertiesSet()}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        previousTime = null;
        super.afterPropertiesSet();
    }

    /**
     * see {@link DisposableBean#destroy()}
     */
    @Override
    public void destroy() throws Exception {
        super.destroy();
    }

    private IpicoUtil getIpicoUtil() {
        if (ipicoUtil == null) {
            ipicoUtil = (IpicoUtil) ApplicationContextFactory.getBean(IpicoUtil.BEAN_NAME);
        }
        return ipicoUtil;
    }
}
