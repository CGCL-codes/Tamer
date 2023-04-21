package cn.ekuma.epos.db.table.crm;

import cn.ekuma.data.dao.bean.I_BaseBean;

public interface I_CustomerProduct extends I_BaseBean<String> {

    public static final String CUSTOMER_ID = "CUSTOMER_ID";

    public static final String PRODUCT_ID = "PRODUCT_ID";

    public static final String PRICE_IN = "PRICE_IN";

    public static final String TIME_IN = "TIME_IN";

    public static final String REASON_IN = "REASON_IN";

    public static final String PRICE_OUT = "PRICE_OUT";

    public static final String TIME_OUT = "TIME_OUT";

    public static final String REASON_OUT = "REASON_OUT";

    public static final String CONTRACTPRICE = "CONTRACTPRICE";
}
