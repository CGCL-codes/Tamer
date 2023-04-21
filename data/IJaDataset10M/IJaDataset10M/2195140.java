package com.ems.common.code;

import java.util.List;
import conf.hibernate.CodeTableBO;

/**
 * @author Chiknin
 */
public interface ICodeCollector {

    public List<CodeTableBO> collect();
}
