package com.rooster.utils.fileformat;

import java.io.File;
import org.apache.log4j.Logger;
import com.rooster.action.HomeAction;

/**
 * Command line program to identify the formats of files.
 * Run this program with file or directory names as parameters.
 * Directories will be recursively scanned for files.
 * @author Marco Schmidt
 */
public class idtree {

    static Logger logger = Logger.getLogger(idtree.class.getName());

    private idtree() {
    }

    private static void handleDirectory(File dir) {
        File[] entries = dir.listFiles();
        if (entries == null || entries.length < 1) {
            return;
        }
        for (int i = 0; i < entries.length; i++) {
            File entry = entries[i];
            if (entry.isFile()) {
                handleFile(entry);
            } else if (entry.isDirectory()) {
                handleDirectory(entry);
            }
        }
    }

    private static void handleFile(File file) {
        System.out.print(file.getAbsolutePath());
        System.out.print(" - ");
        FormatDescription desc = FormatIdentification.identify(file);
        if (desc == null) {
            logger.debug("unknown data.");
        } else {
            logger.debug(desc.getShortName());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java idtree FILESDIRS");
            System.err.println("\twhere FILESDIRS are names of files and directories.");
            System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {
            File entry = new File(args[i]);
            if (entry.isFile()) {
                handleFile(entry);
            } else if (entry.isDirectory()) {
                handleDirectory(entry);
            }
        }
    }
}
