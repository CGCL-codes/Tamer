package org.wings.plaf.xhtml.old;

import java.io.IOException;
import java.awt.Color;
import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;

public final class TextAreaCG extends org.wings.plaf.xhtml.TextAreaCG {

    public void writePrefix(Device d, STextArea textArea) throws IOException {
        SFont font = textArea.getFont();
        Color foreground = textArea.getForeground();
        Utils.writeFontPrefix(d, font, foreground);
        super.writePrefix(d, textArea);
    }

    public void writePostfix(Device d, STextArea textArea) throws IOException {
        SFont font = textArea.getFont();
        super.writePostfix(d, textArea);
        Utils.writeFontPostfix(d, font);
    }
}
