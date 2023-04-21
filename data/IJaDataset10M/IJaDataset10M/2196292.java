package com.ctext.tmxupdater.exporter;

import com.ctext.tmxupdater.tmx.TMXFile;
import com.ctext.tmxupdater.tmx.TU;
import com.ctext.tmxupdater.utils.Logger;
import com.ctext.tmxupdater.utils.StringHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * This class exports the internally generated TMX files to physical locations.
 * @author W. Fourie
 */
public class Exporter {

    private File directory;

    private String username;

    private static final String compoundString = "Compound";

    private static final String extension = "tmx";

    private static final String fieldSeperator = ".";

    private static final String conflictExtenstion = "txt";

    /**
     * Initialise the exporter by specifying the output directory and username.
     * @param directory Output directory where the new TMX files will be saved.
     * @param username The name inserted into the filenames to identify where the
     * TMX file came from.
     */
    public Exporter(File directory, String username) {
        this.directory = directory;
        this.username = username;
    }

    /**
     * Exports all the TMX files defined in the list. For every entry in the list,
     * two files are created. An original and a switched source and target TMX file.
     * @param list The list containing the <code>TMXFiles</code> to export.
     * @return The success of the operation
     */
    public boolean export(LinkedList<TMXFile> list) {
        Logger.logger.log(Level.INFO, StringHandler.getString("LM_EXP_START"));
        Logger.logger.log(Level.INFO, StringHandler.getString("LM_EXP_EXPORT").replace("<number>", String.valueOf(list.size() * 2)));
        String dirPath = directory.getPath();
        String dateTime = dateTime();
        for (TMXFile tmxFile : list) {
            if (tmxFile.conflicts.size() > 0) writeConflicts(tmxFile.conflicts, tmxFile.sourceLang, tmxFile.targetLang);
            for (int i = 0; i <= 1; i++) {
                String srcLang;
                String trgLang;
                String srcCode;
                String trgCode;
                switch(i) {
                    case 0:
                        srcLang = tmxFile.sourceLang;
                        trgLang = tmxFile.targetLang;
                        srcCode = tmxFile.srcCode;
                        trgCode = tmxFile.trgCode;
                        break;
                    case 1:
                        srcLang = tmxFile.targetLang;
                        trgLang = tmxFile.sourceLang;
                        srcCode = tmxFile.trgCode;
                        trgCode = tmxFile.srcCode;
                        break;
                    default:
                        srcLang = tmxFile.sourceLang;
                        trgLang = tmxFile.targetLang;
                        srcCode = tmxFile.srcCode;
                        trgCode = tmxFile.trgCode;
                        break;
                }
                String filename = srcLang + fieldSeperator + trgLang + fieldSeperator + username + fieldSeperator + compoundString + fieldSeperator + dateTime + fieldSeperator + extension;
                Logger.logger.log(Level.INFO, StringHandler.getString("LM_EXP_WRITE") + filename);
                File outFile = new File(dirPath + File.separator + filename);
                if (outFile.exists()) {
                    Logger.logger.log(Level.INFO, StringHandler.getString("LM_EXP_EXIST").replace("<filename>", outFile.getName()));
                    return false;
                }
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
                    out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.newLine();
                    out.write("<!DOCTYPE tmx SYSTEM \"tmx11.dtd\">");
                    out.newLine();
                    out.write("<tmx version=\"1.1\">");
                    out.newLine();
                    out.write("  <header");
                    out.newLine();
                    out.write("   creationtool=\"OmegaT\"");
                    out.newLine();
                    out.write("   creationtoolversion=\"2.1.2\"");
                    out.newLine();
                    out.write("   segtype=\"" + tmxFile.segmentation + "\"");
                    out.newLine();
                    out.write("   o-tmf=\"OmegaT TMX\"");
                    out.newLine();
                    out.write("   adminlang=\"EN-US\"");
                    out.newLine();
                    out.write("   srclang=\"" + srcCode + "\"");
                    out.newLine();
                    out.write("   datatype=\"plaintext\"");
                    out.newLine();
                    out.write("  >");
                    out.newLine();
                    out.write("  </header>");
                    out.newLine();
                    out.write("  <body>");
                    out.newLine();
                    for (TU tu : tmxFile.transUnits) {
                        if (i == 1) {
                            String temp = tu.source;
                            tu.source = tu.target;
                            tu.target = temp;
                        }
                        out.write("    <tu>");
                        out.newLine();
                        out.write("      <tuv lang=\"" + srcCode + "\">");
                        out.newLine();
                        out.write("        <seg>" + tu.source + "</seg>");
                        out.newLine();
                        out.write("      </tuv>");
                        out.newLine();
                        out.write("      <tuv lang=\"" + trgCode + "\">");
                        out.newLine();
                        out.write("        <seg>" + tu.target + "</seg>");
                        out.newLine();
                        out.write("      </tuv>");
                        out.newLine();
                        out.write("    </tu>");
                        out.newLine();
                    }
                    out.write("  </body>");
                    out.newLine();
                    out.write("</tmx>");
                    out.newLine();
                    out.flush();
                    out.close();
                } catch (IOException iox) {
                    Logger.logger.log(Level.SEVERE, StringHandler.getString("LM_EXP_FAIL") + outFile, iox);
                }
            }
        }
        return true;
    }

    private void writeConflicts(LinkedList<TU> conflicts, String src, String trg) {
        File outFile = new File(directory.getPath() + File.separator + src + fieldSeperator + trg + fieldSeperator + username + fieldSeperator + "Conflicts" + fieldSeperator + dateTime() + fieldSeperator + conflictExtenstion);
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            for (TU tu : conflicts) {
                out.write(tu.source);
                out.newLine();
                out.write(tu.target);
                out.newLine();
                out.newLine();
                out.flush();
            }
            out.close();
        } catch (IOException iox) {
            Logger.logger.log(Level.WARNING, StringHandler.getString("LM_EXP_FAIL") + outFile, iox);
        }
    }

    private String dateTime() {
        String full = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        String date = full.substring(0, 4) + "-" + full.substring(4, 6) + "-" + full.substring(6, 8);
        return date;
    }
}
