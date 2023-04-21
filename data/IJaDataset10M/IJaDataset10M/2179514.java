package cw.customermanagementmodul.gui;

/**
 *
 * @author CreativeWorkers.at
 */
public class GeneralCustomerOverviewEditCustomerPresentationModel {

    private CustomerOverviewEditCustomerPresentationModel customerOverviewEditCustomerPresentationModel;

    public GeneralCustomerOverviewEditCustomerPresentationModel(CustomerOverviewEditCustomerPresentationModel customerOverviewEditCustomerPresentationModel) {
        this.customerOverviewEditCustomerPresentationModel = customerOverviewEditCustomerPresentationModel;
        initModels();
        initEventHandling();
    }

    public void initModels() {
    }

    public void initEventHandling() {
    }

    public EditCustomerPresentationModel getEditCustomerPresentationModel() {
        return customerOverviewEditCustomerPresentationModel.getEditCustomerPresentationModel();
    }

    public void dispose() {
    }
}
