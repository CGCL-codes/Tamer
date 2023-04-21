package de.beas.explicanto.client.utlis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import de.bea.services.vidya.client.datasource.GraphicConst;
import de.bea.services.vidya.client.datasource.UserNames;
import de.bea.services.vidya.client.datasource.VidUtil;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.CPageLayout;
import de.bea.services.vidya.client.datastructures.CStyleSheet;
import de.bea.xui.Xui;
import de.bea.xui.XuiExceptionHandler;
import de.beas.explicanto.client.rcp.model.Model;

/**
 * Simulator of <code>de.bea.services.vidya.client.gui.MainController</code>
 * This class is copies from the original one and simulates it's 
 * functionality. It has only the methods needed my other controllers
 * (MediaLibCotrollers, RoleDlgController, etc). This way there is no need 
 * for complicated modifications in these classes. 
 * 
 * It does not open any windows. It is not used by any xui xml file 
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class MainController {

    private Xui xui;

    private VidyaDataTree dataTree;

    public StatusController statusController = null;

    /**
     * The <code>context</code> parameter is the Context class of the 
     * 2.5 version. It is used by superclasses in this form so it has to
     * be kept  
     * 
     * @param parentController - set to null
     * @param context - the context of the aplications
     */
    public MainController() {
        dataTree = VidyaDataTree.getDefault();
        statusController = new StatusController(this);
    }

    public void exit() {
        xui.getXuiThreadManager().clearThreadsInPool();
    }

    public boolean onWindowClosing() {
        return false;
    }

    public void windowClosed() {
    }

    public List getRoleTypes() {
        return dataTree.getRootCustomer().getRoleTypes();
    }

    public Xui getXui() {
        return xui;
    }

    /**
     * @see de.bea.xui.XuiExceptionHandler#getDelegationExceptionHandler()
     */
    public final XuiExceptionHandler getDelegationExceptionHandler() {
        return null;
    }

    /**
     * Returns the connection to the server <code>VidyaDataTree</code> 
     *
     * @return
     */
    public VidyaDataTree getTree() {
        return dataTree;
    }

    public void handleException(Exception e, Object o) {
        e.printStackTrace();
    }

    /**
     * Gets the name of the currently logged in user 
     * 
     * @return user's name
     */
    public String getLoggedInUserName() {
        return UserNames.getLoggedInUser().getUsername();
    }

    public long getLoggedInUserId() {
        return UserNames.getLoggedInUserId();
    }

    public boolean isAdmin() {
        return UserNames.getLoggedInUser().getAdmin() == 1;
    }
}
