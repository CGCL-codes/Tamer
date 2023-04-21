package cw.customermanagementmodul.customer.extention;

import java.util.ArrayList;
import java.util.List;
import cw.boardingschoolmanagement.extention.point.CWIViewExtentionPoint;
import cw.boardingschoolmanagement.gui.component.CWView;
import cw.customermanagementmodul.customer.gui.CustomerOverviewEditCustomerPresentationModel;
import cw.customermanagementmodul.customer.gui.CustomerOverviewEditCustomerView;
import cw.customermanagementmodul.customer.gui.EditCustomerPresentationModel;
import cw.customermanagementmodul.customer.gui.EditCustomerView;
import cw.customermanagementmodul.customer.gui.GeneralCustomerOverviewEditCustomerPresentationModel;
import cw.customermanagementmodul.customer.gui.GeneralCustomerOverviewEditCustomerView;

/**
 *
 * @author Manuel Geier
 */
public class GeneralCustomerOverviewEditCustomerExtention implements CWIViewExtentionPoint<CustomerOverviewEditCustomerView, CustomerOverviewEditCustomerPresentationModel> {

    private GeneralCustomerOverviewEditCustomerPresentationModel model;

    private GeneralCustomerOverviewEditCustomerView view;

    @Override
    public void initPresentationModel(CustomerOverviewEditCustomerPresentationModel baseModel) {
        model = new GeneralCustomerOverviewEditCustomerPresentationModel(baseModel);
    }

    @Override
    public Class<?> getExtentionClass() {
        return null;
    }

    @Override
    public List<Class<?>> getExtentionClassList() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(CustomerOverviewEditCustomerView.class);
        list.add(CustomerOverviewEditCustomerPresentationModel.class);
        return list;
    }

    @Override
    public void initView(CustomerOverviewEditCustomerView baseView) {
        view = new GeneralCustomerOverviewEditCustomerView(model, baseView);
    }

    @Override
    public CWView<?> getView() {
        return view;
    }
}
