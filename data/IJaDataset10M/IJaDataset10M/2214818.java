package blueprint4j.db.utils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import blueprint4j.db.DBConnection;
import blueprint4j.db.DBTools;
import blueprint4j.db.Entity;
import blueprint4j.db.Field;
import blueprint4j.db.FieldAutoId;
import blueprint4j.db.FieldDateTime;
import blueprint4j.db.FieldString;
import blueprint4j.db.FieldStringSelect;
import blueprint4j.db.TableName;
import blueprint4j.utils.Log;
import blueprint4j.utils.ThreadScheduable;
import blueprint4j.utils.ThreadSchedule;

public class Schedular extends Entity implements ThreadScheduable {

    static String VERSION = "$Id: Schedular.java,v 1.4 2004/09/09 11:07:54 jaspervdb Exp $";

    public static String SECOND = "Second", MINUTE = "Minute", HOURLY = "Hourly", DAILY = "Daily", WEEKLY = "Weekly", MONTHLY = "Monthly", YEARLY = "Yearly";

    public static final String TABLE_NAME = "d_schedular";

    public TableName table_name = new TableName(TABLE_NAME, this);

    public FieldAutoId id = new FieldAutoId("id", 0, "The Record Identifier", this);

    public FieldString class_name = new FieldString("class_name", Field.FIELD_NAME, "The scheduable class", 250, this);

    public FieldString description = new FieldString("description", Field.FIELD_NAME, "the description", 250, this);

    public FieldStringSelect frequency = new FieldStringSelect("frequency", 0, "Execute frequency", 10, new String[] { HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY }, this);

    public FieldDateTime last_execute = new FieldDateTime("last_execute", 0, "The last execurte time for this schedualr", this);

    private static SimpleDateFormat datetime_format = new SimpleDateFormat("yyyyMMddHHmmss"), minute_format = new SimpleDateFormat("mm"), hour_format = new SimpleDateFormat("HH"), day_format = new SimpleDateFormat("DDD"), week_format = new SimpleDateFormat("ww"), month_format = new SimpleDateFormat("MM"), year_format = new SimpleDateFormat("yyyy"), time_format = new SimpleDateFormat("HHmmss");

    private static Calendar calendar = Calendar.getInstance();

    static {
        ThreadSchedule.add(new Schedular());
    }

    public Schedular() {
    }

    public Schedular(DBConnection connection) throws SQLException {
        super(connection);
    }

    public Entity getNewInstance() {
        return new Schedular();
    }

    public boolean shouldExecute() throws SQLException {
        if (last_execute.get() == null) {
            return true;
        }
        if (MINUTE.equals(frequency.get())) {
            return !minute_format.format(last_execute.get()).equals(minute_format.format(new Date()));
        }
        if (HOURLY.equals(frequency.get())) {
            return !hour_format.format(last_execute.get()).equals(hour_format.format(new Date()));
        }
        if (DAILY.equals(frequency.get())) {
            return !day_format.format(last_execute.get()).equals(day_format.format(new Date()));
        }
        if (WEEKLY.equals(frequency.get())) {
            return !week_format.format(last_execute.get()).equals(week_format.format(new Date()));
        }
        if (MONTHLY.equals(frequency.get())) {
            return !month_format.format(last_execute.get()).equals(month_format.format(new Date()));
        }
        if (YEARLY.equals(frequency.get())) {
            return !year_format.format(last_execute.get()).equals(year_format.format(new Date()));
        }
        return false;
    }

    public boolean keepAlive() {
        return true;
    }

    public int sleepTime() {
        return 1000;
    }

    public void process() throws Throwable {
        DBConnection dbcon = DBTools.getNDC();
        try {
            for (Schedular schedular = (Schedular) new Schedular(dbcon).find(dbcon.getDataBaseUtils().castTime(new Date()) + " > " + dbcon.getDataBaseUtils().castTimeForField("last_execute") + " or " + "last_execute is null"); schedular != null; schedular = (Schedular) schedular.getNextEntity()) {
                if (schedular.shouldExecute()) {
                    try {
                        schedular.last_execute.set(new Date());
                        schedular.save();
                        new SchedularLog(dbcon, schedular, ((Scheduable) Class.forName(schedular.class_name.get()).getConstructor(new Class[] {}).newInstance(new Object[] {})).runSchedule(dbcon)).save();
                    } catch (Throwable th) {
                        new SchedularLog(dbcon, schedular, th).save();
                        Log.debug.out("SCHEDULAR EXECUTING [" + schedular.class_name.get() + "]", th);
                    }
                }
                dbcon.commit();
            }
        } finally {
            dbcon.close();
        }
    }

    public void close() throws Throwable {
    }
}
