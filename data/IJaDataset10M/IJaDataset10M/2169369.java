package org.dcm4chee.xero.search.macro;

import java.util.Map;
import javax.xml.namespace.QName;
import org.dcm4chee.xero.search.study.Macro;

/**
 * Indicates that the the element has Key Object and it references images having
 * GSPS UIDs.
 * Attribute : kogsps=true
 * 
 * @author smohan
 * 
 */
public class KeyObjectGspsMacro implements Macro {

    public static final QName Q_KEY = new QName(null, "koGSPS");

    private String state;

    public KeyObjectGspsMacro(String keyObject) {
        if (keyObject == null) throw new NullPointerException("KeyObject specified must not be null.");
        this.state = keyObject;
    }

    public int updateAny(Map<QName, String> attrs) {
        attrs.put(Q_KEY, state);
        return 1;
    }

    /**
	 * Indicates if the given image is a key object
	 */
    public String getKeyObject() {
        return state;
    }
}
