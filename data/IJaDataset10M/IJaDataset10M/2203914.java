package org.hardtokenmgmt.core.ui;

import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.TokenException;
import java.util.Collection;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import org.ejbca.core.model.authorization.AvailableAccessRules;
import org.hardtokenmgmt.ws.gen.EjbcaException_Exception;
import org.hardtokenmgmt.ws.gen.HTMFAdminWS;
import org.hardtokenmgmt.common.Constants;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.settings.AdministratorSettings;
import org.hardtokenmgmt.core.settings.GlobalSettings;
import org.hardtokenmgmt.core.token.IToken;
import org.hardtokenmgmt.core.token.TokenManager;
import org.hardtokenmgmt.core.util.ControllerCache;
import org.hardtokenmgmt.core.util.ControllerMemory;
import org.hardtokenmgmt.ui.ErrorController;
import org.signserver.module.wsra.ws.gen.WSRA;

/**
 * A Base class implementing the common methods of a Controller
 * It also contains a lot of help methods of how to access
 * the different interfaces and logging.
 * 
 * @author Philip Vendil 2007 feb 15
 *
 * @version $Id$
 */
public abstract class BaseController implements IController {

    protected BaseView view;

    public BaseController(BaseView view) {
        this.view = view;
    }

    /**
	 * @see org.hardtokenmgmt.core.ui.IController#getView()
	 */
    public BaseView getView() {
        return view;
    }

    /**
	 * Method that should be used when the controller is complete with its
	 * operations and want to give the control to another controller
	 */
    protected void switchControlTo(String controllerClassPath) {
        final String ccP = controllerClassPath;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                switch(GlobalSettings.appMode) {
                    case APPLET:
                        HardTokenManagementApplet.setCurrentController(ControllerCache.getControllerInstance(ccP));
                        break;
                    case APPLICATION:
                        HardTokenManagementApplication.setCurrentController(ControllerCache.getControllerInstance(ccP));
                        break;
                }
            }
        });
    }

    /**
	 * Method used to check if a given controller is the active one.
	 */
    protected boolean isControllerActive(String controllerClassPath) {
        switch(GlobalSettings.appMode) {
            case APPLET:
                return HardTokenManagementApplet.getCurrentController().getClass().getName().equals(controllerClassPath);
            case APPLICATION:
                return HardTokenManagementApplication.getCurrentController().getClass().getName().equals(controllerClassPath);
        }
        return false;
    }

    /**
	 * Same as swithcControlTo but always creates a fresh controller.
	 * What it does is to remove the cached controller and then creates a new one.
	 * 
	 */
    protected void switchToFreshController(String controllerClassPath) {
        ControllerCache.removeControllerInstance(controllerClassPath);
        switchControlTo(controllerClassPath);
    }

    /**
	 * Help method retrieving the EJBCA Interface
	 */
    protected HTMFAdminWS getHTMFAdminInterface() {
        return InterfaceFactory.getHTMFAdminInterface();
    }

    /**
	 * Help method retrieving the WSRA Interface
	 */
    protected WSRA getWSRAInterface() {
        return InterfaceFactory.getWSRAInterface();
    }

    /**
	 * Help method retrieving the Token Manager Interface
	 */
    protected TokenManager getTokenManager() {
        return InterfaceFactory.getTokenManager();
    }

    /**
	 * Help method retrieving the Global Settings Interface
	 */
    protected GlobalSettings getGlobalSettings() {
        return InterfaceFactory.getGlobalSettings();
    }

    /**
	 * Help method retrieving the Administrator Settings Interface
	 */
    protected AdministratorSettings getAdministratorSettings() {
        return InterfaceFactory.getAdministratorSettings();
    }

    /**
	 * Help method returning the instace of the controller memory.
	 */
    protected ControllerMemory getControllerMemory() {
        return ControllerMemory.getInstance();
    }

    /**
	 * Returns the value of the given controller setting.
	 * The controller setting should start with the implementing
	 * controllers classname (not path) in lowercase.
	 * 
	 * Ex for the controller named DummyController calling
	 * getControllerSetting with value 'testsetting' will
	 * lookup the property dummycontroller.testsetting in 
	 * global.properties
	 * 
	 */
    protected String getControllerSetting(String key) {
        String realKey = this.getClass().getSimpleName().toLowerCase() + "." + key;
        if (InterfaceFactory.getGlobalSettings().getProperty(realKey) != null) {
            return InterfaceFactory.getGlobalSettings().getProperty(realKey).trim();
        }
        return null;
    }

    /**
	 * Returns the value of the given controller setting.
	 * The controller setting should start with the implementing
	 * controllers classname (not path) in lowercase.
	 * 
	 * Ex for the controller named DummyController calling
	 * getControllerSetting with value 'testsetting' will
	 * lookup the property dummycontroller.testsetting in 
	 * global.properties
	 * 
	 */
    protected String getControllerSetting(String key, String defaultValue) {
        String realKey = this.getClass().getSimpleName().toLowerCase() + "." + key;
        return InterfaceFactory.getGlobalSettings().getProperty(realKey, defaultValue).trim();
    }

    /**
	 * Help method returning the first processable token
	 * Should only be used by controllers only supporting
	 * on processable token
	 */
    protected IToken getProcessableToken() {
        IToken retval = null;
        try {
            Collection<Slot> slots = getTokenManager().getSlots(TokenManager.SLOTTYPE_PROCESSABLECARDS);
            if (slots != null && slots.size() > 0) {
                Slot slot = (Slot) slots.iterator().next();
                retval = getTokenManager().getToken(slot);
            }
        } catch (TokenException e) {
            error("Error fetching processable token", e);
        }
        return retval;
    }

    /** 
	 * Help method to print a debug message to the local log
	 * @param msg the message to log.
	 */
    protected void debug(String msg) {
        LocalLog.debug(msg);
    }

    /** 
	 * Help method to print a debug message along with an excepttion 
	 * to the local log
	 * @param msg the message to log.
	 * @param throwable exception to log
	 */
    protected void debug(String msg, Throwable throwable) {
        LocalLog.debug(msg);
        LocalLog.debug(throwable);
    }

    /** 
	 * Help method to print a info message to the local log
	 * @param msg the message to log.
	 */
    protected void info(String msg) {
        LocalLog.getLogger().log(Level.INFO, msg);
    }

    /** 
	 * Help method to print a info message along with an excepttion 
	 * to the local log
	 * @param msg the message to log.
	 * @param throwable exception to log
	 */
    protected void info(String msg, Throwable throwable) {
        LocalLog.getLogger().log(Level.INFO, msg, throwable);
    }

    /** 
	 * Help method to print a error message to the local log
	 * @param msg the message to log.
	 */
    protected void error(String msg) {
        LocalLog.getLogger().log(Level.SEVERE, msg);
    }

    /** 
	 * Help method to print a error message along with an excepttion 
	 * to the local log
	 * @param msg the message to log.
	 * @param throwable exception to log
	 */
    protected void error(String msg, Throwable throwable) {
        LocalLog.getLogger().log(Level.SEVERE, msg, throwable);
    }

    /**
	 * Help method to check if the logged in administrator is a card administrator.
	 */
    protected boolean isAdmin() {
        boolean retval = false;
        if (getControllerMemory().getData("ISADMIN") == null) {
            try {
                retval = getHTMFAdminInterface().isAuthorized(AvailableAccessRules.ROLE_ADMINISTRATOR);
                retval &= getHTMFAdminInterface().isAuthorized(AvailableAccessRules.REGULAR_VIEWPUKS);
                getControllerMemory().putData("ISADMIN", retval);
            } catch (EjbcaException_Exception e) {
                getControllerMemory().putData(ErrorController.CCERROREXCEPTION, e);
                switchControlTo(ErrorController.class.getName());
            }
        } else {
            return (Boolean) getControllerMemory().getData("ISADMIN");
        }
        return retval;
    }

    /**
	 * Help method to check if the logged in administrator is a spare card administrator.
	 */
    protected boolean isSpareCardAdmin() {
        boolean retval = false;
        if (getControllerMemory().getData("ISSPARECARDADMIN") == null) {
            try {
                retval = getHTMFAdminInterface().isAuthorized(Constants.ROLE_SPARECARDADMINISTRATOR_ACCESSRULE);
                retval &= getHTMFAdminInterface().isAuthorized(AvailableAccessRules.REGULAR_VIEWPUKS);
                getControllerMemory().putData("ISSPARECARDADMIN", retval);
            } catch (EjbcaException_Exception e) {
                getControllerMemory().putData(ErrorController.CCERROREXCEPTION, e);
                switchControlTo(ErrorController.class.getName());
            }
        } else {
            return (Boolean) getControllerMemory().getData("ISSPARECARDADMIN");
        }
        return retval;
    }

    /**
	 * Help method checking if the pages should
	 * be displayed in non-admin mode
	 */
    protected boolean isNonAdmin() {
        if (getControllerMemory().getData("ISNONADMINPAGE") != null) {
            return ((Boolean) getControllerMemory().getData("ISNONADMINPAGE"));
        }
        return false;
    }
}
