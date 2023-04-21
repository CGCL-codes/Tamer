package com.mts.terminals.service.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mts.terminals.domain.dao.GenericDao;
import com.mts.terminals.domain.model.Procesor;
import com.mts.terminals.service.ProcesorManager;

public class ProcesorManagerImpl implements ProcesorManager {

    private final Log logger = LogFactory.getLog(ProcesorManagerImpl.class);

    private GenericDao procesorDao = null;

    @SuppressWarnings("finally")
    public Procesor get(Integer id) {
        return (Procesor) procesorDao.get(id);
    }

    public GenericDao getProcesorDao() {
        return procesorDao;
    }

    public void setProcesorDao(GenericDao procesorDao) {
        this.procesorDao = procesorDao;
    }

    @SuppressWarnings("finally")
    public Procesor save(Procesor procesor) {
        return (Procesor) procesorDao.save(procesor);
    }

    @SuppressWarnings("finally")
    public void update(Procesor procesor) {
        procesorDao.update(procesor);
    }

    public List<Procesor> getAll() {
        return procesorDao.getAll();
    }
}
