package org.gvsig.gpe.gml.exceptions;

import java.util.Map;
import org.gvsig.exceptions.BaseException;

/**
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 */
public class GMLFileReadException extends BaseException {

    private static final long serialVersionUID = 6522759980715442218L;

    /**
	 * @param args
	 */
    public GMLFileReadException(Throwable error) {
        this.init();
        initCause(error);
    }

    protected Map values() {
        return null;
    }

    public void init() {
        messageKey = "Gml_File_Read_Error";
        formatString = "Error Reading GML File";
        code = serialVersionUID;
    }
}
