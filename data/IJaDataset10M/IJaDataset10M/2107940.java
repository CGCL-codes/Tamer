package org.opensih.documentSourceXDR.temporizador;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import org.opensih.documentSourceXDR.SenderDocument;
import org.opensih.documentSourceXDR.management.Mbean;
import org.opensih.documentSourceXDR.management.ParametersManagement;

@Stateless
public class TemporizadorBean implements Temporizador {

    @Resource
    TimerService ServicioReloj;

    @EJB
    SenderDocument logica;

    public void comienzaReloj() {
        ParametersManagement param = Mbean.getProxyParameters();
        System.out.println("startUpTimer - comienza servicio de timer");
        terminoReloj();
        long intervalDuration = new Integer(param.getMinutosTimer()).longValue() * 60 * 1000;
        Date now = new Date();
        System.out.println("startUpTimer - se crea nuevo timer para atender el servicio en " + intervalDuration + ServicioReloj.createTimer(now, intervalDuration, null) + " tiempo.");
    }

    public void terminoReloj() {
        Collection<Timer> timers = ServicioReloj.getTimers();
        if (timers != null) {
            for (Iterator iterator = timers.iterator(); iterator.hasNext(); ) {
                Timer t = (Timer) iterator.next();
                t.cancel();
                System.out.println("shutDownTimer - timer \"" + t + "\" canceled.");
            }
        }
    }

    @Timeout
    public void execute(Timer timer) {
        try {
            logica.publicarDocumentos();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Accion temporizada ejecutadando presento error: " + e.getMessage());
        }
        System.out.println("Accion temporizada ejecutada y finalizada");
    }
}
