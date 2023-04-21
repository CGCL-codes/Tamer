package org.eclipse.pde.internal.runtime.logview;

import java.text.*;
import java.util.*;

public class LogSession {

    private String sessionData;

    private Date date;

    /**
	 * Constructor for LogSession.
	 */
    public LogSession() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SS");
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
        }
    }

    public void setDateUSA(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
        }
    }

    public String getSessionData() {
        return sessionData;
    }

    void setSessionData(String data) {
        this.sessionData = data;
    }

    public void processLogLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);
        if (tokenizer.countTokens() == 6) {
            tokenizer.nextToken();
            StringBuffer dateBuffer = new StringBuffer();
            for (int i = 0; i < 4; i++) {
                dateBuffer.append(tokenizer.nextToken());
                dateBuffer.append(" ");
            }
            setDate(dateBuffer.toString().trim());
        }
        if (tokenizer.countTokens() != 4) {
            return;
        }
        if (!"!SESSION".equals(tokenizer.nextToken())) {
            return;
        }
        StringBuffer dateBuffer = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            dateBuffer.append(tokenizer.nextToken());
            dateBuffer.append(" ");
        }
        setDateUSA(dateBuffer.toString().trim());
    }
}
