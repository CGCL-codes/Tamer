package mx.ipn.to;

public class InstitucionFinancieraTO extends TransferObject {

    private int idInstitucionFinanciera;

    private String nombre;

    private String telefono;

    public void setIdInstitucionFinanciera(int idInstitucionFinanciera) {
        this.idInstitucionFinanciera = idInstitucionFinanciera;
    }

    public int getIdInstitucionFinanciera() {
        return idInstitucionFinanciera;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public InstitucionFinancieraTO(int idInstitucionFinanciera, String nombre, String telefono) {
        super();
        this.idInstitucionFinanciera = idInstitucionFinanciera;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public InstitucionFinancieraTO() {
        super();
        this.idInstitucionFinanciera = -1;
        this.nombre = null;
        this.telefono = null;
    }
}
