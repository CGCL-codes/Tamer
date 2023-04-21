package org.campware.dream.modules.screens;

import java.util.List;
import org.apache.torque.util.Criteria;
import org.campware.dream.om.ProductCategoryPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class ProductCategoryList extends CreamList {

    protected void initScreen() {
        setModuleType(LOOKUP);
        setModuleName("PRODUCT_CATEGORY");
        setIdName(ProductCategoryPeer.PRODUCT_CAT_ID);
        setDefOrderColumn(ProductCategoryPeer.PRODUCT_CAT_NAME);
    }

    protected String getSortColumn(int sortNo) {
        if (sortNo == 1) {
            return ProductCategoryPeer.PRODUCT_CAT_NAME;
        }
        return "";
    }

    protected List getEntries(Criteria criteria) {
        try {
            return ProductCategoryPeer.doSelect(criteria);
        } catch (Exception e) {
            return null;
        }
    }
}
