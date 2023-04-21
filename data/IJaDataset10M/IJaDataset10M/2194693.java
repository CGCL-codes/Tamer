package com.company.erp.order.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.common.Constant;
import com.company.common.Hibernate.action.BaseAction;
import com.company.common.util.DateUtil;
import com.company.common.util.StringUtil;
import com.company.erp.order.dao.TBOrderDetailsDAO;
import com.company.erp.order.dao.TBOrderHeaderDAO;
import com.company.erp.order.dao.VwOrderDAO;
import com.company.erp.order.dao.VwOrderDetailsDAO;
import com.company.erp.order.dao.VwOrderDetailsSupplierDAO;
import com.company.erp.order.model.TBOrderDetails;
import com.company.erp.order.model.TBOrderHeader;
import com.company.erp.order.model.VwOrderCustomer;
import com.company.erp.order.model.VwOrderDetailsSupplier;
import com.company.erp.order.model.VwOrderHeader;
import com.company.erp.order.model.VwOrderSupplier;

public class ReportManager extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(ReportManager.class);

    private TBOrderHeader obj;

    private TBOrderHeaderDAO dao;

    private TBOrderDetails objDetails;

    private List<TBOrderDetails> detailsList;

    private TBOrderDetailsDAO detailsDAO;

    private VwOrderDAO vwOrderDAO;

    private VwOrderCustomer vwOrderCustomer;

    private VwOrderSupplier vwOrderSupplier;

    private VwOrderDetailsDAO vwOrderDetailsDAO;

    private VwOrderDetailsSupplierDAO vwOrderDetailsSupplierDAO;

    private List<VwOrderDetailsSupplier> vwOrderDetailsLists;

    public String customerListPage() {
        getRight();
        String value = getRightMap().get(Constant.ORDER_SEARCH_ALL);
        if (Constant.RIGHT_NO.equals(value)) {
            return "noPermission";
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            if (vwOrderCustomer != null) {
                if (vwOrderCustomer.getPartnerName() != null && !vwOrderCustomer.getPartnerName().equals("")) map.put("partnerName", vwOrderCustomer.getPartnerName());
            } else {
                vwOrderCustomer = new VwOrderCustomer();
                vwOrderCustomer.setOrderMonth(DateUtil.getThisYM());
            }
            String orderMonth = "";
            if (vwOrderCustomer.getOrderMonth() != null && !vwOrderCustomer.getOrderMonth().equals("")) orderMonth = vwOrderCustomer.getOrderMonth().replaceAll("-", "");
            map.put("orderMonth", orderMonth);
            pageInfo = dao.queryForPage(VwOrderCustomer.class, map, getPageInfo(), Constant.SUMMARYFLAG);
            VwOrderCustomer summary = new VwOrderCustomer();
            summary.setFundsSum(0.00);
            summary.setNum(0);
            for (int i = 0; i < pageInfo.getAllList().size(); i++) {
                VwOrderCustomer temp = (VwOrderCustomer) pageInfo.getAllList().get(i);
                summary.setFundsSum(summary.getFundsSum() + temp.getFundsSum());
                summary.setNum(summary.getNum() + temp.getNum());
            }
            pageInfo.setSummary(summary);
            pageInfo.setAllList(null);
            return "customerList";
        }
    }

    public String customerOrderList() {
        getRight();
        String value = getRightMap().get(Constant.ORDER_SEARCH_P);
        if (Constant.RIGHT_NO.equals(value)) {
            return "noPermission";
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            if (obj != null) {
                if (obj.getName() != null && !obj.getName().equals("")) map.put("name", obj.getName());
                if (obj.getType() != null && !obj.getType().equals("")) map.put("type", obj.getType());
                if (obj.getCustomerName() != null && !obj.getCustomerName().equals("")) {
                    String customerName = getServletRequest().getParameter("obj.customerName");
                    try {
                        customerName = java.net.URLDecoder.decode(customerName, "utf-8");
                        obj.setCustomerName(customerName);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    map.put("customerName", obj.getCustomerName());
                }
                if (obj.getCreateDate() != null && !obj.getCreateDate().equals("")) {
                    String createDate = obj.getCreateDate().replaceAll("-", "").substring(0, 6);
                    obj.setCreateDate(createDate);
                    map.put("createDate", createDate);
                }
            }
            pageInfo = dao.queryForPage(VwOrderHeader.class, map, getPageInfo(), Constant.SUMMARYFLAG);
            for (int i = 0; i < pageInfo.getDataList().size(); i++) {
                VwOrderHeader vwOrderHeader = (VwOrderHeader) pageInfo.getDataList().get(i);
                vwOrderHeader.setCostTotal(StringUtil.doubleFormat(vwOrderHeader.getCostTotal()));
                vwOrderHeader.setSellTotal(StringUtil.doubleFormat(vwOrderHeader.getSellTotal()));
            }
            VwOrderHeader summary = new VwOrderHeader();
            summary.setSellTotal(0.00);
            for (int i = 0; i < pageInfo.getAllList().size(); i++) {
                VwOrderHeader temp = (VwOrderHeader) pageInfo.getAllList().get(i);
                summary.setSellTotal(summary.getSellTotal() + temp.getSellTotal());
            }
            pageInfo.setSummary(summary);
            pageInfo.setAllList(null);
            return "customerReport";
        }
    }

    public String orderView() {
        getRight();
        obj = dao.findById(obj.getId());
        obj.setCreateDate(DateUtil.shortStrToStand(obj.getCreateDate()).substring(0, 10));
        detailsList = detailsDAO.findByProperty("headerId", obj.getId());
        return "orderView";
    }

    public String supplierListPage() {
        getRight();
        String value = getRightMap().get(Constant.ORDER_SEARCH_ALL);
        if (Constant.RIGHT_NO.equals(value)) {
            return "noPermission";
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            if (vwOrderSupplier != null) {
                if (vwOrderSupplier.getPartnerName() != null && !vwOrderSupplier.getPartnerName().equals("")) map.put("partnerName", vwOrderSupplier.getPartnerName());
            } else {
                vwOrderSupplier = new VwOrderSupplier();
                vwOrderSupplier.setOrderMonth(DateUtil.getThisYM());
            }
            String orderMonth = "";
            if (vwOrderSupplier.getOrderMonth() != null && !vwOrderSupplier.getOrderMonth().equals("")) orderMonth = vwOrderSupplier.getOrderMonth().replaceAll("-", "");
            map.put("orderMonth", orderMonth);
            pageInfo = dao.queryForPage(VwOrderSupplier.class, map, getPageInfo(), Constant.SUMMARYFLAG);
            VwOrderSupplier summary = new VwOrderSupplier();
            summary.setFundsSum(0.00);
            summary.setNum(0);
            for (int i = 0; i < pageInfo.getAllList().size(); i++) {
                VwOrderSupplier temp = (VwOrderSupplier) pageInfo.getAllList().get(i);
                if (temp != null) {
                    summary.setFundsSum(summary.getFundsSum() + temp.getFundsSum());
                    summary.setNum(summary.getNum() + temp.getNum());
                }
            }
            pageInfo.setSummary(summary);
            pageInfo.setAllList(null);
            return "supplierList";
        }
    }

    public String viewPage() {
        getRight();
        String supplierName = getServletRequest().getParameter("objDetails.supplierName");
        try {
            supplierName = java.net.URLDecoder.decode(supplierName, "utf-8");
            objDetails.setSupplierName(supplierName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        vwOrderDetailsLists = vwOrderDetailsSupplierDAO.findBySupplierNameAndMonth(supplierName, objDetails.getCreateDate());
        for (int i = 0; i < vwOrderDetailsLists.size(); i++) {
            VwOrderDetailsSupplier vwOrderDetails = (VwOrderDetailsSupplier) vwOrderDetailsLists.get(i);
            vwOrderDetails.setCreateDate(DateUtil.shortStrToStand(vwOrderDetails.getCreateDate()));
        }
        return "supplierReport";
    }

    public TBOrderHeaderDAO getDao() {
        return dao;
    }

    public void setDao(TBOrderHeaderDAO dao) {
        this.dao = dao;
    }

    public TBOrderDetails getObjDetails() {
        return objDetails;
    }

    public void setObjDetails(TBOrderDetails objDetails) {
        this.objDetails = objDetails;
    }

    public TBOrderDetailsDAO getDetailsDAO() {
        return detailsDAO;
    }

    public void setDetailsDAO(TBOrderDetailsDAO detailsDAO) {
        this.detailsDAO = detailsDAO;
    }

    public List<TBOrderDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<TBOrderDetails> detailsList) {
        this.detailsList = detailsList;
    }

    public VwOrderDAO getVwOrderDAO() {
        return vwOrderDAO;
    }

    public void setVwOrderDAO(VwOrderDAO vwOrderDAO) {
        this.vwOrderDAO = vwOrderDAO;
    }

    public TBOrderHeader getObj() {
        return obj;
    }

    public void setObj(TBOrderHeader obj) {
        this.obj = obj;
    }

    public VwOrderCustomer getVwOrderCustomer() {
        return vwOrderCustomer;
    }

    public void setVwOrderCustomer(VwOrderCustomer vwOrderCustomer) {
        this.vwOrderCustomer = vwOrderCustomer;
    }

    public VwOrderSupplier getVwOrderSupplier() {
        return vwOrderSupplier;
    }

    public void setVwOrderSupplier(VwOrderSupplier vwOrderSupplier) {
        this.vwOrderSupplier = vwOrderSupplier;
    }

    public VwOrderDetailsDAO getVwOrderDetailsDAO() {
        return vwOrderDetailsDAO;
    }

    public void setVwOrderDetailsDAO(VwOrderDetailsDAO vwOrderDetailsDAO) {
        this.vwOrderDetailsDAO = vwOrderDetailsDAO;
    }

    public List<VwOrderDetailsSupplier> getVwOrderDetailsLists() {
        return vwOrderDetailsLists;
    }

    public void setVwOrderDetailsLists(List<VwOrderDetailsSupplier> vwOrderDetailsLists) {
        this.vwOrderDetailsLists = vwOrderDetailsLists;
    }

    public VwOrderDetailsSupplierDAO getVwOrderDetailsSupplierDAO() {
        return vwOrderDetailsSupplierDAO;
    }

    public void setVwOrderDetailsSupplierDAO(VwOrderDetailsSupplierDAO vwOrderDetailsSupplierDAO) {
        this.vwOrderDetailsSupplierDAO = vwOrderDetailsSupplierDAO;
    }
}
