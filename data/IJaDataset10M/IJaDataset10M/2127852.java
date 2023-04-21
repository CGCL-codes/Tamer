package com.uusee.crawler.dbwriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.ObjectRetrievalFailureException;
import com.uusee.crawler.framework.Processor;
import com.uusee.crawler.model.CrawlURI;
import com.uusee.framework.bo.UniversalBo;
import com.uusee.shipshape.bk.model.BaikeDataSource;
import com.uusee.shipshape.bk.model.BaikeDataSourcePK;

public class BaikeDataSourceDbWriter extends Processor {

    private static Log log = LogFactory.getLog(BaikeDataSourceDbWriter.class);

    private UniversalBo universalBo;

    protected void innerProcess(CrawlURI crawlURI) {
        BaikeDataSource baike = (BaikeDataSource) crawlURI.getModel();
        BaikeDataSourcePK pk = new BaikeDataSourcePK(baike.getSourceSite(), baike.getOriId());
        try {
            BaikeDataSource oldBaike = null;
            try {
                oldBaike = universalBo.getById(BaikeDataSource.class, pk);
            } catch (ObjectRetrievalFailureException e) {
                oldBaike = null;
            }
            if (oldBaike != null) {
                BeanUtils.copyProperties(baike, oldBaike);
                universalBo.doUpdate(oldBaike);
                log.info(pk.getSourceSite() + "-" + pk.getOriId() + "-更新数据。");
            } else {
                universalBo.doSave(baike);
                log.info(pk.getSourceSite() + "-" + pk.getOriId() + "-增加数据。");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(pk.getSourceSite() + "-" + pk.getOriId() + "-DB时发生错误。", e);
        }
    }

    public void setUniversalBo(UniversalBo universalBo) {
        this.universalBo = universalBo;
    }
}
