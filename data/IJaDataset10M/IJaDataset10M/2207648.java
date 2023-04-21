package com.techstar.framework.web.hunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.techstar.framework.dao.model.CommonQueryObj;
import com.techstar.framework.service.commonquery.ICommonQueryService;
import com.techstar.framework.utils.GridXMLHelper;

/**
 * 通用查询DWR相关方法
 * 
 * @author majian
 */
public class CommonQueryHunter {

    private ICommonQueryService commonQueryService;

    public CommonQueryHunter() {
    }

    public List hunter(Map params) throws Exception {
        System.out.println("<====================================================>");
        String colNames = (String) params.get("KEY_COLNAMES");
        String filter = (String) params.get("KEY_FILTER");
        String sortOnlyCol = (String) params.get("KEY_SORTONLYCOL");
        String wheres = (String) params.get("KEY_WHERE");
        String page = (String) params.get("KEY_PAGE");
        String domainName = (String) params.get("KEY_DOMAINNAME");
        String queryType = (String) params.get("KEY_QUERYTYPE");
        String delParams = (String) params.get("KEY_DELPARAMS");
        String oparams = (String) params.get("KEY_OPARAMS");
        System.out.println("KEY_COLNAMES:::" + colNames);
        System.out.println("KEY_FILTER:::" + filter);
        System.out.println("KEY_SORTONLYCOL:::" + sortOnlyCol);
        System.out.println("KEY_WHERE:::" + wheres);
        System.out.println("KEY_PAGE:::" + page);
        System.out.println("KEY_DOMAINNAME:::" + domainName);
        System.out.println("KEY_QUERYTYPE:::" + queryType);
        System.out.println("KEY_DELPARAMS:::" + delParams);
        System.out.println("KEY_OPARAMS:::" + oparams);
        GridXMLHelper xmlhelper = new GridXMLHelper();
        CommonQueryObj queryObj = xmlhelper.parseQueryCondition(params, 0);
        List dataList = commonQueryService.getLogicElemList(queryObj);
        System.out.println(dataList);
        System.out.println("<====================================================>");
        return dataList;
    }

    public void setCommonQueryService(ICommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }
}
