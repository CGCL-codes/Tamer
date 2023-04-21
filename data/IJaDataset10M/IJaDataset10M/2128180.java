package proyecto.modelo;

public class Vigilante extends Persona {

    private int idBloque, tipoPersona;

    private String turno;

    private Usuario usuario;

    private Bloque bloque;

    public Vigilante() {
        super();
    }

    public Vigilante(int idPersona, String nombre, String apePateno, String apeMaterno, String nroDocumento, String telefono, String correo, String turno, int tipoPersona, int idBloque) {
        super(idPersona, nombre, apePateno, apeMaterno, nroDocumento, telefono, correo);
        this.turno = turno;
        this.idBloque = idBloque;
        this.tipoPersona = tipoPersona;
    }

    public int getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(int idBloque) {
        this.idBloque = idBloque;
    }

    public int getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(int tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }
}
