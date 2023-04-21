package com.ail.openquote.ui.render;

import static com.ail.core.Functions.expand;
import static com.ail.openquote.ui.messages.I18N.i18n;
import static com.ail.openquote.ui.util.Functions.expandRelativeUrl;
import static com.ail.openquote.ui.util.Functions.findError;
import static com.ail.openquote.ui.util.Functions.findErrors;
import static com.ail.openquote.ui.util.Functions.hasErrorMarker;
import static com.ail.openquote.ui.util.Functions.hasErrorMarkers;
import static com.ail.openquote.ui.util.Functions.longDate;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.Behaviour;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.Clause;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.insurance.policy.SumBehaviour;
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotationSummary;
import com.ail.openquote.ui.Answer;
import com.ail.openquote.ui.AnswerScroller;
import com.ail.openquote.ui.AnswerSection;
import com.ail.openquote.ui.AssessmentSheetDetails;
import com.ail.openquote.ui.AttributeField;
import com.ail.openquote.ui.Blank;
import com.ail.openquote.ui.BrokerQuotationSummary;
import com.ail.openquote.ui.ClauseDetails;
import com.ail.openquote.ui.CommandButtonAction;
import com.ail.openquote.ui.InformationPage;
import com.ail.openquote.ui.Label;
import com.ail.openquote.ui.LoginSection;
import com.ail.openquote.ui.NavigationSection;
import com.ail.openquote.ui.Page;
import com.ail.openquote.ui.PageElement;
import com.ail.openquote.ui.PageScript;
import com.ail.openquote.ui.PageSection;
import com.ail.openquote.ui.ParsedUrlContent;
import com.ail.openquote.ui.PaymentDetails;
import com.ail.openquote.ui.PaymentOptionSelector;
import com.ail.openquote.ui.ProposerDetails;
import com.ail.openquote.ui.Question;
import com.ail.openquote.ui.QuestionPage;
import com.ail.openquote.ui.QuestionSection;
import com.ail.openquote.ui.QuestionSeparator;
import com.ail.openquote.ui.QuestionWithDetails;
import com.ail.openquote.ui.QuestionWithSubSection;
import com.ail.openquote.ui.QuotationSummary;
import com.ail.openquote.ui.ReferralSummary;
import com.ail.openquote.ui.RenderingError;
import com.ail.openquote.ui.RowScroller;
import com.ail.openquote.ui.SaveButtonAction;
import com.ail.openquote.ui.SavedQuotations;
import com.ail.openquote.ui.SectionScroller;
import com.ail.openquote.ui.ViewQuotationButtonAction;
import com.ail.openquote.ui.util.Choice;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.QuotationContext;

@SuppressWarnings("deprecation")
public class Xform extends Type implements Renderer {

    private static final long serialVersionUID = 2918957259222383330L;

    private RenderQuotationSummaryHelper renderQuotationSummaryHelper = new RenderQuotationSummaryHelper();

    private RenderAttributeFieldHelper renderAttributeFieldHelper = new RenderAttributeFieldHelper();

    public Type renderAnswer(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Answer answer, String title, String answerText) {
        w.printf("<group><label>%s</label><hint>%s</hint></group>", title, answerText);
        return model;
    }

    @SuppressWarnings("unchecked")
    public Type renderAnswerScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerScroller answerScroller) throws IOException {
        int rowCount = 0;
        for (Iterator it = model.xpathIterate(answerScroller.getBinding()); it.hasNext(); rowCount++) {
            Type t = (Type) it.next();
            for (Answer a : answerScroller.getAnswer()) {
                a.renderResponse(request, response, t);
            }
        }
        return model;
    }

    public Type renderAnswerSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerSection answerSection, String title) throws IOException {
        w.printf("<group>");
        if (title != null) {
            w.printf("<label>%s</label>", title);
        }
        for (Answer a : answerSection.getAnswer()) {
            model = a.renderResponse(request, response, model);
        }
        w.printf("</group>");
        return model;
    }

    public Type renderAssessmentSheetDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AssessmentSheetDetails assessmentSheetDetails) throws IOException {
        return model;
    }

    public Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad) throws IOException {
        return renderAttributeField(w, request, response, model, attributeField, boundTo, id, onChange, onLoad, "", "", "");
    }

    public Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad, String title, String styleClass, String ref) throws IOException {
        Pattern formattedCurrencyPattern = Pattern.compile("([^0-9,.']*)([0-9,.']*)([^0-9,.']*)");
        String onChangeEvent = (onChange != null) ? "onchange='" + onChange + "'" : "";
        String choiceTitle = "";
        if (title != "") {
            choiceTitle = title;
        } else {
            choiceTitle = attributeField.getTitle();
        }
        Attribute attr = (Attribute) model;
        try {
            String attrValue = "<value>" + attr.getValue() + "</value>";
            ;
            if (styleClass != null) {
                if (styleClass.contains("noValue")) {
                    styleClass = styleClass.replace("noValue", "");
                    attrValue = "<value />";
                }
            }
            String alert = "";
            String hint = "";
            String note = "";
            if (Functions.hasErrorMarkers(attr)) {
                for (Attribute at : attr.getAttribute()) {
                    if (at.getId().startsWith("error.")) {
                        alert = alert + "<alert>" + Functions.findError(at.getId().substring(6), model, attributeField) + "</alert>";
                    }
                }
            }
            if (attributeField.getHintText() != null) {
                hint = "<hint>" + attributeField.getHintText().getText() + "</hint>";
            }
            if (attributeField.getHelpText() != null) {
                note = "<help>" + attributeField.getHelpText() + "</help>";
            }
            if (model == null) {
                w.printf("<alert>undefined: %s</alert>", boundTo);
            } else {
                if (attr.isStringType()) {
                    String size = attr.getFormatOption("size");
                    size = (size != null) ? "size='" + size + "'" : "";
                    w.printf("<input ref=\"%s\" %s %s class='textbox %s'>", id, size, onChangeEvent, styleClass);
                    w.printf("<label>%s</label>", choiceTitle);
                    w.printf("%s", attrValue);
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</input>");
                } else if (attr.isNumberType()) {
                    String pattern = attr.getFormatOption("pattern");
                    String trailer = (attr.getFormat().endsWith("percent")) ? "%" : "";
                    int size = (pattern == null) ? 7 : pattern.length();
                    w.printf("<input ref=\"%s\" size=\"%s\" class='number num%s %s' trailer='%s'>", id, size, size, styleClass, trailer);
                    w.printf("<label>%s</label>", choiceTitle);
                    w.printf("%s", attrValue);
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</input>");
                } else if (attr.isCurrencyType()) {
                    String value, pre, post;
                    try {
                        Matcher m = formattedCurrencyPattern.matcher(attr.getFormattedValue());
                        m.matches();
                        pre = m.group(1);
                        value = m.group(2);
                        post = m.group(3);
                    } catch (Throwable e) {
                        pre = "&nbsp;";
                        value = attr.getValue();
                        post = attr.getUnit();
                    }
                    w.printf("<input ref=\"%s\" %s size='7' class='number %s' unit='%s' pre=\"%s\" post=\"%s\">", id, onChangeEvent, styleClass, attr.getUnit(), pre, post);
                    w.printf("<label>%s</label>", choiceTitle);
                    w.printf("<value>%s</value>", value);
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</input>");
                } else if (attr.isChoiceMasterType()) {
                    w.printf("<select1 ref=\"%s\" appearance=\"%s\" class=\"master %s\">", id, attributeField.getRenderHint(), styleClass);
                    w.printf("<label>%s</label>", choiceTitle);
                    w.printf("%s", attrValue);
                    String optionTypeName = attr.getFormatOption("type");
                    Choice choice = (Choice) (new CoreProxy().newProductType(QuotationContext.getQuotation().getProductTypeId(), optionTypeName));
                    w.print(choice.renderAsXmlArray(optionTypeName));
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</select1>");
                } else if (attr.isChoiceSlaveType()) {
                    w.printf("<select1 ref=\"%s\" appearance=\"%s\" class=\"slave %s\">", id, attributeField.getRenderHint(), styleClass);
                    w.printf("<label>%s</label>", choiceTitle);
                    w.printf("%s", attrValue);
                    String optionTypeName = attr.getFormatOption("type");
                    Choice choice = (Choice) (new CoreProxy().newProductType(QuotationContext.getQuotation().getProductTypeId(), optionTypeName));
                    w.print(choice.renderAsXmlArray(optionTypeName));
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</select1>");
                } else if (attr.isChoiceType()) {
                    if (attr.getFormatOption("type") == null) {
                        if ("radio".equals(attributeField.getRenderHint())) {
                            String[] opts = attr.getFormatOption("options").split("[|#]");
                            for (int i = 1; i < opts.length; i += 2) {
                                if (!"?".equals(opts[i])) {
                                    w.printf("<input type='radio' name=\"%s\" value='%s' %s>", id, opts[i], (opts[i].equals(attr.getValue())) ? "checked='checked'" : "");
                                    w.printf("%s", opts[i]);
                                    w.printf("%s", hint);
                                    w.printf("%s", note);
                                    w.printf("%s", alert);
                                    w.printf("</input>");
                                }
                            }
                        } else {
                            w.printf("<select1 ref=\"%s\" class='keep_empty %s' appearance=\"%s\">", id, styleClass, attributeField.getRenderHint());
                            w.printf("%s", attrValue);
                            w.printf("<label>%s</label>", choiceTitle);
                            w.printf("%s", renderAttributeFieldHelper.renderEnumerationAsOptions(attr.getFormatOption("options"), attr.getValue()));
                            w.printf("%s", hint);
                            w.printf("%s", note);
                            w.printf("%s", alert);
                            w.printf("</select1>");
                        }
                    } else {
                        String optionTypeName = attr.getFormatOption("type");
                        Choice choice = (Choice) (new CoreProxy().newProductType(QuotationContext.getQuotation().getProductTypeId(), optionTypeName));
                        String classAttribute = "";
                        if (styleClass != "") {
                            classAttribute = "class=\"" + styleClass + "\"";
                        }
                        w.printf("<select1 ref=\"%s\" appearance=\"%s\" %s><label>%s</label>", id, attributeField.getRenderHint(), classAttribute, choiceTitle);
                        for (Choice c : choice.getChoice()) {
                            w.printf("<item>");
                            w.printf("<label>%s</label>", c.getName());
                            w.printf("<value>%s</value>", c.getName());
                            w.printf("</item>");
                        }
                        w.printf("%s", hint);
                        w.printf("%s", note);
                        w.printf("%s", alert);
                        w.printf("</select1>");
                    }
                } else if (attr.isDateType()) {
                    String dateClass = "";
                    if (styleClass != null) {
                        dateClass = styleClass;
                    } else if (attributeField.getStyleClass() != null) {
                        dateClass = attributeField.getStyleClass();
                    }
                    w.printf("<input ref=\"%s\" class=\"calendar short %s\">", id, dateClass);
                    w.printf("<label>%s</label>", choiceTitle);
                    if (attr.getValue().equals("[today]")) {
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String datetime = dateFormat.format(date);
                        w.printf("<value>%s</value>", datetime);
                    } else {
                        w.printf("%s", attrValue);
                    }
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</input>");
                } else if (attr.isYesornoType()) {
                    String classAttribute = "";
                    if (styleClass != "") {
                        classAttribute = "class=\"" + styleClass + "\"";
                    }
                    if ("checkbox".equals(attributeField.getRenderHint())) {
                        w.printf("<select ref=\"%s\" appearance=\"full\" %s>", id, classAttribute);
                        w.printf("<label>%s</label>", choiceTitle);
                        w.printf("%s", attrValue);
                        w.printf("<item><label>Yes</label><value>Yes</value></item>");
                        w.printf("%s", hint);
                        w.printf("%s", note);
                        w.printf("%s", alert);
                        w.printf("</select>");
                    } else if ("radio".equals(attributeField.getRenderHint())) {
                        w.printf("<select1 ref=\"%s\" appearance=\"full\" %s>", id, classAttribute);
                        w.printf("<label>%s</label>", choiceTitle);
                        w.printf("%s", attrValue);
                        w.printf("<item><label>Yes</label><value>Yes</value></item>");
                        w.printf("<item><label>No</label><value>No</value></item>");
                        w.printf("%s", hint);
                        w.printf("%s", note);
                        w.printf("%s", alert);
                        w.printf("</select1>");
                    } else {
                        w.printf("<select1 ref=\"%s\" appearance=\"full\" %s>", id, classAttribute);
                        w.printf("<label>%s</label>", choiceTitle);
                        w.printf("%s", attrValue);
                        w.printf("<item><label>Yes</label><value>Yes</value></item>");
                        w.printf("<item><label>No</label><value>No</value></item>");
                        w.printf("%s", hint);
                        w.printf("%s", note);
                        w.printf("%s", alert);
                        w.printf("</select1>");
                    }
                } else if (attr.isNoteType()) {
                    w.printf("<textarea name=\"%s\" class='portlet-form-input-field %s' %s rows='3' style='width:100%%'>", id, styleClass, onChangeEvent, attr.getValue());
                    w.printf("<label>%s</label>", choiceTitle);
                    w.printf("%s", attrValue);
                    w.printf("%s", hint);
                    w.printf("%s", note);
                    w.printf("%s", alert);
                    w.printf("</textarea>");
                }
            }
        } catch (Throwable t) {
            throw new RenderingError("Failed to render attribute id:'" + attr.getId() + "', format:'" + attr.getFormat() + "' value:'" + attr.getValue() + "'", t);
        }
        return model;
    }

    public Type renderAttributeFieldPageLevel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id) throws IOException {
        return model;
    }

    public Type renderBlank(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Blank blank) throws IOException {
        return model;
    }

    public Type renderBrokerQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Type model, BrokerQuotationSummary brokerQuotationSummary) {
        return model;
    }

    public Type renderCommandButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label, boolean immediate) {
        w.printf("<submit submission='' ref='op=%s:immediate=%b' class='principle'><label>%s</label></submit>", label, immediate, i18n(label));
        return model;
    }

    public Type renderInformationPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, InformationPage informationPage, String title, List<PageElement> pageElements) throws IOException {
        w.printf("<div class='xform'>");
        w.printf("<model>");
        w.printf("<instance>%s</instance>", new CoreProxy().toXML(model));
        w.printf("<submission id='%s' action='%s' method='post' event=\"return form_check(this);\"/>", informationPage.getId(), response.createActionURL());
        w.printf("</model>");
        w.printf("<group ref='%s' class=\"OpenQuote\">", informationPage.getId());
        if (title != null) {
            w.printf("  <label>%s</label>", title);
        }
        for (PageElement e : informationPage.getPageElement()) {
            model = e.renderResponse(request, response, model);
        }
        w.printf("</group>");
        w.printf("</div>");
        return model;
    }

    public Type renderLabel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Label label, String format, Object[] params) {
        w.printf("<label>%s</label>", format);
        return model;
    }

    public Type renderLoginSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, LoginSection loginSection, String usernameGuess, String nameOfForwardToPortal) {
        String lnk = "<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Create Login\");'>" + loginSection.getInvitationLinkText() + "</a>";
        w.printf("<group id='Proposer Login'>");
        w.printf(loginSection.getInvitationMessageText(), lnk);
        w.printf("<form method='post' action='%s' name='loginform' id='loginForm'>", response.createActionURL());
        w.printf("<label>Email address:</label>");
        w.printf("<input class='text' type='text' ref='username' id='username' value='%s'/>", usernameGuess);
        w.printf("<alert>%s</alert>", findError("username", model, loginSection));
        w.printf("<label>Password:</label>");
        w.printf("<input class='text' type='password' ref='password' id='password' value=''/>");
        w.printf("<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Forgotten Password\");'>Forgotten password?</a>");
        w.printf("<submit submission='' id='loginButton' class='principle' ref='op=%1$s:page=%2$s:portal=%3$s'><label>%1$s</label></submit>", loginSection.getLoginButtonLabel(), loginSection.getForwardToPageName(), nameOfForwardToPortal);
        w.printf("</form>");
        w.printf("</group>");
        w.printf("<group id='Create Login'>");
        w.printf("<label>Create a new account here. If you have an existing account, please <a onClick='showDivDisplay(\"Proposer Login\");hideDivDisplay(\"Create Login\");'>login here</a>.</label>");
        w.printf("<label>Email address:</label>");
        w.printf("<input class='text' ref='username' value=''/>");
        w.printf("<alert>%s</alert>", findError("username", model, loginSection));
        w.printf("<label>Confirm email address:</label>");
        w.printf("<input class='text' ref='cusername' value=''/>");
        w.printf("<alert>%s</alert>", findError("cusername", model, loginSection));
        w.printf("<label>Password:</label>");
        w.printf("<input class='password' ref='password' value=''/>");
        w.printf("<alert>%s</alert>", findError("password", model, loginSection));
        w.printf("<label>Confirm password:</label>");
        w.printf("<input class='password' ref='cpassword' value=''/>");
        w.printf("<alert>%s</alert>", findError("cpassword", model, loginSection));
        w.printf("<submit ref='op=Create:page=%s:portal=%s'><label>Create & Save</label></submit>", loginSection.getForwardToPageName(), nameOfForwardToPortal);
        w.printf("</group>");
        w.printf("<group id='Forgotten Password'>");
        w.printf("<label>Enter your email address below and your password will be emailed to you.</label>");
        w.printf("<label>Email address:</label>");
        w.printf("<input class='text' ref='username' value='%s'/>", usernameGuess);
        w.printf("<submit ref='op=Reminder'><label>Send Reminder</label></submit>");
        w.printf("</group>");
        w.printf("<script type='text/javascript'>");
        if (hasErrorMarker("create", model)) {
            w.printf("hideDivDisplay('Forgotten Password');");
            w.printf("hideDivDisplay('Proposer Login');");
            w.printf("showDivDisplay('Create Login');");
        } else if (hasErrorMarker("login", model)) {
            w.printf("hideDivDisplay('Create Login');");
            w.printf("hideDivDisplay('Forgotten Password');");
            w.printf("showDivDisplay('Proposer Login');");
        } else {
            w.printf("hideDivDisplay('Create Login');");
            w.printf("hideDivDisplay('Forgotten Password');");
            w.printf("hideDivDisplay('Proposer Login');");
        }
        w.printf("</script>");
        return model;
    }

    public Type renderNavigationSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, NavigationSection navigationSection) throws IllegalStateException, IOException {
        for (PageElement element : navigationSection.getPageElement()) {
            model = element.renderResponse(request, response, model);
        }
        if (navigationSection.isQuitDisabled()) {
            w.print("&nbsp;");
        } else {
            model = navigationSection.getQuitButton().renderResponse(request, response, model);
        }
        return model;
    }

    public Type renderPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Page page) {
        return model;
    }

    public Type renderPageScriptHeader(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageScript pageScript) {
        return model;
    }

    public Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title) throws IllegalStateException, IOException {
        return renderPageSection(w, request, response, model, pageSection, title, "", "");
    }

    public Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title, String styleClass, String ref) throws IllegalStateException, IOException {
        String styleClassOutput = "";
        String refOutput = "";
        if (styleClass != null) {
            styleClassOutput = "class=\"" + styleClass + "\"";
        }
        if (ref != null) {
            refOutput = "ref=\"" + ref + "\"";
        }
        w.printf("<group %s %s>", styleClassOutput, refOutput);
        if (!Functions.isEmpty(title)) {
            w.printf("<label>%s</label>", title);
        }
        Iterator<? extends PageElement> it = pageSection.getPageElement().iterator();
        while (it.hasNext()) {
            for (int col = 0; col < pageSection.getColumns(); col++) {
                if (it.hasNext()) {
                    model = it.next().renderResponse(request, response, model);
                } else {
                    w.printf("&nbsp;");
                }
            }
        }
        w.printf("</group>");
        return model;
    }

    public Type renderParsedUrlContent(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ParsedUrlContent parsedUrlContent, String content) {
        w.print(content);
        return model;
    }

    public Type renderPaymentDetails(PrintWriter w, RenderRequest request, RenderResponse response, Quotation model, PaymentDetails paymentDetails) {
        return model;
    }

    public Quotation renderPaymentOptionSelector(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quotation, PaymentOptionSelector paymentOptionSelector) {
        return quotation;
    }

    public Proposer renderProposerDetails(PrintWriter w, RenderRequest request, RenderResponse response, Proposer proposer, ProposerDetails proposerDetails) {
        return proposer;
    }

    public Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext) throws IllegalStateException, IOException {
        return renderQuestion(w, request, response, model, question, title, rowContext, "", "");
    }

    public Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext, String styleClass, String ref) throws IllegalStateException, IOException {
        String renderQ = "";
        renderQ = question.renderAttribute(request, response, model, question.getBinding(), rowContext, question.getOnChange(), question.getOnLoad());
        if (!renderQ.equals("")) {
            w.printf(renderQ);
        } else {
            if (title != null) {
                w.printf("<group><label>" + title + "</label></group>");
            }
        }
        return model;
    }

    public Type renderQuestionPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionPage questionPage, String title) throws IllegalStateException, IOException {
        w.printf("<div class='xform'>");
        w.printf("<model>");
        w.printf("<instance>%s</instance>", new CoreProxy().toXML(model));
        w.printf("<submission id='%s' action='%s' method='post' event=\"return form_check(this);\"/>", questionPage.getId(), response.createActionURL());
        w.printf("</model>");
        w.printf("<group ref='%s' class=\"OpenQuote\">", questionPage.getId());
        if (title != null) {
            w.printf("  <label>%s</label>", title);
        }
        for (PageElement e : questionPage.getPageElement()) {
            model = e.renderResponse(request, response, model);
        }
        w.printf("</group>");
        w.printf("</div>");
        return model;
    }

    public Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title) throws IllegalStateException, IOException {
        return renderQuestionSection(w, request, response, model, questionSection, title, "", "");
    }

    public Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title, String styleClass, String ref) throws IllegalStateException, IOException {
        String styleClassOutput = "";
        String refOutput = "";
        if (styleClass != null) {
            styleClassOutput = "class=\"" + styleClass + "\"";
        }
        if (ref != null) {
            refOutput = "ref=\"" + ref + "\"";
        }
        w.printf("<group %s %s>", styleClassOutput, refOutput);
        if (title != null) {
            w.printf("<label>%s</label>", title);
        }
        Iterator<? extends Question> it = questionSection.getQuestion().iterator();
        while (it.hasNext()) {
            it.next().renderResponse(request, response, model);
        }
        w.printf("</group>");
        return model;
    }

    public Type renderQuestionSeparator(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSeparator questionSeparator, String title) {
        if (title == null) {
            if (questionSeparator.getBinding() != null && hasErrorMarkers(model.xpathGet(questionSeparator.getBinding(), Type.class))) {
                w.printf("<td class='portlet-section-subheader' colspan='4'>%s</td>", findErrors(model.xpathGet(questionSeparator.getBinding(), Type.class), questionSeparator));
            } else {
                w.printf("<td class='portlet-section-subheader' colspan='4'>&nbsp;</td>");
            }
        } else {
            w.printf("<td colspan='4'><table width='100%%''>");
            w.printf("<tr><td class='portlet-section-subheader' colspan='4'>%s</td></tr>", Functions.hideNull(title));
            if (questionSeparator.getBinding() != null && hasErrorMarkers(model.xpathGet(questionSeparator.getBinding(), Type.class))) {
                w.printf("<tr><td class='portlet-msg-error' colspan='4'>%s</td>", findErrors(model.xpathGet(questionSeparator.getBinding(), Type.class), questionSeparator));
            }
            w.printf("</table></td>");
        }
        return model;
    }

    public Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId) throws IllegalStateException, IOException {
        return renderQuestionWithDetails(w, request, response, model, questionWithDetails, title, detailTitle, rowContext, questionId, detailId, "", "");
    }

    public Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId, String styleClass, String ref) throws IllegalStateException, IOException {
        String insertClass = "";
        if (styleClass != null) {
            insertClass = "class=\"" + styleClass + "\"";
        }
        Attribute attr = (com.ail.core.Attribute) model.xpathGet(questionWithDetails.getBinding());
        w.printf("<select1 ref=\"%s\" appearance=\"full\" %s >", questionId, insertClass);
        w.printf("<label>%s</label>", title);
        w.printf("<value>%s</value>", attr.getValue());
        w.printf("<item><label>Yes</label><value>Yes</value><toggle case=\"%s_1\" event=\"DOMActivate\" /></item>", questionId);
        w.printf("<item><label>No</label><value>No</value><toggle case=\"%s_0\" event=\"DOMActivate\" /></item>", questionId);
        w.printf("</select1>");
        w.printf("<switch class=\"disable\"><case id=\"%s_0\">", questionId);
        w.printf("</case>");
        w.printf("<case id=\"%s_1\">", questionId);
        w.printf("%s", questionWithDetails.renderAttributeWithTitle(request, response, model, questionWithDetails.getDetailsBinding(), rowContext, questionWithDetails.getOnChange(), questionWithDetails.getOnLoad(), detailTitle));
        w.printf("</case></switch>");
        return model;
    }

    public Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId) throws IllegalStateException, IOException {
        return renderQuestionWithSubSection(w, request, response, model, questionWithSubSection, title, rowContext, questionId, "", "");
    }

    public Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId, String styleClass, String ref) throws IllegalStateException, IOException {
        String insertClass = "";
        if (styleClass != null) {
            insertClass = "class=\"" + styleClass + "\"";
        }
        Attribute attr = (com.ail.core.Attribute) model.xpathGet(questionWithSubSection.getBinding());
        w.printf("<select1 ref=\"%s\" appearance=\"full\" %s >", questionId, insertClass);
        w.printf("<label>%s</label>", title);
        w.printf("<value>%s</value>", attr.getValue());
        w.printf("<item><label>Yes</label><value>Yes</value><toggle case=\"%s_1\" event=\"DOMActivate\" /></item>", questionId);
        w.printf("<item><label>No</label><value>No</value><toggle case=\"%s_0\" event=\"DOMActivate\" /></item>", questionId);
        w.printf("</select1>");
        w.printf("<switch><case id=\"%s_0\" class=\"hide\">", questionId);
        w.printf("</case>");
        w.printf("<case id=\"%s_1\">", questionId);
        model = questionWithSubSection.getSubSection().renderResponse(request, response, model);
        w.printf("</case></switch>");
        return model;
    }

    public Type renderQuitButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
        w.printf("<submit submission='' ref='op=quit:immediate=true'><label>%1$s</label></submit>", label);
        return model;
    }

    public Quotation renderQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
        w.printf("<QuotationTest/>");
        w.printf("<form name='%s' action='%s' method='post'>", quotationSummary.getId(), response.createActionURL());
        w.printf(" <div>");
        w.printf("  <div>");
        renderQuotationSummaryHelper.renderPremiumSummary(w, request, response, quote, quotationSummary);
        w.printf("  </div>");
        w.printf("  <div>");
        renderQuotationSummaryHelper.renderCoverSummary(w, request, response, quote, quotationSummary);
        w.printf("  </div>");
        w.printf(" </div>");
        w.printf(" <div>");
        w.printf("  <div>");
        renderQuotationSummaryHelper.renderTermsAndConditions(w, request, quote, quotationSummary);
        w.printf("  </div>");
        w.printf(" </div>");
        w.printf("</form>");
        return quote;
    }

    public Quotation renderReferralSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, ReferralSummary referralSummary) throws IOException {
        w.printf("<label>");
        referralSummary.getReferralNotificationSection().renderResponse(request, response, quote);
        w.printf("</label>");
        referralSummary.getNavigationSection().renderResponse(request, response, quote);
        for (PageElement e : referralSummary.getPageElement()) {
            if (e instanceof AnswerSection) {
                e.renderResponse(request, response, quote);
            }
        }
        return quote;
    }

    public Type renderRequoteButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
        w.printf("<submit submission='' ref='op=requote'><label>%1$s</label></submit>", label);
        return model;
    }

    @SuppressWarnings("unchecked")
    public Type renderRowScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, RowScroller rowScroller) throws IllegalStateException, IOException {
        w.printf("<group class=\"horizontal_cols\">");
        if (rowScroller.getTitle() != null) {
            w.print(rowScroller.getExpandedRepeatedTitle(model));
        }
        int rowCount = 0;
        for (Iterator it = model.xpathIterate(rowScroller.getBinding()); it.hasNext(); ) {
            Type t = (Type) it.next();
            w.printf("<repeat bind=\"%s\">", rowScroller.getId());
            for (AttributeField a : rowScroller.getItem()) {
                a.renderResponse(request, response, t, rowScroller.getBinding() + "[" + rowCount + "]");
            }
            if (rowScroller.isAddAndDeleteEnabled()) {
                if (rowCount >= rowScroller.getMinRows()) {
                    w.printf("<trigger class=\"delete\">");
                    w.printf("<label>Delete</label>");
                    w.printf("<insert position=\"after\" bind=\"op=delete:id=%s:row=%d:immediate=true:\" class=\"add\"/>", rowScroller.getId(), rowCount);
                    w.printf("</trigger>");
                }
            }
            w.printf("</repeat>");
            rowCount++;
        }
        if (rowScroller.isAddAndDeleteEnabled()) {
            if (rowScroller.getMaxRows() != -1 && rowCount == rowScroller.getMaxRows()) {
                w.printf("<trigger class=\"disabled\">");
                w.printf("<label>Add</label>");
                w.printf("<insert position=\"after\" bind=\"op=add:id=%s:immediate=true:\" class=\"add\"/>", rowScroller.getId());
                w.printf("</trigger>");
            } else {
                w.printf("<trigger>");
                w.printf("<label>Add</label>");
                w.printf("<insert position=\"after\" bind=\"op=add:id=%s:immediate=true:\" class=\"add\"/>", rowScroller.getId());
                w.printf("</trigger>");
            }
        }
        w.printf("</group>");
        return model;
    }

    public Type renderSaveButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SaveButtonAction saveButtonAction, String label, boolean remoteUser) {
        if (remoteUser) {
            w.printf("<submit submission='' ref='op=%1$s'><label>%1$s</label></submit>", label);
        } else {
            w.printf("<submit submission='' onClick='%s' ref='op=%2$s'><label>%2$s</label></submit>", LoginSection.reset, label);
        }
        return model;
    }

    public List<?> renderSaveQuotations(PrintWriter w, RenderRequest request, RenderResponse response, List<?> quotationSummaries, SavedQuotations savedQuotations) throws IllegalStateException, IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM, yyyy");
        w.printf("<table width='100%%' border='0' cols='5'>");
        w.printf("<tr><td cols='5'>" + i18n("i18n_saved_quotations_title") + "</td></tr>", quotationSummaries.size() == 1 ? "quote" : "quotes");
        w.printf("<tr><td height='10' cols='5'/></tr>");
        w.printf("<tr class='portlet-font'>");
        w.printf("<td align='center' class='portlet-section-alternate'>" + i18n("i18n_saved_quotations_quote_number_heading") + "</td>");
        w.printf("<td align='center' class='portlet-section-alternate'>" + i18n("i18n_saved_quotations_quote_date_heading") + "</td>");
        w.printf("<td align='center' class='portlet-section-alternate'>" + i18n("i18n_saved_quotations_expiry_date_heading") + "</td>");
        w.printf("<td align='center' class='portlet-section-alternate'>" + i18n("i18n_saved_quotations_premium_heading") + "</td>");
        w.printf("<td class='portlet-section-alternate'>&nbsp</td>");
        w.printf("</tr>");
        for (Object o : quotationSummaries) {
            SavedQuotationSummary savedQuote = (SavedQuotationSummary) o;
            w.printf("<tr>");
            w.printf("<td align='center' class='portal-form-label'>%s</td>", savedQuote.getQuotationNumber());
            w.printf("<td align='center' class='portal-form-label'>%s</td>", dateFormat.format(savedQuote.getQuotationDate()));
            w.printf("<td align='center' class='portal-form-label'>%s</td>", dateFormat.format(savedQuote.getQuotationExpiryDate()));
            w.printf("<td align='center' class='portal-form-label'>%s</td>", savedQuote.getPremium().toFormattedString());
            w.printf("<td align='left'>");
            w.printf("<input type='submit' name='op=confirm:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(savedQuotations.getConfirmAndPayLabel()));
            w.printf("<input type='submit' name='op=requote:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(savedQuotations.getRequoteLabel()));
            savedQuotations.getViewQuotationButtonAction().renderResponse(request, response, savedQuote);
            w.printf("</td>");
            w.printf("</tr>");
        }
        w.printf("</table>");
        return quotationSummaries;
    }

    public Type renderSaveQuotationsFooter(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SavedQuotations savedQuotations) {
        return model;
    }

    @SuppressWarnings("unchecked")
    public Type renderSectionScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SectionScroller sectionScroller) throws IllegalStateException, IOException {
        if (sectionScroller.getTitle() != null) {
            w.print(i18n(sectionScroller.getExpandedRepeatedTitle(model)));
        }
        int rowCount = 0;
        for (Iterator<Type> it = (Iterator<Type>) model.xpathIterate(sectionScroller.getBinding()); it.hasNext(); rowCount++) {
            Type t = it.next();
            if (sectionScroller.getSectionTitle() != null) {
                sectionScroller.getSectionTitle().renderResponse(request, response, t);
            }
            if (sectionScroller.getRepeatedTitle() != null) {
                w.print(i18n(sectionScroller.getExpandedRepeatedTitle(t)));
            }
            for (Iterator<AttributeField> question = sectionScroller.getItem().iterator(); question.hasNext(); ) {
                question.next().renderResponse(request, response, t, sectionScroller.getBinding() + "[" + rowCount + "]");
            }
        }
        return model;
    }

    public Type renderViewQuotationButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ViewQuotationButtonAction viewQuotationButtonAction, String quoteNumber, String label) {
        w.printf("<submit submission='' ref='op=%1$s:id=%2$s'><label>%1$s</label></submit>", label, quoteNumber);
        return model;
    }

    public Type renderClauseDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ClauseDetails clauseDetails, String title, Map<String, List<Clause>> groupedClauses) {
        return null;
    }

    private class RenderQuotationSummaryHelper {

        private void renderPremiumSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
            CurrencyAmount premium = quote.getTotalPremium();
            w.printf("<QuotationTest2/>");
            w.printf("<table width='100%%'>");
            w.printf("   <tr valign='middle' class='portlet-table-subheader'><td>" + i18n("i18n_quotation_summary_quote_message") + "</td></tr>", premium.toFormattedString());
            w.printf("   <tr>");
            w.printf("       <td height='15'></td>");
            w.printf("   </tr>");
            w.printf("   <tr>");
            w.printf("       <td class='portlet-font'>");
            w.printf("           <ul>");
            w.printf("               <li>" + i18n("i18n_quotation_summary_quote_number_message") + "</li>", quote.getQuotationNumber());
            w.printf("               <li>" + i18n("i18n_quotation_summary_valid_until_message") + "</li>", longDate(quote.getQuotationExpiryDate()));
            renderTaxSummary(w, quote);
            if (quotationSummary.getWordingsUrl() != null) {
                w.printf("               <li>" + i18n("i18n_quotation_summary_sample_wording_message") + "</li>", expandRelativeUrl(quotationSummary.getWordingsUrl(), request, quote.getProductTypeId()));
            }
            w.printf("           </ul>");
            w.printf("       </td>");
            w.printf("   </tr>");
            if (quotationSummary.getPremiumSummaryFooter() != null) {
                w.printf("<tr>");
                w.printf("<td class='portlet-font'>");
                quotationSummary.getPremiumSummaryFooter().renderResponse(request, response, quote);
                w.printf("</td>");
                w.printf("</tr>");
            }
            w.printf("<tr>");
            w.printf("<td class='portlet-font'>");
            quotationSummary.navigationSection().renderResponse(request, response, quote);
            w.printf("</td>");
            w.printf("</tr>");
            w.printf("<tr>");
            w.printf("<td>");
            quotationSummary.loginSection(quote).renderResponse(request, response, quote);
            w.printf("</td>");
            w.printf("</tr>");
            w.printf("</table>");
        }

        /**
    	 * Render the tax panel. There are two formats here: If there is just one tax, we want to display something like:
    	 * <p><b>This premium is inclusive of IPT at 5%</b></p>
    	 * <p>If there is more than one tax, it is broken out into a list:</p>
    	 * <p><b>This premium is inclusive of:<ul>
    	 * <li>IPT at 5%</li>
    	 * <li>Stamp duty of �3.00</li>
    	 * </ul></b></p>
    	 * @param w
    	 * @param quote
    	 */
        private void renderTaxSummary(PrintWriter w, Quotation quote) {
            Collection<Behaviour> taxLines = quote.getAssessmentSheet().getLinesOfBehaviourType(BehaviourType.TAX).values();
            if (taxLines.size() == 1) {
                Behaviour taxLine = taxLines.iterator().next();
                w.printf("<label>" + i18n("i18n_quotation_summary_inclusive_header_message") + " ");
                if (taxLine instanceof RateBehaviour) {
                    w.printf(i18n("i18n_quotation_summary_inclusive_rate_message"), taxLine.getReason(), ((RateBehaviour) taxLine).getRate().getRate());
                } else if (taxLine instanceof SumBehaviour) {
                    w.printf(i18n("i18n_quotation_summary_inclusive_sum_message"), taxLine.getReason(), ((SumBehaviour) taxLine).getAmount().toFormattedString());
                }
                w.printf("</label>");
            } else if (taxLines.size() > 1) {
                w.printf("<label>" + i18n("i18n_quotation_summary_inclusive_header_message") + ":");
                for (Behaviour taxLine : taxLines) {
                    if (taxLine instanceof RateBehaviour) {
                        w.printf("<label>" + i18n("i18n_quotation_summary_inclusive_rate_message") + "</label>", taxLine.getReason(), ((RateBehaviour) taxLine).getRate().getRate());
                    } else if (taxLine instanceof SumBehaviour) {
                        w.printf("<label>" + i18n("i18n_quotation_summary_inclusive_sum_message") + "</label>", taxLine.getReason(), ((SumBehaviour) taxLine).getAmount().toFormattedString());
                    }
                }
                w.printf("</label>");
            }
        }

        private void renderCoverSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
            for (PageElement e : quotationSummary.getPageElement()) {
                if (e instanceof AnswerSection) {
                    w.printf("<label>");
                    e.renderResponse(request, response, quote);
                    w.printf("<label>");
                }
            }
        }

        private void renderTermsAndConditions(PrintWriter w, RenderRequest request, Quotation quote, QuotationSummary quotationSummary) {
            if (quotationSummary.getTermsAndConditionsUrl() != null) {
                String fullUrl = expandRelativeUrl(quotationSummary.getTermsAndConditionsUrl(), request, quote.getProductTypeId());
                w.printf("<label>");
                try {
                    expand(w, new URL(fullUrl), quote);
                } catch (MalformedURLException e) {
                    w.printf(i18n("i18n_quotation_summary_missing_tandc_message"), quote.getBroker().getQuoteEmailAddress());
                    new CoreProxy().logError("Failed to display terms and conditions for quote: '" + quote.getQuotationNumber() + "', product: '" + quote.getProductTypeId() + "' url:'" + fullUrl + "'");
                }
                w.printf("</label>");
            }
        }
    }

    private class RenderAttributeFieldHelper {

        /**
	     * Render an AttributeField's choice list as a set of HTML options for use in a select element.
	     * See {@link com.ail.core.Attribute} for details of the choice format.
	     * @param format Choice string
	     * @param selected The value of the option to show as selected, or null if no value is selected.
	     * @return Option line as a string.
	     */
        private String renderEnumerationAsOptions(String format, String selected) {
            StringBuffer ret = new StringBuffer();
            String[] opts = format.split("[|#]");
            for (int i = 1; i < opts.length; i += 2) {
                if (opts[i].equals(selected)) {
                    ret.append("<item><label>" + opts[i] + "</label><value>" + opts[i] + "</value></item>");
                } else {
                    ret.append("<item><label>" + opts[i] + "</label><value>" + opts[i] + "</value></item>");
                }
            }
            return ret.toString();
        }
    }
}
