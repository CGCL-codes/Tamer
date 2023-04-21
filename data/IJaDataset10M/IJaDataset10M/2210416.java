package engine.view;

import engine.model.DataModel;

/**
 *
 * @author dosER
 */
public class BaseView<T> {

    String context = "";

    DataModel<T> model = null;

    public BaseView(DataModel<T> model, String context) {
        this.model = model;
        this.context = context;
    }

    public DataModel getModel() {
        return model;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }
}
