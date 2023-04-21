package tdomhan.addict.parser.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.ibm.icu.text.Collator;
import tdomhan.addict.dictionary.Dictionary;
import tdomhan.addict.parser.Translation;
import tdomhan.addict.parser.Translation.TranslationDirection;
import tdomhan.addict.parser.WiktionaryParser;
import tdomhan.addict.parser.Translation.Gender;
import tdomhan.addict.parser.WordType;
import static org.junit.Assert.*;

/**
 * Different tests, that assure that the parser is working correctly.
 * It will parse the file unitTest.xml in the data folder. this file contains
 * complete nonsense!
 * @author tobi
 *
 */
public class WiktionaryParsingTestCase {

    /**
	 * all translations found in ./data/unitTest.xml
	 */
    Vector<Translation> translations;

    /**
	 * vector of translations that shall be tested for existence
	 */
    Vector<Translation> translationsToTest = new Vector<Translation>();

    Collator firstCollator = Collator.getInstance(Locale.US);

    Collator secondCollator = Collator.getInstance(new Locale("fi"));

    @Before
    public void setupFixture() {
        try {
            Translation.setFirstLangCollator(firstCollator);
            Translation.setSecondLangCollator(secondCollator);
            Translation.setTranslationDirection(TranslationDirection.FIRST_SECOND_LANGUAGE);
            SAXParserFactory parserFac = SAXParserFactory.newInstance();
            SAXParser parser = parserFac.newSAXParser();
            WiktionaryParser wikparser = new WiktionaryParser("Finnish", "fi");
            parser.parse("./data/unitTest.xml", wikparser);
            translations = wikparser.getTranslations();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        translationsToTest.add(new Translation("fuck", Gender.UNKOWN, "nainti", Gender.UNKOWN, "an act of sexual intercourse", WordType.NOUN));
        translationsToTest.add(new Translation("fuck", Gender.UNKOWN, "nainti2", Gender.UNKOWN, "an act of sexual intercourse", WordType.NOUN));
        translationsToTest.add(new Translation("fuck", Gender.UNKOWN, "naida", Gender.UNKOWN, "to have sexual intercourse-obscene or vulgar", WordType.VERB));
        translationsToTest.add(new Translation("fuck", Gender.UNKOWN, "panna", Gender.UNKOWN, "to have sexual intercourse-obscene or vulgar", WordType.VERB));
        translationsToTest.add(new Translation("fuck", Gender.UNKOWN, "nussia", Gender.UNKOWN, "to have sexual intercourse-obscene or vulgar", WordType.VERB));
        translationsToTest.add(new Translation("orange", Gender.UNKOWN, "appelsiini", Gender.UNKOWN, "fruit", WordType.NOUN));
        translationsToTest.add(new Translation("orange", Gender.UNKOWN, "oranssi", Gender.UNKOWN, "colour", WordType.NOUN));
        translationsToTest.add(new Translation("orange", Gender.UNKOWN, "oranssi", Gender.UNKOWN, "colour", WordType.ADJECTIVE));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "rappuseta", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "rappuset-", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "-kest�v�", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "nauha", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "keng�nnauha", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "amerikkalainen jalkapallo", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "jenkkifutis", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting; colloquial unoffical", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "portaat", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "tappaa", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting; to kill", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "sokea kuin lepakko", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "Illan", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "ilo silm�lle", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting; idiom", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "t�yty�", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting; when the necessity is absolute", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "pys�hdys", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting; short interruption of a journey made with terrain vehicles", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "abcIllan", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "olla t�rke�mpi", Gender.UNKOWN, "this word does neither exist nor make sense just about the formatting", WordType.VERB));
        translationsToTest.add(new Translation("stairway", Gender.UNKOWN, "rappuset", Gender.UNKOWN, "set of steps allowing one to walk up or down comfortably", WordType.NOUN));
        translationsToTest.add(new Translation("verschachtelung", Gender.UNKOWN, "quark", Gender.UNKOWN, "", WordType.NOUN));
        translationsToTest.add(new Translation("verschachtelung", Gender.UNKOWN, "mitsosse", Gender.UNKOWN, "", WordType.VERB));
    }

    @Test
    public void parseSimpleFile() {
        for (Translation translation : translationsToTest) {
            assertTrue(translations.contains(translation));
        }
    }

    public void serializationTest() {
        Dictionary firstdic = new Dictionary(firstCollator, secondCollator);
        firstdic.constructFromTranslations(translations);
        try {
            FileOutputStream fout = new FileOutputStream("./data/en-fi-test.txt.gz");
            GZIPOutputStream zipout = new GZIPOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(zipout);
            firstdic.writeTo(new BufferedWriter(out));
            out.close();
            zipout.close();
            fout.close();
            GZIPInputStream zipin = new GZIPInputStream(new FileInputStream("./data/en-fi-test.txt.gz"));
            InputStreamReader in = new InputStreamReader(zipin);
            Dictionary seconddic = new Dictionary(firstCollator, secondCollator);
            seconddic.readFrom(new BufferedReader(in));
            zipin.close();
            Vector<Translation> secondTranslations = seconddic.getFirstLangTranslations();
            assertTrue(translations.size() == secondTranslations.size());
            assertTrue(secondTranslations.equals(translations));
            for (Translation translation : translationsToTest) {
                assertTrue(secondTranslations.contains(translation));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
