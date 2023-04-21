package org.vardb.converters.parsers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.db.HashSequenceDB;
import org.biojava.bio.seq.db.SequenceDB;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.RichSequence.IOTools;
import org.vardb.CVardbException;
import org.vardb.alignment.CMafftHelper;
import org.vardb.converters.ncbi.*;
import org.vardb.sequences.CSequenceType;
import org.vardb.sequences.ISequence;
import org.vardb.sequences.dao.CSequence;
import org.vardb.util.CStringHelper;

public class CSequenceFileParser {

    public static List<ISequence> readFasta(String temp) {
        return readFasta(temp, CSequenceType.AA);
    }

    public static List<ISequence> readFasta(String str, CSequenceType type) {
        try {
            str = str.trim();
            if (str.equals("")) return new ArrayList<ISequence>();
            BufferedReader reader = new BufferedReader(new StringReader(str));
            return readFasta(reader, type);
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    public static List<ISequence> readFasta(BufferedReader reader, CSequenceType type) {
        try {
            SymbolTokenization tokenization = getTokenization(type);
            SequenceIterator iter = IOTools.readFasta(reader, tokenization, getNamespace());
            List<ISequence> sequences = convert(iter);
            return sequences;
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    public static String writeFasta(Map<String, String> sequences) {
        try {
            if (sequences.size() == 0) return "";
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeFasta(out, sequences);
            return out.toString();
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    public static String writeFasta(List<ISequence> sequences) {
        try {
            if (sequences.size() == 0) return "";
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (ISequence sequence : sequences) {
                if (sequence.getTranslation() != null) map.put(sequence.getAccession(), sequence.getTranslation());
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeFasta(out, map);
            return out.toString();
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    public static void writeFasta(OutputStream out, Map<String, String> sequences) {
        try {
            String newline = "\n";
            for (Map.Entry<String, String> entry : sequences.entrySet()) {
                String name = entry.getKey();
                String sequence = entry.getValue();
                if (name == null || sequence == null) continue;
                String defline = ">" + name + newline;
                out.write(defline.getBytes());
                out.write(CStringHelper.chunk(sequence, 80, newline).getBytes());
                out.write(newline.getBytes());
            }
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    private static List<ISequence> convert(SequenceIterator iter) {
        try {
            List<ISequence> sequences = new ArrayList<ISequence>();
            while (iter.hasNext()) {
                Sequence seq = iter.nextSequence();
                ISequence sequence = new CSequence(seq.getName(), seq.seqString());
                sequences.add(sequence);
            }
            return sequences;
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    private static SimpleNamespace getNamespace() {
        SimpleNamespace ns = new SimpleNamespace("vardb");
        return ns;
    }

    private static SymbolTokenization getTokenization(CSequenceType type) {
        try {
            SymbolTokenization tokenization;
            if (type == CSequenceType.NT) tokenization = DNATools.getDNA().getTokenization("token"); else tokenization = ProteinTools.getAlphabet().getTokenization("token");
            return tokenization;
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }
}
