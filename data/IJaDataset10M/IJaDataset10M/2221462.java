package com.techstar.griddemo.web.hunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.techstar.framework.service.commonquery.ICommonQueryService;

/**
 * 业务字典Hunter类 * @author 
 * @date
 */
public class BusinessDicHunter {

    private ICommonQueryService commonQueryService;

    public BusinessDicHunter() {
    }

    public String getName() {
        return "businessdic";
    }

    public List hunter(Map maps) throws Exception {
        String src = (String) maps.get("src");
        List list = new ArrayList();
        if (StringUtils.isEmpty(src)) {
            return new ArrayList();
        } else {
            src = src.trim();
        }
        System.out.println(list.toString());
        System.out.println(list);
        return list;
    }

    public void setCommonQueryService(ICommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }
}
