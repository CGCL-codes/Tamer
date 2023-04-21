package ch.bbv.performancetests.stat;

import java.util.ArrayList;
import java.util.List;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.LightweightObject;
import ch.bbv.dog.persistence.PersistenceHandler;
import ch.bbv.dog.updates.DataObjectUpdate;

public class PerformanceTestsDOHWrapper implements PersistenceHandler {

    /**
     * The delegate handler performing all operations.
     */
    private DataObjectHandler handler;

    private List<UpdateSerializationCost> scList;

    public PerformanceTestsDOHWrapper(DataObjectHandler handler) {
        this.handler = handler;
        scList = new ArrayList<UpdateSerializationCost>();
    }

    public UpdateSerializationCost[] getUpdateSerializationCosts() {
        UpdateSerializationCost[] tmpCost = new UpdateSerializationCost[scList.size()];
        return scList.toArray(tmpCost);
    }

    public void clearUpdateSerializationCosts() {
        scList.clear();
    }

    public void closeDatasource(String datasource) {
        handler.closeDatasource(datasource);
    }

    public void openDatasource(String source, boolean readonly) {
        handler.openDatasource(source, readonly);
    }

    public void remove(String datasource, Class clazz, DataObject root) {
        handler.remove(datasource, clazz, root);
    }

    public void remove(String datasource, Class clazz, Integer id) {
        handler.remove(datasource, clazz, id);
    }

    public DataObject retrieve(String datasource, Class clazz, Integer id) {
        return handler.retrieve(datasource, clazz, id);
    }

    public List<DataObject> retrieve(String datasource, Class clazz, String constraints) {
        return handler.retrieve(datasource, clazz, constraints);
    }

    public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
        return handler.retrieveLw(datasource, clazz, id);
    }

    public List<LightweightObject> retrieveLw(String datasource, Class clazz, String constraints) {
        return handler.retrieveLw(datasource, clazz, constraints);
    }

    public List<DataObjectUpdate> store(String datasource, Class clazz, DataObject root) {
        DataObject originalRoot;
        if (!root.isNew()) {
            originalRoot = handler.retrieve(datasource, clazz, root.getId());
        } else {
            originalRoot = null;
        }
        UpdateSerializationCost sCost = new UpdateSerializationCost(root, originalRoot);
        double startTime = System.currentTimeMillis();
        List<DataObjectUpdate> updates = handler.store(datasource, clazz, root);
        sCost.setUpdateDuration((System.currentTimeMillis() - startTime) / 1000);
        scList.add(sCost);
        return updates;
    }
}
