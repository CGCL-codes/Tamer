package no.uib.hplims.views.experiments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import no.uib.hplims.Controller;
import no.uib.hplims.MyVaadinApplication;
import no.uib.hplims.models.Experiment;
import no.uib.hplims.models.MyOrder;
import no.uib.hplims.tools.Icons;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;
import org.vaadin.appfoundation.view.AbstractView;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbstractSelect.MultiSelectMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ExperimentsTab extends AbstractView<VerticalLayout> implements Button.ClickListener {

    private static final long serialVersionUID = -2707048273887213422L;

    private Controller controller = null;

    private VerticalLayout layout = null;

    private Label numberOfItemsLabel = null;

    private Table experimentsTable = null;

    private final ArrayList<Object> visibleColumnIds = new ArrayList<Object>();

    private final ArrayList<String> visibleColumnLabels = new ArrayList<String>();

    private Button newExperimentButton = null;

    private Button refreshButton = null;

    private Button removeItemButton = null;

    public ExperimentsTab() {
        super(new VerticalLayout());
        layout = getContent();
        layout.setMargin(true, true, true, true);
        setIcon(Icons.experimentTabIcon16);
        setCaption("Experiments");
        layout.addComponent(getToolbar());
        Label header = new Label("Experiments");
        header.setStyleName(Runo.LABEL_H2);
        layout.addComponent(header);
        layout.addComponent(getEditToolbar());
        layout.addComponent(getExperimentTable());
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        newExperimentButton = new Button("New Experiment");
        newExperimentButton.setIcon(Icons.newItemIcon16);
        newExperimentButton.addListener(this);
        hl.addComponent(newExperimentButton);
        return hl;
    }

    private Component getEditToolbar() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        refreshButton = new Button("Refresh");
        refreshButton.setIcon(Icons.refreshIcon16);
        refreshButton.addListener(this);
        buttonLayout.addComponent(refreshButton);
        removeItemButton = new Button("Remove Row");
        removeItemButton.setIcon(Icons.removeIcon16);
        removeItemButton.addListener(this);
        removeItemButton.setEnabled(false);
        buttonLayout.addComponent(removeItemButton);
        numberOfItemsLabel = new Label();
        numberOfItemsLabel.setSizeUndefined();
        layout.addComponent(buttonLayout);
        layout.addComponent(numberOfItemsLabel);
        layout.setComponentAlignment(numberOfItemsLabel, Alignment.BOTTOM_RIGHT);
        return layout;
    }

    private Table getExperimentTable() {
        if (experimentsTable == null) {
            experimentsTable = new Table();
            experimentsTable.addListener(new ItemClickListener() {

                private static final long serialVersionUID = 6014639826212248990L;

                public void itemClick(ItemClickEvent event) {
                    if (event.isDoubleClick()) {
                        if (event.getItemId() != null) {
                            Experiment pojo = (Experiment) event.getItemId();
                            if (pojo != null) {
                                getController().showExperiment(pojo);
                            }
                        }
                    }
                }
            });
            experimentsTable.addListener(new Property.ValueChangeListener() {

                private static final long serialVersionUID = -5137190050955582892L;

                public void valueChange(ValueChangeEvent event) {
                    Collection<?> items = (Collection<?>) event.getProperty().getValue();
                    removeItemButton.setEnabled(items.size() > 0);
                }
            });
            experimentsTable.setSizeFull();
            experimentsTable.setEditable(false);
            experimentsTable.setMultiSelect(true);
            experimentsTable.setMultiSelectMode(MultiSelectMode.DEFAULT);
            experimentsTable.setSelectable(true);
            experimentsTable.setWriteThrough(true);
            experimentsTable.setImmediate(true);
            experimentsTable.setColumnReorderingAllowed(true);
            experimentsTable.setColumnCollapsingAllowed(true);
            experimentsTable.setContainerDataSource(getExperimentContainer());
            visibleColumnIds.add("id");
            visibleColumnIds.add("name");
            visibleColumnIds.add("numberOfPeptides");
            visibleColumnIds.add("owner");
            visibleColumnLabels.add("Exp. ID");
            visibleColumnLabels.add("Name");
            visibleColumnLabels.add("# Peptides");
            visibleColumnLabels.add("Owner");
            experimentsTable.setVisibleColumns(visibleColumnIds.toArray());
            experimentsTable.setColumnHeaders(visibleColumnLabels.toArray(new String[0]));
        }
        updateItemsLabel();
        return experimentsTable;
    }

    private BeanItemContainer<Experiment> getExperimentContainer() {
        BeanItemContainer<Experiment> container = getController().getExperimentContainer();
        return container;
    }

    public void activated(Object... params) {
    }

    public void deactivated(Object... params) {
    }

    public void buttonClick(ClickEvent event) {
        if (event.getButton() == newExperimentButton) {
            newExperimentButtonClick();
        } else if (event.getButton() == refreshButton) {
            refreshExperimentContainer();
        } else if (event.getButton() == removeItemButton) {
            final Object selection = experimentsTable.getValue();
            if (selection == null) {
                return;
            }
            if (selection instanceof Collection) {
                final Collection selectionIndexes = (Collection) selection;
                for (final Object selectedIndex : selectionIndexes) {
                    experimentsTable.unselect(selectedIndex);
                    getController().deleteExperiment((Experiment) selectedIndex);
                    updateItemsLabel();
                }
            }
        }
    }

    private void newExperimentButtonClick() {
        getController().showExperiment(null);
        updateItemsLabel();
    }

    private Controller getController() {
        if (controller == null) {
            controller = MyVaadinApplication.getInstance().getController();
        }
        return controller;
    }

    private void refreshExperimentContainer() {
        getController().refreshExperimentContainer();
        updateItemsLabel();
    }

    private void updateItemsLabel() {
        numberOfItemsLabel.setValue("Items: " + getExperimentContainer().size());
    }
}
