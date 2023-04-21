package com.faithbj.shop.model.configuration;

/**
 * Bean类 - 财付通配置
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.faithbj.com</p>
 * 
 * @author 	faithbj
 * @date 	2011-12-16
 * @version 1.0
 */
public class TenpayConfig {

    public static final String PAYMENT_URL = "http://service.tenpay.com/cgi-bin/v3.0/payservice.cgi";

    public static final String QUERY_URL = "http://mch.tenpay.com/cgi-bin/cfbi_query_order_v3.cgi";

    public static final String RETURN_URL = "/shop/payment!tenpayReturn.action";

    public static final String SHOW_URL = "/shop/payment!result.action";

    public static enum CurrencyType {

        CNY, USD, EUR, GBP, CAD, AUD, RUB, HKD, TWD, KRW, SGD, NZD, JPY, MYR, CHF, SEK, DKK, PLZ, NOK, HUF, CSK, MOP
    }

    ;

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
