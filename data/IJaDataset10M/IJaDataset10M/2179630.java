package org.jeditor;

import java.io.IOException;
import java.net.URL;
import java.util.PropertyResourceBundle;

/** Main class, only used to give some informations about the version of the 
 * library on the command line.
 *  @since 0.1
 */
public class Main {

    public static void main(String[] args) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("org/jeditor/resources/jeditor.properties");
        try {
            PropertyResourceBundle prb = new PropertyResourceBundle(url.openStream());
            String version = prb.getString("version");
            String date = prb.getString("date");
            System.out.println("jEditor version " + version + " build on " + date);
            System.out.println("Distributed under MIT license");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
