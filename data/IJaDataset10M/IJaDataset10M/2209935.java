package com.divosa.eformulieren.domain.repository.impl;

import java.util.List;
import com.divosa.eformulieren.domain.domeinobject.Answer;
import com.divosa.eformulieren.domain.repository.AnswerDAO;
import com.divosa.security.exception.ObjectNotFoundException;

/**
 * Hibernate implementation of ADAO.
 * 
 * @author Bart Ottenkamp
 */
public class HibernateAnswerDAO extends HibernateBaseDao implements AnswerDAO {

    private static final String FIND_ANSWER_BY_NAME = "Answer.by.name";

    private static final String ATTR_NAME = "name";

    public List<?> loadAll() {
        return loadAll(Answer.class);
    }

    /**
     * {@inheritDoc}
     */
    public Answer getAnswerByName(final String name) throws ObjectNotFoundException {
        List answers = getHibernateTemplate().findByNamedQueryAndNamedParam(FIND_ANSWER_BY_NAME, new String[] { ATTR_NAME }, new String[] { name });
        if (answers.isEmpty()) {
            throw new ObjectNotFoundException("Antwoord '" + name + "' niet gevonden");
        } else {
            return (Answer) answers.get(0);
        }
    }
}
