package es.caib.sistra.plugins.email;

import java.io.Serializable;

/**
 * Indica el estado de un env�o Email
 * 
 */
public class EstadoEnvio implements Serializable {

    /**
	 * Estado del env�o
	 * @see ConstantesEmail
	 */
    public String estado;

    /**
	 * Descripci�n del estado 
	 */
    public String descripcionEstado;

    /**
	 *  Descripci�n del estado 
	 * @return  descripci�n
	 */
    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    /**
	 *  Descripci�n del estado 
	 * @param descripcionEstado descripci�n
	 */
    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    /**
	 * Estado del env�o
	 * @return estado
	 * @see ConstantesSMS
	 */
    public String getEstado() {
        return estado;
    }

    /**
	 * Estado del env�o
	 * @return estado
	 * @see ConstantesSMS
	 */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
