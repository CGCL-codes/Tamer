package com.firescrum.retrospective.dao;

import java.util.List;
import com.firescrum.core.model.Sprint;
import com.firescrum.infrastructure.dao.IBaseDao;
import com.firescrum.retrospective.model.Retrospective;

/**
 * IDaoRetrospective is the interface definition of the data access layer of the entity {@link Retrospective}.
 * 
 * @author Willame e Ademir
 */
public interface IDaoRetrospective extends IBaseDao<Retrospective> {

    /**
     * Find all retrospectives of the sprint
     * @param sprint
     * @return
     */
    List<Retrospective> findAll(Sprint sprint);

    /**
     * Find the retrospective of the sprint 
     * @param sprint
     * @return
     */
    Retrospective find(Sprint sprint);

    /**
     * 
     * @param retrospectiveId
     * @param status
     * @return
     */
    Retrospective updateRetrospectiveStatus(long retrospectiveId, int status);
}
