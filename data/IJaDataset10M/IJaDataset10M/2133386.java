package saadadb.generationclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import saadadb.database.Database;
import saadadb.exceptions.FatalException;
import saadadb.exceptions.SaadaException;
import saadadb.util.Messenger;
import com.sun.tools.javac.Main;

public class Compile {

    private static String separ = System.getProperty("file.separator");

    public static void compileIt(File sourcefile) throws Exception {
        compileIt(sourcefile, Database.getLogDir());
    }

    /** * @version $Id: Compile.java 118 2012-01-06 14:33:51Z laurent.mistahl $

     * @param sourcefile
     * @param classdir
     * @param log_dir
     * @throws Exception
     */
    public static void compileIt(File sourcefile, String log_dir) throws Exception {
        String classpath = "-classpath";
        String logfile = log_dir + separ + sourcefile.getName() + ".comp.log";
        PrintWriter out = new PrintWriter(new FileWriter(logfile));
        String classname = sourcefile.getName();
        String classdir = sourcefile.getParent();
        if (classdir.length() > 20) if (Messenger.debug_mode) {
            if (classdir.length() > 20) {
                Messenger.printMsg(Messenger.DEBUG, "Compiling: " + classname + " ( -d ..." + classdir.substring(classdir.length() - 20) + ") ");
            } else {
                Messenger.printMsg(Messenger.DEBUG, "Compiling: " + classname + " ( -d " + classdir + ") ");
            }
        }
        int status = Main.compile(new String[] { classpath, System.getProperty("java.class.path"), "-d", classdir, "-target", "1.5", sourcefile.getAbsolutePath() }, out);
        out.close();
        if (status == 0) {
            Messenger.printMsg(Messenger.TRACE, " Compilation of " + classname + ".java met no problems");
        } else {
            BufferedReader lf;
            lf = new BufferedReader(new FileReader(logfile));
            String str;
            while ((str = lf.readLine()) != null) {
                Messenger.printMsg(Messenger.ERROR, str.toString());
            }
            lf.close();
            FatalException.throwNewException(SaadaException.INTERNAL_ERROR, "compilation failed");
        }
    }
}
