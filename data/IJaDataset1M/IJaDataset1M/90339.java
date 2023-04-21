package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Enumer;
import org.eweb4j.util.StringUtil;

public class EnumImpl implements ValidatorCreator {

    private Enumer ann;

    public EnumImpl(Enumer ann) {
        this.ann = ann;
    }

    public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
        if (this.ann == null) return null;
        if (val == null || !Validators.ENUM.equals(val.getName())) {
            val = new ValidatorConfigBean();
            val.setName(Validators.ENUM);
        }
        FieldConfigBean fcb = new FieldConfigBean();
        fcb.setName(fieldName);
        fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
        ParamConfigBean pcb = new ParamConfigBean();
        pcb.setName(Validators.ENUM_WORD_PARAM);
        StringBuilder sb = new StringBuilder();
        for (String s : ann.words()) {
            if (sb.length() > 0) sb.append("#");
            sb.append(StringUtil.parsePropValue(s));
        }
        pcb.setValue(sb.toString());
        fcb.getParam().add(pcb);
        val.getField().add(fcb);
        return val;
    }
}
