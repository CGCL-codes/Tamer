package net.wgen.op.db;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: SuspectPerformanceException.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class SuspectPerformanceException extends RuntimeException {

    private Map _offensiveObjects = new LinkedHashMap();

    public SuspectPerformanceException(String s) {
        super(s);
    }

    public Map getOffensiveObjects() {
        return _offensiveObjects;
    }

    public void addOffensiveObject(String name, Object offensiveObject) {
        _offensiveObjects.put(name, offensiveObject);
    }

    public String getMessage() {
        StringBuffer buf = new StringBuffer(256);
        buf.append(super.getMessage()).append(_offensiveObjects.size()).append(" offensive objects collected: ");
        Iterator it = _offensiveObjects.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            try {
                buf.append("   [").append(entry.getKey()).append("] ").append(entry.getValue());
            } catch (Exception ex) {
                buf.append("Exception encountered on offensiveObject[ ").append(entry.getKey()).append("].toString(): ");
                buf.append(ex.getMessage());
            }
        }
        return buf.toString();
    }
}
