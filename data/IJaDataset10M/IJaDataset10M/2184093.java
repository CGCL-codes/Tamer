package org.progeeks.mparse;

import java.io.*;
import java.util.*;

/**
 *  A character "reader" that supports a configurable
 *  pushback level and has built in line number and position
 *  tracking.
 *
 *  @version   $Revision: 4123 $
 *  @author    Paul Speed
 */
public interface CharStream {

    /**
     *  Reads one character from the stream.
     */
    public int read() throws IOException;

    /**
     *  Returns what will be the next character in the
     *  stream if read() is called.
     */
    public int peek() throws IOException;

    /**
     *  Pushes the specified character back onto the
     *  stream such that it will be the next character
     *  returned by read().
     */
    public void unread(int val) throws IOException;

    public void close() throws IOException;

    public long getPosition();

    public long getLine();

    public long getColumn();
}
