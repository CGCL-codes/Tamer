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
public class ChannelpropForm extends ActionForm {

    private String channelname;

    private String channelpath;

    private String channeltemplate;

    private String description;

    private String doctemplate;

    private String num;

    private String style;

    private String usestatus;

    private String refreshFlag;

    private String pageNum;

    private String template_id = "";

    private String[] field;

    private String[] field_name;

    private String[] order_no;

    private String extendparent = "";

    /**
     * @return Returns the template_id.
     */
    public String getTemplate_id() {
        return template_id;
    }

    /**
     * @param template_id The template_id to set.
     */
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
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

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public String getChannelpath() {
        return channelpath;
    }

    public void setChannelpath(String channelpath) {
        this.channelpath = channelpath;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUsestatus() {
        return usestatus;
    }

    public void setUsestatus(String usestatus) {
        this.usestatus = usestatus;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        return null;
    }

    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
    }

    public String getRefreshFlag() {
        return refreshFlag;
    }

    public void setRefreshFlag(String refreshFlag) {
        this.refreshFlag = refreshFlag;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
