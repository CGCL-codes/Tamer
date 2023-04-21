package com.akjava.wiki.client.htmlconverter;

import com.akjava.wiki.client.core.Node;
import com.akjava.wiki.client.modules.LineFolder;

/**
 * 
 *
 */
public class LineFolderConverter extends AbstractConverter {

    private Class targetClass = LineFolder.class;

    public boolean canConvert(Node node) {
        return node.getClass().equals(targetClass);
    }

    public String toHeader(Node node) {
        String result = "<p>";
        return result;
    }

    public String toFooter(Node node) {
        return "</p>";
    }
}
