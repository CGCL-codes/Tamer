package com.chatserver.io;

import java.io.*;

/**
 *
 * @author crackoder
 */
public class Writer {

    public static void writeFile(OutFormat obj) {
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream("Server.dat"));
            outStream.writeObject(obj);
            outStream.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                outStream.close();
            } catch (IOException ex) {
            }
        }
    }
}
