package net.lukemurphey.nsia.web.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.InputValidationException;
import net.lukemurphey.nsia.NoDatabaseConnectionException;
import net.lukemurphey.nsia.Wildcard;
import net.lukemurphey.nsia.SiteGroupManagement.SiteGroupDescriptor;
import net.lukemurphey.nsia.eventlog.EventLogField;
import net.lukemurphey.nsia.eventlog.EventLogMessage;
import net.lukemurphey.nsia.eventlog.EventLogField.FieldName;
import net.lukemurphey.nsia.eventlog.EventLogMessage.EventType;
import net.lukemurphey.nsia.scan.HttpSeekingScanRule;
import net.lukemurphey.nsia.scan.ScanRule;
import net.lukemurphey.nsia.web.RequestContext;
import net.lukemurphey.nsia.web.URLInvalidException;
import net.lukemurphey.nsia.web.View;
import net.lukemurphey.nsia.web.ViewFailedException;
import net.lukemurphey.nsia.web.ViewNotFoundException;
import net.lukemurphey.nsia.web.SessionMessages.MessageSeverity;
import net.lukemurphey.nsia.web.forms.Field;
import net.lukemurphey.nsia.web.forms.FieldError;
import net.lukemurphey.nsia.web.forms.FieldErrors;
import net.lukemurphey.nsia.web.forms.Form;
import net.lukemurphey.nsia.web.forms.IntegerValidator;
import net.lukemurphey.nsia.web.templates.TemplateLoader;

public class WebDiscoveryRuleEditView extends View {

    public static final String VIEW_NAME = "rule_editor_web_discovery";

    public WebDiscoveryRuleEditView() {
        super("Rule", VIEW_NAME, Pattern.compile("New|Edit", Pattern.CASE_INSENSITIVE), Pattern.compile("[0-9]*"));
    }

    public static String getURL(SiteGroupDescriptor siteGroup) throws URLInvalidException {
        WebDiscoveryRuleEditView view = new WebDiscoveryRuleEditView();
        return view.createURL("New") + "?SiteGroupID=" + siteGroup.getGroupId();
    }

    public static String getURL(int ruleID) throws URLInvalidException {
        WebDiscoveryRuleEditView view = new WebDiscoveryRuleEditView();
        return view.createURL("Edit", ruleID);
    }

    private static URL[] parseURLs(String addresses) throws IOException, InputValidationException {
        Vector<URL> urls = new Vector<URL>();
        BufferedReader reader = new BufferedReader(new StringReader(addresses));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                try {
                    urls.add(new URL(line));
                } catch (MalformedURLException e) {
                    throw new InputValidationException("The URL \"" + line + "\" is invalid", "URL", line);
                }
            }
        }
        URL[] urlsArray = new URL[urls.size()];
        urls.toArray(urlsArray);
        return urlsArray;
    }

    /**
	 * Get a form that can validate the rule.
	 * @return
	 */
    private Form getRuleForm() {
        Form form = new Form();
        form.addField(new Field("StartAddresses"));
        form.addField(new Field("ScanFrequencyUnits", new IntegerValidator(1, 86400)));
        form.addField(new Field("ScanFrequencyValue", new IntegerValidator(1, 1000000)));
        form.addField(new Field("Domain"));
        form.addField(new Field("RecursionDepth", new IntegerValidator(1, 1000)));
        form.addField(new Field("ScanLimit", new IntegerValidator(1, 20000)));
        return form;
    }

    private boolean performActions(HttpServletRequest request, HttpServletResponse response, RequestContext context, String[] args, Map<String, Object> data, HttpSeekingScanRule rule) throws ViewFailedException, URLInvalidException, IOException {
        Form form = getRuleForm();
        FieldErrors errors = form.validate(request);
        if (errors.size() > 0) {
            data.put("form_errors", errors);
        } else {
            Wildcard domain = new Wildcard(request.getParameter("Domain"));
            int scanFreqUnits = Integer.valueOf(request.getParameter("ScanFrequencyUnits"));
            int scanFreqValue = Integer.valueOf(request.getParameter("ScanFrequencyValue"));
            int scanFrequency = scanFreqUnits * scanFreqValue;
            boolean isNewRule = false;
            if (rule == null) {
                rule = new HttpSeekingScanRule(Application.getApplication(), domain, scanFrequency, true);
                isNewRule = true;
            } else {
                rule.setScanFrequency(scanFrequency);
                rule.setDomainRestriction(domain);
            }
            String startAddresses = request.getParameter("StartAddresses");
            if (startAddresses == null || startAddresses.length() == 0) {
                startAddresses = request.getParameter("StartAddresses2");
            }
            if (startAddresses == null) {
                errors = new FieldErrors();
                errors.put(new FieldError("StartAddresses", "", "The list of addresses to scan was not provided"));
                data.put("form_errors", errors);
                return false;
            }
            URL[] urls = null;
            try {
                urls = parseURLs(startAddresses);
            } catch (InputValidationException e) {
                errors = new FieldErrors();
                errors.put(new FieldError("StartAddresses", "", e.getMessage()));
                data.put("form_errors", errors);
                return false;
            } catch (IOException e) {
                errors = new FieldErrors();
                errors.put(new FieldError("StartAddresses", "", "The list of addresses to scan are not valid"));
                data.put("form_errors", errors);
                return false;
            }
            if (urls.length == 0) {
                errors = new FieldErrors();
                errors.put(new FieldError("StartAddresses", "", "No addresses to scan where provided (must provide at least one)"));
                data.put("form_errors", errors);
                return false;
            }
            rule.clearSeedUrls();
            rule.addSeedUrls(urls);
            rule.setRecursionDepth(Integer.valueOf(request.getParameter("RecursionDepth")));
            rule.setScanCountLimit(Integer.valueOf(request.getParameter("ScanLimit")));
            if (request.getParameter("ScanExternalURLs") != null) {
                rule.scanExternalLinks(true);
            } else {
                rule.scanExternalLinks(false);
            }
            try {
                if (isNewRule) {
                    int siteGroupID = Integer.valueOf(request.getParameter("SiteGroupID"));
                    long ruleID = rule.saveNewRuleToDatabase(siteGroupID);
                    Application.getApplication().logEvent(new EventLogMessage(EventType.RULE_ADDED, new EventLogField(FieldName.SOURCE_USER_NAME, context.getUser().getUserName()), new EventLogField(FieldName.SOURCE_USER_ID, context.getUser().getUserID()), new EventLogField(FieldName.RULE_ID, ruleID)));
                    context.addMessage("Rule successfully created", MessageSeverity.SUCCESS);
                    response.sendRedirect(SiteGroupView.getURL(siteGroupID));
                    return true;
                } else {
                    rule.saveToDatabase();
                    Application.getApplication().logEvent(new EventLogMessage(EventType.RULE_MODIFIED, new EventLogField(FieldName.SOURCE_USER_NAME, context.getUser().getUserName()), new EventLogField(FieldName.SOURCE_USER_ID, context.getUser().getUserID()), new EventLogField(FieldName.RULE_ID, rule.getRuleId())));
                    context.addMessage("Rule successfully updated", MessageSeverity.SUCCESS);
                    response.sendRedirect(SiteGroupView.getURL(ScanRule.getSiteGroupForRule(rule.getRuleId())));
                    return true;
                }
            } catch (IllegalStateException e) {
                throw new ViewFailedException(e);
            } catch (SQLException e) {
                throw new ViewFailedException(e);
            } catch (NoDatabaseConnectionException e) {
                throw new ViewFailedException(e);
            }
        }
        return false;
    }

    @Override
    protected boolean process(HttpServletRequest request, HttpServletResponse response, RequestContext context, String[] args, Map<String, Object> data) throws ViewFailedException, URLInvalidException, IOException, ViewNotFoundException {
        boolean viewHandled = false;
        HttpSeekingScanRule rule = null;
        if (data.get("rule") != null) {
            rule = (HttpSeekingScanRule) data.get("rule");
        }
        if (request.getMethod().equalsIgnoreCase("POST")) {
            viewHandled = performActions(request, response, context, args, data, rule);
        }
        if (viewHandled == false) {
            TemplateLoader.renderToResponse("WebDiscoveryRule.ftl", data, response);
        }
        return true;
    }
}
