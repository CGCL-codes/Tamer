package org.magicbox.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javolution.util.FastMap;
import org.magicbox.controller.util.WebUtils;
import org.magicbox.service.BindDataService;
import org.magicbox.util.Constant;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Upload Controller
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class UploadController extends SimpleFormController {

    public UploadController() {
        setCommandClass(org.magicbox.dto.FileUpload.class);
        setFormView("import/upload");
        setSuccessView("import/conferma");
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    public ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        MultipartFile file = (MultipartFile) multipartRequest.getFile(Constant.FILE);
        String ext = recuperaEstensioneFile(file.getOriginalFilename());
        return isValidExtension(ext) ? valorizzaModelAndView(req, ext, file.getBytes()) : invalidType(req.getLocale());
    }

    private boolean isValidExtension(String ext) {
        return ext.equals(Constant.CSV_EXT) || ext.equals(Constant.XLS_EXT);
    }

    private ModelAndView invalidType(Locale locale) {
        return new ModelAndView("import/upload", Constant.MSG, getApplicationContext().getMessage(Constant.I18N_UPLOAD_WRONG_TYPE, null, locale));
    }

    private ModelAndView unSuccessBind(HttpServletRequest req) {
        return new ModelAndView("import/upload", Constant.MSG, getApplicationContext().getMessage(Constant.I18N_UPLOAD_WRONG_DATA, null, req.getLocale()));
    }

    private ModelAndView uploadAndBindSuccess(int size) {
        ModelAndView mav = new ModelAndView("import/conferma");
        mav.addObject(Constant.NUM_USERS, size);
        return mav;
    }

    @SuppressWarnings("unchecked")
    private ModelAndView valorizzaModelAndView(HttpServletRequest req, String ext, byte[] bytes) throws Exception, IOException {
        Map params = new FastMap();
        params.put(Constant.ID_CENTRO, WebUtils.getIdCentro(req));
        params.put(Constant.TEMP, WebUtils.getTempDir(req));
        params.put(Constant.EXT, ext);
        params.put(Constant.ID, req.getSession().getId());
        List utentiFile = bindService.getUtenti(params, bytes);
        int numeroUtenti = utentiFile.size();
        utentiFile = null;
        return utentiFile.size() > 0 ? uploadAndBindSuccess(numeroUtenti) : unSuccessBind(req);
    }

    private String recuperaEstensioneFile(String fullName) {
        int indicePunto = fullName.lastIndexOf(".");
        String estensione = "";
        if (indicePunto != 0) {
            estensione = fullName.substring(indicePunto);
        }
        return estensione.toLowerCase();
    }

    private BindDataService bindService;

    public void setBindService(BindDataService bindService) {
        this.bindService = bindService;
    }
}
