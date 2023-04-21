package ar.com.AmberSoft.iEvenTask.services;

import java.util.Map;
import org.hibernate.Query;
import ar.com.AmberSoft.iEvenTask.backend.entities.Entity;
import ar.com.AmberSoft.iEvenTask.backend.entities.Objetivo;
import ar.com.AmberSoft.iEvenTask.backend.entities.VisibleObjetivo;
import ar.com.AmberSoft.util.ParamsConst;

public class UpdateObjectiveService extends CreateObjectiveService {

    @SuppressWarnings("rawtypes")
    @Override
    public Entity getEntity(Map params) {
        Objetivo objetivo = (Objetivo) super.getEntity(params);
        objetivo.setId((Integer) params.get(ParamsConst.ID));
        return objetivo;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map onExecute(Map params) throws Exception {
        deleteOldVisible((Integer) params.get(ParamsConst.ID));
        GetObjectiveService getObjectiveService = new GetObjectiveService();
        params.put(ParamsConst.TRANSACTION_CONTROL, Boolean.FALSE);
        params.putAll(getObjectiveService.execute(params));
        Objetivo objetivo = (Objetivo) getEntity(params);
        getSession().saveOrUpdate(objetivo);
        getSession().saveOrUpdate(getEntity(params));
        return null;
    }

    public void deleteOldVisible(Integer id) {
        StringBuffer queryText = new StringBuffer();
        queryText.append("DELETE ");
        queryText.append(VisibleObjetivo.class.getName());
        queryText.append(" WHERE  objetivo.id = ?");
        Query query = getSession().createQuery(queryText.toString());
        query.setInteger(0, id);
        query.executeUpdate();
    }
}
