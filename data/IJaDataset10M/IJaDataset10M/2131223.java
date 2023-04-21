package org.wd.test;

import java.sql.Time;

public class TestDateTime {

    public static void main(String[] args) {
        Time time = Time.valueOf("14:30:00");
        System.out.println(time.after(Time.valueOf("14:29:00")));
    }
}
