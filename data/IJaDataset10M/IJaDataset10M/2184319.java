package com.bbn.vessel.core.runtime.condition;

import java.util.LinkedHashMap;
import java.util.Map;
import com.bbn.vessel.core.service.PublisherBase;

/**
 * @author rtomlinson
 *
 * Accumulates logical values from received signals.
 */
public abstract class IsLogicalAccumulator extends PublisherBase {

    private static final class Value {

        public boolean seen = false;

        public boolean is_true = false;
    }

    private final Map<String, Value> in_cds = new LinkedHashMap<String, Value>();

    private int seen_count = 0;

    private int true_count = 0;

    protected boolean is_true;

    protected String getInputWarning() {
        int missing_count = 0;
        StringBuffer buf = new StringBuffer();
        for (Map.Entry<String, Value> me : in_cds.entrySet()) {
            if (me.getValue().seen) {
                continue;
            }
            missing_count++;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(me.getKey());
        }
        if (missing_count <= 0) {
            return null;
        }
        return "waiting for " + missing_count + " in_cds: " + buf;
    }

    protected int getInputCount() {
        return in_cds.size();
    }

    @Override
    protected void configure() {
        super.configure();
        for (Map.Entry<String, Object> me : args.entrySet()) {
            String key = me.getKey();
            String value = (String) me.getValue();
            if (!key.matches("^in_cd[0-9]*$")) {
                if (key.matches("^in_tr[0-9]*$")) {
                    throw new IllegalArgumentException("trigger input: " + me);
                }
                continue;
            }
            in_cds.put(value, new Value());
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Inputs: " + in_cds);
        }
    }

    protected final boolean update(Object o) {
        if (!(o instanceof Condition)) {
            return false;
        }
        Condition c = (Condition) o;
        String s = c.getName();
        boolean b = c.isTrue();
        Value v = in_cds.get(s);
        if (v == null) {
            return false;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("received " + (v.seen ? "" : " initial ") + s + "=" + b);
        }
        if (!v.seen) {
            v.seen = true;
            seen_count++;
            v.is_true = b;
            if (b) {
                true_count++;
            }
            if (seen_count < in_cds.size()) {
                return false;
            }
            is_true = compute(true_count, in_cds.size());
        } else {
            if (v.is_true == b) {
                return false;
            }
            v.is_true = b;
            if (b) {
                true_count++;
            } else {
                true_count--;
            }
            if (seen_count < in_cds.size()) {
                return false;
            }
            boolean old_is_true = is_true;
            is_true = compute(true_count, in_cds.size());
            if (is_true == old_is_true) {
                return false;
            }
        }
        return true;
    }

    protected abstract boolean compute(int i, int n);
}
