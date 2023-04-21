package is.iclt.icenlp.runner;

import is.iclt.icenlp.core.apertium.ApertiumEntry;
import is.iclt.icenlp.core.apertium.ApertiumSegmentizer;
import is.iclt.icenlp.core.apertium.IceNLPTokenConverter;
import is.iclt.icenlp.core.apertium.LemmaGuesser;
import is.iclt.icenlp.core.apertium.LexicalUnit;
import is.iclt.icenlp.core.apertium.LtProcParser;
import is.iclt.icenlp.core.lemmald.Lemmald;
import is.iclt.icenlp.core.tokenizer.IceTokenTags;
import is.iclt.icenlp.core.tokenizer.Segmentizer;
import is.iclt.icenlp.core.utils.FileEncoding;
import is.iclt.icenlp.core.utils.IceTag;
import is.iclt.icenlp.core.utils.MappingLexicon;
import is.iclt.icenlp.core.utils.Word;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Runs IceParser producing Apertium output format.
 * 
 * @author Hrafn Loftsson
 */
public class RunIceTaggerApertium extends RunIceTagger {

    private MappingLexicon mappingLexicon;

    private Lemmald lemmald;

    private boolean showSurfaceForm = false;

    public static void main(String args[]) throws Exception {
        RunIceTaggerApertium runner = new RunIceTaggerApertium();
        Date before = runner.initialize(args);
        if (runner.externalAnalysis.equals("icenlp")) {
            runner.lemmald = Lemmald.getInstance();
        }
        runner.mappingLexicon = new MappingLexicon(runner.tagMapFile, false, false, false, "<NOT MAPPED>", true);
        runner.mappingLexicon.setLeave_lexemes_of_length_one_unchanged(true);
        runner.tokenizer.dateHandling(true);
        runner.performTagging();
        runner.finish(before);
    }

    protected void getParameters(String args[]) {
        super.getParameters(args);
        for (int i = 0; i <= args.length - 1; i++) {
            if (args[i].equals("-sf")) showSurfaceForm = true;
        }
    }

    protected void loadParameters(String filename) throws IOException {
        super.loadParameters(filename);
        Properties parameters = new Properties();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
        parameters.load(in);
        String sf = parameters.getProperty("SURFACE_FORM");
        if (sf.equals("yes")) {
            showSurfaceForm = true;
        }
    }

    protected void performTagging() throws IOException {
        if (standardInputOutput) {
            BufferedWriter out = FileEncoding.getWriter(System.out);
            if (externalAnalysis.equals("apertium")) {
                tagTextExternal(out);
            } else {
                tagText(out);
            }
        } else if (fileList == null) {
            BufferedWriter out = FileEncoding.getWriter(outputFile);
            if (externalAnalysis.equals("apertium")) {
                tagTextExternal(out);
            } else {
                tagText(out);
            }
        } else {
            tagAllFiles();
        }
        logger.close();
    }

    protected void tagTextExternal(BufferedWriter outFile) throws IOException {
        ApertiumSegmentizer segmentizer;
        if (inputFile != null) {
            segmentizer = new ApertiumSegmentizer(inputFile);
        } else {
            segmentizer = new ApertiumSegmentizer(System.in);
        }
        LtProcParser lps;
        ArrayList<ApertiumEntry> entries;
        IceNLPTokenConverter converter;
        ArrayList<IceTokenTags> tokens;
        while (segmentizer.hasMoreSentences()) {
            lps = new LtProcParser(segmentizer.getSentance());
            entries = lps.parse();
            converter = new IceNLPTokenConverter(entries, mappingLexicon, iceLex);
            tokens = converter.convert();
            tagger.tagExternalTokens(tokens);
            converter.changeReflexivePronounTags(tokens);
            printResultsExternal(outFile, tokens, entries, mappingLexicon);
            outFile.flush();
            segmentizer.processNextSentence();
        }
        outFile.close();
    }

    protected void showParameters() {
        super.showParameters();
        System.out.println("  -sf (print surface form)");
    }

    protected void printResultsExternal(BufferedWriter outFile, ArrayList<IceTokenTags> tokens, ArrayList<ApertiumEntry> entries, MappingLexicon mappingLexicon) throws IOException {
        String lexeme;
        List<Word> wordList = new LinkedList<Word>();
        LemmaGuesser guesser;
        Word newWord = null;
        if (tokens.size() == 0) {
            return;
        }
        for (IceTokenTags t : tokens) {
            numTokens++;
            boolean unknown = t.isUnknown();
            if (!unknown) {
                if (t.isUnknownExternal() || ((IceTag) t.getFirstTag()).isForeign()) {
                    unknown = true;
                }
            }
            if (unknown) {
                numUnknowns++;
            }
            if (!t.isProperNoun() && Character.isUpperCase(t.lexeme.charAt(0))) {
                lexeme = t.lexeme.toLowerCase();
            } else {
                lexeme = t.lexeme;
            }
            String lemma = t.getFirstTag().getLemma();
            if (lemma == null && !t.isUnknownExternal()) {
            }
            newWord = new Word(t.lexeme, lemma, t.getFirstTagStr(), t.mweCode, t.tokenCode, t.linkedToPreviousWord, unknown);
            newWord.preSpace = t.preSpace;
            newWord.invMWMark = t.getInvMWMark();
            wordList.add(newWord);
        }
        this.mappingLexicon.processWordList(wordList);
        String output = "";
        for (Word word : wordList) {
            if (outputFormat == Segmentizer.tokenPerLine) {
                if (word.preSpace != null) {
                    outFile.write(word.preSpace);
                }
                if (showSurfaceForm) {
                    if (word.isUnknown()) {
                        outFile.write("^" + word.getLexeme() + "/*" + word.getLexeme() + "$");
                    } else {
                        outFile.write("^" + word.getLexeme() + "/" + word.getLemma() + word.getTag());
                        if (word.invMWMark != null) {
                            outFile.write("#" + word.invMWMark);
                        }
                        outFile.write("$");
                    }
                } else {
                    outFile.write("^" + word.getLemma() + word.getTag());
                    if (word.invMWMark != null) {
                        outFile.write("#" + word.invMWMark);
                    }
                    outFile.write("$");
                }
                outFile.newLine();
            } else {
                if (word.preSpace != null) {
                    output = output + word.preSpace;
                }
                if (showSurfaceForm) {
                    if (word.isUnknown()) {
                        output = output + "^" + word.getLexeme() + "/*" + word.getLexeme() + "$";
                    } else {
                        output = output + "^" + word.getLexeme() + "/" + word.getLemma() + word.getTag();
                        if (word.invMWMark != null) {
                            output = output + "#" + word.invMWMark;
                        }
                        output = output + "$";
                    }
                } else {
                    output = output + "^" + word.getLemma() + word.getTag();
                    if (word.invMWMark != null) {
                        output = output + "#" + word.invMWMark;
                    }
                    output = output + "$";
                }
            }
        }
        if (outputFormat != Segmentizer.tokenPerLine) {
            if (output.charAt(0) == ' ') {
                output = output.substring(1, output.length());
            }
            outFile.write(output);
        }
        outFile.newLine();
    }

    protected void printResults(BufferedWriter outFile) throws IOException {
        String lexeme;
        List<Word> wordList = new LinkedList<Word>();
        if (tokenizer.tokens.size() == 0) {
            return;
        }
        for (Object o : tokenizer.tokens) {
            IceTokenTags t = ((IceTokenTags) o);
            numTokens++;
            if (t.isUnknown()) numUnknowns++;
            if (!t.isProperNoun() && Character.isUpperCase(t.lexeme.charAt(0))) {
                lexeme = t.lexeme.toLowerCase();
            } else {
                lexeme = t.lexeme;
            }
            wordList.add(new Word(t.lexeme, this.lemmald.lemmatize(lexeme, t.getFirstTagStr()).getLemma(), t.getFirstTagStr(), t.mweCode, t.tokenCode, t.linkedToPreviousWord));
        }
        this.mappingLexicon.processWordList(wordList);
        String output = "";
        for (Word word : wordList) {
            if (outputFormat == Segmentizer.tokenPerLine) {
                if (showSurfaceForm) outFile.write("^" + word.getLexeme() + "/" + word.getLemma() + word.getTag() + "$"); else outFile.write("^" + word.getLemma() + word.getTag() + "$");
                outFile.newLine();
            } else {
                if (!word.linkedToPreviousWord) output = output + " ";
                if (showSurfaceForm) output = output + "^" + word.getLexeme() + "/" + word.getLemma() + word.getTag() + "$"; else output = output + "^" + word.getLemma() + word.getTag() + "$";
            }
        }
        if (outputFormat != Segmentizer.tokenPerLine) {
            if (output.charAt(0) == ' ') output = output.substring(1, output.length());
            outFile.write(output);
        }
        outFile.newLine();
    }
}
