package validation;

import java.util.Hashtable;
import timer.ExecutionTimer;
import timer.TimerRecordFile;
import utility.Input;
import database.RecordSearch;
import dbmanager.DBManager;
import dbmanager.GetIdName;

public class Istype implements Validation {

    DBManager db;

    String actualvalue;

    String validationvalue;

    GetIdName gid;

    String value;

    public String getMessage() {
        return "Value must be of type " + validationvalue;
    }

    public void setDbmanager(DBManager db) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.db = db;
        gid = new GetIdName(db);
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Istype", "setDbmanager", t.duration());
    }

    public boolean validate(String Value) {
        return false;
    }

    public boolean validate(String actualvalue, String validationvalue) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        try {
            String id = gid.getId(actualvalue);
            String validationid = gid.getId(validationvalue);
            Hashtable<Object, Object> temp = new Hashtable<Object, Object>();
            temp.put("mid", id);
            temp.put("pv", validationid);
            temp.put("td", "null");
            RecordSearch ps = new RecordSearch(db);
            ps.setConditionMap(temp);
            String line[] = ps.getArrayFromResultSet();
            if (line.length == 0) return false; else return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean validate(String Value, String ConditionValue, String Refvalue) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.actualvalue = Value;
        this.validationvalue = ConditionValue;
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Istype", "validate", t.duration());
        return validate(Value, ConditionValue);
    }

    public void setBasicvalue(String value) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.value = value;
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Istype", "setBasicvalue", t.duration());
    }
}
