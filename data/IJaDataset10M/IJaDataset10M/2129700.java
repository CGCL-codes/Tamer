package br.ufc.quixada.adrs.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ismaily
 */
public class RelatorioTecnologicoResult {

    private String questao;

    private List<Produtor> produtores;

    private List<List<Integer>> comparacaoRespostas;

    private List<Integer> quantidadesRespostas;

    private List<Integer> porcentagemRespostas;

    public RelatorioTecnologicoResult() {
        produtores = new ArrayList<Produtor>();
        comparacaoRespostas = new ArrayList<List<Integer>>();
        quantidadesRespostas = new ArrayList<Integer>();
    }

    public List<Integer> getPorcentagemRespostas() {
        return porcentagemRespostas;
    }

    public void setPorcentagemRespostas(List<Integer> porcentagemRespostas) {
        this.porcentagemRespostas = porcentagemRespostas;
    }

    public List<Integer> getQuantidadesRespostas() {
        return quantidadesRespostas;
    }

    public void setQuantidadesRespostas(List<Integer> quantidadesRespostas) {
        this.quantidadesRespostas = quantidadesRespostas;
    }

    public List<Produtor> getProdutores() {
        return produtores;
    }

    public void setProdutores(List<Produtor> produtores) {
        this.produtores = produtores;
    }

    public String getQuestao() {
        return questao;
    }

    public String getQuestaoCompleta() {
        return new QuestionarioQualitativo().getQuestaoByCodigo(getQuestao());
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public List<List<Integer>> getComparacaoRespostas() {
        return comparacaoRespostas;
    }

    public void setComparacaoRespostas(List<List<Integer>> ComparacaRespostas) {
        this.comparacaoRespostas = ComparacaRespostas;
    }

    public void addInformacaoQuantAoRelatorio(Produtor p, Integer valor) {
        int posicao = quantidadesRespostas.size();
        if (posicao == 0) {
            quantidadesRespostas.add(valor);
            produtores.add(p);
        } else {
            quantidadesRespostas.add(posicao, valor);
            produtores.add(posicao, p);
        }
    }

    public void addInformacaoPorcenAoRelatorio(Produtor p, Integer porcentagem) {
        int posicao = porcentagemRespostas.size();
        if (posicao == 0) {
            porcentagemRespostas.add(porcentagem);
            produtores.add(p);
        } else {
            porcentagemRespostas.add(posicao, porcentagem);
            produtores.add(posicao, p);
        }
    }

    public void addInformacaoCompAoRelatorio(Produtor p, List<Integer> comparacao) {
        int posicao = comparacao.size();
        if (posicao == 0) {
            comparacaoRespostas.add(comparacao);
            produtores.add(p);
        } else {
            comparacaoRespostas.add(posicao, comparacao);
            produtores.add(posicao, p);
        }
    }
}
