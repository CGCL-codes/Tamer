package org.es.uma.XMLEditor.specific;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class XmlFilter extends FileFilter {

    fileChooserUtils Utils = new fileChooserUtils();

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.xml) || extension.equals(Utils.dtd) || extension.equals(Utils.xsd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getDescription() {
        return "XML files (*.xml,*.dtd,*.xsd)";
    }
}
