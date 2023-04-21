package com.jeantessier.diff;

/**
 *  Common visitable interface for differences between codebases.
 *
 *  @see Visitor
 */
public interface Differences {

    public String getName();

    public void accept(Visitor visitor);
}
