package es.caib.sistra.modelInterfaz;

import java.io.Serializable;

/**
 * Informaci�n necesaria para el proceso de login
 * de inicio de un tr�mite
 */
public class InformacionLoginTramite implements Serializable {

    /**
	 * Descripcion tr�mite
	 */
    private String descripcionTramite;

    /**
	 * Niveles autenticaci�n. Combinaci�n de las letras A,U,C (An�nimo, Usuario , Certificado)
	 */
    private String nivelesAutenticacion;

    /**
	 * Indica si se permite inicio por defecto
	 */
    private boolean inicioAnonimoDefecto = false;

    /**
	 * Descripcion tr�mite
	 * @return Descripcion tr�mite
	 */
    public String getDescripcionTramite() {
        return descripcionTramite;
    }

    /**
	 * Descripcion tr�mite
	 * @param descripcionTramite Descripcion tr�mite
	 */
    public void setDescripcionTramite(String descripcionTramite) {
        this.descripcionTramite = descripcionTramite;
    }

    /**
	 * Indica si se permite inicio por defecto
	 * @return true/false
	 */
    public boolean isInicioAnonimoDefecto() {
        return inicioAnonimoDefecto;
    }

    /**
	 * Indica si se permite inicio por defecto
	 * 
	 * @param inicioAnonimoInmediato true/false
	 */
    public void setInicioAnonimoDefecto(boolean inicioAnonimoInmediato) {
        this.inicioAnonimoDefecto = inicioAnonimoInmediato;
    }

    /**
	 * Niveles autenticaci�n. Combinaci�n de las letras A,U,C (An�nimo, Usuario , Certificado)
	 * @return Niveles autenticaci�n
	 */
    public String getNivelesAutenticacion() {
        return nivelesAutenticacion;
    }

    /**
	 * Niveles autenticaci�n. Combinaci�n de las letras A,U,C (An�nimo, Usuario , Certificado)
	 * @param nivelesAutenticacion Niveles autenticaci�n
	 */
    public void setNivelesAutenticacion(String nivelesAutenticacion) {
        this.nivelesAutenticacion = nivelesAutenticacion;
    }
}
