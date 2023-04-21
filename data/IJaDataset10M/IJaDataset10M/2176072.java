package modelo.entidades;

/**
 *
 * @author Manuel
 */
public class RegistroModificacionControlImpl implements RegistroModificacionControl {

    private String idRegistro;

    private String idControl;

    private String motivo;

    public RegistroModificacionControlImpl(String idRegistro, String idControl, String motivo) {
        this.idRegistro = idRegistro;
        this.idControl = idControl;
        this.motivo = motivo;
    }

    public RegistroModificacionControlImpl(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    /**
     * @return the idRegistro
     */
    public String getIdRegistro() {
        return idRegistro;
    }

    /**
     * @param idRegistro the idRegistro to set
     */
    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    /**
     * @return the idControl
     */
    public String getIdControl() {
        return idControl;
    }

    /**
     * @param idControl the idControl to set
     */
    public void setIdControl(String idControl) {
        this.idControl = idControl;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
