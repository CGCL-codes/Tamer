package com.boleyn.oa.manager;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.boleyn.oa.model.Organization;
import junit.framework.TestCase;

public class OrgmangerTest extends TestCase {

    private BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");

    public void testAddOrg() {
        OrgManager orgManager = (OrgManager) factory.getBean("orgManager");
        Organization org = new Organization();
        org.setName("测试机构");
        org.setDescription("技术部");
        orgManager.addOrg(org, 0);
    }

    public void testDelOrg() {
        fail("Not yet implemented");
    }

    public void testUpdateOrg() {
        fail("Not yet implemented");
    }

    public void testFindOrg() {
        fail("Not yet implemented");
    }

    public void testFindOrgs() {
        fail("Not yet implemented");
    }
}
