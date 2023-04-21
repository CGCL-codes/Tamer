package validation;

import timer.ExecutionTimer;
import timer.TimerRecordFile;
import dbmanager.DBManager;

public class Isnumericafterfirstchar implements Validation {

    public String getMessage() {
        return "after first char all char must be numeric";
    }

    public void setDbmanager(DBManager database) {
    }

    public boolean validate(String Value) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        String tempnewstr = Value.substring(1, Value.length());
        System.out.println(tempnewstr);
        Isnumeric isnumeric = new Isnumeric();
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Isnumericafterfirstchar", "validate", t.duration());
        return isnumeric.validate(tempnewstr);
    }

    public boolean validate(String Value, String ConditionValue) {
        return false;
    }

    public boolean validate(String Value, String ConditionValue, String Refvalue) {
        if (ConditionValue.equalsIgnoreCase("true")) {
            return validate(Value);
        } else if (ConditionValue.equalsIgnoreCase("false")) return true; else return false;
    }

    public static void main(String[] args) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        Isnumericafterfirstchar is = new Isnumericafterfirstchar();
        System.out.println(is.validate("+9h1123456"));
        System.out.println(is.validate("+91123456", "false", ""));
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Isnumericafterfirstchar", "main", t.duration());
    }

    public void setBasicvalue(String value) {
    }
}
