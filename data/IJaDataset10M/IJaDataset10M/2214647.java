package org.tamacat.log.impl;

public class SimpleLevel extends Level<String> {

    static final SimpleLevel FATAL = new SimpleLevel("FATAL", 900);

    static final SimpleLevel ERROR = new SimpleLevel("ERROR", 800);

    static final SimpleLevel WARN = new SimpleLevel("WARN", 700);

    static final SimpleLevel INFO = new SimpleLevel("INFO", 500);

    static final SimpleLevel DEBUG = new SimpleLevel("DEBUG", 400);

    static final SimpleLevel TRACE = new SimpleLevel("TRACE", 200);

    final int priority;

    SimpleLevel(String level, int priority) {
        super(level);
        this.priority = priority;
    }

    @Override
    public String toString() {
        return getLevel();
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode() + priority;
        result = prime * result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof SimpleLevel)) return false;
        if (priority != ((SimpleLevel) obj).priority) return false;
        return true;
    }
}
