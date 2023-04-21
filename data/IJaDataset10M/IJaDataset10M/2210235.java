package org.torweg.pulse.component.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import javax.mail.internet.InternetAddress;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.bundle.Controller;
import org.torweg.pulse.component.cms.model.CMSContent;
import org.torweg.pulse.component.cms.model.Page;
import org.torweg.pulse.configuration.ConfigBean;
import org.torweg.pulse.configuration.DeprecatedConfigurable;
import org.torweg.pulse.email.Email;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.NotFoundEvent;
import org.torweg.pulse.service.event.RedirectEvent;
import org.torweg.pulse.service.request.Command;
import org.torweg.pulse.service.request.Parameter;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.site.content.AbstractBasicContent;
import org.torweg.pulse.site.content.AbstractContentDisplayer;
import org.torweg.pulse.util.xml.transform.XSLTOutputter;

/**
 * is a {@code Controller} to convert an HTML form to an e-mail.
 * <p>
 * TODO
 * </p>
 * 
 * @author Thomas Weber
 * @version $Revision: 2072 $
 */
public class CMSFormMailer extends Controller implements DeprecatedConfigurable {

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CMSFormMailer.class);

    /**
	 * the configuration.
	 */
    private CMSFormMailerConfig config;

    /**
	 * processes the request.
	 * 
	 * @param request
	 *            the current request
	 * @return the result of the checks
	 */
    @Action("sendMail")
    @Permission("sendMail")
    public final CMSContentDisplayerResult sendMail(final ServiceRequest request) {
        Command command = request.getCommand();
        String caseName = command.getParameter(this.config.getSwitchParameter()).getFirstValue();
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        int pageIndex = 0;
        CMSContent content;
        try {
            AbstractBasicContent abc = AbstractContentDisplayer.chooseContent(command, s);
            if (!(abc instanceof CMSContent)) {
                tx.commit();
                request.getEventManager().addEvent(new NotFoundEvent());
                return null;
            }
            content = (CMSContent) abc;
            if (command.getParameter("page") != null) {
                pageIndex = Integer.parseInt(command.getParameter("page").getFirstValue());
            }
            content.getPage(pageIndex);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException(e);
        } finally {
            s.close();
        }
        boolean errors = !performChecks(caseName, command, content.getPage(pageIndex));
        CMSContentDisplayerResult result = new CMSContentDisplayerResult(content, request);
        result.setPageId(content.getPage(pageIndex).getId());
        if (!errors) {
            LOGGER.debug("sending mails");
            formSuccessfullyProcessed(command, caseName, request);
            return result;
        } else {
            LOGGER.debug("loading error result");
            try {
                fillInFormFields(content.getPage(pageIndex), command, this.config.getSwitchParameter(), caseName);
            } catch (Exception e) {
                LOGGER.warn("error post processing the error result: " + e.getLocalizedMessage(), e);
            }
            return result;
        }
    }

    /**
	 * fills the sent values into the form fields for the error response.
	 * 
	 * @param page
	 *            the page with the form
	 * @param command
	 *            the current command
	 * @param switchParameterName
	 *            the name of the switch parameter
	 * @param caseName
	 *            the name of the processed case
	 * @throws JDOMException
	 *             on errors processing the page
	 * @throws IOException
	 *             on errors processing the page
	 */
    private void fillInFormFields(final Page page, final Command command, final String switchParameterName, final String caseName) throws JDOMException, IOException {
        XPath xpath = XPath.newInstance("//form[descendant::input[@name='" + switchParameterName + "' and @value='" + caseName + "']]");
        Element form = (Element) xpath.selectSingleNode(page.getContents());
        String classAttr = form.getAttributeValue("class");
        if (classAttr == null) {
            classAttr = "activeForm";
        } else {
            classAttr += " activeForm";
        }
        form.setAttribute("class", classAttr);
        xpath = XPath.newInstance("descendant::input|descendant::select|descendant::textarea");
        @SuppressWarnings("unchecked") List<Element> inputElements = (List<Element>) xpath.selectNodes(form);
        for (Element input : inputElements) {
            if (input.getName().equals("input")) {
                processInputElement(command, input);
            } else if (input.getName().equals("select")) {
                processSelectElement(command, input);
            } else if (input.getName().equals("textarea")) {
                input.setText(command.getParameter(input.getAttributeValue("name")).getFirstValue());
            }
        }
    }

    /**
	 * processes a {@code &ltselect/&gt;} element.
	 * 
	 * @param command
	 *            the current command
	 * @param input
	 *            the current input element
	 * @throws JDOMException
	 *             on errors processing the element
	 */
    private void processSelectElement(final Command command, final Element input) throws JDOMException {
        XPath xpath;
        xpath = XPath.newInstance("descendant::option");
        @SuppressWarnings("unchecked") List<Element> nodes = (List<Element>) xpath.selectNodes(input);
        for (Element option : nodes) {
            String checkValue = "";
            if ((option.getAttribute("value") != null)) {
                checkValue = option.getAttributeValue("value");
            } else {
                checkValue = option.getTextNormalize();
            }
            input.removeAttribute("selected");
            for (String value : command.getParameter(input.getAttributeValue("name")).getValues()) {
                if (checkValue.equals(value)) {
                    input.setAttribute("selected", "selected");
                }
            }
        }
    }

    /**
	 * processes an {@code &ltinput/&gt;} element.
	 * 
	 * @param command
	 *            the current command
	 * @param input
	 *            the current input element
	 */
    private void processInputElement(final Command command, final Element input) {
        String type = input.getAttributeValue("type");
        LOGGER.debug("processing : {}", input.getAttributeValue("name"));
        if (type.equals("text")) {
            input.setAttribute("value", command.getParameter(input.getAttributeValue("name")).getFirstValue());
        } else if ((type.equals("radio") || type.equals("checkbox")) && (command.getParameter(input.getAttributeValue("name")) != null)) {
            for (String value : command.getParameter(input.getAttributeValue("name")).getValues()) {
                if (value.equals(input.getAttributeValue("value"))) {
                    input.setAttribute("checked", "checked");
                } else {
                    input.removeAttribute("checked");
                }
            }
        }
    }

    /**
	 * checks the form elements as defined by the case.
	 * <p>
	 * TODO: describe handling of error message &ltspan/&gt; elements.
	 * </p>
	 * 
	 * @param caseName
	 *            the name of the case
	 * @param command
	 *            the current command
	 * @param p
	 *            the {@code Page}
	 * @return {@code true}, if and only if all checks have completed
	 *         successfully. Otherwise false.
	 */
    private boolean performChecks(final String caseName, final Command command, final Page p) {
        boolean success = true;
        if (!checkRequiredFields(caseName, command, p)) {
            success = false;
        }
        if (!checkRegexFields(caseName, command, p)) {
            success = false;
        }
        if (success) {
            activateSuccessMessage(p, caseName);
        }
        return success;
    }

    /**
	 * activates the success span ( {@code &lt;span name="success_message"/&gt;}
	 * ).
	 * 
	 * @param p
	 *            the current page
	 * @param caseName
	 *            the name of the case
	 */
    private void activateSuccessMessage(final Page p, final String caseName) {
        try {
            XPath xpath = XPath.newInstance("//div[@name='" + caseName + "_" + "success_message']");
            @SuppressWarnings("unchecked") List<Element> nodes = (List<Element>) xpath.selectNodes(p.getContents());
            for (Element node : nodes) {
                String styleAttr = node.getAttributeValue("style");
                node.setAttribute("style", setDisplayInline(styleAttr));
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing form (success message) '{}': {}", caseName, e.getLocalizedMessage());
        }
    }

    /**
	 * checks the required fields.
	 * 
	 * @param caseName
	 *            the name of the case
	 * @param command
	 *            the current command
	 * @param p
	 *            the {@code Page}
	 * @return {@code true}, if and only if all checks have completed
	 *         successfully. Otherwise false.
	 */
    private boolean checkRequiredFields(final String caseName, final Command command, final Page p) {
        boolean success = true;
        for (String fieldName : this.config.getRequiredFields(caseName)) {
            if ((command.getParameter(fieldName) == null) || (command.getParameter(fieldName).getFirstValue().equals(""))) {
                success = false;
                try {
                    XPath xpath = XPath.newInstance("//div[@name='" + caseName + "_" + fieldName + "_required']");
                    @SuppressWarnings("unchecked") List<Element> nodes = (List<Element>) xpath.selectNodes(p.getContents());
                    for (Element node : nodes) {
                        String styleAttr = node.getAttributeValue("style");
                        node.setAttribute("style", setDisplayInline(styleAttr));
                    }
                } catch (Exception e) {
                    LOGGER.error("Error while processing form (regex) '" + caseName + "': " + e.getLocalizedMessage(), e);
                }
            }
        }
        return success;
    }

    /**
	 * @param styleAttr
	 *            the text content of the style attribute
	 * @return the modified text
	 */
    private String setDisplayInline(final String styleAttr) {
        StringBuilder style = new StringBuilder(styleAttr.replaceAll("display\\s*?:\\s*?\\w+;?", ""));
        if ((style.length() > 0) && (style.charAt(style.length() - 1) != ';')) {
            style.append(';');
        }
        return style.append("display: inline;").toString();
    }

    /**
	 * checks the regex fields.
	 * 
	 * @param caseName
	 *            the name of the case
	 * @param command
	 *            the current command
	 * @param p
	 *            the {@code Page}
	 * @return {@code true}, if and only if all checks have completed
	 *         successfully. Otherwise false.
	 */
    private boolean checkRegexFields(final String caseName, final Command command, final Page p) {
        boolean success = true;
        for (Entry<String, String> entry : this.config.getRegexFields(caseName).entrySet()) {
            String regex = entry.getValue();
            Parameter parameter = command.getParameter(entry.getKey());
            if ((parameter == null) || (!parameter.getFirstValue().matches(regex))) {
                success = false;
                try {
                    XPath xpath;
                    if (this.config.regexFieldUseStandardMessage(caseName, entry.getKey())) {
                        xpath = XPath.newInstance("//div[@name='" + caseName + "_" + entry.getKey() + "_required']");
                    } else {
                        xpath = XPath.newInstance("//div[@name='" + caseName + "_" + entry.getKey() + "_regex']");
                    }
                    @SuppressWarnings("unchecked") List<Element> nodes = (List<Element>) xpath.selectNodes(p.getContents());
                    for (Element node : nodes) {
                        String styleAttr = node.getAttributeValue("style");
                        node.setAttribute("style", setDisplayInline(styleAttr));
                    }
                } catch (Exception e) {
                    LOGGER.error("Error while processing form (regex) '" + caseName + "': " + e.getLocalizedMessage(), e);
                }
            }
        }
        return success;
    }

    /**
	 * actually sends the mails.
	 * 
	 * @param command
	 *            the {@code Command}
	 * @param caseName
	 *            the name of the current case
	 * @param request
	 *            the current request
	 */
    private void formSuccessfullyProcessed(final Command command, final String caseName, final ServiceRequest request) {
        for (InternetAddress recipient : this.config.getRecipients(caseName)) {
            try {
                Email email = new Email();
                email.setFrom(this.config.getFrom(caseName, request));
                email.setReplyTo(this.config.getFrom(caseName, request));
                email.setSubject(this.config.getSubject(caseName));
                email.setAttachments(this.config.getAttachments(caseName));
                email.setInlineImages(this.config.getInlineImages(caseName));
                buildEmailBody(command, caseName, email);
                email.setRecipient(recipient);
                Lifecycle.getMailQueue().add(email);
            } catch (Exception e) {
                throw new PulseException("Error adding mail to queue: " + e.getLocalizedMessage(), e);
            }
        }
        if (this.config.getRedirectURI(caseName) != null) {
            request.getEventManager().addEvent(new RedirectEvent(this.config.getRedirectURI(caseName)));
        } else if (this.config.getRedirectCommandBuilder(caseName) != null) {
            request.getEventManager().addEvent(new RedirectEvent(this.config.getRedirectCommandBuilder(caseName).mixIn(request.getCommand().createCopy(false)).toCommandURL(request)));
        }
    }

    /**
	 * actually builds the body of the e-Mail.
	 * 
	 * @param command
	 *            the current command for parameter extraction
	 * @param caseName
	 *            the name of the current case (see configuration)
	 * @param email
	 *            the email itself
	 */
    private void buildEmailBody(final Command command, final String caseName, final Email email) {
        Element parameters = new Element("parameters");
        for (Parameter p : command.getParameters()) {
            if (!p.getName().equals(this.config.getSwitchParameter())) {
                parameters.addContent(p.deserializeToJDOM());
            }
        }
        if (this.config.getTextXSLFile(caseName) != null) {
            email.setTextContent(XSLTOutputter.output(parameters, this.config.getTextXSLFile(caseName)));
        }
        if (this.config.getHtmlXSLFile(caseName) != null) {
            email.setHTMLContent(XSLTOutputter.output(parameters, this.config.getHtmlXSLFile(caseName)));
        }
    }

    /**
	 * @param conf
	 *            the configuration
	 * @see org.torweg.pulse.configuration.DeprecatedConfigurable#init(org.torweg.pulse.configuration.ConfigBean)
	 */
    public void init(final ConfigBean conf) {
        this.config = (CMSFormMailerConfig) conf;
    }
}
