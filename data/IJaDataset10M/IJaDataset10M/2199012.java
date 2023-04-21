package name.angoca.db2sa.core.lexical.impl;

import java.util.ArrayList;
import java.util.List;
import name.angoca.db2sa.AbstractDB2SAException;
import name.angoca.db2sa.core.lexical.model.Token;
import name.angoca.db2sa.core.syntactic.impl.ImplementationSyntacticAnalyzer;
import name.angoca.db2sa.core.syntactic.model.GraphAnswer;
import name.angoca.db2sa.grammar.api.GrammarReaderController;
import name.angoca.db2sa.interfaze.model.ReturnOptions;
import name.angoca.db2sa.tools.Constants;
import name.angoca.db2sa.tools.configurator.Configurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is the set of test for the lexical analyzer. The inputs are phrases that
 * represents the user's command, they are received like strings and this class
 * tests its conversion to Tokens and analyze the results.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.1.0</li>
 * <li>0.2.0</li>
 * <li>0.2.1 Recommendations from PMD.</li>
 * <li>0.2.2 Organized.</li>
 * <li>0.2.3 Setup class.</li>
 * <li>0.3.0 jUnit 4 annotations.</li>
 * <li>0.4.0 Invalid graph exception.</li>
 * <li>0.4.1 Set up method.</li>
 * <li>0.4.2 Destroy instance.</li>
 * <li>0.4.3 final and comments.</li>
 * <li>0.4.4 Reset system variable.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Exception hierarchy changed.</li>
 * <li>1.2.0 New excception</li>
 * <li>1.3.0 Throws</li>
 * <li>1.4.0 Space after phrase</li>
 * <li>1.5.0 Null phrase and grammar controller</li>
 * <li>1.6.0 Param null tests</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.6.0 2009-11-27
 */
public final class ImplementationLexicalAnalyzerTest {

    private static final String CREATE_TAB = "create tab";

    /**
     * The ')' word.
     */
    private static final String CLOSE_PARENTHESIS = ")";

    /**
     * The '&lt;colName&gt;' word.
     */
    private static final String COL_NAME = "<colName>";

    /**
     * The 'create' word.
     */
    private static final String CREATE = "create";

    /**
     * The ' ' word.
     */
    private static final String SPACE = " ";

    /**
     * Load the graph.
     * 
     * @throws AbstractDB2SAException
     *             Never
     */
    @BeforeClass
    public static void setUpBeforeClass() throws AbstractDB2SAException {
        ImplementationLexicalAnalyzer.destroyInstance();
        Configurator.destroyInstance();
        System.setProperty(Constants.DB2SA_CONF_XML_PROPERTY, "db2sa_conf-test.xml");
        Configurator.getInstance().setProperty(Constants.GRAMMAR_READER_NAME, Configurator.GRAMMAR_READER_NAME);
        Configurator.getInstance().setProperty(Constants.GRAMMAR_FILE_NAME, "grammar-test.xml");
        GrammarReaderController.destroyInstance();
        ImplementationLexicalAnalyzer.getInstance();
    }

    /**
     * Clears the configuration file name property at the end of the test.
     * 
     */
    @AfterClass
    public static void tearDownAfterClass() {
        System.clearProperty(Constants.DB2SA_CONF_XML_PROPERTY);
    }

    /**
     * Default constructor.
     */
    public ImplementationLexicalAnalyzerTest() {
    }

    /**
     *Test of the options for the 'create table t1 (c1 int, c2 char(10'
     * command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCloseFirstParenthesis() throws AbstractDB2SAException {
        final String cmdIn = "create table t1 (c1 int, c2 char(10";
        final String cmdOut = "create table t1 (c1 int, c2 char(10";
        final String option1 = ImplementationLexicalAnalyzerTest.CLOSE_PARENTHESIS;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { option1 });
        Assert.assertEquals("object for 'create table t1 (c1 int, c2 char(10'", expected, actual);
    }

    /**
     * Test of the options for the 'create table t2(c1 int' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testComma() throws AbstractDB2SAException {
        final String cmdIn = "create table t2(c1 int";
        final String cmdOut = "create table t2(c1 int";
        final String option1 = ",";
        final String option2 = ImplementationLexicalAnalyzerTest.CLOSE_PARENTHESIS;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { option1, option2 });
        Assert.assertEquals("object for 'create table t2(c1 int'", expected, actual);
    }

    /**
     * Tests the token conversion.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testConvertionCrea() throws AbstractDB2SAException {
        final String phraseIn = "crea";
        final String phraseOut = ImplementationLexicalAnalyzerTest.CREATE;
        final String token1 = "crea";
        final List<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(token1));
        final boolean endsWithSpace = false;
        final GraphAnswer answer = ImplementationSyntacticAnalyzer.getInstance().getOptions(tokens, endsWithSpace);
        final String actual = ImplementationLexicalAnalyzer.getInstance().replaceLastToken(phraseIn, tokens, answer);
        final String expected = phraseOut;
        Assert.assertEquals("conversion for 'crea'", expected, actual);
    }

    /**
     * Tests the token conversion 'create tab' -> 'create tab'.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testConvertionCreateTab() throws AbstractDB2SAException {
        final String phraseIn = ImplementationLexicalAnalyzerTest.CREATE_TAB;
        final String phraseOut = ImplementationLexicalAnalyzerTest.CREATE_TAB;
        final String token1 = ImplementationLexicalAnalyzerTest.CREATE;
        final String token2 = "tab";
        final List<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(token1));
        tokens.add(new Token(token2));
        final boolean endsWithSpace = false;
        final GraphAnswer answer = ImplementationSyntacticAnalyzer.getInstance().getOptions(tokens, endsWithSpace);
        final String actual = ImplementationLexicalAnalyzer.getInstance().replaceLastToken(phraseIn, tokens, answer);
        final String expected = phraseOut;
        Assert.assertEquals("conversion for 'create tab'", expected, actual);
    }

    /**
     * Test to complete the phrase crea (phrase).
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreaPhrase1() throws AbstractDB2SAException {
        final String cmdIn = "crea";
        final String cmdOut = ImplementationLexicalAnalyzerTest.CREATE;
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final String actual = ret.getPhrase();
        final String expected = cmdOut;
        Assert.assertEquals("complete for 'crea'", expected, actual);
    }

    /**
     * Test to complete the phrase crea (options).
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreaPhrase2() throws AbstractDB2SAException {
        final String cmdIn = "crea";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final int actual = ret.getOptions().length;
        final int expected = new String[] {}.length;
        Assert.assertEquals("options for 'crea'", expected, actual);
    }

    /**
     * Test that the phrase 'crea' does not have several phrases.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreaPhrase3() throws AbstractDB2SAException {
        final String cmdIn = "crea";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final int actual = ret.getPhrases().length;
        final int expected = new String[] {}.length;
        Assert.assertEquals("phrases for 'crea'", expected, actual);
    }

    /**
     * Test to complete the phrase 'crea' (object).
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreaPhrase4() throws AbstractDB2SAException {
        final String cmdIn = "crea";
        final String cmdOut = ImplementationLexicalAnalyzerTest.CREATE;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] {});
        Assert.assertEquals("object for 'crea'", expected, actual);
    }

    /**
     * Test of the options for the 'create' command. There is only one option.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateCommand() throws AbstractDB2SAException {
        final String cmdIn = ImplementationLexicalAnalyzerTest.CREATE;
        final String cmdOut = ImplementationLexicalAnalyzerTest.CREATE;
        final String opt1 = "table";
        final String opt2 = "tablespace";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { opt1, opt2 });
        Assert.assertEquals("object for 'create'", expected, actual);
    }

    /**
     * Test the token completion tab -> table.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTab() throws AbstractDB2SAException {
        final String cmdIn = ImplementationLexicalAnalyzerTest.CREATE_TAB;
        final String cmdOut = ImplementationLexicalAnalyzerTest.CREATE_TAB;
        final String opt1 = "table";
        final String opt2 = "tablespace";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] { opt1, opt2 }, new String[] {});
        Assert.assertEquals("object for 'create tab'", expected, actual);
    }

    /**
     * Test of the options for the 'create table' command. Test options.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTableCommand1() throws AbstractDB2SAException {
        final String cmdIn = "create table";
        final String opt1 = "<tableName>";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final String[] actual = ret.getOptions();
        final String[] expected = new String[] { opt1 };
        Assert.assertEquals("options for 'create table'", expected.length, actual.length);
    }

    /**
     * Test of the options for the 'create table' command. Test phrases.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTableCommand2() throws AbstractDB2SAException {
        final String cmdIn = "create table";
        final String phrase1 = "tablespace";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final String[] actual = ret.getPhrases();
        final String[] expected = new String[] { phrase1 };
        Assert.assertEquals("phrases for 'create table'", expected.length, actual.length);
    }

    /**
     * Test of the options for the 'create table' command. Phrase empty.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTableCommand3() throws AbstractDB2SAException {
        final String cmdIn = "create table";
        final String cmdOut = "create table";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final String actual = ret.getPhrase();
        final String expected = cmdOut;
        Assert.assertEquals("complete for 'create table'", expected, actual);
    }

    /**
     * Test of the options for the 'create table' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTableCommand4() throws AbstractDB2SAException {
        final String cmdIn = "create table";
        final String cmdOut = "create table";
        final String phrase1 = "tablespace";
        final String opt1 = "<tableName>";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] { phrase1 }, new String[] { opt1 });
        Assert.assertEquals("object for 'create table'", expected, actual);
    }

    /**
     * Test of the options for the 'create table' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTableCommand4WithSpace() throws AbstractDB2SAException {
        final String cmdIn = "create table ";
        final String cmdOut = "create table";
        final String opt1 = "<tableName>";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { opt1 });
        Assert.assertEquals("object for 'create table '", expected, actual);
    }

    /**
     * Test of the options for the 'create table &lt;tableName&gt;' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCreateTableTableNameCommand() throws AbstractDB2SAException {
        final String cmdIn = "create table t1";
        final String cmdOut = "create table t1";
        final String opt1 = "(";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { opt1 });
        Assert.assertEquals("object for 'create table t1'", expected, actual);
    }

    /**
     * Test of the options for the 'create table &lt;tableName&gt;(' command.
     * Without space between tableName and command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCTtnParenthesis() throws AbstractDB2SAException {
        final String cmdIn = "create table t2(";
        final String cmdOut = "create table t2(";
        final String opt1 = ImplementationLexicalAnalyzerTest.COL_NAME;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { opt1 });
        Assert.assertEquals("object for 'create table t2('", expected, actual);
    }

    /**
     * Test of the options for the 'create table &lt;tableName&gt; (' command.
     * With space between tableName and command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCTtnSpaceParenthesis() throws AbstractDB2SAException {
        final String cmdIn = "create table t2 (";
        final String cmdOut = "create table t2 (";
        final String opt1 = ImplementationLexicalAnalyzerTest.COL_NAME;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { opt1 });
        Assert.assertEquals("object for 'create table t2 ('", expected, actual);
    }

    /**
     * Test of the options for the 'create table &lt;tableName&gt; (' command.
     * With space between tableName and command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testCTtnSpaceParenthesisWithSpace() throws AbstractDB2SAException {
        final String cmdIn = "create table t2 ( ";
        final String cmdOut = "create table t2 (";
        final String opt1 = ImplementationLexicalAnalyzerTest.COL_NAME;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { opt1 });
        Assert.assertEquals("object for 'create table t2 ('", expected, actual);
    }

    /**
     * Test of the options for the 'create table t2(c1' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testDataType() throws AbstractDB2SAException {
        final String cmdIn = "create table t2(c1";
        final String cmdOut = "create table t2(c1";
        final String option1 = "int";
        final String option2 = "char";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { option1, option2 });
        Assert.assertEquals("object for 'create table t2(c1'", expected, actual);
    }

    /**
     * Empty string as a command gives also a completed phrase empty.
     * 
     * @throws AbstractDB2SAException
     *             Never
     */
    @Test
    public void testEmptyString1() throws AbstractDB2SAException {
        final String cmdIn = "";
        final String cmdOut = "";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final String actual = ret.getPhrase();
        final String expected = cmdOut;
        Assert.assertEquals("Return Empty phrase", expected, actual);
    }

    /**
     * Empty string as a command gives a set of options different to zero. At
     * least, the grammar has to have one option.
     * <p>
     * If the grammar was not defined is it possible to return zero options,
     * however this is not a normal case.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testEmptyString2() throws AbstractDB2SAException {
        final String cmdIn = "";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final boolean condition = 0 < actual.getOptions().length;
        Assert.assertTrue("Zero options for an empty phrase", condition);
    }

    /**
     * An empty string does not have several way of phrases.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testEmptyString3() throws AbstractDB2SAException {
        final String cmdIn = "";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        Assert.assertEquals("Zero phrases for an empty phrase", 0, actual.getPhrases().length);
    }

    /**
     * Invalid, no options.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testInvalidCommand1() throws AbstractDB2SAException {
        final String cmdIn = "create table t1 (c1 int char)";
        final String cmdOut = "create table t1 (c1 int char)";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] {});
        Assert.assertEquals("object for 'create table t1 (c1 int char)'", expected, actual);
    }

    /**
     * Invalid, no options.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testInvalidCommand2() throws AbstractDB2SAException {
        final String cmdIn = "Andres Gomez Casanova";
        final String cmdOut = "Andres Gomez Casanova";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] {});
        Assert.assertEquals("object for 'Andres Gomez Casanova'", expected, actual);
    }

    /**
     * Tries to add a null answer.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test(expected = AssertionError.class)
    public void testNullAnswerReplacetoken() throws AbstractDB2SAException {
        ImplementationLexicalAnalyzer.getInstance().replaceLastToken("", new ArrayList<Token>(), null);
        Assert.fail("Exception before this");
    }

    /**
     * Tests that a phrase cannot be null.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test(expected = AssertionError.class)
    public void testNullPhrase() throws AbstractDB2SAException {
        ImplementationLexicalAnalyzer.getInstance().processPhrase(null);
        Assert.fail("Exception before this");
    }

    /**
     * Tries to add a null phrase.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test(expected = AssertionError.class)
    public void testNullPhraseReplacetoken() throws AbstractDB2SAException {
        ImplementationLexicalAnalyzer.getInstance().replaceLastToken(null, new ArrayList<Token>(), new GraphAnswer(new ArrayList<Token>(), new ArrayList<Token>()));
        Assert.fail("Exception before this");
    }

    /**
     * Tries to add a null in the list of tokens.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test(expected = AssertionError.class)
    public void testNullTokenInTokensReplacetoken() throws AbstractDB2SAException {
        final List<Token> tokens = new ArrayList<Token>();
        tokens.add(null);
        ImplementationLexicalAnalyzer.getInstance().replaceLastToken("", tokens, new GraphAnswer(new ArrayList<Token>(), new ArrayList<Token>()));
        Assert.fail("Exception before this");
    }

    /**
     * Tries to add a null list of tokens.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test(expected = AssertionError.class)
    public void testNullTokensReplacetoken() throws AbstractDB2SAException {
        ImplementationLexicalAnalyzer.getInstance().replaceLastToken("", null, new GraphAnswer(new ArrayList<Token>(), new ArrayList<Token>()));
        Assert.fail("Exception before this");
    }

    /**
     *Test of the options for the 'create table t1 (c1 int, c2 char(' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testPrecision() throws AbstractDB2SAException {
        final String cmdIn = "create table t1 (c1 int, c2 char(";
        final String cmdOut = "create table t1 (c1 int, c2 char(";
        final String option1 = "<charTypeValue>";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { option1 });
        Assert.assertEquals("object for 'create table t1 (c1 int, c2 char('", expected, actual);
    }

    /**
     * Test of the options for the 'create table t2(c1 int,' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testSecondColumn() throws AbstractDB2SAException {
        final String cmdIn = "create table t2(c1 int,";
        final String cmdOut = "create table t2(c1 int,";
        final String option = ImplementationLexicalAnalyzerTest.COL_NAME;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { option });
        Assert.assertEquals("object for create table t2(c1 int,'", expected, actual);
    }

    /**
     *Test of the options for the 'create table t1 (c1 int, c2 char(10)'
     * command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testSeveralOptionsAfterColumn() throws AbstractDB2SAException {
        final String cmdIn = "create table t1 (c1 int, c2 char(10)";
        final String cmdOut = "create table t1 (c1 int, c2 char(10)";
        final String option1 = ",";
        final String option2 = ImplementationLexicalAnalyzerTest.CLOSE_PARENTHESIS;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] { option1, option2 });
        Assert.assertEquals("object for 'create table t1 (c1 int, c2 char(10)'", expected, actual);
    }

    /**
     * The string with just one space returns a string with one space also.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testSpace1() throws AbstractDB2SAException {
        final String cmdIn = ImplementationLexicalAnalyzerTest.SPACE;
        final String cmdOut = "";
        final ReturnOptions ret = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final String actual = ret.getPhrase();
        final String expected = cmdOut;
        Assert.assertEquals("Empty phrase for an space", expected, actual);
    }

    /**
     * The string with just one space returns a string with one space also.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testSpace2() throws AbstractDB2SAException {
        final String cmdIn = ImplementationLexicalAnalyzerTest.SPACE;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final boolean condition = 0 < actual.getOptions().length;
        Assert.assertTrue("Several options for a space", condition);
    }

    /**
     * An space does not have several ways of phrases.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testSpace3() throws AbstractDB2SAException {
        final String cmdIn = ImplementationLexicalAnalyzerTest.SPACE;
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        Assert.assertEquals("Zero phrases for a space", 0, actual.getPhrases().length);
    }

    /**
     *Test of the options for the 'create table t1 (c1 int)' command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testValidCommand1() throws AbstractDB2SAException {
        final String cmdIn = "create table t1 (c1 int)";
        final String cmdOut = "create table t1 (c1 int)";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] {});
        Assert.assertEquals("object for 'create table t1 (c1 int)'", expected, actual);
    }

    /**
     *Test of the options for the 'create table t1 (c1 int, c2 char(10))'
     * command.
     * 
     * @throws AbstractDB2SAException
     *             Never.
     */
    @Test
    public void testValidCommand2() throws AbstractDB2SAException {
        final String cmdIn = "create table t1 (c1 int, c2 char(10))";
        final String cmdOut = "create table t1 (c1 int, c2 char(10))";
        final ReturnOptions actual = ImplementationLexicalAnalyzer.getInstance().processPhrase(cmdIn);
        final ReturnOptions expected = new ReturnOptions(cmdOut, new String[] {}, new String[] {});
        Assert.assertEquals("object for 'create table t1 (c1 int, c2 char(10))'", expected, actual);
    }
}
