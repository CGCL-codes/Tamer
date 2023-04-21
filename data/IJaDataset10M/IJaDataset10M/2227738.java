package org.webhop.ywdc.admin;

import java.util.ArrayList;
import java.util.List;
import org.webhop.ywdc.beans.OrderPayment;
import org.webhop.ywdc.beans.OrderStatus;
import org.webhop.ywdc.extras.Status;
import org.webhop.ywdc.guice.GuiceModule;
import org.webhop.ywdc.services.AuthenticationServices;
import org.webhop.ywdc.util.HibernateConnection;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.opensymphony.xwork2.ActionSupport;

public class JsonRefundedOrdersAction extends ActionSupport {

    private static final long serialVersionUID = 2675265508666801225L;

    public List<OrderStatus> orderstatusList;

    public List<OrderPayment> gridModel;

    public Integer categoryId;

    public Integer rows = 0;

    public Integer page = 0;

    public Integer total;

    public Integer records = 0;

    public String UserTempToken;

    public String execute() {
        Injector injector = Guice.createInjector(new GuiceModule());
        HibernateConnection connection = injector.getInstance((HibernateConnection.class));
        AuthenticationServices service = injector.getInstance((AuthenticationServices.class));
        service.setConnection(connection);
        service.setInjector(injector);
        records = service.countTotalRefundedOrders();
        Integer firstResult = ((page - 1) * rows) + 1;
        total = (int) Math.ceil((double) records / (double) rows);
        gridModel = new ArrayList<OrderPayment>();
        orderstatusList = service.getRefundedOrders(firstResult, rows);
        for (OrderStatus oStatus : orderstatusList) {
            OrderPayment currentOrderPayment = service.getOrderPaymentByOrderId(oStatus.getOrderid());
            gridModel.add(currentOrderPayment);
        }
        return SUCCESS;
    }

    public List<OrderStatus> getOrderstatusList() {
        return orderstatusList;
    }

    public void setOrderstatusList(List<OrderStatus> orderstatusList) {
        this.orderstatusList = orderstatusList;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public List<OrderPayment> getGridModel() {
        return gridModel;
    }

    public void setGridModel(List<OrderPayment> gridModel) {
        this.gridModel = gridModel;
    }

    public String getUserTempToken() {
        return UserTempToken;
    }

    public void setUserTempToken(String userTempToken) {
        UserTempToken = userTempToken;
    }
}
