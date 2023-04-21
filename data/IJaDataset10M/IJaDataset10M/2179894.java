package emast.model.problem;

import emast.model.model.AbstractModel;
import java.io.Serializable;

public abstract class AbstractProblem<M extends AbstractModel> implements Cloneable, Serializable {

    private M model;

    public AbstractProblem() {
    }

    public AbstractProblem(final M pModel) {
        model = pModel;
    }

    public void setModel(final M pModel) {
        model = pModel;
    }

    public M getModel() {
        return model;
    }

    @Override
    public String toString() {
        return model.toString() + "\nProblem: \n";
    }

    protected void copyPropertiesTo(AbstractProblem p) {
        p.setModel(model);
    }
}
