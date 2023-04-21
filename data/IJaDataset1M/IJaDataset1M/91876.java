package org.tonguetied.datatransfer.exporting;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.jaxp13.Validator;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;
import org.w3c.dom.Document;

/**
 * Test class for testing the output from the resource template.
 * 
 * @author bsion
 *
 */
public class ResourcesTemplateTest extends TemplateTester {

    private Country usa;

    private Country china;

    private Language english;

    private Language simplifiedChinese;

    private Language traditionalChinese;

    private Bundle bundle;

    private Keyword keyword1;

    private Keyword keyword2;

    private Keyword keyword3;

    private Translation translation1_1;

    private Translation translation1_2;

    private Translation translation2_1;

    private Translation translation2_2;

    private Translation translation2_3;

    private Translation translation3_1;

    private Translation translation3_2;

    private Translation translation3_3;

    private static final String FILE_EXTENSION = "resx";

    /**
     * Create a new instance of PropertiesTemplateTest.
     */
    public ResourcesTemplateTest() {
        super("resx.ftl", FILE_EXTENSION);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        usa = new Country();
        usa.setCode(CountryCode.US);
        usa.setName("United States");
        china = new Country();
        china.setCode(CountryCode.CN);
        china.setName("China");
        english = new Language();
        english.setCode(LanguageCode.en);
        english.setName("English");
        simplifiedChinese = new Language();
        simplifiedChinese.setCode(LanguageCode.zh);
        simplifiedChinese.setName("Chinese");
        traditionalChinese = new Language();
        traditionalChinese.setCode(LanguageCode.zht);
        traditionalChinese.setName("Traditional Chinese");
        bundle = new Bundle();
        bundle.setName("testBundle");
        bundle.setResourceName("ResourceTemplateTest");
        bundle.setDescription("this is a test bundle");
        keyword1 = new Keyword();
        keyword1.setKeyword("akeyword");
        keyword1.setContext("keyword 1");
        translation1_1 = new Translation();
        translation1_1.setBundle(bundle);
        translation1_1.setCountry(china);
        translation1_1.setLanguage(english);
        translation1_1.setKeyword(keyword1);
        translation1_1.setValue("translation 1_1");
        translation1_1.setState(TranslationState.VERIFIED);
        keyword1.addTranslation(translation1_1);
        translation1_2 = new Translation();
        translation1_2.setBundle(bundle);
        translation1_2.setCountry(usa);
        translation1_2.setLanguage(english);
        translation1_2.setKeyword(keyword1);
        translation1_2.setValue("split line \\\ncontinue on second");
        translation1_2.setState(TranslationState.VERIFIED);
        keyword1.addTranslation(translation1_2);
        keyword2 = new Keyword();
        keyword2.setKeyword("anotherkeyword");
        keyword2.setContext("Keyword 2");
        translation2_1 = new Translation();
        translation2_1.setBundle(bundle);
        translation2_1.setCountry(china);
        translation2_1.setLanguage(english);
        translation2_1.setKeyword(keyword2);
        translation2_1.setValue("unix linebreak \\\nanother line");
        translation2_1.setState(TranslationState.VERIFIED);
        keyword2.addTranslation(translation2_1);
        translation2_2 = new Translation();
        translation2_2.setBundle(bundle);
        translation2_2.setCountry(china);
        translation2_2.setLanguage(simplifiedChinese);
        translation2_2.setValue("xml < ! & \" '>");
        translation2_2.setKeyword(keyword2);
        translation2_2.setState(TranslationState.VERIFIED);
        keyword2.addTranslation(translation2_2);
        translation2_3 = new Translation();
        translation2_3.setBundle(bundle);
        translation2_3.setCountry(usa);
        translation2_3.setLanguage(english);
        translation2_3.setKeyword(keyword2);
        translation2_3.setValue(null);
        translation2_3.setState(TranslationState.VERIFIED);
        keyword2.addTranslation(translation2_3);
        keyword3 = new Keyword();
        keyword3.setKeyword("KeywordThree");
        translation3_1 = new Translation();
        translation3_1.setBundle(bundle);
        translation3_1.setCountry(china);
        translation3_1.setLanguage(simplifiedChinese);
        translation3_1.setState(TranslationState.VERIFIED);
        translation3_1.setKeyword(keyword3);
        translation3_1.setValue("translation 3_1");
        keyword3.addTranslation(translation3_1);
        translation3_2 = new Translation();
        translation3_2.setBundle(bundle);
        translation3_2.setCountry(usa);
        translation3_2.setLanguage(english);
        translation3_2.setKeyword(keyword3);
        translation3_2.setState(TranslationState.VERIFIED);
        translation3_2.setValue("");
        keyword3.addTranslation(translation3_2);
        translation3_3 = new Translation();
        translation3_3.setBundle(bundle);
        translation3_3.setCountry(china);
        translation3_3.setLanguage(english);
        translation3_3.setKeyword(keyword3);
        translation3_3.setValue("blah blah");
        translation3_3.setState(TranslationState.VERIFIED);
        keyword3.addTranslation(translation3_3);
        List<Translation> translations = new ArrayList<Translation>();
        translations.add(translation1_1);
        translations.add(translation1_2);
        translations.add(translation2_1);
        translations.add(translation2_2);
        translations.add(translation2_3);
        translations.add(translation3_1);
        translations.add(translation3_2);
        translations.add(translation3_3);
        super.setTranslations(translations);
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        super.setTranslations(null);
    }

    @Override
    public void runAssertions() throws Exception {
        final Collection<File> files = FileUtils.listFiles(getOutputDir().getAbsoluteFile(), getOutputExtensions(), false);
        assertEquals(3, files.size());
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Validator validator;
        StreamSource source;
        Document document;
        for (File file : files) {
            validator = new Validator(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            source = new StreamSource(file);
            document = builder.parse(file);
            if ("ResourceTemplateTest.en-US.resx".equals(file.getName())) {
                assertXpathExists("/root/data[@name=\"akeyword\"]", document);
                assertXpathEvaluatesTo("split line \\\ncontinue on second", "/root/data[@name=\"akeyword\"]/value", document);
                assertXpathExists("/root/data[@name=\"anotherkeyword\"]", document);
                assertXpathEvaluatesTo("", "/root/data[@name=\"anotherkeyword\"]/value", document);
                assertXpathExists("/root/data[@name=\"KeywordThree\"]", document);
                assertXpathEvaluatesTo("", "/root/data[@name=\"KeywordThree\"]/value", document);
            } else if ("ResourceTemplateTest.en-CN.resx".equals(file.getName())) {
                assertXpathExists("/root/data[@name=\"akeyword\"]", document);
                assertXpathEvaluatesTo("translation 1_1", "/root/data[@name=\"akeyword\"]/value", document);
                assertXpathExists("/root/data[@name=\"anotherkeyword\"]", document);
                assertXpathEvaluatesTo("unix linebreak \\\nanother line", "/root/data[@name=\"anotherkeyword\"]/value", document);
                assertXpathExists("/root/data[@name=\"KeywordThree\"]", document);
                assertXpathEvaluatesTo("blah blah", "/root/data[@name=\"KeywordThree\"]/value", document);
            } else if ("ResourceTemplateTest.zh-CHS.resx".equals(file.getName())) {
                assertXpathExists("/root/data[@name=\"anotherkeyword\"]", document);
                assertXpathEvaluatesTo("xml < ! & \" '>", "/root/data[@name=\"anotherkeyword\"]/value", document);
                assertXpathExists("/root/data[@name=\"KeywordThree\"]", document);
                assertXpathEvaluatesTo("translation 3_1", "/root/data[@name=\"KeywordThree\"]/value", document);
            } else {
                fail("Unexpected file: " + file.getPath());
            }
        }
    }
}
