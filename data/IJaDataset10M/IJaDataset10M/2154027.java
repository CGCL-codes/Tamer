package model_test;

import java.io.IOException;
import javax.xml.bind.annotation.DomHandler;
import model.Vokabel;
import model.Vokabeltrainer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;
import xml.Dom4JXmlWriter;
import xml.DomHolder;

public class VokabeltrainerTest {

    Vokabeltrainer vt;

    @Before
    public void VokabetrainerTest() throws DocumentException, IOException {
        vt = new Vokabeltrainer();
    }

    @Test(expected = NullPointerException.class)
    public void addVokabelNull() throws IOException {
        vt.addVokabelOrUpdateVokable(null);
    }

    @Test
    public void addVokabel() throws IOException {
        vt.addVokabelOrUpdateVokable(new Vokabel("laufen", "run", 5, 5));
        vt.addVokabelOrUpdateVokable(new Vokabel("fliegen", "fly", 5, 5));
        vt.addVokabelOrUpdateVokable(new Vokabel("kriechen", "crow", 5, 5));
        vt.addVokabelOrUpdateVokable(new Vokabel("schreien", "scream", 5, 5));
        vt.addVokabelOrUpdateVokable(new Vokabel("werden", "become", 5, 5));
    }

    @Test
    public void getQuestionTest() {
        for (int i = 0; i < 50; i++) System.out.println(vt.getQuestion('g').getGerman() + "------------ " + (i + 1));
        System.out.println();
        System.out.println();
        for (int i = 0; i < 50; i++) System.out.println(vt.getQuestion('e').getEnglish() + "------------ " + (i + 1));
    }

    @Test
    public void prepareXmlForNewTest() throws IOException {
        Dom4JXmlWriter writer = new Dom4JXmlWriter();
        writer.writeXml("src-test-resources/vokabeltrainerTestResult.xml");
        DomHolder.getInstance().getDom().getRootElement().clearContent();
        writer.writeXml("src-test-resources/vokabeltrainerTest.xml");
    }
}
