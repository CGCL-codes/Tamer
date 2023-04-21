package ctupressagent;

import com.sun.rave.web.ui.appbase.AbstractRequestBean;
import javax.faces.FacesException;

/**
 * <p>Request scope data bean for your application.  Create properties
 *  here to represent data that should be made available across different
 *  pages in the same HTTP request, so that the page bean classes do not
 *  have to be directly linked to each other.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @author biba
 */
public class RequestBean1 extends AbstractRequestBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    /**
     * <p>Construct a new request data bean instance.</p>
     */
    public RequestBean1() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * request scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * request scope.</p>
     * 
     * <p>You may customize this method to allocate resources that are required
     * for the lifetime of the current request.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("RequestBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    /**
     * <p>This method is called when this bean is removed from
     * request scope.  This occurs automatically when the corresponding
     * HTTP response has been completed and sent to the client.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the request.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }
}
