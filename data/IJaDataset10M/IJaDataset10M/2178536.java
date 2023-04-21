package tests;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import redstone.xmlrpc.XmlRpcServlet;

@SuppressWarnings("serial")
public class TestServlet extends XmlRpcServlet {

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        TestService test = new TestService();
        this.getXmlRpcServer().addInvocationHandler("Test", test);
    }
}
