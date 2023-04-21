package org.stat.model.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.stat.model.Model;
import org.stat.model.comparison.ModelErrComparator;

/**
 *
 * @author stewari1
 */
public class ModelSelection {

    private List<Model> models = new ArrayList<Model>();

    private ModelErrComparator modelErrComparator;

    private boolean modelsOrdered = false;

    public ModelSelection() {
    }

    public void setModelErrComparator(ModelErrComparator modelErrComparator) {
        this.modelErrComparator = modelErrComparator;
        this.modelsOrdered = false;
    }

    public void addModel(Model model) {
        this.models.add(model);
        this.modelsOrdered = false;
    }

    public void removeModel(Model model) {
        this.models.remove(model);
        this.modelsOrdered = false;
    }

    public Model[] getOrderModels() {
        if (!this.modelsOrdered) {
            Collections.sort(this.models, this.modelErrComparator);
            this.modelsOrdered = true;
        }
        return this.models.toArray(new Model[0]);
    }

    public Model getBestModel() {
        return getOrderModels()[0];
    }
}
