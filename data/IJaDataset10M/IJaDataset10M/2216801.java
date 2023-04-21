package net.sf.campusip.web.webwork.action.aula;

import net.sf.campusip.domain.infraestructura.Aula;

/**
 * @author vns
 * 
 */
public class ViewAula extends AulaAction {

    protected int id;

    /**
     * 
     */
    public ViewAula() {
        super();
    }

    public String execute() throws Exception {
        this.aula = (Aula) jcolegio.getById(Aula.class, id);
        if (aula == null) {
            addActionError("Mateira no existente");
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
}
