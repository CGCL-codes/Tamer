package samples.servlet.xml.validation;

import org.scopemvc.controller.servlet.xml.XSLScopeServlet;
import org.scopemvc.core.Controller;

/**
 * <P>
 *
 * Override XSLScopeServlet to initialise application. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:51 $
 */
public class ValidationServlet extends XSLScopeServlet {

    /**
     * @return TODO: Describe the Return Value
     * @see ScopeServlet#createApplicationController()
     * @throws Exception TODO: Describe the Exception
     */
    protected Controller createApplicationController() throws Exception {
        return new ValidationController();
    }
}
