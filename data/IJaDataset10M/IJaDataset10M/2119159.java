package netgest.bo.xwc.xeo.beans;

import netgest.bo.def.boDefHandler;
import netgest.bo.ejb.boManagerLocal;
import netgest.bo.runtime.boBridgeIterator;
import netgest.bo.runtime.boBridgeRow;
import netgest.bo.runtime.boObject;
import netgest.bo.runtime.boObjectList;
import netgest.bo.runtime.boRuntimeException;
import netgest.bo.runtime.bridgeHandler;
import netgest.bo.system.Logger;
import netgest.bo.system.boApplication;
import netgest.bo.xwc.components.classic.GridColumnRenderer;
import netgest.bo.xwc.components.classic.GridPanel;
import netgest.bo.xwc.components.classic.MessageBox;
import netgest.bo.xwc.components.classic.scripts.XVWScripts;
import netgest.bo.xwc.components.connectors.DataFieldConnector;
import netgest.bo.xwc.components.connectors.DataListConnector;
import netgest.bo.xwc.components.connectors.DataRecordConnector;
import netgest.bo.xwc.components.connectors.XEOObjectListConnector;
import netgest.bo.xwc.framework.XUIMessage;
import netgest.bo.xwc.framework.XUIRequestContext;
import netgest.bo.xwc.framework.components.XUIForm;
import netgest.bo.xwc.framework.components.XUIViewRoot;
import netgest.bo.xwc.xeo.components.EditToolBar;
import netgest.bo.xwc.xeo.localization.BeansMessages;

/**
 * 
 *  Bean to support the "User Favorites" viewer. Which displays a grid with the list
 *  of favorite viewers of a user (which are added using the {@link EditToolBar} component
 *  on an edit viewer
 *
 */
public class FavoritesBean extends XEOBaseBean {

    /**
	 * The Logger
	 */
    private static final Logger logger = Logger.getLogger(FavoritesBean.class);

    /**
	 * The renderer for the grid to display the label of the object model (classname) 
	 */
    private GridColumnRenderer classname = null;

    /**
	 * Field to hold the currently selected favorite to remove from the list
	 */
    private long selectedBouiForDelete = 0;

    /**
	 * A constant with the title for the viewer
	 */
    private static final String BEAN_TITLE = "<img src='ext-xeo/icons/favorite.png' />" + BeansMessages.FAVORITES_BEAN_TITLE.toString();

    public String getTitle() {
        return BEAN_TITLE;
    }

    /**
	 * 
	 * Retrieves the list of favorites objects for the current user
	 * 
	 * @return A list of Ebo_FavoriteViewer instances
	 */
    public DataListConnector getFavoritesList() {
        String boql = "select Ebo_UserPreferences.favorites where owner = CTX_PERFORMER_BOUI";
        boObjectList list = boObjectList.list(getEboContext(), boql);
        return new XEOObjectListConnector(list);
    }

    /**
	 * Given a selected Ebo_Favorite instance, opens the corresponding viewer in a new tab
	 */
    public void openObject() {
        GridPanel p = (GridPanel) getViewRoot().findComponent(GridPanel.class);
        DataRecordConnector line = p.getActiveRow();
        if (line != null) {
            try {
                long boui = Long.valueOf(line.getAttribute("targetBouiObj").getValue().toString()).longValue();
                boObject current = boApplication.getDefaultApplication().getObjectManager().loadObject(getEboContext(), boui);
                String viewerName = current.getName();
                if (boDefHandler.getBoDefinition(viewerName) == null) {
                    throw new RuntimeException("Object " + viewerName + " does not exist");
                }
                XUIViewRoot viewRoot = getSessionContext().createChildView("viewers/" + viewerName + "/edit.xvw");
                getRequestContext().setViewRoot(viewRoot);
                XEOEditBean bean = (XEOEditBean) viewRoot.getBean("viewBean");
                bean.setCurrentObjectKey(current.getBoui());
                getRequestContext().renderResponse();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (boRuntimeException e) {
                logger.severe(e);
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * 
	 * A Column renderer to display the XEOMOdel label in the list of favorite instances
	 * so that a user knows the type of each instance
	 * 
	 * @return
	 */
    public GridColumnRenderer getClassName() {
        if (classname != null) return classname;
        classname = new GridColumnRenderer() {

            @Override
            public String render(GridPanel grid, DataRecordConnector record, DataFieldConnector field) {
                long boui = Long.valueOf(record.getAttribute("targetBouiObj").getValue().toString());
                boObject toDisplay;
                try {
                    toDisplay = boApplication.getDefaultApplication().getObjectManager().loadObject(getEboContext(), boui);
                    return toDisplay.getLabel();
                } catch (boRuntimeException e) {
                    e.printStackTrace();
                    logger.severe(e);
                }
                return "";
            }
        };
        return classname;
    }

    /**
	 * Open a popup dialog which asks the user to confirm the deletion of the selected
	 * favorite
	 */
    public void removeConfirm() {
        GridPanel p = (GridPanel) getViewRoot().findComponent(GridPanel.class);
        DataRecordConnector line = p.getActiveRow();
        if (line != null) {
            XUIForm f = (XUIForm) getViewRoot().findComponent(XUIForm.class);
            MessageBox alertBox = (MessageBox) f.findComponent(f.getId() + ":removeConfirm");
            this.selectedBouiForDelete = Long.valueOf(line.getAttribute("BOUI").getValue().toString());
            if (alertBox != null) alertBox.show();
        }
    }

    /**
	 * Dummy method for the "No" button when asking to remove an instance
	 */
    public void dummy() {
    }

    /**
	 * Removes a given favorite from the user's list of favorites
	 */
    public void removeFavorite() {
        if (selectedBouiForDelete > 0) {
            try {
                boManagerLocal obManager = boApplication.getDefaultApplication().getObjectManager();
                boObject toRemoveObj = obManager.loadObject(getEboContext(), selectedBouiForDelete);
                boObject parentUserSettings = obManager.loadObject(getEboContext(), "select Ebo_UserPreferences where owner = CTX_PERFORMER_BOUI and favorites IN ( ? )", new Object[] { selectedBouiForDelete });
                bridgeHandler favoritesBridge = parentUserSettings.getBridge("favorites");
                boBridgeIterator it = favoritesBridge.iterator();
                while (it.next()) {
                    boBridgeRow row = it.currentRow();
                    boObject candidateToRemove = row.getObject();
                    if (candidateToRemove.getBoui() == selectedBouiForDelete) {
                        favoritesBridge.remove();
                        parentUserSettings.update();
                        break;
                    }
                }
                toRemoveObj.destroy();
                getRequestContext().addMessage("deleted", new XUIMessage(XUIMessage.TYPE_ALERT, XUIMessage.SEVERITY_INFO, BeansMessages.FAVORITES_BEAN_DELETED_MSG_TITLE.toString(), BeansMessages.FAVORITES_BEAN_DELETED_MSG.toString()));
                selectedBouiForDelete = 0;
            } catch (boRuntimeException e) {
                e.printStackTrace();
                logger.warn(e);
            }
        } else {
            getRequestContext().addMessage("noObject", new XUIMessage(XUIMessage.TYPE_ALERT, XUIMessage.SEVERITY_INFO, BeansMessages.FAVORITES_BEAN_NO_OBJ_MSG_TITLE.toString(), BeansMessages.FAVORITES_BEAN_NO_OBJ_MSG.toString()));
        }
    }

    /**
	 * Close the tab
	 */
    public void canCloseTab() {
        XUIRequestContext oRequestContext;
        oRequestContext = XUIRequestContext.getCurrentContext();
        XVWScripts.closeView(oRequestContext.getViewRoot());
        XUIViewRoot viewRoot = oRequestContext.getSessionContext().createView("netgest/bo/xwc/components/viewers/Dummy.xvw");
        oRequestContext.setViewRoot(viewRoot);
        oRequestContext.renderResponse();
    }
}
