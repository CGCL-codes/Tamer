package com.microfly.formula;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * IntFunction
 *   ����ֵ����ȡ��Ϊ��ӽ������
 *
 * a new publishing system
 * Copyright: Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 *
*/
public class IntFunction implements Function {

    public Object Evaluate(List args) throws Exception {
        if (args == null) throw new Exception("arguments error for INT function");
        if (args.size() == 1) {
            Object obj = args.get(0);
            if (obj instanceof BigDecimal) {
                return new Integer(((BigDecimal) obj).intValue());
            } else if (obj instanceof Float) {
                return new Integer(((Float) obj).intValue());
            } else if (obj instanceof Double) {
                return new Integer(((Double) obj).intValue());
            }
            return obj;
        }
        List ret = new ArrayList();
        for (Object obj : args) {
            if (obj instanceof BigDecimal) {
                ret.add(new Integer(((BigDecimal) obj).intValue()));
            } else if (obj instanceof Float) {
                ret.add(new Integer(((Float) obj).intValue()));
            } else if (obj instanceof Double) {
                ret.add(new Integer(((Double) obj).intValue()));
            } else {
                ret.add(obj);
            }
        }
        return ret;
    }
}
