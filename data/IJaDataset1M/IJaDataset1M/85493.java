package jode.decompiler;

import jode.GlobalOptions;
import jode.bytecode.SearchPath;
import jode.bytecode.ClassInfo;
import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;

/**
 * This interface is used by jode to tell about its progress.  You
 * supply an instance of this interface to the 
 * {@link Decompiler.decompile} method.<br>
 * 
 * @author <a href="mailto:jochen@gnu.org">Jochen Hoenicke</a>
 * @version 1.0 */
public interface ProgressListener {

    /**
    * Gets called when jode makes some progress.
    * @param progress A number between 0.0 and 1.0
    * @param detail   
    *   The name of the currently decompiled method or class.
    */
    public void updateProgress(double progress, String detail);
}
