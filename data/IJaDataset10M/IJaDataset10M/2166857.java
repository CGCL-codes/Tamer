package net.sourceforge.simulaeco.gui.acoes;

import java.util.Properties;
import javax.swing.ActionMap;
import net.sourceforge.simulaeco.automato.JogoDaVidaImpl;
import net.sourceforge.simulaeco.constantes.Constantes;
import net.sourceforge.simulaeco.genetico.AlgoritmoGenetico;
import net.sourceforge.simulaeco.gui.JanelaPrincipal;
import net.sourceforge.simulaeco.gui.SimulaECO;
import net.sourceforge.simulaeco.gui.utils.AnaliseDados;
import net.sourceforge.simulaeco.gui.utils.Configuracoes;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationAction;

/**
 * @author Rafael Gonzaga Camargo
 * @date 03/03/2010 {@literal Descri��o: ???}
 */
public class SimulacaoAction implements Runnable {

    private static final Logger LOG = Logger.getLogger(SimulacaoAction.class);

    private static SimulacaoAction instance;

    private AlgoritmoGenetico genetico;

    private Thread simulacaoThread;

    private ApplicationAction inicio;

    private ApplicationAction pausa;

    private ApplicationAction pare;

    private JogoDaVidaImpl automato;

    /**
     * A��es do processo de simula��o
     * 
     * @return {@link SimulacaoAction}
     */
    public static final SimulacaoAction getInstance() {
        if (instance == null) instance = new SimulacaoAction();
        return instance;
    }

    /**
     * Construtor padr�o oculto pelo Singleton
     */
    private SimulacaoAction() {
        super();
        ActionMap acoes = SimulaECO.getAplicacao().getContext().getActionMap(this);
        this.inicio = (ApplicationAction) acoes.get("inicio");
        this.pausa = (ApplicationAction) acoes.get("pausa");
        this.pare = (ApplicationAction) acoes.get("pare");
    }

    /**
     * Respons�vel por iniciar o processo de simula��o
     */
    @Action
    public void inicio() {
        Properties cfg = Configuracoes.getInstance().getConfiguracoes();
        int tamanhoPopulacao = Integer.parseInt((String) cfg.get(Constantes.TAMANHO_POPULACAO));
        int tamanhoCenario = Integer.parseInt((String) cfg.get(Constantes.TAMANHO_CENARIO));
        double taxaMutacao = Double.parseDouble((String) cfg.get(Constantes.TAXA_MUTACAO));
        double taxaReproducao = Double.parseDouble((String) cfg.get(Constantes.TAXA_REPRODUCAO));
        double fatorMutacao = Double.parseDouble((String) cfg.get(Constantes.FATOR_MUTACAO));
        int tamanhoRegiao = Integer.parseInt((String) cfg.get(Constantes.TAMANHO_REGIAO));
        int tamanhoCromossomo = (int) Math.pow((tamanhoCenario / tamanhoRegiao), 2);
        int quantidadeGeracoes = Integer.parseInt((String) cfg.get(Constantes.QUANTIDADE_GERACOES));
        ;
        int qtdPresas = Integer.parseInt((String) cfg.get(Constantes.QTD_PRESAS));
        double nivelPresas = Double.parseDouble((String) cfg.get(Constantes.NIVEL_PRESAS));
        int natalidadePresas = Integer.parseInt((String) cfg.get(Constantes.NATALIDADE_PRESAS));
        int mortalidadePresas = Integer.parseInt((String) cfg.get(Constantes.NATALIDADE_PREDADORES));
        int qtdPredadores = Integer.parseInt((String) cfg.get(Constantes.QTD_PREDADORES));
        double nivelPredadores = Double.parseDouble((String) cfg.get(Constantes.NIVEL_PREDADORES));
        int natalidadePredadores = Integer.parseInt((String) cfg.get(Constantes.NATALIDADE_PREDADORES));
        int mortalidadePredadores = Integer.parseInt((String) cfg.get(Constantes.MORTALIDADE_PREDADORES));
        int tempoExecucao = Integer.parseInt((String) cfg.get(Constantes.TEMPO_EXECUCAO));
        JanelaPrincipal jnlPrincipal = (JanelaPrincipal) SimulaECO.getAplicacao().getMainView();
        this.genetico = new AlgoritmoGenetico(tamanhoPopulacao, tamanhoCromossomo, quantidadeGeracoes, taxaMutacao, taxaReproducao, fatorMutacao);
        this.genetico.addPropertyChangeListener("geracaoAtual", jnlPrincipal.getPanelIndicadores().new GeracaoGeneticoListener());
        this.automato = jnlPrincipal.getJogoDaVida();
        this.automato.setTamanhoRegiao(tamanhoRegiao);
        this.automato.setTempoExecucao(tempoExecucao);
        this.automato.setQtdPresas(qtdPresas);
        this.automato.setNivelPresas(nivelPresas);
        this.automato.setNatalidadePresas(natalidadePresas);
        this.automato.setMortalidadePresas(mortalidadePresas);
        this.automato.setQtdPredadores(qtdPredadores);
        this.automato.setNivelPredadores(nivelPredadores);
        this.automato.setNatalidadePredadores(natalidadePredadores);
        this.automato.setMortalidadePredadores(mortalidadePredadores);
        this.automato.addPropertyChangeListener("simAptidao", jnlPrincipal.getPanelIndicadores().new AptidaoGeneticoListener());
        this.automato.addPropertyChangeListener("simAptidao", jnlPrincipal.getPanelIndicadores().getGrafAptidao().getColetor().new AptidaoListener());
        this.automato.addPropertyChangeListener("simPresas", jnlPrincipal.getPanelIndicadores().new PresaListener());
        this.automato.addPropertyChangeListener("simPresas", jnlPrincipal.getPanelIndicadores().getGrafEspecies().getColetorPresa().new EspecieListener());
        this.automato.addPropertyChangeListener("simPredadores", jnlPrincipal.getPanelIndicadores().new PredadorListener());
        this.automato.addPropertyChangeListener("simPredadores", jnlPrincipal.getPanelIndicadores().getGrafEspecies().getColetorPredador().new EspecieListener());
        this.automato.addPropertyChangeListener("simPaisagem", jnlPrincipal.getPanelIndicadores().new PaisagemListener());
        this.automato.addPropertyChangeListener("simMancha", jnlPrincipal.getPanelIndicadores().new ManchaListener());
        this.genetico.setFuncaoAptidao(automato);
        int idSimulacao = AnaliseDados.armazenaSimulacao(18, "Simulacao");
        this.automato.setIdSimulacao(idSimulacao);
        this.estadoBotoesInicio();
        this.simulacaoThread = new Thread(this);
        this.simulacaoThread.start();
    }

    private void estadoBotoesInicio() {
        this.inicio.setEnabled(false);
        this.pausa.setEnabled(true);
        this.pare.setEnabled(true);
    }

    /**
     * Respons�vel por pausar o processo de simula��o. {@literal Se o processo
     * de simula��o estiver em execu��o este m�todo ser� respons�vel por par�-lo
     * caso contr�rio colocar� em execu��o.}
     */
    @Action
    public void pausa() {
        if (this.simulacaoThread != null) {
            if (LOG.isDebugEnabled()) LOG.debug("Pausa simulacao");
            this.simulacaoThread = null;
        } else {
            if (LOG.isDebugEnabled()) LOG.debug("Reinicia simulacao");
            this.simulacaoThread = new Thread(this);
            this.simulacaoThread.start();
        }
    }

    /**
     * Respons�vel por para o processo de simula��o. {@literal Este m�todo
     * finaliza a simula��o.}
     */
    @Action
    public void pare() {
        if (LOG.isDebugEnabled()) LOG.debug("Para simulacao");
        this.simulacaoThread = null;
        this.estadoBotoesPare();
    }

    private void estadoBotoesPare() {
        this.inicio.setEnabled(true);
        this.pausa.setEnabled(false);
        this.pare.setEnabled(false);
    }

    @Override
    public void run() {
        while (this.simulacaoThread != null && this.genetico.getGeracaoAtual() < this.genetico.getQuantidadeGeracoes()) {
            if (LOG.isDebugEnabled()) LOG.debug("AG, geracao= " + this.genetico.getGeracaoAtual());
            this.genetico.setGeracaoAtual(this.genetico.getGeracaoAtual() + 1);
            this.genetico.executar();
        }
        if (this.genetico.getGeracaoAtual() == this.genetico.getQuantidadeGeracoes()) {
            if (LOG.isDebugEnabled()) LOG.debug("Chegou ao limite de geracoes.");
            this.automato.novo();
            pare();
        }
    }
}
