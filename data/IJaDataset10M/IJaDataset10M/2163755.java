package edu.rice.cs.drjava.config;

import java.io.*;
import java.util.Date;

/** A Configuration object that can be read and saved from a Stream.
 *  @version $Id: SavableConfiguration.java 5232 2010-04-24 00:14:05Z mgricken $
 */
public class SavableConfiguration extends Configuration {

    /** Creates a new Configuration based on the given OptionMap.
   * @param map an empty OptionMap
   */
    public SavableConfiguration(OptionMap map) {
        super(map);
    }

    /** Creates an OptionMapLoader with the values loaded from the InputStream
   * (and defaults where values weren't specified) and loads them into
   * this Configuration's OptionMap.
   * @param is InputStream containing properties-style keys and values
   */
    public void loadConfiguration(InputStream is) throws IOException {
        new OptionMapLoader(is).loadInto(map);
    }

    /** Used to save the values from this Configuration into the given OutputStream
   * as a Properties file. The elements weren't ordered, so now the properties
   * are written in the same way as the about dialog.
   * Values equal to their defaults are not written to disk.
   */
    public void saveConfiguration(OutputStream os, String header) throws IOException {
        PrintWriter w = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
        Date date = new Date();
        w.println("#" + header);
        w.println("#" + date.toString());
        w.print(toString());
        w.close();
    }
}
