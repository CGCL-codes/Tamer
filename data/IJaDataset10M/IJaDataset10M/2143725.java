package org.jivesoftware.spark.ui;

public interface Sparkler {

    /**
     * @param message Message for sparkler.
     * @param decorator Decorator to handler sparkler.
     */
    void decorateMessage(String message, SparklerDecorator decorator);
}
