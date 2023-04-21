package com.jeecms.article.action.front;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.jeecms.article.lucene.LuceneArticleSvc;
import com.jeecms.cms.CmsIndeAction;
import com.jeecms.cms.Constants;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.RequestUtils;

/**
 * ���¶���ģ��
 * 
 * @author liufang
 * 
 */
@Scope("prototype")
@Controller("article.artiIndeAct")
public class ArtiIndeAct extends CmsIndeAction {

    private static final Logger log = LoggerFactory.getLogger(ArtiIndeAct.class);

    public String search() throws CorruptIndexException, IOException, ParseException {
        searchKey = RequestUtils.getQueryParam(ServletActionContext.getRequest(), "searchKey", "GBK");
        if (count > 200) {
            count = 200;
        }
        pagination = luceneArticleSvc.search(contextPvd.getAppRealPath(Constants.LUCENE_ARTICLE_PATH), searchKey, websiteId, chnlId, pageNo, count);
        return handleResult("Search");
    }

    @Override
    protected String getSysType() {
        return Constants.ARTICLE_SYS;
    }

    private int count;

    private String searchKey;

    private Long websiteId;

    private Long chnlId;

    private Pagination pagination;

    @Autowired
    private LuceneArticleSvc luceneArticleSvc;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Long getChnlId() {
        return chnlId;
    }

    public void setChnlId(Long chnlId) {
        this.chnlId = chnlId;
    }

    public Long getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Long websiteId) {
        this.websiteId = websiteId;
    }
}
