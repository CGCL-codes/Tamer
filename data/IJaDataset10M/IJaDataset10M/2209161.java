package mx.ipn.to;

public class GrupoUsuarioTO extends TransferObject {

    private short idGrupoUsuario;

    private String nombre;

    private String descripcion;

    public void setIdGrupoUsuario(short idGrupoUsuario) {
        this.idGrupoUsuario = idGrupoUsuario;
    }

    public short getIdGrupoUsuario() {
        return idGrupoUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public GrupoUsuarioTO(short idGrupoUsuario, String nombre, String descripcion) {
        super();
        this.idGrupoUsuario = idGrupoUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public GrupoUsuarioTO() {
        super();
        this.idGrupoUsuario = (short) -1;
        this.nombre = null;
        this.descripcion = null;
    }
}
