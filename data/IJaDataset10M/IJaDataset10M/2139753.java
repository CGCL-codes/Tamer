package es.tecnocom.swtclient.ui.secciones;

import java.util.List;

public class Menu {

    private List<SeccionAplicacion> secciones;

    /**
	 * @return the secciones
	 */
    public List<SeccionAplicacion> getSecciones() {
        return secciones;
    }

    /**
	 * @param secciones the secciones to set
	 */
    public void setSecciones(List<SeccionAplicacion> secciones) {
        this.secciones = secciones;
    }

    /**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("Menu(").append("secciones=").append(this.secciones).append(" )");
        return retValue.toString();
    }
}
