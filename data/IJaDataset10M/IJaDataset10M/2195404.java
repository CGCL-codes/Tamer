package fondefitco.Modelo;

/**
 *
 * @author Juan De La Puente
 */
public class Solicitud_retiro_asociados extends Persona_Asocidado {

    private String fecha_solicitud;

    private String motivo_retiro;

    private String comentarios;

    private double aportes;

    private String deudas;

    private String diferencias;

    /**
     * @return the fecha_solicitud
     */
    public String getFecha_solicitud() {
        return fecha_solicitud;
    }

    /**
     * @param fecha_solicitud the fecha_solicitud to set
     */
    public void setFecha_solicitud(String fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    /**
     * @return the motivo_retiro
     */
    public String getMotivo_retiro() {
        return motivo_retiro;
    }

    /**
     * @param motivo_retiro the motivo_retiro to set
     */
    public void setMotivo_retiro(String motivo_retiro) {
        this.motivo_retiro = motivo_retiro;
    }

    /**
     * @return the comentarios
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * @return the aportes
     */
    public double getAportes() {
        return aportes;
    }

    /**
     * @param aportes the aportes to set
     */
    public void setAportes(double aportes) {
        this.aportes = aportes;
    }

    /**
     * @return the deudas
     */
    public String getDeudas() {
        return deudas;
    }

    /**
     * @param deudas the deudas to set
     */
    public void setDeudas(String deudas) {
        this.deudas = deudas;
    }

    /**
     * @return the diferencias
     */
    public String getDiferencias() {
        return diferencias;
    }

    /**
     * @param diferencias the diferencias to set
     */
    public void setDiferencias(String diferencias) {
        this.diferencias = diferencias;
    }
}
