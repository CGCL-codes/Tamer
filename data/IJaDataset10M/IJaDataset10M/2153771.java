package net.sourceforge.websnaptool.b2c.system.service.impl;

import net.sourceforge.websnaptool.b2c.service.Service;
import net.sourceforge.websnaptool.b2c.system.dao.SeqDao;
import net.sourceforge.websnaptool.b2c.system.service.SeqService;

/**
 * SeqServiceImpl:
 *  
 * User: Muwang Zheng
 * Date: 2010-12-23
 * Time: 下午01:40:45
 * 
 */
public class SeqServiceImpl extends Service implements SeqService {

    /**
	 * SeqDao
	 */
    private SeqDao seqDao;

    /**
	 * @return the seqDao
	 */
    public SeqDao getSeqDao() {
        return seqDao;
    }

    /**
	 * @param seqDao the seqDao to set
	 */
    public void setSeqDao(SeqDao seqDao) {
        this.seqDao = seqDao;
    }

    @Override
    public Long buildNextSeq(String id) {
        this.getSeqDao().incSeq(id);
        return this.getSeqDao().getSeq(id);
    }
}
