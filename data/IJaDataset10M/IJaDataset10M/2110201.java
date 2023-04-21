package co.com.uniandes.creditscore.mundo;

/**
 * Representa el Estado Civil<br>
 * <b>inv:</b><br>
 * codigo > 0 <br>
 */
public class EstadoCivil {

    /**
     * C�digo del Estado Civil
     */
    private int codigo;

    /**
     * Descripcion del Estado Civil
     */
    private String descripcion;

    /**
     * Crea un Estado Civil.
     * @param elCodigo
     * @param laDescripcion
     */
    public EstadoCivil(int elCodigo, String laDescripcion) {
        codigo = elCodigo;
        descripcion = laDescripcion;
        verificarInvariante();
    }

    /**
     * Devuelve el c�digo del Estado Civil
     * @return codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Asigna el c�digo del EstadoCivil
     * @param elCodigo.
     */
    void setCodigo(int elCodigo) {
        codigo = elCodigo;
    }

    /**
     * Devuelve la descripcion del Estado Civil
     * @return Descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna la descripcion del Estado Civil
     * @param laDescripcion.
     */
    void setDescripcion(String laDescripcion) {
        descripcion = laDescripcion;
    }

    /**
     * Verifica el invariante de la clase <br>
     * <b>Inv </b> codigo > 0 
     */
    private void verificarInvariante() {
        assert (codigo > 0) : "El c�digo debe ser mayor que cero";
    }
}
