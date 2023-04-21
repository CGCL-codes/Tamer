package org.ujac.web.demo.actions;

import java.io.IOException;
import javax.servlet.ServletException;
import org.ujac.util.UjacException;
import org.ujac.web.servlet.ActionContext;
import org.ujac.web.servlet.ActionSupport;
import org.ujac.web.servlet.ForwardAction;

/**
 * Name: IndexAction<br>
 * Description: Description of the class.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2004/12/24 02:06:38  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 2011 $
 */
public class IndexAction extends ActionSupport {

    /**
   * @see org.ujac.web.servlet.Action#getName()
   */
    public String getName() {
        return "index";
    }

    /**
   * @see org.ujac.web.servlet.ActionSupport#getView()
   */
    public String getView() {
        return "/welcome.jsp";
    }

    /**
   * @see org.ujac.web.servlet.Action#perform(org.ujac.web.servlet.ActionContext)
   */
    public ForwardAction perform(ActionContext ctx) throws ServletException, IOException, UjacException {
        return null;
    }
}
