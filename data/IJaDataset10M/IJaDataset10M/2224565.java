package com.dmoving.log;

public class ItemTimeStamp {

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private int second;

    private int millisec;

    public ItemTimeStamp(String year, String month, String day, String hour, String minute, String second, String milliSecond) {
        this.year = Integer.valueOf(year).intValue();
        this.month = Integer.valueOf(month).intValue();
        this.day = Integer.valueOf(day).intValue();
        this.hour = Integer.valueOf(hour).intValue();
        this.minute = Integer.valueOf(minute).intValue();
        this.second = Integer.valueOf(second).intValue();
        this.millisec = Integer.valueOf(milliSecond).intValue();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String toString() {
        return this.year + "/" + this.month + "/" + this.day + " " + this.hour + ":" + this.minute + ":" + this.second + ":" + this.millisec;
    }

    public int getMilisec() {
        return millisec;
    }

    public void setMilisec(int milisec) {
        this.millisec = milisec;
    }

    /**
		 * @param its
		 * @return if ItemTimeStamp after the time represented by targetITS,return true,else ,return false
		 */
    public boolean compareTo(ItemTimeStamp targetITS) {
        if (this.getYear() > targetITS.getYear()) {
            return true;
        } else if (this.getYear() == targetITS.getYear()) {
            if (this.getMonth() > targetITS.getMonth()) {
                return true;
            } else if (this.getMonth() == targetITS.getMonth()) {
                if (this.getDay() > targetITS.getDay()) {
                    return true;
                } else if (this.getDay() == targetITS.getDay()) {
                    if (this.getHour() > targetITS.getHour()) {
                        return true;
                    } else if (this.getHour() == targetITS.getHour()) {
                        if (this.getMinute() > targetITS.getMinute()) {
                            return true;
                        } else if (this.getMinute() == targetITS.getMinute()) {
                            if (this.getSecond() > targetITS.getSecond()) {
                                return true;
                            } else if (this.getSecond() == targetITS.getSecond()) {
                                if (this.getMilisec() >= targetITS.getMilisec()) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
