package net.sf.bluex.parser;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import net.sf.bluex.controller.FileModule;
import net.sf.bluex.plugin.PluginMetaData;

/**
 *
 * @author Blue
 */
public class PluginSaving {

    public static boolean saveToDB(Vector<PluginMetaData> vectPMD) {
        PrintWriter pw = null;
        PluginSavingAlgo psa = null;
        boolean saved = false;
        try {
            File file = new File(FileModule.PLUGINS_DB);
            pw = new PrintWriter(file);
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!--");
            pw.println("Document    : " + file.getName());
            pw.println("Last Updated: " + new Date());
            pw.println("Author      : BlueX- Plugin API");
            pw.println("-->");
            pw.println("<BlueX>");
            psa = new PluginSavingAlgo(vectPMD, pw);
            saved = psa.save();
            pw.println("</BlueX>");
        } catch (Exception e) {
        } finally {
            try {
                pw.close();
            } catch (Exception e) {
            }
        }
        return saved;
    }

    public static boolean savePlugin(PluginMetaData pmd, File file) {
        PrintWriter pw = null;
        PluginSavingAlgo psa = null;
        boolean saved = false;
        try {
            pw = new PrintWriter(file);
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!--");
            pw.println("Document   : " + file.getName());
            pw.println("Created On : " + new Date());
            pw.println("Author     : BlueX- Plugin API");
            pw.println("-->");
            psa = new PluginSavingAlgo(pw);
            psa.addPlugin(pmd);
            saved = psa.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (saved) pw.close();
            } catch (Exception e) {
            }
        }
        return saved;
    }
}
