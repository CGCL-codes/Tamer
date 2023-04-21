package com.hx.persistence.services.b;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import com.hx.persistence.dao.b.BDao;
import com.hx.persistence.model.b.B;

public class BServiceImpl implements BService {

    private BDao bDao;

    @Override
    public void save(B b) {
        this.bDao.save(b);
    }

    @Override
    public List<B> find() {
        List<B> all = this.bDao.find();
        return all;
    }

    @Override
    public B getById(long id) {
        return this.bDao.getById(id);
    }

    @Override
    public List<B> findByNameLessThan(String name) {
        return this.bDao.findByNameLessThan(name);
    }

    @Override
    public List<B> scroll() {
        LinkedList<B> list = new LinkedList<B>();
        Enumeration<B> scroll = this.bDao.scroll();
        while (scroll.hasMoreElements()) {
            list.add(scroll.nextElement());
        }
        return list;
    }

    public BDao getBDao() {
        return bDao;
    }

    public void setBDao(BDao dao) {
        bDao = dao;
    }
}
