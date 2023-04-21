package persistencia;

import java.util.LinkedList;

/**
 *
 * @author Fabio
 */
public class DadosMatriz {

    private int qColunas = 1;

    private int qLinhas = 0;

    private String nomeMatriz;

    private LinkedList<String> tituloLinha;

    private LinkedList<String> tituloColuna;

    private LinkedList<LinkedList> linha;

    public DadosMatriz() {
        tituloLinha = new LinkedList<String>();
        tituloColuna = new LinkedList<String>();
        linha = new LinkedList<LinkedList>();
        this.nomeMatriz = "Requisitos X Use Case";
        tituloColuna.add("");
    }

    public DadosMatriz(String nome) {
        tituloLinha = new LinkedList<String>();
        tituloColuna = new LinkedList<String>();
        linha = new LinkedList<LinkedList>();
        this.nomeMatriz = nome;
        tituloColuna.add("");
    }

    public int getQColunas() {
        return qColunas;
    }

    /**
     * Número positivo aumenta, negativo diminui.
     * @param colunas Quantidade para aumentar ou diminuir.
     */
    public void setQColunas(int colunas) {
        this.qColunas += colunas;
    }

    public int getQLinhas() {
        return qLinhas;
    }

    /**
     * Número positivo aumenta, negativo diminui.
     * @param linhas Quantidade para aumentar ou diminuir.
     */
    public void setQLinhas(int linhas) {
        this.qLinhas += linhas;
    }

    public String getNomeMatriz() {
        return nomeMatriz;
    }

    public void setNomeMatriz(String nome) {
        this.nomeMatriz = nome;
    }

    public String getTituloColuna(int index) {
        return tituloColuna.get(index);
    }

    public LinkedList<String> getTituloColuna() {
        return tituloColuna;
    }

    public void setTituloColuna(int index, String titulo) {
        tituloColuna.set(index, titulo);
    }

    public LinkedList<String> getTituloLinha() {
        return tituloLinha;
    }

    public void setTituloLinha(LinkedList<String> tituloLinha) {
        this.tituloLinha = tituloLinha;
    }

    public LinkedList<LinkedList> getLinhas() {
        return linha;
    }

    public LinkedList<String> getLinha(int linha) {
        return this.linha.get(linha);
    }

    public void setLinha(LinkedList<LinkedList> linha) {
        this.linha = linha;
    }
}
