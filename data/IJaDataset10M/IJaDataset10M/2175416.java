package cw.customermanagementmodul.customer.extention;

import java.util.ArrayList;
import java.util.List;
import cw.boardingschoolmanagement.extention.point.CWIViewExtentionPoint;
import cw.boardingschoolmanagement.gui.component.CWView;
import cw.customermanagementmodul.customer.gui.CustomerOverviewEditCustomerPresentationModel;
import cw.customermanagementmodul.customer.gui.CustomerOverviewEditCustomerView;
import cw.customermanagementmodul.customer.gui.EditCustomerPresentationModel;
import cw.customermanagementmodul.customer.gui.EditCustomerView;

/**
 *
 * @author Manuel Geier
 */
public class CustomerOverviewEditCustomerTabExtention implements CWIViewExtentionPoint<EditCustomerView, EditCustomerPresentationModel> {

    private CustomerOverviewEditCustomerPresentationModel model;

    private CustomerOverviewEditCustomerView view;

    @Override
    public void initPresentationModel(EditCustomerPresentationModel baseModel) {
        model = new CustomerOverviewEditCustomerPresentationModel(baseModel, baseModel.getEntityManager());
    }

    @Override
    public Class<?> getExtentionClass() {
        return null;
    }

    @Override
    public List<Class<?>> getExtentionClassList() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(EditCustomerView.class);
        list.add(EditCustomerPresentationModel.class);
        return list;
    }

    @Override
    public void initView(EditCustomerView baseView) {
        view = new CustomerOverviewEditCustomerView(model, baseView);
    }

    @Override
    public CWView<?> getView() {
        return view;
    }
}
