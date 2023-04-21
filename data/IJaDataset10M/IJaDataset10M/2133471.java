package com.xfltr.hapax;

import static com.xfltr.hapax.Template.parse;
import com.xfltr.hapax.parser.CyclicIncludeException;
import com.xfltr.hapax.parser.EztParser;
import com.xfltr.hapax.parser.TemplateParser;
import com.xfltr.hapax.parser.TemplateParserException;
import junit.framework.TestCase;

public class EztTest extends TestCase {

    private TemplateDictionary dict_;

    private static final String[][] GOOD_INPUT = { { "", "" }, { "Plain Text", "Plain Text" }, { "[title]", "Default" }, { "[define title]Issue[end]", "" }, { "[define title]Issue[end][title]", "Issue" }, { "[is title \"Title\"]Issue[end]", "" }, { "[is title \"Default\"]Issue[end]", "Issue" }, { "[define title][end][is title \"No title\"]Issue[end]", "" }, { "[define title]Issue[end][is title \"Issue\"]Issue[end]", "Issue" }, { "[if-any title]Issue[end]", "Issue" }, { "[if-any title]Issue[else]No issue[end]", "Issue" }, { "[define title][end][if-any title]Issue[end]", "" }, { "[define title][end][if-any title]Issue[else]No issue[end]", "No issue" }, { "[[][define B][[][end][B][B]", "[[[" }, { "[[][# comments]", "[" }, { "[if-any x]X[else]Y[end][if-any a]B[else]C[end]", "YC" }, { "[define  whitespace ][end][if-any whitespace]Issue[else]No issue[end]", "No issue" }, { "[define nest.var2]x[end][if-any nest.var2][nest.var2][else]fail[end]", "x" } };

    private static String[] EVAL_FAILURES = { "[define X]", "[if-any X]" };

    @Override
    protected void setUp() throws Exception {
        dict_ = TemplateDictionary.create();
    }

    public void testGoodInput() throws TemplateException {
        for (String[] strings : GOOD_INPUT) {
            TemplateDictionary dict = TemplateDictionary.create();
            dict.put("title", "Default");
            String template = strings[0];
            String expected_result = strings[1];
            assertEquals(template, expected_result, parseEzt(template).renderToString(dict));
        }
    }

    public void testEvalFailures() throws TemplateParserException {
        for (String template : EVAL_FAILURES) {
            TemplateDictionary dict = TemplateDictionary.create();
            Template t = parseEzt(template);
            try {
                t.renderToString(dict);
                fail("Template '" + template + "' should have thrown TemplateException.");
            } catch (TemplateException e) {
            }
        }
    }

    public void testInHtml() throws TemplateException {
        String expected = "<a href=\"http://url/url.txt\">Anchor Text</a>";
        String template = "<a href=\"[if-any url][url][else]http://url/url.txt[end]\">Anchor Text</a>";
        assertEquals(template, expected, parseEzt(template).renderToString(dict_));
    }

    public void testNestedIfsWithDictionary() throws TemplateException {
        TemplateDictionary td = TemplateDictionary.create();
        td.put("wombat", "hello");
        String template = "[if-any title]Issue[else][if-any wombat][wombat][else]No issue[end][end]";
        assertEquals(template, "hello", parse(EztParser.create(), template).renderToString(td));
    }

    public void testIfAnyMissingEndThrowsException() throws TemplateParserException {
        Template t = parseEzt("[if-any x]X");
        try {
            t.renderToString(dict_);
            fail("should have thrown exception");
        } catch (TemplateException e) {
        }
    }

    public void testParserRecognizesIncompleteDirectives() {
        try {
            parseEzt("[define]X");
            fail("[define] is recognized as a valid variable name, " + "and it shouldn't be.");
        } catch (TemplateException e) {
        }
    }

    public void testDefineMissingEndThrowsException() throws TemplateException {
        Template tmpl = parseEzt("[define x]X");
        try {
            tmpl.renderToString(dict_);
            fail("should have thrown exception");
        } catch (TemplateException e) {
        }
    }

    public void testIncludesWithLiteralFilename() throws TemplateException {
        MockTemplateLoader loader = new MockTemplateLoader(EztParser.create());
        loader.put("x.ezt", "hello");
        loader.put("parent.ezt", "[include \"x.ezt\"]");
        assertEquals("hello", loader.getTemplate("parent.ezt").renderToString(dict_));
    }

    public void testIncludesWithLeadingHtmlString() throws TemplateException {
        MockTemplateLoader loader = new MockTemplateLoader(EztParser.create());
        loader.put("/x.ezt", "hello");
        loader.put("parent.ezt", "[include \"/html/x.ezt\"]");
        assertEquals("hello", loader.getTemplate("parent.ezt").renderToString(dict_));
    }

    public void testCyclicIncludesThrowException() throws TemplateException {
        dict_.put("x", "x.ezt");
        dict_.put("y", "y.ezt");
        dict_.put("z", "z.ezt");
        MockTemplateLoader loader = new MockTemplateLoader(EztParser.create());
        loader.put("x.ezt", "[include y]");
        loader.put("y.ezt", "[include x]");
        try {
            loader.getTemplate("x.ezt").renderToString(dict_);
            fail("Failed to throw exception when templates have cyclic includes.");
        } catch (CyclicIncludeException e) {
        }
    }

    public void testIncludesWithVariableFilename() throws TemplateException {
        dict_.put("x", "x.ezt");
        MockTemplateLoader loader = new MockTemplateLoader(EztParser.create());
        loader.put("x.ezt", "hello");
        loader.put("parent.ezt", "[include x]");
        assertEquals("hello", loader.getTemplate("parent.ezt").renderToString(dict_));
    }

    public void testFormatIsIgnored() throws TemplateException {
        assertEquals("", parseEzt("[format raw]").renderToString(dict_));
    }

    public void testMultipleParses() throws TemplateException {
        TemplateParser tp = EztParser.create();
        Template t1 = Template.parse(tp, "[define x]X[end][x]");
        Template t2 = Template.parse(tp, "[x][define y]Y[end][y]");
        assertEquals("X", t1.renderToString(TemplateDictionary.create()));
        assertEquals("Y", t2.renderToString(TemplateDictionary.create()));
    }

    private Template parseEzt(String template) throws TemplateParserException {
        return Template.parse(EztParser.create(), template);
    }
}
