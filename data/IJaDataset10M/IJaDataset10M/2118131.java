package cn.myapps.core.workcalendar.calendar.ejb;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workcalendar.special.ejb.SpecialDayVO;
import cn.myapps.core.workcalendar.standard.ejb.BaseDay;
import cn.myapps.core.workcalendar.standard.ejb.StandardDayVO;
import cn.myapps.core.workcalendar.util.Month;

/**
 * 日历
 * 
 * @author Chris
 * 
 */
public class CalendarVO extends ValueObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id;

    private String type;

    private String name;

    private String remark;

    private Date lastModifyDate;

    private Collection standardDays;

    private Collection specialDays;

    private int workingTime;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
	 * 获取特例天
	 * 
	 * @return 特例天
	 */
    public Collection getSpecialDays() {
        if (this.specialDays == null) this.specialDays = new HashSet();
        return this.specialDays;
    }

    /**
	 * 获取常规天
	 * 
	 * @return 常规天
	 */
    public Collection getStandardDays() {
        if (this.standardDays == null) this.standardDays = new HashSet();
        return this.standardDays;
    }

    /**
	 * 根据当前日期获取常规天
	 * 
	 * @param calendar
	 * @return
	 */
    public BaseDay getStandardDayByDate(Calendar calendar) {
        if (this.standardDays == null) this.standardDays = new HashSet();
        Iterator its = this.standardDays.iterator();
        BaseDay standard = null;
        BaseDay[] stdrd = new StandardDayVO[7];
        int weekDay = 0;
        if (its != null) while (its.hasNext()) {
            standard = (BaseDay) its.next();
            stdrd[standard.getWeekDays()] = standard;
        }
        weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        standard = new StandardDayVO();
        standard.setApplicationid(stdrd[weekDay].getApplicationid());
        standard.setCalendar(stdrd[weekDay].getCalendar());
        standard.setDayIndex(stdrd[weekDay].getDayIndex());
        standard.setStartTime1(stdrd[weekDay].getStartTime1());
        standard.setStartTime2(stdrd[weekDay].getStartTime2());
        standard.setStartTime3(stdrd[weekDay].getStartTime3());
        standard.setStartTime4(stdrd[weekDay].getStartTime4());
        standard.setStartTime5(stdrd[weekDay].getStartTime5());
        standard.setEndTime1(stdrd[weekDay].getEndTime1());
        standard.setEndTime2(stdrd[weekDay].getEndTime2());
        standard.setEndTime3(stdrd[weekDay].getEndTime3());
        standard.setEndTime4(stdrd[weekDay].getEndTime4());
        standard.setEndTime5(stdrd[weekDay].getEndTime5());
        standard.setId(stdrd[weekDay].getId());
        standard.setWeekDays(stdrd[weekDay].getWeekDays());
        standard.setWorkingDayStatus(stdrd[weekDay].getWorkingDayStatus());
        standard.setRemark(stdrd[weekDay].getRemark());
        return standard;
    }

    /**
	 * 根据当前日期获取特例天
	 * 
	 * @param calendar
	 * @return 特例天
	 */
    public SpecialDayVO getSpecialDayByDate(Calendar calendar) {
        if (this.specialDays == null) this.specialDays = new HashSet();
        Iterator its = this.specialDays.iterator();
        SpecialDayVO special = null;
        if (its != null) while (its.hasNext()) {
            special = (SpecialDayVO) its.next();
            if (special != null && special.getStartDate().compareTo(calendar.getTime()) <= 0 && special.getEndDate().compareTo(calendar.getTime()) >= 0) {
                SpecialDayVO standard = new SpecialDayVO();
                standard.setApplicationid(special.getApplicationid());
                standard.setCalendar(special.getCalendar());
                standard.setDayIndex(special.getDayIndex());
                standard.setStartTime1(special.getStartTime1());
                standard.setStartTime2(special.getStartTime2());
                standard.setStartTime3(special.getStartTime3());
                standard.setStartTime4(special.getStartTime4());
                standard.setStartTime5(special.getStartTime5());
                standard.setEndTime1(special.getEndTime1());
                standard.setEndTime2(special.getEndTime2());
                standard.setEndTime3(special.getEndTime3());
                standard.setEndTime4(special.getEndTime4());
                standard.setEndTime5(special.getEndTime5());
                standard.setId(special.getId());
                standard.setWeekDays(special.getWeekDays());
                standard.setWorkingDayStatus(special.getWorkingDayStatus());
                standard.setRemark(special.getRemark());
                standard.setStartDate(special.getStartDate());
                standard.setEndDate(special.getEndDate());
                return standard;
            }
        }
        return null;
    }

    /**
	 * 获取日历的类型
	 * 
	 * @return 日历的类型
	 */
    public String getType() {
        return this.type;
    }

    /**
	 * 设置标识
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * 设置名称
	 * 
	 * @param name名称
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * 设置特殊日历
	 * 
	 * @param specialDays
	 *            特殊日历
	 */
    public void setSpecialDays(Collection specialDays) {
        this.specialDays = specialDays;
    }

    /**
	 * 设置正常日历
	 * 
	 * @param standardDays
	 *            正常日历
	 */
    public void setStandardDays(Collection standardDays) {
        this.standardDays = standardDays;
    }

    /**
	 * 设置类型
	 * 
	 * @param type
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * 获取描述
	 * 
	 * @return 描述
	 */
    public String getRemark() {
        return remark;
    }

    /**
	 * 设置描述
	 * 
	 * @param remark
	 *            描述
	 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
	 * 获取最后一次修改时间
	 * 
	 * @return 修改时间
	 */
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    /**
	 * 设置最后一次修改时间
	 * 
	 * @param lastModifyDate
	 *            最后一次修改时间
	 */
    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    /**
	 * 获得某年某月的所有天
	 * 
	 * @param year
	 *            年
	 * @param monthIndex
	 *            月份
	 * @param calendar
	 *            日历类别
	 * @return year年monthIndex月的所有天
	 */
    public Month getMonth(int year, int monthIndex) {
        Month mm = new Month();
        BaseDay[][] days;
        days = new BaseDay[6][7];
        for (int i = 0; i < 6; i++) for (int j = 0; j < 7; j++) days[i][j] = null;
        Calendar thisMonth = getThisMonth(year, monthIndex);
        if (thisMonth == null) return null;
        int firstIndex = thisMonth.get(Calendar.DAY_OF_WEEK) - 1;
        int maxIndex = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day;
        BaseDay[][] std = new BaseDay[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                day = (i * 7 + j) - firstIndex + 1;
                if (day > 0 && day <= maxIndex) {
                    Calendar month = thisMonth;
                    month.set(Calendar.DAY_OF_MONTH, day);
                    std[i][j] = getSpecialDayByDate(month);
                    if (std[i][j] != null) {
                        std[i][j].setDayIndex(day);
                    } else {
                        std[i][j] = getStandardDayByDate(thisMonth);
                        std[i][j].setDayIndex(day);
                    }
                } else {
                    Calendar month = getThisMonth(year, monthIndex);
                    month.set(Calendar.DAY_OF_MONTH, day);
                    std[i][j] = getStandardDayByDate(month);
                    if (std[i][j] != null) {
                        std[i][j].setDayIndex(0);
                    }
                }
                days[i][j] = std[i][j];
            }
        }
        mm.setYear(year);
        mm.setMonthIndex(monthIndex);
        mm.setDays(days);
        return mm;
    }

    public static Calendar getThisMonth(int year, int monthIndex) {
        Calendar thisMonth = Calendar.getInstance();
        if (year > 1900) thisMonth.set(Calendar.YEAR, year); else thisMonth.set(Calendar.YEAR, 1901);
        if (monthIndex >= 0 && monthIndex < 12) thisMonth.set(Calendar.MONTH, monthIndex); else thisMonth.set(Calendar.MONTH, 0);
        thisMonth.setFirstDayOfWeek(Calendar.SUNDAY);
        thisMonth.set(Calendar.DAY_OF_MONTH, 1);
        return thisMonth;
    }

    /**
	 * 获取工作时间
	 * 
	 * @return 工作时间
	 */
    public int getWorkingTime() {
        return workingTime;
    }

    /**
	 * 设置工作时间
	 * 
	 * @param workingTime
	 *            工作时间
	 */
    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }
}
