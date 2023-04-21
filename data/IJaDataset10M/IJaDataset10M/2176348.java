package org.gvsig.remoteClient.wfs.exceptions;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 */
public class WFSException extends Exception {

    private static final long serialVersionUID = 1476084093500156070L;

    private String wfsCode = null;

    protected String formatString;

    protected String messageKey;

    protected long code;

    public WFSException() {
        init();
    }

    public WFSException(Throwable cause) {
        init();
        initCause(cause);
    }

    public WFSException(String wfsCode, String message) {
        init();
        formatString = message;
        this.wfsCode = wfsCode;
    }

    protected Map values() {
        Hashtable params;
        params = new Hashtable();
        return params;
    }

    public void init() {
        messageKey = "wfs_exception";
        formatString = "WFS Exception";
        code = serialVersionUID;
    }

    /**
	 * @return Returns the wfsCode.
	 */
    public String getWfsCode() {
        return wfsCode;
    }
}
