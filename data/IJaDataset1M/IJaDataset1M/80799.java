package javax.i18n4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.i18n4j.Translator;
import org.junit.Test;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TranslatorTest extends TestCase {

    private static final Translator translator = Translator.getTranslator(TranslatorTest.class);

    @Test
    public void testSingleton() {
        Assert.assertNotNull(translator);
        Assert.assertSame(translator, Translator.getTranslator(TranslatorTest.class));
    }

    @Test
    public void testUnknownTranslation() {
        Assert.assertNotNull(translator);
        String origin = "This text was never translated!";
        String translation = translator.i18n(origin);
        Assert.assertEquals(origin, translation);
    }

    @Test
    public void testTranslation() {
        Translator.setDefault(new Locale("de", "DE"));
        Assert.assertNotNull(translator);
        String origin = "This text will be translated!";
        String translation = "Dieser Text wird uebersetzt werden!";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
    }

    @Test
    public void testOneParameterInTranslation() {
        Translator.setDefault(new Locale("de", "DE"));
        Assert.assertNotNull(translator);
        String origin = "This text will be translated in {0,number,integer} lines!";
        String translation = "Dieser Text wird in {0,number,integer} Zeilen uebersetzt werden!";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
        Assert.assertEquals("Dieser Text wird in 3 Zeilen uebersetzt werden!", translator.i18n(origin, 3));
    }

    @Test
    public void testMultipleParametersInTranslation() {
        Translator.setDefault(new Locale("de", "DE"));
        Assert.assertNotNull(translator);
        String origin = "Switch {0,number,integer} and {1,number,integer}!";
        String translation = "Vertausche {1,number,integer} und {0,number,integer}!";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
        Assert.assertEquals("Vertausche 2 und 1!", translator.i18n(origin, 1, 2));
    }

    @Test
    public void testChoiceInTranslation() {
        Translator.setDefault(new Locale("de", "DE"));
        Assert.assertNotNull(translator);
        String origin = "Some files were opened: {0,choice,0#no files|1#one file|1<{0,number,integer} files}";
        String translation = "Es wurden Dateien geoeffnet: {0,choice,0#keine Datei|1#eine Datei|1<{0,number,integer} Dateien}";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
        Assert.assertEquals("Es wurden Dateien geoeffnet: keine Datei", translator.i18n(origin, 0));
        Assert.assertEquals("Es wurden Dateien geoeffnet: eine Datei", translator.i18n(origin, 1));
        Assert.assertEquals("Es wurden Dateien geoeffnet: 2 Dateien", translator.i18n(origin, 2));
        Assert.assertEquals("Es wurden Dateien geoeffnet: 3 Dateien", translator.i18n(origin, 3));
    }

    @Test
    public void testDateLocalizationInTranslation() {
        Translator.setDefault(new Locale("de", "DE"));
        Assert.assertNotNull(translator);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2009-07-07 12:00:00");
        } catch (ParseException e) {
            Assert.fail();
        }
        String origin = "This test was create on {0,date}.";
        String translation = "Dieser Test wurde am {0,date} erstellt.";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
        Locale.setDefault(new Locale("de", "DE"));
        Assert.assertEquals("Dieser Test wurde am 07.07.2009 erstellt.", translator.i18n(origin, date));
        origin = "This test was create on {0,date,long}.";
        translation = "Dieser Test wurde am {0,date,long} erstellt.";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
        Locale.setDefault(new Locale("de", "DE"));
        Assert.assertEquals("Dieser Test wurde am 7. Juli 2009 erstellt.", translator.i18n(origin, date));
        origin = "This test was create on {0,date,short}.";
        translation = "Dieser Test wurde am {0,date,short} erstellt.";
        translator.setTranslation(origin, "de", translation);
        Assert.assertEquals(translation, translator.i18n(origin));
        Locale.setDefault(new Locale("de", "DE"));
        Assert.assertEquals("Dieser Test wurde am 07.07.09 erstellt.", translator.i18n(origin, date));
    }

    @Test
    public void testMultipleTranslationWithoutArgument() {
        Translator.setDefault(new Locale("de", "DE"));
        Translator.setAdditionalLocales(new Locale("vi", "VN"), new Locale("en", "US"));
        translator.setTranslation("English", "de", "Deutsch");
        translator.setTranslation("English", "vi", "Tieng Viet");
        translator.setTranslation("English", "en", "English");
        Assert.assertEquals("Deutsch (Tieng Viet) (English)", translator.i18n("English"));
    }

    @Test
    public void testMultipleTranslationWithArgument() {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2009-07-07 12:00:00");
        } catch (ParseException e) {
            Assert.fail();
        }
        Translator.setDefault(new Locale("de", "DE"));
        Translator.setAdditionalLocales(new Locale("vi", "VN"), new Locale("en", "US"));
        translator.setTranslation("English: {0,date,short}", "de", "Deutsch: {0,date,short}");
        translator.setTranslation("English: {0,date,short}", "vi", "Tieng Viet: {0,date,short}");
        translator.setTranslation("English: {0,date,short}", "en", "English: {0,date,short}");
        Assert.assertEquals("Deutsch: 07.07.09 " + "(Tieng Viet: 07/07/2009) " + "(English: 7/7/09)", translator.i18n("English: {0,date,short}", date));
    }

    @Test
    public void testMultipleLineTranslation() {
        translator.setTranslation("Line1\nLine2", "de", "Zeile1\nZeile2");
        Assert.assertEquals("Zeile1\nZeile2", translator.translate("Line1\nLine2", "de"));
    }
}
