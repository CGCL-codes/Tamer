package com.zhongkai.service.declare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.zhongkai.dao.book.TDjCcdjxxDAO;
import com.zhongkai.dao.book.TDjCcdjxxLsDAO;
import com.zhongkai.dao.book.TDjCcywclDAO;
import com.zhongkai.dao.declare.TSbCcssbxxDao;
import com.zhongkai.dao.declare.TSbCcsspxxDao;
import com.zhongkai.dao.ticket.TPzYwlsDao;
import com.zhongkai.model.book.TDjCcdjxx;
import com.zhongkai.model.book.TDjCcdjxxLs;
import com.zhongkai.model.book.TDjCcywcl;
import com.zhongkai.model.config.User;
import com.zhongkai.model.declare.TSbCcssbxx;
import com.zhongkai.model.declare.TSbCcsspxx;
import com.zhongkai.model.ticket.TPzYwls;
import com.zhongkai.service.BaseService;
import com.zhongkai.tools.DateTool;

@Component
@Transactional
public class SpCancelService extends BaseService {

    private TDjCcdjxxDAO tDjCcdjxxDAO;

    private TDjCcywclDAO tDjCcywclDAO;

    private TDjCcdjxxLsDAO tDjCcdjxxLsDAO;

    private TSbCcssbxxDao tSbCcssbxxDao;

    private TSbCcsspxxDao tSbCcsspxxDao;

    private TPzYwlsDao tPzYwlsDao;

    @Resource
    public void settDjCcdjxxDAO(TDjCcdjxxDAO tDjCcdjxxDAO) {
        this.tDjCcdjxxDAO = tDjCcdjxxDAO;
    }

    @Resource
    public void settDjCcdjxxLsDAO(TDjCcdjxxLsDAO tDjCcdjxxLsDAO) {
        this.tDjCcdjxxLsDAO = tDjCcdjxxLsDAO;
    }

    @Resource
    public void settDjCcywclDAO(TDjCcywclDAO tDjCcywclDAO) {
        this.tDjCcywclDAO = tDjCcywclDAO;
    }

    @Resource
    public void settSbCcssbxxDao(TSbCcssbxxDao tSbCcssbxxDao) {
        this.tSbCcssbxxDao = tSbCcssbxxDao;
    }

    @Resource
    public void settSbCcsspxxDao(TSbCcsspxxDao tSbCcsspxxDao) {
        this.tSbCcsspxxDao = tSbCcsspxxDao;
    }

    @Resource
    public void settPzYwlsDao(TPzYwlsDao tPzYwlsDao) {
        this.tPzYwlsDao = tPzYwlsDao;
    }

    public void save(TDjCcdjxx djCcdjxx) throws Exception {
        tDjCcdjxxDAO.insert(djCcdjxx);
    }

    public List<TDjCcdjxx> findByHql(String hql, Object[] values) {
        return tDjCcdjxxDAO.findByHql(hql, values);
    }

    public Object selectById(Class entityClass, Integer id) throws Exception {
        return tDjCcdjxxLsDAO.selectById(entityClass, id);
    }

    public void spCancel(TSbCcsspxx tSbCcsspxx, String identifier) throws Exception {
        Date date = new Date();
        tSbCcsspxx.setPzztDm("09");
        tSbCcsspxx.setZfkpRq(date);
        tSbCcsspxxDao.update(tSbCcsspxx);
        TPzYwls tPzYwls = new TPzYwls();
        tPzYwls.setYwlxDm("03");
        tPzYwls.setYprDm(identifier);
        tPzYwls.setPzzlDm(tSbCcsspxx.getPzzlDm());
        tPzYwls.setZb(tSbCcsspxx.getZb());
        tPzYwls.setPzztDm(tSbCcsspxx.getPzztDm());
        tPzYwls.setSphmQ(tSbCcsspxx.getSphm());
        tPzYwls.setSphmZ(tSbCcsspxx.getSphm());
        tPzYwls.setCzrDm(identifier);
        tPzYwls.setSl(1);
        tPzYwls.setCzRq(date);
        tPzYwlsDao.insert(tPzYwls);
        TSbCcssbxx tSbCcssbxx = (TSbCcssbxx) super.getSingle(TSbCcssbxx.class, "spXh", tSbCcsspxx.getSpXh());
        TDjCcywcl tDjCcywcl = new TDjCcywcl();
        tDjCcywcl.setYwlxDm("07");
        Integer ccdjh = tSbCcssbxx.getCcdjh();
        tDjCcywcl.setCcdjh(ccdjh);
        tDjCcywcl.setCzrDm(identifier);
        tDjCcywcl.setCzRq(date);
        tDjCcywclDAO.insert(tDjCcywcl);
        List<TDjCcdjxx> tDjCcdjxxList = tDjCcdjxxDAO.select("from TDjCcdjxx where ccdjh=? and djztDm='01'", new Object[] { ccdjh });
        Date dateMinCurr = (Date) tSbCcssbxxDao.findBySql("select min(ssqq_rq) from t_sb_ccssbxx where sp_xh=" + tSbCcsspxx.getSpXh());
        String ssqqStr = DateTool.formatDate(dateMinCurr, "yyyyMM");
        TDjCcdjxx tDjCcdjxx = tDjCcdjxxList.get(0);
        TDjCcdjxxLs tDjCcdjxxLs = new TDjCcdjxxLs();
        BeanUtils.copyProperties(tDjCcdjxx, tDjCcdjxxLs);
        tDjCcdjxx.setSbqsny(ssqqStr);
        tDjCcdjxx.setXgrDm(identifier);
        tDjCcdjxx.setXgRq(date);
        tDjCcdjxxDAO.update(tDjCcdjxx);
        tDjCcdjxxLs.setYwclXh(tDjCcywcl.getYwclXh());
        tDjCcdjxxLs.setXgrDm(identifier);
        tDjCcdjxxLs.setXgRq(date);
        tDjCcdjxxLsDAO.insert(tDjCcdjxxLs);
    }
}
