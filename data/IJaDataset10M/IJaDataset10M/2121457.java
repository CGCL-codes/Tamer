package EntidadesCompartidas;

/**
 *
 * @author Andres
 */
public class Oferta_Empleo {

    private int Id;

    private String Descripcion;

    private Usuario Cliente;

    private Estado Estado;

    private String Titulo;

    public Usuario getCliente() {
        return Cliente;
    }

    public void setCliente(Usuario Cliente) {
        this.Cliente = Cliente;
    }

    public EntidadesCompartidas.Estado getEstado() {
        return Estado;
    }

    public void setEstado(EntidadesCompartidas.Estado Estado) {
        this.Estado = Estado;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }
}
