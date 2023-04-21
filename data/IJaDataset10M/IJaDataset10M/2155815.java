package ces.platform.infoplat.ui.website.form;

import org.apache.struts.action.*;
import javax.servlet.http.*;

/**
 * <p>Title: ������Ϣƽ̨</p>
 * <p>Description: ���doctypePath��Ĭ��ֵ��</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: �Ϻ�������Ϣ��չ���޹�˾</p>
 * @author ����
 * @version 2.5
 */
public class SitepropForm extends ActionForm {

    private String appserver;

    private String channeltemplate;

    private String description;

    private String doctemplate;

    private String increment;

    private String maxruntime;

    private String password;

    private String path;

    private String period1;

    private String period2;

    private String port;

    private String remarknumber;

    private String saveData;

    private String server;

    private String sitename;

    private String sitepath;

    private String isstartup;

    private String starttime1;

    private String starttime2;

    private String status;

    private String user;

    private int usestatus;

    private String[] field;

    private String[] field_name;

    private String[] order_no;

    private String extendparent = "";

    /**
     * @return Returns the extendparent.
     */
    public String getExtendparent() {
        return extendparent;
    }

    /**
     * @param extendparent The extendparent to set.
     */
    public void setExtendparent(String extendparent) {
        this.extendparent = extendparent;
    }

    /**
     * @return Returns the field.
     */
    public String[] getField() {
        return field;
    }

    /**
     * @param field The field to set.
     */
    public void setField(String[] field) {
        this.field = field;
    }

    /**
     * @return Returns the field_name.
     */
    public String[] getField_name() {
        return field_name;
    }

    /**
     * @param field_name The field_name to set.
     */
    public void setField_name(String[] field_name) {
        this.field_name = field_name;
    }

    /**
     * @return Returns the order_no.
     */
    public String[] getOrder_no() {
        return order_no;
    }

    /**
     * @param order_no The order_no to set.
     */
    public void setOrder_no(String[] order_no) {
        this.order_no = order_no;
    }

    public String getAppserver() {
        return appserver;
    }

    public void setAppserver(String appserver) {
        this.appserver = appserver;
    }

    public String getChanneltemplate() {
        return channeltemplate;
    }

    public void setChanneltemplate(String channeltemplate) {
        this.channeltemplate = channeltemplate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctemplate() {
        return doctemplate;
    }

    public void setDoctemplate(String doctemplate) {
        this.doctemplate = doctemplate;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getMaxruntime() {
        return maxruntime;
    }

    public void setMaxruntime(String maxruntime) {
        this.maxruntime = maxruntime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPeriod1() {
        return period1;
    }

    public void setPeriod1(String period1) {
        this.period1 = period1;
    }

    public String getPeriod2() {
        return period2;
    }

    public void setPeriod2(String period2) {
        this.period2 = period2;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRemarknumber() {
        return remarknumber;
    }

    public void setRemarknumber(String remarknumber) {
        this.remarknumber = remarknumber;
    }

    public String getSaveData() {
        return saveData;
    }

    public void setSaveData(String saveData) {
        this.saveData = saveData;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getSitepath() {
        return sitepath;
    }

    public void setSitepath(String sitepath) {
        this.sitepath = sitepath;
    }

    public String getIsstartup() {
        return isstartup;
    }

    public void setIsstartup(String isstartup) {
        this.isstartup = isstartup;
    }

    public String getStarttime1() {
        return starttime1;
    }

    public void setStarttime1(String starttime1) {
        this.starttime1 = starttime1;
    }

    public String getStarttime2() {
        return starttime2;
    }

    public void setStarttime2(String starttime2) {
        this.starttime2 = starttime2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIntUsestatus() {
        if (usestatus == 0) {
            return 1;
        } else return usestatus;
    }

    public int getUsestatus() {
        return usestatus;
    }

    public void setUsestatus(int usestatus) {
        this.usestatus = usestatus;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        return null;
    }

    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
    }
}
