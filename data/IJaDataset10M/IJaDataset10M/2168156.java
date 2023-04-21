package de.wesodi.ui.richfaces;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import wesodi.entities.persi.AccessPoint;
import wesodi.entities.persi.AvailableProduct;
import wesodi.entities.persi.DistributedProduct;
import wesodi.entities.persi.DistributionPoint;
import wesodi.entities.persi.UpdateRule;
import wesodi.entities.transi.ServerRequest;
import wesodi.entities.transi.ServerResponse;
import wesodi.logic.manage.interfaces.AvailableProductManagement;
import wesodi.logic.manage.interfaces.DistributionManagement;

/**
 * @author Maria Krieg
 * @date 10.03.2009
 * 
 */
@SuppressWarnings("unused")
public class Initializer extends AbstractManagedBean {

    private List<SelectItem> availableArchives;

    private List<AvailableProduct> myAvailableProducts;

    private List<DistributedProduct> myDistributedProducts;

    private List<SelectItem> myDistributionPoints;

    private List<AvailableProduct> availableProducts;

    private List<SelectItem> availableUpdateRules;

    @SuppressWarnings("unchecked")
    public List<SelectItem> getAvailableArchives() {
        ServerRequest request = new ServerRequest(AvailableProductManagement.METH_GET_ARCHIVES, AvailableProductManagement.class.getSimpleName());
        try {
            ServerResponse response = userSession.handle(request);
            if (isResponseValid(response)) {
                List<AccessPoint> accessPoints = (List<AccessPoint>) response.getResults().get(AvailableProductManagement.RESP_KEY_ARCHIVES);
                List<SelectItem> list = new ArrayList<SelectItem>();
                if (accessPoints != null) {
                    for (AccessPoint accessPoint : accessPoints) {
                        list.add(new SelectItem(accessPoint));
                    }
                } else {
                    System.out.println("Initializer.getAvailableArchives() >>> accessPoints is null");
                }
                return list;
            }
        } catch (Exception e) {
            addServerErrorMessage("Error while handling server request.");
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<AvailableProduct> getMyAvailableProducts() {
        ServerRequest request = new ServerRequest(AvailableProductManagement.METH_GET_AVAILABLE_PRODUCTS, AvailableProductManagement.class.getSimpleName());
        request.addParameter(AvailableProductManagement.REQ_KEY_MY, "justNotNull");
        ServerResponse response = userSession.handle(request);
        if (isResponseValid(response)) {
            List<AvailableProduct> productList = (List<AvailableProduct>) response.getResults().get(AvailableProductManagement.RESP_KEY_PRODUCT_LIST);
            if (productList == null) {
                productList = new ArrayList<AvailableProduct>();
            }
            return productList;
        }
        return null;
    }

    public void setMyAvailableProducts(List<SelectItem> listMyAvailableProducts) {
    }

    public void setAvailableArchives(List<AccessPoint> list) {
    }

    /**
	 * @param myDistributedProducts
	 *            the myDistributedProducts to set
	 */
    public void setMyDistributedProducts(List<DistributedProduct> myDistributedProducts) {
        this.myDistributedProducts = myDistributedProducts;
    }

    /**
	 * @return the myDistributedProducts
	 */
    @SuppressWarnings("unchecked")
    public List<DistributedProduct> getMyDistributedProducts() {
        ServerRequest request = new ServerRequest(DistributionManagement.METH_GET_DISTRIBUTED_PRODUCTS, DistributionManagement.class.getSimpleName());
        request.addParameter(DistributionManagement.REQ_KEY_MY, "justNotNull");
        ServerResponse response = userSession.handle(request);
        if (isResponseValid(response)) {
            List<DistributedProduct> productList = (List<DistributedProduct>) response.getResults().get(DistributionManagement.RESP_KEY_MY_DISTRIBUTED_PRODUCTS);
            if (productList == null) {
                productList = new ArrayList<DistributedProduct>();
            }
            return productList;
        }
        return null;
    }

    /**
	 * @return the myDistributionPoints
	 */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getMyDistributionPoints() {
        ServerRequest request = new ServerRequest(DistributionManagement.METH_GET_DISTRIBUTIONPOINTS, DistributionManagement.class.getSimpleName());
        request.addParameter(DistributionManagement.REQ_KEY_MY, "justNotNull");
        ServerResponse response = userSession.handle(request);
        if (isResponseValid(response)) {
            List<DistributionPoint> responseValue = (List<DistributionPoint>) response.getResults().get(DistributionManagement.RESP_KEY_MY_DISTRIBUTIONPOINTS);
            List<SelectItem> list = new ArrayList<SelectItem>();
            if (responseValue != null) {
                if (responseValue.size() > 0) {
                    for (DistributionPoint point : responseValue) {
                        list.add(new SelectItem(point));
                    }
                }
                return list;
            } else {
                System.out.println("Initializer.getMyDistributionPoints() >>>>>>>>> responseValue is null.");
            }
        }
        return null;
    }

    /**
	 * @param myDistributionPoints
	 *            the myDistributionPoints to set
	 */
    public void setMyDistributionPoints(List<SelectItem> myDistributionPoints) {
    }

    @SuppressWarnings("unchecked")
    public List<AvailableProduct> getAvailableProducts() {
        ServerRequest request = new ServerRequest(AvailableProductManagement.METH_GET_AVAILABLE_PRODUCTS, AvailableProductManagement.class.getSimpleName());
        request.addParameter(AvailableProductManagement.REQ_KEY_ALL, "justNotNull");
        ServerResponse response = userSession.handle(request);
        if (isResponseValid(response)) {
            List<AvailableProduct> productList = (List<AvailableProduct>) response.getResults().get(AvailableProductManagement.RESP_KEY_PRODUCT_LIST);
            if (productList == null) {
                productList = new ArrayList<AvailableProduct>();
            }
            return productList;
        }
        return null;
    }

    public void setAvailableProducts(List<AvailableProduct> availableProducts) {
        this.availableProducts = availableProducts;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getAvailableUpdateRules() {
        ServerRequest request = new ServerRequest(DistributionManagement.METH_GET_UPDATE_RULES, DistributionManagement.class.getSimpleName());
        ServerResponse response = userSession.handle(request);
        if (isResponseValid(response)) {
            List<UpdateRule> responseList = (List<UpdateRule>) response.getResults().get(DistributionManagement.RESP_KEY_UPDATE_RULES);
            List<SelectItem> list = new ArrayList<SelectItem>();
            if (responseList != null) {
                for (UpdateRule rule : responseList) {
                    list.add(new SelectItem(rule));
                }
            } else {
                System.out.println("Initializer.getAvailableUpdateRules() >>> Server returned null value in response.");
            }
            return list;
        }
        return null;
    }

    public void setAvailableUpdateRules(List<SelectItem> availableUpdateRules) {
        this.availableUpdateRules = availableUpdateRules;
    }
}
