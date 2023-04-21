package si.unimb.isportal07.iiFaq.controller.faqStates;

import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.State;

/**
 * @author gpolancic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ErrorFaqState extends State {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 4120855464432318768L;

    public static final String STATE_NAME = "errorFaqState";

    /**
	 * Constructor
	 */
    public ErrorFaqState() {
        super(ErrorFaqState.STATE_NAME, ErrorFaqState.STATE_NAME + "Description");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
        } catch (Exception e) {
        } finally {
        }
    }
}
