package net.solosky.maplefetion.bean;

import net.solosky.maplefetion.util.StringHelper;

/**
 *
 * 聊天消息
 * 
 * 聊天的内容是很丰富的，有字体，颜色，大小，图片等。。
 * 这里暂时只对字体，颜色，大小做支持，其他的逐步添加
 *
 * @author solosky <solosky772@qq.com>
 */
public class Message {

    /**
	 * 无格式文本
	 */
    public static final String TYPE_PLAIN = "text/plain";

    /**
	 * HTML文本
	 */
    public static final String TYPE_HTML = "text/html-fragment";

    /**
	 * 消息类型
	 *  plain 或者 html 
	 */
    private String type;

    /**
	 * 消息正文
	 */
    private String content;

    /**
	 * 默认构造函数
	 */
    public Message() {
        this("", Message.TYPE_HTML);
    }

    /** 
	 * 以一个无格式的字符串构造消息
	 */
    public Message(String content) {
        this(content, Message.TYPE_HTML);
    }

    /**
	 * 详细的构造函数
	 * @param content 消息内容
	 * @param type		消息类型常量，定义在Message中
	 */
    public Message(String content, String type) {
        this.content = content;
        this.type = type;
        if (type == null || (!type.equals(TYPE_HTML) && !type.equals(TYPE_PLAIN))) {
            throw new IllegalArgumentException("invalid message type. message type must be Message.TYPE_HTML or Message.TYPE_PLAIN.");
        }
    }

    /**
	 * 返回去掉文本格式的消息内容
     * @return the text
     */
    public String getText() {
        return type.equals(TYPE_HTML) ? StringHelper.stripHtmlSpecialChars(content) : content;
    }

    /**
     * 返回消息的类型
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 返回消息的完整内容
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * ToString
     */
    public String toString() {
        return this.content;
    }

    /**
     * 使用默认的格式封装普通消息
     * @param plain		普通文本消息
     * @return
     */
    public static Message wrap(String plain) {
        return new Message("<Font Face='宋体' Color='-16777216' Size='10'>" + StringHelper.qouteHtmlSpecialChars(plain) + "</Font> ", Message.TYPE_HTML);
    }
}
