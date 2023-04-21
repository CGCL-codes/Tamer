package net.sourceforge.symba.web.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import net.sourceforge.symba.web.client.gui.panel.MetadataViewer;
import net.sourceforge.symba.web.client.gui.panel.SymbaController;
import net.sourceforge.symba.web.shared.InvestigationDetail;
import java.util.ArrayList;

public class SummariseInvestigationView extends FlexTable {

    public static enum ViewType {

        EXTENDED, COPY_CHOSEN, DISPLAY_CHOSEN_METADATA
    }

    private final ViewType viewType;

    private final SymbaController controller;

    private PopupPanel popup = null;

    private final Button copyButton;

    private final Button deleteButton;

    private FlexTable investigationsTable;

    private ListBox investigationsListBox;

    private ArrayList<InvestigationDetail> investigationDetails;

    /**
     * If a popup is passed, then we should also hide the popup on completion of the click handling.
     *
     * @param symba    the controller panel
     * @param viewType which view to display
     * @param popup    the popup to hide
     */
    public SummariseInvestigationView(SymbaController symba, ViewType viewType, PopupPanel popup) {
        this(symba, viewType);
        this.popup = popup;
    }

    public SummariseInvestigationView(SymbaController controller, ViewType viewType) {
        this.controller = controller;
        this.viewType = viewType;
        copyButton = new Button("Copy");
        deleteButton = new Button("Delete");
        investigationsTable = new FlexTable();
        investigationsListBox = new ListBox();
        if (viewType == ViewType.COPY_CHOSEN) {
            makeListBox();
        } else if (viewType == ViewType.EXTENDED) {
            makeExpandedTable();
            controller.setEastWidgetDirections("<p>Click on an Investigation to edit or view. Click on the " + "radio button next to an Investigation to select an Investigation to copy, then click " + "the \"Copy\" button.</p>");
        } else if (viewType == ViewType.DISPLAY_CHOSEN_METADATA) {
            makeMetadataListBox();
        }
    }

    private void makeListBox() {
        setWidget(0, 0, investigationsListBox);
        Button chooseButton = new Button("Copy");
        setWidget(0, 1, chooseButton);
        chooseButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int selected = investigationsListBox.getSelectedIndex();
                if (selected == -1) {
                    selected = 0;
                }
                copyInvestigation(investigationsListBox.getValue(selected));
            }
        });
    }

    private void makeMetadataListBox() {
        setWidget(0, 0, investigationsListBox);
        Button chooseButton = new Button("Display Investigation Metadata");
        setWidget(0, 1, chooseButton);
        chooseButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int selected = investigationsListBox.getSelectedIndex();
                if (selected == -1) {
                    selected = 0;
                }
                MetadataViewer viewer = new MetadataViewer(controller);
                viewer.display(investigationsListBox.getValue(selected));
                controller.hideEastWidget();
                popup.hide();
            }
        });
    }

    private void makeExpandedTable() {
        HorizontalPanel menuPanel = new HorizontalPanel();
        menuPanel.setBorderWidth(0);
        menuPanel.setSpacing(0);
        menuPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        menuPanel.add(copyButton);
        menuPanel.add(deleteButton);
        setWidget(0, 0, menuPanel);
        investigationsTable.setCellSpacing(0);
        investigationsTable.setCellPadding(0);
        investigationsTable.setWidth("100%");
        setWidget(1, 0, investigationsTable);
        addExpandedHandlers();
    }

    public void addExpandedHandlers() {
        deleteButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                boolean response = Window.confirm("Are you sure you wish to delete this Investigation?");
                if (response) {
                    deleteSelectedInvestigation();
                } else {
                    controller.setEastWidgetUserStatus("<p>Deletion of Investigation cancelled.</p>");
                }
            }
        });
        copyButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int selectedRow = getSelectedRow();
                if (selectedRow == -1) {
                    Window.alert("You must select exactly one investigation to copy.");
                    return;
                }
                copyInvestigation(investigationDetails.get(selectedRow).getId());
            }
        });
        investigationsTable.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int selectedRow = getClickedRow(event);
                if (selectedRow >= 0) {
                    controller.setCenterWidgetAsEditExperiment(investigationDetails.get(selectedRow).getId());
                }
            }
        });
    }

    private void copyInvestigation(final String id) {
        controller.getRpcService().copyInvestigation(id, controller.getUser().getId(), new AsyncCallback<InvestigationDetail>() {

            public void onSuccess(InvestigationDetail result) {
                investigationDetails.add(result);
                controller.setStoredInvestigationDetails(investigationDetails);
                sortInvestigationDetails();
                setViewData();
                if (viewType == ViewType.COPY_CHOSEN) {
                    controller.setCenterWidgetAsEditExperiment(result.getId());
                }
                if (popup != null) {
                    popup.hide();
                }
            }

            public void onFailure(Throwable caught) {
                Window.alert("Error copying investigation");
            }
        });
    }

    private void deleteSelectedInvestigation() {
        int selectedRow = getSelectedRow();
        String id = investigationDetails.get(selectedRow).getId();
        controller.getRpcService().deleteInvestigation(id, new AsyncCallback<ArrayList<InvestigationDetail>>() {

            public void onSuccess(ArrayList<InvestigationDetail> result) {
                investigationDetails = result;
                controller.setStoredInvestigationDetails(result);
                sortInvestigationDetails();
                setViewData();
            }

            public void onFailure(Throwable caught) {
                Window.alert("Error deleting selected investigations");
            }
        });
    }

    /**
     * Non-templates should be shown first, and then templates.
     * Within each grouping, sort alphabetically by contact surname.
     * Within each contact surname, sort alphabetically on investigation title.
     */
    public void sortInvestigationDetails() {
        for (int i = 0; i < investigationDetails.size(); ++i) {
            for (int j = 0; j < investigationDetails.size() - 1; ++j) {
                if (investigationDetails.get(j).isTemplate()) {
                    InvestigationDetail tmp = investigationDetails.get(j);
                    investigationDetails.set(j, investigationDetails.get(j + 1));
                    investigationDetails.set(j + 1, tmp);
                }
            }
        }
        for (int i = 0; i < investigationDetails.size(); ++i) {
            for (int j = 0; j < investigationDetails.size() - 1; ++j) {
                if (investigationDetails.get(j).getProvider().getLastName().compareToIgnoreCase(investigationDetails.get(j + 1).getProvider().getLastName()) >= 0 && investigationDetails.get(j).isTemplate() == investigationDetails.get(j + 1).isTemplate()) {
                    InvestigationDetail tmp = investigationDetails.get(j);
                    investigationDetails.set(j, investigationDetails.get(j + 1));
                    investigationDetails.set(j + 1, tmp);
                }
            }
        }
        for (int i = 0; i < investigationDetails.size(); ++i) {
            for (int j = 0; j < investigationDetails.size() - 1; ++j) {
                if (investigationDetails.get(j).getInvestigationTitle().compareToIgnoreCase(investigationDetails.get(j + 1).getInvestigationTitle()) >= 0 && investigationDetails.get(j).getProvider().getLastName().equals(investigationDetails.get(j + 1).getProvider().getLastName()) && investigationDetails.get(j).isTemplate() == investigationDetails.get(j + 1).isTemplate()) {
                    InvestigationDetail tmp = investigationDetails.get(j);
                    investigationDetails.set(j, investigationDetails.get(j + 1));
                    investigationDetails.set(j + 1, tmp);
                }
            }
        }
    }

    public void setViewData() {
        if (viewType != ViewType.EXTENDED) {
            for (int iii = 0; iii < investigationsListBox.getItemCount(); iii++) {
                investigationsListBox.removeItem(iii);
            }
            for (InvestigationDetail detail : investigationDetails) {
                investigationsListBox.addItem(detail.summarise().getHTML(), detail.getId());
            }
        } else {
            investigationsTable.removeAllRows();
            for (int i = 0; i < investigationDetails.size(); ++i) {
                investigationsTable.setWidget(i, 0, new RadioButton("investigationListing"));
                investigationsTable.setWidget(i, 1, investigationDetails.get(i).summarise());
            }
        }
    }

    public int getClickedRow(ClickEvent event) {
        int selectedRow = -1;
        HTMLTable.Cell cell = investigationsTable.getCellForEvent(event);
        if (cell != null) {
            if (cell.getCellIndex() > 0) {
                selectedRow = cell.getRowIndex();
            }
        }
        return selectedRow;
    }

    public int getSelectedRow() {
        for (int i = 0; i < investigationsTable.getRowCount(); ++i) {
            RadioButton radioButton = (RadioButton) investigationsTable.getWidget(i, 0);
            if (radioButton.getValue()) {
                return i;
            }
        }
        return -1;
    }

    public void setInvestigationDetails(ArrayList<InvestigationDetail> investigationDetails) {
        this.investigationDetails = investigationDetails;
        controller.setStoredInvestigationDetails(investigationDetails);
        sortInvestigationDetails();
        setViewData();
    }
}
