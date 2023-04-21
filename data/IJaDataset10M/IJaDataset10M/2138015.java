package com.pp.cameleon.portal.action.document.program;

import com.opensymphony.xwork2.ActionContext;
import com.pp.cameleon.api.document.dto.Program;
import com.pp.cameleon.api.document.service.exception.ProgramException;
import com.pp.cameleon.api.security.service.exception.SessionExpiredException;
import com.pp.cameleon.api.security.service.exception.UserException;
import com.pp.cameleon.api.validation.ValidationFailedException;
import com.pp.cameleon.portal.action.common.AbstractListDocumentAction;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Administrator
 * Date: 8-Feb-2008
 */
public class ListProgramsAction extends AbstractListDocumentAction<Program> {

    /**
     * Lists all documents to be listed.
     *
     * @return The list of documents from the required source.
     * @throws SessionExpiredException   The session is expired.
     * @throws UserException             The loggued user is invalid.
     * @throws ValidationFailedException The listing operation is not valid based on the search bean.
     */
    protected Collection<Program> listDocuments() throws UserException, ValidationFailedException, SessionExpiredException {
        Collection<Program> programs = new ArrayList<Program>();
        Program program = new Program(model.getQuery());
        try {
            programs = serviceFacade.listPrograms(program, getToken(), ActionContext.getContext().getLocale());
        } catch (ProgramException e) {
            addActionError(e.getMessage());
        }
        return programs;
    }
}
