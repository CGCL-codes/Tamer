package org.aitools.synonyms.thesaurus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.aitools.synonyms.utils.FileUtils;

public class ThesaurusReader {

    private final Thesaurus thesaurus;

    private final BufferedReader reader;

    public ThesaurusReader(String fileName) throws IOException {
        this.reader = FileUtils.getResource(fileName);
        this.thesaurus = new Thesaurus();
        readFile();
    }

    public final Thesaurus getThesaurus() {
        return thesaurus;
    }

    public static final Thesaurus getThesaurus(String fileName) throws IOException {
        ThesaurusReader thesaurusReader = new ThesaurusReader(fileName);
        return thesaurusReader.getThesaurus();
    }

    private final void readFile() throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            Set<PhraseCntr> phrases = parseLine(line);
            if (phrases.isEmpty()) {
                continue;
            }
            thesaurus.add(phrases);
        }
        reader.close();
    }

    final Set<PhraseCntr> parseLine(String line) {
        if (line == null || line.length() == 0) {
            return Collections.emptySet();
        }
        if (line.startsWith("#")) {
            return Collections.emptySet();
        }
        return parsePhrases(line);
    }

    final Set<PhraseCntr> parsePhrases(String line) {
        Set<PhraseCntr> phrases = new LinkedHashSet<PhraseCntr>();
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreElements()) {
            String rawPhrase = tokenizer.nextToken();
            phrases.add(parsePhrase(rawPhrase));
        }
        return phrases;
    }

    final PhraseCntr parsePhrase(final String rawPhrase) {
        StringBuilder shortPhraseBuilder = new StringBuilder(rawPhrase.length());
        StringTokenizer tokenizer = new StringTokenizer(rawPhrase);
        while (tokenizer.hasMoreElements()) {
            String word = tokenizer.nextToken();
            if (word.indexOf('(') >= 0) {
                while (word.indexOf(')') < 0 && tokenizer.hasMoreElements()) {
                    word = tokenizer.nextToken();
                }
                continue;
            }
            shortPhraseBuilder.append(word);
            shortPhraseBuilder.append(' ');
        }
        if (shortPhraseBuilder.lastIndexOf(" ") >= 0) {
            shortPhraseBuilder.setLength(shortPhraseBuilder.length() - 1);
        }
        return new PhraseCntr(shortPhraseBuilder.toString(), rawPhrase);
    }
}
