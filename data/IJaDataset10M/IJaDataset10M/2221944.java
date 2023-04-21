package be.kuleuven.peno3.mobiletoledo.model;

import java.util.Calendar;
import java.util.Date;

public class Message {

    private int id;

    private String title;

    private String author;

    private String content;

    private String date;

    private Calendar cal = null;

    public Calendar getCal() {
        if (cal == null) {
            cal = Calendar.getInstance();
            cal.setTime(new Date(date));
        }
        return cal;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }
}
