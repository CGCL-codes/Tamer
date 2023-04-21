package org.verus.ngl.web.technicalprocessing;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.jdom.Element;
import org.verus.ngl.sl.bprocess.technicalprocessing.AuthorityFiles;
import org.verus.ngl.sl.bprocess.technicalprocessing.NGLAuthoritySearch;
import org.verus.ngl.sl.bprocess.technicalprocessing.Utility;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLUtility;
import org.verus.ngl.utilities.NGLXMLUtility;
import org.verus.ngl.utilities.logging.NGLLogging;
import org.verus.ngl.utilities.marc.NGLConverter;
import org.verus.ngl.utilities.marc.NGLMARCRecord;
import org.verus.ngl.utilities.marc.NGLTag;
import org.verus.ngl.web.util.NGLButtonConstants;
import org.verus.ngl.web.util.NGLUserSession;
import org.verus.ngl.web.util.NGLZKUtility;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectionEvent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Naveen A
 */
public class AuthorityDataSearch extends Window implements Composer, NGLButtonConstants {

    public static final int MODE_SEARCH = 1;

    public static final int MODE_VALIDATION = 2;

    private int clickedButton = BUTTON_CANCEL;

    private int searchMode = MODE_SEARCH;

    public AuthorityDataSearch(Window parent, int searchMode) {
        setParentWindow(parent);
        this.searchMode = searchMode;
        setTitle(Labels.getLabel("label.AuthorityDataSearch"));
        setWidth("90%");
        setContentStyle("autoflow:auto");
        setClosable(true);
        setMaximizable(true);
        setMaximized(true);
        setSizable(true);
        initComponents();
        getSearchResultsGroupBox().setOpen(false);
        getSearchResultsGroupBox().setVisible(false);
        NGLZKUtility.getInstance().applyEscEvent(this);
    }

    private Window getParentWindow() {
        return (Window) getAttribute("parentWindow");
    }

    private void setParentWindow(Window parent) {
        if (parent == null) {
            parent = this;
        }
        setAttribute("parentWindow", parent);
    }

    public void onPressEsc(Event evt) {
        detach();
    }

    private void initComponents() {
        Vbox vbox = new Vbox();
        vbox.setWidth("100%");
        buildSearchOptionsGroupBox().setParent(vbox);
        buildSearchResultsGroupBox().setParent(vbox);
        buildButtonsBox().setParent(vbox);
        vbox.setParent(this);
    }

    private Component buildButtonsBox() {
        Div div = new Div();
        div.setAlign("center");
        Hbox hbox = new Hbox();
        hbox.setParent(div);
        if (searchMode == MODE_VALIDATION) {
            Button bnSelectExisting = new Button(Labels.getLabel("label.SelectExisting"));
            bnSelectExisting.setId("bnSelectExisting");
            bnSelectExisting.setParent(hbox);
            bnSelectExisting.setDisabled(true);
            bnSelectExisting.addForward("onClick", "", "onClickOK");
            Button bnCreateNew = new Button(Labels.getLabel("label.CreateNew"));
            bnCreateNew.setParent(hbox);
            bnCreateNew.addForward("onClick", "", "onClickCreateNewQuick");
        } else {
            Button bnOK = new Button(Labels.getLabel("label.OK"));
            bnOK.setId("bnOK");
            bnOK.addForward("onClick", "", "onClickOK");
            bnOK.setDisabled(true);
            bnOK.setParent(hbox);
        }
        Button bnCancel = new Button(Labels.getLabel("label.Cancel"));
        bnCancel.setId("bnCancel");
        bnCancel.addForward("onClick", "", "onClickCancel");
        bnCancel.setParent(hbox);
        return div;
    }

    public void onClickCancel(Event evt) {
        NGLLogging.getFineLogger().fine("Cancel button is clicked");
        setClickedButton(BUTTON_CANCEL);
        detach();
    }

    public void onClickOK(Event evt) {
        if (searchMode == MODE_VALIDATION) {
            setClickedButton(BUTTON_SELECT_EXISTING);
        } else {
            setClickedButton(BUTTON_OK);
        }
        detach();
    }

    public int getSelectedRecordId() {
        Hashtable ht = getSelectedRecord();
        if (ht != null) {
            return NGLUtility.getInstance().getTestedInteger(ht.get("Id"));
        }
        return -1;
    }

    public Hashtable getSelectedRecord() {
        return (Hashtable) getAttribute("data");
    }

    public void onClickOtherTerm(Event evt) {
        Toolbarbutton lnk = (Toolbarbutton) getTarget(evt);
        search("\"" + lnk.getLabel() + "\"");
    }

    private Component getTarget(Event evt) {
        if (evt instanceof ForwardEvent) {
            ForwardEvent fEvent = (ForwardEvent) evt;
            Event event = fEvent.getOrigin();
            if (event instanceof MouseEvent) {
                return ((MouseEvent) event).getTarget();
            } else if (event instanceof SelectionEvent) {
                return ((SelectionEvent) event).getTarget();
            } else if (event instanceof KeyEvent) {
                return ((KeyEvent) event).getTarget();
            }
        }
        return evt.getTarget();
    }

    private Groupbox buildSearchOptionsGroupBox() {
        Groupbox grpBox = new Groupbox();
        grpBox.setWidth("100%");
        Caption caption = new Caption(Labels.getLabel("label.SearchOptions"));
        caption.setParent(grpBox);
        Vbox vbox = new Vbox();
        vbox.setWidth("100%");
        Hbox hboxType = new Hbox();
        hboxType.setWidth("100%");
        hboxType.setWidths("30%,5%,24%,11%,30%");
        new Label("").setParent(hboxType);
        new Label(Labels.getLabel("label.Type")).setParent(hboxType);
        buildAuthIndexesComboBox().setParent(hboxType);
        Checkbox chkAll = new Checkbox(Labels.getLabel("label.All"));
        chkAll.setId("chkAll");
        chkAll.addForward("onCheck", "", "onCheckAll");
        chkAll.setParent(hboxType);
        new Label("").setParent(hboxType);
        hboxType.setParent(vbox);
        Hbox hboxTerm = new Hbox();
        hboxTerm.setWidth("100%");
        hboxTerm.setWidths("30%,5%,24%,11%,30%");
        new Label("").setParent(hboxTerm);
        new Label(Labels.getLabel("label.Term")).setParent(hboxTerm);
        Textbox textbox = new Textbox();
        textbox.setId("txtTerm");
        textbox.addForward("onOK", "", "onClickSearch");
        textbox.setParent(hboxTerm);
        Button bnSearch = new Button(Labels.getLabel("label.Search"));
        bnSearch.addForward("onClick", "", "onClickSearch");
        bnSearch.setParent(hboxTerm);
        new Label().setParent(hboxTerm);
        hboxTerm.setParent(vbox);
        Hbox hboxRPP = new Hbox();
        hboxRPP.setWidth("100%");
        hboxRPP.setWidths("78%,14%,8%");
        hboxRPP.setParent(vbox);
        new Label().setParent(hboxRPP);
        getMessageBox().setParent(vbox);
        new Label(Labels.getLabel("label.RecordsPerPage")).setParent(hboxRPP);
        buildRecPerPageListBox().setParent(hboxRPP);
        vbox.setParent(grpBox);
        return grpBox;
    }

    private Component getMessageBox() {
        Div div = new Div();
        div.setAlign("center");
        Hbox hbox = new Hbox();
        hbox.setParent(div);
        Label lblMessages = new Label();
        lblMessages.setId("lblMessage");
        lblMessages.setStyle("font-weight:bold;background:#FAE86F");
        lblMessages.setParent(hbox);
        Toolbarbutton lnkCreateNew = new Toolbarbutton(Labels.getLabel("label.CreateNew"));
        lnkCreateNew.addForward("onClick", "", "onClickCreateNew");
        lnkCreateNew.setId("lnkCreateNew");
        lnkCreateNew.setVisible(false);
        lnkCreateNew.setParent(hbox);
        return div;
    }

    public void onClickCreateNewQuick(Event evt) {
        NGLUserSession userSession = new NGLUserSession();
        NGLTag nGLTag = getValidationTag();
        AuthorityFiles auth = (AuthorityFiles) NGLBeanFactory.getInstance().getBean("authorityFiles");
        Hashtable ht = auth.bm_getWorkSheet(userSession.getDatabaseId(), Integer.parseInt(userSession.getLibraryId()), nGLTag.getTag());
        if (ht != null) {
            String worksheet = NGLUtility.getInstance().getTestedString(ht.get("WorkSheet"));
            NGLMARCRecord mARCRecord = NGLConverter.getInstance().parseToMARCRecord(worksheet);
            NGLTag[] nGLTags = mARCRecord.getTags();
            for (int i = 0; i < nGLTags.length; i++) {
                if (nGLTags[i].getTag().equalsIgnoreCase(nGLTag.getTag())) {
                    nGLTags[i] = nGLTag;
                }
            }
            NGLLogging.getFineLogger().fine(mARCRecord.toString());
            String marcXML = NGLConverter.getInstance().getMarcXml(new NGLMARCRecord[] { mARCRecord });
            int id = auth.bm_createAuthorityRecord(userSession.getUserId(), Integer.parseInt(userSession.getLibraryId()), userSession.getDatabaseId(), nGLTag.getTag(), marcXML);
            if (id != -1) {
                Hashtable htAuth = auth.bm_getAuthorityRecord(userSession.getDatabaseId(), new Integer(userSession.getLibraryId()), id);
                setAttribute("data", htAuth);
                setClickedButton(BUTTON_CREATE_NEW);
                detach();
            } else {
                setMessage("Failed to create the new record");
            }
        }
    }

    public void onClickCreateNew(Event evt) {
        String headingType = ((Hashtable) getAuthIndexesComboBox().getSelectedItem().getValue()).get("IndexName").toString();
        AuthorityDataComposer composer = new AuthorityDataComposer(getParentWindow(), headingType);
        composer.setParent(getParentWindow());
        try {
            composer.doModal();
        } catch (Exception e) {
        }
        removeChild(composer);
    }

    public NGLTag getValidationTag() {
        return (NGLTag) getAttribute("validationTag");
    }

    public void setValidationTag(NGLTag nGLTag) {
        setAttribute("validationTag", nGLTag);
        setHeadingType(nGLTag.getTag());
        search(getSearchableData(nGLTag));
    }

    private String getSearchableData(NGLTag nGLTag) {
        String tag = nGLTag.getTag();
        String[] data = null;
        if (tag.equalsIgnoreCase("185")) {
            data = nGLTag.getSubfieldsData("v");
        } else if (tag.equalsIgnoreCase("180")) {
            data = nGLTag.getSubfieldsData("x");
        } else if (tag.equalsIgnoreCase("182")) {
            data = nGLTag.getSubfieldsData("y");
        } else if (tag.equalsIgnoreCase("181")) {
            data = nGLTag.getSubfieldsData("z");
        } else {
            data = nGLTag.getSubfieldsData("a");
        }
        if (data != null && data.length > 0) {
            return data[0];
        }
        return "";
    }

    public void onCheckAll(Event evt) {
        NGLLogging.getFineLogger().fine("Checkbox All is clicked");
        if (((Checkbox) getFellow("chkAll")).isChecked()) {
            ((Combobox) getFellow("comboAuthIndexes")).setDisabled(true);
        } else {
            ((Combobox) getFellow("comboAuthIndexes")).setDisabled(false);
        }
    }

    private Listbox buildRecPerPageListBox() {
        Listbox listbox = new Listbox();
        listbox.setId("listRecPerPage");
        listbox.setRows(1);
        listbox.setMold("select");
        String[] values = new String[] { "10", "25", "50", "75", "100" };
        for (int i = 0; i < values.length; i++) {
            String string = values[i];
            Listitem item = new Listitem(string);
            item.setParent(listbox);
            if (string.equalsIgnoreCase("10")) {
                listbox.setSelectedItem(item);
            }
        }
        return listbox;
    }

    private Groupbox buildSearchResultsGroupBox() {
        Groupbox groupboxSRs = new Groupbox();
        Vbox vbox = new Vbox();
        groupboxSRs.setId("groupSearchResults");
        Caption caption = new Caption(Labels.getLabel("label.SearchResults"));
        caption.setId("caption");
        caption.setParent(groupboxSRs);
        getHeaderBox().setParent(vbox);
        Radiogroup radiogroup = new Radiogroup();
        buildSearchResultsGrid().setParent(radiogroup);
        radiogroup.setParent(vbox);
        getFooterBox().setParent(vbox);
        vbox.setParent(groupboxSRs);
        return groupboxSRs;
    }

    private Hbox getHeaderBox() {
        Hbox hbox = new Hbox();
        hbox.setWidth("100%");
        hbox.setWidths("72%,18%,5%,5%");
        Button bnNew = new Button(Labels.getLabel("label.New"), "/images/16/document-new.png");
        bnNew.setParent(hbox);
        bnNew.addForward("onClick", "", "onClickCreateNew");
        Label lblMessage = new Label();
        lblMessage.setId("lblMessageResults");
        lblMessage.setStyle("font-weight: bold");
        lblMessage.setParent(hbox);
        Toolbarbutton lnkPrev = new Toolbarbutton(Labels.getLabel("label.Prev"));
        lnkPrev.setId("lnkPrevHeader");
        lnkPrev.addForward("onClick", "", "onClickPrev");
        lnkPrev.setParent(hbox);
        Toolbarbutton lnkNext = new Toolbarbutton(Labels.getLabel("label.Next"));
        lnkNext.setId("lnkNextHeader");
        lnkNext.addForward("onClick", "", "onClickNext");
        lnkNext.setParent(hbox);
        return hbox;
    }

    public void onClickNext(Event evt) {
        NGLLogging.getFineLogger().fine("Next link is clicked");
        int totCount = NGLUtility.getInstance().getTestedInteger(getAttribute("totalResultsCount"));
        int from = NGLUtility.getInstance().getTestedInteger(getAttribute("from"));
        int to = NGLUtility.getInstance().getTestedInteger(getAttribute("to"));
        String queryXML = NGLUtility.getInstance().getTestedString(getAttribute("queryXML"));
        int recPerPage = getRecordsPerPage();
        from = to + 1;
        setPrevVisible(true);
        if (to + recPerPage < totCount) {
            to = to + recPerPage;
        } else {
            to = totCount;
            setNextVisible(false);
        }
        search(queryXML, from, to);
    }

    public void onClickPrev(Event evt) {
        NGLLogging.getFineLogger().fine("Prev link is clicked");
        int totCount = NGLUtility.getInstance().getTestedInteger(getAttribute("totalResultsCount"));
        int from = NGLUtility.getInstance().getTestedInteger(getAttribute("from"));
        int to = NGLUtility.getInstance().getTestedInteger(getAttribute("to"));
        String queryXML = NGLUtility.getInstance().getTestedString(getAttribute("queryXML"));
        int recPerPage = getRecordsPerPage();
        to = from - 1;
        from = from - recPerPage;
        if (from <= 0) {
            from = 1;
            to = recPerPage;
            if (recPerPage > totCount) {
                to = totCount;
            }
        }
        if (from == 1) {
            setPrevVisible(false);
        }
        if (totCount > to) {
            setNextVisible(true);
        } else {
            setNextVisible(false);
        }
        search(queryXML, from, to);
    }

    private Grid getSearchResultsGrid() {
        return (Grid) getFellow("gridSearchResults");
    }

    private Groupbox getSearchResultsGroupBox() {
        return (Groupbox) getFellow("groupSearchResults");
    }

    public int getRecordsPerPage() {
        return NGLUtility.getInstance().getTestedInteger(((Listbox) getFellow("listRecPerPage")).getSelectedItem().getLabel());
    }

    private Hbox getFooterBox() {
        Hbox hbox = new Hbox();
        hbox.setWidth("100%");
        hbox.setWidths("90%,5%,5%");
        Button bnNew = new Button(Labels.getLabel("label.New"), "/images/16/document-new.png");
        bnNew.setParent(hbox);
        bnNew.addForward("onClick", "", "onClickCreateNew");
        Toolbarbutton lnkPrev = new Toolbarbutton(Labels.getLabel("label.Prev"));
        lnkPrev.setId("lnkPrevFooter");
        lnkPrev.addForward("onClick", "", "onClickPrev");
        lnkPrev.setParent(hbox);
        Toolbarbutton lnkNext = new Toolbarbutton(Labels.getLabel("label.Next"));
        lnkNext.setId("lnkNextFooter");
        lnkNext.addForward("onClick", "", "onClickNext");
        lnkNext.setParent(hbox);
        return hbox;
    }

    private Grid buildSearchResultsGrid() {
        Grid gridSRs = new Grid();
        gridSRs.setId("gridSearchResults");
        gridSRs.setFixedLayout(true);
        Columns cols = new Columns();
        Column colSelect = new Column(Labels.getLabel("label.Select"));
        colSelect.setWidth("7%");
        colSelect.setAlign("center");
        colSelect.setParent(cols);
        Column colDisplay = new Column(Labels.getLabel("label.Display"));
        colDisplay.setWidth("65%");
        colDisplay.setParent(cols);
        Column colAT = new Column(Labels.getLabel("label.AuthorityType"));
        colAT.setWidth("20%");
        colAT.setParent(cols);
        Column colFun = new Column(Labels.getLabel("label.Functions"));
        colFun.setWidth("8%");
        colFun.setParent(cols);
        cols.setParent(gridSRs);
        gridSRs.setRowRenderer(new AuthorityDataRenderer());
        gridSRs.setModel(new SimpleListModel(buildAuthDataHash(0)));
        return gridSRs;
    }

    private Hashtable[] buildAuthDataHash(int length) {
        Hashtable[] hts = new Hashtable[length];
        for (int i = 0; i < length; i++) {
            Hashtable ht = new Hashtable();
            ht.put("id", i);
            ht.put("AuthorisedHeading", "AuthorisedHeading-" + i);
            ht.put("AuthorityType", "AuthorityType-" + i);
            hts[i] = ht;
        }
        return hts;
    }

    public void onSelectAuthRecord(Event evt) {
        NGLLogging.getFineLogger().fine("Authority record is selected");
        ForwardEvent fEvt = (ForwardEvent) evt;
        CheckEvent chkEvt = (CheckEvent) fEvt.getOrigin();
        Component comp = chkEvt.getTarget();
        setAttribute("data", comp.getAttribute("data"));
        if (searchMode == MODE_VALIDATION) {
            ((Button) getFellow("bnSelectExisting")).setDisabled(false);
        } else {
            ((Button) getFellow("bnOK")).setDisabled(false);
        }
    }

    public void onClickSearch(Event evt) {
        search();
    }

    public String getSearchTerm() {
        return NGLUtility.getInstance().getTestedString(((Textbox) getFellow("txtTerm")).getValue());
    }

    private String[] getAllFields() {
        Hashtable ht = getHtAuthIndexes();
        Enumeration enumeration = ht.keys();
        String[] fields = null;
        while (enumeration.hasMoreElements()) {
            Hashtable htIndex = (Hashtable) ht.get(enumeration.nextElement());
            fields = merge(fields, (String[]) htIndex.get("IndexFields"));
        }
        return fields;
    }

    private String[] merge(String[] array1, String[] array2) {
        if (array1 == null || array1.length == 0) {
            return array2;
        }
        if (array2 == null || array2.length == 0) {
            return array1;
        }
        String[] arrayFinal = new String[array1.length + array2.length];
        int j = 0;
        for (int i = 0; i < array1.length; i++) {
            arrayFinal[j] = array1[i];
            j++;
        }
        for (int i = 0; i < array2.length; i++) {
            arrayFinal[j] = array2[i];
            j++;
        }
        return arrayFinal;
    }

    private Combobox getAuthIndexesComboBox() {
        return (Combobox) getFellow("comboAuthIndexes");
    }

    public void setSearchTerm(String searchTerm) {
        ((Textbox) getFellow("txtTerm")).setValue(searchTerm);
    }

    private void setMessage(String message) {
        ((Label) getFellow("lblMessage")).setValue(message);
    }

    private Toolbarbutton getCreateNewLink() {
        return (Toolbarbutton) getFellow("lnkCreateNew");
    }

    private int search() {
        int totResultsCount = 0;
        String term = getSearchTerm();
        String queryXML = "";
        if (term.trim().length() > 0) {
            String[] fields = null;
            if (((Checkbox) getFellow("chkAll")).isChecked()) {
                fields = getAllFields();
            } else {
                fields = (String[]) ((Hashtable) getAuthIndexesComboBox().getSelectedItem().getValue()).get("IndexFields");
            }
            NGLUserSession userSession = new NGLUserSession();
            queryXML = Utility.getInstance().buildLuceneQueryXML(term, fields, userSession.getLibraryId());
            NGLAuthoritySearch searchAuth = (NGLAuthoritySearch) NGLBeanFactory.getInstance().getBean("nGLAuthoritySearch");
            totResultsCount = searchAuth.bm_countSearchResults(userSession.getDatabaseId(), queryXML);
        } else {
            setMessage(Labels.getLabel("message.TheSearchTermShouldNotBeEmpty"));
            getCreateNewLink().setVisible(true);
            return 0;
        }
        if (totResultsCount > 0) {
            int recPerPage = getRecordsPerPage();
            getSearchResultsGroupBox().setVisible(true);
            ((Caption) getFellow("caption")).setLabel(Labels.getLabel("label.SearchResults") + " (" + totResultsCount + ")");
            setAttribute("totalResultsCount", totResultsCount);
            setAttribute("term", term);
            setAttribute("queryXML", queryXML);
            int to = recPerPage;
            if (recPerPage > totResultsCount) {
                to = totResultsCount;
            }
            search(queryXML, 1, to);
            setPrevVisible(false);
            if (recPerPage < totResultsCount) {
                setNextVisible(true);
            } else {
                setNextVisible(false);
            }
            setMessage("");
            getCreateNewLink().setVisible(false);
        } else {
            setMessage(Labels.getLabel("message.NoResultsFoundWithTerm") + " \"" + term + "\"");
            getCreateNewLink().setVisible(true);
            getSearchResultsGroupBox().setVisible(false);
        }
        if (searchMode == MODE_VALIDATION) {
            ((Button) getFellow("bnSelectExisting")).setDisabled(true);
        } else {
            ((Button) getFellow("bnOK")).setDisabled(true);
        }
        return totResultsCount;
    }

    public int search(String term, String type) {
        setHeadingType(type);
        return search(term);
    }

    public int search(String term) {
        setSearchTerm(term);
        return search();
    }

    public void setHeadingType(String type) {
        ((Checkbox) getFellow("chkAll")).setChecked(false);
        ((Checkbox) getFellow("chkAll")).setDisabled(true);
        Combobox combo = getAuthIndexesComboBox();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAtIndex(i).getDescription().equalsIgnoreCase(type)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
        combo.setDisabled(true);
    }

    private int search(String queryXML, int from, int to) {
        setAttribute("from", from);
        setAttribute("to", to);
        Element root = NGLXMLUtility.getInstance().getRootElementFromXML(queryXML);
        root.setAttribute("from", String.valueOf(from));
        root.setAttribute("to", String.valueOf(to));
        queryXML = NGLXMLUtility.getInstance().generateXML((Element) root.clone());
        if (to - from + 1 > 0) {
            NGLAuthoritySearch searchAuth = (NGLAuthoritySearch) NGLBeanFactory.getInstance().getBean("nGLAuthoritySearch");
            NGLUserSession userSession = new NGLUserSession();
            List list = searchAuth.bm_searchAuthirityData(userSession.getDatabaseId(), new Integer(userSession.getLibraryId()), queryXML);
            getSearchResultsGrid().setModel(buildGridModel(list));
            ((Groupbox) getFellow("groupSearchResults")).setOpen(true);
            ((Label) getFellow("lblMessageResults")).setValue(from + " to " + to + " of " + getTotalResultsCount());
            return to - from + 1;
        }
        return 0;
    }

    private ListModel buildGridModel(List list) {
        String term = NGLUtility.getInstance().getTestedString(getAttribute("term"));
        if (term.trim().charAt(term.trim().length() - 1) == '*') {
            term = term.trim().substring(0, term.trim().length() - 1);
        }
        for (int i = 0; list != null && i < list.size(); i++) {
            Hashtable ht = (Hashtable) list.get(i);
            ht.put("Term", term);
        }
        return new SimpleListModel(list);
    }

    public int getTotalResultsCount() {
        return NGLUtility.getInstance().getTestedInteger(getAttribute("totalResultsCount"));
    }

    private void setPrevVisible(boolean visibility) {
        ((Toolbarbutton) getFellow("lnkPrevHeader")).setVisible(visibility);
        ((Toolbarbutton) getFellow("lnkPrevFooter")).setVisible(visibility);
    }

    private void setNextVisible(boolean visibility) {
        ((Toolbarbutton) getFellow("lnkNextHeader")).setVisible(visibility);
        ((Toolbarbutton) getFellow("lnkNextFooter")).setVisible(visibility);
    }

    private Combobox buildAuthIndexesComboBox() {
        Combobox comboAuthIndexes = new Combobox();
        comboAuthIndexes.setReadonly(true);
        comboAuthIndexes.setId("comboAuthIndexes");
        loadAuthorityIndexes();
        Hashtable htIndexes = getHtAuthIndexes();
        String[] keys = new String[] { "100", "110", "111", "130", "148", "150", "151", "155", "185", "180", "182", "181" };
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Hashtable ht = (Hashtable) htIndexes.get(key);
            String display = ht.get("IndexDisplay").toString();
            Comboitem item = new Comboitem(display);
            item.setDescription(key);
            item.setValue(ht);
            item.setParent(comboAuthIndexes);
            if (key.equalsIgnoreCase("100")) {
                comboAuthIndexes.setSelectedItem(item);
            }
        }
        return comboAuthIndexes;
    }

    public void setFocusOnSearchTermTextBox() {
        ((Textbox) getFellow("txtTerm")).setFocus(true);
    }

    private void loadAuthorityIndexes() {
        Hashtable ht = new Hashtable();
        Hashtable htAuth = new Hashtable();
        htAuth.put("IndexName", "100");
        htAuth.put("IndexDisplay", "Personal Name");
        htAuth.put("IndexFields", new String[] { "100_a", "400_a" });
        ht.put("100", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "110");
        htAuth.put("IndexDisplay", "Corporate Name");
        htAuth.put("IndexFields", new String[] { "110_a", "410_a" });
        ht.put("110", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "111");
        htAuth.put("IndexDisplay", "Meeting Name");
        htAuth.put("IndexFields", new String[] { "111_a", "411_a" });
        ht.put("111", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "130");
        htAuth.put("IndexDisplay", "Uniform Title");
        htAuth.put("IndexFields", new String[] { "130_a", "430_a" });
        ht.put("130", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "148");
        htAuth.put("IndexDisplay", "Chronological Term");
        htAuth.put("IndexFields", new String[] { "148_a", "448_a" });
        ht.put("148", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "150");
        htAuth.put("IndexDisplay", "Topical Term");
        htAuth.put("IndexFields", new String[] { "150_a", "450_a" });
        ht.put("150", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "151");
        htAuth.put("IndexDisplay", "Geographic Name");
        htAuth.put("IndexFields", new String[] { "151_a", "451_a" });
        ht.put("151", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "155");
        htAuth.put("IndexDisplay", "Genre/Form Term");
        htAuth.put("IndexFields", new String[] { "155_a", "455_a" });
        ht.put("155", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "185");
        htAuth.put("IndexDisplay", "Form Subdivision");
        htAuth.put("IndexFields", new String[] { "185_v", "485_v" });
        ht.put("185", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "180");
        htAuth.put("IndexDisplay", "General Subdivision");
        htAuth.put("IndexFields", new String[] { "180_x", "480_x" });
        ht.put("180", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "182");
        htAuth.put("IndexDisplay", "Chronological Subdivision");
        htAuth.put("IndexFields", new String[] { "182_y", "482_y" });
        ht.put("182", htAuth);
        htAuth = new Hashtable();
        htAuth.put("IndexName", "181");
        htAuth.put("IndexDisplay", "Geographic Subdivision");
        htAuth.put("IndexFields", new String[] { "181_z", "481_z" });
        ht.put("181", htAuth);
        setHtAuthIndexes(ht);
    }

    @Override
    public void doAfterCompose(Component arg0) throws Exception {
        setTitle(Labels.getLabel("label.AuthorityDataSearch"));
    }

    public Hashtable getHtAuthIndexes() {
        return htAuthIndexes;
    }

    public void setHtAuthIndexes(Hashtable htAuthIndexes) {
        this.htAuthIndexes = htAuthIndexes;
    }

    private Hashtable htAuthIndexes = null;

    public int getClickedButton() {
        return clickedButton;
    }

    public void setClickedButton(int clickedButton) {
        this.clickedButton = clickedButton;
    }

    public int getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;
    }
}
