package com.velocityme.www.actionforms;

import com.velocityme.interfaces.ContactablePK;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

/**
 *
 * @author  Robert
 */
public class ReassignResponsibilityActionForm extends ActionForm {

    private String m_message;

    private Integer[] m_allResponsibleContactableIds;

    private Integer[] m_responsibleContactableIds;

    private FormFile m_file;

    /** Creates a new instance of ReassignResponsibilityActionForm */
    public ReassignResponsibilityActionForm() {
        m_responsibleContactableIds = new Integer[0];
    }

    public String getMessage() {
        return m_message;
    }

    public void setMessage(String message) {
        m_message = message;
    }

    public Integer[] getAllResponsibleContactableIds() {
        return m_allResponsibleContactableIds;
    }

    public void setAllResponsibleContactableIds(Integer[] allResponsibleContactableIds) {
        m_allResponsibleContactableIds = allResponsibleContactableIds;
    }

    public Integer[] getResponsibleContactableIds() {
        return m_responsibleContactableIds;
    }

    public void setResponsibleContactableIds(Integer[] responsibleContactableIds) {
        m_responsibleContactableIds = responsibleContactableIds;
    }

    public FormFile getFile() {
        return m_file;
    }

    public void setFile(FormFile file) {
        m_file = file;
    }

    /**
     * Check to make sure the client hasn't exceeded the maximum allowed upload size inside of this
     * validate method.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = null;
        Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
        if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
            errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.file.maxLengthExceeded"));
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.file.maxLengthExplanation"));
        }
        return errors;
    }

    public ContactablePK[] getResponsibleContactablePKs() {
        ContactablePK[] contactablePKs = new ContactablePK[m_responsibleContactableIds.length];
        for (int i = 0; i < m_responsibleContactableIds.length; i++) {
            contactablePKs[i] = new ContactablePK(m_responsibleContactableIds[i]);
        }
        return contactablePKs;
    }
}
