package net.sf.jabs.test;

import java.io.IOException;
import javax.naming.NamingException;
import junit.framework.TestCase;
import net.sf.jabs.data.project.ProjectDAO;
import net.sf.jabs.test.util.JNDIUnitTestHelper;

public abstract class AbstractTestCase extends TestCase {

    private String _basePath;

    protected ProjectDAO _projectDAO;

    public AbstractTestCase(String method) {
        super(method);
    }

    public void initPath() {
        _basePath = System.getProperty("jabs.path");
    }

    public String getProjectPath() {
        if (_basePath == null) {
            _basePath = System.getProperty("jabs.path");
        }
        return _basePath;
    }

    public void initJNDI() {
        if (JNDIUnitTestHelper.notInitialized()) {
            try {
                String jndiConfig = "test/etc/jndihelper.properties";
                if (System.getProperty("jabs.jndihelper") != null) {
                    jndiConfig = System.getProperty("jabs.jndihelper");
                }
                JNDIUnitTestHelper.init(jndiConfig);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                fail("IOException thrown : " + ioe.getMessage());
            } catch (NamingException ne) {
                ne.printStackTrace();
                fail("NamingException thrown on Init : " + ne.getMessage());
            }
        }
    }

    protected void setUp() {
        System.out.println("SETUP");
        initJNDI();
    }

    protected ProjectDAO getProjectDAO() {
        if (_projectDAO == null) {
            _projectDAO = new ProjectDAO();
        }
        return _projectDAO;
    }
}
