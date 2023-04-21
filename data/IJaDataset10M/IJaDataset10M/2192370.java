package com.fastfly.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@MappedSuperclass
public class BaseEntity implements Serializable {

    public static final String CODE_VERSION = "1.0.3";

    @Transient
    protected final Log log = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 1L;

    private static SimpleDateFormat timeFormat;

    @Transient
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    @Transient
    protected static DecimalFormat decimalFormat = new DecimalFormat("0000");

    @Transient
    protected static Random random = new Random((new Date()).getTime());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    /**
	 * Indicates if this object was already stored in the DB or it's new
	 * 
	 * @return
	 */
    @Transient
    public boolean getNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - id: " + id + " - version: " + version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        int varId = (id == null) ? 0 : id.intValue();
        result = prime * result + (int) (varId ^ (varId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final BaseEntity other = (BaseEntity) obj;
        if (id != other.id) return false;
        return true;
    }

    public static synchronized SimpleDateFormat getTimeFormat() {
        if (timeFormat == null) {
            timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);
        }
        return timeFormat;
    }

    protected Date setupDateTime(Date date, String time) throws ParseException {
        if (date != null && time != null) {
            Calendar calTime = Calendar.getInstance();
            calTime.setTime(getTimeFormat().parse(time));
            date = getDateWithSpecificTime(date, calTime.get(Calendar.HOUR_OF_DAY), calTime.get(Calendar.MINUTE));
        }
        return date;
    }

    protected Date getDateWithSpecificTime(Date date, int hour, int minute) {
        return getDateWithSpecificTime(date, 0, hour, minute);
    }

    protected Date getDateWithSpecificTime(Date date, int daysToAdd, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    protected int getRandomInt(int limit) {
        return Math.abs(random.nextInt()) % (limit + 1);
    }

    protected String getRandomStr() {
        int num = getRandomInt(9999);
        return decimalFormat.format(num);
    }
}
