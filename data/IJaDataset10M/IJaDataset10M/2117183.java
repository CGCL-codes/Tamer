package br.gov.frameworkdemoiselle.monitoring.example.mbean;

import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;

/**
 * @author SERPRO
 */
@RunWith(DemoiselleRunner.class)
public class MBeanExecution {

    @Inject
    private EscolaMonitoringMBean escola;

    @Inject
    private MyMonitoringMBean mbean;

    @Test
    public void init() {
        escola.getVersaoAplicacao();
        mbean.getDummy();
    }
}
