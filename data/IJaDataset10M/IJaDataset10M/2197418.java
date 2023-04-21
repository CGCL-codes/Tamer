package servlet.sm;

/**
 *
 * @author  Administrator
 */
public class SubscriptionHandler {

    /** Creates a new instance of SubscriptionHandler */
    public SubscriptionHandler(ejb.bprocess.sm.SubscriptionHome home) {
        this.home = home;
    }

    public String retrieveAllBudgets(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String libraryId = rootele.getChild("LibraryId").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.retrieveAllBudgets(libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String retrieveAllBudgets2(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String libraryId = rootele.getChild("LibraryId").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.retrieveAllBudgets2(libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getSubscriptionDetails(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String libraryId = rootele.getChild("LibraryId").getText();
        String status = rootele.getChild("Status").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.getSubscriptionDetails(libraryId, status);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getSubscriptionDetails2(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String libraryId = rootele.getChild("LibraryId").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.getSubscriptionDetails2(libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getSubscribedBudgets(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionId = rootele.getChild("SubscriptionId").getText();
        String libraryId = rootele.getChild("LibraryId").getText();
        String status = rootele.getChild("Status").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.getSubscribedBudgets(subscriptionId, libraryId, status);
            if (xml.trim().equals("NOBUDGET")) {
                xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response>NOBUDGET</Response>";
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getSubscriptionSelectedBudgets(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionId = rootele.getChild("SubscriptionId").getText();
        String libraryId = rootele.getChild("LibraryID").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.getBudgetsForSelected(subscriptionId, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getSubscribedLibraries(org.jdom.Document doc) {
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionId = rootele.getChild("SubscriptionId").getText();
        String libraryId = rootele.getChild("LibraryId").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.getSubscribedLibraries(subscriptionId, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String saveSubscription(org.jdom.Document doc) {
        java.util.Hashtable htSubLib = new java.util.Hashtable();
        java.util.Hashtable htBudgetLib = new java.util.Hashtable();
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionLibraryId = rootele.getChild("SubscriptionLibraryId").getText();
        java.util.List vec = (java.util.List) rootele.getChildren("LibRec");
        org.jdom.Element element = null;
        for (int i = 0; i < vec.size(); i++) {
            element = (org.jdom.Element) vec.get(i);
            org.jdom.Element element1 = null;
            htSubLib.put(element.getText(), element.getChild("Copies").getText());
            java.util.List vec1 = (java.util.List) element.getChildren("BudgetList");
            java.util.Vector vec2 = new java.util.Vector();
            for (int j = 0; j < vec1.size(); j++) {
                element1 = (org.jdom.Element) vec1.get(j);
                vec2.add(element1.getText());
            }
            htBudgetLib.put(element.getText(), vec2);
        }
        String catalogueRecordId = rootele.getChild("CatalogueRecordId").getText();
        String title = rootele.getChild("Title").getText();
        String issn = rootele.getChild("Issn").getText();
        String publisher = rootele.getChild("Publisher").getText();
        String ownerLibraryId = rootele.getChild("OwnerLibraryId").getText();
        String skipOrder = rootele.getChild("SkipOrder").getText();
        String entryId = rootele.getChild("EntryId").getText();
        String status = rootele.getChild("Status").getText();
        System.out.println("Status in handler==" + status);
        System.out.println("skipOrder in handler===" + skipOrder);
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.saveSubscription(subscriptionLibraryId, catalogueRecordId, ownerLibraryId, htSubLib, htBudgetLib, entryId, status, title, issn, publisher, skipOrder);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String saveSubscription2(org.jdom.Document doc) {
        java.util.Hashtable htSubLib = new java.util.Hashtable();
        java.util.Hashtable htBudgetLib = new java.util.Hashtable();
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionLibraryId = rootele.getChild("SubscriptionLibraryId").getText();
        java.util.List vec = (java.util.List) rootele.getChildren("LibRec");
        org.jdom.Element element = null;
        for (int i = 0; i < vec.size(); i++) {
            element = (org.jdom.Element) vec.get(i);
            org.jdom.Element element1 = null;
            htSubLib.put(element.getText(), element.getChild("Copies").getText());
            java.util.List vec1 = (java.util.List) element.getChildren("BudgetList");
            java.util.Vector vec2 = new java.util.Vector();
            for (int j = 0; j < vec1.size(); j++) {
                element1 = (org.jdom.Element) vec1.get(j);
                vec2.add(element1.getText());
            }
            htBudgetLib.put(element.getText(), vec2);
        }
        String catalogueRecordId = rootele.getChild("CatalogueRecordId").getText();
        String title = rootele.getChild("Title").getText();
        String issn = rootele.getChild("Issn").getText();
        String publisher = rootele.getChild("Publisher").getText();
        String StartYear = "";
        String GratisStatus = rootele.getChild("GratiseStatus").getText();
        String GratisText = rootele.getChild("GratiseText").getText();
        String ownerLibraryId = rootele.getChild("OwnerLibraryId").getText();
        String skipOrder = rootele.getChild("SkipOrder").getText();
        String entryId = rootele.getChild("EntryId").getText();
        String status = rootele.getChild("Status").getText();
        System.out.println("Status in handler==" + status);
        System.out.println("skipOrder in handler===" + skipOrder);
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.saveSubscription2(subscriptionLibraryId, catalogueRecordId, ownerLibraryId, htSubLib, htBudgetLib, entryId, status, title, issn, publisher, skipOrder, StartYear, GratisStatus, GratisText);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String modifySubscription(org.jdom.Document doc) {
        java.util.Hashtable htSubLib = new java.util.Hashtable();
        java.util.Hashtable htBudgetLib = new java.util.Hashtable();
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionLibraryId = rootele.getChild("SubscriptionLibraryId").getText();
        java.util.List vec = (java.util.List) rootele.getChildren("LibRec");
        org.jdom.Element element = null;
        for (int i = 0; i < vec.size(); i++) {
            element = (org.jdom.Element) vec.get(i);
            System.out.println("MadhulikaLibrary....." + element.getText());
            System.out.println("MadhulikaCopies....." + element.getChild("Copies").getText());
            org.jdom.Element element1 = null;
            htSubLib.put(element.getText(), element.getChild("Copies").getText());
            java.util.List vec1 = (java.util.List) element.getChildren("BudgetList");
            java.util.Vector vec2 = new java.util.Vector();
            for (int j = 0; j < vec1.size(); j++) {
                element1 = (org.jdom.Element) vec1.get(j);
                vec2.add(element1.getText());
            }
            htBudgetLib.put(element.getText(), vec2);
        }
        String catalogueRecordId = rootele.getChild("CatalogueRecordId").getText();
        String title = rootele.getChild("Title").getText();
        String issn = rootele.getChild("Issn").getText();
        String publisher = rootele.getChild("Publisher").getText();
        String ownerLibraryId = rootele.getChild("OwnerLibraryId").getText();
        String subscriptionId = rootele.getChild("SubscriptionId").getText();
        String entryId = rootele.getChild("EntryId").getText();
        String status = rootele.getChild("Status").getText();
        String skiporder = rootele.getChild("SkipOrder").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.modifySubscription(subscriptionLibraryId, catalogueRecordId, ownerLibraryId, htSubLib, htBudgetLib, entryId, status, title, issn, publisher, subscriptionId, skiporder);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String modifySubscription2(org.jdom.Document doc) {
        java.util.Hashtable htSubLib = new java.util.Hashtable();
        java.util.Hashtable htBudgetLib = new java.util.Hashtable();
        String xml = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionLibraryId = rootele.getChild("SubscriptionLibraryId").getText();
        java.util.List vec = (java.util.List) rootele.getChildren("LibRec");
        org.jdom.Element element = null;
        for (int i = 0; i < vec.size(); i++) {
            element = (org.jdom.Element) vec.get(i);
            System.out.println("MadhulikaLibrary....." + element.getText());
            System.out.println("MadhulikaCopies....." + element.getChild("Copies").getText());
            org.jdom.Element element1 = null;
            htSubLib.put(element.getText(), element.getChild("Copies").getText());
            java.util.List vec1 = (java.util.List) element.getChildren("BudgetList");
            java.util.Vector vec2 = new java.util.Vector();
            for (int j = 0; j < vec1.size(); j++) {
                element1 = (org.jdom.Element) vec1.get(j);
                vec2.add(element1.getText());
            }
            htBudgetLib.put(element.getText(), vec2);
        }
        String catalogueRecordId = rootele.getChild("CatalogueRecordId").getText();
        String title = rootele.getChild("Title").getText();
        String issn = rootele.getChild("Issn").getText();
        String publisher = rootele.getChild("Publisher").getText();
        String StartYear = "";
        String GratisStatus = rootele.getChild("GratiseStatus").getText();
        String GratisText = rootele.getChild("GratiseText").getText();
        String ownerLibraryId = rootele.getChild("OwnerLibraryId").getText();
        String subscriptionId = rootele.getChild("SubscriptionId").getText();
        String entryId = rootele.getChild("EntryId").getText();
        String status = rootele.getChild("Status").getText();
        String skiporder = rootele.getChild("SkipOrder").getText();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xml = subscription.modifySubscription2(subscriptionLibraryId, catalogueRecordId, ownerLibraryId, htSubLib, htBudgetLib, entryId, status, title, issn, publisher, subscriptionId, skiporder, GratisText, GratisStatus, StartYear);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String deleteSubscriptionSelected(org.jdom.Document doc) {
        String xmlStr = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionLibraryId = rootele.getChildText("LibraryID").toString();
        String subscriptionId = rootele.getChildText("SubscriptionId").toString();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xmlStr = subscription.deleteSubscription(subscriptionLibraryId, subscriptionId);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        return xmlStr;
    }

    public String deleteSubscriptionSelected2(org.jdom.Document doc) {
        String xmlStr = "";
        org.jdom.Element rootele = doc.getRootElement();
        String subscriptionLibraryId = rootele.getChildText("LibraryID").toString();
        String subscriptionId = rootele.getChildText("SubscriptionId").toString();
        try {
            ejb.bprocess.sm.Subscription subscription = home.create();
            xmlStr = subscription.deleteSubscription2(subscriptionLibraryId, subscriptionId);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        return xmlStr;
    }

    private ejb.bprocess.sm.SubscriptionHome home = null;
}
