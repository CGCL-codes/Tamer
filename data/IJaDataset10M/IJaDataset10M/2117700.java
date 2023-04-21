package org.openjf.bbcode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.openjf.template.RegExMatcherModel;
import org.openjf.template.SimpleTemplate;

public class SimpleRegExTag implements Tag {

    private String bbCode;

    private SimpleTemplate openTagTemplate;

    private SimpleTemplate openTagCleanTemplate;

    private String closeHtmlTag;

    private SimpleTemplate closeTagTemplate;

    private SimpleTemplate closeTagCleanTemplate;

    private Pattern pattern;

    private boolean suppressLineBreaks;

    public SimpleRegExTag(String bbCode, String parameterRegEx, String openHtmlTag, String closeHtmlTag, String openHtmlCleanTag, String closeHtmlCleanTag) {
        this.bbCode = bbCode;
        this.openTagTemplate = new SimpleTemplate(openHtmlTag);
        this.closeHtmlTag = closeHtmlTag;
        if (closeHtmlTag != null) {
            this.closeTagTemplate = new SimpleTemplate(closeHtmlTag);
        }
        if (openHtmlCleanTag != null) {
            this.openTagCleanTemplate = new SimpleTemplate(openHtmlCleanTag);
        }
        if (closeHtmlCleanTag != null) {
            this.closeTagCleanTemplate = new SimpleTemplate(closeHtmlCleanTag);
        }
        this.pattern = Pattern.compile(parameterRegEx);
    }

    public SimpleRegExTag(String bbCode, String parameterRegEx, String openHtmlTag, String closeHtmlTag) {
        this(bbCode, parameterRegEx, openHtmlTag, closeHtmlTag, null, null);
    }

    public boolean valid(TagState state) {
        if (state.getParameter() == null) {
            return false;
        }
        return pattern.matcher(state.getParameter()).matches();
    }

    protected String createOpenTag(TagState state, SimpleTemplate template) {
        RegExMatcherModel regExEval = new RegExMatcherModel(pattern, state.getParameter());
        Map evaluators = new HashMap(state.getTemplateModel());
        evaluators.put("matcher", regExEval);
        return template.format(evaluators);
    }

    public void open(TagState state) {
        state.setSupressLineBreaks(suppressLineBreaks);
        state.setText(createOpenTag(state, openTagTemplate));
    }

    public void openClean(TagState state) {
        state.setSupressLineBreaks(suppressLineBreaks);
        if (openTagCleanTemplate == null) {
            state.setText(" ");
        } else {
            state.setText(createOpenTag(state, openTagCleanTemplate));
        }
    }

    public void close(TagState state, boolean explicitClose) {
        state.setSupressLineBreaks(suppressLineBreaks);
        if (closeTagTemplate != null) {
            state.setText(closeTagTemplate.format(state.getTemplateModel()));
        }
    }

    public void closeClean(TagState state, boolean explicitClose) {
        state.setSupressLineBreaks(suppressLineBreaks);
        if (closeTagCleanTemplate == null) {
            state.setText(" ");
        } else {
            state.setText(closeTagCleanTemplate.format(state.getTemplateModel()));
        }
    }

    public String getBBCode() {
        return bbCode;
    }

    public boolean endTagNeeded(TagState state) {
        return (closeHtmlTag != null);
    }

    public boolean isSupressLineBreaks() {
        return suppressLineBreaks;
    }

    public void setSupressLineBreaks(boolean suppressLineBreaks) {
        this.suppressLineBreaks = suppressLineBreaks;
    }
}
