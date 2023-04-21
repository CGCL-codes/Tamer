package org.campware.dream.modules.screens;

import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.campware.dream.om.DoutboxEvent;
import org.campware.dream.om.DoutboxEventPeer;
import org.campware.dream.om.DinboxEvent;
import org.campware.dream.om.DinboxEventPeer;
import org.campware.dream.om.DistributorPeer;
import org.campware.dream.om.ProductPeer;
import org.campware.dream.om.ProjectPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class DoutboxEventForm extends CreamForm {

    protected void initScreen() {
        setModuleType(DOCUMENT);
        setModuleName("DOUTBOX");
        setIdName(DoutboxEventPeer.DOUTBOX_EVENT_ID);
        setFormIdName("doutboxeventid");
    }

    protected boolean getEntry(Criteria criteria, Context context) {
        try {
            DoutboxEvent entry = (DoutboxEvent) DoutboxEventPeer.doSelect(criteria).get(0);
            context.put("entry", entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNewRelated(int relid, Context context) {
        try {
            Criteria criteria = new Criteria();
            criteria.add(DinboxEventPeer.DINBOX_EVENT_ID, relid);
            DinboxEvent relEntry = (DinboxEvent) DinboxEventPeer.doSelect(criteria).get(0);
            DoutboxEvent entry = new DoutboxEvent();
            entry.setDistributorId(relEntry.getDistributorId());
            entry.setProductId(relEntry.getProductId());
            entry.setProjectId(relEntry.getProjectId());
            String oldSubject = relEntry.getSubject();
            if (oldSubject.startsWith("Re:")) {
                entry.setSubject(oldSubject);
            } else {
                entry.setSubject("Re: " + oldSubject);
            }
            entry.setBody("\n\n\n----- Original Message -----\n" + relEntry.getBody());
            context.put("entry", entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNew(Context context) {
        try {
            DoutboxEvent entry = new DoutboxEvent();
            context.put("entry", entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getLookups(Context context) {
        try {
            Criteria custcrit = new Criteria();
            custcrit.add(DistributorPeer.DISTRIBUTOR_ID, 999, Criteria.GREATER_THAN);
            custcrit.addAscendingOrderByColumn(DistributorPeer.DISTRIBUTOR_DISPLAY);
            context.put("distributors", DistributorPeer.doSelect(custcrit));
            Criteria prodcrit = new Criteria();
            prodcrit.add(ProductPeer.PRODUCT_ID, 999, Criteria.GREATER_THAN);
            prodcrit.addAscendingOrderByColumn(ProductPeer.PRODUCT_DISPLAY);
            context.put("products", ProductPeer.doSelect(prodcrit));
            Criteria projcrit = new Criteria();
            projcrit.add(ProjectPeer.PROJECT_ID, 999, Criteria.GREATER_THAN);
            projcrit.addAscendingOrderByColumn(ProjectPeer.PROJECT_NAME);
            context.put("projects", ProjectPeer.doSelect(projcrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
