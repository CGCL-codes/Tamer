package com.xsm.gwt.widgets.client;

/**
 * @author Sony Mathew
 */
public interface Source {

    /**
     * Identifies the consumer consuming the Source.
     */
    public interface Consumer {

        public void setContent(Source src, String content);

        public void failedContentLoad(Source src, Throwable x);
    }

    /**
     * Add a consumer to receive content.
     * 
     * author Sony Mathew
     */
    public void addConsumer(Consumer c);

    /**
     * Load the content from the source this object represents.
     * 
     * author Sony Mathew
     */
    public void refresh();
}
