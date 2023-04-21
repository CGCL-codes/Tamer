package org.gvsig.gpe.kml.exceptions;

import java.util.Hashtable;
import java.util.Map;
import javax.xml.namespace.QName;
import org.gvsig.exceptions.BaseException;

/**
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 */
public class KmlNotRootTagException extends BaseException {

    private static final long serialVersionUID = -8296305437981111079L;

    private QName tag = null;

    public KmlNotRootTagException(QName tag) {
        this.tag = tag;
        init();
    }

    public KmlNotRootTagException(QName tag, Throwable exception) {
        this.tag = tag;
        init();
        initCause(exception);
    }

    private void init() {
        messageKey = "error_kml_root_tag";
        formatString = "The root element is %(tag) and must be kml";
        code = serialVersionUID;
    }

    protected Map values() {
        Hashtable params = new Hashtable();
        params.put("tag", tag);
        return params;
    }
}
