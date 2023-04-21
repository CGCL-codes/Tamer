package com.adpython.dao.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.springframework.orm.jdo.support.JdoDaoSupport;
import com.adpython.dao.ChannelDao;
import com.adpython.dao.PMF;
import com.adpython.domain.Channel;

public class ChannelDaoImpl extends JdoDaoSupport implements ChannelDao {

    public Channel get(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Extent r = pm.getExtent(Channel.class, false);
            String filter = "id==" + id;
            Query query = pm.newQuery(r, filter);
            List<Channel> list = (List<Channel>) query.execute();
            if (!list.isEmpty()) return list.get(0); else return null;
        } finally {
            pm.close();
        }
    }

    public void remove(Channel channel) {
        if (channel != null) this.removeById(channel.getId());
    }

    public void removeById(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Extent r = pm.getExtent(Channel.class, false);
            String filter = "id==" + id;
            Query query = pm.newQuery(r, filter);
            List<Channel> list = (List<Channel>) query.execute();
            if (!list.isEmpty()) {
                Channel delChannel = list.get(0);
                pm.deletePersistent(delChannel);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            pm.close();
        }
    }

    public void save(Channel channel) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistent(channel);
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            pm.close();
        }
    }

    public void update(Channel channel) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Channel old = pm.getObjectById(Channel.class, channel.getId());
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            old.setName(channel.getName());
            old.setRank(channel.getRank());
            old.setTitle(channel.getTitle());
            old.setUpdateTime(new Date());
            old.setUpdateUserId(1);
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            pm.close();
        }
    }

    public List<Channel> queryAll() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Channel.class);
            query.setOrdering("rank asc");
            return (List<Channel>) pm.detachCopyAll((Collection<Channel>) query.execute());
        } finally {
            pm.close();
        }
    }

    public Channel queryByName(String name) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Channel.class);
            query.setFilter("name=='" + name + "'");
            List<Channel> list = (List<Channel>) pm.detachCopyAll((Collection<Channel>) query.execute());
            if (list.size() > 0) return list.get(0); else return null;
        } finally {
            pm.close();
        }
    }
}
