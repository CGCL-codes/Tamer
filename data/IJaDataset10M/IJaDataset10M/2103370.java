package com.bbn.vessel.author.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enumeration of all possible terminal types. There is an input (sink) and
 * output (source) type for each signal type.
 */
public final class TerminalType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<String, TerminalType> MAP;

    private static final Map<SignalType, TerminalType> MAP_TRUE;

    private static final Map<SignalType, TerminalType> MAP_FALSE;

    static {
        Map<SignalType, TerminalType> mt = new HashMap<SignalType, TerminalType>();
        Map<SignalType, TerminalType> mf = new HashMap<SignalType, TerminalType>();
        Map<String, TerminalType> m = new LinkedHashMap<String, TerminalType>();
        for (SignalType st : SignalType.values()) {
            TerminalType tt = new TerminalType(st, true);
            mt.put(st, tt);
            m.put(tt.name(), tt);
            TerminalType tf = new TerminalType(st, false);
            mf.put(st, tf);
            m.put(tf.name(), tf);
        }
        MAP = m;
        MAP_TRUE = mt;
        MAP_FALSE = mf;
    }

    public static final TerminalType TRIGGER_SINK = valueOf(SignalType.TRIGGER, true);

    public static final TerminalType TRIGGER_SOURCE = valueOf(SignalType.TRIGGER, false);

    public static final TerminalType CONDITION_SINK = valueOf(SignalType.CONDITION, true);

    public static final TerminalType CONDITION_SOURCE = valueOf(SignalType.CONDITION, false);

    public static final TerminalType REPORT_SINK = valueOf(SignalType.REPORT, true);

    public static final TerminalType REPORT_SOURCE = valueOf(SignalType.REPORT, false);

    private final SignalType signalType;

    private final boolean isSink;

    private final String name;

    private final String coreTypeName;

    private final String displayName;

    private TerminalType(SignalType signalType, boolean isSink) {
        this.signalType = signalType;
        this.isSink = isSink;
        String sn = signalType.getName();
        String ext = (isSink ? "SINK" : "SOURCE");
        this.name = sn + "_" + ext;
        this.coreTypeName = ((isSink ? "in_" : "out_") + (sn.equals("TRIGGER") ? "tr" : sn.equals("CONDITION") ? "cd" : sn.equals("REPORT") ? "rpt" : "ds"));
        this.displayName = capitalize(sn) + " " + capitalize(ext);
    }

    public static TerminalType valueOf(String s) {
        TerminalType ret = MAP.get(s);
        if (ret == null) {
            throw new IllegalArgumentException("Unknown TerminalType " + s);
        }
        return ret;
    }

    public static TerminalType valueOf(SignalType st, boolean isSink) {
        TerminalType ret = (isSink ? MAP_TRUE.get(st) : MAP_FALSE.get(st));
        if (ret == null) {
            throw new IllegalArgumentException("Unknown TerminalType " + st + " " + isSink);
        }
        return ret;
    }

    public static TerminalType[] values() {
        Collection<TerminalType> c = MAP.values();
        return c.toArray(new TerminalType[c.size()]);
    }

    public String name() {
        return name;
    }

    public String getName() {
        return name;
    }

    /**
     * Check if this terminal type is an input
     * 
     * @return true if this terminal type is an input
     */
    public boolean isInputType() {
        return isSink;
    }

    /**
     * @return the SignalType corresponding to this terminal type
     */
    public SignalType getSignalType() {
        return signalType;
    }

    /**
     * @return the string representation of this TerminalType as used in the
     *         core
     */
    public String getCoreTypeName() {
        return coreTypeName;
    }

    /**
     * @return the display form of this terminal type
     */
    public String getDisplayName() {
        return displayName;
    }

    public boolean isOpposite(TerminalType other) {
        return (isSink != other.isSink && signalType == other.signalType);
    }

    public TerminalType getOpposite() {
        return valueOf(signalType, !isSink);
    }

    @Override
    public String toString() {
        return name;
    }

    private Object readResolve() {
        return valueOf(name);
    }

    private static String capitalize(String string) {
        return string.substring(0, 1) + string.substring(1).toLowerCase();
    }
}
