package com.alipay.poc.remoting.service.echo.test.base;

import java.net.MalformedURLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.spring.AbstractXFireSpringTest;
import org.springframework.context.ApplicationContext;

/**
 * ������webserver������в��Ի��࣬��Ҫָ����ʼ���õ�xml�ļ�
 * 
 * @author qihao
 * 
 * BaseLocationTest.java 2007-9-29
 */
public class BaseLocationTest extends AbstractXFireSpringTest {

    private static final Log log = LogFactory.getLog(BaseLocationTest.class);

    protected ApplicationContext createContext() {
        return new ClassPathXmlApplicationContext(new String[] { "classpath:org/codehaus/xfire/spring/xfire.xml", "classpath*:SpringRemotingServlet-servlet.xml", "classpath*:beans-echo.xml" });
    }

    /**
	 * �õ�serverʵ��
	 * 
	 * @param serviceClass�ӿ���
	 * @param serverName
	 *            �����XML�ж����serverName
	 * @return
	 */
    public Object getXFireService(Class<?> serviceClass, String serverName) {
        Object server = null;
        Service serviceModel = new ObjectServiceFactory().create(serviceClass);
        XFireProxyFactory factory = new XFireProxyFactory(getXFire());
        try {
            log.info("��ʼ����server");
            server = factory.create(serviceModel, "xfire.local://" + serverName);
            log.info("����server");
        } catch (MalformedURLException e) {
            log.info("serviceClass: " + serviceClass);
            log.info("serverName: " + serverName);
            log.error("I cant get the service!!", e);
        }
        return server;
    }
}
