package org.esk.dablog.web.forms;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindException;
import org.esk.dablog.service.EntryManager;
import org.esk.dablog.model.Entry;
import org.esk.dablog.model.Picture;
import org.esk.dablog.exceptions.ParameterException;
import org.esk.dablog.exceptions.BusinessException;
import org.esk.dablog.web.util.RequestUtils;
import org.esk.dablog.web.functions.EntryFunctions;
import org.acegisecurity.annotation.Secured;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * This class
 * User: jc
 * Date: 22.11.2006
 * Time: 12:32:34
 * $Id:$
 */
@Secured({ "ROLE_REGISTERED_USER" })
public class EditEntryForm extends SimpleFormController {

    private EntryManager entryManager;

    private boolean safeInput = true;

    public EditEntryForm() {
        setCommandClass(Entry.class);
    }

    public EntryManager getEntryManager() {
        return entryManager;
    }

    public void setEntryManager(EntryManager entryManager) {
        this.entryManager = entryManager;
    }

    /**
     * processes form submission
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param object
     * @param bindException
     * @return
     * @throws Exception
     */
    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        Entry e = (Entry) object;
        if (safeInput) {
            e.setText(EntryFunctions.toBr(e.getText()));
        }
        e.setIp(httpServletRequest.getRemoteAddr());
        processEntry(e);
        entryManager.updateEntry(e);
        return new ModelAndView(new RedirectView(getSuccessView() + "/" + e.getId(), true));
    }

    /**
     * protected method for additional pre-save processing
     * @param e
     */
    protected void processEntry(Entry e) {
    }

    /**
     * binding custom property: handling uploaded files
     *
     * @param req
     * @param object
     * @param bindException
     * @throws Exception
     */
    protected void onBind(HttpServletRequest req, Object object, BindException bindException) throws Exception {
        if (req instanceof AbstractMultipartHttpServletRequest) {
            DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) req;
            Entry e = (Entry) object;
            for (Iterator r = request.getFileNames(); r.hasNext(); ) {
                MultipartFile file = request.getFile((String) r.next());
                if (!file.isEmpty()) {
                    Picture p = new Picture();
                    p.setData(file.getBytes());
                    p.setOriginalFilename(file.getOriginalFilename());
                    e.addPicture(p);
                }
            }
        }
    }

    /**
     * initializes Entry object from request param. redirects to home page in case of errors
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long entryId = RequestUtils.extractLongParameter(request);
        Entry e = entryManager.getEntry(getCommandClass(), entryId);
        if (safeInput) {
            e.setText(EntryFunctions.toCr(e.getText()));
        }
        if (!request.isUserInRole("ROLE_SUPERVISOR") && !EntryFunctions.isAllowedEditing(e, request.getRemoteUser())) {
            throw new BusinessException("User " + request.getRemoteUser() + " cannot edit entry id " + entryId + " belonging to user " + e.getAuthor().getUsername());
        }
        return e;
    }

    public void setSafeInput(boolean safeInput) {
        this.safeInput = safeInput;
    }
}
