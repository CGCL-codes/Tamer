package ge.telasi.tasks.controller;

import ge.telasi.tasks.model.BinaryData;
import javax.persistence.EntityManager;

/**
 * @author dimitri
 */
public class BinaryDataController extends Controller {

    public long createBinaryData(EntityManager em, byte[] data) {
        BinaryData content = new BinaryData();
        content.setContent(data);
        em.persist(content);
        em.flush();
        return content.getId();
    }

    public void deleteBinaryData(EntityManager em, long id) {
        BinaryData content = em.find(BinaryData.class, id);
        em.remove(content);
    }

    public void updateBinaryData(EntityManager em, long id, byte[] data) {
        BinaryData bin = em.find(BinaryData.class, id);
        bin.setContent(data);
    }

    public byte[] getData(EntityManager em, long id) {
        BinaryData content = em.find(BinaryData.class, id);
        return content.getContent();
    }
}
