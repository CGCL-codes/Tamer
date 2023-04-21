package com.sunshulin.tag;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import com.bhms.module.resources.village.mapper.CycleFace;
import com.bhms.module.resources.village.service.VillageService;

/**
 * 环线朝向标签
 * 
 * @author 孙树林
 * 
 */
public class CycleFaceTag extends GeneralTag {

    private static final long serialVersionUID = 3112874229965672738L;

    /** 名称 */
    private String name;

    /** 事件 */
    private String onchange;

    private String id;

    /** 引用标签 */
    private String refId;

    /** 控件ID */
    private String formId;

    @Override
    public int doStartTag() throws JspException {
        VillageService service = (VillageService) getBean("villageService");
        List<CycleFace> list = service.searchCycleFace();
        try {
            JspWriter writer = pageContext.getOut();
            writer.write(createSelectTag(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String createSelectTag(List<CycleFace> list) {
        StringBuilder sb = new StringBuilder("<select name='" + name + "'");
        if (formId != null) {
            sb.append(" id='" + formId + "'");
        }
        if (onchange != null) {
            sb.append(" onchange='" + onchange + "'>");
        } else {
            sb.append(">");
        }
        sb.append("<option value=''>请选择..</option>");
        String value = findValue(name) != null ? findValue(name).toString() : null;
        for (CycleFace m : list) {
            if (value != null && value.equals(m.getFace_name())) {
                sb.append("<option key='" + m.getId() + "' value='" + m.getFace_name() + "' selected='selected'>" + m.getFace_name() + "</option>");
            } else {
                sb.append("<option key='" + m.getId() + "' value='" + m.getFace_name() + "'>" + m.getFace_name() + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
