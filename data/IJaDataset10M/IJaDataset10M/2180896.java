package org.w3c.tidy.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.servlet.jsp.tagext.ValidationImageTag;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;

/**
 * Common class used by Filter and Tag to process responce.
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision: 766 $ ($Author: vlads $)
 */
public class TidyProcessor {

    /**
     * The request with which this Processor is associated.
     */
    HttpSession httpSession;

    HttpServletRequest request;

    HttpServletResponse response;

    /**
     * JTidy Parser configutation string Examples of config string: indent: auto; indent-spaces: 2
     */
    private String config;

    /**
     * validateOnly only do not change output.
     */
    private boolean validateOnly;

    /**
     * Performs validation of html processed by &lt;jtidy:tidy&gt; jsp tag By default this is not done. Only Usefull for
     * testing JTidy This will create second requestID to store the data
     */
    private boolean doubleValidation;

    private boolean commentsSubst;

    /**
     * Logger.
     */
    private Log log = LogFactory.getLog(TidyProcessor.class);

    /**
     * Initialize Processor.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public TidyProcessor(HttpSession httpSession, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpSession = httpSession;
        this.request = httpServletRequest;
        this.response = httpServletResponse;
    }

    private void parsConfig(Configuration configuration) {
        if (config == null) {
            return;
        }
        Properties properties = new Properties();
        StringTokenizer st = new StringTokenizer(config, ";");
        while (st.hasMoreTokens()) {
            String nv = st.nextToken();
            int split = nv.indexOf(':');
            if (split > 0) {
                String n = nv.substring(0, split).trim();
                String v = nv.substring(split + 1).trim();
                if (Configuration.isKnownOption(n)) {
                    properties.put(n, v);
                    log.debug("add option " + n + "=" + v);
                } else {
                    log.warn("TidyTag unknown option " + n);
                }
            }
        }
        configuration.addProps(properties);
        configuration.adjust();
    }

    public boolean parse(InputStream in, OutputStream out, String html) {
        if (this.request.getAttribute(Consts.ATTRIBUTE_IGNORE) != null) {
            log.debug("IGNORE");
            return false;
        }
        RepositoryFactory factory = JTidyServletProperties.getInstance().getRepositoryFactoryInstance();
        Object requestID = factory.getResponseID(this.httpSession, this.request, this.response, false);
        if (requestID == null) {
            log.debug("IGNORE requestID == null");
            return false;
        }
        boolean secondPass = false;
        if (this.request.getAttribute(Consts.ATTRIBUTE_PROCESSED) != null) {
            if (!doubleValidation) {
                log.debug("IGNORE !doubleValidation");
                return false;
            }
            requestID = factory.getResponseID(this.httpSession, this.request, this.response, true);
            secondPass = true;
        }
        if (!secondPass) {
            log.debug("addCookie");
            this.response.addCookie(new Cookie(Consts.ATTRIBUTE_REQUEST_ID, requestID.toString()));
        }
        boolean rc = parse(in, out, html, requestID, factory);
        if (!secondPass) {
            this.request.setAttribute(Consts.ATTRIBUTE_PROCESSED, requestID);
        }
        if (rc && (!this.validateOnly) && (this.request.getAttribute(Consts.ATTRIBUTE_PASS) != null)) {
            rc = false;
        }
        return rc;
    }

    public boolean parse(InputStream in, OutputStream out, String html, Object requestID, RepositoryFactory factory) {
        long start = System.currentTimeMillis();
        Tidy tidy = new Tidy();
        parsConfig(tidy.getConfiguration());
        tidy.setSmartIndent(true);
        tidy.setQuiet(true);
        ByteArrayOutputStream mesageBuffer = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(mesageBuffer);
        tidy.setErrout(pw);
        boolean useOut = false;
        ResponseRecord result = factory.createRecord(requestID, this.httpSession, this.request, this.response);
        result.setRequestID(requestID);
        tidy.setMessageListener(result);
        boolean fatalError = false;
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        try {
            log.debug("processing request " + requestID + "...");
            tidy.parse(in, outBuffer);
            useOut = (result.getParseErrors() == 0);
            if (commentsSubst) {
                doCommentsSubst(outBuffer, requestID);
            }
            if (out != null) {
                outBuffer.writeTo(out);
            }
        } catch (Throwable e) {
            log.error("JTidy parsing error", e);
            result.messageReceived(new TidyMessage(0, 0, 0, TidyMessage.Level.ERROR, "JTidy parsing error" + e.getMessage()));
            fatalError = true;
        }
        result.setHtmlInput(html);
        if ((result.getParseErrors() > 0) || fatalError) {
            result.setHtmlOutput(html);
        } else {
            result.setHtmlOutput(outBuffer.toString());
        }
        if (!fatalError) {
        }
        long time = System.currentTimeMillis() - start;
        result.setParsTime(time);
        if (log.isDebugEnabled()) {
            log.debug("processed in " + time + " millis");
        }
        ResponseRecordRepository repository = factory.getRepositoryInstance(this.httpSession);
        repository.addRecord(result);
        if (JTidyServletProperties.getInstance().getBooleanProperty(JTidyServletProperties.PROPERTY_BOOLEAN_LOG_VALIDATION_MESSAGES, false)) {
            for (Iterator iter = result.getMessages().iterator(); iter.hasNext(); ) {
                TidyMessage message = (TidyMessage) iter.next();
                StringBuffer msg = new StringBuffer();
                msg.append(message.getLevel());
                msg.append(" (L").append(message.getLine());
                msg.append(":C").append(message.getColumn()).append(") ");
                msg.append(message.getMessage());
                log.info(msg.toString());
            }
        }
        String shortMessage;
        if ((result.getParseErrors() != 0) || (result.getParseWarnings() != 0)) {
            if (result.getParseErrors() == 0) {
                shortMessage = "found " + result.getParseWarnings() + " warnings in generated HTML";
            } else {
                shortMessage = "found " + result.getParseErrors() + " errors and " + result.getParseWarnings() + " warnings in generated HTML";
            }
        } else {
            shortMessage = "no problems found";
        }
        log.info(shortMessage + " request " + requestID);
        return (useOut && (out != null));
    }

    private void doCommentsSubst(ByteArrayOutputStream outBuffer, Object requestID) {
        log.debug("doCommentsSubst");
        if (response != null) {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", -1);
        }
        String html = outBuffer.toString();
        html = replaceAll(html, "<!--jtidy:requestID-->", requestID.toString());
        String aLink = ValidationImageTag.getImageHTML(requestID.toString(), null, null, request);
        html = replaceAll(html, "<!--jtidy:validationImage-->", aLink);
        outBuffer.reset();
        try {
            outBuffer.write(html.getBytes());
        } catch (IOException e) {
            log.error("Internal error", e);
        }
    }

    /**
     * @param config The config to set.
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * @return Returns the doubleValidation.
     */
    public boolean isDoubleValidation() {
        return doubleValidation;
    }

    /**
     * @param doubleValidation The doubleValidation to set.
     */
    public void setDoubleValidation(boolean doubleValidation) {
        this.doubleValidation = doubleValidation;
    }

    /**
     * @param validateOnly The validateOnly to set.
     */
    public void setValidateOnly(boolean validateOnly) {
        this.validateOnly = validateOnly;
    }

    /**
     * @param commentsSubst The commentsSubst to set.
     */
    public void setCommentsSubst(boolean commentsSubst) {
        this.commentsSubst = commentsSubst;
    }

    /**
     * jre 1.3 compatible replaceAll.
     * @param str text to search and replace in
     * @param replace the String to search for
     * @param replacement the String to replace with
     * @return the text with any replacements processed
     */
    public String replaceAll(String str, String replace, String replacement) {
        StringBuffer sb = new StringBuffer(str);
        int firstOccurrence = str.indexOf(replace);
        while (firstOccurrence != -1) {
            sb.replace(firstOccurrence, firstOccurrence + replace.length(), replacement);
            firstOccurrence = sb.toString().indexOf(replace);
        }
        return sb.toString();
    }
}
