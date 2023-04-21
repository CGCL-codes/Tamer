package com.xiaxueqi.web.shop;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.web.struts2.Struts2Utils;
import com.niagara.service.base.BaseService;
import com.niagara.web.struts2.CRUDActionSupport;
import com.xiaxueqi.entity.book.Address;
import com.xiaxueqi.entity.book.OrderDetail;
import com.xiaxueqi.entity.book.ShopOrder;

/**
 * 订单处理设计
 * 
 * @author liangThink
 * 
 */
public class ShoppingOrderAction extends CRUDActionSupport<ShopOrder> {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BaseService baseService;

    private Address address;

    private List<OrderDetail> detailList;

    private ShopOrder order;

    private ShopCart shopCart = (ShopCart) Struts2Utils.getSession().getAttribute("shopCart");

    private Integer id;

    @Override
    public String delete() throws Exception {
        return null;
    }

    @Override
    public String list() throws Exception {
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {
        if (id != null) {
            order = baseService.get(ShopOrder.class, id);
        } else {
            order = new ShopOrder();
        }
    }

    @Override
    public String save() throws Exception {
        detailList = shopCart.getBuyList();
        if (order.getAddressId() == null) {
            address = new Address();
            address.setUserName(Struts2Utils.getParameter("userName"));
            address.setProvince(Struts2Utils.getParameter("province"));
            address.setCity(Struts2Utils.getParameter("city"));
            address.setArea(Struts2Utils.getParameter("area"));
            address.setTown(Struts2Utils.getParameter("town"));
            address.setLoaction(Struts2Utils.getParameter("location"));
            address.setZip(Struts2Utils.getParameter("zip"));
            address.setTel(Struts2Utils.getParameter("tel"));
            address.setPhone(Struts2Utils.getParameter("phone"));
            address.setQq(Struts2Utils.getParameter("qq"));
            baseService.insert(address);
            order.setAddressId(address.getId());
        }
        baseService.insert(order);
        id = order.getId();
        for (int i = 0; i < detailList.size(); i++) {
            OrderDetail detail = detailList.get(i);
            detail.setOrderId(id);
            baseService.insert(detail);
        }
        Struts2Utils.getSession().removeAttribute("shopCart");
        return SUCCESS;
    }

    @Override
    public String input() {
        return INPUT;
    }

    public ShopOrder getModel() {
        return this.order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public ShopCart getShopCart() {
        return shopCart;
    }

    public ShopOrder getOrder() {
        return order;
    }
}
