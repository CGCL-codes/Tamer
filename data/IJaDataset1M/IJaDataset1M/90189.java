package com.juanfrivaldes.cio2005.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import com.juanfrivaldes.cio2005.domain.Usuario;

/**
 * @author root
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AltaPonenciaFinalForm extends ActionForm {

    private static Log log = LogFactory.getLog(AltaPonenciaFinalForm.class);

    private String tipo = "ponencia";

    private FormFile archivo;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return Returns the coautor.
	 */
    public String getTipo() {
        return tipo;
    }

    /**
	 * @param coautor The coautor to set.
	 */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 3256723961709410102L;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        tipo = "ponencia";
        archivo = null;
        id = 0;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (archivo == null || archivo.getFileName().equals("") || archivo.getFileSize() == 0) {
            errors.add("resumen", new ActionMessage("altaPonencia.file.problema"));
            return errors;
        }
        String name = archivo.getFileName();
        log.trace("Fichero " + name.substring(name.lastIndexOf('.')).toUpperCase());
        if (!name.substring(name.lastIndexOf('.')).toUpperCase().equals(".PDF")) {
            errors.add("resumen", new ActionMessage("altaPonencia.noPdf.problema"));
            return errors;
        }
        if (this.archivo != null) {
            Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
            if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                log.trace("se excedio el tamaño maximo de fichero");
                errors.add("resumen", new ActionMessage("altaPonencia.tamanio.problema"));
                return errors;
            }
        }
        return errors;
    }

    /**
	 * @return Returns the resumen.
	 */
    public FormFile getArchivo() {
        return archivo;
    }

    /**
	 * @param resumen The resumen to set.
	 */
    public void setArchivo(FormFile resumen) {
        this.archivo = resumen;
    }
}
