package com.emeraldjb.base;

import java.util.*;

/**
 * <p>
 * A Nugget represents an aribrary collection of columns selected from 
 * any number of tables.
 * </p>
 * <p>
 * Copyright (c) 2003, 2004 by Emeraldjb LLC
 * All Rights Reserved.
 * </p>
 */
public class Nugget extends EmeraldjbBean {

    private String name = "";

    private String rawFrom = "";

    private List fields;

    private List finders;

    private Javadoc javadoc;

    private List from;

    /**
	 * Returns the fields.
	 * @return List
	 */
    public List getFields() {
        if (fields == null) fields = new Vector();
        return fields;
    }

    /**
	 * Returns the finders.
	 * @return List
	 */
    public List getFinders() {
        if (finders == null) finders = new Vector();
        return finders;
    }

    /**
	 * Returns the name.
	 * @return String
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the javadoc.
	 * @return Javadoc
	 */
    public Javadoc getJavadoc() {
        return javadoc;
    }

    /**
	 * Returns the rawFrom.
	 * @return String
	 */
    public String getRawFrom() {
        return rawFrom;
    }

    /**
	 * Returns the from.
	 * @return List
	 */
    public List getFrom() {
        return from;
    }

    /**
	 * Sets the fields.
	 * @param fields The fields to set
	 */
    public void setFields(List fields) {
        this.fields = fields;
    }

    /**
	 * Sets the finders.
	 * @param finders The finders to set
	 */
    public void setFinders(List finders) {
        this.finders = finders;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        if (name == null) {
            throw new RuntimeException("Nugget name must not be null.");
        }
        String oldName = this.name;
        this.name = name;
        this.firePropertyChange("Name", oldName, this.name);
    }

    /**
	 * Sets the javadoc.
	 * @param javadoc The javadoc to set
	 */
    public void setJavadoc(Javadoc javadoc) throws EmeraldjbException {
        if (javadoc == null) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to set javadoc to null value.");
        }
        this.javadoc = javadoc;
        javadoc.setParent(this);
        javadoc.setInModel(true);
        fireEmeraldjbEvent(javadoc, EmeraldjbEvent.ACTION_ADD, 0);
    }

    public void updateJavadoc(Javadoc javadoc) throws EmeraldjbException {
        if (!(this.javadoc == javadoc)) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to update javadoc that is different from current instance:" + javadoc.toString());
        }
        fireEmeraldjbEvent(javadoc, EmeraldjbEvent.ACTION_UPDATE, 0);
    }

    /**
	 * Sets the rawFrom.
	 * @param rawFrom The rawFrom to set
	 */
    public void setRawFrom(String rawFrom) {
        if (rawFrom == null) {
            throw new RuntimeException("rawFrom must not be null.");
        }
        String oldRawFrom = this.rawFrom;
        this.rawFrom = rawFrom;
        this.firePropertyChange("rawFrom", oldRawFrom, this.rawFrom);
    }

    /**
	 * Sets the from.
	 * @param from The from to set
	 */
    public void setFrom(List from) {
        this.from = from;
    }

    public void addField(Field field) throws EmeraldjbException {
        addField(field, getFields().size());
    }

    public void addField(Field field, int index) throws EmeraldjbException {
        synchronized (fields) {
            if (index >= 0 || index <= getFields().size()) {
                getFields().add(index, field);
            } else {
                throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempting to insert field beyond array values.");
            }
            field.setParent(this);
            field.setInModel(true);
            fireEmeraldjbEvent(field, EmeraldjbEvent.ACTION_ADD, index);
        }
    }

    public void removeField(Field field) throws EmeraldjbException {
        int index = -1;
        synchronized (fields) {
            index = getFields().indexOf(field);
            if (!getFields().remove(field)) {
                throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to remove field that is not present:" + field.toString());
            }
        }
        field.setParent(null);
        field.setInModel(false);
        fireEmeraldjbEvent(field, EmeraldjbEvent.ACTION_DEL, index);
    }

    public void moveField(Field field, String direction) throws EmeraldjbException {
        if (!(getFields().contains(field))) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to move field that is not present:" + field.toString());
        }
        if (!(EmeraldjbEvent.DIRECTION_UP.equalsIgnoreCase(direction) || EmeraldjbEvent.DIRECTION_DOWN.equalsIgnoreCase(direction))) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Must move field EmeraldjbEvent.DIRECTION_UP or EmeraldjbEvent.DIRECTION_DOWN, attempted to move with (" + direction + ")");
        }
        synchronized (fields) {
            int size = getFields().size();
            int index = getFields().indexOf(field);
            if (EmeraldjbEvent.DIRECTION_UP.equalsIgnoreCase(direction)) {
                if (index > 0) {
                    Field prevField = (Field) getFields().get(index - 1);
                    getFields().set(index - 1, field);
                    getFields().set(index, prevField);
                    fireEmeraldjbEvent(field, EmeraldjbEvent.ACTION_MOVE, direction, index);
                }
            } else if (EmeraldjbEvent.DIRECTION_DOWN.equalsIgnoreCase(direction)) {
                if (index < size - 1) {
                    Field nextField = (Field) getFields().get(index + 1);
                    getFields().set(index + 1, field);
                    getFields().set(index, nextField);
                    fireEmeraldjbEvent(field, EmeraldjbEvent.ACTION_MOVE, direction, index);
                }
            }
        }
    }

    public void updateField(Field field) throws EmeraldjbException {
        if (!(getFields().contains(field))) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to update field that is not present:" + field.toString());
        }
        int index = getFields().indexOf(field);
        fireEmeraldjbEvent(field, EmeraldjbEvent.ACTION_UPDATE, index);
    }

    public void addFinder(Finder finder) throws EmeraldjbException {
        addFinder(finder, getFinders().size());
    }

    public void addFinder(Finder finder, int index) throws EmeraldjbException {
        synchronized (finders) {
            if (index >= 0 || index <= getFinders().size()) {
                getFinders().add(index, finder);
            } else {
                throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempting to insert finder beyond array values.");
            }
            finder.setParent(this);
            finder.setInModel(true);
            fireEmeraldjbEvent(finder, EmeraldjbEvent.ACTION_ADD, index);
        }
    }

    public void removeFinder(Finder finder) throws EmeraldjbException {
        int index = -1;
        synchronized (finders) {
            index = getFinders().indexOf(finder);
            if (!getFinders().remove(finder)) {
                throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to remove finder that is not present:" + finder.toString());
            }
        }
        finder.setParent(null);
        finder.setInModel(false);
        fireEmeraldjbEvent(finder, EmeraldjbEvent.ACTION_DEL, index);
    }

    public void moveFinder(Finder finder, String direction) throws EmeraldjbException {
        if (!(getFinders().contains(finder))) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to move finder that is not present:" + finder.toString());
        }
        if (!(EmeraldjbEvent.DIRECTION_UP.equalsIgnoreCase(direction) || EmeraldjbEvent.DIRECTION_DOWN.equalsIgnoreCase(direction))) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Must move finder EmeraldjbEvent.DIRECTION_UP or EmeraldjbEvent.DIRECTION_DOWN, attempted to move with (" + direction + ")");
        }
        synchronized (finders) {
            int size = getFinders().size();
            int index = getFinders().indexOf(finder);
            if (EmeraldjbEvent.DIRECTION_UP.equalsIgnoreCase(direction)) {
                if (index > 0) {
                    Finder prevFinder = (Finder) getFinders().get(index - 1);
                    getFinders().set(index - 1, finder);
                    getFinders().set(index, prevFinder);
                    fireEmeraldjbEvent(finder, EmeraldjbEvent.ACTION_MOVE, direction, index);
                }
            } else if (EmeraldjbEvent.DIRECTION_DOWN.equalsIgnoreCase(direction)) {
                if (index < size - 1) {
                    Finder nextFinder = (Finder) getFinders().get(index + 1);
                    getFinders().set(index + 1, finder);
                    getFinders().set(index, nextFinder);
                    fireEmeraldjbEvent(finder, EmeraldjbEvent.ACTION_MOVE, direction, index);
                }
            }
        }
    }

    public void updateFinder(Finder finder) throws EmeraldjbException {
        if (!(getFinders().contains(finder))) {
            throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E, "Attempted to update finder that is not present:" + finder.toString());
        }
        int index = getFinders().indexOf(finder);
        fireEmeraldjbEvent(finder, EmeraldjbEvent.ACTION_UPDATE, index);
    }
}
