package com.ncs.mail.service.impl;

import com.ncs.common.service.impl.BaseServiceImpl;
import com.ncs.mail.IConstant;
import com.ncs.mail.dao.CustomerDao;
import com.ncs.mail.dao.SourceDao;
import com.ncs.mail.service.SourceService;
import com.ncs.mail.to.SourceTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.management.Query;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: hjl
 * Date: 6/20/11
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("mailSourceService")
public class SourceServiceImpl extends BaseServiceImpl<SourceTo, String> implements SourceService {

    @Resource
    public void setBaseDao(SourceDao sourceDao) {
        super.setBaseDao(sourceDao);
    }

    @Resource
    private SourceDao sourceDao;

    @Resource
    private CustomerDao customerDao;

    public List<SourceTo> getAllSource() {
        return sourceDao.getAllSource();
    }

    public List<SourceTo> getValidSource() {
        return sourceDao.getValidSource();
    }

    @Transactional
    public String save2DeleteSource(SourceTo sourceTo) {
        Set set = customerDao.getList("source", sourceTo);
        if (set.size() == 0) {
            sourceDao.delete(sourceTo.getId());
            return IConstant.RESULT_OK;
        } else {
            return IConstant.RESULT_FAILED;
        }
    }

    @Transactional
    public void updateSource(SourceTo sourceTo) {
        SourceTo to = sourceDao.get(sourceTo.getId());
        to.setSourceName(sourceTo.getSourceName());
        to.setSeq(sourceTo.getSeq());
        sourceDao.update(to);
    }
}
