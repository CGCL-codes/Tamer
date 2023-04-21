package uk.ac.shef.oak.iracema;

import java.util.ArrayList;
import java.util.Set;
import uk.ac.shef.oak.iracema.annotation.Annotation;
import uk.ac.shef.oak.iracema.document.Document;
import uk.ac.shef.oak.iracema.document.aktivemedia.AktiveMediaDocument;
import junit.framework.TestCase;

public class AktiveMediaDocumentTests extends TestCase {

    private String m_documentPath;

    private String m_drdfPath;

    private static final Token[] f_TOKENS = new Token[17];

    private static final Annotation[] f_ANNOTATIONS = new Annotation[3];

    private Document m_aktiveMediaDoc;

    @Override
    protected void setUp() {
        m_documentPath = "./resources/sample-test-documents/iracema-aktivemedia-test.txt";
        m_drdfPath = "./resources/sample-test-documents/iracema-aktivemedia-test.txt.drdf";
        m_aktiveMediaDoc = new AktiveMediaDocument(m_drdfPath, m_documentPath);
        f_TOKENS[0] = new Token(Iracema.f_TOKEN_INDEX.add("Dr."), 0, m_aktiveMediaDoc);
        f_TOKENS[1] = new Token(Iracema.f_TOKEN_INDEX.add("John"), 29, m_aktiveMediaDoc);
        f_TOKENS[2] = new Token(Iracema.f_TOKEN_INDEX.add("Smith"), 34, m_aktiveMediaDoc);
        f_TOKENS[3] = new Token(Iracema.f_TOKEN_INDEX.add("has"), 44, m_aktiveMediaDoc);
        f_TOKENS[4] = new Token(Iracema.f_TOKEN_INDEX.add("come"), 48, m_aktiveMediaDoc);
        f_TOKENS[5] = new Token(Iracema.f_TOKEN_INDEX.add("all"), 53, m_aktiveMediaDoc);
        f_TOKENS[6] = new Token(Iracema.f_TOKEN_INDEX.add("the"), 57, m_aktiveMediaDoc);
        f_TOKENS[7] = new Token(Iracema.f_TOKEN_INDEX.add("way"), 61, m_aktiveMediaDoc);
        f_TOKENS[8] = new Token(Iracema.f_TOKEN_INDEX.add("from"), 65, m_aktiveMediaDoc);
        f_TOKENS[9] = new Token(Iracema.f_TOKEN_INDEX.add("South"), 70, m_aktiveMediaDoc);
        f_TOKENS[10] = new Token(Iracema.f_TOKEN_INDEX.add("Africa"), 76, m_aktiveMediaDoc);
        f_TOKENS[11] = new Token(Iracema.f_TOKEN_INDEX.add("to"), 83, m_aktiveMediaDoc);
        f_TOKENS[12] = new Token(Iracema.f_TOKEN_INDEX.add("study"), 86, m_aktiveMediaDoc);
        f_TOKENS[13] = new Token(Iracema.f_TOKEN_INDEX.add("Information"), 123, m_aktiveMediaDoc);
        f_TOKENS[14] = new Token(Iracema.f_TOKEN_INDEX.add("Extraction"), 135, m_aktiveMediaDoc);
        f_TOKENS[15] = new Token(Iracema.f_TOKEN_INDEX.add("in"), 154, m_aktiveMediaDoc);
        f_TOKENS[16] = new Token(Iracema.f_TOKEN_INDEX.add("England."), 157, m_aktiveMediaDoc);
        f_ANNOTATIONS[0] = new Annotation(29, 39, "Person_Identifier", "John Smith", m_aktiveMediaDoc);
        f_ANNOTATIONS[1] = new Annotation(70, 82, "Locational_Identifier", "South Africa", m_aktiveMediaDoc);
        f_ANNOTATIONS[2] = new Annotation(157, 164, "Locational_Identifier", "England", m_aktiveMediaDoc);
    }

    public void testGetAnnotations() {
        Set<Annotation> annotations = m_aktiveMediaDoc.getAnnotations();
        assertEquals(f_ANNOTATIONS.length, annotations.size());
        ArrayList<Annotation> annotationsList = new ArrayList<Annotation>(annotations);
        for (int i = 0; i < annotationsList.size(); i++) assertTrue(annotationsList.get(i).equals(f_ANNOTATIONS[i]));
    }

    public void testExportText() {
        String textWithEmbeddedTags = m_aktiveMediaDoc.exportWithTags();
        assertTrue(textWithEmbeddedTags.equals("Dr. <a href=\"http://foo.com\"><person_identifier>John Smith</person_identifier></a>" + " has come all the way from" + " <locational_identifier>South Africa</locational_identifier>" + " to study <i><a href=\"http://foo.com/ie\">Information Extraction</a></i> in" + " <locational_identifier>England</locational_identifier>."));
    }

    public void testExportOriginal() {
        String plainText = m_aktiveMediaDoc.exportOriginal();
        assertTrue(plainText.equals("Dr. <a href=\"http://foo.com\">John Smith</a>" + " has come all the way from South Africa to study" + " <i><a href=\"http://foo.com/ie\">Information Extraction</a></i> " + "in England."));
    }
}
