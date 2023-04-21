package com.googlecode.greenbridge.storyharvester.impl;

import java.util.List;

/**
 *
 * @author ryan
 */
public interface ConfluenceContentParseStrategy {

    public List<String> getNarrative(String content);

    public Datatable getDatatable(String content);
}
