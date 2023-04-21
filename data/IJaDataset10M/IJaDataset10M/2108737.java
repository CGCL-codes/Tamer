package Logica;

public class TipoMoneda {

    private int idsunattipodemoneda;

    private String codigo;

    private String decripcion;

    public TipoMoneda() {
    }

    public TipoMoneda(String decripcion) {
        this.decripcion = decripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String val) {
        this.codigo = val;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public void setDecripcion(String val) {
        this.decripcion = val;
    }

    public int getIdsunattipodemoneda() {
        return idsunattipodemoneda;
    }

    public void setIdsunattipodemoneda(int val) {
        this.idsunattipodemoneda = val;
    }
}
