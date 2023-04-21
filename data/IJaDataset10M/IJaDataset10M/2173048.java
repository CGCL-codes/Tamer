package net.cattaka.rdbassistant.sql;

public class TimeColumnConverter extends AbstractDateColumnConverter {

    public TimeColumnConverter() {
        super("HH:mm:ss");
    }
}
