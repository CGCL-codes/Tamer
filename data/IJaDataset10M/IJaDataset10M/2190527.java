package br.rmpestano.finantial.model.report;

import br.rmpestano.finantial.model.Income;
import br.rmpestano.finantial.model.Outcome;
import java.util.List;

/**
 *
 * @author rmpestano
 */
public class ReceitaPorDespesaReport {

    private List<Income> receitas;

    private List<Outcome> despesas;

    private String mes;

    private Double somaReceitas;

    private Double somaDespesas;

    public ReceitaPorDespesaReport(List<Income> receitas, List<Outcome> despesas, String mes) {
        this.receitas = receitas;
        this.despesas = despesas;
        this.mes = mes;
        calculaSomas();
    }

    private void calculaSomas() {
        Double sum = 0.0;
        for (Income income : receitas) {
            if (income.getValue() != null) {
                sum += income.getValue();
            }
        }
        this.somaReceitas = sum;
        sum = 0.0;
        for (Outcome outcome : despesas) {
            if (outcome.getValue() != null) {
                sum += outcome.getValue();
            }
        }
        this.somaDespesas = sum;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getSomaDespesas() {
        return somaDespesas;
    }

    public void setSomaDespesas(Double somaDespesas) {
        this.somaDespesas = somaDespesas;
    }

    public Double getSomaReceitas() {
        return somaReceitas;
    }

    public void setSomaReceitas(Double somaReceitas) {
        this.somaReceitas = somaReceitas;
    }
}
