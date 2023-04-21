package org.butu.utils.date;

/**
 * ����� ��� �������� �������� �����������������
 * @author kbakaras
 *
 */
public class TimeValue {

    private int hours;

    private int minutes;

    private boolean sign = false;

    public TimeValue(int length) {
        if (length < 0) {
            sign = true;
            length *= -1;
        }
        hours = length / 60;
        minutes = length - hours * 60;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getStrValue() {
        if (hours != 0 || minutes != 0) {
            String result = String.format("%d", minutes);
            if (minutes < 10) result = "0" + result;
            result = String.format("%d", hours) + ":" + result;
            return sign ? "-" + result : result;
        } else {
            return null;
        }
    }
}
