package org.grill.fatwhacker.data;

import java.io.Serializable;
import java.util.ArrayList;
import org.joda.time.LocalDate;

public class FoodWeek implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private LocalDate startDay;

    private ArrayList<DailyRecord> days = new ArrayList<DailyRecord>();

    private int dailyPoints = 28;

    private int flexPoints = 35;

    public FoodWeek(LocalDate date) {
        startDay = date;
        initDays();
    }

    private void initDays() {
        for (int i = 0; i < 7; i++) {
            DailyRecord rec = new DailyRecord();
            LocalDate recDate = startDay.plusDays(i);
            rec.setDate(recDate);
            rec.setWeek(this);
            days.add(rec);
        }
    }

    public float getFlexpointsUsed() {
        float total = 0;
        for (DailyRecord day : days) {
            float cumTotal = day.getCumTotal();
            if (cumTotal > dailyPoints) total += (cumTotal - dailyPoints);
        }
        return total;
    }

    public int getExercisePointsEarned() {
        float total = 0;
        for (DailyRecord day : days) {
            total += day.getExercisePoints();
        }
        return (int) total;
    }

    public int getDailyPoints() {
        return dailyPoints;
    }

    public void setDailyPoints(int dailyPoints) {
        this.dailyPoints = dailyPoints;
    }

    public ArrayList<DailyRecord> getDays() {
        return days;
    }

    public void setDays(ArrayList<DailyRecord> days) {
        this.days = days;
    }

    public int getFlexPoints() {
        return flexPoints;
    }

    public void setFlexPoints(int flexPoints) {
        this.flexPoints = flexPoints;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public String toString() {
        LocalDate endDay = startDay.plusDays(6);
        return startDay.monthOfYear().getAsShortText() + " " + startDay.dayOfMonth().getAsShortText() + " - " + endDay.monthOfYear().getAsShortText() + " " + endDay.dayOfMonth().getAsShortText();
    }

    public DailyRecord getDay(LocalDate date) {
        for (DailyRecord day : days) {
            if (day.getDate().equals(date)) return day;
        }
        throw new IllegalArgumentException("Invalid Date");
    }

    public DailyRecord getFirst() {
        return days.get(0);
    }
}
