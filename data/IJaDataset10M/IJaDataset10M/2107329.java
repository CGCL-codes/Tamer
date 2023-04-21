package com.jujunie.service.web;

/**
 * Represent a list of Areas
 * @author julien
 */
public interface WebAreaList {

    /**
     * @return true if another element is available
     */
    boolean hasNext();

    /**
     * @return the next WebArea in the list
     * @throws WebItemNotAvailableException if the list is exhausted
     */
    WebArea next() throws WebItemNotAvailableException;
}
