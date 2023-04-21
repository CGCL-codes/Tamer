package com.myres.struts2.action;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.myres.model.Shop;
import com.myres.model.User;
import com.myres.service.ShopService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class GetMyShopAction extends ActionSupport {

    private Set<Shop> myShop;

    private Map<String, String> times;

    public Map<String, String> getTimes() {
        return times;
    }

    public void setTimes(Map<String, String> times) {
        this.times = times;
    }

    public Set<Shop> getMyShop() {
        return myShop;
    }

    public void setMyShop(Set<Shop> myShop) {
        this.myShop = myShop;
    }

    private ShopService getShopListService;

    private String[] shopList_String;

    private String aa;

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public GetMyShopAction() {
        super();
        System.out.println("constructor of GetMyShopAction");
    }

    public ShopService getGetShopListService() {
        return getShopListService;
    }

    public void setGetShopListService(ShopService getShopListService) {
        this.getShopListService = getShopListService;
    }

    public void setShopList_String(String[] shopList_String) {
        this.shopList_String = shopList_String;
    }

    public String[] getShopList_String() {
        return shopList_String;
    }

    public String execute() {
        User user = (User) ActionContext.getContext().getSession().get("user");
        this.myShop = getShopListService.getMyShop(user.getId());
        if (this.myShop == null || myShop.size() == 0) {
            times = new TreeMap<String, String>();
            for (int i = 0; i < 24; i++) {
                String key = i < 10 ? "0" + i + ":00" : "" + i + ":00";
                String value = i < 10 ? "0" + i + ":00:00" : "" + i + ":00" + ":00";
                times.put(key, value);
            }
            return "no_shop";
        } else {
            System.out.print("fafdasfadsmyShip.size=" + myShop.size());
            return SUCCESS;
        }
    }
}
