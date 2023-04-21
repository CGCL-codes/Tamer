package org.opensih.temporizador;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensih.controladores.IEnvioMail;

/**
 * Implementaci�n del servicio @local IAlarmScheduler.
 */
@Stateless
public class AlarmScheduler implements IAlarmScheduler {

    private static final Log log = LogFactory.getLog(AlarmScheduler.class);

    @EJB
    IEnvioMail logica_EMail;

    /** Injecci�n del TimerService */
    @Resource
    TimerService timerService;

    /** Hora de ejecuci�n: 00 horas */
    private static final int START_HOUR = 0;

    /** Minutos de ejecuci�n: 00 minutos */
    private static final int START_MINUTES = 0;

    /** Segundos de ejecuci�n: 00  */
    private static final int START_SECONDS = 0;

    /** Intervalo de la ejecuci�n: 1440 = 24 horas */
    private static final int INTERVAL_IN_MINUTES = 1440;

    /**
	 * Levanta el servicio 
	 */
    public void startUpTimer() {
        log.info("startUpTimer - alarm scheduler service is active.");
        shutDownTimer();
        Calendar initialExpiration = Calendar.getInstance();
        initialExpiration.set(Calendar.HOUR_OF_DAY, START_HOUR);
        initialExpiration.set(Calendar.MINUTE, START_MINUTES);
        initialExpiration.set(Calendar.SECOND, START_SECONDS);
        long intervalDuration = new Integer(INTERVAL_IN_MINUTES).longValue() * 60 * 1000;
        log.info("startUpTimer - create new timer service at \"" + initialExpiration.getTime() + "\", with \"" + intervalDuration + "\" interval in milis.");
        timerService.createTimer(initialExpiration.getTime(), intervalDuration, null);
    }

    /**
	 * Para el servicio 
	 */
    public void shutDownTimer() {
        Collection<Timer> timers = timerService.getTimers();
        log.info("shutDownTimer - existing timers? " + timers);
        if (timers != null) {
            for (Iterator iterator = timers.iterator(); iterator.hasNext(); ) {
                Timer t = (Timer) iterator.next();
                t.cancel();
                log.info("shutDownTimer - timer \"" + t + "\" canceled.");
            }
        }
    }

    /**
	 *  m�todo callback que se invocar� al terminar el intervalo definido
	 */
    @Timeout
    public void execute(Timer timer) {
        log.info("executing");
        Calendar today = new GregorianCalendar();
        today.setTime(new Date());
        if (today.get(Calendar.DATE) == 1) {
            System.out.println("envio mail");
            logica_EMail.enviarEmail("Va el reporte, salute", "Informe Mensual");
        }
    }
}
