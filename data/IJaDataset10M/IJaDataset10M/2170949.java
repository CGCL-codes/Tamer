package controller;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;
import model.Compartimento;
import model.Cor;
import model.Filtro;
import model.Peca;
import model.Peca.Tecido;
import model.TipoDePeca;
import view.peca.TelaCadastrarPeca;
import view.peca.TelaExcluirPeca;

/**
 *
 * @author a5432168
 */
public class ControlePeca {

    private Davi davi;

    private TelaCadastrarPeca telaCadastrarPeca;

    private TelaExcluirPeca telaExcluirPeca;

    private ArrayList<Compartimento.infoCompartimento> sugestoesCompartimento;

    private PecaAux pecaAux;

    private int numeroSugestao;

    public ControlePeca(Davi davi) {
        this.davi = davi;
    }

    public boolean confirmaCompartimento() {
        if (!Peca.existe(pecaAux.codigoPeca)) {
            Peca.cadastrar(pecaAux.codigoPeca, sugestoesCompartimento.get(numeroSugestao).getIdCompartimento(), sugestoesCompartimento.get(numeroSugestao).getIdLista(), pecaAux.tecido, pecaAux.cor, pecaAux.tipoDePeca, pecaAux.filtros, davi.getMorador());
            Peca peca = new Peca(pecaAux.codigoPeca);
            peca.setDisponivel(true);
            return true;
        }
        return false;
    }

    public void pedidoExcluirCadastro(String codigoPeca) {
        if (!Peca.existe(codigoPeca)) {
            telaExcluirPeca.informa("Peça não cadastrada");
            return;
        }
        Peca peca = new Peca(codigoPeca);
        if (telaExcluirPeca.confirmacao(peca.toString())) {
            peca.excluirCadastro(davi.getMorador());
            telaExcluirPeca.informa("Peça " + peca.toString() + " excluida com sucesso");
        } else telaExcluirPeca.informa("Exclusão de cadastro abortada");
    }

    public void pedidoVoltarConfiguracoesPeca(JPanel telaAtual) {
        davi.pedidoConfigPeca(telaAtual);
    }

    public void trocarCompartimento() {
        numeroSugestao++;
        if (numeroSugestao >= sugestoesCompartimento.size()) numeroSugestao = 0;
        sugereCompartimento();
    }

    /**
     * método chamado pela classe DAVI para a utilização da tela de cadastro de 
     * peça
     */
    void pedidoTelaCadastrarPeca() {
        telaCadastrarPeca = new TelaCadastrarPeca(this, Arrays.asList(TipoDePeca.values()), Filtro.getFiltros(), Arrays.asList(Peca.Tecido.values()), Arrays.asList(Cor.values()));
        Davi.janela.insereTela(telaCadastrarPeca, "Cadastrar Peça");
    }

    /**
     * método chamado  pelo uso do botão de confimar características da peça
     * da tela cadastrar peça
     * @param codigoPeca o código da peça que se deseja cadastrar
     * @param filtros os filtros que caracterizam a peça
     * @param tipoPeca  o tipo da peça selecionado 
     * @param tecido o tecido da peça selecionado
     * @param cor a cor selecionada da peça
     */
    public void pedidoConfirmarDados(String codigoPeca, ArrayList<String> filtros, Object tipoPeca, Object tecido, Object cor) {
        if (Peca.existe(codigoPeca)) {
            Peca peca = new Peca(codigoPeca);
            boolean ehDono;
            String s = "O código, " + codigoPeca + ", da peca que você deseja " + "inserir já está cadastrado e suas caracteríticas são " + peca.toString() + ". E seus donos são: " + peca.getDonos() + ".";
            if (peca.ehDono(davi.getMorador())) telaCadastrarPeca.informaCodigoExistente(s); else {
                s += " Voce deseja se adicionar como dono dessa peça?";
                if (telaCadastrarPeca.pergutarNovoDono(s)) peca.addDono(davi.getMorador());
                telaCadastrarPeca.informa("Dono adicionado à peça");
            }
        } else {
            pecaAux = new PecaAux(codigoPeca, filtros, (TipoDePeca) tipoPeca, (Peca.Tecido) tecido, (Cor) cor);
            sugestoesCompartimento = Compartimento.getSugestoes(pecaAux.tipoDePeca, davi.getMorador());
            numeroSugestao = 0;
            sugereCompartimento();
        }
    }

    void pedidoTelaExcluirPeca() {
        telaExcluirPeca = new TelaExcluirPeca(this);
        Davi.janela.insereTela(telaExcluirPeca, "Excluir Cadastro de Peça");
    }

    private void sugereCompartimento() {
        String s = "Sugiro que essa peça seja guaradada na pilha " + sugestoesCompartimento.get(numeroSugestao).getIdLista();
        if (Compartimento.TipoDeCompartimento.CABIDE.equals(sugestoesCompartimento.get(numeroSugestao).getTipoCompartimento()) || Compartimento.TipoDeCompartimento.MALEIRO.equals(sugestoesCompartimento.get(numeroSugestao).getTipoCompartimento())) s += " do "; else s += " da ";
        s += sugestoesCompartimento.get(numeroSugestao).getIdCompartimento() + ".";
        if (sugestoesCompartimento.get(numeroSugestao).estahCheio()) s += " Porém essa pilha já está em seu limite de capacidade.";
        telaCadastrarPeca.informaSugestao(s);
    }

    private class PecaAux {

        String codigoPeca;

        ArrayList<String> filtros;

        TipoDePeca tipoDePeca;

        Peca.Tecido tecido;

        Cor cor;

        public PecaAux(String codigoPeca, ArrayList<String> filtros, TipoDePeca tipoDePeca, Tecido tecido, Cor cor) {
            this.codigoPeca = codigoPeca;
            this.filtros = filtros;
            this.tipoDePeca = tipoDePeca;
            this.tecido = tecido;
            this.cor = cor;
        }
    }
}
