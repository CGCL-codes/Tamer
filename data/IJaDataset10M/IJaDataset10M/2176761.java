package mem;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: adenysenko
 * Date: 31/1/2008
 * Time: 18:53:56
 * To change this template use File | Settings | File Templates.
 */
public class ViewDepthElem implements Serializable {

    String key;

    int depth;

    public ViewDepthElem(int depth, String key) {
        this.depth = depth;
        this.key = key;
    }

    public int getDepth() {
        return depth;
    }

    public String toString() {
        return key + "@" + depth;
    }

    public boolean isCommand() {
        return Hist.CMD_KEY.equals(key);
    }

    public boolean isView() {
        return !isCommand();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewDepthElem that = (ViewDepthElem) o;
        if (depth != that.depth) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (key != null ? key.hashCode() : 0);
        result = 31 * result + depth;
        return result;
    }
}
