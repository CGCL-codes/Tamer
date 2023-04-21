package tcbnet.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe responsavel por dividir e controlar recursos de banda (input e
 * output) de comunicacao entre Nodes. Ele e composto de:<br>
 * - Um conjunto de fontes de IO.<br>
 * - Um conjunto de callbacks para disparar fontes de IO quando prontos.<br>
 * - Um limite de banda por periodo de agendamento.<br>
 * - Um periodo de agendamento.<br>
 * Ele opera em colaboracao com as fontes de IO:<br>
 * 1) Cada fonte de IO (daqui pra frente chamados de BandwidthIO) registra seu
 * callback atraves do Scheduler, para que seja possivel desabilita-lo
 * temporariamente caso nos nao temos mais banda disponivel no periodo.<br>
 * 2) Cada BandwidthIO requer uma quantidade de banda que quer usar.<br>
 * 3) Depois de usar, cada BandwidthIO anuncia para o Scheduler o quanto de
 * banda realmente foi utilizado.<br>
 * Periodicamente, o Scheduler roda para calcular o quanto de banda estara
 * disponivel para o proximo periodo.<br>
 *
 * Esse codigo foi baseado no modulo bsched do gtk-gnutella escrito por
 * Raphael Manfredi.
 *
 * @author $Author: rcouto $
 * @version $Revision: 1.27 $
 */
public class BandwidthScheduler {

    /** Ultima hora que rodamos nosso periodo. */
    private long lastPeriod;

    /** Lista de BandwidthIO. */
    private List bios;

    /** Nome, por motivos de debug. */
    private String name;

    /** Periodo fixo de agendamento, em ms. */
    private long period;

    /** Periodo minimo sem correcao. */
    private long minPeriod;

    /** Periodo maximo sem correcao. */
    private long maxPeriod;

    /** EMA do periodo, em ms. */
    private long periodEMA;

    /** Bandwidth em bytes/s. */
    private long bwPerSecond;

    /** Bandwidth maximo por periodo. */
    private long bwMax;

    /** Bandwidth usada ate agora no periodo. */
    private long bwActual;

    /** Quanta basico de bandwidth por BandwidthIO. */
    private long bwSlot;

    /** EMA de bandwidth realmente usado. */
    private long bwEMA;

    /** Diferenca dinamica entre banda gasta actual e banda gasta teorica. */
    private long bwDelta;

    /** Flag que diz que o Scheduler esta ativo. */
    private boolean enabled;

    /** Flag que diz que o Scheduler nao tem mais banda disponivel. */
    private boolean noBW;

    /** Flag que diz que o valor de bwSlot esta congelado. */
    private boolean frozenSlot;

    /** Bandwidth/slot minimo para realocacao. */
    private static long SLOT_MIN = 256;

    /**
     * Construtor.
     *
     * @param name Nome do agendador, por motivos de debug.
     * @param bandwidth A banda espera em bytes por segundo.
     * @param period O periodo em milissegundos.
     */
    public BandwidthScheduler(String name, int bandwidth, long period) {
        if (bandwidth < 0) throw new IllegalArgumentException("bandiwth < 0");
        if (period <= 0) throw new IllegalArgumentException("period <= 0");
        this.name = name;
        this.period = period;
        this.enabled = false;
        this.noBW = false;
        this.frozenSlot = false;
        this.bios = new ArrayList();
        this.minPeriod = period >> 1;
        this.minPeriod = period << 1;
        this.periodEMA = period;
        this.bwPerSecond = bandwidth;
        this.bwMax = bandwidth * period / 1000;
    }

    /**
     * Ativa o agendamento, marca o inicio do periodo.
     */
    public synchronized void enable() {
        this.enabled = true;
        this.lastPeriod = System.currentTimeMillis();
    }

    /**
     * Adiciona um BandwidthIO na lista de fontes de IO do agendador.
     *
     * @param bio BandwidthIO a ser adicionado.
     */
    public synchronized void addBandwidthIO(BandwidthIO bio) {
        this.bios.add(bio);
        this.bwSlot = this.bwMax / this.bios.size();
        if (this.bwSlot < BandwidthScheduler.SLOT_MIN) this.frozenSlot = true;
    }

    /**
     * Remove um BandwidthIO da lista de fontes de IO do agendador.
     *
     * @param bio BandwidthIO a ser removido.
     */
    public synchronized void removeBandwidthIO(BandwidthIO bio) {
        this.bios.remove(bio);
        if (this.bios.size() > 0) this.bwSlot = this.bwMax / this.bios.size();
    }

    /**
     * Transfere no maximo 'len' bytes do/para o buffer de um BandwidthIO,
     * conforme permite a banda.
     *
     * @param bio BandwidthIO cujo buffer sera lido/escrito.
     * @param len Numero maximo de bytes para ser transmitido.
     * @return Quantos bytes foram realmente transmitidos.
     */
    public long doIO(BandwidthIO bio, long len) {
        if (len < 0) throw new IllegalArgumentException("len < 0");
        long available = this.available(bio, len);
        if (available == 0) return 0;
        long amount = len > available ? available : len;
        bio.activate();
        long transf = bio.callbackIO(amount);
        if (transf > 0) {
            this.bwUpdate(transf);
            bio.addBwActual(transf);
        }
        return transf;
    }

    /**
     * Retorna a banda disponivel para um determinado BandwidthIO.
     *
     * @param bio BandwidthIO que quer transmitir.
     * @param len Quantidade de bytes requeridos pela aplicacao.
     * @return A banda disponivel para um determinado BandwidthIO.
     */
    private synchronized long available(BandwidthIO bio, long len) {
        long available;
        if (!this.enabled) return len;
        if (this.noBW) return 0;
        available = this.bwMax - this.bwActual;
        if (!this.frozenSlot && (available > BandwidthScheduler.SLOT_MIN) && (bio.active())) {
            long slot = available / this.bios.size();
            if (slot > BandwidthScheduler.SLOT_MIN) {
                this.clearActive();
                this.bwSlot = slot;
            } else {
                this.frozenSlot = true;
                this.bwSlot = BandwidthScheduler.SLOT_MIN;
            }
        }
        if (available <= 0) {
            this.noMoreBandwidth();
            available = 0;
        }
        return Math.min(this.bwSlot, available);
    }

    /**
     * Atualiza a banda usada, e estatisticas do agendador. Se nao ha mais
     * banda disponivel, desativa todas as bios.
     *
     * @param used A quantidade de bytes transmitidos.
     */
    private synchronized void bwUpdate(long used) {
        if (!this.enabled) return;
        this.bwActual += used;
        if (this.bwActual >= this.bwMax) this.noMoreBandwidth();
    }

    /**
     * Remove a indicacao de ativo de todos os bios.
     */
    private synchronized void clearActive() {
        Iterator i = this.bios.iterator();
        while (i.hasNext()) {
            BandwidthIO bio = (BandwidthIO) i.next();
            bio.activate();
        }
    }

    /**
     * Desativa todas as bios e o agendador porque nao temos mais banda.
     */
    private synchronized void noMoreBandwidth() {
        Iterator i = this.bios.iterator();
        while (i.hasNext()) {
            BandwidthIO bio = (BandwidthIO) i.next();
            if (bio.enabled()) bio.disable();
        }
        this.noBW = true;
    }

    /**
     * Metodo chamado a cada vez que uma nova fatia de tempo comeca. Re-ativa
     * todas as bios e indica que possuimos banda. Atualiza as estaticas por
     * fonte de IO. Limpa todas as indicacoes de ativacao nas bios.
     */
    private synchronized void beginTimeSlice() {
        Iterator i = this.bios.iterator();
        while (i.hasNext()) {
            BandwidthIO bio = (BandwidthIO) i.next();
            bio.beginTimeSlice();
        }
        this.noBW = false;
        this.frozenSlot = false;
        if (this.bwSlot < BandwidthScheduler.SLOT_MIN) this.frozenSlot = true;
    }

    /**
     * Atualizacao do periodo.
     *
     * @param time Hora atual.
     */
    private synchronized void heartBeat(long time) {
        long delay, overused, theoric, correction;
        if (this.enabled) {
            delay = time - this.lastPeriod;
            this.lastPeriod = time;
            if (delay < this.minPeriod) {
                delay = this.period;
            } else if (delay > this.maxPeriod) {
                delay = this.period;
            }
            if (delay < 0) throw new IllegalStateException("delay < 0");
            this.periodEMA += (delay >> 2) - (this.periodEMA >> 2);
            this.bwEMA += (this.bwActual >> 2) - (this.bwEMA >> 2);
            if (this.periodEMA < 0) throw new IllegalStateException("this.period < 0");
            if (this.bwEMA < 0) throw new IllegalStateException("this.bwEMA < 0");
            theoric = this.bwPerSecond * delay / 1000;
            overused = this.bwActual - theoric;
            this.bwDelta += overused;
            this.bwMax = this.bwPerSecond * this.periodEMA / 1000;
            correction = this.bwEMA - theoric;
            correction = Math.max(correction, overused);
            if (correction > 0) {
                this.bwMax -= correction;
                if (this.bwMax < 0) this.bwMax = 0;
            }
            if (this.bios.size() > 0) this.bwSlot = this.bwMax / this.bios.size(); else this.bwSlot = 0;
        }
        this.bwActual = 0;
        this.beginTimeSlice();
    }

    /**
     * Classe responsavel pela execucao periodica de atualizacoes.
     */
    class BandwidthSchedulerTimer extends TimerTask {

        private BandwidthScheduler bs;

        /**
	 * Construtor.
	 *
	 * @param bs BandwidthScheduler para atualizar.
	 */
        public BandwidthSchedulerTimer(BandwidthScheduler bs) {
            this.bs = bs;
        }

        /**
	 * Tarefa a ser executada periodicamente.
	 */
        public void run() {
            this.bs.heartBeat(System.currentTimeMillis());
        }
    }
}
