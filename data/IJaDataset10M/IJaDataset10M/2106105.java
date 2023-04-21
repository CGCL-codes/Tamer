package br.gov.frameworkdemoiselle.sample.monitoring.mbean;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import br.gov.component.demoiselle.monitoring.jmx.MBean;
import br.gov.component.demoiselle.monitoring.snmp.AccessMode;
import br.gov.component.demoiselle.monitoring.snmp.ManagedObject;
import br.gov.component.demoiselle.monitoring.snmp.ManagementBase;

@MBean(name = "br.gov.demoiselle:name=Escola")
@ManagementBase(prefix = ".25.3.1")
public class EscolaMonitoring implements EscolaMonitoringMBean {

    private long qtdAlunos = 0;

    @ManagedObject(oid = ".1", type = Gauge32.class)
    public long getQtdAlunosMatriculados() {
        this.qtdAlunos = (int) (Math.random() * 100) + 100;
        return this.qtdAlunos;
    }

    private int qtdTurmas = 0;

    @ManagedObject(oid = ".2", type = Counter32.class)
    public int getQtdTurmasIncluidas() {
        this.qtdTurmas += (int) (Math.random() * 10);
        return this.qtdTurmas;
    }

    private static final String[] USUARIOS = { "Fulano", "Sicrano", "Beltrano" };

    @ManagedObject(oid = ".3", type = OctetString.class)
    public String getUltimoUsuarioLogado() {
        int pos = (int) (Math.random() * USUARIOS.length);
        return USUARIOS[pos];
    }

    private static final String VERSAO = "2.4.1-RC2";

    @ManagedObject(oid = ".4")
    public String getVersaoAplicacao() {
        return VERSAO;
    }

    @ManagedObject(oid = ".5", access = AccessMode.READ_WRITE, type = Integer32.class, allowedValues = { 1, 2, 3, 4 })
    private int nivelLog = 1;

    public int getNivelLog() {
        return this.nivelLog;
    }

    public void setNivelLog(final int nivelLog) {
        this.nivelLog = nivelLog;
    }
}
