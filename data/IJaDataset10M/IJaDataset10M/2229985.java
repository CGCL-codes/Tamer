package cw.customermanagementmodul.extention;

import cw.boardingschoolmanagement.gui.HomePresentationModel;
import cw.customermanagementmodul.gui.CustomerHomeExtentionPresentationModel;
import cw.customermanagementmodul.gui.CustomerHomeExtentionView;
import cw.boardingschoolmanagement.extention.point.HomeExtentionPoint;
import cw.boardingschoolmanagement.gui.component.CWPanel;

/**
 *
 * @author ManuelG
 */
public class CustomerHomeExtention implements HomeExtentionPoint {

    private CustomerHomeExtentionPresentationModel model;

    private CustomerHomeExtentionView view;

    public void initPresentationModel(HomePresentationModel homePresentationModel) {
        model = new CustomerHomeExtentionPresentationModel();
        view = new CustomerHomeExtentionView(model);
    }

    public CWPanel getView() {
        return view;
    }

    public void dispose() {
        view.dispose();
    }

    public Object getModel() {
        return model;
    }
}
