package com.zhiyun.estore.website.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.zhiyun.estore.common.Page;
import com.zhiyun.estore.common.action.BaseActionSupport;
import com.zhiyun.estore.common.utils.NamingConstants;
import com.zhiyun.estore.common.utils.URLConstants;
import com.zhiyun.estore.common.vo.EbOrder;
import com.zhiyun.estore.website.service.OrderService;

public class OrderAction extends BaseActionSupport {

    private static final long serialVersionUID = 6414598676348728535L;

    private OrderService orderService;

    private Page<EbOrder> pageInfo;

    private String id;

    private Date startDate;

    private Date endDate;

    private String orderType;

    private String baseUrl;

    private String msg;

    public Page<EbOrder> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Page<EbOrder> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String searchOrder() {
        msg = "";
        baseUrl = URLConstants.get(NamingConstants.BASE_URL);
        Map<String, Object> params = new HashMap<String, Object>();
        if (id != null && !id.equals("")) {
            params.put("id", id);
        }
        if (startDate != null && endDate != null) {
            params.put("createTimeStart", startDate);
            params.put("createTimeEnd", endDate);
        }
        if (params.size() == 0) {
            return SUCCESS;
        }
        params.put("type", orderType);
        params.put("status", new Integer(0));
        int limit = Integer.parseInt(URLConstants.get(NamingConstants.SYS_PAGESIZE));
        if (pageInfo == null || pageInfo.getPageNo() < 1) {
            pageInfo = new Page<EbOrder>();
            pageInfo.setPageNo(1);
        }
        int start = (pageInfo.getPageNo() - 1) * limit;
        try {
            pageInfo = orderService.pagedQuery(params, start, limit);
            if (pageInfo.getData() != null && pageInfo.getData().size() > 0) {
                pageInfo.setPageSize(limit);
                pageInfo.setPageCount(pageInfo.getData().size() / limit + 1);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据库异常, 请重新尝试";
            pageInfo = null;
            return SUCCESS;
        }
    }

    public String orderList() {
        msg = "";
        if (!userLogined()) {
            return LOGIN;
        }
        baseUrl = URLConstants.get(NamingConstants.BASE_URL);
        int limit = Integer.parseInt(URLConstants.get(NamingConstants.SYS_PAGESIZE));
        if (pageInfo == null || pageInfo.getPageNo() < 1) {
            pageInfo = new Page<EbOrder>();
            pageInfo.setPageNo(1);
        }
        int start = (pageInfo.getPageNo() - 1) * limit;
        try {
            pageInfo = orderService.pagedQuery("ebUser", getSession().getAttribute("user"), start, limit);
            if (pageInfo.getData() != null && pageInfo.getData().size() > 0) {
                pageInfo.setPageSize(limit);
                pageInfo.setPageCount(pageInfo.getData().size() / limit + 1);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据库异常, 请重新尝试";
            pageInfo = null;
            return SUCCESS;
        }
    }
}
