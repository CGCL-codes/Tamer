package com.nodeshop.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.nodeshop.dao.ReceiverDao;
import com.nodeshop.entity.Receiver;

/**
 * Dao实现类 - 收货地址
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshopFDB0C29D95AE89A58BB8DA83E757D119
 
 */
@Repository
public class ReceiverDaoImpl extends BaseDaoImpl<Receiver, String> implements ReceiverDao {

    @Override
    @SuppressWarnings("unchecked")
    public String save(Receiver receiver) {
        if (receiver.getIsDefault()) {
            String hql = "from Receiver receiver where receiver.member = ? and receiver.isDefault = ?";
            List<Receiver> receiverList = getSession().createQuery(hql).setParameter(0, receiver.getMember()).setParameter(1, true).list();
            if (receiverList != null) {
                for (Receiver r : receiverList) {
                    r.setIsDefault(false);
                }
            }
        }
        return super.save(receiver);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Receiver receiver) {
        if (receiver.getIsDefault()) {
            String hql = "from Receiver receiver where receiver.member = ? and receiver.isDefault = ? and receiver != ?";
            List<Receiver> receiverList = getSession().createQuery(hql).setParameter(0, receiver.getMember()).setParameter(1, true).setParameter(2, receiver).list();
            if (receiverList != null) {
                for (Receiver r : receiverList) {
                    r.setIsDefault(false);
                }
            }
        }
        super.update(receiver);
    }
}
