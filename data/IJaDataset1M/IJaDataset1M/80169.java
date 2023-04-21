package org.mandarax.compiler.defaultImpl;

import java.util.List;
import org.mandarax.compiler.CompilerException;

public class HeadResultTemplate extends Template {

    private String rtName;

    private List<String> params;

    public HeadResultTemplate(String rtName, List<String> params) {
        this.rtName = rtName;
        this.params = params;
    }

    @Override
    protected void initialize() throws CompilerException {
        setVar(VAR_RESULT_TYPE, rtName);
        setVar(VAR_PARAMETERS, params);
    }
}
