package com.hand.test;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.hand.model.TVisitWeb;
import com.hand.service.IVisitWebService;

/**
 * 系统名：HCSMobileApp
 * 子系统名：测试拜访Service接口
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 12, 2011
 */
public class TestIVisitWebService extends TestBase {

    private IVisitWebService visitWebService;

    /**
	 * 测试根据创建者获取最后一次拜访信息
	 */
    public void testLastVisitWebByStaffMember() {
        try {
            int count = visitWebService.getMinNotExportTVisitWeb();
            this.assertNotNull(count);
            this.assertEquals(count, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setComplete();
    }

    public void setVisitWebService(IVisitWebService visitWebService) {
        this.visitWebService = visitWebService;
    }
}
