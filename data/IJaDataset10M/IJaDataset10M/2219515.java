package com.alexmcchesney.poster.gui;

/**
 * Exception thrown when we cannot get a post from the post
 * editor because the user has entered invalid values.
 * @author alexmcchesney
 *
 */
public class InvalidPostException extends Exception {

    public InvalidPostException(String sMsg) {
        super(sMsg);
    }
}
