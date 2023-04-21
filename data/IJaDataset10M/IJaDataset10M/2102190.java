package com.vertigrated.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This ignore all exceptions whose getMessage() matches the one supplied, case is NOT important
 */
public class IgnoreExceptionHandler extends SQLExceptionHandler {

    private List<String> messagesToIgnore;

    public IgnoreExceptionHandler(final Object object, final String messageToIgnore) {
        this(object);
    }

    public IgnoreExceptionHandler(final Object object) {
        super(object);
        this.messagesToIgnore = new ArrayList<String>();
    }

    public void add(final String messageToIgnore) {
        this.messagesToIgnore.add(messageToIgnore);
    }

    /**
     * Tests the SQLException.getMessage() against the messagesToIgnore, if they match it just ignores them
     * if it doesn't it just passes on thru to the super class
     *
     * @param sql
     * @param e
     */
    public boolean handleException(final SQLStatement sql, final SQLException e) {
        if (this.messagesToIgnore.contains(sql.getCommand())) {
            return true;
        } else {
            return super.handleException(sql, e);
        }
    }
}
