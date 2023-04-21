package com.streamsicle;

/**
 * Title:        Sequence
 * Description:  A singleton object that just returns the next number in a sequence.
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Unknown
 * @version 1.0
 */
public class Sequence {

    private static Sequence seq;

    public static Sequence getSequence() {
        if (seq == null) {
            seq = new Sequence();
        }
        return seq;
    }

    private int value;

    public Sequence() {
        value = 0;
    }

    public Sequence(int startVal) {
        value = startVal;
    }

    public synchronized int nextVal() {
        value += 1;
        return value;
    }
}
