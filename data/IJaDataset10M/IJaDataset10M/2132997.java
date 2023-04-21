package com.stakface.ocmd.actions.plugins;

import java.awt.event.ActionEvent;
import java.io.*;
import com.stakface.ocmd.*;
import com.stakface.ocmd.actions.FileAction;
import com.stakface.ocmd.gui.*;
import com.stakface.ocmd.util.filefilters.*;

public class IncrementSelectionByExtension extends FileAction {

    public IncrementSelectionByExtension(OCmd ocmd) {
        super(ocmd);
    }

    public void actionPerformed(ActionEvent ae) {
        FilePane fp = _ocmd.getActiveFilePane();
        if (fp.supportsMultiSelect()) {
            File activeFile = fp.getActiveFile();
            if (activeFile != null) {
                String filename = activeFile.getName();
                int index = filename.lastIndexOf('.');
                fp.setSelectionFilter(new FileExtensionFilter(fp.getSelectedFiles(), true, index < 0 ? "" : filename.substring(index + 1)));
            }
        }
    }
}
