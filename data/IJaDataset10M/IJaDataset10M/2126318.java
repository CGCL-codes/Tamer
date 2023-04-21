package graphlab.library;

/**
 * @author Omid
 *
 */
public class BaseVertexProperties implements Cloneable {

    public int color;

    public boolean mark;

    /**
     * You can store anything you want.
     */
    public Object obj;

    public BaseVertexProperties(BaseVertexProperties p) {
        color = p.color;
        mark = p.mark;
    }

    public BaseVertexProperties(int color, boolean mark) {
        this.color = color;
        this.mark = mark;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseVertexProperties)) return false;
        BaseVertexProperties b = (BaseVertexProperties) obj;
        return b.color == color;
    }
}
