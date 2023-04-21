package com.mangobop.consequence;

/**
 * @author Stefan Meyer
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface ConsequenceEvent {

    public static final int ASYNC_LOADING = 0;

    public static final int LOADED = 1;

    public static final int LOADING_FAILED = 2;

    public ConsequenceEvent getCause();

    public Exception getException();

    public Consequence getConsequence();

    public String getStackTrace();

    public int getStatus();
}
