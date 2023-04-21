package controller;

import java.util.ArrayList;
import javax.swing.JPanel;
import model.Filtro;
import model.Morador;
import view.morador.TelaCadastroMorador;
import view.filtro.TelaDefinirFiltroPadrao;
import view.morador.TelaExcluirMorador;

/**
 *
 * @author leandro.paz
 */
public class ControleMorador {

    private boolean acao = false;

    Davi davi;

    TelaExcluirMorador telaExcluirMorador;

    TelaDefinirFiltroPadrao telaDefinirFiltroPadrao;

    public ControleMorador(Davi davi) {
        this.davi = davi;
    }

    public void pedidoExcluirCadastro(String idMorador, String senha) {
        if (idMorador.toLowerCase().equals(davi.getMorador().getID().toLowerCase()) && senha.equals(davi.getMorador().getSenha())) {
            if (telaExcluirMorador.confirmacao()) {
                ArrayList<String> restricoes = davi.getMorador().excluirCadastro();
                if (restricoes.size() == 0) {
                    Davi.janela.removerTela(telaExcluirMorador);
                    ControleLogin controleLogin = new ControleLogin(davi);
                    controleLogin.inicio();
                } else {
                    String aviso = restricoes.get(0);
                    if (restricoes.size() >= 2) {
                        for (int i = 1; i < restricoes.size() - 1; i++) aviso += ", " + restricoes.get(i);
                        aviso += " e " + restricoes.get(restricoes.size() - 1);
                    }
                    telaExcluirMorador.informa("Existem " + aviso + " cadastrados, " + "a exclusão foi abortada");
                }
            } else telaExcluirMorador.informa("Exclusão de cadastro abortada.");
        } else {
            telaExcluirMorador.informa("Seu ID e/ou Senha não conferem, por favor " + "digite-os novamente");
        }
    }

    public void pedidoTelaCadastroMorador() {
        TelaCadastroMorador tela = new TelaCadastroMorador(this);
        Davi.janela.insereTela(tela, "Tela Cadastro Morador");
    }

    public void voltarConfiguracoesMorador(JPanel telaAtual) {
        davi.pedidoConfigMorador(telaAtual);
    }

    public String receberDados(String nome, String senha, String userName) {
        if (Morador.checaId(userName)) {
            Morador.cadastrarMorador(nome, userName, senha);
            return "Cadastro realizado com sucesso!";
        } else return "ID já existente!";
    }

    /**
     * Método chamado pela TelaDefinirFiltroPadrao para definir o filtro padrao
     * do usuário
     * @param itemSelecionado
     */
    public void definirFiltroPadrao(Object itemSelecionado) {
        davi.getMorador().setFiltroPadrao(new Filtro((String) itemSelecionado), davi.getMorador());
    }

    /**
     * Pedido da TelaConfiguracoesMorador para criar uma tela 
     * de exclusao de morador
     */
    public void pedidoTelaExclusaoMorador() {
        telaExcluirMorador = new TelaExcluirMorador(this);
        Davi.janela.insereTela(telaExcluirMorador, "Tela Exclusão Morador");
    }

    /**
     * Pedido feito pela TelaConfigMorador para criar uma tela definir
     * filtro padrão
     */
    void pedidoDefinirFiltroPadrao() {
        telaDefinirFiltroPadrao = new TelaDefinirFiltroPadrao(this, Filtro.getFiltros(), davi.getMorador().getFiltroPadrao().toString());
        Davi.janela.insereTela(telaDefinirFiltroPadrao, "Tela Definir Filtro Padrão");
    }
}
