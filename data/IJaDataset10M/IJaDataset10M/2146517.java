package br.ufmg.lcc.pcollecta.dto;

import java.util.Date;
import br.ufmg.lcc.arangi.dto.BasicDTO;

public class CampoDB extends BasicDTO {

    private static final long serialVersionUID = 1L;

    private Object valor;

    private Coluna coluna;

    private Date valorDate;

    private Double valorDouble;

    public Coluna getColuna() {
        return coluna;
    }

    public void setColuna(Coluna coluna) {
        this.coluna = coluna;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public Date getValorDate() {
        if (valor instanceof Date) {
            return (Date) valor;
        }
        return valorDate;
    }

    public void setValorDate(Date valorDate) {
        this.valorDate = valorDate;
    }

    public Double getValorDouble() {
        if (valor instanceof Number) {
            return new Double(((Number) valor).doubleValue());
        }
        return valorDouble;
    }

    public void setValorDouble(Double valorDouble) {
        this.valorDouble = valorDouble;
    }
}
