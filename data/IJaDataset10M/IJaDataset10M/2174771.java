package com.yubuild.coreman.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.yubuild.coreman.Constants;
import com.yubuild.coreman.business.MemoManager;
import com.yubuild.coreman.common.SearchData;
import com.yubuild.coreman.data.searchable.MemoSearchable;
import com.yubuild.coreman.web.util.CdmsUtil;

public class MemoListController implements Controller {

    private final transient Log log = LogFactory.getLog(getClass().getName());

    private MemoManager memoManager;

    public MemoListController() {
        memoManager = null;
    }

    public void setMemoManager(MemoManager memoManager) {
        this.memoManager = memoManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) log.debug("entering 'handleRequest' method...");
        String descriptionSearch = RequestUtils.getStringParameter(request, "descriptionSearch", null);
        SearchData sd = new SearchData("memoTable", request, "ASC", "description");
        MemoSearchable memoSearch = new MemoSearchable();
        memoSearch.setDescription(descriptionSearch);
        sd.setSearchCondition(memoSearch);
        CdmsUtil.addOrderBy(sd, memoSearch);
        memoSearch.setPageNumber(sd.getPageNo());
        memoSearch.setItemsPerPage(new Integer(Constants.MEMO_LIST_PAGE_SIZE));
        SearchData sdr = memoManager.getMemos(memoSearch);
        sd.setResults(sdr.getResults());
        sd.setResultsCount(sdr.getResultsCount());
        sd.fillRequest(request);
        request.setAttribute("memoList", sd.getResults());
        return new ModelAndView("memoList");
    }
}
