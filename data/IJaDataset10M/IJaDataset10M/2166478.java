package org.synote.player.client;

public class TimeFormat {

    private static TimeFormat instance;

    private TimeFormat() {
    }

    public int parse(String string) {
        if (string == null || string.length() == 0) return 0;
        String seconds = null;
        int index = string.lastIndexOf(":");
        if (index != -1) {
            seconds = string.substring(index + 1);
            string = string.substring(0, index);
        } else {
            seconds = string;
            string = null;
        }
        String minutes = null;
        index = (string != null) ? string.lastIndexOf(":") : -1;
        if (index != -1) {
            minutes = string.substring(index + 1);
            string = string.substring(0, index);
        } else {
            minutes = string;
            string = null;
        }
        String hours = string;
        int time = 0;
        if (seconds != null && seconds.length() != 0) time += Integer.parseInt(seconds);
        if (minutes != null && minutes.length() != 0) time += Integer.parseInt(minutes) * 60;
        if (hours != null && hours.length() != 0) time += Integer.parseInt(hours) * 3600;
        return time * 1000;
    }

    private String format(int number) {
        return (number < 10) ? "0" + Integer.toString(number) : Integer.toString(number);
    }

    public String toString(int time) {
        time = time / 1000;
        int hours = time / 3600;
        time -= hours * 3600;
        int minutes = time / 60;
        time -= minutes * 60;
        int seconds = time;
        if (hours == 0 && minutes == 0) return "0:" + format(seconds); else if (hours == 0) return minutes + ":" + format(seconds); else return hours + ":" + format(minutes) + ":" + format(seconds);
    }

    public String toString(Integer time) {
        return (time != null) ? toString(time.intValue()) : null;
    }

    public String toString(String time) {
        return (time != null && time.length() > 0) ? toString(Integer.parseInt(time)) : null;
    }

    public static TimeFormat getInstance() {
        if (instance == null) instance = new TimeFormat();
        return instance;
    }
}
