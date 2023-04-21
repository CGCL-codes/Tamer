package cnery;

import com.daveoxley.cbus.CGateSession;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

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
@Name("applicationBean")
@Startup()
@Scope(ScopeType.APPLICATION)
public class ApplicationBean implements Serializable {

    private static final Log log = LogFactory.getLog(ApplicationBean.class);

    @In
    private CGateSession cGateSession;

    /**
     * <p>Construct a new application data bean instance.</p>
     */
    public ApplicationBean() {
    }

    @Create
    public void init() {
        log.info("Creating ApplicationBean");
    }
}
