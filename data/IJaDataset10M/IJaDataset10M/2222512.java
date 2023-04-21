package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.page.ApplicationPage;
import com.codeforces.graygoose.validation.ResponseCodesValidator;
import org.nocturne.main.Component;
import org.nocturne.validation.*;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RuleTypeUtil {

    public static void setupRuleProperties(Rule rule, Component component) {
        SortedMap<String, String> properties = new TreeMap<String, String>();
        for (String property : rule.getRuleType().getPropertyNames()) {
            properties.put(property, component.getString(property));
        }
        rule.setData(properties);
    }

    public static void addRulePropertyValidators(ApplicationPage page, String ruleTypeName) {
        if (Rule.RuleType.RESPONSE_CODE_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("expectedCodes", new RequiredValidator());
            page.addValidator("expectedCodes", new ResponseCodesValidator());
        } else if (Rule.RuleType.SUBSTRING_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("expectedSubstring", new RequiredValidator());
            page.addValidator("expectedSubstring", new LengthValidator(1, 256));
            page.addValidator("expectedSubstringMinimalCount", new RequiredValidator());
            page.addValidator("expectedSubstringMinimalCount", new IntegerValidator(0, 1024));
            page.addValidator("expectedSubstringMaximalCount", new RequiredValidator());
            page.addValidator("expectedSubstringMaximalCount", new IntegerValidator(0, 1024));
        } else if (Rule.RuleType.REGEX_RULE_TYPE.toString().equals(ruleTypeName)) {
            page.addValidator("expectedRegex", new RequiredValidator());
            page.addValidator("expectedRegex", new LengthValidator(1, 512));
            page.addValidator("expectedRegex", new Validator() {

                @Override
                public void run(String value) throws ValidationException {
                    try {
                        Pattern.compile(value);
                    } catch (PatternSyntaxException e) {
                        throw new ValidationException("Illegal regex: " + e.getMessage());
                    }
                }
            });
        }
    }

    private RuleTypeUtil() {
    }
}
