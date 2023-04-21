package net.sf.portions.controller.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Datos de configuraci�n de un forward
 * 
 * @version 1.0 01 Jan 2007
 * @author Ra�l Herranz
 */
public class Forward {

    /** Log de la clase */
    private Log log = LogFactory.getLog(this.getClass());

    /** Nombre del forward */
    private String name = "";

    /** Modo del forward */
    private String mode = "";

    /** Estado del forward */
    private String state = "";

    /** Path al que redirige el forward */
    private String path = "";

    /** Content (content-type de la respuesta) del forward */
    private String content = "";

    /**
	 * Retorna el valor de la propiedad content.
	 * 
	 * @return valor de content.
	 */
    public String getContent() {
        return content;
    }

    /**
	 * Establece el valor de la propiedad content.
	 *
	 * @param content El valor de content a establecer.
	 */
    public void setContent(String content) {
        if (content != null) {
            this.content = content;
        } else {
            this.content = "";
        }
    }

    /**
	 * Retorna el valor de la propiedad mode.
	 * 
	 * @return valor de mode.
	 */
    public String getMode() {
        return mode;
    }

    /**
	 * Establece el valor de la propiedad mode.
	 *
	 * @param mode El valor de mode a establecer.
	 */
    public void setMode(String mode) {
        if (mode != null) {
            this.mode = mode;
        } else {
            this.mode = "";
        }
    }

    /**
	 * Retorna el valor de la propiedad name.
	 * 
	 * @return valor de name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Establece el valor de la propiedad name.
	 *
	 * @param name El valor de name a establecer.
	 */
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }
    }

    /**
	 * Retorna el valor de la propiedad path.
	 * 
	 * @return valor de path.
	 */
    public String getPath() {
        return path;
    }

    /**
	 * Establece el valor de la propiedad path.
	 *
	 * @param path El valor de path a establecer.
	 */
    public void setPath(String path) {
        if (path != null) {
            this.path = path;
        } else {
            this.path = "";
        }
    }

    /**
	 * Retorna el valor de la propiedad state.
	 * 
	 * @return valor de state.
	 */
    public String getState() {
        return state;
    }

    /**
	 * Establece el valor de la propiedad state.
	 *
	 * @param state El valor de state a establecer.
	 */
    public void setState(String state) {
        if (state != null) {
            this.state = state;
        } else {
            this.state = "";
        }
    }

    /**
	 * Determina la igualdad o desigualdad de dos instancias de la clase
	 * 
	 * @param object Objeto a comparar
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return "true" si el objeto recibido es equivalente a la instancia actual
	 */
    public boolean equals(Object object) {
        if (log.isDebugEnabled()) {
            log.debug("Iniciando Forward::equals");
        }
        boolean retorno = true;
        if (object == null) {
            retorno = false;
        } else if (!(object instanceof Forward)) {
            retorno = false;
        } else {
            Forward forward = (Forward) object;
            if ((this.name == null && forward.getName() != null) || (this.name != null && !this.name.equals(forward.getName()))) {
                retorno = false;
            } else if ((this.mode == null && forward.getMode() != null) || (this.mode != null && !this.mode.equals(forward.getMode()))) {
                retorno = false;
            } else if ((this.state == null && forward.getState() != null) || (this.state != null && !this.state.equals(forward.getState()))) {
                retorno = false;
            } else if ((this.path == null && forward.getPath() != null) || (this.path != null && !this.path.equals(forward.getPath()))) {
                retorno = false;
            } else if ((this.content == null && forward.getContent() != null) || (this.content != null && !this.content.equals(forward.getContent()))) {
                retorno = false;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Finalizando Forward::equals (retorno: " + retorno + ")");
        }
        return retorno;
    }

    /**
	 * Retorna una representaci�n textual de la instancia actual de la clase
	 * 
	 * @see java.lang.Object#toString() 
	 * @return representaci�n textual de la instancia actual
	 */
    public String toString() {
        String retorno = "";
        retorno += "<forward>\n";
        retorno += "\t<name>" + name + "</name>\n";
        retorno += "\t<mode>" + mode + "</mode>\n";
        retorno += "\t<state>" + state + "</state>\n";
        retorno += "\t<path>" + path + "</path>\n";
        retorno += "\t<content>" + content + "</content>\n";
        retorno += "</forward>\n";
        return retorno;
    }
}
