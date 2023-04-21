package com.wonebiz.crm.server.testSpring;

import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import com.isomorphic.datasource.DSRequest;
import com.wonebiz.crm.server.service.VisitService;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestVisit extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource(name = "visitService")
    private VisitService visitService;

    @Test
    public void simple() {
        visitService.fetch(new DSRequest());
    }
}
