package net.sf.jup.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.SocketTimeoutException;

public class ReadLine extends Thread {

    BufferedReader input;

    private static final String newLine = "\n";

    Thread parent;

    private int responseCode;

    private StringBuilder responseBody;

    private Boolean done;

    private Boolean timeoutCountingEnabled = false;

    private Boolean timeoutCountingPending = false;

    private static int nReadLine = 0;

    public static final int RESPONSE_UNKNOWN_HOST = -100;

    public static final int RESPONSE_CANNOT_OPEN = -101;

    public static final int RESPONSE_TIMEOUT = -102;

    public static final int RESPONSE_UNKNOWN_ERROR = -103;

    private static final int MAX_RESPONSE_LENGTH = 1024;

    public ReadLine(BufferedReader i, Thread p) {
        super("ReadLine " + String.valueOf(nReadLine++));
        parent = p;
        input = i;
        done = false;
        responseCode = RESPONSE_TIMEOUT;
        responseBody = new StringBuilder(MAX_RESPONSE_LENGTH / 2);
    }

    public void run() {
        Boolean isStatusLine;
        Boolean skipHeaders;
        Boolean continueReading;
        String statusLine;
        try {
            continueReading = true;
            while (continueReading) {
                String line = "";
                isStatusLine = true;
                skipHeaders = true;
                responseBody = new StringBuilder(MAX_RESPONSE_LENGTH / 2);
                statusLine = "";
                Boolean timeout = false;
                try {
                    while ((line = input.readLine()) != null) {
                        if (isStatusLine) {
                            statusLine = line;
                            isStatusLine = false;
                        }
                        if (skipHeaders) {
                            if (line.equals("")) skipHeaders = false;
                        } else {
                            if (responseBody.length() < MAX_RESPONSE_LENGTH) {
                                responseBody.append(line);
                                responseBody.append(newLine);
                            }
                        }
                    }
                } catch (SocketTimeoutException e) {
                    if (timeoutCountingEnabled) {
                        responseCode = RESPONSE_TIMEOUT;
                        return;
                    } else timeout = true;
                }
                if (!timeout) {
                    responseCode = parseStatusLine(statusLine);
                    if (responseCode != 100) continueReading = false;
                }
                timeoutCountingEnabled = timeoutCountingPending;
            }
        } catch (IOException ioe) {
            responseCode = RESPONSE_UNKNOWN_ERROR;
        }
        synchronized (parent) {
            done = true;
            try {
                parent.notify();
            } catch (IllegalMonitorStateException ime) {
            }
        }
    }

    private int parseStatusLine(String reply) {
        if (reply != null) {
            Pattern rx = Pattern.compile("HTTP/(.*) ([0-9]*) (.*)");
            Matcher matcher = rx.matcher(reply);
            if (matcher.find()) {
                try {
                    return Integer.valueOf(matcher.group(2));
                } catch (Exception e) {
                    return RESPONSE_UNKNOWN_ERROR;
                }
            } else return RESPONSE_UNKNOWN_ERROR;
        } else return RESPONSE_TIMEOUT;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody.toString();
    }

    public synchronized Boolean isDone() {
        return done;
    }

    public void startTimeoutCounting() {
        timeoutCountingPending = true;
    }
}
