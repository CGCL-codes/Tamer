package beans2;

/**
 *
 * @author Administrador
 */
public class Reserva {

    private int idReserva;

    private Recurso recurso;

    private TipoRecurso tipoRecurso;

    private Usuario usuario;

    private int idaprobador;

    private int idsolicitudreserva;

    private String fechainicio;

    private String horainicio;

    private String fechafinal;

    private String horafinal;

    private String estado;

    private String observacion;

    private String ubicacion;

    private String personaRetiro;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(String fechafinal) {
        this.fechafinal = fechafinal;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(String horafinal) {
        this.horafinal = horafinal;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public int getIdaprobador() {
        return idaprobador;
    }

    public void setIdaprobador(int idaprobador) {
        this.idaprobador = idaprobador;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdsolicitudreserva() {
        return idsolicitudreserva;
    }

    public void setIdsolicitudreserva(int idsolicitudreserva) {
        this.idsolicitudreserva = idsolicitudreserva;
    }

    public TipoRecurso getTiporecurso() {
        return tipoRecurso;
    }

    public void setTiporecurso(TipoRecurso tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observación) {
        this.observacion = observación;
    }

    public String getPersonaRetiro() {
        return personaRetiro;
    }

    public void setPersonaRetiro(String personaRetiro) {
        this.personaRetiro = personaRetiro;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicación) {
        this.ubicacion = ubicación;
    }
}
