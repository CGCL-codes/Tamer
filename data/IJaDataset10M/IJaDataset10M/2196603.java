package net.sourceforge.jorgen.generator;

import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class JspIdGeneratorRegex {

    private int _id = 1;

    private String filename = null;

    private static final String TAG = "<(.*)>";

    private static final String ENDTAG = "</(.*)>";

    private static final String JSPTAG = "<%(.*)%>";

    private static final String HASID = "<(.*)id=(.*)>";

    private static final String SINGLELINETAG = "<(.*)/>";

    private static final String IGNORELIST = "<jsp:(.*)>|<!--(.*)-->|<(.*):view(.*)>|<html(.*)>|<head(.*)>|<meta(.*)>|<link(.*)>|<body(.*)>" + "|<(.*):loadbundle(.*)>|<(.*):facet(.*)>|<(.*):verbatim(.*)>" + "|<(.*):saveState(.*)>|<(.*):validate(.*)>|<script(.*)>" + "|<c:(.*)>|<fmt:(.*)>|<(.*):convert(.*)>|<title(.*)>";

    private static final String IDGEN_ID = "<(.*)id=\"idgen_(.*)>";

    private static final String VERBATIMSTART = "<f:verbatim(.*)>(.*)";

    private static final String VERBATIMEND = "(.*)</f:verbatim(.*)>";

    private void setFilename(File file) {
        String name = file.getName();
        int pos = name.indexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        filename = name;
    }

    public void removeIds(File file) throws Exception {
        setFilename(file);
        File out = new File(file.getParentFile(), file.getName() + ".out");
        BufferedWriter bw = new BufferedWriter(new FileWriter(out));
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
            String trimline = line.trim().toLowerCase();
            if (trimline.matches(TAG)) {
                if (trimline.matches(IDGEN_ID)) {
                    line = line.replaceFirst(" id=\"idgen_(.*)\"", "");
                }
            }
            bw.write(line);
            bw.newLine();
        }
        br.close();
        bw.flush();
        bw.close();
        file.delete();
        out.renameTo(file);
    }

    public void process(File file) throws Exception {
        setFilename(file);
        File out = new File(file.getParentFile(), file.getName() + ".out");
        BufferedWriter bw = new BufferedWriter(new FileWriter(out));
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        boolean skip = false;
        while ((line = br.readLine()) != null) {
            String trimline = line.trim().toLowerCase();
            if (trimline.matches(TAG)) {
                if (trimline.matches(VERBATIMSTART) && trimline.matches(VERBATIMEND)) {
                    bw.write(line);
                    bw.newLine();
                    continue;
                } else if (trimline.matches(VERBATIMEND)) {
                    bw.write(line);
                    bw.newLine();
                    skip = false;
                    continue;
                } else if (trimline.matches(VERBATIMSTART)) {
                    bw.write(line);
                    bw.newLine();
                    skip = true;
                    continue;
                }
                if (skip || trimline.matches(ENDTAG) || trimline.matches(JSPTAG) || trimline.matches(IGNORELIST)) {
                    bw.write(line);
                    bw.newLine();
                    continue;
                }
                if (!trimline.matches(HASID)) {
                    if (trimline.matches(SINGLELINETAG)) {
                        line = line.replaceFirst("/>", " id=\"" + generateId() + "\"/>");
                    } else {
                        line = line.replaceFirst(">", " id=\"" + generateId() + "\">");
                    }
                }
            }
            bw.write(line);
            bw.newLine();
        }
        br.close();
        bw.flush();
        bw.close();
        file.delete();
        out.renameTo(file);
    }

    private String generateId() {
        return "idgen_" + filename + "_" + _id++;
    }
}
