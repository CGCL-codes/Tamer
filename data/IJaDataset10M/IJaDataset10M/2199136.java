package com.inet.qlcbcc.repository.support;

import org.springframework.stereotype.Repository;
import org.webos.repository.hibernate.AbstractHibernateReadableRepository;
import com.inet.qlcbcc.domain.FamilyRelationship;
import com.inet.qlcbcc.repository.FamilyRelationshipReadableRepository;

/**
 * FamilyRelationshipReadableRepositorySupport.
 *
 * @author Thoang Tran
 * @version $Id: FamilyRelationshipReadableRepositorySupport.java Nov 24, 2011 10:18:42 PM thoangtd $
 *
 * @since 1.0
 */
@Repository(value = "familyRelationshipReadableRepository")
public class FamilyRelationshipReadableRepositorySupport extends AbstractHibernateReadableRepository<FamilyRelationship, String> implements FamilyRelationshipReadableRepository {

    /**
   * @param clazz
   */
    protected FamilyRelationshipReadableRepositorySupport() {
        super(FamilyRelationship.class);
    }
}
