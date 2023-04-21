package com.metanology.mde.core.htmlLifeCycle;

import org.apache.oro.text.regex.*;
import com.metanology.mde.core.codeFactory.LogFactory;
import com.metanology.mde.utils.Messages;

public class FormControl extends HtmlAspControl {

    public String action;

    public String otherProperties;

    /**
	 *  Get action.
	 */
    public String getAction() {
        return this.action;
    }

    /**
	 *  Set action.
	 */
    public void setAction(String newVal) {
        this.action = newVal;
    }

    /**
	 *  Get otherProperties.
	 */
    public String getOtherProperties() {
        return this.otherProperties;
    }

    /**
	 *  Set otherProperties.
	 */
    public void setOtherProperties(String newVal) {
        this.otherProperties = newVal;
    }

    public FormControl(String content) {
        if (content == null || content.length() == 0) {
            this.content = "";
            return;
        }
        content = HtmlAspRepository.removeNewLineReturnChars(content);
        this.content = "";
        this.action = "";
        this.metaId = "";
        this.otherProperties = "";
        ParsedTagAttribute attr = ParsedTagAttribute.parseTagAttribute(content, "id");
        if (attr.value.length() == 0 || attr.value.charAt(0) != '_') {
            throw new RuntimeException("Not a FormControl");
        }
        this.metaId = attr.value;
        content = attr.rest;
        Pattern pattern1 = null;
        PatternMatcherInput input;
        MatchResult result;
        try {
            pattern1 = HtmlAspRepository.compiler.compile(FormControl.patternStr1, Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            LogFactory.getLog().error(Messages.get(Messages.MSG_BAD_REGEX_PATTERN_ARG1_ARG2, "FormControl", patternStr1));
            return;
        }
        input = new PatternMatcherInput(content);
        if (this.matcher.contains(input, pattern1)) {
            result = this.matcher.getMatch();
            String grp1 = result.group(1);
            if (grp1 == null) grp1 = ""; else grp1 = grp1.trim();
            String grp2 = result.group(2);
            if (grp2 == null) grp2 = ""; else grp2 = grp2.trim();
            grp2 = HtmlAspRepository.removeNewLineReturnChars(grp2);
            if (grp1.equalsIgnoreCase("html:")) {
                this.setType(STRUTS);
            }
            attr = ParsedTagAttribute.parseTagAttribute(grp2, "action");
            this.action = attr.value;
            this.otherProperties = attr.rest;
        } else {
            throw new RuntimeException("Not a form meta control");
        }
    }

    public String merge(HtmlAspControl newControl) {
        if (newControl == null) {
            return HtmlAspRepository.DELETE_FIELD_PREFIX + this.toString() + HtmlAspRepository.DELETE_FIELD_SUFFIX;
        } else if (newControl instanceof FormControl) {
            FormControl c = (FormControl) newControl;
            this.action = c.action;
            this.metaId = c.getMetaId();
            if (this.getType() != c.getType()) {
                this.setType(c.getType());
                this.otherProperties = c.otherProperties;
            }
            return this.toString();
        } else {
            return newControl.toString();
        }
    }

    private PatternMatcher matcher = new Perl5Matcher();

    private static String patternStr1;

    static {
        patternStr1 = "<\\s*(html:)?form \\s*(.*?)>$";
    }

    public String toString() {
        StringBuffer retStr = new StringBuffer("");
        retStr.append("form action=\"");
        retStr.append(this.action);
        retStr.append("\"");
        if (this.otherProperties.length() > 0) {
            retStr.append(" ");
            retStr.append(this.otherProperties);
        }
        if (this.getType() != STRUTS) {
            retStr.append(" id=\"");
            retStr.append(this.metaId);
            retStr.append("\"");
        }
        retStr.append(">");
        if (this.getType() == STRUTS) {
            retStr.append("<%--id=\"" + metaId + "\"--%>");
            retStr.insert(0, "<html:");
        } else {
            retStr.insert(0, "<");
        }
        return retStr.toString();
    }
}
