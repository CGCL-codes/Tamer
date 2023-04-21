package br.quentinha.webapp.action;

import java.util.List;
import org.appfuse.service.GenericManager;
import org.appfuse.webapp.action.BaseAction;
import br.quentinha.model.TipoQuentinha;

public class TipoQuentinhaAction extends BaseAction {

    private static final long serialVersionUID = 3808836185699819045L;

    private GenericManager<TipoQuentinha, Long> tipoQuentinhaManager;

    private List<TipoQuentinha> tipoQuentinhas;

    public void setPessoaManager(GenericManager<TipoQuentinha, Long> tipoQuentinhaManager) {
        this.tipoQuentinhaManager = tipoQuentinhaManager;
    }

    public List getTipoQuentinhas() {
        return tipoQuentinhas;
    }

    public String list() {
        tipoQuentinhas = tipoQuentinhaManager.getAll();
        return SUCCESS;
    }
}
