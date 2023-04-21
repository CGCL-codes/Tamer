package com.sleepycat.je.tree;

/**
 * Error to indicate that a bottom level BIN has cursors on it during a
 * delete subtree operation.
 */
public class CursorsExistException extends Exception {

    private static final long serialVersionUID = 1051296202L;

    public static final CursorsExistException CURSORS_EXIST = new CursorsExistException();

    public CursorsExistException() {
    }
}
