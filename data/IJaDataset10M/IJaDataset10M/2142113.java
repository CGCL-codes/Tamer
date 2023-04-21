package com.michaelbelyakov1967.projects.VICZONE;

import java.util.*;
import com.michaelbelyakov1967.gps.*;

public class VicZoneGPSData extends GPSData {

    VicZoneGPSData(String s) {
        super(s);
    }

    public void parseData(String s) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.substring(0, 2)) + 4);
        calendar.set(Calendar.MINUTE, Integer.parseInt(s.substring(2, 4)));
        calendar.set(Calendar.SECOND, Integer.parseInt(s.substring(4, 6)));
        latGrades = Integer.parseInt(s.substring(7, 9));
        latMinutes = Double.parseDouble(s.substring(9, 16));
        lonGrades = Integer.parseInt(s.substring(17, 20));
        lonMinutes = Double.parseDouble(s.substring(20, 27));
        valid = s.charAt(6) == 'A';
        north = s.charAt(16) == 'N';
        east = s.charAt(27) == 'E';
        s = s.substring(28);
        speed = Double.parseDouble(s.substring(0, 5));
        calendar.set(Calendar.YEAR, 2000 + Integer.parseInt(s.substring(5, 7)));
        calendar.set(Calendar.MONTH, Integer.parseInt(s.substring(7, 9)) - 1);
        calendar.set(Calendar.DATE, Integer.parseInt(s.substring(9, 11)));
        date = calendar.getTime();
        direction = Double.parseDouble(s.substring(11, 17));
        int n = s.length() - 7;
        milProv = s.charAt(n) == 'L';
        mileage = Integer.parseInt(s.substring(n + 1));
    }
}
