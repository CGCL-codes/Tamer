package org.jcompany.control.appender.jms;

import java.util.Date;
import java.util.Timer;
import org.apache.log4j.Logger;
import org.jcompany.commons.PlcException;
import org.jcompany.control.PlcConstants;
import org.jcompany.control.appender.PlcBaseAppender;
import org.jcompany.control.appender.jms.helper.PlcJMonitorMsg;
import org.jcompany.control.batch.PlcBaseTimerTask;

/**
 * Dispara uma mensagem a ser enviada via JMS para atualizar o "tempo no ar" da
 * aplica��o.
 * 
 * @since jCompany 3.0
 * @version $Id: PlcJMonitorTimerTask.java,v 1.7 2006/07/29 12:56:17 alvim Exp $
 * @author Roberto Badar�
 */
public class PlcJMonitorTimerTask extends PlcBaseTimerTask {

    private static final Logger log = Logger.getLogger(PlcJMonitorTimerTask.class);

    private PlcJMonitorMsg msg = new PlcJMonitorMsg();

    @Override
    protected void runApi() throws PlcException {
        msg.setDate(new Date());
        log.info(msg);
    }

    /**
     * Verifica se h� appender JMS configurado e, em caso positivo, configura o
     * TimerTask para executar a cada 5 minutos.
     */
    public static void start() {
        if (PlcBaseAppender.getInstance().getAppender(PlcConstants.JMS.CLASS_JMS_SEND_EMAIL) != null) {
            long delay = 10 * 60 * 1000;
            long repetir = 5 * 60 * 1000;
            Timer timer = new Timer(true);
            timer.schedule(new PlcJMonitorTimerTask(), delay, repetir);
        }
    }
}
