package cn.ekuma.epos.db.table;

import cn.ekuma.data.dao.bean.I_ModifiedLogBean;

public interface I_Product extends I_ModifiedLogBean<String> {

    public static String REFERENCE = "REFERENCE";

    public static String CODE = "CODE";

    public static String NAME = "NAME";

    public static String ISCOM = "ISCOM";

    public static String ISSCALE = "ISSCALE";

    public static String PRICEBUY = "PRICEBUY";

    public static String PRICESELL = "PRICESELL";

    public static String CATEGORY = "CATEGORY";

    public static String TAXCAT = "TAXCAT";

    public static String ATTRIBUTESET_ID = "ATTRIBUTESET_ID";

    public static String ATTRIBUTES = "ATTRIBUTES";

    public static String CUSTOMERPRICESELL = "CUSTOMERPRICESELL";

    public static String ORIGIN = "ORIGIN";

    public static String QUANTITY = "QUANTITY";

    public static String UNIT = "UNIT";

    public static String MANUFACTUR = "MANUFACTUR";

    public static String SIMILARCODE = "SIMILARCODE";

    public static String DURABILITY = "DURABILITY";

    public static String MNEMONIC = "MNEMONIC";

    public static String MANUFACTURING = "MANUFACTURING";

    public static String WHOLESALEPRICE = "WHOLESALEPRICE";

    public static String CURDATE = "CURDATE";

    public static String WEIGHT = "WEIGHT";

    public static String ISBOMSTORE = "ISBOMSTORE";
}
