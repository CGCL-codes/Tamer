package org.systemsbiology.apps.gui.client.widget.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.systemsbiology.apps.gui.client.constants.OutputFileConstants;
import org.systemsbiology.apps.gui.client.constants.PipelineStep;
import org.systemsbiology.apps.gui.client.data.Model;
import org.systemsbiology.apps.gui.client.util.FileUtils;
import org.systemsbiology.apps.gui.client.widget.general.GuiButton;
import org.systemsbiology.apps.gui.domain.CandidateTransition;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options.Policy;

public class TransitionListSummary extends Composite implements EntryPoint {

    private final int MAX_ROWS = 20;

    private static final Comparator<CandidateTransition> displayOrderComparator = new Comparator<CandidateTransition>() {

        @Override
        public int compare(CandidateTransition o1, CandidateTransition o2) {
            return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
        }
    };

    private VerticalPanel vPanel;

    private GuiButton downloadTransitionsButton;

    private GuiButton showTransitionsButton;

    private Label summaryLabel;

    private int transitionCount;

    List<CandidateTransition> tranList;

    private Table transitionListTable;

    private DataTable data;

    private Table.Options options;

    private DataView dataView;

    public TransitionListSummary() {
        tranList = new ArrayList<CandidateTransition>();
        vPanel = new VerticalPanel();
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setSpacing(20);
        summaryLabel = new Label();
        transitionCount = 0;
        summaryLabel.setText("No transitions loaded");
        hPanel.add(summaryLabel);
        this.downloadTransitionsButton = downloadTransitionsButton();
        this.downloadTransitionsButton.setEnabled(false);
        hPanel.add(downloadTransitionsButton);
        this.showTransitionsButton = showTransitionsButton();
        this.showTransitionsButton.setEnabled(false);
        hPanel.add(showTransitionsButton);
        vPanel.add(hPanel);
        transitionListTable = new Table();
        data = DataTable.create();
        options = Table.Options.create();
        options.setShowRowNumber(true);
        options.setPageSize(MAX_ROWS);
        options.setPage(Policy.ENABLE);
        dataView = DataView.create(data);
        data.addColumn(ColumnType.STRING, "Protein");
        data.addColumn(ColumnType.STRING, "Peptide");
        data.addColumn(ColumnType.NUMBER, "Q1 M/Z");
        data.addColumn(ColumnType.NUMBER, "Q3 M/Z");
        data.addColumn(ColumnType.NUMBER, "Retention Time");
        data.addColumn(ColumnType.NUMBER, "Q1 Charge");
        data.addColumn(ColumnType.NUMBER, "Q3 Charge");
        data.addColumn(ColumnType.NUMBER, "Peak Intensity");
        data.addColumn(ColumnType.STRING, "Ion Label");
        data.addColumn(ColumnType.NUMBER, "Collision Energy");
        data.addColumn(ColumnType.NUMBER, "SSR");
        data.addColumn(ColumnType.NUMBER, "Ordinal Rank");
        data.addColumn(ColumnType.BOOLEAN, "Decoy");
        transitionListTable.draw(dataView, options);
        vPanel.add(transitionListTable);
        initWidget(vPanel);
    }

    public void onModuleLoad() {
        final Label label = new Label("Display Transition List Summary Widget");
        final Label label1 = new Label("                                   ");
        TransitionListSummary transitionListSummary = new TransitionListSummary();
        RootPanel.get().add(label);
        RootPanel.get().add(label1);
        RootPanel.get().add(transitionListSummary);
    }

    private GuiButton showTransitionsButton() {
        final GuiButton b = new GuiButton("Show Transitions");
        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent sender) {
                showResult();
            }
        });
        return b;
    }

    private GuiButton downloadTransitionsButton() {
        final GuiButton b = new GuiButton("Download Transitions");
        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent sender) {
                Long projectId = Model.getAccessModel().getProject().getId();
                projectId = (projectId == null) ? -1 : projectId;
                String url = FileUtils.get().getFileUrl(projectId.toString(), OutputFileConstants.CSV, String.valueOf(PipelineStep.TRAN_LIST_GEN.getId()));
                Window.open(url, "_blank", "");
            }
        });
        return b;
    }

    public void clear() {
        int rows = data.getNumberOfRows();
        data.removeRows(0, rows);
        this.transitionListTable.draw(data);
        this.transitionListTable.setVisible(false);
    }

    private void showSummary() {
        transitionCount = tranList.size();
        if (transitionCount > 0) {
            String summaryString = transitionCount + " Transition";
            if (transitionCount > 1) summaryString += "s";
            summaryLabel.setText(summaryString);
            this.setEnabled(true);
        } else {
            summaryLabel.setText("No transitions loaded");
            this.setEnabled(false);
        }
    }

    private void showResult() {
        this.clear();
        if (tranList == null) return;
        if (tranList.size() > 0) {
            data.addRows(tranList.size());
            CandidateTransition candidateTransition;
            for (int i = 0; i < tranList.size(); i++) {
                candidateTransition = tranList.get(i);
                data.setValue(i, 0, candidateTransition.getProteinName());
                data.setValue(i, 1, candidateTransition.getPeptideName());
                if (candidateTransition.getQ1_mz() != null) data.setValue(i, 2, candidateTransition.getQ1_mz());
                if (candidateTransition.getQ3_mz() != null) data.setValue(i, 3, candidateTransition.getQ3_mz());
                if (candidateTransition.getRt() != null) data.setValue(i, 4, candidateTransition.getRt());
                if (candidateTransition.getQ1_chg() != null) data.setValue(i, 5, candidateTransition.getQ1_chg());
                if (candidateTransition.getQ3_chg() != null) data.setValue(i, 6, candidateTransition.getQ3_chg());
                if (candidateTransition.getPeak_intensity() != null) data.setValue(i, 7, candidateTransition.getPeak_intensity());
                if (candidateTransition.getIon_label() != null) data.setValue(i, 8, candidateTransition.getIon_label());
                if (candidateTransition.getCollision() != null) data.setValue(i, 9, candidateTransition.getCollision());
                if (candidateTransition.getSsr() != null) data.setValue(i, 10, candidateTransition.getSsr());
                if (candidateTransition.getRank() != null) data.setValue(i, 11, candidateTransition.getRank());
                if (candidateTransition.getDecoy() != null) data.setValue(i, 12, candidateTransition.getDecoy());
            }
            this.transitionListTable.draw(data, options);
            this.transitionListTable.setVisible(true);
        }
    }

    private void setEnabled(boolean enabled) {
        this.downloadTransitionsButton.setEnabled(enabled);
        this.showTransitionsButton.setEnabled(enabled);
        this.transitionListTable.setVisible(enabled);
    }

    public List<CandidateTransition> getTranList() {
        return tranList;
    }

    public void setTranList(List<CandidateTransition> tranList) {
        this.tranList = tranList;
        Collections.sort(tranList, displayOrderComparator);
        this.showSummary();
    }
}
