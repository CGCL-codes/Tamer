package validation;

import java.text.SimpleDateFormat;
import java.util.Date;
import timer.ExecutionTimer;
import timer.TimerRecordFile;
import dbmanager.DBManager;

public class Gt implements Validation {

    String ConditionValue;

    public boolean validate(String Value, String ConditionValue) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        try {
            double val = Double.parseDouble(Value);
            double convalue = Double.parseDouble(ConditionValue);
            if (val > convalue) {
                return true;
            } else return false;
        } catch (NumberFormatException e) {
            try {
                if (Value.length() == ConditionValue.length()) {
                    String pattern = "yyyy-MM-dd HH:MM:ss";
                    SimpleDateFormat sdm = new SimpleDateFormat(pattern);
                    Date date_value;
                    try {
                        date_value = (Date) sdm.parse(Value);
                    } catch (Exception e1) {
                        Value = Value + " 00:00:00";
                        date_value = (Date) sdm.parse(Value);
                    }
                    Date date_condition;
                    try {
                        date_condition = (Date) sdm.parse(ConditionValue);
                    } catch (Exception e1) {
                        ConditionValue = ConditionValue + " 00:00:00";
                        date_condition = (Date) sdm.parse(ConditionValue);
                    }
                    if (date_value.compareTo(date_condition) > 0) return true; else return false;
                } else {
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat sdm = new SimpleDateFormat(pattern);
                    Date date_value = (Date) sdm.parse(Value);
                    Date date_condition = (Date) sdm.parse(ConditionValue);
                    if (date_value.compareTo(date_condition) > 0) return true; else return false;
                }
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Gt", "validate", t.duration());
        return false;
    }

    public boolean validate(String Value) {
        return false;
    }

    public String getMessage() {
        return " value must be greater than " + ConditionValue;
    }

    public static void main(String args[]) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        Gt gt = new Gt();
        System.out.println("Validate : " + gt.validate("2006-06-10 00:00:00", "2006-06-10 00:00:00"));
        System.out.println(gt.getMessage());
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Gt", "main", t.duration());
    }

    public void setDbmanager(DBManager database) {
    }

    public boolean validate(String Value, String ConditionValue, String Refvalue) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.ConditionValue = ConditionValue;
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("validation", "Gt", "validate", t.duration());
        return validate(Value, ConditionValue);
    }

    public void setBasicvalue(String value) {
    }
}
