package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;

/**
 * 验证器帮助类
 * 
 * @author cfuture.aw
 * 
 */
public class ValidatorHelper implements ValidatorIF {

    private String regex;

    public ValidatorHelper(String regex) {
        this.regex = regex;
    }

    public Validation validate(ValidatorConfigBean val, Map<String, String[]> map, HttpServletRequest request) {
        Map<String, String> valError = new HashMap<String, String>();
        for (FieldConfigBean f : val.getField()) {
            String key = f.getName();
            String[] value = map.get(key);
            if (value == null || value.length == 0) continue;
            for (String v : value) {
                if (!v.matches(this.regex)) {
                    String mess = f.getMessage();
                    if (mess.length() == 0) mess = " %s-validator : your input { %s = %s } must matches [ %s ]";
                    valError.put(key, String.format(mess, val.getName(), key, v, regex));
                    break;
                }
            }
            request.setAttribute(key, value);
        }
        Validation validation = new Validation();
        if (!valError.isEmpty()) validation.getErrors().put(val.getName(), valError);
        return validation;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
