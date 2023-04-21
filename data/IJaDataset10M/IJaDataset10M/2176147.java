package org.makumba.test.tags;

import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.Request;
import org.apache.commons.collections.CollectionUtils;
import org.makumba.Transaction;
import org.makumba.commons.NamedResources;
import org.makumba.forms.responder.ResponderFactory;
import org.makumba.test.util.MakumbaJspTestCase;
import org.makumba.test.util.MakumbaTestData;
import org.makumba.test.util.MakumbaTestSetup;
import org.makumba.test.util.MakumbaWebTestSetup;
import org.xml.sax.SAXException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

/**
 * @author Johannes Peeters
 * @author Manuel Bernhardt <manuel@makumba.org>
 * @version $Id: FormsOQLTest.java 5775 2010-12-03 22:44:55Z rosso_nero $
 */
public class FormsOQLTest extends MakumbaJspTestCase {

    @Override
    protected String getJspDir() {
        return "forms-oql";
    }

    @Override
    protected MakumbaTestSetup getSetup() {
        return setup;
    }

    static Suite setup;

    private static final class Suite extends MakumbaWebTestSetup {

        private Suite(Test arg0) {
            super(arg0, "oql");
        }
    }

    public static Test suite() {
        setup = new Suite(new TestSuite(FormsOQLTest.class));
        return setup;
    }

    private WebResponse submissionResponse;

    private static final String namePersonIndivSurname = "Makumbian";

    public void testDbReset() {
        System.err.println("cleaning caches");
        NamedResources.cleanStaticCache("Databases open");
    }

    public void testTomcat() {
    }

    public void testMakNewForm() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakNewForm(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void beginMakAddForm(Request request) throws Exception {
        WebForm form = getFormInJspWithTestName(false);
        form.setParameter("email", "bartolomeus@rogue.be");
        form.submit();
    }

    public void testMakAddForm() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakAddForm(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testMakEditForm() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakEditForm(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testMakForm() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endMakForm(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testMakInputOptionCustomisation() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endMakInputOptionCustomisation(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testBug946() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endBug946(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testBug1115() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endBug1115(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testMakFormRepeatedForms() throws ServletException, IOException, SAXException {
        pageContext.include("forms-oql/testMakRepeatedForms.jsp");
    }

    public void endMakRepeatedForms(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testFormNestedForms() throws ServletException, IOException, SAXException {
        pageContext.include("forms-oql/testMakNestedForms.jsp");
    }

    public void endNestedForms(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testFormMakNewFile() throws ServletException, IOException, SAXException {
        pageContext.include("forms-oql/testMakNewFormFile.jsp");
    }

    public void endMakNewFile(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testFormMakNewBinary() throws ServletException, IOException, SAXException {
        pageContext.include("forms-oql/testMakNewFormBinary.jsp");
    }

    public void endMakNewBinary(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void beginMakAddToNewForm(Request request) throws Exception {
        WebForm form = getFormInJspWithTestName(false);
        form.setParameter("indiv.name", MakumbaTestData.namePersonIndivName_AddToNew);
        form.setParameter("description_1", "addToNewDescription");
        form.setParameter("email_1", "addToNew@makumba.org");
        form.submit();
    }

    public void testMakAddToNewForm() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakAddToNewForm(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void beginMakAddToNewFormValidation(Request request) throws Exception {
        WebForm form = getFormInJspWithTestName(false);
        form.setParameter("indiv.name", MakumbaTestData.namePersonIndivName_AddToNewValidation);
        form.setParameter("description_1", "a");
        form.setParameter("email_1", "addToNew@makumba.org");
        form.submit();
    }

    public void testMakAddToNewFormValidation() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakAddToNewFormValidation(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
        Transaction t = null;
        try {
            t = setup.getTransaction();
            Vector<Dictionary<String, Object>> v = t.executeQuery("select p.indiv.name as name from test.Person p where p.indiv.name = $1", new Object[] { MakumbaTestData.namePersonIndivName_AddToNewValidation });
            assertEquals(0, v.size());
        } finally {
            if (t != null) {
                t.close();
            }
        }
    }

    public void beginMakSearchForm(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakSearchForm.jsp", false);
        form.setParameter("indiv.name", "a");
        submissionResponse = form.submit();
    }

    public void testMakSearchForm() throws ServletException, IOException {
    }

    public void endMakSearchForm(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginMakSearchForm2(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakSearchForm2.jsp", false);
        form.setParameter("indiv.name", "a");
        submissionResponse = form.submit();
    }

    public void testMakSearchForm2() throws ServletException, IOException {
    }

    public void endMakSearchForm2(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginMakSearchForm3(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakSearchForm3.jsp", false);
        submissionResponse = form.submit();
    }

    public void testMakSearchForm3() throws ServletException, IOException {
    }

    public void endMakSearchForm3(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void testMakSearchFormDefaultMatchMode() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endMakSearchFormDefaultMatchMode(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void beginMakSearchFormInSet(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakSearchFormInSet.jsp", false);
        form.setParameter("gender", form.getOptionValues("gender"));
        form.setParameter("brother", "34dqsls");
        submissionResponse = form.submit();
    }

    public void testMakSearchFormInSet() throws ServletException, IOException {
    }

    public void endMakSearchFormInSet(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginFormAnnotation(Request request) throws Exception {
        WebForm form = getFormInJspWithTestName(false);
        form.setParameter("indiv.name", "name");
        form.setParameter("indiv.surname", "surname");
        form.setParameter("age", "invalidInt");
        form.setParameter("weight", "invalidReal");
        form.setParameter("email", "invalidEmail");
        GregorianCalendar first = new GregorianCalendar(1990, 0, 1);
        form.setParameter("firstSex_0", String.valueOf(first.get(Calendar.DAY_OF_MONTH)));
        form.setParameter("firstSex_1", String.valueOf(first.get(Calendar.MONTH)));
        form.setParameter("firstSex_2", String.valueOf(first.get(Calendar.YEAR)));
        GregorianCalendar john = new GregorianCalendar();
        john.setTime(MakumbaTestData.birthdateJohn);
        form.setParameter("birthdate_0", String.valueOf(john.get(Calendar.DAY_OF_MONTH)));
        form.setParameter("birthdate_1", String.valueOf(john.get(Calendar.MONTH)));
        form.setParameter("birthdate_2", String.valueOf(john.get(Calendar.YEAR) - 100));
        form.setParameter("uniqDate_0", String.valueOf(john.get(Calendar.DAY_OF_MONTH)));
        form.setParameter("uniqDate_1", String.valueOf(john.get(Calendar.MONTH)));
        form.setParameter("uniqDate_2", String.valueOf(john.get(Calendar.YEAR)));
        form.setParameter("hobbies", " ");
        form.setParameter("uniqInt", MakumbaTestData.uniqInt.toString());
        form.setParameter("uniqChar", MakumbaTestData.uniqChar);
        submissionResponse = form.submit();
    }

    public void beginMakSearchFormFilterMode(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakSearchFormFilterMode.jsp", false);
        form.setParameter("indiv.name", "v");
        submissionResponse = form.submit();
    }

    public void testMakSearchFormFilterMode() throws ServletException, IOException {
    }

    public void endMakSearchFormFilterMode(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginMakSearchFormStaticWhere(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakSearchFormStaticWhere.jsp", false);
        form.setParameter("indiv.name", "h");
        submissionResponse = form.submit();
    }

    public void testMakSearchFormStaticWhere() throws ServletException, IOException {
    }

    public void endMakSearchFormStaticWhere(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void testFormAnnotation() throws ServletException, IOException {
    }

    public void endFormAnnotation(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginFormResponderOrder(Request request) throws Exception {
        WebResponse resp = getJspResponse("/forms-oql/beginMakNestedNewForms.jsp", true, false);
        HTMLElement[] responderElements = resp.getElementsWithAttribute("name", "__makumba__responder__");
        String[] responderCodesString = new String[responderElements.length];
        for (int i = 0; i < responderElements.length; i++) {
            responderCodesString[i] = responderElements[i].getAttribute("value");
        }
        WebConversation wc = new WebConversation();
        WebResponse r = wc.getResponse(System.getProperty("cactus.contextURL") + "/testInit?getResponderBaseDir=true");
        String responderBaseDir = r.getText().trim();
        String contextPath = "tests";
        ResponderFactory responderFactory = ResponderFactory.getInstance();
        responderFactory.setResponderWorkingDir(responderBaseDir);
        List<String> list = Arrays.asList(responderCodesString);
        Vector<String> v = new Vector<String>();
        v.addAll(list);
        Iterator<String> responderCodes = responderFactory.getResponderCodes(v);
        Iterator<String> orderedResponderCodes = responderFactory.getOrderedResponderCodes(list.iterator());
        ArrayList<String> responderCodesAsList = new ArrayList<String>();
        CollectionUtils.addAll(responderCodesAsList, responderCodes);
        ArrayList<String> orderedResponderCodesAsList = new ArrayList<String>();
        CollectionUtils.addAll(orderedResponderCodesAsList, orderedResponderCodes);
    }

    public void testFormResponderOrder() throws ServletException, IOException {
    }

    public void endFormResponderOrder(WebResponse response) throws Exception {
    }

    public void testClientSideValidationMultipleForms() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endClientSideValidationMultipleForms(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testMakInputTypes() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endMakInputTypes(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void testCalendarEditor() throws ServletException, IOException, SAXException {
        includeJspWithTestName();
    }

    public void endCalendarEditor(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void beginMakNestedNewFormsSimple(Request request) throws Exception {
        WebForm form = getFormInJspWithTestName(false);
        form.setParameter("indiv.name", MakumbaTestData.namePersonIndivName_FirstBrother);
        form.setParameter("indiv.surname", "Person");
        form.setParameter("indiv.name_1", MakumbaTestData.namePersonIndivName_SecondBrother);
        form.setParameter("indiv.surname_1", "Person");
        submissionResponse = form.submit();
    }

    public void testMakNestedNewFormsSimple() throws ServletException, IOException {
    }

    public void endMakNestedNewFormsSimple(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginMakNestedNewAndEditFormsSimple(Request request) throws Exception {
        WebForm form = getFormInJspWithTestName(false);
        form.setParameter("indiv.name", MakumbaTestData.namePersonIndivName_StepBrother);
        form.setParameter("indiv.surname", namePersonIndivSurname);
        submissionResponse = form.submit();
    }

    public void testMakNestedNewAndEditFormsSimple() throws ServletException, IOException {
    }

    public void endMakNestedNewAndEditFormsSimple(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void testMakSubmit() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakSubmit(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    /** TODO this is not a real test, we should test for the result of the response of the partial postback **/
    public void testMakFormAjax() throws ServletException, IOException {
        includeJspWithTestName();
    }

    public void endMakFormAjax(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }

    public void beginMakEditFormWithDiff(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakEditFormWithDiff.jsp", false);
        form.setParameter("indiv.surname", "D'oh");
        form.setParameter("weight", "57.5");
        form.setParameter("intSet", "1");
        form.setParameter("charSet", new String[] { "e", "f" });
        submissionResponse = form.submit();
    }

    public void testMakEditFormWithDiff() throws ServletException, IOException {
    }

    public void endMakEditFormWithDiff(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginMakEditFormWithDiffRevert(Request request) throws Exception {
        WebForm form = getFormInJsp("/forms-oql/testMakEditFormWithDiff.jsp", false);
        form.setParameter("indiv.surname", "von Neumann");
        form.setParameter("weight", "85.7");
        form.setParameter("intSet", new String[] { "0", "1" });
        form.removeParameter("charSet");
        submissionResponse = form.submit();
    }

    public void testMakEditFormWithDiffRevert() throws ServletException, IOException {
    }

    public void endMakEditFormWithDiffRevert(WebResponse response) throws Exception {
        compareToFileWithTestName(submissionResponse, false);
    }

    public void beginLogin(Request request) throws Exception {
        WebForm form = getFormInJsp("/login/testLogin.jsp", false, false);
        form.setParameter("username", "manu");
        form.setParameter("password", "secret");
        form.submit();
    }

    public void testLogin() throws ServletException, IOException {
        pageContext.include("login/testLogin.jsp");
    }

    public void endLogin(WebResponse response) throws Exception {
        compareToFileWithTestName(response, false);
    }
}
