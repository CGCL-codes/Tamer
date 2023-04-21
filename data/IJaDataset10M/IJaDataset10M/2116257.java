package org.campware.dream.modules.screens;

import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.campware.dream.om.Dreturn;
import org.campware.dream.om.DreturnPeer;
import org.campware.dream.om.LocationPeer;
import org.campware.dream.om.SalesDistrictPeer;
import org.campware.dream.om.DistributorPeer;
import org.campware.dream.om.DorderPeer;
import org.campware.dream.om.DorderItemPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class DreturnForm extends CreamForm {

    protected void initScreen() {
        setModuleType(DOCUMENT);
        setModuleName("DRETURN");
        setIdName(DreturnPeer.DRETURN_ID);
        setFormIdName("dreturnid");
    }

    protected boolean getEntry(Criteria criteria, Context context) {
        try {
            Dreturn entry = (Dreturn) DreturnPeer.doSelect(criteria).get(0);
            context.put("entry", entry);
            Criteria orditemcrit = new Criteria();
            orditemcrit.add(DorderItemPeer.DORDER_ID, entry.getDorderId());
            orditemcrit.addJoin(LocationPeer.LOCATION_ID, DorderItemPeer.LOCATION_ID);
            orditemcrit.addJoin(SalesDistrictPeer.SALES_DISTRICT_ID, LocationPeer.SALES_DISTRICT_ID);
            orditemcrit.addAscendingOrderByColumn(SalesDistrictPeer.SALES_DISTRICT_NAME);
            orditemcrit.addAscendingOrderByColumn(LocationPeer.LOCATION_DISPLAY);
            context.put("entryitems", DorderItemPeer.doSelect(orditemcrit));
            Criteria ordcrit = new Criteria();
            Criteria.Criterion a1 = ordcrit.getNewCriterion(DorderPeer.DORDER_ID, new Integer(1000), Criteria.EQUAL);
            Criteria.Criterion a2 = ordcrit.getNewCriterion(DorderPeer.DORDER_ID, new Integer(entry.getDorderId()), Criteria.EQUAL);
            Criteria.Criterion a3 = ordcrit.getNewCriterion(DorderPeer.DISTRIBUTOR_ID, new Integer(entry.getDistributorId()), Criteria.EQUAL);
            Criteria.Criterion a4 = ordcrit.getNewCriterion(DorderPeer.STATUS, new Integer(30), Criteria.EQUAL);
            String inSelect = new String("DORDER_ID IN (SELECT DORDER_ID FROM DSHIPMENT WHERE STATUS=" + new Integer(50).toString() + ")");
            Criteria.Criterion a5 = criteria.getNewCriterion(DorderPeer.DORDER_ID, (Object) inSelect, Criteria.CUSTOM);
            ordcrit.add(a1.or(a2.or(a3.and(a4).and(a5))));
            ordcrit.addAscendingOrderByColumn(DorderPeer.DORDER_CODE);
            context.put("orders", DorderPeer.doSelect(ordcrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNew(Context context) {
        try {
            Dreturn entry = new Dreturn();
            context.put("entry", entry);
            Criteria ordcrit = new Criteria();
            ordcrit.add(DorderPeer.DORDER_ID, 1000, Criteria.EQUAL);
            context.put("orders", DorderPeer.doSelect(ordcrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getLookups(Context context) {
        try {
            Criteria custcrit = new Criteria();
            Criteria.Criterion b1 = custcrit.getNewCriterion(DistributorPeer.DISTRIBUTOR_ID, new Integer(1000), Criteria.EQUAL);
            Criteria.Criterion b2 = custcrit.getNewCriterion(DistributorPeer.STATUS, new Integer(29), Criteria.GREATER_THAN);
            custcrit.add(b1.or(b2));
            custcrit.addAscendingOrderByColumn(DistributorPeer.DISTRIBUTOR_DISPLAY);
            context.put("distributors", DistributorPeer.doSelect(custcrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
