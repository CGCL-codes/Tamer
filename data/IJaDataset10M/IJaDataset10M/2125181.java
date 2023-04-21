package jaxlib.security.action;

import java.security.AccessController;
import java.security.AccessControlContext;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import jaxlib.security.Securities;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: GetSubjectAction.java 2597 2008-04-29 14:18:03Z joerg_wassmer $
 */
public final class GetSubjectAction extends Object implements PrivilegedAction<Subject> {

    private final AccessControlContext context;

    public GetSubjectAction() {
        super();
        this.context = AccessController.getContext();
    }

    public GetSubjectAction(final AccessControlContext context) {
        super();
        this.context = (context == null) ? AccessController.getContext() : context;
    }

    @Override
    public final Subject run() {
        return Securities.getSubject(this.context);
    }
}
