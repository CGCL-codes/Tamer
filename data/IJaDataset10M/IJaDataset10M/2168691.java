package org.mnlr.arp.db;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mnlr.arp.util.ARPHibernateUtil;

/**
 *
 * @author rgsaavedra
 */
public class ApplicantDocType extends Type {

    public enum ApplicantDoc {

        MEDICAL, TRACKINGCARD, PASSPORT, JOBOFFER, RESUME, INVITATIONFORM, VISANOTICE;

        public String getShortCode() {
            if (this.toString().length() > 5) {
                return this.toString().substring(0, 5);
            }
            return this.toString();
        }
    }

    public static List<ApplicantDocument> getDocuments() {
        ArrayList<ApplicantDocument> list = new ArrayList<ApplicantDocument>();
        for (ApplicantDoc doc : ApplicantDoc.values()) {
            ApplicantDocument docu = new ApplicantDocument();
            docu.setType(doc);
            list.add(docu);
        }
        return list;
    }
}
