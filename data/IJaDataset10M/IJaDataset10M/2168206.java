package com.fisoft.phucsinh.phucsinhsrv.service.payables;

import com.fisoft.phucsinh.phucsinhsrv.entity.EntityStatus;
import com.fisoft.phucsinh.phucsinhsrv.entity.CmServiceItem;
import com.fisoft.phucsinh.phucsinhsrv.service.common.QuerySelector;
import java.util.HashMap;

/**
 *
 * @author vantinh
 */
public class CmServiceItemQuerySelector extends QuerySelector<CmServiceItem> {

    public void selectQuery(HashMap pCriteria) {
        nameIndex = "";
        String sql = "CmServiceItem.findByCriteria";
        if (pCriteria.get("description") == null) {
            pCriteria.put("description", "%");
        }
        if (pCriteria.get("itemCode") == null) {
            pCriteria.put("itemCode", "%");
        }
        if (pCriteria.get("itemType") == null) {
            pCriteria.put("itemType", null);
        }
        if (pCriteria.get("accountCode") == null) {
            pCriteria.put("accountCode", "%");
        }
        if (pCriteria.get("company") == null) {
            pCriteria.put("company", null);
        }
        if (pCriteria.get("activeStatus") == null) {
            pCriteria.put("activeStatus", EntityStatus.ACTIVE.getValue());
        }
        sql += nameIndex;
        this.query = sql;
        this.useNamedQuery = true;
    }
}
