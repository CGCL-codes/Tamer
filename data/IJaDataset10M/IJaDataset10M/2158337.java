package ro.k.web.beans.order;

public class ReqPiesaBean {

    private String nume;

    private Double cant;

    private String stare;

    public ReqPiesaBean(String nume, Double cant, String stare) {
        super();
        this.nume = nume;
        this.cant = cant;
        this.stare = stare;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cant == null) ? 0 : cant.hashCode());
        result = prime * result + ((nume == null) ? 0 : nume.hashCode());
        result = prime * result + ((stare == null) ? 0 : stare.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ReqPiesaBean other = (ReqPiesaBean) obj;
        if (cant == null) {
            if (other.cant != null) return false;
        } else if (!cant.equals(other.cant)) return false;
        if (nume == null) {
            if (other.nume != null) return false;
        } else if (!nume.equals(other.nume)) return false;
        if (stare == null) {
            if (other.stare != null) return false;
        } else if (!stare.equals(other.stare)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReqPiesaBean [nume=" + nume + ", cant=" + cant + ", stare=" + stare + "]";
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public String getStare() {
        return stare;
    }

    public void setStare(String stare) {
        this.stare = stare;
    }
}
