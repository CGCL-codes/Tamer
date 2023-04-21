package de.iteratec.turm.functionalTests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import junit.framework.Assert;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import de.iteratec.turm.common.Logger;

/**
 * Infrastructure class for writing functional tests for turm. This class provides a number of
 * helper methods to handle functional tests for turm.
 */
public class TurmSession {

    private static final Logger logger = Logger.getLogger(TurmSession.class);

    private boolean loggedIn = false;

    private final WebClient webClient;

    private Page currentPage;

    private Page previousPage;

    private final URL baseURL;

    public TurmSession(final String baseURL) throws MalformedURLException {
        this(baseURL, null, 0);
    }

    public TurmSession(final String baseURL, final String proxyName, int proxyPort) throws MalformedURLException {
        logger.debug("Creating TurmSession with baseURL: " + baseURL + " and proxy: " + proxyName + " " + proxyPort);
        if (proxyName == null) {
            this.webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0);
        } else {
            this.webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0, proxyName, proxyPort);
        }
        if (!this.webClient.isJavaScriptEnabled()) {
            throw new IllegalStateException("JavaScript support not provided");
        }
        if (baseURL.endsWith("/")) {
            this.baseURL = new URL(baseURL);
        } else {
            this.baseURL = new URL(baseURL + "/");
        }
        this.webClient.setHomePage(baseURL);
        this.webClient.setRedirectEnabled(true);
        this.webClient.setRefreshHandler(new IgnoringRefreshHandler());
        this.webClient.addRequestHeader("Accept-Language", "de");
    }

    /**
   * Used for checking and responding to JavaScript Dialogs.
   */
    public static final class DialogExpectation {

        /** expected text */
        private final String text;

        /** does {{@link #text} contain regular expressions */
        private final boolean textRegexp;

        /** how to respond to the dialog. */
        private final String response;

        public DialogExpectation(String text, boolean textRegexp, String response) {
            this.text = text;
            this.textRegexp = textRegexp;
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public String getText() {
            return text;
        }

        public boolean isTextRegexp() {
            return textRegexp;
        }
    }

    /**
   * Handle JavaScript dialogs
   */
    private final class AlertAndConfirmHandler implements AlertHandler, ConfirmHandler {

        private int pos;

        private final DialogExpectation[] expectations;

        public AlertAndConfirmHandler(DialogExpectation[] expectations) {
            this.expectations = expectations;
            this.pos = 0;
        }

        public void handleAlert(Page page, String message) {
            checkMessage(message);
            pos++;
        }

        private void checkMessage(String message) {
            String expected = expectations[pos].getText();
            if (expectations[pos].isTextRegexp()) {
                Assert.assertTrue(message.matches(".*" + expected + ".*"));
            } else {
                Assert.assertEquals(message, expected);
            }
        }

        public boolean handleConfirm(Page page, String message) {
            String response = expectations[pos].getResponse();
            if (!"".equals(expectations[pos].getText())) {
                checkMessage(message);
            }
            pos++;
            return response.equalsIgnoreCase("true");
        }
    }

    /**
   * Set up handlers for JavaScript dialogs.
   */
    public void expectDialogs(DialogExpectation[] expectations) {
        AlertAndConfirmHandler handler = new AlertAndConfirmHandler(expectations);
        getClient().setAlertHandler(handler);
        getClient().setConfirmHandler(handler);
    }

    /**
   * respond no to JavaScript dialog
   */
    protected void confirmno() {
        expectDialogs(new DialogExpectation[] { new DialogExpectation("", false, "false") });
    }

    /**
   * respond yes to JavaScript dialog
   */
    protected void confirmyes() {
        expectDialogs(new DialogExpectation[] { new DialogExpectation("", false, "true") });
    }

    public void login(final String userName, final String password, boolean checkSuccess) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        if (loggedIn) {
            logout();
        }
        goToStartPage();
        HtmlForm form = ((HtmlPage) currentPage).getFormByName("login_form");
        HtmlTextInput userField = (HtmlTextInput) form.getInputByName("j_username");
        userField.setValueAttribute(userName);
        HtmlPasswordInput passwordField = (HtmlPasswordInput) form.getInputByName("j_password");
        passwordField.setValueAttribute(password);
        HtmlSubmitInput loginButton = (HtmlSubmitInput) form.getInputByName("login_button");
        setCurrentPage(loginButton.click());
        if (checkSuccess) {
            Assert.assertTrue("wrong servlet page after login", this.currentPage.getWebResponse().getUrl().getPath().endsWith("users"));
            assertText(Constants.TITLE, false);
            this.loggedIn = true;
        }
    }

    public void login(final String userName, final String password) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        login(userName, password, true);
    }

    public void goToStartPage() throws IOException, MalformedURLException {
        setCurrentPage(this.webClient.getPage(this.baseURL));
    }

    public void logout() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        if (!loggedIn) {
            throw new IllegalStateException("Cannot log out since not logged in");
        }
        setCurrentPage(this.webClient.getPage(this.webClient.getHomePage() + "/j_turm_logout"));
        Assert.assertNotNull(((HtmlPage) this.currentPage).getFormByName("login_form"));
        this.loggedIn = false;
    }

    public HtmlForm getFormByName(String formName) {
        HtmlForm form = ((HtmlPage) getCurrentPage()).getFormByName(formName);
        Assert.assertNotNull(form);
        return form;
    }

    public HtmlForm getFormById(String formId) {
        HtmlForm form = (HtmlForm) ((HtmlPage) getCurrentPage()).getHtmlElementById(formId);
        Assert.assertNotNull(form);
        return form;
    }

    public HtmlElement getHtmlElementById(String id) {
        return ((HtmlPage) this.currentPage).getHtmlElementById(id);
    }

    public void setCurrentPage(final Page newPage) {
        this.previousPage = this.currentPage;
        this.currentPage = newPage;
    }

    public void goToPreviousPage() {
        if (this.previousPage == null) {
            throw new IllegalStateException("No further previous page");
        }
        this.currentPage = this.previousPage;
        this.previousPage = null;
    }

    public void setTextFieldByName(final String formName, final String textFieldName, final String newValue) {
        HtmlInput input = getFormByName(formName).getInputByName(textFieldName);
        input.setValueAttribute(newValue);
    }

    public void setTextFieldById(String inputFieldId, String content) {
        HtmlInput textField = (HtmlInput) getHtmlElementById(inputFieldId);
        textField.setValueAttribute(content);
    }

    public void clickButtonByName(final String formName, final String buttonName) throws IOException {
        HtmlInput element = getFormByName(formName).getInputByName(buttonName);
        setCurrentPage(element.click());
    }

    public void clickButtonById(String htmlId) throws IOException {
        HtmlButton button = (HtmlButton) getHtmlElementById(htmlId);
        setCurrentPage(button.click());
    }

    public void clickOptionById(String htmlId) throws IOException {
        HtmlOption option = (HtmlOption) getHtmlElementById(htmlId);
        setCurrentPage(option.click());
    }

    protected void clickOption(final String formName, final String selectName, final String optionText) throws IOException {
        HtmlSelect select = getFormByName(formName).getSelectByName(selectName);
        List<?> options = select.getOptions();
        for (Iterator<?> it = options.iterator(); it.hasNext(); ) {
            HtmlOption option = (HtmlOption) it.next();
            if (optionText.equals(option.asText())) {
                setCurrentPage(option.click());
                if (logger.isDebugEnabled()) {
                    logger.debug("option text '" + option.asText() + "' clicked for select with name '" + selectName + "'");
                }
                break;
            }
        }
    }

    public void assertNoError() {
        assertXPath("count(//div[@class='error'])=0");
        assertText(Constants.TITLE, false);
    }

    public void assertXPath(final String xpathExpression) {
        try {
            XPath xpath = new HtmlUnitXPath(xpathExpression);
            Assert.assertTrue("XPath '" + xpathExpression + "' does not evaluate to true", xpath.booleanValueOf(this.currentPage));
        } catch (JaxenException e) {
            Assert.fail("Could not evaluate XPath '" + xpathExpression + "' (" + e.getMessage() + ")");
        }
    }

    public void assertSelectOptionCountByName(String formName, String selectFieldName, int optionCount) {
        Assert.assertEquals(optionCount, getFormByName(formName).getSelectByName(selectFieldName).getOptionSize());
    }

    public void assertSelectOptionCountById(String selectFieldId, int optionCount) {
        HtmlSelect select = (HtmlSelect) getHtmlElementById(selectFieldId);
        Assert.assertEquals(optionCount, select.getOptionSize());
    }

    public void assertSelectOptionIsSelectedById(String selectFieldId, String optionText) {
        HtmlSelect select = (HtmlSelect) getHtmlElementById(selectFieldId);
        assertSelectOptionIsSelected(select, optionText);
    }

    public void assertSelectOptionExistsByName(String formId, String selectFieldName, String optionText) {
        HtmlSelect select = getFormById(formId).getSelectByName(selectFieldName);
        findOptionInSelect(select, optionText);
    }

    public void assertSelectOptionExistsById(String optionId) {
        HtmlOption option = (HtmlOption) getHtmlElementById(optionId);
        Assert.assertNotNull(option);
    }

    public void assertSelectOptionExistsByIdAndText(String optionId, String text) {
        HtmlOption option = (HtmlOption) getHtmlElementById(optionId);
        Assert.assertEquals(text, option.getFirstChild().asText());
    }

    private void assertSelectOptionIsSelected(HtmlSelect select, String optionText) {
        HtmlOption option = findOptionInSelect(select, optionText);
        Assert.assertTrue(option.isSelected());
    }

    private HtmlOption findOptionInSelect(HtmlSelect select, String optionText) {
        List<?> options = select.getOptions();
        HtmlOption option = null;
        for (Iterator<?> iter = options.iterator(); iter.hasNext(); ) {
            option = (HtmlOption) iter.next();
            if (optionText.equals(option.asText())) {
                return option;
            }
        }
        Assert.fail("Option with text " + optionText + " could not be found in select " + select.getNameAttribute());
        return null;
    }

    public void assertTextFieldContentsById(String inputFieldId, String content) {
        String contentInPage = ((HtmlInput) getHtmlElementById(inputFieldId)).asText();
        Assert.assertEquals(content, contentInPage);
    }

    public void assertTextFieldContentsByName(String formName, String inputFieldName, String content) {
        String contentInPage = getFormByName(formName).getInputByName(inputFieldName).asText();
        Assert.assertEquals(content, contentInPage);
    }

    public void assertElementIdDoesNotExist(String elementId) {
        try {
            getHtmlElementById(elementId);
            Assert.fail("Element with id " + elementId + " exists.");
        } catch (Throwable t) {
        }
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public WebClient getClient() {
        return this.webClient;
    }

    public Page getCurrentPage() {
        return this.currentPage;
    }

    public HtmlElement getDocumentElement() {
        return ((HtmlPage) this.currentPage).getDocumentElement();
    }

    public void goToPage(final String path) throws MalformedURLException, IOException {
        setCurrentPage(this.webClient.getPage(new URL(this.baseURL, path)));
    }

    public void clickElementById(String id) throws IOException {
        ClickableElement link = (ClickableElement) getHtmlElementById(id);
        setCurrentPage(link.click());
    }

    public void assertText(String text, boolean regexp) {
        String foundText = getCurrentPage().getWebResponse().getContentAsString();
        if (regexp) {
            if (!regexpMatches(text, foundText)) {
                Assert.fail("Text '" + text + "' does not match anything on the page.");
            }
        } else {
            if (foundText.indexOf(text) == -1) {
                Assert.fail("Text '" + text + "' not found on the page.");
            }
        }
    }

    public void assertNoText(String text, boolean regexp) {
        try {
            assertText(text, regexp);
            Assert.fail("Text '" + text + "' was not expected, but was found on page.");
        } catch (Throwable t) {
        }
    }

    private boolean regexpMatches(String patternToMatch, String target) {
        return Pattern.compile(patternToMatch, Pattern.DOTALL).matcher(target).find();
    }

    public void assertTextStrings(Object[] names) {
        String searchTxt = "";
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                searchTxt += ".*";
            }
            searchTxt += names[i];
        }
        assertText(searchTxt, true);
    }

    public void goToPasswordPage() throws MalformedURLException, IOException {
        goToPage(Constants.PASSWORD_URL);
    }
}
