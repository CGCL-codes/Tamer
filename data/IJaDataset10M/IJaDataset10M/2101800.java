package com.ecco.persistent;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ObjectsAgentBean implements ObjectsAgentLocal, ObjectsAgentRemote {

    public static final String OB_NAME = "ob_name";

    @PersistenceContext(unitName = "eccodb")
    private EntityManager entityManager;

    public void save(Objects persistentInstance) {
        entityManager.persist(persistentInstance);
    }

    public void delete(Objects persistentInstance) {
        entityManager.remove(persistentInstance);
    }

    public Objects update(Objects detachedInstance) {
        return entityManager.merge(detachedInstance);
    }

    @SuppressWarnings("unchecked")
    public List<Objects> findByObName(String ob_name) {
        String queryString = "SELECT model FROM Objects model WHERE model.ob_name = :obNameValue";
        return entityManager.createQuery(queryString).setParameter("obNameValue", ob_name).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Objects> searchByObName(String ob_name) {
        String queryString = "SELECT model FROM Objects model WHERE model.ob_name LIKE :obNameValue";
        return entityManager.createQuery(queryString).setParameter("obNameValue", ob_name).getResultList();
    }

    public Objects findObjectsById(Integer ob_id) {
        Objects objects = entityManager.find(Objects.class, ob_id);
        objects.getAocRecords().size();
        return objects;
    }

    @SuppressWarnings("unchecked")
    public List<Objects> findAll() {
        String queryString = "SELECT model FROM Objects model";
        return entityManager.createQuery(queryString).getResultList();
    }

    public void addObjects(String ob_name) {
        Objects objects = new Objects();
        objects.setOb_name(ob_name);
        entityManager.persist(objects);
    }

    public void updateObjects(Integer ob_id, String ob_name) {
        Objects objects = entityManager.find(Objects.class, ob_id);
        if (objects != null) {
            objects.setOb_name(ob_name);
            entityManager.merge(objects);
        }
    }

    public void removeObjectsById(Integer ob_id) {
        Objects objects = entityManager.find(Objects.class, ob_id);
        if (objects != null) entityManager.remove(objects);
    }

    public Integer findNumOfAOCRecordsByOb_id(Integer ob_id) {
        Objects objects = entityManager.find(Objects.class, ob_id);
        return objects.getAocRecords().size();
    }

    @SuppressWarnings("unchecked")
    public Boolean checkByObName(String ob_name) {
        String queryString = "SELECT model FROM Objects model WHERE model.ob_name = :obNameValue";
        List checkObjectResult = entityManager.createQuery(queryString).setParameter("obNameValue", ob_name).getResultList();
        if (checkObjectResult.size() == 0) {
            return false;
        }
        return true;
    }
}
