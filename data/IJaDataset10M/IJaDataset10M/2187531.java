package com.f2ms.dao.shipmentitem;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import com.f2ms.dao.AbstractDAOHib;
import com.f2ms.exception.DAOException;
import com.f2ms.model.Shipment;
import com.f2ms.model.ShipmentItem;

public class ShipmentItemDAOHibImpl extends AbstractDAOHib<ShipmentItem> implements IShipmentItemDAO {

    @Override
    public List<ShipmentItem> findAllPackagingItems() throws DAOException {
        return super.findAll(ShipmentItem.class);
    }

    @Override
    public ShipmentItem create(ShipmentItem shipmentItem) throws DAOException {
        shipmentItem.setCreatedOn(new Date());
        shipmentItem.setChangedOn(new Date());
        Long id = (Long) super.create(shipmentItem);
        shipmentItem.setId(id);
        return shipmentItem;
    }

    @Override
    public ShipmentItem edit(ShipmentItem shipmentItem) throws DAOException {
        shipmentItem.setChangedOn(new Date());
        super.edit(shipmentItem);
        return shipmentItem;
    }

    @Override
    public ShipmentItem findPackagingById(Long rowId) throws DAOException {
        return (ShipmentItem) super.find(ShipmentItem.class, rowId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ShipmentItem> findPackagingByCriteria(ShipmentItem shipmentItem) throws DAOException {
        DetachedCriteria criteria = DetachedCriteria.forClass(Shipment.class);
        if (StringUtils.isNotBlank(shipmentItem.getShipment().getPackagingNo())) {
            criteria.add(Restrictions.eq("packagingNo", shipmentItem.getShipment().getPackagingNo() + "%"));
        }
        if (StringUtils.isNotBlank(shipmentItem.getBooking().getDocNo())) {
            criteria.add(Restrictions.eq("bookingNo", shipmentItem.getBooking().getDocNo() + "%"));
        }
        criteria.addOrder(Property.forName("id").asc());
        List<ShipmentItem> listResult = getHibernateTemplate().findByCriteria(criteria);
        return listResult;
    }
}
