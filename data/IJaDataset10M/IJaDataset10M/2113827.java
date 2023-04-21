package net.syseventfw4j.ws.xmlrpc.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.syseventfw4j.configuration.ConfigurationManager;
import net.syseventfw4j.receiver.ReceiverFactory;
import net.syseventfw4j.test.TestBasicEvents;
import net.syseventfw4j.ws.IWSReceiver;
import net.syseventfw4j.ws.xmlrpc.XMLRPCReceiver;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class TestXMLRCP extends TestBasicEvents {

    private WebServer webServer;

    public TestXMLRCP() {
        super();
    }

    public TestXMLRCP(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestXMLRCP("testSimpleLogging"));
        suite.addTest(new TestXMLRCP("testBasicExceptionLogging"));
        suite.addTest(new TestXMLRCP("testTraceSimpleAndInformationLogging"));
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ConfigurationManager.loadRelativeAndInitialize("net/syseventfw4j/ws/xmlrpc/test/test_localConsoleConfiguration.xml");
        webServer = new WebServer(8080);
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.setRequestProcessorFactoryFactory(new RequestProcessorFactoryFactory() {

            public RequestProcessorFactory getRequestProcessorFactory(Class class1) throws XmlRpcException {
                return new RequestProcessorFactory() {

                    public Object getRequestProcessor(XmlRpcRequest xmlrpcrequest) throws XmlRpcException {
                        return ReceiverFactory.getInstance("wsReceiver");
                    }
                };
            }
        });
        phm.addHandler(IWSReceiver.class.getName(), XMLRPCReceiver.class);
        xmlRpcServer.setHandlerMapping(phm);
        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(true);
        webServer.start();
        setSetup(true);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        webServer.shutdown();
    }
}
