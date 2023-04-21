package cn.sduo.app.validator;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;
import cn.sduo.app.util.DateUtils;
import cn.sduo.app.util.lang.ChineseUtil;
import cn.sduo.app.util.lang.StringUtil;

public class ValidateUtil {

    public static boolean isEmail(String email) {
        if (StringUtil.isEmptyString(email)) {
            return false;
        }
        email = email.toLowerCase();
        String pattern = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|" + "[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|" + "((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?" + "(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|" + "(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*" + "(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))" + "@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|" + "(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|" + "~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|" + "\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*" + "([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$";
        if (!email.matches(pattern)) {
            return false;
        }
        return true;
    }

    public static boolean hasChinese(String content) {
        if (StringUtil.isEmptyString(content)) {
            return false;
        }
        int len1 = content.length();
        String newString = "";
        try {
            newString = new String(content.getBytes(), "8859_1");
        } catch (UnsupportedEncodingException e) {
        }
        int len2 = newString.length();
        return len1 != len2;
    }

    /**
	 * Check the string is valid or not.Treat as valid with null string.
	 * @param data
	 * @return
	 */
    public static boolean isValidCharacter(String data) {
        if (StringUtil.isEmptyString(data)) {
            return true;
        }
        if (ChineseUtil.isChineseAlphaNumeric(data)) {
            return true;
        }
        if (isEuropean(data) || isRussian(data)) {
            return true;
        }
        return false;
    }

    /**
	 * Check the string is European or not.Treat as valid with null string.
	 * @param data
	 * @return
	 */
    private static boolean isEuropean(String data) {
        if (StringUtil.isEmptyString(data)) {
            return true;
        }
        for (int i = 0; i < data.length(); i++) {
            if (data.codePointAt(i) > 255) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Check the string is Russian or not.Treat as valid with null string.
	 * @param data
	 * @return
	 */
    private static boolean isRussian(String data) {
        if (StringUtil.isEmptyString(data)) {
            return true;
        }
        for (int i = 0; i < data.length(); i++) {
            int codePoint = data.codePointAt(i);
            System.out.println("codePoint" + codePoint);
            if (codePoint > 255 && (codePoint < 1024 || codePoint > 1280)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Check the string is valid date or not.
	 * @param dateStr
	 * @param format
	 * @return
	 */
    public static boolean isDate(String dateStr, String dateFormat) {
        if (StringUtil.isEmptyString(dateStr) || StringUtil.isEmptyString(dateFormat)) {
            return false;
        }
        try {
            Date dateValue = DateUtils.parseDate(dateStr, dateFormat);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
	 * Validate number with format 
	 * @param value
	 * @param precision
	 * @param scale
	 * @return
	 */
    public static boolean isValidNumberWithFormat(String value, int precision, int scale) {
        if (StringUtil.isEmptyString(value)) {
            return true;
        }
        String pattern = "^([1-9]\\d{0," + (precision - scale) + "}|0)(\\.\\d{1," + scale + "})?$";
        if (!Pattern.matches(pattern, value)) {
            return false;
        }
        String[] valueArray = value.split("\\.");
        if (valueArray != null && valueArray.length == 2) {
            valueArray[1] = StringUtil.removeTailZero(valueArray[1]);
            if (StringUtil.isEmptyString(valueArray[1])) {
                value = valueArray[0];
            } else {
                value = valueArray[0] + valueArray[1];
            }
        }
        String valueTem = value.replace(".", "");
        if (!StringUtil.isEmptyString(valueTem) && valueTem.length() > (precision - scale)) {
            return false;
        }
        return true;
    }

    public static boolean isValidYYYY(String yyyy) {
        if (StringUtil.isEmptyString(yyyy)) return false;
        if (yyyy.length() < 4 || !StringUtil.isAllDigit(yyyy)) {
            return false;
        }
        return true;
    }

    public static boolean isValidMM(String mm) {
        if (StringUtil.isEmptyString(mm)) return false;
        if (!StringUtil.isAllDigit(mm)) {
            return false;
        }
        int month = Integer.valueOf(mm);
        if (month < 1 || month > 12) {
            return false;
        }
        return true;
    }

    public static boolean isValidDD(String dd) {
        if (StringUtil.isEmptyString(dd)) return false;
        if (!StringUtil.isAllDigit(dd)) {
            return false;
        }
        int day = Integer.valueOf(dd);
        if (day < 1 || day > 31) {
            return false;
        }
        return true;
    }

    public static boolean isInt(String value, int maxLength) {
        if (StringUtil.isEmptyString(value) || value.length() > maxLength) {
            return false;
        }
        return isInt(value);
    }

    public static boolean isInt(String value) {
        if (StringUtil.isEmptyString(value)) {
            return false;
        }
        if (StringUtil.isAllDigit(value)) {
            if (value.length() == 1) {
                return true;
            }
            if ("0".equals(value.substring(0, 1))) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] arg) {
    }
}
