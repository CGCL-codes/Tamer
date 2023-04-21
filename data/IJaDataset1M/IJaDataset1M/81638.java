package org.antlr.runtime3_2_0.tree;

/** Ref to ID or expr but no tokens in ID stream or subtrees in expr stream */
public class RewriteEmptyStreamException extends RewriteCardinalityException {

    public RewriteEmptyStreamException(String elementDescription) {
        super(elementDescription);
    }
}
