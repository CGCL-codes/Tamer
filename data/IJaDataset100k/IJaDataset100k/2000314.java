package net.sf.jaybox.iso;

import java.io.*;
import java.awt.*;
import net.sf.jaybox.common.*;

public class XIsoExaminer extends StatReader {

    private int current_depth;

    private String name;

    private Image logo;

    public XIsoExaminer(String fallbackname) {
        current_depth = 0;
        name = StringUtilities.extractFilename(fallbackname);
    }

    public void onEnter(String path, String dir) {
        super.onEnter(path, dir);
        current_depth++;
    }

    public void onLeave(String path, String dir) {
        super.onLeave(path, dir);
        current_depth--;
    }

    public void onFile(ReadHelper rh, String path, DirHeader dh) throws IOException {
        super.onFile(rh, path, dh);
        if (current_depth == 1 && dh.name.equalsIgnoreCase("default.xbe")) {
            XbeExaminer examiner = new XbeExaminer();
            new FileCopier(rh, dh, examiner);
            name = examiner.getName();
            logo = examiner.getLogo();
        }
    }

    public String getName() {
        return name;
    }

    public Image getLogo() {
        return logo;
    }
}
