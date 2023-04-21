package sirarq.actions;

import sirarq.bd.TipoMedioActualDAO;
import sirarq.dominio.TipoMedioActual;
import com.opensymphony.xwork2.ActionSupport;

public class TipoMedioActualAction extends ActionSupport implements CrudInterfaz {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4222846804812008053L;

    private TipoMedioActual tipomedioactual;

    private String accion_cambiable = "update";

    private Integer id;

    public TipoMedioActual getTipomedioactual() {
        return tipomedioactual;
    }

    @Override
    public String create() {
        if (tipomedioactual != null) {
            TipoMedioActualDAO tmaDao = new TipoMedioActualDAO();
            tmaDao.insert(this.getTipomedioactual());
            tmaDao.close_connection();
            return SUCCESS;
        }
        return ERROR;
    }

    @Override
    public String update() throws Exception {
        TipoMedioActualDAO tmaDao = new TipoMedioActualDAO();
        tmaDao.update(this.getTipomedioactual());
        tmaDao.close_connection();
        return SUCCESS;
    }

    @Override
    public String delete() {
        if (this.id != null) {
            TipoMedioActualDAO tmaDao = new TipoMedioActualDAO();
            tmaDao.delete(this.id);
            tmaDao.close_connection();
        }
        return null;
    }

    @Override
    public String input() throws Exception {
        if (id != null) {
            TipoMedioActualDAO tmaDao = new TipoMedioActualDAO();
            TipoMedioActual medio = tmaDao.select(id);
            this.setTipomedioactual(medio);
            tmaDao.close_connection();
        } else {
            this.setAccion_cambiable("create");
        }
        return INPUT;
    }

    /** Getters and Setters **/
    public void setTipomedioactual(TipoMedioActual tipomedioactual) {
        this.tipomedioactual = tipomedioactual;
    }

    public String getAccion_cambiable() {
        return accion_cambiable;
    }

    public void setAccion_cambiable(String accion_cambiable) {
        this.accion_cambiable = accion_cambiable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
