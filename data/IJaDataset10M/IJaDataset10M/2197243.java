package jvc.web.module;

import org.jdom.Element;

/**
 * <p>Title :���ʽ��װ</p>
 * <p>Description:</p>
 * <p>Created on 2005-5-20</p>
 * <p>Company :JVC</p>
 *  @author : Ru_fj
 *  @version : 1.0
 */
public class Expression {

    private String Value;

    private String msg;

    public Expression(Element e) {
        Value = e.getAttributeValue("value");
        msg = e.getAttributeValue("msg");
    }

    /**
	 * @return Returns the value.
	 */
    public String getValue() {
        return Value;
    }

    public String getMsg() {
        return msg;
    }
}
