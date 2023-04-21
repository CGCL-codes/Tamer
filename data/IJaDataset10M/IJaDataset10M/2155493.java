package org.gvsig.fmap.geometries.operation;

import java.util.HashMap;
import java.util.Map;
import org.gvsig.exceptions.BaseException;

/**
 * @author Jorge Piera Llodr� (jorge.piera@iver.es)
 */
public class GeometryOperationException extends BaseException {

    private static final long serialVersionUID = 1L;

    private int geometryType = -1;

    private int operationCode = -1;

    public GeometryOperationException(int geometryType, int operationCode) {
        this.geometryType = geometryType;
        this.operationCode = operationCode;
    }

    /**
	 * Initializes some values
	 */
    public void init() {
        messageKey = "geometries_opeartion_exception";
        formatString = "Exception executing the operation with code %(operationCode) " + "for the geometry with type %(geometryType).";
        code = serialVersionUID;
    }

    protected Map values() {
        HashMap map = new HashMap();
        map.put("geometryType", geometryType);
        map.put("operationCode", operationCode);
        return map;
    }
}
