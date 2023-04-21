package net.blogbotplatform.blogbot;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static BufferedReader in;

    private static String line = null;

    public static int day;

    public static int month;

    public static int year;

    public static int hour;

    public static int minute;

    public static int second;

    public static long moment;

    public static String readConfigFile(String fileName) {
        System.out.println("start reading config file");
        String configString = "";
        try {
            in = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            System.out.println("problem opening file" + e);
        }
        try {
            for (; ; ) {
                line = in.readLine();
                if (line == null) break;
                System.out.println("reading");
                System.out.println(line);
                configString += line + ",";
            }
        } catch (IOException e) {
            ;
        } finally {
            try {
                in.close();
            } catch (IOException e2) {
                ;
            }
            System.out.println("finished reading config file");
        }
        return configString;
    }

    public static void writeTextFile(String content, String fileName, String dirName) {
        File dirObj = new File(dirName);
        if (dirObj.exists() == false) {
            System.err.println("Save Dir does not exist");
            return;
        }
        try {
            FileWriter out = new FileWriter(dirName + "/" + fileName, false);
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static int unsignedByteAsInt(int signedInt) {
        int unsignedInt;
        if (signedInt < 0) {
            unsignedInt = 256 + signedInt;
        } else {
            unsignedInt = signedInt;
        }
        return (unsignedInt);
    }

    public static void setTimeStamp() {
        Calendar rightNow = Calendar.getInstance();
        day = rightNow.get(Calendar.DAY_OF_MONTH);
        month = (rightNow.get(Calendar.MONTH) + 1);
        year = rightNow.get(Calendar.YEAR);
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        second = rightNow.get(Calendar.SECOND);
        moment = System.currentTimeMillis();
    }

    public static String returnTimeStampAsText() {
        String timeStampText;
        timeStampText = "";
        if (day < 10) {
            timeStampText += "0" + day + "/";
        } else {
            timeStampText += day + "/";
        }
        if (month < 10) {
            timeStampText += "0" + month + "/";
        } else {
            timeStampText += month + "/";
        }
        timeStampText += year + " @ ";
        if (hour < 10) {
            timeStampText += "0" + hour + ":";
        } else {
            timeStampText += hour + ":";
        }
        if (minute < 10) {
            timeStampText += "0" + minute + ".";
        } else {
            timeStampText += minute + ".";
        }
        if (second < 10) {
            timeStampText += "0" + second;
        } else {
            timeStampText += second;
        }
        return (timeStampText);
    }
}
