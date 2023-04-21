package org.moltools.lib.seq.io.impl;

import java.io.PrintWriter;
import org.apache.commons.collections.Transformer;
import org.moltools.lib.Renameable;
import org.moltools.lib.Named;
import org.moltools.lib.seq.Sequence;
import org.moltools.lib.seq.io.SequenceOutputFormatter;

public class BlastDBFastaOutputFormatter implements SequenceOutputFormatter {

    int width = 60;

    public void printFormattedSequence(PrintWriter ps, Sequence s, Transformer outCon) {
        StringBuffer defline = new StringBuffer(">");
        defline.append(s.getID());
        if (s instanceof Renameable) defline.append(" " + ((Named) s).getName());
        ps.println(defline);
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < defline.length(); i++) {
            char c = defline.charAt(i);
            int code = defline.charAt(i);
            if (c == '\'') bf.append("[prime]"); else if (code > 127) bf.append("[?]"); else bf.append(c);
        }
        String sequence = s.seqString();
        if (outCon != null) sequence = (String) outCon.transform(sequence);
        ps.println(bf);
        for (int i = 0, length = sequence.length(); i < length; i += width) {
            ps.println(sequence.substring(i, Math.min(i + width, length)));
        }
    }
}
