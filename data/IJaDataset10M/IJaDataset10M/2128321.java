package net.simpleframework.util;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Option extends TextObject {

    private static final long serialVersionUID = 902846958952694700L;

    public Option(final String name) {
        this(name, name);
    }

    public Option(final String name, final String text) {
        super(name, text);
    }
}
