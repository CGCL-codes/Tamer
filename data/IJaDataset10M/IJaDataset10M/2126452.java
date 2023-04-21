package com.tll.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * AppProperty
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = { "name" }))
public class AppProperty extends NamedEntity {

    private static final long serialVersionUID = 601145261743504878L;

    public static final int MAXLEN_NAME = 128;

    public static final int MAXLEN_VALUE = 255;

    private String value;

    @Override
    public Class<? extends IEntity> entityClass() {
        return AppProperty.class;
    }

    @Override
    @NotEmpty
    @Length(max = MAXLEN_NAME)
    public String getName() {
        return name;
    }

    @NotEmpty
    @Length(max = MAXLEN_VALUE)
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
