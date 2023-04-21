package com.google.code.sagetvaddons.sre.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A concrete implementation of AiringMonitor that monitors Vancouver 2010 Ice Hockey
 * @version $Id$
 */
class AiringMonitorOlyIceHockeyWomen extends AiringMonitor {

    private static final Logger LOG = Logger.getLogger(AiringMonitorOlyIceHockeyWomen.class);

    private static final SimpleDateFormat DATE_FMT = initFmt();

    private static final SimpleDateFormat initFmt() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return fmt;
    }

    /**
	 * The URL used to parse game data from
	 */
    static final String FEED_NAME = "oly-ih";

    static final String TITLE = "Olympic Women's Ice Hockey";

    private static final JSONObject readFeed(long airingDate) {
        String date;
        if (airingDate > 0) date = DATE_FMT.format(new Date(airingDate)); else date = "0";
        String url = "http://sre-maps.appspot.com/feedFetcher?raw=1&f=oly-ih&d=" + date;
        try {
            return new JSONObject(WebReader.read(url));
        } catch (JSONException e) {
            LOG.error("Failed to parse scores feed from Olympic data feed!", e);
            return null;
        }
    }

    /**
	 * Constructor; find unique terms for game and parse web page for status
	 * @param desc String that identifies who's playing in the game
	 * @param airingDate The airing time of the event being monitored as a Java timestamp
	 */
    AiringMonitorOlyIceHockeyWomen(String actualTitle, String desc, long airingDate) {
        super(actualTitle, desc, airingDate);
        List<String> terms = generateSearchTerms(desc, AiringMonitor.EventType.OLY_IH);
        JSONObject events = readFeed(airingDate);
        if (events != null) {
            JSONArray scores = null;
            try {
                scores = events.getJSONArray("events");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            boolean foundMatch = false;
            String homeName, awayName;
            String status = null;
            for (int i = 0; i < scores.length(); ++i) {
                try {
                    JSONObject e = scores.getJSONObject(i);
                    if (!e.getString("link").contains("women")) continue;
                    homeName = e.getJSONObject("homeTeam").getString("nocId");
                    awayName = e.getJSONObject("awayTeam").getString("nocId");
                    status = e.getString("status").toUpperCase();
                } catch (JSONException x) {
                    LOG.error("Unable to find game data!", x);
                    continue;
                }
                int matches = 0;
                for (String term : terms) {
                    if (homeName.equals(term) || awayName.equals(term)) ++matches;
                    if (matches == 2) {
                        foundMatch = true;
                        break;
                    }
                }
                if (foundMatch) break;
            }
            if (foundMatch) {
                setError(false);
                setValid(true);
                setOver(status.startsWith("OFFICIAL"));
                setStatus(status);
            } else {
                setError(true);
                setValid(false);
            }
        }
    }
}
