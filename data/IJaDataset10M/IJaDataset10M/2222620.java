package com.jxva.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-04-07 16:13:19 by Jxva
 */
public class QueryImpl implements Query {

    private final Statement statement;

    private final JdbcTemplate template;

    public QueryImpl(Statement statement, JdbcTemplate template) {
        this.statement = statement;
        this.template = template;
    }

    public int executeUpdate() {
        return template.update(statement.getSql());
    }

    public String[] getNamedParameters() {
        return null;
    }

    public String getQueryString() {
        return statement.getSql();
    }

    public Iterator iterate() {
        return null;
    }

    public List list() {
        return null;
    }

    public Query setBigDecimal(int position, BigDecimal number) {
        return null;
    }

    public Query setBigDecimal(String name, BigDecimal number) {
        return null;
    }

    public Query setBigInteger(int position, BigInteger number) {
        return null;
    }

    public Query setBigInteger(String name, BigInteger number) {
        return null;
    }

    public Query setBinary(int position, byte[] val) {
        return null;
    }

    public Query setBinary(String name, byte[] val) {
        return null;
    }

    public Query setBoolean(int position, boolean val) {
        return null;
    }

    public Query setBoolean(String name, boolean val) {
        return null;
    }

    public Query setByte(int position, byte val) {
        return null;
    }

    public Query setByte(String name, byte val) {
        return null;
    }

    public Query setCacheable(boolean cacheable) {
        return null;
    }

    public Query setCalendar(int position, Calendar calendar) {
        return null;
    }

    public Query setCalendar(String name, Calendar calendar) {
        return null;
    }

    public Query setCalendarDate(int position, Calendar calendar) {
        return null;
    }

    public Query setCalendarDate(String name, Calendar calendar) {
        return null;
    }

    public Query setCharacter(int position, char val) {
        return null;
    }

    public Query setCharacter(String name, char val) {
        return null;
    }

    public Query setDate(int position, Date date) {
        return null;
    }

    public Query setDate(String name, Date date) {
        return null;
    }

    public Query setDouble(int position, double val) {
        return null;
    }

    public Query setDouble(String name, double val) {
        return null;
    }

    public Query setFetchSize(int fetchSize) {
        return null;
    }

    public Query setFirstResult(int firstResult) {
        return null;
    }

    public Query setFloat(int position, float val) {
        return null;
    }

    public Query setFloat(String name, float val) {
        return null;
    }

    public Query setInteger(int position, int val) {
        return null;
    }

    public Query setInteger(String name, int val) {
        return null;
    }

    public Query setLocale(int position, Locale locale) {
        return null;
    }

    public Query setLocale(String name, Locale locale) {
        return null;
    }

    public Query setLong(int position, long val) {
        return null;
    }

    public Query setLong(String name, long val) {
        return null;
    }

    public Query setMaxResults(int maxResults) {
        return null;
    }

    public Query setParameter(int position, Object val) {
        return null;
    }

    public Query setParameter(String name, Object val) {
        return null;
    }

    public Query setProperties(Map<String, Object> bean) {
        return null;
    }

    public Query setReadOnly(boolean readOnly) {
        return null;
    }

    public Query setSerializable(int position, Serializable val) {
        return null;
    }

    public Query setSerializable(String name, Serializable val) {
        return null;
    }

    public Query setShort(int position, short val) {
        return null;
    }

    public Query setShort(String name, short val) {
        return null;
    }

    public Query setString(int position, String val) {
        return null;
    }

    public Query setString(String name, String val) {
        return null;
    }

    public Query setText(int position, String val) {
        return null;
    }

    public Query setText(String name, String val) {
        return null;
    }

    public Query setTime(int position, Date date) {
        return null;
    }

    public Query setTime(String name, Date date) {
        return null;
    }

    public Query setTimestamp(int position, Date date) {
        return null;
    }

    public Query setTimestamp(String name, Date date) {
        return null;
    }

    public Object uniqueResult() {
        return null;
    }
}
