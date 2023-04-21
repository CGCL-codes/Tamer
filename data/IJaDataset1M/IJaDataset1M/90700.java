package net.sourceforge.pmakbsc.dao;

import net.sourceforge.pmakbsc.entity.Meta;

/**
 *
 */
public class MetaJpaDAO extends GenericJpaDAO<Meta> implements MetaDAO {

    public MetaJpaDAO() {
        super(Meta.class);
    }
}
