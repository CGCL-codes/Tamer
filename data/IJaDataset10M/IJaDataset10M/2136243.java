package net.sf.buildbox.parser.maven;

import junit.framework.Assert;
import net.sf.buildbox.parser.ParserTestUtils;
import net.sf.buildbox.parser.core.AstBuildingListener;
import net.sf.buildbox.parser.core.FormalGrammar;
import net.sf.buildbox.parser.core.LLParser;
import net.sf.buildbox.parser.model.MultiLineTerminal;
import net.sf.buildbox.parser.maven.model.Mvn221GrammarBuilder;
import net.sf.buildbox.parser.model.*;
import net.sf.buildbox.worker.StdoutFeedback;
import net.sf.buildbox.worker.maven.MavenLogListener;
import org.junit.Before;
import org.junit.Test;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Petr Kozelka
 */
public class Mvn221GrammarBuilderTest {

    FormalGrammar g;

    @Before
    public void setUp() throws Exception {
        final Mvn221GrammarBuilder builder = new Mvn221GrammarBuilder();
        g = builder.build();
        g.buildPredictionSet();
        ParserTestUtils.print(g);
    }

    @Test
    public void M221AdhocTest() throws Exception {
        final String m221 = "../../../../../adhoc/M221/";
        testParser(m221 + "memory/M221-IOE-CannotAllocateMemory.log");
    }

    @Test
    public void M221Failure_fae_X() throws Exception {
        testParser("M221Failure-fae-X.log");
    }

    @Test
    public void M221Failure_fae() throws Exception {
        testParser("M221Failure-fae.log");
    }

    @Test
    public void M221Failure() throws Exception {
        testParser("M221Failure.log");
    }

    @Test
    public void M221skipTests() throws Exception {
        testParser("M221skipTests.log");
    }

    private void testParser(String fileName) throws IOException, JAXBException, TransformerException, ParserConfigurationException {
        testParser(resourceToFile(fileName));
    }

    private void testParser(File file) throws IOException, JAXBException, TransformerException, ParserConfigurationException {
        System.out.println("========== Parsing: " + file + " ===========");
        final LLParser parser = new LLParser();
        parser.setGrammar(g);
        final AstBuildingListener listener = new MavenLogListener(new StdoutFeedback());
        listener.setCondensed(false);
        parser.setListener(listener);
        parser.parseFile(file);
        System.out.println("Log structure: " + file);
        ParserTestUtils.printTree(listener.getRootAstNode());
    }

    private File resourceToFile(String resourceName) throws FileNotFoundException {
        final URL url = getClass().getResource(resourceName);
        if (url == null) {
            throw new FileNotFoundException(resourceName);
        }
        final File file = new File(url.getPath());
        System.out.println("file = " + file.getAbsolutePath());
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        return file;
    }

    @Test
    public void testMultiLineMatcher() throws Exception {
        final TerminalSymbol HEADING_LINE = new ExactLineTerm("[INFO] ------------------------------------------------------------------------").name("HEADING");
        final TerminalSymbol BUILDING_MODULE_PREFIX = new PrefixLineTerm("[INFO] Building ", "moduleName").name("BUILDING");
        final TerminalSymbol TASKSEGMENT_PATTERN = new PatternLineTerm(Pattern.compile("\\[INFO\\]    task-segment: \\[(.+)\\]"), "taskSegment").name("TASKS");
        final TerminalSymbol MODULE_BUILD_HEADING = new MultiLineTerminal(HEADING_LINE, BUILDING_MODULE_PREFIX, TASKSEGMENT_PATTERN, HEADING_LINE).name("MODULE_BUILD_HEADING");
        final Deque<String> d = new LinkedList<String>();
        d.addFirst("[INFO] ------------------------------------------------------------------------");
        d.addFirst("[INFO] Building buildbox-api:1-SNAPSHOT");
        d.addFirst("[INFO]    task-segment: [clean, install]");
        d.addFirst("[INFO] ------------------------------------------------------------------------");
        final Map<String, String> m = MODULE_BUILD_HEADING.matches(d);
        System.out.println("m = " + m);
        Assert.assertNotNull("Multiline matcher does not work", m);
    }
}
