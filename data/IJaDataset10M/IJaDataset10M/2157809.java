package velox.spring.admin.web;

import java.io.Serializable;
import velox.spring.admin.spi.repository.GetRepositoryAdapter;

public class ViewController extends AbstractViewController {

    GetRepositoryAdapter viewRepositoryAdapter;

    public ViewController() {
        setCommandClass(ViewCommand.class);
    }

    public void setViewRepositoryAdapter(GetRepositoryAdapter viewRepositoryAdapter) {
        this.viewRepositoryAdapter = viewRepositoryAdapter;
    }

    protected Object getEntity(Serializable key) {
        return viewRepositoryAdapter.get(type, key);
    }
}
