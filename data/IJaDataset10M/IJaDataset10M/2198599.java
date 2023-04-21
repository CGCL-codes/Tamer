package com.xdh.export.convert.dt.orcl;

import com.xdh.export.convert.IDTConverter;

public class OrclDateConvert implements IDTConverter {

    public String cvtToStr(Object data) {
        if (data == null) return "null";
        return "to_date('" + data.toString() + "','yyyy-mm-dd')";
    }
}
