package riceSystem.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import riceSystem.dao.DaoTemplateInterface;
import riceSystem.entity.Buyer;
import riceSystem.exception.RunException;
import riceSystem.service.ServiceTemplateInterface;

@Component("buyerService")
public class BuyerServiceImpl implements ServiceTemplateInterface<Buyer> {

    private DaoTemplateInterface<Buyer> buyerDao;

    public DaoTemplateInterface<Buyer> getBuyerDao() {
        return buyerDao;
    }

    @Resource(name = "buyerDao")
    public void setBuyerDao(DaoTemplateInterface<Buyer> buyerDao) {
        this.buyerDao = buyerDao;
    }

    public void add(Buyer t) throws RunException {
        buyerDao.save(t);
    }

    public void removeById(long id) throws RunException {
        buyerDao.deleteById(id);
    }

    public void remove(Buyer t) throws RunException {
        buyerDao.delete(t);
    }

    public long update(Buyer t) throws RunException {
        return buyerDao.update(t);
    }

    public Buyer getById(long id) throws RunException {
        return buyerDao.loadById(id);
    }

    public List<Buyer> getAll() throws RunException {
        return buyerDao.loadAll();
    }
}
