package sratworld.base.stats;

import java.io.*;

/** A loader that loads all level stats from a formatted text stream.

    Author: Henrik B�rbak Christensen 
*/
public interface StatLoader {

    /** Read and interpret the stream from a Reader.
   * @param inputReader the text input stream
   * @return either "Stat loaded" if the load was successful,
   * or a description of the incorrect line including linenumber.
  */
    public String load(BufferedReader inputStream);
}
