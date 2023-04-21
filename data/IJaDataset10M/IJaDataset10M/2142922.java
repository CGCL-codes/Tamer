package xbird.servlet.webdav;

import xbird.servlet.webdav.methods.*;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class DavMethodFactory {

    private static final String METHOD_HEAD = "HEAD";

    private static final String METHOD_PROPFIND = "PROPFIND";

    private static final String METHOD_PROPPATCH = "PROPPATCH";

    private static final String METHOD_MKCOL = "MKCOL";

    private static final String METHOD_COPY = "COPY";

    private static final String METHOD_MOVE = "MOVE";

    private static final String METHOD_LOCK = "LOCK";

    private static final String METHOD_UNLOCK = "UNLOCK";

    private DavMethodFactory() {
    }

    public static DavMethod create(String methodName) {
        if (METHOD_HEAD.equals(methodName)) {
            return new Head();
        } else if (METHOD_PROPFIND.equals(methodName)) {
            return new Propfind();
        } else if (METHOD_PROPPATCH.equals(methodName)) {
            return new Proppatch();
        } else if (METHOD_MKCOL.equals(methodName)) {
            return new Mkcol();
        } else if (METHOD_COPY.equals(methodName)) {
            return new Copy();
        } else if (METHOD_MOVE.equals(methodName)) {
            return new Move();
        } else if (METHOD_LOCK.equals(methodName)) {
            return new Lock();
        } else if (METHOD_UNLOCK.equals(methodName)) {
            return new Unlock();
        }
        return null;
    }
}
