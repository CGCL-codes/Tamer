package com.hk.frame.util;

public class HkValidate {

    /**
	 * 验证数据，如果数据为空或者长度超出范围，就是验证失败
	 * 
	 * @param value 需要验证的数据
	 * @param toText 是否把 html转换为纯文本再验证
	 * @param maxLen 最大长度
	 * @return true:验证通过,false:验证失败
	 *         2010-6-8
	 */
    public static boolean validateEmptyAndLength(String value, boolean toText, int maxLen) {
        if (DataUtil.isEmpty(value)) {
            return false;
        }
        String s = null;
        if (toText) {
            s = DataUtil.toText(value);
        } else {
            s = value;
        }
        return validateLength(s, maxLen);
    }

    public static boolean validateEmptyAndLength(String value, boolean toText, int minLen, int maxLen) {
        if (DataUtil.isEmpty(value)) {
            return false;
        }
        String s = null;
        if (toText) {
            s = DataUtil.toText(value);
        } else {
            s = value;
        }
        if (s.length() < minLen) {
            return false;
        }
        return validateLength(s, maxLen);
    }

    /**
	 * 验证数据长度，如果数据为空或者数据长度在范围之内，验证是成功的
	 * 
	 * @param value 需要验证的数据
	 * @param toText 是否把 html转换为纯文本再验证
	 * @param maxLen 最大长度
	 * @return true:验证通过,false:验证失败
	 *         2010-6-8
	 */
    public static boolean validateLength(String value, boolean toText, int maxLen) {
        if (DataUtil.isEmpty(value)) {
            return true;
        }
        String s = null;
        if (toText) {
            s = DataUtil.toText(value);
        } else {
            s = value;
        }
        return validateLength(s, maxLen);
    }

    public static boolean validateLength(String value, int maxLen) {
        if (DataUtil.isEmpty(value)) {
            return true;
        }
        if (value.length() > maxLen) {
            return false;
        }
        return true;
    }
}
