package cw.customermanagementmodul.extention;

import cw.boardingschoolmanagement.gui.component.CWPanel;
import cw.customermanagementmodul.extention.point.EditCustomerTabExtentionPoint;
import cw.customermanagementmodul.gui.EditCustomerEditCustomerPresentationModel;
import cw.customermanagementmodul.gui.EditCustomerEditCustomerView;
import cw.customermanagementmodul.gui.EditCustomerPresentationModel;
import java.util.List;

/**
 *
 * @author Manuel Geier
 */
public class EditCustomerEditCustomerTabExtention implements EditCustomerTabExtentionPoint {

    private EditCustomerEditCustomerPresentationModel model;

    private EditCustomerEditCustomerView view;

    private EditCustomerPresentationModel editCustomerModel;

    public void initPresentationModel(EditCustomerPresentationModel editCustomerModel) {
        this.editCustomerModel = editCustomerModel;
        model = new EditCustomerEditCustomerPresentationModel(editCustomerModel);
        view = new EditCustomerEditCustomerView(model);
    }

    public CWPanel getView() {
        return view;
    }

    public Object getModel() {
        return model;
    }

    public void save() {
        model.save();
    }

    public List<String> validate() {
        return null;
    }

    public void dispose() {
        view.dispose();
    }

    public int priority() {
        return 90;
    }
}
