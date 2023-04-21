package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;

/**
 * This interface defines the methods that are needed to access the dissector's
 * DOM.
 */
public interface DocumentVisitor {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Called when visiting the document.
     * @param document The document that is being visited.
     */
    public void visitDocument(DissectableDocument document) throws DissectionException;

    /**
     * Called when visiting an element node.
     * @param element The element node.
     */
    public void visitElement(DissectableElement element) throws DissectionException;

    /**
     * Called when visiting an text node.
     * @param text The text node.
     */
    public void visitText(DissectableText text) throws DissectionException;

    /**
     * Called when visiting a shard link element.
     * @param element The element node.
     */
    public void visitShardLink(DissectableElement element) throws DissectionException;

    /**
     * Called when visiting a shard link group element.
     * @param element The element node.
     */
    public void visitShardLinkGroup(DissectableElement element) throws DissectionException;

    /**
     * Called when visiting a shard link conditional element.
     * @param element The element node.
     */
    public void visitShardLinkConditional(DissectableElement element) throws DissectionException;

    /**
     * Called when visiting a dissectable area element.
     * @param element The element node.
     */
    public void visitDissectableArea(DissectableElement element) throws DissectionException;

    /**
     * Called when visiting a keep together element.
     * @param element The element node.
     */
    public void visitKeepTogether(DissectableElement element) throws DissectionException;
}
