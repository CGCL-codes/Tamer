package com.aepryus.loom;

import java.text.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import com.aepryus.util.*;
import com.aepryus.util.Time;

public abstract class SQL {

    private static Persistence persistence;

    public static void setPersistence(Persistence persistence) {
        SQL.persistence = persistence;
    }

    public static Persistence getPersistence() throws PersistenceException {
        if (persistence == null) Controller.controller.initializePersistence();
        return persistence;
    }

    public static Map[] query(String query, String[] names) throws PersistenceException {
        return getPersistence().query(query, names);
    }

    public static Map[] query(String query) throws PersistenceException {
        return getPersistence().query(query);
    }

    public static Matrix query(String key, String query) throws PersistenceException {
        return getPersistence().query(key, query);
    }

    public static Matrix query(String key, String query, String[] names) throws PersistenceException {
        return getPersistence().query(key, query, names);
    }

    public static long queryLong(String query) throws PersistenceException {
        return getPersistence().queryLong(query);
    }

    public static double queryDouble(String query) throws PersistenceException {
        return getPersistence().queryDouble(query);
    }

    public static Date queryDate(String query) throws PersistenceException {
        return getPersistence().queryDate(query);
    }

    public static int update(String update) throws PersistenceException {
        return getPersistence().update(update);
    }

    public static void commit() throws PersistenceException {
        getPersistence().commit();
    }

    public static void rollback() throws PersistenceException {
        getPersistence().rollback();
    }

    public static boolean isConnected() {
        return persistence != null;
    }

    public static void close() throws PersistenceException {
        if (isConnected()) getPersistence().close();
        persistence = null;
    }

    public static String toSQL(String value) {
        return persistence.toSQL(value);
    }

    public static synchronized String toSQL(java.util.Date value) {
        return persistence.toSQL(value);
    }

    public static synchronized String toSQL(com.aepryus.util.Time value) {
        return value == null ? "0" : "" + value.intValue();
    }

    public static synchronized String toSQL(com.aepryus.util.DateTime value) {
        return persistence.toSQL(value);
    }

    public static String toSQL(long value) {
        return "" + value;
    }

    public static String toSQL(double value) {
        return "" + value;
    }

    public static String toSQL(boolean value) {
        return value ? "1" : "0";
    }

    public static String toString(Object value) {
        return (String) value;
    }

    public static Date toDate(Object value) {
        return persistence.toDate(value);
    }

    public static Time toTime(Object value) {
        if (value == null) return null;
        return new com.aepryus.util.Time(((Number) value).intValue());
    }

    public static DateTime toDateTime(Object value) {
        return persistence.toDateTime(value);
    }

    public static int toInt(Object value) {
        return value == null ? 0 : ((Number) value).intValue();
    }

    public static long toLong(Object value) {
        return value == null ? 0 : ((Number) value).longValue();
    }

    public static double toDouble(Object value) {
        return value == null ? 0 : ((Number) value).doubleValue();
    }

    public static boolean toBoolean(Object value) {
        return value != null && ((Number) value).longValue() == 1;
    }

    public static Double add(Object a, Object b) {
        double x = a == null ? 0 : ((Number) a).doubleValue();
        double y = b == null ? 0 : ((Number) b).doubleValue();
        return x + y;
    }

    public static Double sub(Object a, Object b) {
        double x = a == null ? 0 : ((Number) a).doubleValue();
        double y = b == null ? 0 : ((Number) b).doubleValue();
        return x - y;
    }
}
