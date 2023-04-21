package com.enerjy.analyzer.java.rules.testfiles.T0241;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * JAVA0241 <code>readObject</code> or <code>writeObject</code> should be declared private in Serializable class.
 * 
 * @author Peter Carr
 */
@SuppressWarnings("all")
public class FTest02 implements Serializable {

    Object itemOne = null;

    public void setItemOne(Object o) {
        this.itemOne = o;
    }

    Object itemTwo = null;

    void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        itemOne = in.readObject();
        itemTwo = in.readObject();
    }
}
