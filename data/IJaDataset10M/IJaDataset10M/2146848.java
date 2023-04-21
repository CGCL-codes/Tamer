package com.pioneer.app.sms.mobile;

import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.dom4j.Document;
import org.hibernate.Transaction;
import com.app.util.Dom4jUtil;
import com.opensymphony.xwork2.Action;

public class UpSmsAction implements Action, ServletRequestAware, ServletResponseAware, Serializable {

    private java.lang.Integer id;

    private java.lang.Integer[] ids;

    private UpSms upsms;

    private java.lang.String tel;

    private java.lang.String txt;

    private java.util.Date uptime;

    private java.lang.Integer status;

    private java.lang.String destAddr;

    private java.lang.String serviceCode;

    private java.lang.String exNumber;

    private java.lang.String serviceName;

    private List objects = null;

    private String condition = null;

    private String rtPage = null;

    private String message = null;

    private HttpServletResponse response;

    private HttpServletRequest request;

    /** 构造函数*/
    public UpSmsAction() {
    }

    /** full constructor */
    public UpSmsAction(java.lang.String tel, java.lang.String txt, java.util.Date uptime, java.lang.Integer status, java.lang.String destAddr, java.lang.String serviceCode, java.lang.String exNumber, java.lang.String serviceNam) {
        this.tel = tel;
        this.txt = txt;
        this.uptime = uptime;
        this.status = status;
        this.destAddr = destAddr;
        this.serviceCode = serviceCode;
        this.exNumber = exNumber;
        this.serviceName = serviceName;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setIds(java.lang.Integer[] ids) {
        this.ids = ids;
    }

    public java.lang.Integer[] getIds() {
        return ids;
    }

    public void setUpSms(UpSms upsms) {
        this.upsms = upsms;
    }

    public UpSms getUpSms() {
        return upsms;
    }

    public void setTel(java.lang.String tel) {
        this.tel = tel;
    }

    public java.lang.String getTel() {
        return tel;
    }

    public void setTxt(java.lang.String txt) {
        this.txt = txt;
    }

    public java.lang.String getTxt() {
        return txt;
    }

    public void setUptime(java.util.Date uptime) {
        this.uptime = uptime;
    }

    public java.util.Date getUptime() {
        return uptime;
    }

    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getStatus() {
        return status;
    }

    public void setDestAddr(java.lang.String destAddr) {
        this.destAddr = destAddr;
    }

    public java.lang.String getDestAddr() {
        return destAddr;
    }

    public void setServiceCode(java.lang.String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public java.lang.String getServiceCode() {
        return serviceCode;
    }

    public void setExNumber(java.lang.String exNumber) {
        this.exNumber = exNumber;
    }

    public java.lang.String getExNumber() {
        return exNumber;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    /**对象列表的get set 方法*/
    public List getObjects() {
        return objects;
    }

    public void setObjects(List objects) {
        this.objects = objects;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRtPage() {
        return rtPage;
    }

    public void setRtPage(String rtPage) {
        this.rtPage = rtPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String execute() throws Exception {
        return "SUCESS";
    }

    /** 根据条件查找出对象list*/
    public String doList() {
        UpSmsDAO dao = new UpSmsDAO();
        this.objects = dao.findByCondition(condition);
        return "list";
    }

    public String doAdd() {
        return "add";
    }

    /** 显示编辑界面*/
    public String doEdit() {
        try {
            UpSmsDAO dao = new UpSmsDAO();
            this.upsms = dao.findById(id);
            this.sedObj();
            ;
        } catch (Exception e) {
            this.message = "提取数据出错！";
        }
        return "edit";
    }

    /** 保存信息*/
    public String doSave() {
        Transaction tx = null;
        try {
            this.buildObj();
            UpSmsDAO dao = new UpSmsDAO();
            tx = dao.getSession().beginTransaction();
            if (null == this.upsms.getId()) {
                dao.add(upsms);
            } else {
                dao.getSession().clear();
                dao.update(upsms);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            this.message = "保存数据出错！";
        }
        if (null == rtPage) {
            rtPage = "list";
        }
        return rtPage;
    }

    /** 显示详细信息*/
    public String doDetail() {
        UpSmsDAO dao = new UpSmsDAO();
        this.upsms = dao.findById(id);
        this.sedObj();
        if (null == rtPage) {
            rtPage = "list";
        }
        return rtPage;
    }

    /** 删除信息*/
    public String doDelete() {
        try {
            UpSmsDAO dao = new UpSmsDAO();
            dao.deleteObjs(ids);
        } catch (Exception e) {
            this.message = "删除数据出错！";
        }
        return "list";
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /** 显示选择窗体中的检索结构*/
    public String doSelWin() {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            UpSmsDAO dao = new UpSmsDAO();
            condition = "txt like'%" + this.txt + "%'";
            Document doc = dao.findSelWinDoc(condition);
            this.response.setContentType("text/xml");
            Dom4jUtil.writeDocToOut(doc, "utf-8", this.response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void buildObj() {
        this.upsms = new UpSms();
        this.upsms.setId(this.id);
        this.upsms.setTel(this.tel);
        this.upsms.setTxt(this.txt);
        this.upsms.setUptime(this.uptime);
        this.upsms.setStatus(this.status);
        this.upsms.setDestAddr(this.destAddr);
        this.upsms.setServiceCode(this.serviceCode);
        this.upsms.setExNumber(this.exNumber);
        this.upsms.setServiceName(this.serviceName);
    }

    private void sedObj() {
        if (null != this.upsms) {
            this.id = this.upsms.getId();
            this.tel = this.upsms.getTel();
            this.txt = this.upsms.getTxt();
            this.uptime = this.upsms.getUptime();
            this.status = this.upsms.getStatus();
            this.destAddr = this.upsms.getDestAddr();
            this.serviceCode = this.upsms.getServiceCode();
            this.exNumber = this.upsms.getExNumber();
            this.serviceName = this.upsms.getServiceName();
        }
    }
}
