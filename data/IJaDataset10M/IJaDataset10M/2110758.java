package ar.com.AmberSoft.iEvenTask.services;

import ar.com.AmberSoft.iEvenTask.backend.entities.Tarea;

public class GetTaskService extends GetEntityService {

    @Override
    public String getEntity() {
        return Tarea.class.getName();
    }
}
