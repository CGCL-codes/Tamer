package com.metanology.mde.core.htmlLifeCycle;

import org.apache.oro.text.regex.*;
import com.metanology.mde.core.codeFactory.LogFactory;
import com.metanology.mde.utils.Messages;

public class RadioControl extends InputControl {

    public String checked;

    /**
	 *  Get checked.
	 */
    public String getChecked() {
        return this.checked;
    }

    /**
	 *  Set checked.
	 */
    public void setChecked(String newVal) {
        this.checked = newVal;
    }

    public RadioControl(String metaId, String name, String value, String otherProp) {
        this.content = "";
        this.metaId = metaId;
        this.name = name;
        this.value = value;
        this.otherProperties = otherProp;
        this.checked = "";
        Pattern pattern1 = null;
        PatternMatcherInput input;
        MatchResult result;
        try {
            pattern1 = HtmlAspRepository.compiler.compile(RadioControl.patternStr1, Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            LogFactory.getLog().error(Messages.get(Messages.MSG_BAD_REGEX_PATTERN_ARG1_ARG2, "RadioControl", patternStr1));
            return;
        }
        input = new PatternMatcherInput(otherProp);
        if (this.matcher.contains(input, pattern1)) {
            result = this.matcher.getMatch();
            String grp1 = result.group(1).trim();
            String grp2 = result.group(2).trim();
            String grp3 = result.group(3).trim();
            if (grp2 != null && grp2.length() > 0) this.checked = grp2;
            this.otherProperties = "";
            if (grp1 != null && grp1.length() > 0) this.otherProperties = grp1;
            if (grp3 != null && grp3.length() > 0) {
                if (this.otherProperties.length() > 0) this.otherProperties += " ";
                this.otherProperties += grp3;
            }
        }
    }

    public String merge(HtmlAspControl newControl) {
        if (newControl == null) {
            return HtmlAspRepository.DELETE_FIELD_PREFIX + this.toString() + HtmlAspRepository.DELETE_FIELD_SUFFIX;
        } else if (newControl instanceof RadioControl) {
            RadioControl c = (RadioControl) newControl;
            this.name = c.name;
            this.value = c.value;
            this.checked = c.checked;
            this.metaId = c.metaId;
            this.property = c.property;
            if (this.getType() != STRUTS && c.getType() == STRUTS) {
                this.setGenDemo(true);
                this.demoOtherProperties = this.otherProperties;
                this.otherProperties = parseTagAttributes(this.otherProperties);
            }
            this.setType(c.getType());
            if (this.getType() == STRUTS) {
                this.setTagPrefix(c.getTagPrefix());
                ParsedTagAttribute attr0 = ParsedTagAttribute.parseTagAttribute(this.otherProperties, "indexed");
                ParsedTagAttribute attr = ParsedTagAttribute.parseTagAttribute(c.otherProperties, "indexed");
                if (attr0.value.length() == 0 && attr.value.length() > 0) {
                    this.otherProperties = "indexed=\"true\" " + this.otherProperties;
                } else if (attr0.value.length() > 0 && attr.value.length() == 0) {
                    this.otherProperties = attr0.rest;
                }
            }
            return this.toString();
        } else {
            return newControl.toString();
        }
    }

    private PatternMatcher matcher = new Perl5Matcher();

    private static String patternStr1;

    static {
        patternStr1 = "([\\s\\S]*)(<%=[^%>]*\"checked\"[^%>]*%>)([\\s\\S]*)";
    }

    public String toString() {
        StringBuffer retStr = new StringBuffer();
        retStr.append("<");
        if (this.getType() == STRUTS) {
            retStr.append(this.getTagPrefix());
            retStr.append("radio");
        } else {
            retStr.append("input type=\"radio\"");
        }
        if (this.name.length() > 0) {
            retStr.append(" name=\"");
            retStr.append(this.name);
            retStr.append("\"");
        }
        if (this.getType() == STRUTS && this.property.length() > 0) {
            retStr.append(" property=\"" + this.property + "\"");
        }
        if (this.value.length() > 0) {
            retStr.append(" value=\"");
            retStr.append(this.value);
            retStr.append("\"");
        }
        if (this.getType() != STRUTS && this.checked.length() > 0) {
            retStr.append(" ");
            retStr.append(this.checked);
        }
        if (this.otherProperties.length() > 0) {
            retStr.append(" ");
            retStr.append(this.otherProperties);
        }
        if (this.getType() != STRUTS) {
            retStr.append(" id=\"");
            retStr.append(this.metaId);
            retStr.append("\"");
        }
        if (this.getType() == STRUTS) {
            retStr.append("/");
        }
        retStr.append(">");
        if (this.getType() == STRUTS) {
            retStr.append("<%--id=\"" + this.metaId + "\"--%>");
        }
        if (this.getIsDemo() && this.getGenDemo()) {
            retStr.append("<!--_begin_demo-->");
            retStr.append("<input type=\"radio\" ");
            retStr.append(this.demoOtherProperties);
            retStr.append(">");
            retStr.append("<!--_end_demo-->");
        }
        return retStr.toString();
    }
}
