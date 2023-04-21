package edu.pitt.dbmi.odie.gapp.gwt.client.umls;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ODIE_UmlsPanel extends VLayout {

    private String currentOntologyId = null;

    private String currentClsId = null;

    private VLayout ontologyGroup = null;

    private HLayout clsLayout = null;

    private VLayout clsGroup = null;

    private VLayout subClsGroup = null;

    private VLayout superClsGroup = null;

    public ODIE_UmlsPanel() {
        setLayoutMargin(5);
        ontologyGroup = buildOntologyGroup();
        addMember(ontologyGroup);
        buildClsGroups();
    }

    private VLayout buildOntologyGroup() {
        DataSource ontologyDataSource = null;
        ontologyDataSource = buildOntologyDataSource();
        final ListGrid ontologyListGrid = buildListGrid();
        ontologyListGrid.setDataSource(ontologyDataSource);
        ontologyListGrid.setAutoFetchData(true);
        ontologyListGrid.setAlternateRecordStyles(true);
        ontologyListGrid.setShowAllRecords(true);
        ontologyListGrid.setSelectionType(SelectionStyle.SINGLE);
        ontologyListGrid.setCanSort(true);
        ontologyListGrid.addRecordDoubleClickHandler(buildOntologyDoubleClickListener());
        ontologyListGrid.setDataSource(ontologyDataSource);
        ontologyListGrid.setCanAcceptDrop(false);
        ontologyListGrid.setCanEdit(false);
        ontologyListGrid.setCanReorderRecords(false);
        ontologyListGrid.setCanFreezeFields(false);
        ontologyListGrid.setCanReorderFields(false);
        ontologyListGrid.setCanGroupBy(false);
        ontologyListGrid.setCanHover(false);
        ontologyListGrid.setCanSelectAll(false);
        ontologyListGrid.setCanSelectText(false);
        ontologyListGrid.setCanResizeFields(false);
        ontologyListGrid.setShowHeaderContextMenu(false);
        ontologyListGrid.addDataArrivedHandler(new DataArrivedHandler() {

            public void onDataArrived(DataArrivedEvent event) {
                ontologyListGrid.setSortField("sab");
                ontologyListGrid.sort();
                ontologyListGrid.setCanSort(false);
                ontologyListGrid.redraw();
            }
        });
        ontologyListGrid.setSortField("displayLabel");
        ontologyGroup = new VLayout();
        ontologyGroup.setLayoutMargin(5);
        ontologyGroup.setIsGroup(true);
        ontologyGroup.setGroupTitle("Ontology Navigator");
        ontologyGroup.addMember(ontologyListGrid);
        return ontologyGroup;
    }

    private DataSource buildOntologyDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDataFormat(DSDataFormat.XML);
        dataSource.setRecordXPath("//sab");
        dataSource.setDataURL("odie_client/umls");
        DataSourceTextField ontologyIdField = new DataSourceTextField("@name", "sab");
        ontologyIdField.setValueXPath("@name");
        dataSource.setFields(ontologyIdField);
        return dataSource;
    }

    private RecordDoubleClickHandler buildOntologyDoubleClickListener() {
        return new RecordDoubleClickHandler() {

            public void onRecordDoubleClick(RecordDoubleClickEvent event) {
                Record record = event.getRecord();
                currentOntologyId = record.getAttribute("@name");
                currentClsId = null;
                buildClsGroups();
                redraw();
            }
        };
    }

    private void buildClsGroups() {
        if (clsLayout != null) {
            removeMember(clsLayout);
        }
        clsGroup = buildClsGroup("Current Cls", null);
        subClsGroup = buildClsGroup("Subclasses", "children");
        superClsGroup = buildClsGroup("Superclasses", "parents");
        clsLayout = new HLayout();
        clsLayout.setMembersMargin(5);
        clsLayout.setLayoutMargin(0);
        clsLayout.addMember(superClsGroup);
        clsLayout.addMember(clsGroup);
        clsLayout.addMember(subClsGroup);
        addMember(clsLayout);
    }

    private VLayout buildClsGroup(String groupName, String relation) {
        VLayout groupLayout = null;
        ListGrid grid = buildListGrid();
        grid.addRecordDoubleClickHandler(buildClsRecordDoubleClickListener());
        if (this.currentOntologyId != null) {
            DataSource dataSource = null;
            if (relation == null) {
                dataSource = buildClsDataSource(currentOntologyId, currentClsId);
            } else if (relation.equals("parents")) {
                dataSource = buildClsParentsDataSource(currentOntologyId, currentClsId);
            } else if (relation.equals("children")) {
                dataSource = buildClsChildrenDataSource(currentOntologyId, currentClsId);
            }
            grid.setDataSource(dataSource);
        }
        grid.setSortField("str");
        groupLayout = new VLayout();
        groupLayout.setLayoutMargin(5);
        groupLayout.setIsGroup(true);
        groupLayout.setGroupTitle(groupName);
        groupLayout.addMember(grid);
        return groupLayout;
    }

    private RecordDoubleClickHandler buildClsRecordDoubleClickListener() {
        return new RecordDoubleClickHandler() {

            public void onRecordDoubleClick(RecordDoubleClickEvent event) {
                Record record = event.getRecord();
                currentClsId = record.getAttribute("cui");
                buildClsGroups();
                redraw();
            }
        };
    }

    private DataSource buildClsDataSource(String ontologyId, String clsId) {
        DataSource dataSource = new DataSource();
        dataSource.setDataFormat(DSDataFormat.XML);
        dataSource.setRecordXPath("//concept/term");
        String urlName = "odie_client/umls?ontologyId=" + ontologyId;
        if (clsId != null) {
            urlName += "&clsId=" + clsId;
        }
        dataSource.setDataURL(urlName);
        DataSourceTextField cuiField = new DataSourceTextField("cui", "cui");
        DataSourceTextField strField = new DataSourceTextField("str", "str");
        DataSourceTextField luiField = new DataSourceTextField("lui", "lui");
        luiField.setHidden(true);
        DataSourceTextField suiField = new DataSourceTextField("sui", "sui");
        suiField.setHidden(true);
        DataSourceTextField auiField = new DataSourceTextField("aui", "aui");
        auiField.setHidden(true);
        DataSourceTextField sAuiField = new DataSourceTextField("saui", "saui");
        sAuiField.setHidden(true);
        DataSourceTextField sCuiField = new DataSourceTextField("scui", "scui");
        sCuiField.setHidden(true);
        DataSourceTextField sabField = new DataSourceTextField("sab", "sab");
        sabField.setHidden(true);
        DataSourceTextField isprefField = new DataSourceTextField("ispref", "ispref");
        isprefField.setHidden(true);
        dataSource.setFields(cuiField, strField, luiField, suiField, auiField, sAuiField, sCuiField, sabField, isprefField);
        return dataSource;
    }

    private DataSource buildClsParentsDataSource(String ontologyId, String clsId) {
        DataSource dataSource = new DataSource();
        dataSource.setDataFormat(DSDataFormat.XML);
        dataSource.setRecordXPath("//concept/parent");
        String urlName = "odie_client/umls?ontologyId=" + ontologyId;
        urlName += "&clsId=" + clsId;
        urlName += "&parents=true";
        dataSource.setDataURL(urlName);
        DataSourceTextField cuiField = new DataSourceTextField("cui", "cui");
        cuiField.setHidden(true);
        DataSourceTextField strField = new DataSourceTextField("str", "str");
        DataSourceTextField auiField = new DataSourceTextField("aui", "aui");
        auiField.setHidden(true);
        DataSourceTextField isprefField = new DataSourceTextField("ispref", "ispref");
        isprefField.setHidden(true);
        dataSource.setFields(cuiField, strField, auiField, isprefField);
        return dataSource;
    }

    private DataSource buildClsChildrenDataSource(String ontologyId, String clsId) {
        DataSource dataSource = new DataSource();
        dataSource.setDataFormat(DSDataFormat.XML);
        dataSource.setRecordXPath("//concept/child");
        String urlName = "odie_client/umls?ontologyId=" + ontologyId;
        urlName += "&clsId=" + clsId;
        urlName += "&children=true";
        dataSource.setDataURL(urlName);
        DataSourceTextField cuiField = new DataSourceTextField("cui", "cui");
        cuiField.setHidden(true);
        DataSourceTextField strField = new DataSourceTextField("str", "str");
        DataSourceTextField auiField = new DataSourceTextField("aui", "aui");
        auiField.setHidden(true);
        DataSourceTextField isprefField = new DataSourceTextField("ispref", "ispref");
        isprefField.setHidden(true);
        dataSource.setFields(cuiField, strField, auiField, isprefField);
        return dataSource;
    }

    private ListGrid buildListGrid() {
        ListGrid grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setAutoFetchData(true);
        return grid;
    }
}
