package net.smdp.java.lib;

import java.io.*;

/**
 * @author Felix Kronlage <fkr@grummel.net>
 * @version 0.01
 */
public class SmdpAudioFile extends File {

    /**
     * creates new SmdpFile
     * @param String containing the path to the file
     * @param String containing the filename
     */
    public SmdpAudioFile(String pathToFile, String filename) {
        super(pathToFile, filename);
    }
}
