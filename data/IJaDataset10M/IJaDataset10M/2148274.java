package org.jfor.jfor.rtflib.rtfdoc;

import java.io.Writer;
import java.io.*;
import java.util.*;
import java.io.IOException;

public class RtfJforCmd extends RtfContainer {

    private final String PARA_KEEP_ON = "para-keep:on";

    private final String PARA_KEEP_OFF = "para-keep:off";

    private final RtfAttributes m_attrib;

    private ParagraphKeeptogetherContext m_paragraphKeeptogetherContext;

    RtfJforCmd(RtfContainer parent, Writer w, RtfAttributes attrs) throws IOException {
        super((RtfContainer) parent, w);
        m_attrib = attrs;
        m_paragraphKeeptogetherContext = ParagraphKeeptogetherContext.getInstance();
    }

    public boolean isEmpty() {
        return true;
    }

    public void process() {
        for (Iterator it = m_attrib.nameIterator(); it.hasNext(); ) {
            final String cmd = (String) it.next();
            if (cmd.equals(PARA_KEEP_ON)) {
                m_paragraphKeeptogetherContext.KeepTogetherOpen();
            } else if (cmd.equals(PARA_KEEP_OFF)) {
                m_paragraphKeeptogetherContext.KeepTogetherClose();
            } else {
                this.getRtfFile().getLog().logInfo("JFOR-CMD ignored, command not recognised:" + cmd);
            }
        }
    }
}
