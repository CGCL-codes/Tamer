package org.vardb.converters.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.vardb.sequences.ISequence;
import org.vardb.sequences.dao.CSequence;

public class CMultipleAlignmentParser implements IMultipleAlignmentParser {

    public static CMultipleAlignmentParser getInstance() {
        CMultipleAlignmentParser parser = new CMultipleAlignmentParser();
        return parser;
    }

    public Map<String, String> parse(String str) {
        IMultipleAlignmentParser parser;
        if (str.startsWith("CLUSTAL")) parser = new CClustalMultipleAlignmentParser(); else parser = new CFastaMultipleAlignmentParser();
        return parser.parse(str);
    }

    public List<ISequence> getSequences(String str) {
        Map<String, String> map = parse(str);
        List<ISequence> sequences = new ArrayList<ISequence>();
        for (String name : map.keySet()) {
            String aligned = map.get(name);
            ISequence sequence = new CSequence(name, aligned);
            sequence.setAligned(aligned);
            sequences.add(sequence);
        }
        return sequences;
    }
}
