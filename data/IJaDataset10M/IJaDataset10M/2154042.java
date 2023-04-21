package org.verus.ngl.web.circulation;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.verus.ngl.sl.bprocess.administration.ItemDetails;
import org.verus.ngl.sl.bprocess.technicalprocessing.Document;
import org.verus.ngl.utilities.logging.NGLLogging;
import org.verus.ngl.web.util.NGLUserSession;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @author root
 */
public class BindingItemsComposer extends Window implements Composer {

    @Override
    public void doAfterCompose(Component win) throws Exception {
    }

    public void onClickingGetItemDetails(Event event) {
        try {
            NGLUserSession nGLUserSession = new NGLUserSession();
            String databaseId = nGLUserSession.getDatabaseId();
            String libraryId = nGLUserSession.getLibraryId();
            String itemId = ((Textbox) getFellow("itemId")).getValue();
            Document document = (Document) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("document");
            String status_available = org.verus.ngl.sl.objectmodel.status.DOCUMENT.STATUS_AVAILBLE_IN_LIBRARY;
            String availableStatus = document.bm_getDocAvailableStatus(itemId, new Integer(libraryId), databaseId, status_available);
            NGLLogging.getFineLogger().fine("status of Document======" + availableStatus);
            Grid grid = (Grid) getFellow("itemsAvailableGrid");
            if (availableStatus.equals("true")) {
                if (((Button) getFellow("separateForBinding")).isDisabled()) {
                    ((Button) getFellow("separateForBinding")).setDisabled(false);
                }
                if (!grid.isVisible()) {
                    grid.setVisible(true);
                    ((Label) getFellow("itemDetails")).setVisible(true);
                }
                Row row = buildItemDetails(itemId, new Integer(libraryId), databaseId);
                row.setParent(getFellow("itemsAvailable"));
            } else {
                try {
                    Messagebox.show("Document is not availble in the library !!!", "", Messagebox.OK, "");
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Row buildItemDetails(String itemId, Integer libraryId, String databaseId) {
        ItemDetails itemDetails = (ItemDetails) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("item_details");
        Hashtable hashtable = itemDetails.getItemDetails(itemId, libraryId, databaseId);
        NGLLogging.getFineLogger().fine("======= item no ===========: " + hashtable.get("ItemId").toString());
        NGLLogging.getFineLogger().fine("======= titile ===========: " + hashtable.get("Title").toString());
        NGLLogging.getFineLogger().fine("======= vol no ===========: " + hashtable.get("VolumeNo").toString());
        NGLLogging.getFineLogger().fine("======= author ===========: " + hashtable.get("Author").toString());
        NGLLogging.getFineLogger().fine("======= edtion ===========: " + hashtable.get("Edition").toString());
        NGLLogging.getFineLogger().fine("======= InPrint ===========: " + hashtable.get("InPrint").toString());
        NGLLogging.getFineLogger().fine("======= ISSN ===========: " + hashtable.get("ISSN").toString());
        NGLLogging.getFineLogger().fine("======= ISBN ===========: " + hashtable.get("ISBN").toString());
        NGLLogging.getFineLogger().fine("======= Type ===========: " + hashtable.get("Type").toString());
        NGLLogging.getFineLogger().fine("======= Category ===========: " + hashtable.get("Category").toString());
        Row row = new Row();
        if (hashtable != null && hashtable.size() > 0) {
            Label itemIdLabel = new Label();
            itemIdLabel.setValue(hashtable.get("ItemId").toString());
            itemIdLabel.setParent(row);
            Vbox vbox = new Vbox();
            if (hashtable.get("Title") != null && hashtable.get("Title").toString().length() > 0 && !hashtable.get("Title").toString().equalsIgnoreCase("")) {
                Hbox hbox1 = new Hbox();
                Label titleLabel = new Label(Labels.getLabel("label.Title") + ": ");
                titleLabel.setWidth("50px");
                titleLabel.setMaxlength(10);
                titleLabel.setStyle("font-weight:bold");
                titleLabel.setParent(hbox1);
                Label titleValue = new Label();
                titleValue.setValue(hashtable.get("Title").toString());
                titleValue.setParent(hbox1);
                hbox1.setParent(vbox);
            }
            if (hashtable.get("Author") != null && hashtable.get("Author").toString().length() > 0 && !hashtable.get("Author").toString().equalsIgnoreCase("")) {
                Hbox hbox2 = new Hbox();
                Label authorLabel = new Label(Labels.getLabel("label.Author") + ": ");
                authorLabel.setWidth("50px");
                authorLabel.setMaxlength(10);
                authorLabel.setStyle("font-weight:bold");
                authorLabel.setParent(hbox2);
                Label authorValue = new Label();
                authorValue.setValue(hashtable.get("Author").toString());
                authorValue.setParent(hbox2);
                hbox2.setParent(vbox);
            }
            if (hashtable.get("VolumeNo") != null && hashtable.get("VolumeNo").toString().length() > 0 && !hashtable.get("VolumeNo").toString().equalsIgnoreCase("")) {
                Hbox hbox3 = new Hbox();
                Label volumeLabel = new Label(Labels.getLabel("label.VolumeNo") + ": ");
                volumeLabel.setWidth("50px");
                volumeLabel.setMaxlength(10);
                volumeLabel.setStyle("font-weight:bold");
                volumeLabel.setParent(hbox3);
                Label volumeValue = new Label();
                volumeValue.setValue(hashtable.get("VolumeNo").toString());
                volumeValue.setParent(hbox3);
                hbox3.setParent(vbox);
            }
            if (hashtable.get("Edition") != null && hashtable.get("Edition").toString().length() > 0 && !hashtable.get("Edition").toString().equalsIgnoreCase("")) {
                Hbox hbox4 = new Hbox();
                Label editionLabel = new Label(Labels.getLabel("label.Edition") + ": ");
                editionLabel.setWidth("50px");
                editionLabel.setMaxlength(10);
                editionLabel.setStyle("font-weight:bold");
                editionLabel.setParent(hbox4);
                Label editionValue = new Label();
                editionValue.setValue(hashtable.get("Edition").toString());
                editionValue.setParent(hbox4);
                hbox4.setParent(vbox);
            }
            if (hashtable.get("InPrint") != null && hashtable.get("InPrint").toString().length() > 0 && !hashtable.get("InPrint").toString().equalsIgnoreCase("")) {
                Hbox hbox5 = new Hbox();
                Label imprintLabel = new Label(Labels.getLabel("label.Imprint") + ": ");
                imprintLabel.setWidth("50px");
                imprintLabel.setMaxlength(10);
                imprintLabel.setStyle("font-weight:bold");
                imprintLabel.setParent(hbox5);
                Label imprintValue = new Label();
                imprintValue.setValue(hashtable.get("InPrint").toString());
                imprintValue.setParent(hbox5);
                hbox5.setParent(vbox);
            }
            if (hashtable.get("ISSN") != null && hashtable.get("ISSN").toString().length() > 0 && !hashtable.get("ISSN").toString().equalsIgnoreCase("")) {
                Hbox hbox6 = new Hbox();
                Label issnLabel = new Label(Labels.getLabel("label.ISSN") + ": ");
                issnLabel.setWidth("50px");
                issnLabel.setMaxlength(10);
                issnLabel.setStyle("font-weight:bold");
                issnLabel.setParent(hbox6);
                Label issnValue = new Label();
                issnValue.setValue(hashtable.get("ISSN").toString());
                issnValue.setParent(hbox6);
                hbox6.setParent(vbox);
            }
            if (hashtable.get("ISBN") != null && hashtable.get("ISBN").toString().length() > 0 && !hashtable.get("ISBN").toString().equalsIgnoreCase("")) {
                Hbox hbox7 = new Hbox();
                Label isbnLabel = new Label(Labels.getLabel("label.ISBN") + ": ");
                isbnLabel.setWidth("50px");
                isbnLabel.setMaxlength(10);
                isbnLabel.setStyle("font-weight:bold");
                isbnLabel.setParent(hbox7);
                Label isbnValue = new Label();
                isbnValue.setValue(hashtable.get("ISBN").toString());
                isbnValue.setParent(hbox7);
                hbox7.setParent(vbox);
            }
            vbox.setParent(row);
            Button bnDelete = new Button("", "/images/16/edit-delete.png");
            bnDelete.setAttribute("row", row);
            bnDelete.setAttribute("itemId", itemId);
            bnDelete.addForward("onClick", "", "onDeleteClick");
            bnDelete.setOrient("vertical");
            bnDelete.setParent(row);
        }
        return row;
    }

    public void onDeleteClick(Event event) {
        NGLLogging.getFineLogger().fine("In On Delete Click");
        ForwardEvent fe = (ForwardEvent) event;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        String itemId = comp.getAttribute("itemId").toString();
        NGLUserSession nGLUserSession = new NGLUserSession();
        String databaseId = nGLUserSession.getDatabaseId();
        String libraryId = nGLUserSession.getLibraryId();
        Document document = (Document) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("document");
        String status_available = org.verus.ngl.sl.objectmodel.status.DOCUMENT.STATUS_AVAILBLE_IN_LIBRARY;
        String availableStatus = document.bm_getDocAvailableStatus(itemId, new Integer(libraryId), databaseId, status_available);
        if (availableStatus.equals("true")) {
            ((Rows) getFellow("itemsAvailable")).removeChild(comp.getParent());
            Grid grid = ((Grid) getFellow("itemsAvailableGrid"));
            List rowsCount = ((Rows) getFellow("itemsAvailable")).getChildren();
            if (rowsCount.size() <= 0) {
                grid.setVisible(false);
                ((Label) getFellow("itemDetails")).setVisible(false);
                ((Button) getFellow("separateForBinding")).setDisabled(true);
            }
        } else {
            ((Rows) getFellow("itemsToBeBinded")).removeChild(comp.getParent());
            document.bm_changeStatus(itemId, status_available, new Integer(libraryId), databaseId);
        }
    }

    public void onClickingCancel(Event event) {
        ((Textbox) getFellow("itemId")).setValue(null);
        ((Textbox) getFellow("itemId")).setFocus(true);
        ((Label) getFellow("itemDetails")).setVisible(false);
        Grid grid = ((Grid) getFellow("itemsAvailableGrid"));
        if (grid.isVisible() && grid != null) {
            List rowsCount = ((Rows) getFellow("itemsAvailable")).getChildren();
            for (int i = rowsCount.size() - 1; i >= 0; i--) {
                Component component = (Component) rowsCount.get(i);
                ((Rows) getFellow("itemsAvailable")).removeChild(component);
            }
            grid.setVisible(false);
            ((Label) getFellow("itemDetails")).setVisible(false);
            ((Button) getFellow("separateForBinding")).setDisabled(true);
        }
    }

    public void onOpenItemsSeparatedForBinding(Event event) {
        try {
            NGLUserSession nGLUserSession = new NGLUserSession();
            String databaseId = nGLUserSession.getDatabaseId();
            String libraryId = nGLUserSession.getLibraryId();
            Document document = (Document) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("document");
            String status_separated = org.verus.ngl.sl.objectmodel.status.DOCUMENT.STATUS_SEPARATED_FOR_BINDING;
            Vector v = document.bm_getSeparatedForBindingItems(new Integer(libraryId), databaseId, status_separated);
            Grid grid = ((Grid) getFellow("itemsAvailableGrid"));
            if (((Groupbox) getFellow("itemsForBinding")).isOpen()) {
                clearGridData(event);
                for (int i = 0; i < v.size(); i++) {
                    String itemId = v.get(i).toString();
                    Row row = buildItemDetails(itemId, new Integer(libraryId), databaseId);
                    row.setParent(getFellow("itemsToBeBinded"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickingSeparateForBinding(Event event) {
        int retValue = 0;
        NGLLogging.getFineLogger().fine("in Onclicking separate for binding========");
        NGLUserSession nGLUserSession = new NGLUserSession();
        String databaseId = nGLUserSession.getDatabaseId();
        String libraryId = nGLUserSession.getLibraryId();
        Document document = (Document) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("document");
        String status_separated = org.verus.ngl.sl.objectmodel.status.DOCUMENT.STATUS_SEPARATED_FOR_BINDING;
        Grid grid = ((Grid) getFellow("itemsAvailableGrid"));
        if (grid.isVisible() && grid != null) {
            List rowsCount = ((Rows) getFellow("itemsAvailable")).getChildren();
            for (int i = rowsCount.size() - 1; i >= 0; i--) {
                Row row = (Row) rowsCount.get(i);
                Label lbl = (Label) row.getFirstChild();
                String itemId = lbl.getValue();
                int changeStatus = document.bm_changeStatus(itemId, status_separated, new Integer(libraryId), databaseId);
                if (changeStatus == -1) {
                    retValue = -1;
                    break;
                }
                ((Rows) getFellow("itemsAvailable")).removeChild(row);
            }
            if (retValue == 0) {
                onOpenItemsSeparatedForBinding(event);
                grid.setVisible(false);
                ((Label) getFellow("itemDetails")).setVisible(false);
                ((Button) getFellow("separateForBinding")).setDisabled(true);
            } else {
                try {
                    int val = Messagebox.show("Unable to complete the transaction ", "Information", Messagebox.CANCEL, Messagebox.ERROR);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
    }

    public void clearGridData(Event evt) {
        Grid grid = ((Grid) getFellow("itemsForBindingGrid"));
        if (grid != null) {
            List rowsCount = ((Rows) getFellow("itemsToBeBinded")).getChildren();
            for (int i = rowsCount.size() - 1; i >= 0; i--) {
                Component component = (Component) rowsCount.get(i);
                ((Rows) getFellow("itemsToBeBinded")).removeChild(component);
            }
        }
    }
}
