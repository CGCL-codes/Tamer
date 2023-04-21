package com.acv.connector.plex.command.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlexUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static Date parse(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }
}
