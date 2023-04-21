package org.identifylife.key.engine.harvest.dao;

import org.identifylife.key.engine.harvest.model.Descriptlet;

/**
 * @author dbarnier
 *
 */
public interface DescriptletDao {

    Long create(Descriptlet descriptlet);

    Long createOrUpdate(Descriptlet descriptlet);

    void deleteAll();
}
