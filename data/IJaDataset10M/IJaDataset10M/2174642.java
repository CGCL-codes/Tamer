package com.akjava.wiki.client.core;

/**
 * 
 *
 */
public interface LineParser {

    public boolean canParse(String line);

    public Element parse(Element element, String line);
}
