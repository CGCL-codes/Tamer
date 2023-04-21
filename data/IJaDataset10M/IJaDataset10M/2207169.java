package br.usp.ime.protoc.genotipagem;

public class TuboSoro extends Tubo {

    private String aliquotas;

    public String getAliquotas() {
        return this.aliquotas;
    }

    public void setAliquotas(String value) {
        if (this.aliquotas != value) {
            this.aliquotas = value;
        }
    }

    /**
    * <pre>
    *           1..*         has          1 
    * TuboSoro ----------------------------- AmostraSangue
    *           tuboSoro      amostraSangue 
    * </pre>
    */
    private AmostraSangue amostraSangue;

    public AmostraSangue getAmostraSangue() {
        return this.amostraSangue;
    }

    public boolean setAmostraSangue(AmostraSangue value) {
        boolean changed = false;
        if (this.amostraSangue != value) {
            AmostraSangue oldValue = this.amostraSangue;
            if (this.amostraSangue != null) {
                this.amostraSangue = null;
                oldValue.removeFromTuboSoro(this);
            }
            this.amostraSangue = value;
            if (value != null) {
                value.addToTuboSoro(this);
            }
            changed = true;
        }
        return changed;
    }

    public void removeYou() {
        AmostraSangue tmpAmostraSangue = getAmostraSangue();
        if (tmpAmostraSangue != null) {
            setAmostraSangue(null);
        }
        super.removeYou();
    }
}
