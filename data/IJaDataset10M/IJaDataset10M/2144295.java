package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;

/**
 * Outputs a quoted value <code>"'&lt;value&gt;'"</code>.
 *
 * @author James Ahlborn
 */
public class ValueObject extends Expression {

    private Object _value;

    public ValueObject(Object value) {
        _value = value;
    }

    @Override
    public boolean hasParens() {
        return false;
    }

    @Override
    protected void collectSchemaObjects(ValidationContext vContext) {
    }

    @Override
    public void appendTo(AppendableExt app) throws IOException {
        app.append("'").append(_value).append("'");
    }
}
