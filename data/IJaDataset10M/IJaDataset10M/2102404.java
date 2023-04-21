package edu.cibertec.bean;

import java.io.Serializable;

public class BeanFormula implements Serializable {

    private int codFormula;

    private String descFormula;

    private String formula;

    public int getCodFormula() {
        return codFormula;
    }

    public void setCodFormula(int codFormula) {
        this.codFormula = codFormula;
    }

    public String getDescFormula() {
        return descFormula;
    }

    public void setDescFormula(String descFormula) {
        this.descFormula = descFormula;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
