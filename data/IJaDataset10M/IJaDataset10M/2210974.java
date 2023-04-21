package com.aqua.stations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jsystem.framework.analyzer.AnalyzerParameterImpl;

/**
 * Analyzer that handles Ping command results.
 * The analyzer supports ping results in Windows and Linux formats.
 * Windows format:
 * ...
 * Ping statistics for 127.0.0.1:
 *		
 *     Packets: Sent = 4, Received = 4, Lost = 0 (0% loss),
 * ...
 * Linux format:
 * ...
 * --- 127.0.0.1 ping statistics ---
 * 4 packets transmitted, 4 received, 0% packet loss, time 3002ms
 * ...
 * 
 * @author Yoram Shamir
 */
public class PingAnalyzer extends AnalyzerParameterImpl {

    private int expected;

    private int sent;

    private int received;

    private int lost;

    private int min;

    private int max;

    private int average;

    /**
	 * Analyze ping results.
	 * Compare number of expected lost packets to actual lost packets
	 * @param expected	Expected loss percentage
	 */
    public PingAnalyzer(int expected) {
        this.expected = expected;
    }

    /**
	 * Analyze ping results.
	 * Compare number of tolerated lost packets to actual lost packets.
	 * Note that the analyzer does not use the expected number packets for analysis but for reporting only.
	 */
    public void analyze() {
        String text = (String) testAgainst;
        status = true;
        if (text == null) {
            title = "null testAgainst";
            status = false;
            return;
        }
        message = text;
        Pattern p = Pattern.compile("Sent\\s*=\\s*(\\d+),\\s*Received\\s*=\\s*(\\d+),\\s*Lost\\s*=\\s*\\d+\\s*\\((\\d+)%.*Minimum\\s*=\\s*(\\d+).*Maximum\\s*=\\s*(\\d+).*Average\\s*=\\s*(\\d+)", Pattern.DOTALL);
        Matcher m = p.matcher(text);
        if (!m.find()) {
            p = Pattern.compile("---\\s*\\n(\\d*)\\s*packets transmitted,\\s*(\\d*)\\s*received,\\s*(\\d*)%");
            m = p.matcher(text);
            if (!m.find()) {
                title = "Unknown ping output format";
                status = false;
                return;
            }
        }
        sent = Integer.parseInt(m.group(1));
        received = Integer.parseInt(m.group(2));
        lost = Integer.parseInt(m.group(3));
        min = Integer.parseInt(m.group(4));
        max = Integer.parseInt(m.group(5));
        average = Integer.parseInt(m.group(6));
        if (lost > expected) {
            status = false;
        }
        title = "Packets: Expected " + expected + "% lost. Sent = " + sent + ", Received = " + received + ", Lost = " + lost + "%" + ", Average = " + average;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
