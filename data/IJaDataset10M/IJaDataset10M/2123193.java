package servlet.sm;

/**
 *
 * @author  Administrator
 */
public class BindingOrderHandler {

    /** Creates a new instance of BindingOrderHandler */
    public BindingOrderHandler(ejb.bprocess.sm.BindingOrderHome home) {
        this.home = home;
    }

    public String getBinders(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryID");
        try {
            xml = home.create().getBinders(libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getPhysicalVolumes(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        try {
            xml = home.create().getPhysicalVolumes(libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getExistingOrders(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        String binderId = doc.getRootElement().getChildText("BinderId");
        try {
            xml = home.create().getExistingOrders(binderId, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getOrderDetails(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        String orderNo = doc.getRootElement().getChildText("OrderNo");
        try {
            xml = home.create().getOrderDetails(orderNo, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String getBindSpecs(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        String subId = doc.getRootElement().getChildText("SubscriptionId");
        try {
            xml = home.create().getBindSpecs(subId, libraryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String saveBindingOrder(org.jdom.Document doc) {
        String xml = "";
        String libraryId = doc.getRootElement().getChildText("LibraryId");
        String binderId = doc.getRootElement().getChildText("BinderId");
        String orderNo = doc.getRootElement().getChildText("OrderNo");
        String orderAmount = doc.getRootElement().getChildText("OrderAmount");
        String entryId = doc.getRootElement().getChildText("EntryId");
        String content = doc.getRootElement().getChildText("Content");
        java.util.List lLibDet = doc.getRootElement().getChildren("LibDetails");
        java.util.List lBudget = doc.getRootElement().getChildren("Budget");
        java.util.Vector vLibDet = new java.util.Vector();
        java.util.Vector vBudget = new java.util.Vector();
        for (int i = 0; i < lLibDet.size(); i++) {
            vLibDet.addElement(((org.jdom.Element) lLibDet.get(i)).getChildText("GroupId"));
            vLibDet.addElement(((org.jdom.Element) lLibDet.get(i)).getChildText("Library"));
            vLibDet.addElement(((org.jdom.Element) lLibDet.get(i)).getChildText("Cost"));
        }
        for (int i = 0; i < lBudget.size(); i++) {
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("Library"));
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("BudgetId"));
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("Amount"));
        }
        try {
            xml = home.create().saveBindingOrder(libraryId, binderId, orderNo, orderAmount, entryId, vLibDet, vBudget, content);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    public String removeVolume(org.jdom.Document doc) {
        String xml = "";
        String orderNo = doc.getRootElement().getChildText("OrderNo");
        String groupId = doc.getRootElement().getChildText("GroupId");
        String groupLibId = doc.getRootElement().getChildText("GroupLibId");
        String bindLibId = doc.getRootElement().getChildText("BindLibId");
        java.util.List lBudget = doc.getRootElement().getChildren("Budget");
        String amount = doc.getRootElement().getChildText("Amount");
        String entryId = doc.getRootElement().getChildText("EntryId");
        java.util.Vector vBudget = new java.util.Vector();
        for (int i = 0; i < lBudget.size(); i++) {
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("TaId"));
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("Library"));
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("BudgetId"));
            vBudget.addElement(((org.jdom.Element) lBudget.get(i)).getChildText("Amount"));
        }
        try {
            xml = home.create().removeVolume(orderNo, groupId, groupLibId, bindLibId, vBudget, amount, entryId);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return xml;
    }

    private ejb.bprocess.sm.BindingOrderHome home;
}
