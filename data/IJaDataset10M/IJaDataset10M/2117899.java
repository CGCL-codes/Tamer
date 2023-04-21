package servlet.sm;

/**
 *
 * @author  Administrator
 */
public class RegisterSerialHandler {

    /** Creates a new instance of RegisterSerialHandler */
    public RegisterSerialHandler(ejb.bprocess.sm.RegisterSerialHome home) {
        this.home = home;
    }

    public String getLiveAndDormantSubscriptions(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        try {
            xml = home.create().getLiveAndDormantSubscriptions(libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String insertModifySave(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().insertModifyIssue(xmlStr);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String registerSerialInitialData(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        String subscriptionId = doc.getRootElement().getChildText("SubscriptionId");
        String yearUpperLimit = doc.getRootElement().getChildText("YearUpperLimit");
        String yearLowerLimit = doc.getRootElement().getChildText("YearLowerLimit");
        try {
            xml = home.create().registerSerialInitialData(subscriptionId, libraryId, yearUpperLimit, yearLowerLimit);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String registerSerialInitialData2(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().registerSerialInitialData2(xmlStr);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getPrevousSubscriptions(org.jdom.Document doc) {
        String xml = "";
        System.out.println("in handler");
        String libraryId = doc.getRootElement().getChildText("LibraryID");
        String date1 = doc.getRootElement().getChildText("Date1");
        String date2 = doc.getRootElement().getChildText("Date2");
        try {
            xml = home.create().getPreviousSubscriptions(libraryId, date1, date2);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println("return xml string in handler===" + xml);
        return xml;
    }

    public String getLastVolumeandIsuue(org.jdom.Document doc) {
        String xml = "";
        System.out.println("in handler");
        String libraryId = doc.getRootElement().getChildText("SubscriptionLibraryId");
        String subId = doc.getRootElement().getChildText("SubscriptionId");
        try {
            xml = home.create().getLastVolumeandIssue(subId, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println("return xml string in handler===" + xml);
        return xml;
    }

    public String SubscriptionForDead(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("SubscriptionLibraryId");
        String subscriptionId = doc.getRootElement().getChildText("SubscriptionId");
        String volno = doc.getRootElement().getChildText("VolumeNo");
        String issno = doc.getRootElement().getChildText("IssueNo");
        try {
            xml = home.create().checkSubscriptionDead(subscriptionId, libraryId, volno, issno);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getSelectedVolumeDetails(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        String subscriptionId = doc.getRootElement().getChildText("SubscriptionId");
        String yearUpperLimit = doc.getRootElement().getChildText("YearUpperLimit");
        String yearLowerLimit = doc.getRootElement().getChildText("YearLowerLimit");
        String volumeNumber = doc.getRootElement().getChildText("VolumeNumber");
        String issueYear = doc.getRootElement().getChildText("IssueYear");
        try {
            xml = home.create().getSelectedVolumeDetails(subscriptionId, libraryId, volumeNumber, issueYear, yearUpperLimit, yearLowerLimit);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getIndexTitlePageSatelliteDetails(org.jdom.Document doc) {
        String xml = "";
        String indextitlepageid = doc.getRootElement().getChildText("IndexTitlePageId");
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        try {
            xml = home.create().getIndexTitlePageSatelliteDetails(indextitlepageid, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String updateCatalogueDetails(org.jdom.Document doc) {
        String xml = "";
        String catalogueRecordId = doc.getRootElement().getChildTextTrim("CatalogueRecordId");
        String ownerLibraryId = doc.getRootElement().getChildTextTrim("OwnerLibraryId");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        try {
            xml = home.create().updateCatalogueDetails(catalogueRecordId, ownerLibraryId, subscriptionId, subscriptionLibraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String saveIssueRegister2(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().saveIssueRegister2(xmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public String updateIssueCopies2(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().updateIssueCopies2(xmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public String saveIssueRegister(org.jdom.Document doc) {
        String xml = "";
        String subscriptionId = doc.getRootElement().getChildText("SubscriptionId");
        String subscriptionLibraryId = doc.getRootElement().getChildText("SubscriptionLibraryId");
        String entryId = doc.getRootElement().getChildText("CreatorId");
        String entryLibraryId = doc.getRootElement().getChildText("CreatorLibraryId");
        String catalogueRecordId = doc.getRootElement().getChildText("CatalogueRecordId");
        String ownerLibraryId = doc.getRootElement().getChildText("OwnerLibraryId");
        java.util.Vector vecRegister = new java.util.Vector(1, 1);
        java.util.Vector vecMissed = new java.util.Vector(1, 1);
        String[] predicted = null;
        java.util.List liRegister = doc.getRootElement().getChild("Register").getChildren();
        java.util.Hashtable haForDist = new java.util.Hashtable();
        for (int i = 0; i < liRegister.size(); i++) {
            java.util.Vector veone = new java.util.Vector(1, 1);
            org.jdom.Element elex = (org.jdom.Element) liRegister.get(i);
            veone.addElement(elex.getChildText("VolumeNumber"));
            veone.addElement(elex.getChildText("IssueYear"));
            veone.addElement(elex.getChildText("IssueNumber"));
            veone.addElement(elex.getChildText("IssueType"));
            veone.addElement(elex.getChildText("RegistrationDate"));
            veone.addElement(elex.getChildText("ParentRegistrationId"));
            veone.addElement(elex.getChildText("URL"));
            veone.addElement(elex.getChildText("RegistrationId"));
            veone.addElement(elex.getChildText("IssueDetails"));
            java.util.List licopy = elex.getChildren("Copy");
            java.util.Hashtable htcopy = new java.util.Hashtable();
            for (int j = 0; j < licopy.size(); j++) {
                org.jdom.Element elecopy = (org.jdom.Element) licopy.get(j);
                String[] stx = new String[2];
                stx[0] = elecopy.getChildTextTrim("LibraryId");
                stx[1] = elecopy.getChildTextTrim("Status");
                htcopy.put(elecopy.getChildTextTrim("CopyId"), stx);
            }
            if (i == 0) haForDist = htcopy;
            veone.addElement(htcopy);
            veone.addElement(elex.getChildText("AccMatter"));
            veone.addElement(elex.getChildText("SupplementTitle"));
            vecRegister.addElement(veone);
        }
        System.out.println("Near missed");
        liRegister = doc.getRootElement().getChild("Missed").getChildren();
        for (int i = 0; i < liRegister.size(); i++) {
            java.util.Vector veone = new java.util.Vector(1, 1);
            org.jdom.Element elex = (org.jdom.Element) liRegister.get(i);
            veone.addElement(elex.getChildText("VolumeNumber"));
            veone.addElement(elex.getChildText("IssueYear"));
            veone.addElement(elex.getChildText("IssueNumber"));
            veone.addElement(elex.getChildText("IssueType"));
            veone.addElement(elex.getChildText("ParentRegistrationId"));
            vecMissed.addElement(veone);
        }
        System.out.println("Near prediction");
        org.jdom.Element elePrediction = doc.getRootElement().getChild("Prediction").getChild("Issue");
        if (elePrediction != null) {
            predicted = new String[4];
            predicted[0] = elePrediction.getChildText("VolumeNumber");
            predicted[1] = elePrediction.getChildText("IssueYear");
            predicted[2] = elePrediction.getChildText("IssueNumber");
            predicted[3] = elePrediction.getChildText("PredictedDate");
        }
        System.out.println("Near  distribution");
        java.util.List liDistribution = doc.getRootElement().getChild("Distribution").getChildren();
        java.util.Hashtable haDist = new java.util.Hashtable();
        java.util.Enumeration enum1 = haForDist.keys();
        while (enum1.hasMoreElements()) {
            String key = enum1.nextElement().toString();
            String[] valx = (String[]) haForDist.get(key);
            String libidf = valx[0];
            String copyidf = valx[1];
            if (haDist.containsKey(haDist)) {
                int intval = Integer.parseInt(haDist.get(libidf).toString());
                intval++;
                haDist.put(libidf, String.valueOf(intval));
            } else {
                haDist.put(libidf, "1");
            }
        }
        System.out.println("Done");
        try {
            xml = home.create().saveIssueRegister(vecRegister, vecMissed, predicted, haDist, subscriptionId, subscriptionLibraryId, entryId, entryLibraryId, catalogueRecordId, ownerLibraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String registerIndexTitlePage(org.jdom.Document doc) {
        String xml = "";
        String volumeNumber = doc.getRootElement().getChildTextTrim("VolumeNumber");
        String issueYear = doc.getRootElement().getChildTextTrim("IssueYear");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        java.util.List liDistribution = doc.getRootElement().getChild("Distribution").getChildren();
        java.util.Hashtable haDist = new java.util.Hashtable();
        for (int i = 0; i < liDistribution.size(); i++) {
            org.jdom.Element elex = (org.jdom.Element) liDistribution.get(i);
            haDist.put(elex.getAttributeValue("Id"), elex.getChildText("Copies"));
        }
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String registrationDate = doc.getRootElement().getChildTextTrim("RegistrationDate");
        String indexPageId = doc.getRootElement().getChildTextTrim("IndexPageId");
        String titlePageId = doc.getRootElement().getChildTextTrim("TitlePageId");
        try {
            xml = home.create().registerIndexTitlePage(volumeNumber, issueYear, subscriptionId, subscriptionLibraryId, haDist, entryId, "", "", registrationDate, indexPageId, titlePageId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String registerIndexPage(org.jdom.Document doc) {
        String xml = "";
        System.out.println("index page handler entered ");
        String volumeNumber = doc.getRootElement().getChildTextTrim("VolumeNumber");
        String issueYear = doc.getRootElement().getChildTextTrim("IssueYear");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String st = doc.getRootElement().getChildTextTrim("Status");
        System.out.println("calling 1");
        java.util.List liDistribution = doc.getRootElement().getChild("Distribution").getChildren();
        java.util.Hashtable haDist = new java.util.Hashtable();
        for (int i = 0; i < liDistribution.size(); i++) {
            org.jdom.Element elex = (org.jdom.Element) liDistribution.get(i);
            String copyid = elex.getAttributeValue("Id");
            String libid = elex.getChildText("LibraryName");
            String status = elex.getChildText("Status");
            String[] str = new String[2];
            str[0] = libid;
            str[1] = status;
            haDist.put(copyid, str);
        }
        System.out.println("calling 2");
        String EntryLibId = doc.getRootElement().getChildTextTrim("EntryLibraryId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String registrationDate = doc.getRootElement().getChildTextTrim("RegistrationDate");
        String indexPageId = doc.getRootElement().getChildTextTrim("IndexPageId");
        try {
            System.out.println("calling 3");
            xml = home.create().registerIndexPage(volumeNumber, issueYear, subscriptionId, subscriptionLibraryId, haDist, entryId, EntryLibId, "", registrationDate, indexPageId, st);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String registerTitlePage(org.jdom.Document doc) {
        String xml = "";
        String volumeNumber = doc.getRootElement().getChildTextTrim("VolumeNumber");
        String issueYear = doc.getRootElement().getChildTextTrim("IssueYear");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        java.util.List liDistribution = doc.getRootElement().getChild("Distribution").getChildren();
        java.util.Hashtable haDist = new java.util.Hashtable();
        for (int i = 0; i < liDistribution.size(); i++) {
            org.jdom.Element elex = (org.jdom.Element) liDistribution.get(i);
            haDist.put(elex.getAttributeValue("Id"), elex.getChildText("Copies"));
        }
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String registrationDate = doc.getRootElement().getChildTextTrim("RegistrationDate");
        String titlePageId = doc.getRootElement().getChildTextTrim("TitlePageId");
        try {
            xml = home.create().registerTitlePage(volumeNumber, issueYear, subscriptionId, subscriptionLibraryId, haDist, entryId, "", "", registrationDate, titlePageId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getIssuesRegisteredTillNow(org.jdom.Document doc) {
        String xml = "";
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        try {
            xml = home.create().getIssuesRegisteredTillNow(subscriptionLibraryId, registrationId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String updateIssueCopies(org.jdom.Document doc) {
        String xml = "";
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        java.util.Hashtable htDist = new java.util.Hashtable();
        java.util.List li = doc.getRootElement().getChild("Distribution").getChildren();
        System.out.println("li size in handler====" + li.size());
        for (int i = 0; i < li.size(); i++) {
            org.jdom.Element ele1 = (org.jdom.Element) li.get(i);
            String copyid = ele1.getChildText("CopyId").trim();
            String lname = ele1.getChildText("LibraryName").trim();
            String st = ele1.getChildText("Status").trim();
            String[] str1 = new String[2];
            str1[0] = lname;
            str1[1] = st;
            if (!htDist.contains(copyid)) {
                htDist.put(copyid, str1);
            }
        }
        System.out.println("hash table size in handler==" + htDist.size());
        java.util.Enumeration e1 = htDist.keys();
        while (e1.hasMoreElements()) {
            String key = e1.nextElement().toString();
            String[] st = (String[]) htDist.get(key);
            System.out.println("in handler");
            System.out.println("key===" + key + "---values===" + st[0] + "/" + st[1]);
        }
        try {
            xml = home.create().updateIssueCopies(registrationId, subscriptionLibraryId, htDist, entryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getIndexTitlePagesRegisteredTillNow(org.jdom.Document doc) {
        String xml = "";
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String indexTitleId = doc.getRootElement().getChildTextTrim("IndexTitleId");
        try {
            xml = home.create().getIndexTitlePagesRegisteredTillNow(subscriptionLibraryId, indexTitleId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String updateIndexTitlePageCopies(org.jdom.Document doc) {
        String xml = "";
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String indexTitleId = doc.getRootElement().getChildTextTrim("IndexTitleId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        java.util.Hashtable htDist = new java.util.Hashtable();
        java.util.List li = doc.getRootElement().getChild("Distribution").getChildren();
        System.out.println("li size in handler====" + li.size());
        for (int i = 0; i < li.size(); i++) {
            org.jdom.Element ele1 = (org.jdom.Element) li.get(i);
            String copyid = ele1.getChildText("CopyId").trim();
            String lname = ele1.getChildText("LibraryName").trim();
            String st = ele1.getChildText("Status").trim();
            String[] str1 = new String[2];
            str1[0] = lname;
            str1[1] = st;
            if (!htDist.contains(copyid)) {
                htDist.put(copyid, str1);
            }
        }
        System.out.println("hash table size in handler==" + htDist.size());
        try {
            xml = home.create().updateIndexTitlePageCopies(indexTitleId, subscriptionLibraryId, entryId, htDist);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String markIssueAsMissed(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String volumeNumber = doc.getRootElement().getChildTextTrim("VolumeNumber");
        String issueNumber = doc.getRootElement().getChildTextTrim("IssueNumber");
        String issueYear = doc.getRootElement().getChildTextTrim("IssueYear");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String predictedDate = doc.getRootElement().getChildTextTrim("PredictedDate");
        String predictedVolumeNumber = doc.getRootElement().getChildTextTrim("PredictedVolumeNumber");
        String predictedIssueNumber = doc.getRootElement().getChildTextTrim("PredictedIssueNumber");
        String predictedIssueYear = doc.getRootElement().getChildTextTrim("PredictedIssueYear");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        try {
            xml = home.create().markIssueAsMissed(libraryId, volumeNumber, issueNumber, issueYear, entryId, predictedDate, predictedVolumeNumber, predictedIssueNumber, predictedIssueYear, subscriptionId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String markIndexTitlePageAsMissed(org.jdom.Document doc) {
        String xml = "";
        String volumeNumber = doc.getRootElement().getChildTextTrim("VolumeNumber");
        String issueYear = doc.getRootElement().getChildTextTrim("IssueYear");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String indexTitlePageType = doc.getRootElement().getChildTextTrim("IndexTitlePageType");
        try {
            xml = home.create().markIndexTitlePageAsMissed(volumeNumber, issueYear, entryId, libraryId, subscriptionId, indexTitlePageType);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String setIssueAsNoLongerAvailable(org.jdom.Document doc) {
        String xml = "";
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        try {
            xml = home.create().setIssueAsNoLongerAvailable(registrationId, libraryId, entryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String setIndexTitlePageAsNoLongerAvailable(org.jdom.Document doc) {
        String xml = "";
        String indexTitleId = doc.getRootElement().getChildTextTrim("IndexTitleId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String pagestatus = doc.getRootElement().getChildTextTrim("PageStatus");
        String type = doc.getRootElement().getChildTextTrim("Type");
        String delayeddate = "";
        if (doc.getRootElement().getChildTextTrim("Type") != null) delayeddate = doc.getRootElement().getChildTextTrim("Type");
        java.util.Hashtable ht = new java.util.Hashtable();
        java.util.List l1 = doc.getRootElement().getChild("Copies").getChildren("Copy");
        for (int i = 0; i < l1.size(); i++) {
            org.jdom.Element e1 = (org.jdom.Element) l1.get(i);
            String copyid = e1.getAttributeValue("Id");
            String[] str = new String[2];
            String lid = e1.getChildText("LibraryId");
            String st = e1.getChildText("Status");
            str[0] = lid;
            str[1] = st;
            if (!ht.contains(copyid)) ht.put(copyid, str);
        }
        try {
            xml = home.create().setIndexTitlePageAsNoLongerAvailable(indexTitleId, libraryId, entryId, pagestatus, type, ht, delayeddate);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String setIssueAsDelayed(org.jdom.Document doc) {
        String xml = "";
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String expectedDate = doc.getRootElement().getChildTextTrim("DelayedDate");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String Issdetails = doc.getRootElement().getChildTextTrim("IssueDetails");
        String status = doc.getRootElement().getChildTextTrim("Status");
        try {
            xml = home.create().setIssueAsDelayed(registrationId, libraryId, entryId, expectedDate, subscriptionId, Issdetails, status);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String setIndexTitlePageAsDelayed(org.jdom.Document doc) {
        String xml = "";
        String indexTitleId = doc.getRootElement().getChildTextTrim("IndexTitleId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String expectedDate = doc.getRootElement().getChildTextTrim("ExpectedDate");
        try {
            xml = home.create().setIndexTitlePageAsDelayed(indexTitleId, libraryId, entryId, expectedDate);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String setIssueAsDispatched(org.jdom.Document doc) {
        String xml = "";
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String expectedDate = doc.getRootElement().getChildTextTrim("ExpectedDate");
        try {
            xml = home.create().setIssueAsDispatched(registrationId, libraryId, entryId, expectedDate);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String setIndexTitlePageAsDispatched(org.jdom.Document doc) {
        String xml = "";
        String indexTitleId = doc.getRootElement().getChildTextTrim("IndexTitleId");
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        String expectedDate = doc.getRootElement().getChildTextTrim("ExpectedDate");
        try {
            xml = home.create().setIndexTitlePageAsDispatched(indexTitleId, libraryId, entryId, expectedDate);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String deleteLastRegisteredIssue(org.jdom.Document doc) {
        String xml = "";
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String newPredictionDate = doc.getRootElement().getChildTextTrim("NewPredictionDate");
        try {
            xml = home.create().deleteLastRegisteredIssue(registrationId, subscriptionLibraryId, newPredictionDate);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String deleteRegisteredIssue(org.jdom.Document doc) {
        String xml = "";
        String registrationId = doc.getRootElement().getChildTextTrim("RegistrationId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        try {
            xml = home.create().deleteIssue(registrationId, subscriptionLibraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String deleteRegisteredIssue2(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().deleteIssue2(xmlStr);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String registerDuplicateCopies(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().registerDuplicateIssues(xmlStr);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String generateClaimsManually2(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().generateClaimsManually2(xmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public java.lang.String generateClaimsManually(org.jdom.Document doc) {
        String xml = "";
        String entryId = doc.getRootElement().getChildTextTrim("EntryId");
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String libraryId = doc.getRootElement().getChildTextTrim("LibraryId");
        java.util.List li = doc.getRootElement().getChildren("Registration");
        java.util.Vector vecregis = new java.util.Vector(1, 1);
        for (int i = 0; i < li.size(); i++) {
            org.jdom.Element elereg = (org.jdom.Element) li.get(i);
            String[] stx = new String[7];
            stx[0] = elereg.getChildTextTrim("VolumeNo");
            stx[1] = elereg.getChildTextTrim("IssueNo");
            stx[2] = elereg.getChildTextTrim("IssueYear");
            stx[3] = elereg.getChildTextTrim("IssueDetails");
            stx[4] = elereg.getChildTextTrim("IssueType");
            stx[5] = elereg.getChildTextTrim("Nature");
            stx[6] = elereg.getChildTextTrim("RegistrationId");
            vecregis.addElement(stx);
        }
        try {
            xml = home.create().generateClaimsManually(vecregis, subscriptionId, libraryId, entryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public java.lang.String modifyVolume(org.jdom.Document doc) {
        String xml = "";
        String subscriptionId = doc.getRootElement().getChildTextTrim("SubscriptionId");
        String subscriptionLibraryId = doc.getRootElement().getChildTextTrim("SubscriptionLibraryId");
        String oldVolumeNo = doc.getRootElement().getChildTextTrim("OldVolumeNo");
        String newVolumeNo = doc.getRootElement().getChildTextTrim("NewVolumeNo");
        try {
            xml = home.create().modifyVolume(subscriptionId, subscriptionLibraryId, oldVolumeNo, newVolumeNo);
        } catch (Exception exp) {
        }
        return xml;
    }

    public java.lang.String modifyIssue(String xmlStr) {
        try {
            xmlStr = home.create().modifyIssue(xmlStr);
        } catch (Exception exp) {
        }
        return xmlStr;
    }

    public String getCaptionDetails(org.jdom.Document doc) {
        String xmlStr = "";
        try {
            org.jdom.Element root = doc.getRootElement();
            String catid = "", ownlib = "";
            catid = root.getChildText("catalogueId");
            ownlib = root.getChildText("ownerLibraryId");
            xmlStr = home.create().getCaptionsAndPatterns(catid, ownlib);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlStr;
    }

    public String getLiveAndDormantSubscriptions2(String xmlStr) {
        String xml = "";
        try {
            xml = home.create().getLiveAndDormantSubscriptions2(xmlStr);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    private ejb.bprocess.sm.RegisterSerialHome home;
}
