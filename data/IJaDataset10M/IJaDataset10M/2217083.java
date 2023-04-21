package dados;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Esta classe guarda os dados de uma determinada quest�o, incluindo a lista de
 * entradas e gabaritos para esta quest�o e a nota m�xima que um aluno pode obter
 * nela.
 */
public class Questao implements Serializable {

    private ArrayList<Teste> testes = null;

    private double notaMax = 0;

    private String nomeArquivoSaida = null;

    /** 
     * Cria uma nova inst�ncia da classe Questao.
     */
    public Questao() {
        testes = new ArrayList<Teste>();
    }

    /**
     * Retorna o �ndice da lista de entradas e gabaritos para esta quest�o.
     */
    public Teste getTeste(int indice) {
        return testes.get(indice);
    }

    public ArrayList<Teste> getTestes() {
        return testes;
    }

    public void adicionarTeste() {
        testes.add(new Teste());
    }

    public void editarTeste(int indice, Teste teste) {
        testes.set(indice, teste);
    }

    public void removerTeste(int indice) {
        testes.remove(indice);
    }

    public void limparListaTestes() {
        testes = new ArrayList<Teste>();
    }

    /**
     * Retorna a nota m�xima que esta quest�o pode ter.
     */
    public double getNotaMax() {
        return notaMax;
    }

    /**
     * Seta a nota m�xima que esta quest�o pode ter.
     */
    public void setNotaMax(double notaQuestao) {
        this.notaMax = notaQuestao;
        PastaCorrecao.getInstancia().setModificado(true);
    }

    public String getNomeArquivoSaida() {
        return nomeArquivoSaida;
    }

    public void setNomeArquivoSaida(String nomeArquivoSaida) {
        this.nomeArquivoSaida = nomeArquivoSaida;
    }
}
