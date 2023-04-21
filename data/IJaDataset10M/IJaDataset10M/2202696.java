package com.google.inject.struts2.example;

import com.google.inject.servlet.SessionScoped;

/**
 * Session-scoped counter.
 */
@SessionScoped
public class Counter {

    int count = 0;

    /** Increments the count and returns the new value. */
    public synchronized int increment() {
        return count++;
    }
}
