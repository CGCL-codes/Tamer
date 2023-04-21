package com.pp.cameleon.portal.action.document.exercice;

import com.opensymphony.xwork2.ActionContext;
import com.pp.cameleon.api.document.dto.Exercice;
import com.pp.cameleon.api.document.service.exception.ExerciceException;
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
public class ListExercicesAction extends AbstractListDocumentAction<Exercice> {

    /**
     * Lists all documents to be listed.
     *
     * @return The list of documents from the required source.
     * @throws SessionExpiredException   The session is expired.
     * @throws UserException             The loggued user is invalid.
     * @throws ValidationFailedException The listing operation is not valid based on the search bean.
     */
    protected Collection<Exercice> listDocuments() throws UserException, ValidationFailedException, SessionExpiredException {
        Collection<Exercice> exercices = new ArrayList<Exercice>();
        Exercice exercice = new Exercice(model.getQuery());
        try {
            exercices = serviceFacade.listExercices(exercice, getToken(), ActionContext.getContext().getLocale());
        } catch (ExerciceException e) {
            addActionError(e.getMessage());
        }
        return exercices;
    }
}
