package krico.arara.servlet;

import krico.arara.controller.Facade;
import krico.arara.controller.IllegalFacadeStateException;
import krico.arara.controller.UserNotLogedInException;
import krico.arara.model.User;
import krico.arara.model.Group;
import krico.arara.model.Permission;
import krico.arara.model.PermissionException;
import krico.arara.model.DataFactory;
import krico.arara.model.DataFactoryException;
import krico.arara.model.calendar.CalendarView;
import krico.arara.model.calendar.Calendar;
import krico.arara.view.HtmlHelper;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Super class for all action handlers
 * @author @arara-author@
 * @version @arara-version@
 * @sf $Header: /cvsroot/arara/arara/sources/krico/arara/servlet/ActionHandler.java,v 1.8 2002/01/11 01:37:30 krico Exp $
 */
public abstract class ActionHandler {

    /**
   * Constructor that receives the parent controller as a parameter
   */
    protected ActionHandler(AraraController controller) {
        setController(controller);
    }

    /**
   * A reference to the controller
   */
    private AraraController controller = null;

    /**
   * Get the value of controller.
   * @return value of controller.
   */
    public final AraraController getController() {
        return controller;
    }

    /**
   * Get the facade stored in the controller
   */
    public Facade getFacade() {
        return getController() == null ? null : getController().getFacade();
    }

    /**
   * Set the value of controller.
   * @param v  Value to assign to controller.
   */
    private final void setController(AraraController v) {
        this.controller = v;
    }

    /**
   * This method returns true by default.  Overide it if you are writing an action handler that 
   * does not need a loged in user
   */
    public boolean checkLogedIn() {
        return true;
    }

    /**
   * This is the method called by the controller for an action that should be handled by this action handler
   * @throws IlegalFacadeStateException if the facade was not correctly initialized
   */
    public abstract void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, IllegalFacadeStateException, PermissionException, UserNotLogedInException;
}
