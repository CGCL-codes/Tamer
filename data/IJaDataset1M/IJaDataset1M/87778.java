package carga;

public class MateriaSiiau {

    private String clave;

    private String centro;

    private String calendario;

    public MateriaSiiau(String clave, String centro, String calendario) {
        this.clave = clave;
        this.centro = centro;
        this.calendario = calendario;
    }

    public Object clone() {
        return new MateriaSiiau(clave, centro, calendario);
    }

    public String getClave() {
        return clave;
    }

    public String getCentro() {
        return centro;
    }

    public String getCal() {
        return calendario;
    }
}
