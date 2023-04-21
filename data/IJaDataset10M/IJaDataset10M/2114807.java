package twoadw.wicket.website.addresses;

import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.modelibra.wicket.util.LocalizedText;
import twoadw.website.address.Address;
import twoadw.website.address.Addresss;
import twoadw.website.invoice.Invoices;
import twoadw.wicket.app.twoadw.TwoAdwBasePage;
import twoadw.wicket.app.twoadw.transaction.CheckoutPage;
import twoadw.wicket.website.customers.CustomerInfoPage;
import twoadw.wicket.website.productcategories.CategoryListPage;

public class InputAddressPage extends TwoAdwBasePage {

    private Page tempPage = new CategoryListPage();

    public InputAddressPage(final Address address, String ResponsePage) {
        add(new FeedbackPanel("feedback"));
        if (ResponsePage == "Checkout") tempPage = new CheckoutPage();
        if (ResponsePage == "CustomerInfoPage") tempPage = new CustomerInfoPage();
        Label title = new Label("title", "New Address");
        if (address != null) title.setModelObject("Modify " + address.getAddressName() + " address");
        add(title);
        final Form form = new Form("form");
        add(form);
        final Address newAddress = new Address(getTwoadwAppSession().getCustomer());
        final TextField addressName = new TextField("addressName");
        addressName.setModel(new PropertyModel(newAddress, "addressName"));
        addressName.setRequired(true);
        addressName.add(StringValidator.maximumLength(32));
        form.add(addressName);
        final TextField street = new TextField("street");
        street.setModel(new PropertyModel(newAddress, "street"));
        street.setRequired(true);
        street.add(StringValidator.maximumLength(64));
        form.add(street);
        final TextField city = new TextField("city");
        city.setModel(new PropertyModel(newAddress, "city"));
        city.setRequired(true);
        city.add(StringValidator.maximumLength(32));
        form.add(city);
        final TextField zipCode = new TextField("zipCode");
        zipCode.setModel(new PropertyModel(newAddress, "zipCode"));
        zipCode.setRequired(true);
        zipCode.add(StringValidator.maximumLength(16));
        form.add(zipCode);
        final TextField state = new TextField("state");
        state.setModel(new PropertyModel(newAddress, "state"));
        state.setRequired(true);
        state.add(StringValidator.maximumLength(32));
        form.add(state);
        final TextField country = new TextField("country");
        country.setModel(new PropertyModel(newAddress, "country"));
        country.setRequired(true);
        country.add(StringValidator.maximumLength(32));
        form.add(country);
        final TextField telephone = new TextField("telephone");
        telephone.setModel(new PropertyModel(newAddress, "telephone"));
        telephone.setRequired(true);
        telephone.add(StringValidator.maximumLength(32));
        form.add(telephone);
        form.add(new Link("cancel") {

            @Override
            public void onClick() {
                Invoices invoices = getInvoices();
                invoices.remove(getInvoice());
                setResponsePage(new CategoryListPage());
            }
        });
        if (address == null) {
            addressName.setModelValue("Default");
            form.add(new Button("save") {

                @Override
                public void onSubmit() {
                    Addresss addresss = getTwoadwAppSession().getCustomer().getAddresss();
                    if (addresss.add(newAddress)) {
                        getTwoadwAppSession().setAddress(newAddress);
                        setResponsePage(getTwoadwAppSession().getContextPage());
                    } else {
                        List<String> errorKeys = addresss.getErrors().getKeyList();
                        for (String errorKey : errorKeys) {
                            String errorMsg = LocalizedText.getErrorMessage(this, errorKey);
                            form.error(errorMsg);
                        }
                    }
                }
            });
        } else {
            addressName.setEnabled(false);
            addressName.setModelValue(address.getAddressName());
            street.setModelValue(address.getStreet());
            city.setModelValue(address.getCity());
            zipCode.setModelValue(address.getZipCode());
            state.setModelValue(address.getState());
            country.setModelValue(address.getCountry());
            telephone.setModelValue(address.getTelephone());
            form.add(new Button("save") {

                @Override
                public void onSubmit() {
                    Addresss addresss = getTwoadwAppSession().getCustomer().getAddresss();
                    if (addresss.update(address, newAddress)) {
                        getTwoadwAppSession().setAddress(address);
                        setResponsePage(getTwoadwAppSession().getContextPage());
                    } else {
                        List<String> errorKeys = addresss.getErrors().getKeyList();
                        for (String errorKey : errorKeys) {
                            String errorMsg = LocalizedText.getErrorMessage(this, errorKey);
                            form.error(errorMsg);
                        }
                    }
                }
            });
        }
    }
}
