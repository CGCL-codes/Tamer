package com.companyname.common.util.mvc.objectcast.imp;

import com.companyname.common.util.mvc.objectcast.AbstractCastor;
import java.util.List;

/**
 * 对象类型转换
 * for
 * created by amon
 */
public class Strings2Strings implements AbstractCastor {

    public Object casting(Object sro, List objs) throws ClassCastException {
        if (sro == null) {
            return new String[0];
        } else {
            return sro;
        }
    }
}
