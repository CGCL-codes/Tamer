package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;
import lang_features.LanguageFeaturesTestProvider;
import org.junit.Test;
import framework.AbstractPtolemyTest;

public class LanguageFeaturesTest extends AbstractPtolemyTest {

    public LanguageFeaturesTest() {
        super();
    }

    @Test
    public void parseEvent1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/EventDecls");
        String file = "Event1.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseEvent2() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/EventDecls");
        String file = "Event2.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseVoidEvent() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/EventDecls");
        String file = "VoidEvent.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseBindingDecl1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/BindingStatement");
        String file = "Binding1.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseAnnounceExpr1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/AnnounceExpr");
        String file = "Announce1.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseAnnounceExpr2() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/AnnounceExpr");
        String file = "Announce2.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseAnnounceExpr3() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/AnnounceExpr");
        String file = "Announce3.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseInvoke1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/InvokeExpr");
        String file = "Invoke1.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseThunk1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/Thunks");
        String file = "Thunk1.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseRegister1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/RegisterExpr");
        String file = "Register1.java";
        String res = runChecker(file);
        assertEquals("", res);
    }

    @Test
    public void parseAnnounceTCFail1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/AnnounceExpr");
        String file = "FailTypeCheck1.java";
        String res = runChecker(file);
        assertTrue(res.contains("Semantic Error: Parameter 0 must be a subtype of Integer"));
    }

    @Test
    public void parseEventNameCheckFail1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/EventDecls");
        String file = "NameCheckFail.java";
        String res = runChecker(file);
        assertTrue(res.contains("multiply declared in type "));
    }

    @Test
    public void parsePkgCheck1() {
        setCurrentTestFilesFolder("LanguageFeatures/PtolemyBase/PkgCheck");
        String[] files = { "events/Event.java", "clients/Handler.java", "clients/AppEntry.java" };
        String res = runChecker(files);
        assertEquals("", res);
    }

    @Test()
    public void parseBadBinding() {
        LanguageFeaturesTestProvider.createBadBindingTest().doParse();
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LanguageFeaturesTest.class);
    }
}
