package org.gvsig.gpe.gml.exceptions;

import java.util.Hashtable;
import java.util.Map;
import org.gvsig.exceptions.BaseException;

/**
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 */
public class GMLNoGeometryException extends BaseException {

    private static final long serialVersionUID = 650484131058581045L;

    private String geom;

    public GMLNoGeometryException(int geometry, Throwable exception) {
        super();
        this.init();
        this.geom = new Integer(geometry).toString();
        initCause(exception);
    }

    protected Map values() {
        Hashtable params;
        params = new Hashtable();
        params.put("pos", geom);
        return params;
    }

    public void init() {
        messageKey = "Gml_Geometry_Error";
        formatString = "Geometry %(geom) not found or invalid";
        code = serialVersionUID;
    }
}
