package com.microfly.formula;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * SinFunction ������
 *     Sin(number)
 *
 * a new publishing system
 * Copyright: Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 *
*/
public class SinFunction implements Function {

    public Object Evaluate(List args) throws Exception {
        if (args == null || args.size() == 0) throw new Exception("arguments error for SIN function");
        if (args.size() == 1) {
            Object obj = args.get(0);
            if (obj instanceof BigDecimal) {
                return new BigDecimal(Math.sin(((BigDecimal) obj).doubleValue()));
            } else if (obj instanceof Integer) {
                return new BigDecimal(Math.sin(((Integer) obj).doubleValue()));
            } else if (obj instanceof Float) {
                return new BigDecimal(Math.sin(((Float) obj).doubleValue()));
            } else if (obj instanceof Double) {
                return new BigDecimal(Math.sin(((Double) obj).doubleValue()));
            } else return obj;
        }
        List ret = new ArrayList();
        for (Object obj : args) {
            if (obj instanceof BigDecimal) {
                ret.add(new BigDecimal(Math.sin(((BigDecimal) obj).doubleValue())));
            } else if (obj instanceof Integer) {
                ret.add(new BigDecimal(Math.sin(((Integer) obj).doubleValue())));
            } else if (obj instanceof Float) {
                ret.add(new BigDecimal(Math.sin(((Float) obj).doubleValue())));
            } else if (obj instanceof Double) {
                ret.add(new BigDecimal(Math.sin(((Double) obj).doubleValue())));
            } else {
                ret.add(obj);
            }
        }
        return ret;
    }
}
