package com.uk.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.uk.data.ejbs.IFaturaBean;
import com.uk.data.entities.Parameters;

public class Utils {

    private static final Map<String, String> PARAMS = new HashMap<String, String>();

    public Utils(IFaturaBean faturaBean) {
        List<Parameters> parameters = faturaBean.getParameters();
        for (Parameters parameter : parameters) {
            PARAMS.put(parameter.getParam().name(), parameter.getValue());
        }
    }
}
