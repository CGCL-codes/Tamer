package com.ems.common.code.externalcode;

import java.util.Collections;
import java.util.List;
import com.ems.common.code.ISingleCodeCollector;
import conf.hibernate.CodeTableBO;

/**
 * @author Chiknin
 * 实例
 */
public class ProductCodeCollector implements ISingleCodeCollector {

    public static final String CODE_KEY = "Product";

    public String getCodeType() {
        return CODE_KEY;
    }

    public List<CodeTableBO> collect() {
        return Collections.EMPTY_LIST;
    }
}
