package org.kablink.teaming.lucene.analyzer;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;

public class VibeQueryAnalyzerTest extends TestCase {

    public void testPuntuationAndEmailAddress() throws Exception {
        System.out.println(Charset.defaultCharset());
        Analyzer analyzer = new VibeQueryAnalyzer((Set) null, null, false, false, false);
        String text = "vibe_onprem a.b. test.doc a-b end. 30-12 vibe3_onprem@novell.com 3A";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "vibe", "onprem", "a", "b", "test", "doc", "a", "b", "end", "30", "12", "vibe3", "onprem", "novell", "com", "3a" });
    }

    public void testCases() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer((Set) null, null, false, false, false);
        String text = "Novell nOvell XY&Z NOVELL novell Runs RUNS";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "novell", "novell", "xy", "z", "novell", "novell", "runs", "runs" });
        text = "the The tHe thE THE";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "the", "the", "the", "the", "the" });
    }

    public void testEnglishStemming() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer((Set) null, "English", false, false, false);
        String text = "stemming algorithms Algorithmic breathing breathes runs Runs RUNS ran running";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "stem", "algorithm", "algorithm", "breath", "breath", "run", "run", "run", "ran", "run" });
    }

    public void testStopWords() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer(new File("C:/junk/stop_words.txt"), Charset.defaultCharset().name(), null, false, false, false);
        String text = "the The tHe thE THE";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] {});
        analyzer = new VibeQueryAnalyzer(new File("C:/junk/stop_words.txt"), Charset.defaultCharset().name(), null, false, false, false);
        text = "the The Then tHe thE THE";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "then" });
        analyzer = new VibeQueryAnalyzer(new File("C:/junk/stop_words.txt"), Charset.defaultCharset().name(), null, false, false, false);
        text = "L'�ph�m�ride G�terzug novell �berfuhr by d�nemark Caract�re to br�lante vibe";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "l", "�ph�m�ride", "g�terzug", "novell", "d�nemark", "caract�re", "vibe" });
        analyzer = new VibeQueryAnalyzer(new File("C:/junk/stop_words.utf8.txt"), "UTF-8", null, false, false, false);
        text = "L'�ph�m�ride G�terzug novell �berfuhr by d�nemark Caract�re to br�lante vibe";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "l", "�ph�m�ride", "g�terzug", "novell", "d�nemark", "caract�re", "vibe" });
        analyzer = new VibeQueryAnalyzer(new File("C:/junk/stop_words.utf8.txt"), Charset.defaultCharset().name(), null, false, false, false);
        text = "L'�ph�m�ride G�terzug novell �berfuhr by d�nemark Caract�re to br�lante vibe";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesNotTo(analyzer, text, new String[] { "l", "�ph�m�ride", "g�terzug", "novell", "d�nemark", "caract�re", "vibe" });
        analyzer = new VibeQueryAnalyzer(new File("C:/junk/stop_words.txt"), "UTF-8", null, false, false, false);
        text = "L'�ph�m�ride G�terzug novell �berfuhr by d�nemark Caract�re to br�lante vibe";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesNotTo(analyzer, text, new String[] { "l", "�ph�m�ride", "g�terzug", "novell", "d�nemark", "caract�re", "vibe" });
    }

    public void testFoldingToAscii() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer((Set) null, null, true, false, false);
        String text = "L'�ph�m�ride G�terzug novell �berfuhr by d�nemark Caract�re to br�lante vibe �v�nement";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "l", "�ph�m�ride", "ephemeride", "g�terzug", "guterzug", "novell", "�berfuhr", "uberfuhr", "by", "d�nemark", "danemark", "caract�re", "caractere", "to", "br�lante", "brulante", "vibe", "�v�nement", "evenement" });
    }

    public void testDefaultConfiguration() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET, "English", true, false, false);
        String text = "Kunde Karlsruhe Update update von IBM";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "kund", "karlsruh", "updat", "updat", "von", "ibm" });
    }

    public void testFrenchStemmer() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET, "French", true, false, false);
        String text = "technicit�";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "techniqu", "technicit" });
    }

    public void testGermanStemmer() throws Exception {
        Analyzer analyzer = new VibeQueryAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET, "German", true, false, false);
        String text = "gegessen";
        AnalyzerUtils.displayTokens(analyzer, text);
        System.out.println();
        AnalyzerUtils.assertAnalyzesTo(analyzer, text, new String[] { "gegess" });
    }
}
