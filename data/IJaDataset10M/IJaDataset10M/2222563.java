package org.elf.datalayer.dictionary;

import java.util.*;

/**
 * Definici�n del servicio de parametros de configuraci�n del usuario
 * 
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class DefConfig {

    private String _fqcn;

    private Map<String, Object> _parameters = new HashMap<String, Object>();

    /**
	 * Establece el nuevo valor del FQCN (Fully Qualified Class Name)
	 * que implementar� el interface KernelConfig
	 * @param fqcn Nuevo valor del FQCN.
	 */
    public void setFQCN(String fqcn) {
        _fqcn = fqcn;
    }

    /**
	 * Obtiene el valor del FQCN (Fully Qualified Class Name) el cual
	 * implementa el interface KernelConfig
	 * @return Valor del FQCN..
	 */
    public String getFQCN() {
        return _fqcn;
    }

    /**
	 * Establece el nuevo valor de los par�metro de configuraci�n
	 * del interface KernelConfig. 
         * La signaci�n de estos par�metros a la clase que implementa 
         * la interfaz se realizar� mediante su asignaci�n a propiedades
         * tipo "Bean" que tenga la clase y cuyo nombre de la propiedad 
         * coincidir� con el nombre del par�metro aqui definido.
         * 
	 * @param parameters Nuevo valor del Map de los mar�metros.
	 */
    public void setParameters(Map<String, Object> parameters) {
        _parameters = parameters;
    }

    /**
	 * Obtiene el valor de los par�metro de configuraci�n
	 * del interface KernelConfig. 
         * La signaci�n de estos par�metros a la clase que implementa 
         * la interfaz se realizar� mediante su asignaci�n a propiedades
         * tipo "Bean" que tenga la clase y cuyo nombre de la propiedad 
         * coincidir� con el nombre del par�metro aqui definido.
         * @return Map con los valores de configuraci�n del interface KernelConfig.
	 */
    public Map<String, Object> getParameters() {
        return _parameters;
    }
}
