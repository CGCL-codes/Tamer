package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;
import com.healthmarketscience.sqlbuilder.dbspec.Function;

/**
 * Outputs the "simple" name of a function.
 *
 * @author James Ahlborn
 */
class FunctionObject extends SqlObject {

    protected Function _function;

    FunctionObject(Function function) {
        _function = function;
    }

    @Override
    protected void collectSchemaObjects(ValidationContext vContext) {
    }

    @Override
    public void appendTo(AppendableExt app) throws IOException {
        app.append(_function.getFunctionNameSQL());
    }
}
