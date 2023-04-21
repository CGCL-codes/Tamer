package hr.chus.cchat.db.service.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

/**
 * JPA/Hibernate DAO implementation of picture services.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class PictureServiceImpl implements PictureService {

    private EntityManager entityManager;

    @Override
    public void addPicture(Picture picture) {
        entityManager.persist(picture);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Picture> getAllPictures() {
        return entityManager.createNamedQuery("Picture.getAll").getResultList();
    }

    @Override
    public Picture getPictureById(Integer id) {
        return entityManager.find(Picture.class, id);
    }

    @Override
    public void removePicture(Picture picture) {
        picture = entityManager.getReference(Picture.class, picture.getId());
        entityManager.remove(picture);
    }

    @Override
    public Picture updatePicture(Picture picture) {
        return entityManager.merge(picture);
    }

    @Override
    public boolean checkIfPictureNameExists(String pictureName) {
        try {
            entityManager.createNamedQuery("Picture.getByName").setParameter("name", pictureName).getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Picture> getPicturesByNick(Nick nick) {
        return entityManager.createNamedQuery("Picture.getByNick").setParameter("nick", nick).getResultList();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
