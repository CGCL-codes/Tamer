package com.crypticbit.ipa.ui.swing.concept;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import com.crypticbit.ipa.central.ProgressIndicator;
import com.crypticbit.ipa.entity.concept.wrapper.impl.EventList;
import com.crypticbit.ipa.ui.swing.Mediator;
import com.crypticbit.ipa.ui.swing.ViewingPane;

public class ConceptView extends ViewingPane {

    private ConceptPanel panels[];

    public ConceptView(final Mediator mediator) {
        final JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        final JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        split.setDividerLocation(400);
        split.setResizeWeight(0.2);
        topSplit.setDividerLocation(200);
        topSplit.setResizeWeight(0.2);
        final EventList events = mediator.getBackupDirectory().getAllEvents(ProgressIndicator.nullProgressIndicator);
        final ConceptDataModel conceptTableModel = new ConceptDataModel(events);
        final JComponent mapPanel = new ConceptWherePanel(mediator, conceptTableModel);
        final JComponent table = new ConceptTable(mediator, conceptTableModel);
        final JComponent whatPane = new ConceptWhatPanel(mediator, conceptTableModel);
        final JComponent whenPane = new ConceptWhenPanel(mediator, conceptTableModel);
        final JComponent itemPane = new ConceptItemPanel(mediator, conceptTableModel);
        panels = new ConceptPanel[] { (ConceptPanel) mapPanel, (ConceptPanel) table, (ConceptPanel) whatPane, (ConceptPanel) whenPane, (ConceptPanel) itemPane };
        for (final ConceptPanel cp : panels) {
            cp.registerToUpdateOnSelectionChange();
            cp.registerToUpddateOnMouseOverChange();
            conceptTableModel.addChangeListener(cp);
        }
        final JSplitPane bottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        bottomPanel.setDividerLocation(0.7);
        bottomPanel.setResizeWeight(0.2);
        bottomPanel.add(table, JSplitPane.LEFT);
        bottomPanel.add(itemPane, JSplitPane.RIGHT);
        split.add(bottomPanel, JSplitPane.BOTTOM);
        final JSplitPane selectPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        selectPanel.setDividerLocation(200);
        selectPanel.setResizeWeight(0.5);
        selectPanel.add(whatPane, JSplitPane.TOP);
        selectPanel.add(whenPane, JSplitPane.BOTTOM);
        topSplit.add(selectPanel, JSplitPane.LEFT);
        topSplit.add(mapPanel, JSplitPane.RIGHT);
        split.add(topSplit, JSplitPane.TOP);
        addTab("Explorer", split);
    }

    @Override
    public void cleanUp() {
    }
}
