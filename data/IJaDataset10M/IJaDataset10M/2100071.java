package ua.org.nuos.sdms.clientgui.client.components.factory;

import com.vaadin.data.Item;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.FormLayout;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 25.03.12
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class FormFactory {

    public static Form getForm(FormFieldFactory fieldFactory, Item dataSource, String[] properties) {
        Form form = new Form();
        initForm(form, fieldFactory, dataSource, properties);
        return form;
    }

    public static void initForm(Form form, FormFieldFactory fieldFactory, Item dataSource, String[] properties) {
        form.setSizeFull();
        form.setFormFieldFactory(fieldFactory);
        form.setItemDataSource(dataSource);
        form.setVisibleItemProperties(properties);
        form.addStyleName("custom_100px_width_form");
    }
}
