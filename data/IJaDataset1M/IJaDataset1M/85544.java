package test.jws.cxf;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import test.jws.service.HelloWorld;
import test.jws.service.HelloWorldImpl;

/**
 * CXF jaxws factory bean 更简单的实现服务端
 * 
 * @author Sun
 * @version Server.java 2010-10-15 下午05:52:17
 * @see "http://cxf.apache.org/docs/a-simple-jax-ws-service.html"
 */
public class Server {

    public Server() {
        System.out.println("Starting Server");
        HelloWorldImpl implementor = new HelloWorldImpl();
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        svrFactory.setAddress("http://localhost:9000/helloWorld");
        svrFactory.setServiceClass(HelloWorld.class);
        svrFactory.setServiceBean(implementor);
        svrFactory.create();
    }

    public static void main(String args[]) throws Exception {
        new Server();
        System.out.println("Server ready...");
        Thread.sleep(5 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
}
