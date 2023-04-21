package com.jvantage.ce.web.setup;

import com.jvantage.ce.common.Constants;
import com.jvantage.ce.presentation.html.HTML;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 *  There aren't a lot of saftey error checks in this code.  The setup code
 *  is, unfortunately, not on par with the quality of the rest of jVantage.
 *  Also, there's nothing efficient (at all) about this code.  It is a single
 *  user, run once per installation application.
 *
 * @author  bclay
 */
public class Controller {

    public static final String sfWizardHome = SetupConstants.ServletAlias_Setup;

    public static final boolean sfOmitProgressBar = true;

    public static final boolean sfDoNotOmitProgressBar = false;

    String freshTemplate = null;

    private int currentStep = 0;

    private int numberOfSteps = 0;

    private Hashtable stepURLHash = null;

    private Vector stepVector = null;

    private Hashtable tagValueHash = null;

    /** Creates a new instance of Controller */
    public Controller(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
        stepVector = new Vector(numberOfSteps);
        stepURLHash = new Hashtable();
        tagValueHash = new Hashtable();
        for (int i = 0; i < numberOfSteps; i++) {
            stepVector.addElement(new Boolean(false));
        }
        setStepURL(0, SetupConstants.ServletAlias_Setup);
        setStepURL(1, SetupConstants.ServletAlias_SetupLicense);
        setStepURL(2, SetupConstants.ServletAlias_SetupConfigDir);
        setStepURL(3, SetupConstants.ServletAlias_SetupDataSource);
        setStepURL(4, SetupConstants.ServletAlias_SetupPort);
        setStepURL(5, SetupConstants.ServletAlias_SetupFinished);
    }

    /**
     * DOCUMENT ME!
     *
     * @param templateFileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getTemplatePathAndName(String templateFileName) {
        StringBuffer buf = new StringBuffer();
        buf.append(templateFileName).append(".html");
        return buf.toString();
    }

    public void clearHelpMessage() {
        tagValueHash.remove(SetupConstants.sfTag_HelpMessage);
    }

    public void clearInstructionText() {
        tagValueHash.remove(SetupConstants.sfTag_InstructionText);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearTaggedData() {
        tagValueHash.clear();
    }

    public String getAndRemoveHelpMessage() {
        String helpMessage = getHelpMessage();
        if (helpMessage != null) {
            tagValueHash.remove(SetupConstants.sfTag_HelpMessage);
        }
        return helpMessage;
    }

    public String getContent() {
        return getContent(sfDoNotOmitProgressBar);
    }

    public String getContent(boolean omitProgressBar) {
        String content = new String(freshTemplate);
        content = StringUtils.replace(content, SetupConstants.sfTag_Date, getDate());
        omitProgressBar = true;
        if (omitProgressBar == sfDoNotOmitProgressBar) {
            content = StringUtils.replace(content, SetupConstants.sfTag_PageCount, getStepOfString());
            content = StringUtils.replace(content, SetupConstants.sfTag_Progress, getProgressBar());
        }
        String helpMessage = getAndRemoveHelpMessage();
        if (StringUtils.isNotBlank(helpMessage)) {
            StringBuffer helpContent = new StringBuffer();
            helpContent.append(HTML.tableBeg("Width=\"100%\" Border=\"1\" bordercolor=\"#FAF5E7\"")).append(HTML.tableRowBeg()).append(HTML.tableDataBeg("vAlign=\"top\" Align=\"right\" Width=\"10%\" bordercolor=\"#FAF5E7\"")).append(HTML.image(SetupConstants.sfHelpSuggestionImage, "Suggestion.")).append(HTML.tableDataEnd()).append(HTML.tableData(helpMessage, "vAlign=\"top\" Align=\"left\" Width=\"90%\"  bordercolor=\"#990000\" Class=\"helpMessage\"")).append(HTML.tableRowEnd()).append(HTML.tableEnd());
            content = StringUtils.replace(content, SetupConstants.sfTag_HelpMessage, helpContent.toString());
        }
        Enumeration keys = tagValueHash.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            content = StringUtils.replace(content, key, (String) tagValueHash.get(key));
        }
        String tagPattern = Constants.sfApplicationVariablePrefix_RegEx + ".*" + Constants.sfApplicationVariableSuffix_RegEx;
        Pattern p = Pattern.compile(tagPattern);
        Matcher m = p.matcher(content);
        content = m.replaceAll("");
        return content;
    }

    /** Getter for property currentStep.
     * @return Value of property currentStep.
     *
     */
    public int getCurrentStep() {
        return currentStep;
    }

    public String getDate() {
        java.util.Date d = new java.util.Date();
        java.text.DateFormat f = new java.text.SimpleDateFormat("MMMM d, yyyy", Locale.US);
        return f.format(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHelpMessage() {
        return (String) tagValueHash.get(SetupConstants.sfTag_HelpMessage);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getIsFirstStep() {
        return isCurrentStep(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getIsLastStep() {
        return getCurrentStep() == numberOfSteps;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getProgressBar() {
        StringBuffer buf = new StringBuffer();
        String previousStepURL = sfWizardHome;
        if (getIsFirstStep() == false) {
            previousStepURL = getStepURL(getCurrentStep() - 1);
        }
        buf.append(HTML.anchor(HTML.image(SetupConstants.sfProgressBarImage_PreviousStep, "Alt=\"Previous Step\""), previousStepURL));
        for (int i = 0; i < numberOfSteps; i++) {
            String positionIndicatorAltText = "Alt=\"You Are On " + getStepOfString() + "\"";
            int step = i + 1;
            if (isCurrentStep(step)) {
                buf.append(HTML.image(SetupConstants.sfProgressBarImage_Current, positionIndicatorAltText));
            } else if (isStepComplete(step)) {
                buf.append(HTML.image(SetupConstants.sfProgressBarImage_Complete, positionIndicatorAltText));
            } else {
                buf.append(HTML.image(SetupConstants.sfProgressBarImage_Incomplete, positionIndicatorAltText));
            }
        }
        if (getIsLastStep()) {
            buf.append(HTML.image(SetupConstants.sfProgressBarImage_Blank));
        } else {
            String url = getStepURL(getCurrentStep() + 1);
            buf.append(HTML.anchor(HTML.image(SetupConstants.sfProgressBarImage_NextStep, "Alt=\"Next Step\""), url));
        }
        return buf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStepOfString() {
        StringBuffer msg = new StringBuffer();
        msg.append("Step ").append(getCurrentStep()).append(" of ").append(numberOfSteps);
        return msg.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param stepNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStepURL(int stepNumber) {
        return (String) stepURLHash.get(stepNumber + "");
    }

    /**
     *  This takes an inputStream who's source is the HTML template for
     *  this page.  This stream is retrieved using the ServletContext
     *  from the calling Servlet.
     */
    public void initTemplate(InputStream inStream) {
        StringBuffer buf = new StringBuffer();
        if (inStream == null) {
            System.out.println("Unable to find template resource.");
            return;
        }
        try {
            Reader in = new BufferedReader(new InputStreamReader(inStream));
            int ch = 0;
            while ((ch = in.read()) > -1) {
                buf.append((char) ch);
            }
        } catch (Exception e) {
            System.out.println("Unable to read from resource.");
            e.printStackTrace(System.err);
        }
        freshTemplate = buf.toString();
    }

    public boolean isComplete() {
        Enumeration en = stepVector.elements();
        while (en.hasMoreElements()) {
            if (((Boolean) en.nextElement()).booleanValue() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stepNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isStepComplete(int stepNumber) {
        if (isGoodStepNumber(stepNumber) == false) {
            return false;
        }
        return ((Boolean) stepVector.get(stepNumber - 1)).booleanValue();
    }

    /** Setter for property currentStep.
     * @param currentStep New value of property currentStep.
     *
     */
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public void setGeneratedContent(String text) {
        tagValueHash.put(SetupConstants.sfTag_GeneratedContent, text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setHelpMessage(String text) {
        tagValueHash.put(SetupConstants.sfTag_HelpMessage, text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setInstructions(String text) {
        tagValueHash.put(SetupConstants.sfTag_InstructionText, text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     */
    public void setPageTitle(String title) {
        tagValueHash.put(SetupConstants.sfTag_PageTitle, title);
    }

    /**
     * DOCUMENT ME!
     */
    public void setStepFailed() {
        String url = getStepURL(getCurrentStep());
        String msg = "This step did not complete successfully.";
        tagValueHash.put(SetupConstants.sfTag_CompletionResult, HTML.anchor(HTML.image(SetupConstants.sfFailImage, msg), url));
    }

    /**
     * DOCUMENT ME!
     *
     * @param stepNumber DOCUMENT ME!
     * @param yesOrNo DOCUMENT ME!
     */
    public void setStepIsComplete(int stepNumber, boolean yesOrNo) {
        if (isGoodStepNumber(stepNumber) == false) {
            return;
        }
        stepVector.set(stepNumber - 1, new Boolean(yesOrNo));
    }

    /**
     * DOCUMENT ME!
     */
    public void setStepSucceeded() {
        String url = getStepURL(getCurrentStep() + 1);
        String msg = "Alt=\"This step was successfully completed.\"";
        tagValueHash.put(SetupConstants.sfTag_CompletionResult, HTML.anchor(HTML.image(SetupConstants.sfSuccessImage, msg), url));
        setStepIsComplete(getCurrentStep(), true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param stepNumber DOCUMENT ME!
     * @param URL DOCUMENT ME!
     */
    public void setStepURL(int stepNumber, String URL) {
        if (URL == null) {
            return;
        }
        stepURLHash.put(stepNumber + "", URL);
    }

    public boolean stepIsComplete(int stepNumber) {
        if (isGoodStepNumber(stepNumber) == false) {
            return false;
        }
        Boolean b = (Boolean) stepVector.get(stepNumber - 1);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    private boolean isCurrentStep(int stepNumber) {
        if (isGoodStepNumber(stepNumber) == false) {
            return false;
        }
        if (stepNumber == getCurrentStep()) {
            return true;
        }
        return false;
    }

    private boolean isGoodStepNumber(int stepNumber) {
        if ((stepNumber - 1) > stepVector.size()) {
            return false;
        }
        return true;
    }
}
