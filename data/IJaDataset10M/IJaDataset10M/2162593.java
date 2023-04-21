package cnery;

import cnerydb.HibernateUtil;
import cnerydb.Setting;
import com.daveoxley.cbus.CGateException;
import com.daveoxley.cbus.CGateInterface;
import com.daveoxley.cbus.CGateSession;
import com.daveoxley.cnery.SceneStatusChangeCallback;
import com.sun.rave.web.ui.appbase.AbstractApplicationBean;
import java.net.InetAddress;
import javax.faces.FacesException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * <p>Application scope data bean for your application.  Create properties
 *  here to represent cached data that should be made available to all users
 *  and pages in the application.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
public class ApplicationBean1 extends AbstractApplicationBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    /**
     * <p>Construct a new application data bean instance.</p>
     */
    public ApplicationBean1() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * application scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * application scope.</p>
     * 
     * <p>You may customize this method to initialize and cache application wide
     * data values (such as the lists of valid options for dropdown list
     * components), or to allocate resources that are required for the
     * lifetime of the application.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("ApplicationBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
        initCGateSession();
    }

    /**
     * <p>This method is called when this bean is removed from
     * application scope.  Typically, this occurs as a result of
     * the application being shut down by its owning container.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
     */
    @Override
    public void destroy() {
        if (cGateSession != null && cGateSession.isConnected()) {
            try {
                cGateSession.close();
            } catch (CGateException ex) {
            }
            cGateSession = null;
        }
    }

    /**
     * <p>Return an appropriate character encoding based on the
     * <code>Locale</code> defined for the current JavaServer Faces
     * view.  If no more suitable encoding can be found, return
     * "UTF-8" as a general purpose default.</p>
     *
     * <p>The default implementation uses the implementation from
     * our superclass, <code>AbstractApplicationBean</code>.</p>
     */
    @Override
    public String getLocaleCharacterEncoding() {
        return super.getLocaleCharacterEncoding();
    }

    private static final boolean demoBuild = false;

    void initCGateSession() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        initCGateSession(session);
    }

    void initCGateSession(Session session) {
        try {
            if (cGateSession != null && cGateSession.isConnected()) {
                try {
                    cGateSession.close();
                } catch (CGateException ex) {
                }
                cGateSession = null;
            }
            if (demoBuild) {
                cGateServer = "localhost";
                cGateCommandPort = 30023;
                cGateEventPort = 30024;
                cGateSCPort = 30025;
            } else {
                Query q = session.createQuery("from Setting where name = 'cgate_server'");
                Setting cGateServerSetting = (Setting) q.uniqueResult();
                if (cGateServerSetting != null) cGateServer = cGateServerSetting.getValue();
                q = session.createQuery("from Setting where name = 'cgate_command_port'");
                Setting cGateCommandPortSetting = (Setting) q.uniqueResult();
                if (cGateCommandPortSetting != null) cGateCommandPort = Integer.parseInt(cGateCommandPortSetting.getValue());
                q = session.createQuery("from Setting where name = 'cgate_event_port'");
                Setting cGateEventPortSetting = (Setting) q.uniqueResult();
                if (cGateEventPortSetting != null) cGateEventPort = Integer.parseInt(cGateEventPortSetting.getValue());
                q = session.createQuery("from Setting where name = 'cgate_status_change_port'");
                Setting cGateSCPortSetting = (Setting) q.uniqueResult();
                if (cGateSCPortSetting != null) cGateSCPort = Integer.parseInt(cGateSCPortSetting.getValue());
            }
            InetAddress cGateServerAddr = InetAddress.getByName(cGateServer);
            cGateSession = CGateInterface.connect(cGateServerAddr, cGateCommandPort, cGateEventPort, cGateSCPort);
            cGateSession.registerStatusChangeCallback(new SceneStatusChangeCallback());
        } catch (Exception e) {
            connectException = e;
        }
    }

    private static CGateSession cGateSession = null;

    private static Exception connectException = null;

    public static CGateSession getCGateSession() throws Exception {
        if (cGateSession == null) throw connectException;
        return cGateSession;
    }

    private int cGateCommandPort = 20023;

    public int getCGateCommandPort() {
        return cGateCommandPort;
    }

    private int cGateEventPort = 20024;

    public int getCGateEventPort() {
        return cGateEventPort;
    }

    private int cGateSCPort = 20025;

    public int getCGateSCPort() {
        return cGateSCPort;
    }

    private String cGateServer = "localhost";

    public String getCGateServer() {
        return cGateServer;
    }
}
