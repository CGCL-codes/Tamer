package net.sf.campusip.web.webwork.action.escuelaprocedencia;

import net.sf.campusip.domain.personas.EscuelaProcedencia;
import net.sf.campusip.persistence.JcolegioAware;
import net.sf.campusip.persistence.JcolegioipI;
import com.opensymphony.xwork.ActionSupport;

/**
 * @author vns
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class EscuelaProcedenciaAction extends ActionSupport implements JcolegioAware {

    protected JcolegioipI jcolegio;

    protected EscuelaProcedencia escuela = new EscuelaProcedencia();

    /**
     * 
     */
    public EscuelaProcedenciaAction() {
        super();
    }

    public EscuelaProcedencia getciudad() {
        return escuela;
    }

    public void setJcolegio(JcolegioipI jcolegio) {
        this.jcolegio = jcolegio;
    }
}
