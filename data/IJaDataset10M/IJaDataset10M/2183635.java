package com.pp.cameleon.portal.action.document.program;

import com.opensymphony.xwork2.ActionContext;
import com.pp.cameleon.api.document.dto.Practice;
import com.pp.cameleon.api.document.dto.Program;
import com.pp.cameleon.api.document.service.exception.PracticeException;
import com.pp.cameleon.api.document.service.exception.ProgramException;
import com.pp.cameleon.api.security.service.exception.SessionExpiredException;
import com.pp.cameleon.api.security.service.exception.UserException;
import com.pp.cameleon.api.validation.ValidationFailedException;
import com.pp.cameleon.portal.action.common.AbstractListDocumentAction;
import com.pp.cameleon.portal.model.DefaultListBeanPageModel;
import com.pp.cameleon.portal.model.DefaultListPageModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * User: Administrator
 * Date: 4-Mar-2008
 */
public class DisplayAddPracticeProgramAction extends AbstractListDocumentAction<Practice> {

    /**
     * @return The new default model.
     */
    protected DefaultListPageModel<Practice> createModel() {
        DefaultListBeanPageModel<Program, Practice> defaultModel = new DefaultListBeanPageModel<Program, Practice>();
        defaultModel.setBean(new Program());
        return defaultModel;
    }

    /**
     * Lists all documents to be listed.
     *
     * @return The list of documents from the required source.
     * @throws SessionExpiredException   The session is expired.
     * @throws UserException             The loggued user is invalid.
     * @throws ValidationFailedException The listing operation is not valid based on the search bean.
     */
    protected Collection<Practice> listDocuments() throws UserException, ValidationFailedException, SessionExpiredException {
        Collection<Practice> practices = new ArrayList<Practice>();
        String token = getToken();
        Locale locale = ActionContext.getContext().getLocale();
        DefaultListBeanPageModel<Program, Practice> defaultModel = (DefaultListBeanPageModel<Program, Practice>) model;
        try {
            Integer programId = defaultModel.getBean().getId();
            Program program = serviceFacade.getProgram(programId, token, locale);
            defaultModel.setBean(program);
            Practice practice = new Practice(model.getQuery());
            practice.setOwnerId(program.getOwnerId());
            practices = serviceFacade.listPractices(practice, token, locale);
        } catch (PracticeException e) {
            addActionError(e.getMessage());
        } catch (ProgramException e) {
            addActionError(e.getMessage());
        }
        return practices;
    }
}
