package net.sf.campusip.web.webwork.action.clase;

import com.opensymphony.xwork.Preparable;
import net.sf.campusip.domain.infraestructura.Clase;

/**
 * @author vns
 * 
 */
public class ViewClase extends ClaseAction implements Preparable {

    protected int id;

    /**
     * 
     */
    public ViewClase() {
        super();
    }

    public String execute() throws Exception {
        if (clase == null) {
            addActionError("Clase no existente");
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * @param int i
     */
    public void setId(int id) {
        this.id = id;
    }

    public void prepare() throws Exception {
        if (clase.getId() == 0) this.clase = (Clase) jcolegio.getById(Clase.class, id);
    }
}
