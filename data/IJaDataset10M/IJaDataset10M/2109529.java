package org.omg.tacsit.ui.viewport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.ui.ComponentUtils;
import org.omg.tacsit.common.ui.layouts.FatStackedLayout;
import org.omg.tacsit.common.ui.table.ListTableModel;
import org.omg.tacsit.common.ui.table.NarrowingListTableModel;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.entity.PositionedEntity;
import org.omg.tacsit.query.EntityQuery;
import org.omg.tacsit.query.InstanceOfQuery;
import org.omg.tacsit.query.QueryManager;
import org.omg.tacsit.ui.resources.ActionIcons;
import org.omg.tacsit.ui.resources.DecorationIcons;
import org.omg.tacsit.ui.query.PopulateTableWithQueryAction;
import org.omg.tacsit.ui.entity.PositionedEntityTableModel;
import org.omg.tacsit.ui.TableDefaults;

/**
 * A panel that allows the user to select a set of entities to scale a viewport to.
 * @author Matthew Child
 */
public class ScaleToEntitiesPanel extends JPanel {

    private PositionedEntityTableModel trackTableModel;

    private ListTableModel<Entity> queryResultModel;

    private JFormattedTextField marginField;

    private JTable trackTable;

    private ScaleToEntitiesAction scaleToEntitiesAction;

    private PopulateTableWithQueryAction refreshTableAction;

    /**
   * Creates a new instance.
   */
    public ScaleToEntitiesPanel() {
        initGUI();
        addAncestorListener(new VisibilityListener());
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        JComponent descriptionComponent = createDescriptionComponent();
        descriptionComponent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(descriptionComponent, BorderLayout.NORTH);
        JComponent pointEntryPanel = initEntitySelectionPanel();
        Border pointEntryBorder = BorderFactory.createTitledBorder("Choose Entities");
        pointEntryPanel.setBorder(pointEntryBorder);
        add(pointEntryPanel, BorderLayout.CENTER);
        JComponent scaleAndMarginComponent = initDoScaleAndMarginComponent();
        Insets pointEntryInsets = pointEntryPanel.getInsets();
        scaleAndMarginComponent.setBorder(BorderFactory.createEmptyBorder(pointEntryInsets.top, 30, 0, 30));
        add(scaleAndMarginComponent, BorderLayout.EAST);
    }

    private JComponent initDoScaleAndMarginComponent() {
        JPanel scaleAndMarginPanel = new JPanel(new BorderLayout(5, 5));
        final double INITIAL_MARGIN_VALUE = 0;
        JComponent marginComponent = initMarginComponent(INITIAL_MARGIN_VALUE);
        scaleAndMarginPanel.add(marginComponent, BorderLayout.NORTH);
        JComponent scaleComponent = initDoScaleComponent(INITIAL_MARGIN_VALUE);
        scaleAndMarginPanel.add(scaleComponent, BorderLayout.CENTER);
        return scaleAndMarginPanel;
    }

    private JComponent initDoScaleComponent(double initialMarginValue) {
        JPanel scalePanel = new JPanel(new FatStackedLayout(5));
        scalePanel.add(new JLabel(DecorationIcons.WORLD_ZOOM_128));
        scaleToEntitiesAction = new ScaleToEntitiesAction("Scale Viewport", null);
        scaleToEntitiesAction.setMargin(initialMarginValue);
        JButton scaleToPointsButton = new JButton(scaleToEntitiesAction);
        scaleToPointsButton.setVerticalTextPosition(JButton.BOTTOM);
        scaleToPointsButton.setHorizontalTextPosition(JButton.CENTER);
        scalePanel.add(scaleToPointsButton);
        JPanel doScaleComponent = new JPanel(new GridBagLayout());
        doScaleComponent.add(scalePanel);
        return doScaleComponent;
    }

    private JComponent initMarginComponent(double initialMarginValue) {
        JPanel marginPanel = new JPanel(new BorderLayout(5, 5));
        marginPanel.add(new JLabel("Margin:"), BorderLayout.NORTH);
        marginPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        marginField = new JFormattedTextField(NumberFormat.getNumberInstance());
        marginField.setColumns(12);
        marginField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        marginField.setValue(initialMarginValue);
        marginField.addPropertyChangeListener(ComponentUtils.FORMATTED_FIELD_VALUE_PROPERTY, new MarginValueListener());
        marginPanel.add(marginField, BorderLayout.CENTER);
        marginPanel.add(new JLabel("m"), BorderLayout.EAST);
        return marginPanel;
    }

    private JComponent createDescriptionComponent() {
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        ComponentUtils.configureAsLabel(textArea);
        textArea.setColumns(30);
        textArea.setText("Choose a list of entities (minimum of 1) to use as a basis for scaling the viewport.  The viewport" + " will reorient to fit as tightly as possible around the selected entities, while keeping the given margin" + " of distance around them.");
        descriptionPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return textArea;
    }

    private JComponent initEntitySelectionPanel() {
        JPanel entitySelectionPanel = new JPanel(new BorderLayout(5, 5));
        trackTableModel = new PositionedEntityTableModel();
        queryResultModel = new NarrowingListTableModel(Entity.class, trackTableModel);
        JComponent tableComponent = initTableComponent(queryResultModel);
        entitySelectionPanel.add(tableComponent, BorderLayout.CENTER);
        JComponent tableButtons = initTableButtons(queryResultModel);
        entitySelectionPanel.add(tableButtons, BorderLayout.EAST);
        return entitySelectionPanel;
    }

    private JComponent initTableComponent(TableModel model) {
        trackTable = new JTable(model);
        TableDefaults.initializeEditorAndRendererForTable(trackTable, Angle.class);
        trackTable.getSelectionModel().addListSelectionListener(new SelectedPointsListener());
        JScrollPane tableScroll = new JScrollPane(trackTable);
        tableScroll.setPreferredSize(new Dimension(300, 300));
        return tableScroll;
    }

    private JComponent initTableButtons(ListTableModel<Entity> model) {
        JPanel tableButtonPanel = new JPanel(new FatStackedLayout(0));
        refreshTableAction = new PopulateTableWithQueryAction("Refresh", ActionIcons.REFRESH_24);
        refreshTableAction.setTableModel(model);
        refreshTableAction.setQuery(new InstanceOfQuery(PositionedEntity.class));
        JButton refreshButton = new JButton(refreshTableAction);
        tableButtonPanel.add(refreshButton);
        return tableButtonPanel;
    }

    /**
   * Gets the Viewport that will be scaled.
   * @return The viewport that will be scaled.
   */
    public Viewport getViewportToScale() {
        return scaleToEntitiesAction.getViewportToScale();
    }

    /**
   * Sets the viewport that will be scaled.
   * @param viewport The viewport that will be scaled
   */
    public void setViewportToScale(Viewport viewport) {
        scaleToEntitiesAction.setViewportToScale(viewport);
    }

    /**
   * Sets the query manager that will be used to populate the user selectable entities.
   * @param queryManager The query manager that contains the selectable entities.
   */
    public void setQueryManager(QueryManager queryManager) {
        refreshTableAction.setQueryManager(queryManager);
    }

    /**
   * Gets the query manager that will be used to populate the user selectable entities.
   * @return The query manager that contains the selectable entities.
   */
    public QueryManager getQueryManager() {
        return refreshTableAction.getQueryManager();
    }

    private void refreshTableContents() {
        QueryManager queryManager = getQueryManager();
        if (queryManager == null) {
            return;
        }
        EntityQuery query = refreshTableAction.getQuery();
        if (query == null) {
            return;
        }
        Collection<Entity> matchingEntities = queryManager.submitEntityQuery(query);
        queryResultModel.clear();
        queryResultModel.addAll(matchingEntities);
    }

    private List<PositionedEntity> getSelectedRowsInTable() {
        int[] selectedRowViewIndices = trackTable.getSelectedRows();
        if (selectedRowViewIndices == null) {
            return Collections.emptyList();
        }
        if (selectedRowViewIndices.length == 0) {
            return Collections.emptyList();
        }
        List<PositionedEntity> selectedRows = new ArrayList();
        for (int selectedRowViewIndex : selectedRowViewIndices) {
            int modelIndex = trackTable.convertRowIndexToModel(selectedRowViewIndex);
            PositionedEntity entityAtRow = trackTableModel.getRow(modelIndex);
            selectedRows.add(entityAtRow);
        }
        return selectedRows;
    }

    private class SelectedPointsListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            List<PositionedEntity> selectedRows = getSelectedRowsInTable();
            scaleToEntitiesAction.setEntitiesToContain(selectedRows);
        }
    }

    private class MarginValueListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            Number fieldValue = (Number) marginField.getValue();
            double margin;
            if (fieldValue != null) {
                margin = fieldValue.doubleValue();
            } else {
                margin = 0;
            }
            scaleToEntitiesAction.setMargin(margin);
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        System.out.println("setVisible");
    }

    private class VisibilityListener implements AncestorListener {

        public void ancestorAdded(AncestorEvent event) {
            refreshTableContents();
        }

        public void ancestorRemoved(AncestorEvent event) {
        }

        public void ancestorMoved(AncestorEvent event) {
        }
    }
}
