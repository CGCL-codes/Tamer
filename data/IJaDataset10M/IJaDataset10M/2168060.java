package com.techstar.griddemo.web.form;

import java.util.Hashtable;
import java.util.Map;
import com.techstar.framework.service.dto.DictionaryBaseDto;
import com.techstar.framework.web.form.BaseForm;

/**
 * 业务字典统一处理action form类
 * @author 
 * @date
 */
public class DictionaryBaseForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    public DictionaryBaseForm() {
    }

    public void setDtoInstance(Object dtoobj) {
    }

    private Map instanceMap = new Hashtable();

    public DictionaryBaseDto getDtoInstance(String poName) {
        if (instanceMap.containsKey(poName)) {
            return (DictionaryBaseDto) instanceMap.get(poName);
        } else {
            return null;
        }
    }
}
