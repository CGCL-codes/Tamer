package org.homeunix.thecave.buddi.model;

import java.util.Date;
import org.homeunix.thecave.buddi.model.beans.ModelObjectBean;
import org.homeunix.thecave.buddi.model.exception.DataModelProblemException;

public abstract class ModelObjectImpl implements ModelObject {

    private final DataModel model;

    private final ModelObjectBean bean;

    public ModelObjectImpl(DataModel model, ModelObjectBean bean) {
        if (model == null) throw new DataModelProblemException("Model cannot be null", getModel());
        if (bean == null) throw new DataModelProblemException("Model Bean cannot be null.", getModel());
        this.model = model;
        this.bean = bean;
    }

    public DataModel getModel() {
        return model;
    }

    public ModelObjectBean getBean() {
        return bean;
    }

    /**
	 * Call this from the model absraction layer after all operations which
	 * change a value. 
	 */
    public void modify() {
        getBean().setModifiedDate(new Date());
    }

    @Override
    public int hashCode() {
        return bean.getUid().hashCode();
    }

    public int compareTo(ModelObject o) {
        return this.toString().compareTo(o.toString());
    }

    public boolean equals(Object obj) {
        if (obj instanceof ModelObject) return getBean().equals(((ModelObject) obj).getBean());
        return false;
    }

    /**
	 * Returns the UID string for this object.
	 * @return
	 */
    public String getUid() {
        return getBean().getUid();
    }
}
