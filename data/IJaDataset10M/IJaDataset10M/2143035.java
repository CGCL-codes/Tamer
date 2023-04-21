package br.faimg.pomar.controle.componentevisual;

import br.faimg.pomar.controle.BusinessDelegate.NumerodeSerieBusinessDelegate;
import br.faimg.pomar.modelo.pojo.NumeroSerie;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Juliana
 */
public class TableNumeroSerie {

    private String numeroSerie;

    private Integer ordemProducao;

    public void setNumeroserie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public void setOrdemProducao(Integer ordemProducao) {
        this.ordemProducao = ordemProducao;
    }

    public TableNumeroSerie() {
    }

    public DefaultTableModel getTableModel() {
        NumerodeSerieBusinessDelegate delegate = new NumerodeSerieBusinessDelegate();
        Vector<String> colunas = new Vector<String>();
        Vector<String> linhaTabela = null;
        Vector<Vector> linhas = new Vector<Vector>();
        List<NumeroSerie> numero = delegate.readByExample(numeroSerie, ordemProducao);
        colunas.add("Numero Serie");
        colunas.add("Ordem Producao");
        for (int i = 0; i < numero.size(); i++) {
            linhaTabela = new Vector<String>();
            linhaTabela.add(numero.get(i).getNumSerie());
            linhaTabela.add(numero.get(i).getNumOp().getNumOrp().toString());
            linhas.add(linhaTabela);
        }
        return new DefaultTableModel(linhas, colunas);
    }
}
