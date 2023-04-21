package ch.kerbtier.malurus.components;

import ch.kerbtier.malurus.coreimpl.CiComponent;
import ch.kerbtier.malurus.models.ListModel;
import ch.kerbtier.malurus.models.Model;

public class SingleSelect<T> extends CiComponent {

    private ListModel<T> model = null;

    private Model<T> selectionModel = null;

    private boolean submitOnSelect;

    public boolean isSubmitOnSelect() {
        return submitOnSelect;
    }

    public void setSubmitOnSelect(boolean submitOnSelect) {
        this.submitOnSelect = submitOnSelect;
    }

    public ListModel<T> getModel() {
        return model;
    }

    public void setModel(ListModel<T> model) {
        this.model = model;
    }

    public Model<T> getSelectionModel() {
        return selectionModel;
    }

    public void setSelectionModel(Model<T> selectionModel) {
        this.selectionModel = selectionModel;
    }
}
