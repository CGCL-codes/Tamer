package com.izforge.izpack.util;

import com.izforge.izpack.panels.PasswordGroup;
import com.izforge.izpack.panels.ProcessingClient;
import com.izforge.izpack.panels.Validator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A validator to enforce non-empty fields.
 * <p/>
 * This validator can be used for rule input fields in the UserInputPanel to make sure that the
 * user's entry matches a specified regular expression.
 *
 * @author Mike Cunneen <mike dot cunneen at screwfix dot com>
 */
public class RegularExpressionValidator implements Validator {

    public static final String STR_PATTERN_DEFAULT = "[a-zA-Z0-9._-]{3,}@[a-zA-Z0-9._-]+([.][a-zA-Z0-9_-]+)*[.][a-zA-Z0-9._-]{2,4}";

    private static final String PATTERN_PARAM = "pattern";

    public boolean validate(ProcessingClient client) {
        String patternString;
        if (client.hasParams()) {
            Map<String, String> paramMap = client.getValidatorParams();
            patternString = paramMap.get(PATTERN_PARAM);
        } else {
            patternString = STR_PATTERN_DEFAULT;
        }
        Pattern pattern = Pattern.compile(patternString);
        return pattern.matcher(getString(client)).matches();
    }

    private String getString(ProcessingClient client) {
        String returnValue = "";
        if (client instanceof PasswordGroup) {
            int numFields = client.getNumFields();
            if (numFields > 0) {
                returnValue = client.getFieldContents(0);
            } else {
                returnValue = client.getText();
            }
        } else {
            returnValue = client.getText();
        }
        return returnValue;
    }
}
