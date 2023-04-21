package org.rjam.report;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.rjam.Event;
import org.rjam.EventSummary;
import org.rjam.EventUnit;
import org.rjam.api.IReporter;
import org.rjam.base.BaseQueueReporter;
import org.rjam.xml.Token;

/**
 * @author Tony Bringardner
 * 
 * Keep track of events and report a summary at 
 * some arbitrary frequency.
 *
 */
public abstract class BaseSummaryReporter extends BaseQueueReporter implements IReporter {

    private static final long serialVersionUID = 1L;

    private static final String PROP_EMIT_FREQ = "EminFreq";

    private long emitFreq = 5000;

    private Map<String, EventUnit> eventUnits = new HashMap<String, EventUnit>();

    private Map<String, EventSummary> summary = new HashMap<String, EventSummary>();

    private long emitTime = System.currentTimeMillis() + 5000;

    private int maxEmptyCnt;

    public BaseSummaryReporter() {
    }

    public abstract void deliverSummary(EventSummary sum) throws IOException;

    public void configure(Token tok) {
        super.configure(tok);
        String tmp = getProperty(PROP_EMIT_FREQ);
        if (tmp != null) {
            try {
                setEmitFreq(Long.parseLong(tmp));
            } catch (Exception ex) {
            }
        }
    }

    public long getEmitFreq() {
        return emitFreq;
    }

    public void setEmitFreq(long emitFreq) {
        this.emitFreq = emitFreq;
    }

    public long getEmitTime() {
        return emitTime;
    }

    public void setEmitTime(long emitTime) {
        this.emitTime = emitTime;
    }

    public Map<String, EventUnit> getEventUnits() {
        return eventUnits;
    }

    public void setEventUnits(Map<String, EventUnit> eventUnits) {
        this.eventUnits = eventUnits;
    }

    public Map<String, EventSummary> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, EventSummary> summary) {
        this.summary = summary;
    }

    public void writeEventUnit(EventUnit unit) throws IOException {
        int type = EventUnit.getAction(unit.getAction());
        if (type == EventUnit.ACTION_START) {
            doStart(unit);
        } else if (type == EventUnit.ACTION_END || type == EventUnit.ACTION_ERROR) {
            doEvent(unit);
        } else {
            doAction(unit);
        }
    }

    public String buildKey(EventUnit unit) {
        Event event = unit.getEvent();
        return event.getObjectId() + "." + event.getEventId();
    }

    public String buildSummaryKey(EventUnit unit) {
        Event event = unit.getEvent();
        return event.getName() + "." + unit.getEncodedData();
    }

    public void doEvent(EventUnit unit) throws IOException {
        String key = buildKey(unit);
        EventUnit start = (EventUnit) eventUnits.remove(key);
        if (start != null) {
            key = buildSummaryKey(start);
            updateSummary(start, unit, key);
        }
    }

    public void doAction(EventUnit unit) throws IOException {
        String key = buildKey(unit);
        EventUnit start = (EventUnit) eventUnits.get(key);
        if (start != null) {
            key = buildSummaryKey(start) + "." + unit.getAction() + "." + unit.getEncodedData();
            updateSummary(start, unit, key);
        }
    }

    protected void writeEvents() {
        super.writeEvents();
        try {
            checkEmit();
        } catch (IOException e) {
            logError("Emit error", e);
        }
    }

    public void checkEmit() throws IOException {
        if (emitTime < System.currentTimeMillis()) {
            emitData();
        }
    }

    public void updateSummary(EventUnit start, EventUnit unit, String key) throws IOException {
        EventSummary sum = (EventSummary) summary.get(key);
        if (sum == null) {
            if (unit == null) {
                sum = new EventSummary(unit, start.getEncodedData());
            } else {
                sum = new EventSummary(unit, start.getEncodedData(), unit.getEncodedData());
            }
            summary.put(key, sum);
        }
        sum.incCount(unit.getTime() - start.getTime());
        checkEmit();
    }

    public void emitData() throws IOException {
        for (Iterator<String> it = summary.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            EventSummary sum = (EventSummary) summary.get(key);
            if (sum.getCnt() > 0) {
                deliverSummary(sum);
                sum.clear();
            } else if (sum.getEmptyCount(true) > maxEmptyCnt) {
                it.remove();
            }
        }
        flushOutput();
        emitTime = System.currentTimeMillis() + emitFreq;
    }

    public abstract void flushOutput() throws IOException;

    public void doStart(EventUnit unit) {
        eventUnits.put(buildKey(unit), unit);
    }
}
