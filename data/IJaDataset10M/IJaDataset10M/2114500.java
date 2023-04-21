package net.sourceforge.gosp.dictionary.OLD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import net.sourceforge.gosp.dictionary.util.PartOfSpeechMap;
import au.com.bytecode.opencsv.CSVReader;

public class CDMEParser {

    protected CSVReader reader;

    protected String[] next;

    protected PartOfSpeechMap posMap;

    public CDMEParser(File file) throws IOException {
        this(file, new PartOfSpeechMap());
    }

    public CDMEParser(File file, PartOfSpeechMap posMap) throws IOException {
        reader = new CSVReader(new BufferedReader(new FileReader(file)));
        next = reader.readNext();
        this.posMap = posMap;
    }

    public void close() throws IOException {
        reader.close();
    }

    public List<CDMEEntry> getAllEntries() throws IOException {
        LinkedList<CDMEEntry> list = new LinkedList<CDMEEntry>();
        while (hasNext()) list.add(next());
        return list;
    }

    public boolean hasNext() {
        return next != null;
    }

    public CDMEEntry next() throws IOException {
        String[] line = next;
        next = reader.readNext();
        return parse(line);
    }

    protected CDMEEntry parse(String[] line) {
        CDMEEntry e = new CDMEEntry();
        e.setHierowordLayout(line[2].replaceAll(" ", "").trim());
        e.setTranscription(line[3].replaceAll(" ", "").trim());
        e.setTransliteration(line[4].replaceAll("\\s+", " ").trim());
        e.setDefinition(line[5].replaceAll("\\s+", " ").trim());
        if (line.length > 6) parseNotes(e, line[6]);
        return e;
    }

    private static final Pattern NOT_FAULKNER_PATTERN = Pattern.compile("(not\\s+)?faulkner", Pattern.CASE_INSENSITIVE);

    private void parseNotes(CDMEEntry e, String string) {
        string = string.replaceAll("\\[|\\]|\\(|\\)|\\\"", "");
        string = NOT_FAULKNER_PATTERN.matcher(string).replaceAll("").replaceAll("^ *- *", "").replaceAll(" *- *$", "");
        string = string.replaceAll(" +", " ").trim();
        if (string.equals("")) return;
        PartOfSpeechMap.Entry pos = posMap.get(string);
        e.setNotes(pos.notes);
        e.setPartsOfSpeach(pos.partsOfSpeech);
    }
}
