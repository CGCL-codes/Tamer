package net.sourceforge.simulaeco.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import net.sourceforge.simulaeco.gui.exception.ConfigParamException;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

@SuppressWarnings("serial")
public class DlgPreferenciasGenetico extends JXPanel {

    private JXLabel quantidadeGeracoes;

    private JTextField campoQuantidadeGeracoes;

    private JXLabel tamanhoCenario;

    private JTextField campoTamanhoCenario;

    private JXLabel tamanhoRegiao;

    private JTextField campoTamanhoRegiao;

    private JXLabel tamanhoPopulacao;

    private JTextField campoTamanhoPopulacao;

    private JXLabel taxaMutacao;

    private JSpinner campoTaxaMutacao;

    private JXLabel fatorMutacao;

    private JSpinner campoFatorMutacao;

    private JXLabel taxaReproducao;

    private JSpinner campoTaxaReproducao;

    public DlgPreferenciasGenetico() {
        super();
        this.setName("painelAlgoritmoGenetico");
        this.inicializaComponentes();
        this.montaLayout();
        SimulaECO.getAplicacao().getContext().getResourceMap(DlgPreferenciasGenetico.class).injectComponents(this);
    }

    private void inicializaComponentes() {
        this.quantidadeGeracoes = new JXLabel();
        this.quantidadeGeracoes.setName("quantidadeGeracoes");
        this.campoQuantidadeGeracoes = new JTextField();
        this.campoQuantidadeGeracoes.setName("campoQuantidadeGeracoes");
        this.tamanhoCenario = new JXLabel();
        this.tamanhoCenario.setName("tamanhoCenario");
        this.campoTamanhoCenario = new JTextField();
        this.campoTamanhoCenario.setName("campoTamanhoCenario");
        this.tamanhoRegiao = new JXLabel();
        this.tamanhoRegiao.setName("tamanhoRegiao");
        this.campoTamanhoRegiao = new JTextField();
        this.campoTamanhoRegiao.setName("campoTamanhoRegiao");
        this.tamanhoPopulacao = new JXLabel();
        this.tamanhoPopulacao.setName("tamanhoPopulacao");
        this.campoTamanhoPopulacao = new JTextField();
        this.campoTamanhoPopulacao.setName("campoTamanhoPopulacao");
        this.taxaMutacao = new JXLabel();
        this.taxaMutacao.setName("taxaMutacao");
        SpinnerModel modeloSpinMutacao = new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01);
        this.campoTaxaMutacao = new JSpinner(modeloSpinMutacao);
        this.campoTaxaMutacao.setName("campoTaxaMutacao");
        this.fatorMutacao = new JXLabel();
        this.fatorMutacao.setName("fatorMutacao");
        SpinnerModel modeloSpinFator = new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01);
        this.campoFatorMutacao = new JSpinner(modeloSpinFator);
        this.campoFatorMutacao.setName("campoFatorMutacao");
        this.taxaReproducao = new JXLabel();
        this.taxaReproducao.setName("taxaReproducao");
        SpinnerModel modeloSpinRepro = new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01);
        this.campoTaxaReproducao = new JSpinner(modeloSpinRepro);
        this.campoTaxaReproducao.setName("campoTaxaReproducao");
    }

    private void montaLayout() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = null;
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.quantidadeGeracoes, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.NONE;
        this.add(this.campoQuantidadeGeracoes, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.tamanhoCenario, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.NONE;
        this.add(this.campoTamanhoCenario, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.tamanhoRegiao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.NONE;
        this.add(this.campoTamanhoRegiao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.tamanhoPopulacao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.NONE;
        this.add(this.campoTamanhoPopulacao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.taxaMutacao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.campoTaxaMutacao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.fatorMutacao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.campoFatorMutacao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.anchor = GridBagConstraints.EAST;
        gridConstraints.insets = new Insets(15, 3, 15, 10);
        this.add(this.taxaReproducao, gridConstraints);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.campoTaxaReproducao, gridConstraints);
    }

    public void validacao() throws ConfigParamException {
        int intCenario, intRegiao;
        try {
            intCenario = Integer.parseInt(this.getCampoTamanhoCenario());
        } catch (NumberFormatException e) {
            throw new ConfigParamException("O par�metro Tamanho Cen�rio(N) n�o � um n�mero.", e);
        }
        try {
            intRegiao = Integer.parseInt(this.getCampoTamanhoRegiao());
        } catch (NumberFormatException e) {
            throw new ConfigParamException("O par�metro Tamanho Regi�o(Tr) n�o � um n�mero.", e);
        }
        int qtdRegioes = 0;
        int tamIgual = 0;
        try {
            qtdRegioes = (int) Math.pow((intCenario / intRegiao), 2);
            tamIgual = intCenario % intRegiao;
        } catch (ArithmeticException e) {
            throw new ConfigParamException(e);
        }
        if (qtdRegioes < 16 || tamIgual != 0) throw new ConfigParamException("O par�metro Tamanho Cen�rio(N) e Tamanho Regi�o(Tr) n�o s�o compat�veis.");
    }

    /**
     * @return {@link String} tamanhoCenario
     */
    public String getCampoQuantidadeGeracoes() {
        return this.campoQuantidadeGeracoes.getText();
    }

    /**
     * @param campoTamanhoCenario
     *            the campoTamanhoCenario to set
     */
    public void setCampoQuantidadeGeracoes(String campoQuantidadeGeracoes) {
        this.campoQuantidadeGeracoes.setText(campoQuantidadeGeracoes);
    }

    /**
     * @return {@link String} tamanhoCenario
     */
    public String getCampoTamanhoCenario() {
        return this.campoTamanhoCenario.getText();
    }

    /**
     * @param campoTamanhoCenario
     *            the campoTamanhoCenario to set
     */
    public void setCampoTamanhoCenario(String campoTamanhoCenario) {
        this.campoTamanhoCenario.setText(campoTamanhoCenario);
    }

    /**
     * @return {@link String} tamanhoRegiao
     */
    public String getCampoTamanhoRegiao() {
        return this.campoTamanhoRegiao.getText();
    }

    /**
     * @param campoTamanhoRegiao
     *            the campoTamanhoRegiao to set
     */
    public void setCampoTamanhoRegiao(String campoTamanhoRegiao) {
        this.campoTamanhoRegiao.setText(campoTamanhoRegiao);
    }

    /**
     * @param campoTamanhoPopulacao
     *            the campoTamanhoPopulacao to set
     */
    public void setCampoTamanhoPopulacao(String tamanhoPopulacao) {
        this.campoTamanhoPopulacao.setText(tamanhoPopulacao);
    }

    /**
     * @return {@link String} tamanhoPopulacao
     */
    public String getCampoTamanhoPopulacao() {
        return this.campoTamanhoPopulacao.getText();
    }

    /**
     * @param campoTaxaMutacao
     *            the campoTaxaMutacao to set
     */
    public void setCampoTaxaMutacao(String taxaMutacao) {
        this.campoTaxaMutacao.setValue(Double.parseDouble(taxaMutacao));
    }

    /**
     * @return {@link String} taxaMutacao
     */
    public String getCampoTaxaMutacao() {
        return Double.toString((Double) this.campoTaxaMutacao.getValue());
    }

    /**
     * @param campoFatorMutacao
     *            the campoFatorMutacao to set
     */
    public void setCampoFatorMutacao(String fatorMutacao) {
        this.campoFatorMutacao.setValue(Double.parseDouble(fatorMutacao));
    }

    /**
     * @return {@link String} fatorMutacao
     */
    public String getCampoFatorMutacao() {
        return Double.toString((Double) this.campoFatorMutacao.getValue());
    }

    /**
     * @param campoTaxaReproducao
     *            the campoTaxaReproducao to set
     */
    public void setCampoTaxaReproducao(String taxaReproducao) {
        this.campoTaxaReproducao.setValue(Double.parseDouble(taxaReproducao));
    }

    /**
     * @return {@link String} taxaReproducao
     */
    public String getCampoTaxaReproducao() {
        return Double.toString((Double) this.campoTaxaReproducao.getValue());
    }
}
