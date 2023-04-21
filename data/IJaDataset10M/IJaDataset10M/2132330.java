package org.gvsig.remoteClient.gml.warnings;

import java.util.Hashtable;
import java.util.Map;
import org.gvsig.remoteClient.gml.exceptions.GMLException;

/**
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 */
public class GMLWarningWrongNamespace extends GMLException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5960588828358796702L;

    private String namespace;

    public GMLWarningWrongNamespace(String key) {
        super();
        this.init();
        this.namespace = key;
    }

    protected Map values() {
        Hashtable params;
        params = new Hashtable();
        params.put("namespace", namespace);
        return params;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void init() {
        messageKey = "gml_warning_wrong_Namespace";
        formatString = "Invalid Tag Ignored, not found in %(namespace) namespace";
        code = serialVersionUID;
    }
}
