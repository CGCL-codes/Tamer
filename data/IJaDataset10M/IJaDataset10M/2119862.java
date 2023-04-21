package com.kongur.network.erp.domain.uc;

import com.alibaba.cobar.client.support.domain.BaseDataObject;

/**
 * @author gaojf
 * @version $Id: Report.java,v 0.1 2012-1-10 ����01:26:01 gaojf Exp $
 */
public class Report extends BaseDataObject {

    private static final long serialVersionUID = -9071491836865593848L;

    private String code;

    private int num;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
