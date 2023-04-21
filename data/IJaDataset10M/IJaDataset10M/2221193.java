package ch.ethz.mxquery.test;

import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.QName;
import ch.ethz.mxquery.datamodel.XQName;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.model.AbstractStep;
import ch.ethz.mxquery.model.StepInformation;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.util.Hashtable;
import ch.ethz.mxquery.util.ObjectObjectPair;
import ch.ethz.mxquery.util.Set;
import ch.ethz.mxquery.xdmio.XDMInputFactory;
import ch.ethz.mxquery.xdmio.XDMSerializer;
import ch.ethz.mxquery.xdmio.XDMSerializerSettings;
import ch.ethz.mxquery.xdmio.XMLSource;

/**
 * @author petfisch
 *
 */
public class ProjectionTests extends XQueryTestBase {

    private static final String TEST_PROJECTION_PREFIX = "ProjectionTests";

    private static final String TEST_PROJECTION_QUERY_PREFIX = TEST_PROJECTION_PREFIX + "/queries/";

    private static final String TEST_PROJECTION_DOC_PREFIX = TEST_PROJECTION_PREFIX + "/docs/";

    public void test_SinglePath_RootOnly() throws MXQueryException {
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        String xmlDoc = "<a/>";
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector projPaths = new Vector();
        projPaths.addElement(setUpPath1Keep());
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("", res);
    }

    public void test_SinglePath_OneChild() throws MXQueryException {
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        String xmlDoc = "<a><b/></a>";
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("", res);
    }

    public void test_SinglePath_SingleKeepNoChilds() throws MXQueryException {
        String xmlDoc = "<a><b><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_SingleNoChilds() throws MXQueryException {
        String xmlDoc = "<a><b><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_SingleKeepTextChild() throws MXQueryException {
        String xmlDoc = "<a><b><c>Text</c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c>Text</c></b></a>", res);
    }

    public void test_SinglePath_SingleTextChild() throws MXQueryException {
        String xmlDoc = "<a><b><c>Text</c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_SingleKeepElementChild1() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/></c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><d/></c></b></a>", res);
    }

    public void test_SinglePath_SingleElementChild1() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/></c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_SingleKeepElementChild2() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/><e/></c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><d/><e/></c></b></a>", res);
    }

    public void test_SinglePath_SingleElementChild2() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/><e/></c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_BeforeAfterKeep() throws MXQueryException {
        String xmlDoc = "<a><x/><b><c><d/><e/></c></b><y/></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><d/><e/></c></b></a>", res);
    }

    public void test_SinglePath_BeforeAfter() throws MXQueryException {
        String xmlDoc = "<a><x/><b><c><d/><e/></c></b><y/></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_OneEmptyKeep() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/><e/></c></b><b/></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><d/><e/></c></b></a>", res);
    }

    public void test_SinglePath_OneEmpty() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/><e/></c></b><b/></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_TwoEmptyKeep() throws MXQueryException {
        String xmlDoc = "<a><b/><b><c><d/><e/></c></b><b/></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><d/><e/></c></b></a>", res);
    }

    public void test_SinglePath_TwoEmpty() throws MXQueryException {
        String xmlDoc = "<a><b/><b><c><d/><e/></c></b><b/></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SinglePath_SingleKeepTwoElementChild() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/></c><c></c></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><d/></c><c/></b></a>", res);
    }

    public void test_SinglePath_SingleTwoElementChild() throws MXQueryException {
        String xmlDoc = "<a><b><c><d/></c><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/><c/></b></a>", res);
    }

    public void test_TwoPaths_SingleKeepNoChilds1() throws MXQueryException {
        String xmlDoc = "<a><b><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector path2 = setUpPath2();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        projPaths.addElement(path2);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_TwoPaths_SingleKeepNoChilds2() throws MXQueryException {
        String xmlDoc = "<a><d><e/></d></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector path2 = setUpPath2();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        projPaths.addElement(path2);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><d/></a>", res);
    }

    public void test_TwoPaths_PaperSample1() throws MXQueryException {
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, TEST_PROJECTION_DOC_PREFIX + "proj1.xml", true, Context.NO_VALIDATION, null);
        Vector path1 = setUpPath1Keep();
        Vector path2 = setUpPath2();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        projPaths.addElement(path2);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c><f/></c></b><d/></a>", res);
    }

    public void test_TwoPaths_KeepAbove1() throws MXQueryException {
        String xmlDoc = "<a><b><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = new Vector();
        path1.addElement(AbstractStep.ROOT_STEP);
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("a")));
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("b")));
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("c")));
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("d")));
        Vector path2 = new Vector();
        path2.addElement(AbstractStep.ROOT_STEP);
        path2.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("a")));
        path2.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("b")));
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        projPaths.addElement(path2);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b/></a>", res);
    }

    public void test_SingleDescPath_SingleKeepNoChilds() throws MXQueryException {
        String xmlDoc = "<a><b><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpDescPath1Keep();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    public void test_SingleDescPath_SingleNoChilds() throws MXQueryException {
        String xmlDoc = "<a><b><c/></b></a>";
        Context ctx = new Context();
        ctx.setParserType(Context.NONVALIDATED_INPUT_MODE_STAX_PROJECTION);
        XMLSource reader = XDMInputFactory.createXMLInput(ctx, new StringReader(xmlDoc), true, Context.NO_VALIDATION, null);
        Vector path1 = setUpDescPath1();
        Vector projPaths = new Vector();
        projPaths.addElement(path1);
        String res = runProjectedQuery(reader, projPaths);
        assertEquals("<a><b><c/></b></a>", res);
    }

    private Vector setUpPath1() throws MXQueryException {
        Vector path1 = new Vector();
        path1.addElement(AbstractStep.ROOT_STEP);
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("a")));
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("b")));
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("c")));
        return path1;
    }

    private Vector setUpPath1Keep() throws MXQueryException {
        Vector path1 = setUpPath1();
        path1.addElement(AbstractStep.KEEP_SUBTREE);
        return path1;
    }

    private Vector setUpDescPath1() throws MXQueryException {
        Vector path1 = new Vector();
        path1.addElement(AbstractStep.ROOT_STEP);
        path1.addElement(new AbstractStep(StepInformation.AXIS_DESCENDANT, new QName("b")));
        path1.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("c")));
        return path1;
    }

    private Vector setUpDescPath1Keep() throws MXQueryException {
        Vector path1 = setUpDescPath1();
        path1.addElement(AbstractStep.KEEP_SUBTREE);
        return path1;
    }

    private Vector setUpPath2() throws MXQueryException {
        Vector path2 = new Vector();
        path2.addElement(AbstractStep.ROOT_STEP);
        path2.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("a")));
        path2.addElement(new AbstractStep(StepInformation.AXIS_CHILD, new QName("d")));
        return path2;
    }

    private String runProjectedQuery(XMLSource reader, Vector projPaths) throws MXQueryException {
        reader.setProjectionPaths(projPaths);
        XDMSerializerSettings set = new XDMSerializerSettings();
        set.setOmitXMLDeclaration(true);
        XDMSerializer ser = new XDMSerializer(set);
        String res = ser.eventsToXML(reader);
        return res;
    }

    private void showProjectedQuery(XMLSource reader, Vector projPaths) throws MXQueryException {
        reader.setProjectionPaths(projPaths);
        XDMSerializerSettings set = new XDMSerializerSettings();
        set.setOmitXMLDeclaration(true);
        XDMSerializer ser = new XDMSerializer(set);
        ser.eventsToXML(System.out, reader);
    }

    public void test_Path1() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Path1.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Path2() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Path2.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Path3() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Path3.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Cond1() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Cond1.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Cond2() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Cond2.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Cond3() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Cond3.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Cond4() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Cond4.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_Comp1() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "Comp1.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_FLWOR1() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "FLWOR1.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_FLWOR2() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "FLWOR2.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    public void test_ElemConstr1() throws Exception {
        String query = UriToString(TEST_PROJECTION_QUERY_PREFIX + "ElemConstr1.xq");
        PreparedStatement pi = prepareQuery(query, false, false, false, false, false);
        Hashtable ht = pi.getProjectionPaths();
        handleProjectionPaths(ht);
    }

    private void handleProjectionPaths(Hashtable projPaths) throws Exception {
        ObjectObjectPair paths = (ObjectObjectPair) projPaths.get("DOC");
        Set returned = (Set) paths.getFirst();
        Set used = (Set) paths.getSecond();
        Enumeration en = returned.elements();
        while (en.hasMoreElements()) {
            String curPath = translateProjectionPath(en);
            System.out.println(curPath + " #");
        }
        en = used.elements();
        while (en.hasMoreElements()) {
            String curPath = translateProjectionPath(en);
            System.out.println(curPath);
        }
    }

    private String translateProjectionPath(Enumeration en) throws Exception {
        Vector curPath = (Vector) en.nextElement();
        StringBuffer pathString = new StringBuffer();
        for (int i = 0; i < curPath.size(); i++) {
            AbstractStep as = (AbstractStep) curPath.elementAt(i);
            if (as == AbstractStep.ROOT_STEP) pathString.append("/"); else if (as == AbstractStep.KEEP_SUBTREE) pathString.append(" #"); else {
                pathString.append(as.getDirectionString(true));
                XQName xq = as.getNodeTest().getXQName();
                if (xq != null) pathString.append(xq.toString()); else throw new Exception("Unsupported node test for projection - please report");
                if (i < curPath.size() - 2 || ((AbstractStep) curPath.elementAt(curPath.size() - 1) != AbstractStep.KEEP_SUBTREE && i < curPath.size() - 1)) pathString.append("/");
            }
        }
        return pathString.toString();
    }

    ;
}
