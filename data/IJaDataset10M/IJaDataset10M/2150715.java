package com.creawor.hz_market.resource.groupjieru_resource;

import java.util.Date;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.LockMode;
import com.creawor.imei.base.AbsEditMap;

public class GroupjieruResource_EditMap extends AbsEditMap {

    public void add(GroupjieruResource_Form vo) throws HibernateException {
        GroupjieruResource po = new GroupjieruResource();
        try {
            po.setId(java.lang.Integer.parseInt(vo.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setGroupCode(vo.getGroupCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setGroupName(vo.getGroupName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCompany(vo.getCompany());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setJierufs(vo.getJierufs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setShifouzy(vo.getShifouzy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setGuanglan(vo.getGuanglan());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setDianlan(vo.getDianlan());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setInsertDay(com.creawor.km.util.DateUtil.getDate(vo.getInsertDay(), "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setUpdatedDay(com.creawor.km.util.DateUtil.getDate(vo.getUpdatedDay(), "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Session session = this.beginTransaction();
        try {
            session.save(po);
            this.endTransaction(true);
        } catch (Exception e) {
            this.endTransaction(false);
            e.printStackTrace();
            throw new HibernateException(e);
        }
    }

    public void delete(GroupjieruResource_Form vo) throws HibernateException {
        String id = vo.getId();
        delete(id);
    }

    public void delete(String id) throws HibernateException {
        Session session = this.beginTransaction();
        try {
            GroupjieruResource po = (GroupjieruResource) session.load(GroupjieruResource.class, new Integer(id));
            session.delete(po);
            this.endTransaction(true);
        } catch (HibernateException e) {
            e.printStackTrace();
            this.endTransaction(false);
        }
    }

    public void update(GroupjieruResource_Form vo) throws HibernateException {
        Session session = this.beginTransaction();
        String id = vo.getId();
        GroupjieruResource po = (GroupjieruResource) session.load(GroupjieruResource.class, new Integer(id));
        try {
            po.setId(java.lang.Integer.parseInt(vo.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setGroupCode(vo.getGroupCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setGroupName(vo.getGroupName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCompany(vo.getCompany());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setJierufs(vo.getJierufs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setShifouzy(vo.getShifouzy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setGuanglan(vo.getGuanglan());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setDianlan(vo.getDianlan());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setInsertDay(com.creawor.km.util.DateUtil.getDate(vo.getInsertDay(), "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setUpdatedDay(com.creawor.km.util.DateUtil.getDate(vo.getUpdatedDay(), "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            session.update(po);
            this.endTransaction(true);
        } catch (Exception e) {
            this.endTransaction(false);
            e.printStackTrace();
            throw new HibernateException(e);
        }
    }
}
