package net.sf.imca.model.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import net.sf.imca.model.entities.EquipmentSupplierEntity;

public class EquipmentSupplierDAO {

    private EquipmentSupplierEntity equipmentSupplier;

    @SuppressWarnings("unchecked")
    public EquipmentSupplierEntity findEquipmentSupplier(EntityManager em, String name) {
        EquipmentSupplierEntity equipSup = null;
        Query query = em.createNamedQuery("findEquipmentSupplierByName");
        query.setParameter("name", name);
        List<EquipmentSupplierEntity> list = query.getResultList();
        if (list.size() != 0) {
            equipSup = list.get(0);
        }
        return equipSup;
    }

    public EquipmentSupplierEntity getEquipmentSupplier() {
        return equipmentSupplier;
    }

    public void setEquipmentSupplier(EquipmentSupplierEntity equipmentSupplier) {
        this.equipmentSupplier = equipmentSupplier;
    }
}
