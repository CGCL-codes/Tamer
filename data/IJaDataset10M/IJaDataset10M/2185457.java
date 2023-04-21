package org.vikamine.swing.subgroup.event.connect;

import org.vikamine.app.event.OntologyChangeListener;
import org.vikamine.app.event.OntologyChangedEvent;
import org.vikamine.app.event.PopulationChangeEvent;
import org.vikamine.app.event.PopulationChangeListener;
import org.vikamine.swing.subgroup.editors.tuningtable.TuningTableController;

public class OntoDMManagerToInteractionTableWire implements OntologyChangeListener, PopulationChangeListener {

    private TuningTableController controller;

    public OntoDMManagerToInteractionTableWire() {
        super();
    }

    public OntoDMManagerToInteractionTableWire(TuningTableController controller) {
        this.controller = controller;
    }

    public void ontologyChanged(OntologyChangedEvent eve) {
        controller.retrievePopulation();
        controller.clearTables();
        controller.removeAllColumns();
    }

    public void populationChanged(PopulationChangeEvent e) {
        controller.retrievePopulation();
        controller.fireTableStructureChanged();
    }

    public TuningTableController getController() {
        return controller;
    }

    public void setController(TuningTableController controller) {
        this.controller = controller;
    }
}
