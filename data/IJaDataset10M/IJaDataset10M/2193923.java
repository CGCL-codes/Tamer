package at.redcross.tacos.dbal.helper;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import at.redcross.tacos.dbal.entity.Equipment;
import at.redcross.tacos.dbal.query.EquipmentQueryParam;
import at.redcross.tacos.dbal.utils.StringHelper;

public class EquipmentHelper {

    public static List<Equipment> list(EntityManager manager, EquipmentQueryParam param) {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT entry FROM Equipment entry");
        builder.append(" WHERE ");
        builder.append(" 1=1 ");
        if (param.inventoryStart != null) {
            builder.append(" AND ");
            builder.append(" entry.lastInventoryDate >= :inventoryStart ");
        }
        if (param.inventoryEnd != null) {
            builder.append(" AND ");
            builder.append(" entry.lastInventoryDate <= :inventoryEnd ");
        }
        if (param.expirationStart != null) {
            builder.append(" AND ");
            builder.append(" entry.expirationDate >= :expirationStart ");
        }
        if (param.expirationEnd != null) {
            builder.append(" AND ");
            builder.append(" entry.expirationDate <= :expirationEnd ");
        }
        if (!StringHelper.isNullOrEmpty(param.name)) {
            builder.append(" AND ");
            builder.append(" entry.name LIKE :name ");
        }
        if (!StringHelper.isNullOrEmpty(param.notes)) {
            builder.append(" AND ");
            builder.append(" entry.notes LIKE :notes ");
        }
        if (param.inventoryNumber > 0) {
            builder.append(" AND ");
            builder.append(" entry.inventoryNumber = :inventoryNumber ");
        }
        if (param.actualGreaterTarget) {
            builder.append(" AND ");
            builder.append(" entry.actualNumber >= entry.theoreticalNumber ");
        }
        if (param.targetGreaterActual) {
            builder.append(" AND ");
            builder.append(" entry.theoreticalNumber >= entry.actualNumber ");
        }
        builder.append(" order by entry.name asc ");
        TypedQuery<Equipment> query = manager.createQuery(builder.toString(), Equipment.class);
        if (param.inventoryStart != null) {
            query.setParameter("inventoryStart", param.inventoryStart, TemporalType.DATE);
        }
        if (param.inventoryEnd != null) {
            query.setParameter("inventoryEnd", param.inventoryEnd, TemporalType.DATE);
        }
        if (param.expirationStart != null) {
            query.setParameter("expirationStart", param.expirationStart, TemporalType.DATE);
        }
        if (param.expirationEnd != null) {
            query.setParameter("expirationEnd", param.expirationEnd, TemporalType.DATE);
        }
        if (!StringHelper.isNullOrEmpty(param.name)) {
            String queryParam = StringHelper.toHqlWildcard(param.name);
            query.setParameter("name", queryParam);
        }
        if (!StringHelper.isNullOrEmpty(param.notes)) {
            String queryParam = StringHelper.toHqlWildcard(param.notes);
            query.setParameter("notes", queryParam);
        }
        if (param.inventoryNumber > 0) {
            query.setParameter("inventoryNumber", param.inventoryNumber);
        }
        return query.getResultList();
    }
}
