package org.icehockeymanager.ihm.game.finance;

import java.util.*;
import java.text.*;
import java.io.*;

/**
 * BookingEntry
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class BookingEntry implements Serializable {

    static final long serialVersionUID = -1917732780015341257L;

    /**
   * amount of booking
   */
    private double amount;

    /**
   * date of booing
   */
    private Calendar date;

    /**
   * booking text
   */
    private String text;

    /**
   * BookingEntry constructor
   * 
   * @param amount
   *          double
   * @param date
   *          Calender
   * @param text
   *          String
   */
    public BookingEntry(double amount, Calendar date, String text) {
        this.amount = amount;
        this.date = date;
        this.text = text;
    }

    /**
   * Returns date of booking
   * 
   * @return Calender
   */
    public Calendar getDate() {
        return date;
    }

    /**
   * Returns formated date (dd.mm.yyyy) of booking
   * 
   * @return String
   */
    public String getFormatedDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date.getTime());
    }

    /**
   * Returns booking text
   * 
   * @return String
   */
    public String getText() {
        return text;
    }

    /**
   * Returns amount of booking
   * 
   * @return double
   */
    public double getAmount() {
        return amount;
    }
}
