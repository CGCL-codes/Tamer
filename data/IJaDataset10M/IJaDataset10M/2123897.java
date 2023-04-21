package de.objectcode.time4u.server.entities;

import java.sql.Date;
import java.util.Calendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import de.objectcode.time4u.server.api.data.WeekTimePolicy;

@Entity
@DiscriminatorValue("w")
public class WeekTimePolicyEntity extends TimePolicyEntity {

    private int m_mondayTime;

    private int m_tuesdayTime;

    private int m_wednesdayTime;

    private int m_thursdayTime;

    private int m_fridayTime;

    private int m_saturdayTime;

    private int m_sundayTime;

    /**
   * Default constructor for hibernate.
   */
    protected WeekTimePolicyEntity() {
        super();
    }

    public WeekTimePolicyEntity(final String id, final long revision, final long lastModifiedByClient, final PersonEntity person) {
        super(id, revision, lastModifiedByClient, person);
    }

    public int getMondayTime() {
        return m_mondayTime;
    }

    public void setMondayTime(final int mondayTime) {
        m_mondayTime = mondayTime;
    }

    public int getTuesdayTime() {
        return m_tuesdayTime;
    }

    public void setTuesdayTime(final int tuesdayTime) {
        m_tuesdayTime = tuesdayTime;
    }

    public int getWednesdayTime() {
        return m_wednesdayTime;
    }

    public void setWednesdayTime(final int wednesdayTime) {
        m_wednesdayTime = wednesdayTime;
    }

    public int getThursdayTime() {
        return m_thursdayTime;
    }

    public void setThursdayTime(final int thursdayTime) {
        m_thursdayTime = thursdayTime;
    }

    public int getFridayTime() {
        return m_fridayTime;
    }

    public void setFridayTime(final int fridayTime) {
        m_fridayTime = fridayTime;
    }

    public int getSaturdayTime() {
        return m_saturdayTime;
    }

    public void setSaturdayTime(final int saturdayTime) {
        m_saturdayTime = saturdayTime;
    }

    public int getSundayTime() {
        return m_sundayTime;
    }

    public void setSundayTime(final int sundayTime) {
        m_sundayTime = sundayTime;
    }

    public void toDTO(final WeekTimePolicy timePolicy) {
        super.toDTO(timePolicy);
        timePolicy.setMondayTime(m_mondayTime);
        timePolicy.setTuesdayTime(m_tuesdayTime);
        timePolicy.setWednesdayTime(m_wednesdayTime);
        timePolicy.setThursdayTime(m_thursdayTime);
        timePolicy.setFridayTime(m_fridayTime);
        timePolicy.setSaturdayTime(m_saturdayTime);
        timePolicy.setSundayTime(m_sundayTime);
    }

    public void fromDTO(final WeekTimePolicy timePolicy) {
        super.fromDTO(timePolicy);
        m_mondayTime = timePolicy.getMondayTime();
        m_tuesdayTime = timePolicy.getTuesdayTime();
        m_wednesdayTime = timePolicy.getWednesdayTime();
        m_thursdayTime = timePolicy.getThursdayTime();
        m_fridayTime = timePolicy.getFridayTime();
        m_saturdayTime = timePolicy.getSaturdayTime();
        m_sundayTime = timePolicy.getSundayTime();
    }

    @Override
    public int getRegularTime(final Date date) {
        if (m_validFrom != null && m_validFrom.compareTo(date) > 0) {
            return -1;
        }
        if (m_validUntil != null && m_validUntil.compareTo(date) < 0) {
            return -1;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch(calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return m_mondayTime;
            case Calendar.TUESDAY:
                return m_tuesdayTime;
            case Calendar.WEDNESDAY:
                return m_wednesdayTime;
            case Calendar.THURSDAY:
                return m_thursdayTime;
            case Calendar.FRIDAY:
                return m_fridayTime;
            case Calendar.SATURDAY:
                return m_saturdayTime;
            case Calendar.SUNDAY:
                return m_sundayTime;
        }
        return -1;
    }
}
