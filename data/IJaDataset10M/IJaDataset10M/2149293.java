package org.wdcode.common.constants;

/**
 * HTML页面常量
 * @author WD
 * @since JDK6
 * @version 1.0 2009-11-21
 */
public final class HtmlConstants {

    /**
	 * HTML格式 text/html;
	 */
    public static final String TEXT_HTML;

    /**
	 * HTML格式 text/html; charset=UTF-8
	 */
    public static final String TEXT_HTML_UTF_8;

    /**
	 * HTML格式 text/html; charset=GBK
	 */
    public static final String TEXT_HTML_GBK;

    /**
	 * 静态初始化
	 */
    static {
        TEXT_HTML = "text/html;";
        TEXT_HTML_UTF_8 = "text/html; charset=UTF-8";
        TEXT_HTML_GBK = "text/html; charset=GBK";
    }

    /**
	 * "&"的转义字符"&amp;"
	 */
    public static final String ESC_AMP;

    /**
	 * "<"的转义字符"&lt;"
	 */
    public static final String ESC_LT;

    /**
	 * ">"的转义字符"&gt;"
	 */
    public static final String ESC_GT;

    /**
	 * "\""的转义字符"&quot;"
	 */
    public static final String ESC_QUOT;

    /**
	 * 静态初始化
	 */
    static {
        ESC_AMP = "&amp;";
        ESC_LT = "&lt;";
        ESC_GT = "&gt;";
        ESC_QUOT = "&quot;";
    }

    /**
	 * 私有构造禁止外部实例化
	 */
    private HtmlConstants() {
    }
}
