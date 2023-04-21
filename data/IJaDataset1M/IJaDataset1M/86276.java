package org.verus.ngl.web.circulation;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.verus.ngl.sl.bprocess.administration.Dept;
import org.verus.ngl.sl.bprocess.administration.FormLetterComponent;
import org.verus.ngl.sl.bprocess.administration.FormLetterGeneration;
import org.verus.ngl.sl.bprocess.administration.FormLetterTemplateNames;
import org.verus.ngl.sl.bprocess.administration.ItemDetails;
import org.verus.ngl.sl.bprocess.administration.Patron;
import org.verus.ngl.sl.bprocess.administration.PatronCategory;
import org.verus.ngl.sl.objectmodel.administration.PATRON_CATEGORY;
import org.verus.ngl.sl.objectmodel.status.NGLStatusConstants;
import org.verus.ngl.web.util.NGLUserSession;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.verus.ngl.sl.utilities.CirculationUtility;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLUtility;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.verus.ngl.utilities.logging.NGLLogging;
import org.verus.ngl.web.beans.administration.FormLetterComposer;
import org.verus.ngl.web.beans.administration.UserDetailsComposer;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class CheckInComposer extends Window implements Composer {

    public CheckInComposer() {
    }

    public void onClickingGetUserItemDetails(Event event) {
        try {
            NGLUserSession nGLUserSession = new NGLUserSession();
            String databaseId = nGLUserSession.getDatabaseId();
            String libraryId = nGLUserSession.getLibraryId();
            String itemId = ((Textbox) getFellow("itemId")).getValue();
            if (itemId != null && itemId.trim().length() > 0 && !itemId.equalsIgnoreCase("")) {
                CirculationUtility circulationUtility = (CirculationUtility) NGLBeanFactory.getInstance().getBean("circulationUtility");
                Hashtable ht = new Hashtable();
                ht = circulationUtility.calculateOverDue(itemId, new Integer(libraryId), databaseId);
                String patronId = ht.get("patronId").toString();
                Integer patronLibraryId = (Integer) ht.get("patronLibraryId");
                Double overDue = (Double) ht.get("overdue");
                String dueDate = ht.get("dueDate").toString();
                String taDate = ht.get("taDate").toString();
                String today_date = ht.get("todayDate").toString();
                String accessionNumber = ht.get("accessionNumber").toString();
                int transactions = (Integer) ht.get("transactions");
                Boolean reserved = (Boolean) ht.get("reserved");
                Vector reservedList = (Vector) ht.get("reservedList");
                Boolean documentStatus = (Boolean) ht.get("document_status");
                Boolean ontheflyStatus = (Boolean) ht.get("onthefly_status");
                NGLLogging.getFineLogger().fine("======In composer on the fly status======" + ontheflyStatus);
                NGLLogging.getFineLogger().fine("======In composer document status======" + documentStatus);
                int retUserVal = 0;
                if (documentStatus.equals(true)) {
                    if (transactions == 1) {
                        ((Button) getFellow("checkIn")).setDisabled(false);
                        boolean flag = false;
                        if (this.getAttribute("patron_id") != null) {
                            if (this.getAttribute("onthefly_patron_id") != null) {
                                if (this.getAttribute("onthefly_patron_id").equals(patronId)) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            } else if (this.getAttribute("patron_id").equals(patronId)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        } else {
                            if (this.getAttribute("onthefly_patron_id") != null) {
                                if (this.getAttribute("onthefly_patron_id").equals(patronId)) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            } else {
                                flag = true;
                            }
                        }
                        if (((Vbox) getFellow("userBox")).getFellowIfAny("userDetarils") == null && flag == true) {
                            NGLLogging.getFineLogger().fine("Patron id===========" + patronId);
                            NGLLogging.getFineLogger().fine("###########in creation of userdetails composer loop#############");
                            UserDetailsComposer userDetailsComposer = (UserDetailsComposer) Executions.createComponents("/jsp/administration/UserDetails.zul", event.getTarget(), null);
                            Hashtable ht_user = new Hashtable();
                            ht_user = userDetailsComposer.onloadPage(patronId, patronLibraryId);
                            retUserVal = (Integer) ht_user.get("retVal");
                            NGLLogging.getFineLogger().fine("ret user val=======" + retUserVal);
                            if (retUserVal == 1) {
                                userDetailsComposer.detach();
                                Messagebox mb = new Messagebox();
                                try {
                                    mb.show("Not a valid User !!!", "", Messagebox.OK, "");
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            } else {
                                Vbox userBox = (Vbox) getFellow("userBox");
                                userDetailsComposer.setParent(userBox);
                            }
                        }
                        if (((((Vbox) getFellow("userBox")).getFellowIfAny("userDetarils")) != null)) {
                            if (flag == true) {
                                if (!((Label) getFellow("itemDetails")).isVisible()) {
                                    ((Label) getFellow("itemDetails")).setVisible(true);
                                }
                                NGLLogging.getFineLogger().fine("status of userBox " + getFellow("userBox").isVisible());
                                Grid grid = new Grid();
                                grid = (Grid) getFellowIfAny("gridItemDetails");
                                grid.setVisible(true);
                                ItemDetails itemDetails = (ItemDetails) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("item_details");
                                Hashtable hashtable = itemDetails.getItemDetails(itemId, new Integer(libraryId), databaseId);
                                String item_id = hashtable.get("ItemId").toString();
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
                                if (hashtable != null && hashtable.size() > 0) {
                                    Row row = new Row();
                                    row.setId("row" + itemId);
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
                                    Label typeLabel = new Label();
                                    typeLabel.setValue(hashtable.get("Type").toString());
                                    typeLabel.setParent(row);
                                    Label categoryLabel = new Label();
                                    categoryLabel.setValue(hashtable.get("Category").toString());
                                    categoryLabel.setParent(row);
                                    Vbox dates = new Vbox();
                                    Label checkedOutDateLabel = new Label(Labels.getLabel("label.CheckedOutOn") + ": ");
                                    Label checkedOutDateValue = new Label();
                                    checkedOutDateValue.setValue(taDate);
                                    Hbox checkedOut = new Hbox();
                                    checkedOutDateLabel.setParent(checkedOut);
                                    checkedOutDateValue.setParent(checkedOut);
                                    checkedOut.setParent(dates);
                                    dates.setParent(row);
                                    Label dueDateLabel = new Label(Labels.getLabel("label.DueDate") + ": ");
                                    Label dueDateValue = new Label();
                                    dueDateValue.setValue(dueDate);
                                    Hbox dueBox = new Hbox();
                                    dueDateLabel.setParent(dueBox);
                                    dueDateValue.setParent(dueBox);
                                    dueBox.setParent(dates);
                                    dates.setParent(row);
                                    Label todayDateLabel = new Label(Labels.getLabel("label.TodayDate") + ": ");
                                    Label todayDateValue = new Label();
                                    todayDateValue.setValue(today_date);
                                    Hbox todayBox = new Hbox();
                                    todayDateLabel.setParent(todayBox);
                                    todayDateValue.setParent(todayBox);
                                    todayBox.setParent(dates);
                                    dates.setParent(row);
                                    Label overDueLabel = new Label();
                                    Label overDueValue = new Label();
                                    Vbox overdue = new Vbox();
                                    Hbox hbox = new Hbox();
                                    Hbox hbox1 = new Hbox();
                                    if (!overDue.toString().equals("")) {
                                        String str = overDue.toString();
                                        String subStr = str.substring(str.indexOf(".") + 1, str.length());
                                        NGLLogging.getFineLogger().fine("*********>>>>>>>>>Sub String of overdue>>>$$$$%%%%%%" + subStr);
                                        if (subStr.length() > 2) {
                                            str = str.substring(0, str.indexOf(".") + 3);
                                        }
                                        overDueLabel.setValue(Labels.getLabel("label.OverDue") + " :");
                                        overDueValue.setValue(str);
                                        overDueValue.setId("overDue" + itemId);
                                        overDueLabel.setParent(hbox);
                                        overDueValue.setParent(hbox);
                                        hbox.setParent(overdue);
                                        if (overDue > 0) {
                                            Vbox vbox1 = new Vbox();
                                            Toolbarbutton override = new Toolbarbutton(Labels.getLabel("label.Override"));
                                            override.setAttribute("overdue", str);
                                            override.setAttribute("vbox1", vbox1);
                                            override.setAttribute("override", override);
                                            override.setAttribute("itemId", itemId);
                                            override.addForward("onClick", "", "onClickingNewOverDue");
                                            override.setParent(vbox1);
                                            vbox1.setParent(overdue);
                                            Label overDuePaidLabel = new Label(Labels.getLabel("label.OverDuePaid") + ":");
                                            Textbox overDuePaidValue = new Textbox();
                                            overDuePaidLabel.setParent(hbox1);
                                            overDuePaidValue.setParent(hbox1);
                                            overDuePaidValue.setWidth("70px");
                                            overDuePaidValue.setValue("0.0");
                                            overDuePaidValue.setId("overDuePaid" + itemId);
                                            hbox1.setParent(overdue);
                                        }
                                        overdue.setParent(row);
                                        if (reserved == true) {
                                            Vbox reservedBox = new Vbox();
                                            Toolbarbutton reservationDetails = new Toolbarbutton(Labels.getLabel("label.ReservationDetails"));
                                            reservationDetails.addForward("onClick", "", "onClickingReservationDetails");
                                            reservationDetails.setAttribute("reserved_list", reservedList);
                                            Label reservedValue = new Label(Labels.getLabel("message.Reserved"));
                                            reservedValue.setStyle("color:red");
                                            reservedValue.setParent(reservedBox);
                                            reservationDetails.setParent(reservedBox);
                                            reservedBox.setParent(row);
                                        } else {
                                            Label notReservedValue = new Label();
                                            notReservedValue.setValue(Labels.getLabel("message.ReturnToStacks"));
                                            notReservedValue.setParent(row);
                                        }
                                    } else {
                                        overDueValue.setValue(Labels.getLabel("message.CouldNotBeDetermined"));
                                        overDueValue.setParent(row);
                                    }
                                    Button bt = new Button("", "/images/16/edit-delete.png");
                                    bt.addForward("onClick", "", "onDeleteClick");
                                    bt.setAttribute("item_id", itemId);
                                    bt.setAttribute("row", row);
                                    bt.setParent(row);
                                    row.setParent(getFellow("rows"));
                                } else {
                                    NGLLogging.getFineLogger().fine("item details r not available");
                                }
                                this.setAttribute("patron_id", patronId);
                            } else {
                                Messagebox mb = new Messagebox();
                                try {
                                    mb.show("This item is checked out by another user...pls check-in the items checked out by this user only !!!", "", Messagebox.OK, Messagebox.INFORMATION);
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            }
                        }
                    } else if (transactions == 0) {
                        Messagebox mb = new Messagebox();
                        try {
                            mb.show("Document is availble in the library !!!", "", Messagebox.OK, "");
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    } else {
                        Messagebox mb = new Messagebox();
                        try {
                            mb.show("Maliculous Transaction found !!!", "", Messagebox.OK, "");
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                } else {
                    if (ontheflyStatus.equals(true)) {
                        if (((Button) getFellow("checkIn")).isDisabled()) {
                            ((Button) getFellow("checkIn")).setDisabled(false);
                        }
                        Hashtable hashtable = circulationUtility.bm_getOnTheFlyItem(itemId, new Integer(libraryId), databaseId);
                        String onthefly_patron_id = hashtable.get("patronId").toString();
                        Integer onthefly_patron_libraryId = (Integer) hashtable.get("patronLibraryId");
                        NGLLogging.getFineLogger().fine("On the fly patron id=======" + onthefly_patron_id);
                        boolean flag = false;
                        if (this.getAttribute("onthefly_patron_id") != null) {
                            if (this.getAttribute("onthefly_patron_id").equals(onthefly_patron_id)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        } else {
                            if (this.getAttribute("patron_id") != null) {
                                if (this.getAttribute("patron_id").equals(onthefly_patron_id)) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            } else {
                                flag = true;
                            }
                        }
                        if (((Vbox) getFellow("userBox")).getFellowIfAny("userDetarils") == null && flag == true) {
                            NGLLogging.getFineLogger().fine("Patron id===========" + onthefly_patron_id);
                            NGLLogging.getFineLogger().fine("###########in creation of userdetails composer loop#############");
                            UserDetailsComposer userDetailsComposer = (UserDetailsComposer) Executions.createComponents("/jsp/administration/UserDetails.zul", event.getTarget(), null);
                            Hashtable ht_user = userDetailsComposer.onloadPage(onthefly_patron_id, onthefly_patron_libraryId);
                            retUserVal = (Integer) ht_user.get("retVal");
                            if (retUserVal == 1) {
                                userDetailsComposer.detach();
                                Messagebox mb = new Messagebox();
                                try {
                                    mb.show("Not a valid User !!!", "", Messagebox.OK, "");
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            } else {
                                Vbox userBox = (Vbox) getFellow("userBox");
                                userDetailsComposer.setParent(userBox);
                            }
                        }
                        if (((((Vbox) getFellow("userBox")).getFellowIfAny("userDetarils")) != null)) {
                            if (flag == true) {
                                if (!((Label) getFellow("onTheFlyItems")).isVisible()) {
                                    ((Label) getFellow("onTheFlyItems")).setVisible(true);
                                }
                                Grid grid1 = new Grid();
                                grid1 = (Grid) getFellowIfAny("gridOnTheFlyItems");
                                grid1.setVisible(true);
                                NGLLogging.getFineLogger().fine("======= ON THE FLY ITEMS ===========: ");
                                NGLLogging.getFineLogger().fine("======= titile ===========: " + hashtable.get("Title").toString());
                                NGLLogging.getFineLogger().fine("======= author ===========: " + hashtable.get("Author").toString());
                                NGLLogging.getFineLogger().fine("======= edtion ===========: " + hashtable.get("Edition").toString());
                                NGLLogging.getFineLogger().fine("======= InPrint ===========: " + hashtable.get("ImPrint").toString());
                                NGLLogging.getFineLogger().fine("======= ISBN ===========: " + hashtable.get("ISBN").toString());
                                NGLLogging.getFineLogger().fine("======= Patron Id===========: " + onthefly_patron_id);
                                Row row = new Row();
                                if (hashtable != null && hashtable.size() > 0) {
                                    row.setId("row" + itemId);
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
                                    if (hashtable.get("ImPrint") != null && hashtable.get("ImPrint").toString().length() > 0 && !hashtable.get("ImPrint").toString().equalsIgnoreCase("")) {
                                        Hbox hbox5 = new Hbox();
                                        Label imprintLabel = new Label(Labels.getLabel("label.Imprint") + ": ");
                                        imprintLabel.setWidth("50px");
                                        imprintLabel.setMaxlength(10);
                                        imprintLabel.setStyle("font-weight:bold");
                                        imprintLabel.setParent(hbox5);
                                        Label imprintValue = new Label();
                                        imprintValue.setValue(hashtable.get("ImPrint").toString());
                                        imprintValue.setParent(hbox5);
                                        hbox5.setParent(vbox);
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
                                    bnDelete.addForward("onClick", "", "onDeleteClickOntheFlyItem");
                                    bnDelete.setOrient("vertical");
                                    bnDelete.setParent(row);
                                    row.setParent(getFellow("rows1"));
                                }
                                this.setAttribute("onthefly_patron_id", onthefly_patron_id);
                            } else {
                                Messagebox mb = new Messagebox();
                                try {
                                    mb.show("This item is checked out by another user...pls check-in the items checked out by this user only !!!", "", Messagebox.OK, Messagebox.INFORMATION);
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            }
                        }
                    } else {
                        Messagebox mb = new Messagebox();
                        try {
                            mb.show("Not a valid Document !!!", "", Messagebox.OK, "");
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    Messagebox.show("Please enter the Item Id to be Checked In...", "", Messagebox.OK, Messagebox.INFORMATION);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                ((Textbox) getFellow("itemId")).setFocus(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickingNewOverDue(Event evt) {
        NGLLogging.getFineLogger().fine("in onclicking over due");
        ForwardEvent fe = (ForwardEvent) evt;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        String overDue = (String) comp.getAttribute("overdue");
        String itemId = (String) comp.getAttribute("itemId");
        Hbox newOverDue = new Hbox();
        Label newOverDueLabel = new Label(Labels.getLabel("label.NewOverDue"));
        Textbox newOverDueValue = new Textbox(overDue);
        newOverDueLabel.setParent(newOverDue);
        newOverDueValue.setParent(newOverDue);
        newOverDueValue.setWidth("70px");
        newOverDueValue.setId("newOverDue" + itemId);
        newOverDue.setParent((Vbox) comp.getAttribute("vbox1"));
        ((Toolbarbutton) comp.getAttribute("override")).setDisabled(true);
    }

    public void onClickingReservationDetails(Event evt) {
        NGLLogging.getFineLogger().fine("in onclicking resevation details");
        try {
            ForwardEvent fe = (ForwardEvent) evt;
            MouseEvent me = (MouseEvent) fe.getOrigin();
            Component comp = me.getTarget();
            Vector reservedList = (Vector) comp.getAttribute("reserved_list");
            Window win1 = new Window();
            win1.setWidth("600px");
            win1.setTitle(Labels.getLabel("label.ReservationDetails"));
            Grid gd = new Grid();
            Columns columns = new Columns();
            Column col1 = new Column(Labels.getLabel("label.UserId"));
            col1.setWidth("100px");
            Column col2 = new Column(Labels.getLabel("label.UserDetails"));
            col2.setWidth("300px");
            Column col3 = new Column(Labels.getLabel("label.QueueNo"));
            col3.setWidth("100px");
            col1.setParent(columns);
            col2.setParent(columns);
            col3.setParent(columns);
            columns.setParent(gd);
            Rows rows = new Rows();
            for (int i = 0; i < reservedList.size(); i++) {
                Hashtable reservedDetails = (Hashtable) reservedList.get(i);
                String patron_id = reservedDetails.get("patron_id").toString();
                Integer library_id = (Integer) reservedDetails.get("library_id");
                Integer queue_no = (Integer) reservedDetails.get("queue_no");
                NGLUserSession nGLUserSession = new NGLUserSession();
                String databaseId = nGLUserSession.getDatabaseId();
                Patron patron = (Patron) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron");
                String[] patron_name_id = patron.getPatronIdPatronName(patron_id, library_id, databaseId);
                PatronCategory patronCategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patronCategory");
                Object object = patronCategory.getPatronCategory(Integer.parseInt(patron_name_id[2]), library_id, databaseId);
                PATRON_CATEGORY patron_category = (PATRON_CATEGORY) object;
                String patronCategoryName = patron_category.getPatronCategoryName();
                Dept dept = (Dept) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("dept");
                String deptName = dept.getDeptName(Integer.parseInt(patron_name_id[3]), library_id, databaseId);
                Row row = new Row();
                Label user_id = new Label();
                user_id.setValue(patron_id);
                user_id.setParent(row);
                Vbox vbox = new Vbox();
                Hbox hbox1 = new Hbox();
                Label nameLabel = new Label(Labels.getLabel("label.Name") + ": ");
                nameLabel.setWidth("50px");
                nameLabel.setMaxlength(10);
                nameLabel.setStyle("font-weight:bold");
                nameLabel.setParent(hbox1);
                Label nameValue = new Label();
                nameValue.setValue(patron_name_id[1]);
                nameValue.setParent(hbox1);
                hbox1.setParent(vbox);
                Hbox hbox2 = new Hbox();
                Label categoryLabel = new Label(Labels.getLabel("label.Category") + ": ");
                categoryLabel.setWidth("50px");
                categoryLabel.setMaxlength(10);
                categoryLabel.setStyle("font-weight:bold");
                categoryLabel.setParent(hbox2);
                Label categoryValue = new Label();
                categoryValue.setValue(patronCategoryName);
                categoryValue.setParent(hbox2);
                hbox2.setParent(vbox);
                Hbox hbox3 = new Hbox();
                Label departmentLabel = new Label(Labels.getLabel("label.Department") + ": ");
                departmentLabel.setWidth("50px");
                departmentLabel.setMaxlength(10);
                departmentLabel.setStyle("font-weight:bold");
                departmentLabel.setParent(hbox3);
                Label departmentValue = new Label();
                departmentValue.setValue(deptName);
                departmentValue.setParent(hbox3);
                hbox3.setParent(vbox);
                vbox.setParent(row);
                Label q_no = new Label();
                if (queue_no == 1) {
                    q_no.setValue(queue_no + "(" + Labels.getLabel("message.WillNowBeIntimated") + ")");
                } else {
                    q_no.setValue(queue_no + "");
                }
                q_no.setParent(row);
                row.setParent(rows);
            }
            rows.setParent(gd);
            gd.setParent(win1);
            win1.setParent(this);
            win1.setMaximizable(true);
            win1.setSizable(true);
            win1.setClosable(true);
            try {
                win1.doModal();
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeleteClick(Event event) {
        NGLLogging.getFineLogger().fine("In On Delete Click");
        ForwardEvent fe = (ForwardEvent) event;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        int val = Messagebox.CANCEL;
        try {
            val = Messagebox.show("Are you sure you want to delete this item?", "", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, Messagebox.OK);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        if (val == Messagebox.OK) {
            ((Rows) getFellow("rows")).removeChild(comp.getParent());
            List itemCount = ((Rows) getFellow("rows")).getChildren();
            NGLLogging.getFineLogger().fine("ItemCount=====:" + itemCount.size());
            if (itemCount.size() <= 0) {
                ((Grid) getFellow("gridItemDetails")).setVisible(false);
                ((Label) getFellow("itemDetails")).setVisible(false);
            }
        }
    }

    public void onDeleteClickOntheFlyItem(Event event) {
        NGLLogging.getFineLogger().fine("In On Delete Click");
        ForwardEvent fe = (ForwardEvent) event;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        int val = Messagebox.CANCEL;
        try {
            val = Messagebox.show("Are you sure you want to delete this item?", "", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, Messagebox.OK);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        if (val == Messagebox.OK) {
            ((Rows) getFellow("rows1")).removeChild(comp.getParent());
            List ontheflyItemCount = ((Rows) getFellow("rows1")).getChildren();
            NGLLogging.getFineLogger().fine("ontheflyItemCount=====:" + ontheflyItemCount.size());
            if (ontheflyItemCount.size() <= 0) {
                ((Grid) getFellow("gridOnTheFlyItems")).setVisible(false);
                ((Label) getFellow("onTheFlyItems")).setVisible(false);
            }
        }
    }

    public void onClickingRemove(Event event) {
        this.getChildren().clear();
    }

    public void onClickingCancel(Event event) {
        refreshWindow(event);
    }

    public void createComponents(Event evt) {
        Label itemIdLabel = new Label(Labels.getLabel("label.ItemId"));
        Textbox itemId = new Textbox();
        itemId.setId("itemId");
        Button btn = new Button(Labels.getLabel("label.Get"));
        btn.addForward("onClick", "", "onClickingGetUserItemDetails");
        Label msg = new Label();
        msg.setId("message");
        msg.setVisible(false);
        Hbox hbox1 = new Hbox();
        itemIdLabel.setParent(hbox1);
        itemId.setParent(hbox1);
        btn.setParent(hbox1);
        msg.setParent(hbox1);
        Separator s = new Separator();
        Button checkIn = new Button(Labels.getLabel("label.CheckIn"));
        checkIn.addForward("onClick", "", "onClickingCheckIn");
        Button cancel = new Button(Labels.getLabel("label.Cancel"));
        cancel.addForward("onClick", "", "onClickingCancel");
        Hbox hbox2 = new Hbox();
        checkIn.setParent(hbox2);
        cancel.setParent(hbox2);
        hbox1.setParent(this);
        Vbox vbox = new Vbox();
        vbox.setId("userBox");
        s.setParent(this);
        vbox.setParent(this);
        s.setParent(this);
        Label itemDetails = new Label(Labels.getLabel("label.ItemDetails"));
        itemDetails.setId("itemDetails");
        itemDetails.setVisible(false);
        itemDetails.setParent(this);
        Grid grid1 = new Grid();
        grid1.setId("gridItemDetails");
        grid1.setVisible(false);
        Columns cols = new Columns();
        cols.setSizable(true);
        Column col1 = new Column(Labels.getLabel("label.ItemId"));
        col1.setWidth("25px");
        Column col2 = new Column(Labels.getLabel("label.BibilographicDetails"));
        col2.setWidth("275px");
        Column col3 = new Column(Labels.getLabel("label.Type"));
        col3.setWidth("85px");
        Column col4 = new Column(Labels.getLabel("label.Category"));
        col4.setWidth("75px");
        Column col5 = new Column(Labels.getLabel("label.Dates"));
        col5.setWidth("150px");
        Column col6 = new Column(Labels.getLabel("label.OverDue"));
        col6.setWidth("75px");
        Column col7 = new Column();
        col7.setWidth("100px");
        Column col8 = new Column(Labels.getLabel("label.OtherActions"));
        col8.setWidth("85px");
        col1.setParent(cols);
        col2.setParent(cols);
        col3.setParent(cols);
        col4.setParent(cols);
        col5.setParent(cols);
        col6.setParent(cols);
        col7.setParent(cols);
        col8.setParent(cols);
        cols.setParent(grid1);
        Rows rows = new Rows();
        rows.setId("rows");
        rows.setParent(grid1);
        grid1.setParent(this);
        s.setParent(this);
        Label flyItems = new Label(Labels.getLabel("label.OnTheFlyItems"));
        flyItems.setId("onTheFlyItems");
        flyItems.setVisible(false);
        s.setParent(this);
        Label onTheFlyItems = new Label(Labels.getLabel("label.OnTheFlyItems"));
        onTheFlyItems.setId("onTheFlyItems");
        onTheFlyItems.setVisible(false);
        onTheFlyItems.setParent(this);
        Grid grid2 = new Grid();
        grid2.setId("gridOnTheFlyItems");
        grid2.setVisible(false);
        Columns cols2 = new Columns();
        cols2.setSizable(true);
        Column col21 = new Column(Labels.getLabel("label.ItemId"));
        col21.setWidth("25px");
        Column col22 = new Column(Labels.getLabel("label.BibilographicDetails"));
        col22.setWidth("275px");
        Column col23 = new Column(Labels.getLabel("label.Type"));
        col23.setWidth("85px");
        col21.setParent(cols2);
        col22.setParent(cols2);
        col23.setParent(cols2);
        cols2.setParent(grid2);
        Rows rows1 = new Rows();
        rows1.setId("rows1");
        rows1.setParent(grid2);
        grid2.setParent(this);
        hbox2.setParent(this);
    }

    public void onClickingCheckIn(Event evt) {
        ForwardEvent fe = (ForwardEvent) evt;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        NGLUserSession nGLUserSession = new NGLUserSession();
        String databaseId = nGLUserSession.getDatabaseId();
        String libraryId = nGLUserSession.getLibraryId();
        Grid grid = new Grid();
        grid = (Grid) getFellowIfAny("gridItemDetails");
        Grid grid1 = new Grid();
        grid1 = (Grid) getFellowIfAny("gridOnTheFlyItems");
        if ((grid != null && grid.getRows().getChildren().size() > 0) || (grid1 != null && grid1.getRows().getChildren().size() > 0)) {
            int retVal = 0;
            Vector item_ids = getItemidsFromGrid();
            Vector ontheflyItemIds = getOnTheFlyItemIdsFromGrid();
            CirculationUtility circulationUtility = (CirculationUtility) NGLBeanFactory.getInstance().getBean("circulationUtility");
            retVal = circulationUtility.bm_checkIn(item_ids, ontheflyItemIds, new Integer(libraryId), databaseId);
            if (retVal == 0) {
                int val = Messagebox.NO;
                Messagebox mb = new Messagebox();
                try {
                    val = mb.show("Check In has been completed successfully,Do u want to take the Check In print", Labels.getLabel("label.Question"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                if (val == Messagebox.YES) {
                    NGLLogging.getFineLogger().fine("In Message box yes method");
                    FormLetterGeneration flg = (FormLetterGeneration) NGLBeanFactory.getInstance().getBean("formletterGeneration");
                    String patronId = this.getAttribute("patron_id").toString();
                    String template = FormLetterTemplateNames.CHECK_IN_SLIP;
                    String locale = "";
                    String to_type = "";
                    String to_id = patronId;
                    String to_lib_id = libraryId;
                    boolean show_form_letter = true;
                    String database_id = databaseId;
                    String lib_id = libraryId;
                    String to_email_id = "";
                    Hashtable data = new Hashtable();
                    data.put("user_id", patronId);
                    Vector v = new Vector();
                    for (int i = 0; i < item_ids.size(); i++) {
                        String[] str = new String[4];
                        Hashtable ht = (Hashtable) item_ids.get(i);
                        str[0] = ht.get("itemId").toString();
                        String bibilo_details = "";
                        ItemDetails itemDetails = (ItemDetails) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("item_details");
                        Hashtable hashtable = itemDetails.getItemDetails(str[0], new Integer(libraryId), databaseId);
                        if (hashtable.get("Title") != null && hashtable.get("Title").toString().length() > 0 && !hashtable.get("Title").toString().equalsIgnoreCase("")) {
                            bibilo_details = hashtable.get("Title").toString() + "\n";
                        }
                        if (hashtable.get("Author") != null && hashtable.get("Author").toString().length() > 0 && !hashtable.get("Author").toString().equalsIgnoreCase("")) {
                            bibilo_details = bibilo_details + hashtable.get("Author").toString() + "\n";
                        }
                        if (hashtable.get("VolumeNo") != null && hashtable.get("VolumeNo").toString().length() > 0 && !hashtable.get("VolumeNo").toString().equalsIgnoreCase("")) {
                            bibilo_details = bibilo_details + hashtable.get("VolumeNo").toString() + "\n";
                        }
                        if (hashtable.get("Edition") != null && hashtable.get("Edition").toString().length() > 0 && !hashtable.get("Edition").toString().equalsIgnoreCase("")) {
                            bibilo_details = bibilo_details + hashtable.get("Edition").toString() + "\n";
                        }
                        if (hashtable.get("InPrint") != null && hashtable.get("InPrint").toString().length() > 0 && !hashtable.get("InPrint").toString().equalsIgnoreCase("")) {
                            bibilo_details = bibilo_details + hashtable.get("InPrint").toString() + "\n";
                        }
                        if (hashtable.get("ISSN") != null && hashtable.get("ISSN").toString().length() > 0 && !hashtable.get("ISSN").toString().equalsIgnoreCase("")) {
                            bibilo_details = bibilo_details + hashtable.get("ISSN").toString() + "\n";
                        }
                        if (hashtable.get("ISBN") != null && hashtable.get("ISBN").toString().length() > 0 && !hashtable.get("ISBN").toString().equalsIgnoreCase("")) {
                            bibilo_details = bibilo_details + hashtable.get("ISBN").toString() + "\n";
                        }
                        str[1] = bibilo_details;
                        str[2] = hashtable.get("Type").toString();
                        str[3] = hashtable.get("Category").toString();
                        v.add(str);
                    }
                    data.put("newgenlibtable", v);
                    FormLetterComposer formletter = (FormLetterComposer) Executions.createComponents("/jsp/administration/FormLetterComponent.zul", evt.getTarget(), null);
                    formletter.setHeight("80%");
                    formletter.setWidth("80%");
                    formletter.setClosable(true);
                    formletter.onPageLoad(template, locale, data, to_type, to_id, new Integer(to_lib_id), show_form_letter, database_id, lib_id, to_email_id);
                    try {
                        formletter.doModal();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                refreshWindow(evt);
            } else {
                Messagebox mb = new Messagebox();
                try {
                    int val = mb.show("Unable to complete the transaction ", "Question", Messagebox.CANCEL, Messagebox.INFORMATION);
                } catch (Exception exp) {
                }
                refreshWindow(evt);
            }
        } else {
            Messagebox mb = new Messagebox();
            try {
                mb.show("Pls enter the items to be Checked In", "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    public Vector getOnTheFlyItemIdsFromGrid() {
        Vector vector = new Vector();
        Grid grid = (Grid) getFellowIfAny("gridOnTheFlyItems");
        if (grid != null && grid.getRows().getChildren().size() > 0) {
            List ontheflyItemCount = ((Rows) getFellow("rows1")).getChildren();
            for (int i = 0; i < ontheflyItemCount.size(); i++) {
                String id = new String();
                Component com = (Row) ontheflyItemCount.get(i);
                List list = com.getChildren();
                Label label = (Label) list.get(0);
                id = label.getValue();
                vector.add(id);
            }
        }
        NGLLogging.getFineLogger().fine("=========== ontheflyDetails ======:" + vector);
        return vector;
    }

    public Vector getItemidsFromGrid() {
        Vector v = new Vector();
        Grid grid = (Grid) getFellowIfAny("gridItemDetails");
        if (grid != null && grid.getRows().getChildren().size() > 0) {
            List rowCount = ((Grid) getFellow("gridItemDetails")).getRows().getChildren();
            for (int i = 0; i < rowCount.size(); i++) {
                Hashtable ht = new Hashtable();
                Row row = (Row) rowCount.get(i);
                List list = row.getChildren();
                Label label = (Label) list.get(0);
                ht.put("itemId", label.getValue());
                Vbox vbox = (Vbox) list.get(5);
                List lst = vbox.getChildren();
                Component c = vbox.getFirstChild();
                Hbox hbox1 = (Hbox) c;
                String overdue = "";
                String newoverdue = "";
                String overduepaid = "";
                overdue = ((Label) hbox1.getLastChild()).getValue();
                if (lst.size() > 1) {
                    Vbox vbox1 = (Vbox) lst.get(1);
                    Hbox hbox2 = (Hbox) vbox.getLastChild();
                    Toolbarbutton t = (Toolbarbutton) vbox1.getFirstChild();
                    if (t.isDisabled()) {
                        Hbox hbox = (Hbox) vbox1.getLastChild();
                        newoverdue = ((Textbox) hbox.getLastChild()).getValue();
                    }
                    overduepaid = ((Textbox) hbox2.getLastChild()).getValue();
                }
                ht.put("overdue", overdue);
                ht.put("newoverdue", newoverdue);
                ht.put("overduepaid", overduepaid);
                NGLLogging.getFineLogger().fine("itemid>>>" + label.getValue() + "overdue>>>>>>>" + overdue + "new overdue>>>>>>>>" + newoverdue + "overduepaid" + overduepaid);
                NGLLogging.getFineLogger().fine("Hashtabel with itemsids n  overdue values>>>>>>>>>>>>>>" + ht);
                v.add(ht);
            }
        }
        NGLLogging.getFineLogger().fine("====Vector with hashtables========: " + v);
        return v;
    }

    private void refreshWindow(Event event) {
        ((Textbox) getFellow("itemId")).setValue(null);
        ((Button) getFellow("checkIn")).setDisabled(true);
        ((Textbox) getFellow("itemId")).setFocus(true);
        Vbox userBox = (Vbox) getFellow("userBox");
        List list = userBox.getChildren();
        if (list.size() == 1) {
            Component c = (Component) list.get(0);
            userBox.removeChild(c);
        }
        Grid grid = ((Grid) getFellowIfAny("gridItemDetails"));
        if (grid.isVisible() && grid != null) {
            ((Label) getFellow("itemDetails")).setVisible(false);
            List rowsCount = ((Rows) getFellow("rows")).getChildren();
            for (int i = rowsCount.size() - 1; i >= 0; i--) {
                NGLLogging.getFineLogger().fine("%%%%%@@@@@@ >>>>>>>>>itemdetails grid size is >>>>>>>>>>>" + rowsCount.size());
                NGLLogging.getFineLogger().fine("%%%%%@@@@@@ >>>>>>>>>deleted row>>>>>>>>>>>" + i);
                Component component = (Component) rowsCount.get(i);
                ((Rows) getFellow("rows")).removeChild(component);
            }
            grid.setVisible(false);
        }
        Grid grid1 = ((Grid) getFellowIfAny("gridOnTheFlyItems"));
        if (grid1.isVisible() && grid1 != null) {
            ((Label) getFellow("onTheFlyItems")).setVisible(false);
            List rowsCount = ((Rows) getFellow("rows1")).getChildren();
            for (int i = rowsCount.size() - 1; i >= 0; i--) {
                NGLLogging.getFineLogger().fine("%%%%%@@@@@@ >>>>>>>>>ontheflyitemdetails grid size is >>>>>>>>>>>" + rowsCount.size());
                NGLLogging.getFineLogger().fine("%%%%%@@@@@@ >>>>>>>>>deleted row>>>>>>>>>>>" + i);
                Component component = (Component) rowsCount.get(i);
                ((Rows) getFellow("rows1")).removeChild(component);
            }
            grid1.setVisible(false);
        }
        this.setAttribute("patron_id", null);
        this.setAttribute("onthefly_patron_id", null);
    }

    @Override
    public void doAfterCompose(Component win) throws Exception {
    }
}
