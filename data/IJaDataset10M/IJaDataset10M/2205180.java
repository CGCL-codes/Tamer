package net.simpleframework.content;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractContent extends AbstractContentBase {

    private short mark;

    private boolean ttop;

    public short getMark() {
        return mark;
    }

    public void setMark(final short mark) {
        this.mark = mark;
    }

    public boolean isTtop() {
        return ttop;
    }

    public void setTtop(final boolean ttop) {
        this.ttop = ttop;
    }
}
