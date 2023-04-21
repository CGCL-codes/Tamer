package com.companyname.common.basedata.service;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.companyname.common.basedata.manager.TypeManager;

/**
 * 类别service单元测试用例模板
 *
 * @author amonlei
 *
 */
@Test(groups = { "common.basedata.type.service" }, enabled = true)
public class TestFindTypes4TreeViewService extends BaseDataServiceTestCaseTemplet {

    static Logger logger = Logger.getLogger(TestFindTypes4TreeViewService.class);

    private TypeManager typeManager;

    private FindTypes4TreeViewService findTypes4TreeViewService;

    private HttpServletRequest request;

    @BeforeClass
    public void setUp() {
        this.findTypes4TreeViewService = (FindTypes4TreeViewService) this.getServiceInst(FindTypes4TreeViewService.class);
    }

    @BeforeMethod
    public void mockManagerAndRequest() {
        this.typeManager = (TypeManager) this.getMockedManager(TypeManager.class);
        this.findTypes4TreeViewService.setTypeManager(this.typeManager);
        this.request = this.getMockedHttpServletRequest();
    }

    /** 测试createModels(),枚举不同request参数,这是第一种情况 */
    @Test
    public void testCreateModels1() {
        logger.debug("testCreateModels1()");
    }

    /** 测试setNodeViews(),枚举不同request参数,这是第一种情况 */
    @Test
    public void testSetNodeViews1() {
        logger.debug("testSetNodeViews1()");
    }
}
