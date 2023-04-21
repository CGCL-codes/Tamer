package riceSystem.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import riceSystem.dao.DaoTemplateInterface;
import riceSystem.entity.AnnounceType;
import riceSystem.exception.RunException;
import riceSystem.service.ServiceTemplateInterface;

@Component("announceTypeService")
public class AnnounceTypeServiceImpl implements ServiceTemplateInterface<AnnounceType> {

    private DaoTemplateInterface<AnnounceType> announceTypeDao;

    public DaoTemplateInterface<AnnounceType> getAnnounceTypeDao() {
        return announceTypeDao;
    }

    @Resource(name = "announceTypeDao")
    public void setAnnounceTypeDao(DaoTemplateInterface<AnnounceType> announceTypeDao) {
        this.announceTypeDao = announceTypeDao;
    }

    public void add(AnnounceType t) throws RunException {
        announceTypeDao.save(t);
    }

    public void removeById(long id) throws RunException {
        announceTypeDao.deleteById(id);
    }

    public void remove(AnnounceType t) throws RunException {
        announceTypeDao.delete(t);
    }

    public long update(AnnounceType t) throws RunException {
        return announceTypeDao.update(t);
    }

    public AnnounceType getById(long id) throws RunException {
        return announceTypeDao.loadById(id);
    }

    public List<AnnounceType> getAll() throws RunException {
        return announceTypeDao.loadAll();
    }
}
