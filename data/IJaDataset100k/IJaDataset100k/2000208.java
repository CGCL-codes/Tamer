package com.nodeshop.bean;

import com.nodeshop.bean.SystemConfig.CurrencyType;

/**
 * Bean类 - 财付通配置
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop4AA72A7C04D99AF651FC12C9BE8A88BE
 
 */
public class TenpayConfig {

    public static final String PAYMENT_URL = "http://service.tenpay.com/cgi-bin/v3.0/payservice.cgi";

    public static final String QUERY_URL = "http://mch.tenpay.com/cgi-bin/cfbi_query_order_v3.cgi";

    public static final String RETURN_URL = "/shop/payment!tenpayReturn.action";

    public static final String SHOW_URL = "/shop/payment!result.action";

    public enum TenpayType {

        direct, partnerMaterial, partnerVirtual
    }

    public static final CurrencyType[] currencyType = { CurrencyType.CNY };

    private TenpayType tenpayType;

    private String bargainorId;

    private String key;

    public TenpayType getTenpayType() {
        return tenpayType;
    }

    public void setTenpayType(TenpayType tenpayType) {
        this.tenpayType = tenpayType;
    }

    public String getBargainorId() {
        return bargainorId;
    }

    public void setBargainorId(String bargainorId) {
        this.bargainorId = bargainorId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
