package eof.sm;

import reports.utility.datamodel.sm.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import reports.utility.datamodel.administration.CAPTIONS_PATTERNS;
import reports.utility.datamodel.administration.CAPTIONS_PATTERNS_KEY;
import reports.utility.datamodel.administration.CAPTIONS_PATTERNS_MANAGER;
import reports.utility.datamodel.administration.ADM_FORM_LETTER;
import reports.utility.datamodel.administration.ADM_FORM_LETTER_KEY;
import reports.utility.datamodel.administration.FORM_LETTER_FORMAT;
import reports.utility.datamodel.administration.FORM_LETTER_FORMAT_KEY;
import reports.utility.ADM_FORM_LETTER_CREATOR;

/**
 *
 * @author Administrator
 */
public class SMProcessing {

    private static boolean executing = true;

    private static int count1 = 0, count2 = 0;

    private Thread t;

    private String message = "";

    private int current = 0;

    private boolean done = false;

    private boolean canceled = false;

    /** Creates a new instance of SMProcessing */
    public SMProcessing() {
    }

    public void execute() {
        setExecuting(true);
        try {
            Session session = tools.HibernateUtil.getSessionFactory().openSession();
            Integer loginLibId = new Integer(reports.utility.StaticValues.getInstance().getLoginLibraryId());
            java.util.List list = session.createQuery("from SM_SUBSCRIPTION as s where s.status like 'B'").list();
            for (int i = 0; i < list.size(); i++) {
                SM_SUBSCRIPTION sms = (SM_SUBSCRIPTION) list.get(i);
                Integer subId = sms.getPrimaryKey().getSubscription_id();
                Integer libId = sms.getPrimaryKey().getLibrary_id();
                count1 = subId.intValue();
                java.util.List list2 = session.createQuery("from SM_REGISTRATION as s where s.subscription_id=" + subId + " and s.primaryKey.library_id=" + libId).list();
                for (int j = 0; j < list2.size(); j++) {
                    SM_REGISTRATION sr = (SM_REGISTRATION) list2.get(j);
                    count2 = sr.getPrimaryKey().getRegistration_id().intValue();
                    java.util.List list3 = session.createQuery("from SM_PREDICTION_NEW as sp where sp.primaryKey.library_id=" + libId + " and sp.primaryKey.subscription_id=" + subId).list();
                    for (int k = 0; k < list3.size(); k++) {
                        SM_PREDICTION_NEW spn = (SM_PREDICTION_NEW) list3.get(k);
                        java.sql.Timestamp predectedDate = spn.getPridicted_date();
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal = reports.utility.Utility.getInstance().removeHourMinuteSecondMillisecond(cal);
                        java.sql.Timestamp currentDate = new java.sql.Timestamp(cal.getTimeInMillis());
                        System.out.println("predectedDate=" + predectedDate);
                        System.out.println("currentDate=" + currentDate);
                        if (predectedDate.equals(currentDate)) {
                            String enumChrono = spn.getEnum_chrono();
                            Transaction tx1 = session.beginTransaction();
                            SM_REGISTRATION sr1 = new SM_REGISTRATION();
                            SM_REGISTRATION_KEY srk1 = new SM_REGISTRATION_KEY();
                            srk1.setLibrary_id(libId);
                            java.util.List maxRegId = session.createQuery("select max(sr.primaryKey.registration_id) from SM_REGISTRATION as sr where sr.primaryKey.library_id=" + libId).list();
                            int regId = ((Integer) maxRegId.get(0)).intValue();
                            srk1.setRegistration_id(new Integer(regId + 1));
                            sr1.setPrimaryKey(srk1);
                            sr1.setAcc_matter(sr.getAcc_matter());
                            sr1.setCap_pat_id(spn.getCap_pat_id());
                            sr1.setCataloguerecordid(sr.getCataloguerecordid());
                            sr1.setCopies_status(sr.getCopies_status());
                            sr1.setEntry_date(sms.getEntry_date());
                            sr1.setEntry_id(sms.getEntry_id());
                            sr1.setEnum_index_str(sr.getEnum_index_str());
                            sr1.setExpected_date(null);
                            sr1.setIss_details(sr.getIss_details());
                            sr1.setIssue_no(sr.getIssue_no());
                            CAPTIONS_PATTERNS_KEY cpk = new CAPTIONS_PATTERNS_KEY();
                            cpk.setCap_pat_id(spn.getCap_pat_id());
                            cpk.setCataloguerecordid(sms.getCataloguerecordid());
                            cpk.setOwner_library_id(libId);
                            CAPTIONS_PATTERNS cp = (CAPTIONS_PATTERNS) session.load(CAPTIONS_PATTERNS.class, cpk);
                            sr1.setIssue_type(cp.getCap_pat_type());
                            pa.PredictionAlgorithm pa = new pa.PredictionAlgorithm(cp.getCap_pat(), enumChrono, new Long(currentDate.getTime()));
                            sr1.setEnum_chron(pa.getEnumerationChronologyXMLString());
                            Transaction tx3 = session.beginTransaction();
                            try {
                                session.createQuery("update CAPTIONS_PATTERNS as cp set cp.cap_pat=" + pa.getCaptionsPatternsXMLString() + " where cp.primaryKey.cataloguerecordid=" + sms.getCataloguerecordid() + " and cp.primaryKey.owner_library_id=" + libId + " and cp.primaryKey.cap_pat_id=" + spn.getCap_pat_id());
                                tx3.commit();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            SM_SUBSCRIPTION_FAMILY_KEY sfk = new SM_SUBSCRIPTION_FAMILY_KEY();
                            sfk.setLibrary_id(loginLibId);
                            sfk.setSubscription_id(subId);
                            sfk.setSubscription_library_id(libId);
                            SM_SUBSCRIPTION_FAMILY sf = (SM_SUBSCRIPTION_FAMILY) session.load(SM_SUBSCRIPTION_FAMILY.class, sfk);
                            sr1.setNo_copies(sf.getNo_of_copies());
                            sr1.setOwner_library_id(loginLibId);
                            sr1.setParent_registration_id(sr.getParent_registration_id());
                            sr1.setRegistration_date(reports.utility.StaticValues.getInstance().getReferenceDate());
                            sr1.setSerial_id(null);
                            sr1.setStatus("B");
                            sr1.setSubscription_id(subId);
                            sr1.setSupp_title("");
                            sr1.setUrl("");
                            sr1.setVolume_no("");
                            try {
                                session.save(sr1);
                                tx1.commit();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            for (int x = 0; x < sr1.getNo_copies().intValue(); x++) {
                                Transaction tx2 = session.beginTransaction();
                                SM_REGISTRATION_COPY_KEY srck = new SM_REGISTRATION_COPY_KEY();
                                srck.setRegistration_id(new Integer(regId + 1));
                                srck.setSubscription_library_id(libId);
                                srck.setCopy_id(new Integer(x));
                                SM_REGISTRATION_COPY src = new SM_REGISTRATION_COPY();
                                src.setPrimaryKey(srck);
                                src.setDelayed_date(null);
                                src.setEntry_date(sr1.getEntry_date());
                                src.setEntry_id(sr1.getEntry_id());
                                src.setEntry_library_id(sr1.getOwner_library_id());
                                src.setLibrary_id(loginLibId);
                                src.setStatus("B");
                                try {
                                    session.save(src);
                                    tx2.commit();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                            Transaction tx4 = session.beginTransaction();
                            try {
                                session.createQuery("update SM_PREDICTION_NEW as spn set (spn.status='B' and spn.enum_chrono='" + enumChrono + "') where spn.primaryKey.library_id=" + libId + " and spn.primaryKey.subscription_id=" + subId + " and spn.primaryKey.prediction_id=" + spn.getPrimaryKey().getPrediction_id());
                                tx4.commit();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }
            java.util.List list1 = session.createQuery("from SM_SUBSCRIPTION as s where s.status like 'B'").list();
            for (int i = 0; i < list1.size(); i++) {
                SM_SUBSCRIPTION sms = (SM_SUBSCRIPTION) list1.get(i);
                Integer subId = sms.getPrimaryKey().getSubscription_id();
                Integer libId = sms.getPrimaryKey().getLibrary_id();
                count1 = subId.intValue();
                java.util.List list3 = session.createQuery("from SM_REGISTRATION as sr where sr.primaryKey.library_id=" + loginLibId + " and sr.subscription_id=" + subId + " and sr.status like 'B'").list();
                for (int j = 0; j < list3.size(); j++) {
                    SM_REGISTRATION sr = (SM_REGISTRATION) list3.get(j);
                    Integer regId = sr.getPrimaryKey().getRegistration_id();
                    count2 = regId.intValue();
                    java.util.List list4 = session.createQuery("from SM_REGISTRATIONS_IN_CLAIM as s where s.primaryKey.library_id=" + libId + " and s.primaryKey.registration_id=" + regId).list();
                    int claimsSent = list4.size();
                    int maxClaims = sms.getNo_of_claims().intValue();
                    if (claimsSent < maxClaims && claimsSent != 0) {
                        SM_REGISTRATIONS_IN_CLAIM sric = (SM_REGISTRATIONS_IN_CLAIM) list4.get(claimsSent - 1);
                        Integer claimId = sric.getPrimaryKey().getClaim_id();
                        SM_CLAIM_ISSUE_KEY scik = new SM_CLAIM_ISSUE_KEY();
                        scik.setClaim_id(claimId);
                        scik.setLibrary_id(libId);
                        SM_CLAIM_ISSUE sci = (SM_CLAIM_ISSUE) session.load(SM_CLAIM_ISSUE.class, scik);
                        java.sql.Timestamp lastDateSent = sci.getClaim_date();
                        java.util.Date lastDateSent1 = new java.util.Date(lastDateSent.getTime());
                        java.util.Calendar cal1 = java.util.Calendar.getInstance();
                        cal1 = reports.utility.Utility.getInstance().removeHourMinuteSecondMillisecond(cal1);
                        long time2 = cal1.getTimeInMillis();
                        cal1.setTime(lastDateSent1);
                        long time1 = cal1.getTimeInMillis();
                        int delay = sms.getDays_between_claims().intValue();
                        int lastDay = (int) (time1 / (1000 * 60 * 60 * 24));
                        int currentDay = (int) (time2 / (1000 * 60 * 60 * 24));
                        System.out.println("-------------------------------------------------------");
                        System.out.println("currentDay=" + currentDay);
                        System.out.println("lastDay+delay=" + lastDay + delay);
                        System.out.println("last date=" + lastDay);
                        System.out.println("delay=" + delay);
                        System.out.println("--------------------------------------------------------");
                        if (currentDay == (lastDay + delay)) {
                            int formatId = 22;
                            String toId = sms.getVendor_id().toString();
                            int toLibraryId = libId.intValue();
                            String toType = "B";
                            String toEmailId = sms.getPub_email_id();
                            java.sql.Timestamp referenceDate = new java.sql.Timestamp(reports.utility.Utility.getInstance().removeHourMinuteSecondMillisecond(java.util.Calendar.getInstance()).getTimeInMillis());
                            String entryId = sms.getEntry_id();
                            String contentParameters[] = new String[10];
                            contentParameters[0] = reports.utility.StaticValues.getInstance().getLoginLibraryName();
                            contentParameters[1] = "";
                            contentParameters[2] = sms.getPublisher();
                            contentParameters[3] = sms.getPub_address();
                            contentParameters[4] = sms.getTitle();
                            contentParameters[5] = "";
                            contentParameters[6] = sr.getSubscription_id().toString();
                            java.util.List list6 = session.createQuery("from SM_SUBSCRIPTION_INVOICE as s where s.primaryKey.subscription_library_id=" + libId + " and s.primaryKey.subscription_id like '" + sr.getSubscription_id() + "'").list();
                            SM_SUBSCRIPTION_INVOICE ssi = (SM_SUBSCRIPTION_INVOICE) list6.get(0);
                            contentParameters[7] = ssi.getPrimaryKey().getInvoice_no() + " Dated: " + ssi.getInvoice_date();
                            FORM_LETTER_FORMAT_KEY flfk = new FORM_LETTER_FORMAT_KEY();
                            flfk.setFormat_id(new Integer(22));
                            flfk.setLibrary_id(libId);
                            FORM_LETTER_FORMAT flm = (FORM_LETTER_FORMAT) session.load(FORM_LETTER_FORMAT.class, flfk);
                            contentParameters[8] = flm.getPrefix() + " " + (flm.getMax_no().intValue() + 1);
                            contentParameters[9] = reports.utility.StaticValues.getInstance().getReferenceDate().toString();
                            java.sql.Connection con = reports.utility.database.ConnectionPoolFactory.getInstance().getConnectionPool().getConnection();
                            java.util.Hashtable parameters = null;
                            ADM_FORM_LETTER_CREATOR aflc = new ADM_FORM_LETTER_CREATOR();
                            aflc.generateFormLetter(con, session, libId.intValue(), formatId, toId, toLibraryId, toType, toEmailId, referenceDate, entryId, parameters, contentParameters, 8, -1);
                            con.close();
                            Transaction tx1 = session.beginTransaction();
                            java.util.List list5 = session.createQuery("select max(s.primaryKey.claim_id) from SM_CLAIM_ISSUE as s where s.primaryKey.library_id=" + libId).list();
                            Integer maxClaimId = (Integer) list5.get(0);
                            int newClaimId = maxClaimId.intValue() + 1;
                            SM_CLAIM_ISSUE sci1 = new SM_CLAIM_ISSUE();
                            SM_CLAIM_ISSUE_KEY scik1 = new SM_CLAIM_ISSUE_KEY();
                            scik1.setClaim_id(new Integer(newClaimId));
                            scik1.setLibrary_id(libId);
                            sci1.setPrimaryKey(scik1);
                            sci1.setClaim_date(referenceDate);
                            sci1.setClaim_letter_no(contentParameters[8]);
                            sci1.setEmail_status("N");
                            sci1.setEntry_date(referenceDate);
                            sci1.setEntry_id(entryId);
                            java.util.List list7 = session.createQuery("select max(afl.primaryKey.form_id) from ADM_FORM_LETTER as afl where afl.primaryKey.library_id=" + libId + " and afl.format_id=" + new Integer(formatId)).list();
                            sci1.setForm_id((Integer) list7.get(0));
                            ADM_FORM_LETTER_KEY aflk = new ADM_FORM_LETTER_KEY();
                            aflk.setForm_id((Integer) list7.get(0));
                            aflk.setLibrary_id(libId);
                            ADM_FORM_LETTER afl = (ADM_FORM_LETTER) session.load(ADM_FORM_LETTER.class, aflk);
                            sci1.setClaim_content(tools.StringProcessor.getInstance().verifyString(afl.getForm_letter_content()));
                            sci1.setPrint_status("N");
                            sci1.setReceived_date(null);
                            try {
                                session.save(sci1);
                                tx1.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Transaction tx2 = session.beginTransaction();
                            SM_REGISTRATIONS_IN_CLAIM sric1 = new SM_REGISTRATIONS_IN_CLAIM();
                            SM_REGISTRATIONS_IN_CLAIM_KEY srick1 = new SM_REGISTRATIONS_IN_CLAIM_KEY();
                            srick1.setClaim_id(new Integer(newClaimId));
                            srick1.setLibrary_id(libId);
                            srick1.setRegistration_id(regId);
                            sric1.setPrimaryKey(srick1);
                            try {
                                session.save(sric1);
                                tx2.commit();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                java.util.List list4 = session.createQuery("from SM_REGISTRATION as sr where sr.primaryKey.library_id=" + loginLibId + " and (sr.status like 'D' or sr.status like 'E')").list();
                for (int k = 0; k < list4.size(); k++) {
                    SM_REGISTRATION sr = (SM_REGISTRATION) list4.get(k);
                    Integer regId = sr.getPrimaryKey().getRegistration_id();
                    count2 = regId.intValue();
                    java.util.List list5 = session.createQuery("from SM_REGISTRATION_COPY as s where s.primaryKey.subscription_library_id=" + libId + " and s.primaryKey.registration_id=" + regId + " and (s.status like 'D' or s.status like 'E')").list();
                    for (int j = 0; j < list5.size(); j++) {
                        SM_REGISTRATION_COPY src = (SM_REGISTRATION_COPY) list5.get(j);
                        java.sql.Timestamp delayDate = src.getDelayed_date();
                        java.util.Date delayDate1 = new java.util.Date(delayDate.getTime());
                        java.util.Calendar cal1 = java.util.Calendar.getInstance();
                        cal1.setTime(delayDate1);
                        long time3 = cal1.getTimeInMillis();
                        java.util.List list6 = session.createQuery("from SM_REGISTRATIONS_IN_CLAIM as s where s.primaryKey.library_id=" + libId + " and s.primaryKey.registration_id=" + regId).list();
                        int claimsSent = list6.size();
                        int maxClaims = sms.getNo_of_claims().intValue();
                        if (claimsSent < maxClaims && claimsSent != 0) {
                            SM_REGISTRATIONS_IN_CLAIM sric = (SM_REGISTRATIONS_IN_CLAIM) list6.get(claimsSent - 1);
                            Integer claimId = sric.getPrimaryKey().getClaim_id();
                            SM_CLAIM_ISSUE_KEY scik = new SM_CLAIM_ISSUE_KEY();
                            scik.setClaim_id(claimId);
                            scik.setLibrary_id(libId);
                            SM_CLAIM_ISSUE sci = (SM_CLAIM_ISSUE) session.load(SM_CLAIM_ISSUE.class, scik);
                            java.sql.Timestamp lastDateSent = sci.getClaim_date();
                            java.util.Calendar cal = reports.utility.Utility.getInstance().removeHourMinuteSecondMillisecond(java.util.Calendar.getInstance());
                            java.sql.Timestamp currentDate = new java.sql.Timestamp(cal.getTimeInMillis());
                            java.util.Date lastDateSent1 = new java.util.Date(lastDateSent.getTime());
                            java.util.Calendar cal2 = java.util.Calendar.getInstance();
                            cal2.setTime(lastDateSent1);
                            long time1 = cal2.getTimeInMillis();
                            java.util.Date currentDate1 = new java.util.Date(currentDate.getTime());
                            cal2.setTime(currentDate1);
                            long time2 = cal2.getTimeInMillis();
                            int delay = sms.getDays_between_claims().intValue();
                            int lastDay = (int) (time1 / (1000 * 60 * 60 * 24));
                            int currentDay = (int) (time2 / (1000 * 60 * 60 * 24));
                            int delayDays = (int) (time3 / (1000 * 60 * 60 * 24));
                            if ((lastDay + delayDays + delay) == currentDay) {
                                int formatId = 22;
                                String toId = sms.getVendor_id().toString();
                                int toLibraryId = libId.intValue();
                                String toType = "B";
                                String toEmailId = sms.getPub_email_id();
                                java.sql.Timestamp referenceDate = new java.sql.Timestamp(reports.utility.Utility.getInstance().removeHourMinuteSecondMillisecond(java.util.Calendar.getInstance()).getTimeInMillis());
                                String entryId = sms.getEntry_id();
                                String contentParameters[] = new String[10];
                                contentParameters[0] = reports.utility.StaticValues.getInstance().getLoginLibraryName();
                                contentParameters[1] = "";
                                contentParameters[2] = sms.getPublisher();
                                contentParameters[3] = sms.getPub_address();
                                contentParameters[4] = sms.getTitle();
                                contentParameters[5] = "";
                                contentParameters[6] = sr.getSubscription_id().toString();
                                java.util.List list7 = session.createQuery("from SM_SUBSCRIPTION_INVOICE as s where s.primaryKey.subscription_library_id=" + libId + " and s.primaryKey.subscription_id like '" + sr.getSubscription_id() + "'").list();
                                SM_SUBSCRIPTION_INVOICE ssi = (SM_SUBSCRIPTION_INVOICE) list7.get(0);
                                contentParameters[7] = ssi.getPrimaryKey().getInvoice_no() + " Dated: " + reports.utility.Utility.getInstance().getFormattedDate(ssi.getInvoice_date());
                                FORM_LETTER_FORMAT_KEY flfk = new FORM_LETTER_FORMAT_KEY();
                                flfk.setFormat_id(new Integer(22));
                                flfk.setLibrary_id(libId);
                                FORM_LETTER_FORMAT flm = (FORM_LETTER_FORMAT) session.load(FORM_LETTER_FORMAT.class, flfk);
                                contentParameters[8] = flm.getPrefix() + " " + (flm.getMax_no().intValue() + 1);
                                contentParameters[9] = reports.utility.Utility.getInstance().getFormattedDate(referenceDate);
                                java.sql.Connection con = reports.utility.database.ConnectionPoolFactory.getInstance().getConnectionPool().getConnection();
                                java.util.Hashtable parameters = null;
                                ADM_FORM_LETTER_CREATOR aflc = new ADM_FORM_LETTER_CREATOR();
                                aflc.generateFormLetter(con, session, libId.intValue(), formatId, toId, toLibraryId, toType, toEmailId, referenceDate, entryId, parameters, contentParameters, 8, -1);
                                con.close();
                                Transaction tx1 = session.beginTransaction();
                                java.util.List list8 = session.createQuery("select max(s.primaryKey.claim_id) from SM_CLAIM_ISSUE as s where s.primaryKey.library_id=" + libId).list();
                                Integer maxClaimId = (Integer) list8.get(0);
                                int newClaimId = maxClaimId.intValue() + 1;
                                SM_CLAIM_ISSUE sci1 = new SM_CLAIM_ISSUE();
                                SM_CLAIM_ISSUE_KEY scik1 = new SM_CLAIM_ISSUE_KEY();
                                scik1.setClaim_id(new Integer(newClaimId));
                                scik1.setLibrary_id(libId);
                                sci1.setPrimaryKey(scik1);
                                sci1.setClaim_date(referenceDate);
                                sci1.setClaim_letter_no(contentParameters[8]);
                                sci1.setEmail_status("N");
                                sci1.setEntry_date(referenceDate);
                                java.util.List list9 = session.createQuery("select max(afl.primaryKey.form_id) from ADM_FORM_LETTER as afl where afl.primaryKey.library_id=" + libId + " and afl.format_id=" + new Integer(formatId)).list();
                                sci1.setForm_id((Integer) list9.get(0));
                                ADM_FORM_LETTER_KEY aflk = new ADM_FORM_LETTER_KEY();
                                aflk.setForm_id((Integer) list9.get(0));
                                aflk.setLibrary_id(libId);
                                ADM_FORM_LETTER afl = (ADM_FORM_LETTER) session.load(ADM_FORM_LETTER.class, aflk);
                                sci1.setClaim_content(tools.StringProcessor.getInstance().verifyString(afl.getForm_letter_content()));
                                sci1.setForm_id(new Integer(formatId));
                                sci1.setPrint_status("N");
                                sci1.setReceived_date(null);
                                try {
                                    session.save(sci1);
                                    tx1.commit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Transaction tx2 = session.beginTransaction();
                                SM_REGISTRATIONS_IN_CLAIM sric1 = new SM_REGISTRATIONS_IN_CLAIM();
                                SM_REGISTRATIONS_IN_CLAIM_KEY srick1 = new SM_REGISTRATIONS_IN_CLAIM_KEY();
                                srick1.setClaim_id(new Integer(newClaimId));
                                srick1.setLibrary_id(libId);
                                srick1.setRegistration_id(regId);
                                sric1.setPrimaryKey(srick1);
                                try {
                                    session.save(sric1);
                                    tx2.commit();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setExecuting(false);
    }

    public boolean isExecuting() {
        return executing;
    }

    public void setExecuting(boolean executing) {
        this.executing = executing;
    }

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }

    public String getMessage() {
        message = "The no of serials processing=" + count1 + "\n";
        return message;
    }

    public void setMessage(String me) {
        message = me;
    }

    public void go() {
        final tools.SwingWorker worker = new tools.SwingWorker() {

            public Object construct() {
                current = 0;
                done = false;
                canceled = false;
                return new ActualTask();
            }
        };
        worker.start();
    }

    class ActualTask {

        ActualTask() {
            while (!canceled && !done) {
                try {
                    Thread.sleep(1000);
                    SMProcessing smp = new SMProcessing();
                    smp.execute();
                    current = smp.getCount1();
                    if (!smp.isExecuting()) {
                        done = true;
                        current = smp.getCount1();
                    }
                } catch (InterruptedException e) {
                    System.out.println("ActualTask interrupted");
                }
            }
        }
    }
}
