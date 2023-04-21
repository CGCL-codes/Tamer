package com.xerox.amazonws.fps;

import java.io.Serializable;

/**
 * @author J. Bernard
 * @author Elastic Grid, LLC.
 * @author jerome.bernard@elastic-grid.com
 */
public class UsageLimit implements Serializable {

    private final Type type;

    private final Periodicity periodicity;

    public UsageLimit(Type type) {
        this(type, null);
    }

    public UsageLimit(Type type, Periodicity periodicity) {
        this.type = type;
        this.periodicity = periodicity;
    }

    public Type getType() {
        return type;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UsageLimit");
        sb.append("{type=").append(type);
        sb.append(", periodicity=").append(periodicity);
        sb.append('}');
        return sb.toString();
    }

    enum Type {

        AMOUNT("Anount"), COUNT("Count");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    class Periodicity {

        private final int count;

        private final RecurringGranularity granularity;

        public Periodicity(int count, RecurringGranularity granularity) {
            this.count = count;
            this.granularity = granularity;
        }

        public int getCount() {
            return count;
        }

        public RecurringGranularity getGranularity() {
            return granularity;
        }

        public String toString() {
            return count + " " + granularity.toString();
        }
    }
}
