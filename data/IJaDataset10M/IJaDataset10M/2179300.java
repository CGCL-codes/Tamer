package org.ourgrid.worker.dao;

import org.ourgrid.common.interfaces.WorkerSpecListener;
import br.edu.ufcg.lsd.commune.Application;
import br.edu.ufcg.lsd.commune.container.servicemanager.dao.DAO;

public class WorkerSpecListenerDAO extends DAO {

    private WorkerSpecListener peer;

    public WorkerSpecListenerDAO(Application container) {
        super(container);
        this.peer = null;
    }

    public void setPeerWorkerSpecListener(WorkerSpecListener listener) {
        this.peer = listener;
    }

    public WorkerSpecListener getPeerWorkerSpecListener() {
        return this.peer;
    }
}
