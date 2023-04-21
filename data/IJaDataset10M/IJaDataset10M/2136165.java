package net.sf.yavtags.rules;

import java.text.ParseException;
import java.util.List;
import net.sf.yavtags.YavConfig;
import net.sf.yavtags.YavError;
import net.sf.yavtags.util.Misc;

public class MaxLength extends YavRule {

    private int maxLength;

    public MaxLength(String rule, List<YavRule> yavRules, YavConfig yavConfig) throws java.text.ParseException {
        super(rule, yavRules, yavConfig);
        super.i18nKeys[0] = "MAXLENGTH_MSG";
        try {
            maxLength = java.lang.Integer.parseInt(getRuleSuffix());
        } catch (NumberFormatException ex) {
            throw new ParseException(getRuleSuffix(), 0);
        }
    }

    @Override
    public YavError checkError(String value) {
        if (!Misc.isEmptyOrNull(value)) {
            if (value.length() > maxLength) {
                return new YavError(this.getFieldName(), this.getFieldLabel() + " must be no more than " + maxLength + " characters long.");
            }
        }
        return null;
    }

    /** If this rule has a second part after rule type. */
    @Override
    protected boolean hasRuleSuffix() {
        return true;
    }

    /** If this rule has a final help message. */
    @Override
    protected boolean hasRuleMessage() {
        return true;
    }
}
