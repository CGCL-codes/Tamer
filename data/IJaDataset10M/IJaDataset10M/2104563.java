package net.sf.cyberrails.Util.PathUtils;

/** NoSuchPathException signals that there is no path between
 * two nodes in a graph.
 **/
public class NoSuchPathException extends Exception {

    public NoSuchPathException() {
        super();
    }

    public NoSuchPathException(String arg0) {
        super(arg0);
    }

    public NoSuchPathException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public NoSuchPathException(Throwable arg0) {
        super(arg0);
    }

    public NoSuchPathException(Object start, Object end) {
        super("No path from start node: \"" + String.valueOf(start) + "\" to end node: \"" + String.valueOf(end) + "\"");
    }
}
