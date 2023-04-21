package org.dueam.ui.web.spring.common;

import org.dueam.common.util.BeanUtils;
import org.dueam.service.Service;

/**
 * @author Anemone
 * lgh@onhonest.cn
 */
public abstract class BaseExtVerify implements ExtVerify {

    protected Service service;

    private String idField = "id";

    private String idsField = "ids";

    protected String[] getIds(Object command) {
        String idsTmp = (String) BeanUtils.getFieldValue(command, idsField);
        if (null == idsTmp) return new String[0];
        return idsTmp.split(",");
    }

    protected String getId(Object command) {
        return (String) BeanUtils.getFieldValue(command, idField);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getIdsField() {
        return idsField;
    }

    public void setIdsField(String idsField) {
        this.idsField = idsField;
    }

    public abstract String getErrorCode();

    public abstract String getErrorMsg();

    public abstract String getField();
}
