package ar.com.AmberSoft.iEvenTask.services;

import ar.com.AmberSoft.iEvenTask.backend.entities.Tarea;

public class DeleteTaskService extends DeleteService {

    @SuppressWarnings("rawtypes")
    @Override
    public Class getEntity() {
        return Tarea.class;
    }
}
