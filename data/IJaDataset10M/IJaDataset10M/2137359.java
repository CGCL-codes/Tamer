package org.campware.cream.modules.screens;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.campware.cream.om.Contact;
import org.campware.cream.om.ContactPeer;
import org.campware.cream.om.CustomerPeer;
import org.campware.cream.om.LanguagePeer;
import org.campware.cream.om.ContactCategoryPeer;
import org.campware.cream.om.ProductPeer;
import org.campware.cream.om.ProjectPeer;
import org.campware.cream.om.SalutationPeer;
import org.campware.cream.om.JobPositionPeer;
import org.campware.cream.om.CountryPeer;
import org.campware.cream.om.RegionPeer;
import org.campware.cream.om.ContactDocPeer;
import org.campware.cream.om.SorderDocPeer;
import org.campware.cream.om.Task;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class ContactForm extends CreamForm {

    protected void initScreen() {
        setModuleType(ENTITY);
        setModuleName("CONTACT");
        setIdName(ContactPeer.CONTACT_ID);
        setFormIdName("contactid");
    }

    protected boolean getEntry(Criteria criteria, Context context) {
        try {
            Contact entry = (Contact) ContactPeer.doSelect(criteria).get(0);
            context.put("entry", entry);
            Criteria custcrit = new Criteria();
            Criteria.Criterion cu1 = custcrit.getNewCriterion(CustomerPeer.CUSTOMER_ID, new Integer(1000), Criteria.EQUAL);
            Criteria.Criterion cu2 = custcrit.getNewCriterion(CustomerPeer.STATUS, new Integer(30), Criteria.EQUAL);
            Criteria.Criterion cu3 = custcrit.getNewCriterion(CustomerPeer.CUSTOMER_ID, new Integer(entry.getCustomerId()), Criteria.EQUAL);
            custcrit.add(cu1.or(cu2.or(cu3)));
            custcrit.addAscendingOrderByColumn(CustomerPeer.CUSTOMER_DISPLAY);
            context.put("customers", CustomerPeer.doSelect(custcrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNew(Context context) {
        try {
            Contact entry = new Contact();
            context.put("entry", entry);
            Criteria custcrit = new Criteria();
            Criteria.Criterion cu1 = custcrit.getNewCriterion(CustomerPeer.CUSTOMER_ID, new Integer(1000), Criteria.EQUAL);
            Criteria.Criterion cu2 = custcrit.getNewCriterion(CustomerPeer.STATUS, new Integer(30), Criteria.EQUAL);
            custcrit.add(cu1.or(cu2));
            custcrit.addAscendingOrderByColumn(CustomerPeer.CUSTOMER_DISPLAY);
            context.put("customers", CustomerPeer.doSelect(custcrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNewRelated(int relform, int relid, Context context) {
        try {
            Contact entry = new Contact();
            if (relform == CUSTOMER) {
                entry.setCustomerId(relid);
                Criteria custcrit = new Criteria();
                Criteria.Criterion cu1 = custcrit.getNewCriterion(CustomerPeer.CUSTOMER_ID, new Integer(1000), Criteria.EQUAL);
                Criteria.Criterion cu2 = custcrit.getNewCriterion(CustomerPeer.CUSTOMER_ID, new Integer(relid), Criteria.EQUAL);
                custcrit.add(cu1.or(cu2));
                custcrit.addAscendingOrderByColumn(CustomerPeer.CUSTOMER_DISPLAY);
                context.put("customers", CustomerPeer.doSelect(custcrit));
            }
            context.put("entry", entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getLookups(Context context) {
        try {
            Criteria langcrit = new Criteria();
            langcrit.add(LanguagePeer.LANGUAGE_ID, 999, Criteria.GREATER_THAN);
            langcrit.addAscendingOrderByColumn(LanguagePeer.LANGUAGE_NAME);
            context.put("languages", LanguagePeer.doSelect(langcrit));
            Criteria custcatcrit = new Criteria();
            custcatcrit.add(ContactCategoryPeer.CONTACT_CAT_ID, 999, Criteria.GREATER_THAN);
            custcatcrit.addAscendingOrderByColumn(ContactCategoryPeer.CONTACT_CAT_NAME);
            context.put("contactcats", ContactCategoryPeer.doSelect(custcatcrit));
            Criteria countrycrit = new Criteria();
            countrycrit.add(CountryPeer.COUNTRY_ID, 999, Criteria.GREATER_THAN);
            countrycrit.addAscendingOrderByColumn(CountryPeer.COUNTRY_NAME);
            context.put("countries", CountryPeer.doSelect(countrycrit));
            Criteria regioncrit = new Criteria();
            regioncrit.add(RegionPeer.REGION_ID, 999, Criteria.GREATER_THAN);
            regioncrit.addAscendingOrderByColumn(RegionPeer.REGION_NAME);
            context.put("regions", RegionPeer.doSelect(regioncrit));
            Criteria incomecrit = new Criteria();
            incomecrit.add(SalutationPeer.SALUTATION_ID, 999, Criteria.GREATER_THAN);
            incomecrit.addAscendingOrderByColumn(SalutationPeer.SALUTATION_NAME);
            context.put("salutations", SalutationPeer.doSelect(incomecrit));
            Criteria educrit = new Criteria();
            educrit.add(JobPositionPeer.JOB_POSITION_ID, 999, Criteria.GREATER_THAN);
            educrit.addAscendingOrderByColumn(JobPositionPeer.JOB_POSITION_NAME);
            context.put("jobpositions", JobPositionPeer.doSelect(educrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
