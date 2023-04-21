package cn.adamkts.core;

/**
 * 常用正则表达式校验字符串
 * @author Adam
 *
 */
public class RegexValidation {

    /**
	 * 验证每一位字符都是数字：
	 */
    public static boolean isNumberByEachCharacter(String str) {
        return str.matches("^[0-9]*$");
    }

    /**
	 * 验证是一个整数或浮点数
	 * @param str
	 * @return
	 */
    public static boolean isNumber(String str) {
        return str.matches("^(-?\\d+)(\\.\\d+)?");
    }

    /**
	 * 验证是验证n位的数字
	 * @param str
	 * @return
	 */
    public static boolean isNumber(String str, int n) {
        return str.matches("^\\d{" + n + "}$");
    }

    /**
	 * 验证至少n位数字
	 * @param str
	 * @param n
	 * @return
	 */
    public static boolean isNumberLeate(String str, int n) {
        return str.matches("^\\d{" + n + ",}$");
    }

    /**
	 * 验证m-n位的数字
	 * @param str
	 * @param min
	 * @param max
	 * @return
	 */
    public static boolean isNumberBetween(String str, int min, int max) {
        return str.matches("^\\d{" + min + "," + max + "}$");
    }

    /**
	 * 验证零和非零开头的数字
	 * @param str
	 * @return
	 */
    public static boolean isInteger(String str) {
        return str.matches("^(0|[1-9][0-9]*)$");
    }

    /**
	 * 验证有两位小数的正实数
	 * @param str
	 * @return
	 */
    public static boolean isRealNumber(String str) {
        return str.matches("^[0-9]+(.[0-9]{2})?$");
    }

    /**
	 * 验证有1-3位小数的正实数
	 * @param str
	 * @return
	 */
    public static boolean isDecimalFraction(String str) {
        return str.matches("^[0-9]+(.[0-9]{1,3})?$");
    }

    /**
	 * 验证非零的正整数
	 * @param str
	 * @return
	 */
    public static boolean isNonzeroInteger(String str) {
        return str.matches("^\\+?[1-9][0-9]*$");
    }

    /**
	 * 验证非零的负整数
	 * @param str
	 * @return
	 */
    public static boolean isNonzeroBearIntrger(String str) {
        return str.matches("^\\-[1-9][0-9]*$");
    }

    /**
	 * 验证非负整数（正整数 + 0）
	 * @param str
	 * @return
	 */
    public static boolean isNotBearIntrger(String str) {
        return str.matches("^\\d+$");
    }

    /**
	 * 验证非正整数（负整数 + 0）
	 * @param str
	 * @return
	 */
    public static boolean isNotIntrger(String str) {
        return str.matches("^((-\\d+)|(0+))$");
    }

    /**
	 * 验证长度为3的字符
	 * @param str
	 * @return
	 */
    public static boolean islength3(String str) {
        return str.matches("^.{3}$");
    }

    /**
	 * 验证由26个英文字母组成的字符串
	 * @param str
	 * @return
	 */
    public static boolean isValidateString(String str) {
        return str.matches("^[A-Za-z]+$");
    }

    /**
	 * 验证由26个大写英文字母组成的字符串
	 * @param str
	 * @return
	 */
    public static boolean isCapitalization(String str) {
        return str.matches("^[A-Z]+$");
    }

    /**
	 * 验证由26个小写英文字母组成的字符串
	 * @param str
	 * @return
	 */
    public static boolean isLowercase(String str) {
        return str.matches("^[a-z]+$");
    }

    /**
	 * 验证由数字和26个英文字母组成的字符串
	 * @param str
	 * @return
	 */
    public static boolean isNumberString(String str) {
        return str.matches("^[A-Za-z0-9]+$");
    }

    /**
	 * 验证由数字、26个英文字母或者下划线组成的字符串
	 * @param str
	 * @return
	 */
    public static boolean isUnderline(String str) {
        return str.matches("^\\w+$");
    }

    /**
	 * 验证用户密码
	 * 正确格式为：以字母开头，长度在6-18之间，只能包含字符、数字和下划线。
	 * @param str
	 * @return
	 */
    public static boolean isPassword(String str) {
        return str.matches("^[a-zA-Z]\\w{5,17}$");
    }

    /**
	 * 验证是否含有 ^%&',;=?$\" 等字符
	 * @param str
	 * @return
	 */
    public static boolean isCharacter(String str) {
        return str.matches("[^%&',;=?$\\x22]+");
    }

    /**
	 * 验证汉字
	 * @param str
	 * @return
	 */
    public static boolean isChineseharacters(String str) {
        return str.matches("^[一-龥],{0,}$");
    }

    /**
	 * 验证Email地址
	 * @param str
	 * @return
	 */
    public static boolean isEmail(String str) {
        return str.matches("^[a-zA-Z0-9_]+@([a-zA-Z0-9][a-zA-Z0-9]+.([a-zA-Z0-9.]*)[a-zA-Z]{2,5})$");
    }

    /**
	 * 验证InternetURL
	 * @param str
	 * @return
	 */
    public static boolean isURL(String str) {
        return str.matches("^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$ ；^[a-zA-z]+://(w+(-w+)*)(.(w+(-w+)*))*(?S*)?$");
    }

    /**
	 * 验证电话号码
	 * 正确格式为：XXXX-XXXXXXX，XXXX-XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX
	 * @param str
	 * @return
	 */
    public static boolean isPhoneNumber(String str) {
        return str.matches("^(\\(\\d{3,4}\\)|\\d{3,4}-)?\\d{7,8}$");
    }

    /**
	 * 验证身份证号（15位或18位数字）
	 * @param str
	 * @return
	 */
    public static boolean isIdentityCard(String str) {
        return str.matches("^\\d{15}|\\d{}18$");
    }

    /**
	 * 验证一年的12个月
	 * 正确格式为：“01”-“09”和“1”“12”
	 * @param str
	 * @return
	 */
    public static boolean isMonth(String str) {
        return str.matches("^(0?[1-9]|1[0-2])$");
    }

    /**
	 * 验证一个月的31天
	 * 正确格式为：01、09和1、31
	 * @param str
	 * @return
	 */
    public static boolean isDay(String str) {
        return str.matches("^((0?[1-9])|((1|2)[0-9])|30|31)$");
    }

    /**
	 * 整数
	 * @param str
	 * @return
	 */
    public static boolean isInt(String str) {
        return str.matches("^-?\\d+$");
    }

    /**
	 * 非负浮点数（正浮点数 + 0）
	 * @param str
	 * @return
	 */
    public static boolean isNotBearDouble(String str) {
        return str.matches("^\\d+(\\.\\d+)?$");
    }

    /**
	 * 正浮点数
	 * @param str
	 * @return
	 */
    public static boolean isJustDouble(String str) {
        return str.matches("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
    }

    /**
	 * 非正浮点数（负浮点数 + 0）
	 * @param str
	 * @return
	 */
    public static boolean isNotJustDouble(String str) {
        return str.matches("^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$");
    }

    /**
	 * 负浮点数
	 * @param str
	 * @return
	 */
    public static boolean isBearDouble(String str) {
        return str.matches(" ^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$");
    }

    /**
	 * 不为空
	 * @param str
	 */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.equals("");
    }

    /**
	 * 为空
	 * @param str
	 */
    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    /**
	 * 是否是SHA1密文
	 * @param str
	 * @return
	 */
    public static boolean isSha1(String str) {
        return str.matches("^[A-fa-f0-9]{40}$");
    }
}
