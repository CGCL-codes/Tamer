package db.dbio;

import java.util.Vector;
import db.core.DBIO;

public class PriorityDBIOImpl extends DBIO {

    private final String addPriorityString = "INSERT INTO  priority " + " ( priorityName , priorityValue , coef ) VALUES (?,?,?) ";

    private final String getAllPriorityString = "SELECT priorityName ," + "priorityValue , coef " + "FROM priority ";

    private final String getPriorityIdByNameString = "SELECT priority_id FROM priority WHERE  priorityName = ?";

    private final String getPriorityByIdString = "SELECT priorityName , " + "priorityValue , coef " + "FROM priority WHERE priority_id = ? ";

    public boolean addPriority(String priorityName, int priorityValue, int coef) {
        Vector<Object> data = new Vector<Object>();
        data.add(priorityName);
        data.add(priorityValue);
        data.add(coef);
        return updateDB(addPriorityString, data);
    }

    public Vector<Vector<Object>> getAllPriorities() {
        return select(getAllPriorityString);
    }

    public Vector<Vector<Object>> getPriorityById(int priority_id) {
        Vector<Object> data = new Vector<Object>();
        data.add(priority_id);
        return select(getPriorityByIdString, data);
    }

    public Vector<Vector<Object>> getPriorityIdByName(String priorityName) {
        Vector<Object> data = new Vector<Object>();
        data.add(priorityName);
        return select(getPriorityIdByNameString, data);
    }
}
