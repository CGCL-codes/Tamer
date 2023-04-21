package logic;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import affd.logic.Config;
import affd.logic.ConverterClient;
import affd.logic.ConverterClientDebug;
import affd.logic.Extractor;
import affd.logic.HtmlDocument;
import affd.logic.LogicException;
import affd.logic.NameAnonymizer;
import affd.logic.NameReplacementFI;
import affd.logic.Sentence;
import affd.logic.Token;
import affd.logic.Tokenizer;
import affd.logic.Token.Type;

public class ExtractorTest {

    private String doNotDeleteFileName = null;

    private ArrayList<Sentence> sentences;

    @Before
    public void setUp() {
        String[] args = new String[4];
        args[0] = "Easy_words.html";
        args[1] = "Easy_words_reviewed.html";
        args[2] = "test.txt";
        args[3] = "debug";
        String ARGUMENT_DEBUG = "debug";
        try {
            if (args.length < 2) {
                throw new LogicException("Invalid argument count !");
            }
            final String inputFilename = args[0];
            final String outputFilename = args[1];
            doNotDeleteFileName = null;
            if (args.length > 2 && !args[2].equals("debug")) {
                doNotDeleteFileName = args[2];
            }
            boolean debug = true;
            System.out.println("We are here 1");
            Config config = new Config("config.cfg", "UTF8");
            System.out.println("We are here 2");
            ConverterClient converter = new ConverterClient();
            if (debug) {
                converter = new ConverterClientDebug(8100);
            }
            String htmlFilename = converter.convert(inputFilename, ConverterClient.EXTENSION_HTML);
            Tokenizer tokenizer = new Tokenizer(config.getValue("Punctuations"), config.getValue("PunctuationsEnd"));
            HtmlDocument document = new HtmlDocument(htmlFilename, tokenizer);
            document.read();
            sentences = document.getSentences();
        } catch (Exception e) {
            fail("Error");
        }
    }

    private HashMap<String, ArrayList<Token>> createExpected() {
        HashMap<String, ArrayList<Token>> expected = new HashMap<String, ArrayList<Token>>();
        ArrayList<Token> tokens = null;
        String[] keys = { "testidokumentti", "lauserakenne", "nimi", "Matti", "Virtanen", "lisä", "lisätä", "lisää", "matti", "virtanen", "Haagar" };
        for (int i = 0; i < keys.length; i++) {
            tokens = new ArrayList<Token>();
            switch(i) {
                case 0:
                    tokens.add(new Token("Testidokumentti", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 1:
                    tokens.add(new Token("Lauserakenteiden", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 2:
                    tokens.add(new Token("Nimien", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 3:
                    tokens.add(new Token("Matti", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 4:
                    tokens.add(new Token("Virtanen", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 5:
                    tokens.add(new Token("Lisää", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 6:
                    tokens.add(new Token("Lisää", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 7:
                    tokens.add(new Token("Lisää", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 8:
                    tokens.add(new Token("Matti", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 9:
                    tokens.add(new Token("Virtanen", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
                case 10:
                    tokens.add(new Token("Haagar", Type.WORD));
                    tokens.add(new Token("Haagaria", Type.WORD));
                    tokens.add(new Token("Haagarille", Type.WORD));
                    expected.put(keys[i], tokens);
                    break;
            }
        }
        return expected;
    }

    @Test
    public void testExtractPossibleNames() throws Exception {
        System.out.println("We are here 0");
        setUp();
        Extractor extractor = new Extractor();
        HashMap<String, ArrayList<Token>> actual = extractor.extractPossibleNames(sentences);
        HashMap<String, ArrayList<Token>> expected = createExpected();
        System.out.println("HashMap actual: " + actual);
        System.out.println("HashMap expected: " + expected);
        assertEquals(expected.toString(), actual.toString());
    }
}
