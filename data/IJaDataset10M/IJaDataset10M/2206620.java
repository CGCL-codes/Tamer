package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Phone;
import org.eweb4j.util.StringUtil;

public class PhoneImpl implements ValidatorCreator {

    private Phone ann;

    public PhoneImpl(Phone ann) {
        this.ann = ann;
    }

    public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
        if (this.ann == null) return null;
        if (val == null || !Validators.PHONE.equals(val.getName())) {
            val = new ValidatorConfigBean();
            val.setName(Validators.PHONE);
        }
        FieldConfigBean fcb = new FieldConfigBean();
        fcb.setName(fieldName);
        fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
        val.getField().add(fcb);
        return val;
    }
}
