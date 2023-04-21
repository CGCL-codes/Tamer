package uk.ac.ebi.intact.core.persistence.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.core.context.IntactSession;
import uk.ac.ebi.intact.model.Annotation;
import uk.ac.ebi.intact.core.persistence.dao.AnnotationDao;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * DAO for annotations
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: AnnotationDaoImpl.java 13109 2009-05-11 10:30:49Z baranda $
 * @since <pre>07-jul-2006</pre>
 */
@Repository
@Transactional(readOnly = true)
@SuppressWarnings({ "unchecked" })
public class AnnotationDaoImpl extends IntactObjectDaoImpl<Annotation> implements AnnotationDao {

    public AnnotationDaoImpl() {
        super(Annotation.class);
    }

    public AnnotationDaoImpl(EntityManager entityManager, IntactSession intactSession) {
        super(Annotation.class, entityManager, intactSession);
    }

    public List<Annotation> getByTextLike(String text) {
        return getSession().createCriteria(getEntityClass()).add(Restrictions.like("annotationText", text)).list();
    }
}
