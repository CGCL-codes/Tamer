package net.sf.cplab.av;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;

/**
 * MediaFactory - creator of Media objects
 * 
 * @author babylab
 *
 */
public class MediaFactory {

    /**
	 * Vector that stores the exception caught
	 */
    public static Vector exceptions = new Vector();

    public static JukeBox createJukeBox(File path) throws Exception {
        JukeBox jb = new JukeBox();
        if (path != null) {
            jb.open(path);
        } else {
            jb.open();
        }
        return jb;
    }

    public static Audio createAudio(File file) throws Exception {
        Audio audio = new Audio();
        audio.open(file);
        return audio;
    }

    public static void printExceptions(PrintStream out) {
        Iterator e = exceptions.iterator();
        while (e.hasNext()) {
            ((Exception) (e.next())).printStackTrace(out);
        }
    }

    public static void main(String[] args) {
    }
}
