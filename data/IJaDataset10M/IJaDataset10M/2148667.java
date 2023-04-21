package de.fmf.loganalzyer.searchcase.apache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import de.fmf.loganalzyer.interfaces.ISearchComm;

public class ApacheSession_Db implements Runnable {

    private int totalRequestCount;

    private long endTime;

    private long startTime;

    private int responseTime_max;

    private int responseTime_avg;

    private String plainStartTime;

    private String plainEndTime;

    private String sessionId;

    private String browser;

    private HashMap<String, ALE_ResponseCode> responseCodes;

    private ArrayList<Integer> responseTimes;

    private SimpleDateFormat sdf_apache_time;

    private ArrayList<Integer> entriesInDb;

    private ArrayList<String> sess = new ArrayList<String>();

    public ApacheSession_Db(ISearchComm comm, String sessionId) {
        this.sessionId = sessionId;
        this.entriesInDb = new ArrayList<Integer>();
    }

    public void run() {
        this.sdf_apache_time = new SimpleDateFormat("HH:mm:ss");
        this.responseCodes = new HashMap<String, ALE_ResponseCode>();
        responseTimes = new ArrayList<Integer>();
        try {
            for (Iterator<String> iterator = sess.iterator(); iterator.hasNext(); ) {
                try {
                    String inputLine = iterator.next();
                    analyse(inputLine);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(sessionId);
            e.printStackTrace();
        }
        for (Iterator<Integer> iter = responseTimes.iterator(); iter.hasNext(); ) {
            Integer value = iter.next();
            responseTime_avg = responseTime_avg + value.intValue();
        }
        if (responseTime_avg > 0) responseTime_avg = responseTime_avg / responseTimes.size();
        sdf_apache_time = null;
        responseTimes = null;
        sess = null;
    }

    public void addLineToSession(String line) {
        this.sess.add(line);
    }

    private void analyse(String inputLine) throws Exception {
        try {
            ApacheRequestRecord arr = new ApacheRequestRecord(inputLine);
            setBrowserConfiguration(arr.getBrowser());
            addResponseCode(arr.getResponseCode());
            setSessionTimes(arr.getTimeCode());
            analyzeResponseTimes(arr);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR - COULD NOT PROCESS: " + inputLine);
        }
    }

    private void analyzeResponseTimes(ApacheRequestRecord arr) {
        try {
            int responseTime = (Integer.parseInt(arr.getResponseTime()));
            if (responseTime_max < responseTime) responseTime_max = responseTime;
            responseTimes.add(new Integer(responseTime));
        } catch (Exception e) {
            System.out.println("not processed: " + arr.getCompleteRecord());
        }
    }

    private void setSessionTimes(String inputLine) throws Exception {
        String line = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.indexOf(" "));
        if (plainStartTime == null) {
            this.plainStartTime = inputLine;
            startTime = sdf_apache_time.parse(line).getTime();
        } else {
            this.plainEndTime = inputLine;
            endTime = sdf_apache_time.parse(line).getTime();
        }
    }

    public void setBrowserConfiguration(String browser) {
        if (this.browser == null) {
            this.browser = browser;
            HashMap<String, ALE_Browser> xxx = ApacheLog_Db.getBrowserConfiguration();
            ALE_Browser x = xxx.get(browser);
            if (x != null) x.incrementBrwoserCount(); else System.out.println("Browser Db is null: " + browser);
        }
    }

    public void addResponseCode(String responseCode) {
        if (!responseCodes.containsKey(responseCode)) responseCodes.put(responseCode, new ALE_ResponseCode(responseCode)); else responseCodes.get(responseCode).incrementCount();
    }

    public String getSessionID() {
        return this.sessionId;
    }

    public int getTotalRequestCount() {
        return this.totalRequestCount;
    }

    public HashMap<String, ALE_ResponseCode> getResponseCodes() {
        return this.responseCodes;
    }

    public String getPlainStartTime() {
        return this.plainStartTime;
    }

    public String getPlainEndTime() {
        if (plainEndTime == null) return this.plainStartTime;
        return this.plainEndTime;
    }

    public long getSessionStartTime() {
        return this.startTime;
    }

    public long getSessionEndTime() {
        if (endTime == 0) return startTime;
        return this.endTime;
    }

    public long getSessionDuration() {
        if (endTime != 0 && startTime != 0) {
            return endTime - startTime;
        }
        return 0;
    }

    public int getResponseTime_max() {
        return this.responseTime_max;
    }

    public int getResponseTime_avg() {
        return this.responseTime_avg;
    }

    public ArrayList<Integer> getResponseTimes() {
        return this.responseTimes;
    }

    public ArrayList<Integer> getEntriesInDb() {
        return this.entriesInDb;
    }

    public void addEntryInDb(Integer lineNumber) {
        entriesInDb.add(lineNumber);
    }
}
