package com.hisham.util;

import java.io.*;

/**
 *
 * <p>Title: Web Services for Parking</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p> </p>
 *
 * @author Ali Hisham Malik
 * @version 2.0
 */
public class IntegerField implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 2947213459420313196L;

    protected int value;

    protected String title = new String();

    public IntegerField() {
    }

    public IntegerField(String title, int value) {
        this.setTitle(title);
        this.setValue(value);
    }

    public IntegerField(int value, String title) {
        this.setTitle(title);
        this.setValue(value);
    }

    public String getTitle() {
        return title;
    }

    public int getValue() {
        return value;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }
}
