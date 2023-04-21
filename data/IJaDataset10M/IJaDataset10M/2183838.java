package com.daveoxley.cnery.dao;

import com.daveoxley.cnery.entities.Scene;
import com.daveoxley.cnery.entities.SceneAction;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

/**
 *
 * @author dave
 */
@Name("sceneActionDAO")
@AutoCreate
public class SceneActionDAO implements Serializable {

    @In
    protected EntityManager entityManager;

    public SceneAction findSceneAction(String sceneActionId) {
        try {
            StringBuilder query = new StringBuilder("select sa from SceneAction sa where sa.id = :sceneActionId");
            return (SceneAction) entityManager.createQuery(query.toString()).setParameter("sceneActionId", sceneActionId).getSingleResult();
        } catch (EntityNotFoundException ex) {
        } catch (NoResultException ex) {
        }
        return null;
    }

    public List<SceneAction> findSceneActions() {
        try {
            return entityManager.createQuery("select sa from SceneAction sa").getResultList();
        } catch (EntityNotFoundException ex) {
        } catch (NoResultException ex) {
        }
        return null;
    }

    public List<SceneAction> findSceneActionsByScene(Scene scene) {
        try {
            return entityManager.createQuery("select sa from SceneAction sa where sa.scene = :scene").setParameter("scene", scene).getResultList();
        } catch (EntityNotFoundException ex) {
        } catch (NoResultException ex) {
        }
        return null;
    }
}
