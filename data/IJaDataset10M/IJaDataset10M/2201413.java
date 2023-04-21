package org.agile.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

public class LocalDatastoreTestCase extends LocalServiceTestCase {

    private ApplicationContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
        context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring-*.xml");
    }

    @Override
    public void tearDown() throws Exception {
        ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
        datastoreService.clearProfiles();
        super.tearDown();
    }

    public Object getBean(String name) {
        return context.getBean(name);
    }
}
