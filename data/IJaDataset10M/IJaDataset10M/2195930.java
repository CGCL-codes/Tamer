package info.gdeDengi.common;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ExhrateService extends CommonService {

    protected BasicDao basicDao;

    public BasicDao getBasicDao() {
        return basicDao;
    }

    public void setBasicDao(BasicDao basicDao) {
        this.basicDao = basicDao;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Exchrate> findAllForCurrency(Integer _curId) {
        String q = "select er from Exchrate er where er.currency.currencyid = " + String.valueOf(_curId);
        List<?> categories = this.basicDao.find(q, null);
        return (List<Exchrate>) categories;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void persistEvent(Exchrate er) {
        this.basicDao.persist(er);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteEvent(Exchrate er) throws EntityNotFoundException {
        this.basicDao.remove(Exchrate.class, er.getExchrateid());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void mergeEvent(Exchrate er) throws EntityNotFoundException {
        this.basicDao.merge(er);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Exchrate findForCurrencyAndDate(Integer _curId, Date _date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String q = "select er from Exchrate er where er.currency.currencyid = " + String.valueOf(_curId) + "and er.rateDate = " + sdf.format(_date);
        return (Exchrate) this.basicDao.findSingle(q, null);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Exchrate findCurrentRate(Integer _curId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String q = "select er from Exchrate er where er.currency.currencyid = " + String.valueOf(_curId) + "order by er.rateDate DESC";
        return (Exchrate) this.basicDao.findTop(q, 1, null).get(0);
    }
}
