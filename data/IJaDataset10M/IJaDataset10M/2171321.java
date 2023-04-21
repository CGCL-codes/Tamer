package sirf.actions.yacimiento;

import sirf.actions.InterfazCrudAction;
import sirf.dao.yacimiento.TipoYacimientoDAO;
import sirf.dominio.yacimiento.TipoYacimiento;
import com.opensymphony.xwork2.ActionSupport;

public class TipoYacimientoAction extends ActionSupport implements InterfazCrudAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2720053478143536871L;

    private TipoYacimiento tipoyacimiento;

    private String accion_cambiable = "update";

    private Integer id;

    @Override
    public String create() {
        TipoYacimientoDAO dao = new TipoYacimientoDAO();
        dao.insert(this.tipoyacimiento);
        return SUCCESS;
    }

    @Override
    public String update() throws Exception {
        TipoYacimientoDAO dao = new TipoYacimientoDAO();
        dao.update(this.tipoyacimiento);
        return SUCCESS;
    }

    @Override
    public String delete() {
        if (id != null) {
            TipoYacimientoDAO dao = new TipoYacimientoDAO();
            dao.delete(id);
        }
        return SUCCESS;
    }

    public String input() throws Exception {
        if (this.id != null) {
            TipoYacimientoDAO dao = new TipoYacimientoDAO();
            this.tipoyacimiento = dao.select(this.id);
        } else {
            this.accion_cambiable = "create";
        }
        return INPUT;
    }

    public void setTipoyacimiento(TipoYacimiento tipo) {
        this.tipoyacimiento = tipo;
    }

    public TipoYacimiento getTipoyacimiento() {
        return tipoyacimiento;
    }

    public void setAccion_cambiable(String accion_cambiable) {
        this.accion_cambiable = accion_cambiable;
    }

    public String getAccion_cambiable() {
        return accion_cambiable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
