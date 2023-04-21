package jshomeorg.simplytrain.toolset;

import jshomeorg.simplytrain.service.path;
import jshomeorg.simplytrain.service.trackObjects.pathableObject;

/**
 *
 * @author js
 */
public class pathInfo {

    public static final int CODE_SET = 1;

    public static final int CODE_NOTSET = 2;

    public static final int CODE_LONGNOTSET = 3;

    pathableObject p = null;

    String msg = null;

    int code = 0;

    /** Creates a new instance of pathInfo */
    public pathInfo(pathableObject _p, String _msg, int _code) {
        p = _p;
        msg = _msg;
        code = _code;
    }

    public pathInfo(pathableObject _p, int _code, String _msg) {
        p = _p;
        msg = _msg;
        code = _code;
    }

    /**
	 * 
	 * @return Signal, may be null!!
	 */
    public pathableObject getSignal() {
        return p;
    }

    public String getMessage() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
