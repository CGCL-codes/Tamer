package org.authorsite.bib.web.struts.form;

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.*;
import org.authorsite.bib.web.util.*;

/**
 *
 * @author  jejking
 * @version $Revision: 1.2 $
 */
public class MediaItemGetDetailsForm extends ActionForm {

    private String mediaItemID;

    public String getMediaItemID() {
        return mediaItemID;
    }

    public void setMediaItemID(String newMediaItemID) {
        mediaItemID = newMediaItemID;
    }

    /** Creates a new instance of MediaItemGetDetailsForm */
    public MediaItemGetDetailsForm() {
    }

    /**
     * Simply checks that the submitted mediaItemID parameter is a valid integer
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
        ActionErrors errors = new ActionErrors();
        if (!InputChecker.isInteger(mediaItemID)) {
            ActionError error = new ActionError("web.errors.notAnInteger", "mediaItemID");
            errors.add(ActionErrors.GLOBAL_ERROR, error);
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        resetFields();
    }

    private void resetFields() {
        mediaItemID = "";
    }
}
