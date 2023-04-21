package org.idontknow.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

class SystemTrace implements Trace {

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");

    public void debug(String message) {
        System.out.println(sdf.format(new Date()) + " [Debug]: " + message);
    }

    public void info(String message) {
        System.out.println(sdf.format(new Date()) + " [Info] : " + message);
    }

    public void error(String message) {
        System.out.println(sdf.format(new Date()) + " [Error]: " + message);
    }

    public void info(int number) {
        System.out.println(sdf.format(new Date()) + " [Info] : " + number);
    }
}
