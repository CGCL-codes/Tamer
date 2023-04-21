package de.wesodi.ui.richfaces.productmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import wesodi.entities.persi.AvailableProduct;
import wesodi.entities.persi.DistributedProduct;
import wesodi.entities.persi.compositeIds.DistributedProductId;
import wesodi.logic.session.UserSessionRemote;

/**
 * @author Maria Krieg
 * @date 23.03.2009
 */
public class ListMyAvailableProducts {

    private UserSessionRemote userSession;

    private List<AvailableProduct> myProducts;

    private List<AvailableProduct> dirtyProducts;

    private String accessPoint;

    private Map<DistributedProductId, String> state;

    private List<SelectItem> states;

    private Long saveId;

    /**
	 * @return the saveId
	 */
    public Long getSaveId() {
        return saveId;
    }

    /**
	 * @param saveId the saveId to set
	 */
    public void setSaveId(Long saveId) {
        this.saveId = saveId;
    }

    public void save() {
        System.out.println("ListMyAvailableProducts.save() >>> performed");
    }

    /**
	 * @param states
	 *            the states to set
	 */
    public void setStates(List<SelectItem> states) {
        this.states = states;
    }

    private List<SelectItem> availableArchives;

    /**
	 * @return the distProdStates
	 */
    public List<SelectItem> getStates() {
        if (states == null) {
            List<String> statesList = DistributedProduct.getStates();
            states = new ArrayList<SelectItem>();
            if (statesList != null) {
                for (String string : statesList) {
                    states.add(new SelectItem(string));
                }
            }
        }
        return states;
    }

    /**
	 * @return the accessPoint
	 */
    public String getAccessPoint() {
        return accessPoint;
    }

    /**
	 * @param accessPoint
	 *            the accessPoint to set
	 */
    public void setAccessPoint(String accessPoint) {
        this.accessPoint = accessPoint;
    }

    /**
	 * @return the userSession
	 */
    public UserSessionRemote getUserSession() {
        return userSession;
    }

    /**
	 * @param userSession
	 *            the userSession to set
	 */
    public void setUserSession(UserSessionRemote userSession) {
        this.userSession = userSession;
    }

    /**
	 * @return the myProducts
	 */
    public List<AvailableProduct> getMyProducts() {
        return myProducts;
    }

    /**
	 * @param myProducts
	 *            the myProducts to set
	 */
    public void setMyProducts(List<AvailableProduct> myProducts) {
        this.myProducts = myProducts;
    }

    /**
	 * @return the dirtyProducts
	 */
    public List<AvailableProduct> getDirtyProducts() {
        return dirtyProducts;
    }

    /**
	 * @param dirtyProducts
	 *            the dirtyProducts to set
	 */
    public void setDirtyProducts(List<AvailableProduct> dirtyProducts) {
        this.dirtyProducts = dirtyProducts;
    }

    /**
	 * @return the availableArchives
	 */
    public List<SelectItem> getAvailableArchives() {
        return availableArchives;
    }

    /**
	 * @param availableArchives
	 *            the availableArchives to set
	 */
    public void setAvailableArchives(List<SelectItem> availableArchives) {
        this.availableArchives = availableArchives;
    }

    /**
	 * @return the state
	 */
    public Map<DistributedProductId, String> getState() {
        if (state == null) {
            state = new HashMap<DistributedProductId, String>();
        }
        return state;
    }

    /**
	 * @param state
	 *            the state to set
	 */
    public void setState(HashMap<DistributedProductId, String> state) {
        this.state = state;
    }
}
