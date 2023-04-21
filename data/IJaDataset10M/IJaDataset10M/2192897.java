package com.chessclub.simulbot.datagrams;

import com.chessclub.simulbot.Settings;

public class Tell extends Datagram {

    public Tell(String message) {
        super(message);
    }

    public Tell(Datagram d) {
        this(d, true);
    }

    public Tell(Datagram d, boolean lowerCase) {
        super(d);
        if (args.get(1).equals(Settings.username.toLowerCase())) {
            args.set(1, " ");
        }
    }

    public String getHandle() {
        return args.get(0).toLowerCase();
    }

    public String getTitles() {
        return args.get(1);
    }

    public String getMessage(boolean lowerCase) {
        String temp = args.get(2).substring(0, args.get(2).length() - 1);
        if (lowerCase) {
            return temp.toLowerCase();
        } else {
            return temp;
        }
    }

    public void truncateFirstSpace() {
        String temp = getMessage(false);
        temp = truncateFirstSpaceHelper(temp);
        temp = temp + "~";
        args.set(2, temp);
    }

    public String truncateFirstSpaceHelper(String message) {
        if (message.indexOf(" ") != -1) {
            return message.substring(message.indexOf(" ") + 1);
        } else {
            return "";
        }
    }
}
