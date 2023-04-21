package com.genia.toolbox.web.gwt.basics.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.genia.toolbox.basics.bean.impl.AutoCleanFileDataContainer;
import com.genia.toolbox.basics.bean.impl.FileDataContainer;
import com.genia.toolbox.basics.exception.technical.TechnicalException;
import com.genia.toolbox.basics.manager.ExceptionManager;
import com.genia.toolbox.basics.manager.FileManager;
import com.genia.toolbox.web.gwt.basics.bean.FileInformation;
import com.genia.toolbox.web.gwt.basics.form.GwtFileUploadForm;
import com.genia.toolbox.web.gwt.basics.manager.GwtFileUploadManager;

/**
 * {@link org.springframework.web.servlet.mvc.Controller} that allows to upload
 * a file.
 */
public abstract class AbstractGwtFileUploadController extends SimpleFormController {

    /**
   * the {@link ExceptionManager} to use.
   */
    private ExceptionManager exceptionManager;

    /**
   * the {@link GwtFileUploadManager} to use.
   */
    private GwtFileUploadManager gwtFileUploadManager;

    /**
   * the {@link FileManager} to use.
   */
    private FileManager fileManager;

    /**
   * do store the uploaded file.
   * 
   * @param request
   *          the request
   * @param response
   *          the response
   * @param form
   *          the form begin submited
   * @param bindException
   *          bind errors
   * @return the {@link ModelAndView} to forward to
   * @throws TechnicalException
   *           when an error occured
   * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.lang.Object,
   *      org.springframework.validation.BindException)
   */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object form, BindException bindException) throws TechnicalException {
        try {
            GwtFileUploadForm fileUploadForm = (GwtFileUploadForm) form;
            MultipartFile multipartFile = fileUploadForm.getFile();
            if (multipartFile != null) {
                File uploadedFile = getFileManager().createAutoDeletableTempFile("uploadedFile", ".bin");
                multipartFile.transferTo(uploadedFile);
                FileDataContainer dc = new AutoCleanFileDataContainer();
                dc.setContentType(multipartFile.getContentType());
                dc.setName(multipartFile.getOriginalFilename());
                dc.setFile(uploadedFile);
                FileInformation fileInformation = createFileInformation();
                fileInformation.setDataContainer(dc);
                fileInformation.getGwtFileInformation().setSessionIdentifier(fileUploadForm.getIdentifier());
                getGwtFileUploadManager().registerFileInformation(fileInformation);
                response.setStatus(200);
                response.setContentType("text/plain");
                response.getWriter().print("200");
            }
            return null;
        } catch (IOException e) {
            throw getExceptionManager().convertException(e);
        }
    }

    /**
   * factory method for a {@link FileInformation}.
   * 
   * @return a new {@link FileInformation}
   */
    public abstract FileInformation createFileInformation();

    /**
   * getter for the exceptionManager property.
   * 
   * @return the exceptionManager
   */
    public ExceptionManager getExceptionManager() {
        return exceptionManager;
    }

    /**
   * setter for the exceptionManager property.
   * 
   * @param exceptionManager
   *          the exceptionManager to set
   */
    public void setExceptionManager(ExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }

    /**
   * getter for the gwtFileUploadManager property.
   * 
   * @return the gwtFileUploadManager
   */
    public GwtFileUploadManager getGwtFileUploadManager() {
        return gwtFileUploadManager;
    }

    /**
   * setter for the gwtFileUploadManager property.
   * 
   * @param gwtFileUploadManager
   *          the gwtFileUploadManager to set
   */
    public void setGwtFileUploadManager(GwtFileUploadManager gwtFileUploadManager) {
        this.gwtFileUploadManager = gwtFileUploadManager;
    }

    /**
   * getter for the fileManager property.
   * 
   * @return the fileManager
   */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
   * setter for the fileManager property.
   * 
   * @param fileManager
   *          the fileManager to set
   */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
