package es.caib.sistra.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Configuraci�n gestor de formularios para invocar al gestor desde sistra.
 * 
 * Contiene la configuraci�n din�mica del formulario y las propiedades del gestor.
 * 
 */
public class ConfiguracionGestorFlujoFormulario implements Serializable {

    /**
	 * Gestor utilizado
	 */
    private GestorFormulario gestorFormulario;

    /**
	 * Propiedades espec�ficas del gestor de formularios
	 */
    private HashMap propiedades = new HashMap();

    /**
	 * Configuraci�n din�mica del formulario
	 */
    private ConfiguracionFormulario configuracionFormulario;

    /**
	 * Datos actuales formulario
	 */
    private String datosActualesFormulario;

    public void setPropiedad(String propiedad, String valor) {
        propiedades.put(propiedad, valor);
    }

    public String getPropiedad(String propiedad) {
        if (propiedades.containsKey(propiedad)) return (String) propiedades.get(propiedad);
        return null;
    }

    public ConfiguracionFormulario getConfiguracionFormulario() {
        return configuracionFormulario;
    }

    public void setConfiguracionFormulario(ConfiguracionFormulario configuracionFormulario) {
        this.configuracionFormulario = configuracionFormulario;
    }

    public String getDatosActualesFormulario() {
        return datosActualesFormulario;
    }

    public void setDatosActualesFormulario(String datosActualesFormulario) {
        this.datosActualesFormulario = datosActualesFormulario;
    }

    public GestorFormulario getGestorFormulario() {
        return gestorFormulario;
    }

    public void setGestorFormulario(GestorFormulario gestorFormulario) {
        this.gestorFormulario = gestorFormulario;
    }
}
