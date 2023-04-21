package es.ehrflex.client.main.component;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import es.ehrflex.client.main.medicaldata.model.DataObjectGTO;

/**
 * Extension of normal TextField<E> to have a better constructor <br>
 * 
 * @author Anton Brass
 * @version 1.0, 12.05.2009
 */
public class EHRflexCheckBox extends CheckBox {

    DataObjectGTO bindedObject;

    public DataObjectGTO getDataObject() {
        return bindedObject;
    }

    public void setDataObject(DataObjectGTO pDataObject) {
        this.bindedObject = pDataObject;
    }

    public EHRflexCheckBox(String label) {
        super();
        this.setFieldLabel(label);
    }
}
