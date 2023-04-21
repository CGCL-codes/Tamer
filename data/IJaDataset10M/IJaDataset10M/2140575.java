package com.yilan.java.util;

import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class YilanLogger implements Log {

    private Logger logger;

    public static Log getLogger(Class<?> clazz) {
        String name = clazz.getName();
        YilanLogger yl = new YilanLogger(Logger.getLogger(name));
        return yl;
    }

    public YilanLogger(Logger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public void exception(Exception e) {
        logger.warning("Exception: " + e.getMessage());
    }

    public void addHandler(Handler handler) throws SecurityException {
        logger.addHandler(handler);
    }

    public void config(String msg) {
        logger.config(msg);
    }

    public void entering(String sourceClass, String sourceMethod, Object param1) {
        logger.entering(sourceClass, sourceMethod, param1);
    }

    public void entering(String sourceClass, String sourceMethod, Object[] params) {
        logger.entering(sourceClass, sourceMethod, params);
    }

    public void entering(String sourceClass, String sourceMethod) {
        logger.entering(sourceClass, sourceMethod);
    }

    public boolean equals(Object obj) {
        return logger.equals(obj);
    }

    public void exiting(String sourceClass, String sourceMethod, Object result) {
        logger.exiting(sourceClass, sourceMethod, result);
    }

    public void exiting(String sourceClass, String sourceMethod) {
        logger.exiting(sourceClass, sourceMethod);
    }

    public void fine(String msg) {
        logger.fine(msg);
    }

    public void finer(String msg) {
        logger.finer(msg);
    }

    public void finest(String msg) {
        logger.finest(msg);
    }

    public Filter getFilter() {
        return logger.getFilter();
    }

    public Handler[] getHandlers() {
        return logger.getHandlers();
    }

    public Level getLevel() {
        return logger.getLevel();
    }

    public String getName() {
        return logger.getName();
    }

    public Logger getParent() {
        return logger.getParent();
    }

    public ResourceBundle getResourceBundle() {
        return logger.getResourceBundle();
    }

    public String getResourceBundleName() {
        return logger.getResourceBundleName();
    }

    public boolean getUseParentHandlers() {
        return logger.getUseParentHandlers();
    }

    public int hashCode() {
        return logger.hashCode();
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    public void log(Level level, String msg, Object param1) {
        logger.log(level, msg, param1);
    }

    public void log(Level level, String msg, Object[] params) {
        logger.log(level, msg, params);
    }

    public void log(Level level, String msg, Throwable thrown) {
        logger.log(level, msg, thrown);
    }

    public void log(Level level, String msg) {
        logger.log(level, msg);
    }

    public void log(LogRecord record) {
        logger.log(record);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
        logger.logp(level, sourceClass, sourceMethod, msg, param1);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
        logger.logp(level, sourceClass, sourceMethod, msg, params);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
        logger.logp(level, sourceClass, sourceMethod, msg, thrown);
    }

    public void logp(Level level, String sourceClass, String sourceMethod, String msg) {
        logger.logp(level, sourceClass, sourceMethod, msg);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
    }

    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
        logger.logrb(level, sourceClass, sourceMethod, bundleName, msg);
    }

    public void removeHandler(Handler handler) throws SecurityException {
        logger.removeHandler(handler);
    }

    public void setFilter(Filter newFilter) throws SecurityException {
        logger.setFilter(newFilter);
    }

    public void setLevel(Level newLevel) throws SecurityException {
        logger.setLevel(newLevel);
    }

    public void setParent(Logger parent) {
        logger.setParent(parent);
    }

    public void setUseParentHandlers(boolean useParentHandlers) {
        logger.setUseParentHandlers(useParentHandlers);
    }

    public void severe(String msg) {
        logger.severe(msg);
    }

    public void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
        logger.throwing(sourceClass, sourceMethod, thrown);
    }

    public String toString() {
        return logger.toString();
    }

    public void warning(String msg) {
        logger.warning(msg);
    }
}
