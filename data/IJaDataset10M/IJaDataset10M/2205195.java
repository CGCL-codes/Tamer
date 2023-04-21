package org.gwtoolbox.sample.bean.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwtoolbox.bean.client.BeanInfo;
import org.gwtoolbox.bean.client.BeanInfoRegistry;
import org.gwtoolbox.bean.client.PropertyDescriptor;
import org.gwtoolbox.bean.client.attributes.FormatAttributes;
import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Uri Boness
 */
public class BeanSampleApp implements EntryPoint {

    public void onModuleLoad() {
        try {
            doLoad();
        } catch (RuntimeException re) {
            re.printStackTrace(System.err);
        }
    }

    public void doLoad() {
        FlowPanel main = new FlowPanel();
        final BeanInfo<Person> beanInfo = BeanInfoRegistry.get().getBeanInfo(Person.class);
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        main.add(new HTML("<h1>Person Class<h1>"));
        main.add(new HTML("<h3>Meta Data:</h3>"));
        main.add(new HTML("<b>Name:</b> " + beanInfo.getDisplayName()));
        main.add(new HTML("<b>Description:</b> " + beanInfo.getDescription()));
        main.add(new HTML("<h3>Properties:</h3>"));
        Grid grid = new Grid(descriptors.length + 1, 14);
        grid.setBorderWidth(1);
        grid.setCellPadding(2);
        grid.setCellSpacing(2);
        grid.setHTML(0, 0, "<b>Name</b>");
        grid.setHTML(0, 1, "<b>Type</b>");
        grid.setHTML(0, 2, "<b>Component Type</b>");
        grid.setHTML(0, 3, "<b>Display Name</b>");
        grid.setHTML(0, 4, "<b>Description</b>");
        grid.setHTML(0, 5, "<b>Type.isDate()</b>");
        grid.setHTML(0, 6, "<b>Type.isNumber()</b>");
        grid.setHTML(0, 7, "<b>Type.isPrimitive()</b>");
        grid.setHTML(0, 8, "<b>Type.isBean()</b>");
        grid.setHTML(0, 9, "<b>Type.isCollection()</b>");
        grid.setHTML(0, 10, "<b>Type.isMap()</b>");
        grid.setHTML(0, 11, "<b>Type.isArray()</b>");
        grid.setHTML(0, 12, "<b>Type.isEnum()</b>");
        grid.setHTML(0, 13, "<b>Format Attributes</b>");
        int row = 1;
        for (PropertyDescriptor<?, ?> descriptor : descriptors) {
            grid.setHTML(row, 0, descriptor.getName());
            grid.setHTML(row, 1, descriptor.getType().getTypeClass().getName());
            grid.setHTML(row, 2, (descriptor.getType().isArray() || descriptor.getType().isCollection()) ? descriptor.getType().getComponentType().getTypeClass().getName() : "");
            grid.setHTML(row, 3, descriptor.getDisplayName());
            grid.setHTML(row, 4, descriptor.getDescription());
            grid.setHTML(row, 5, String.valueOf(descriptor.getType().isDate()));
            grid.setHTML(row, 6, String.valueOf(descriptor.getType().isNumber()));
            grid.setHTML(row, 7, String.valueOf(descriptor.getType().isPrimitive()));
            grid.setHTML(row, 8, String.valueOf(descriptor.getType().isBean()));
            grid.setHTML(row, 9, String.valueOf(descriptor.getType().isCollection()));
            grid.setHTML(row, 10, String.valueOf(descriptor.getType().isMap()));
            grid.setHTML(row, 11, String.valueOf(descriptor.getType().isArray()));
            grid.setHTML(row, 12, String.valueOf(descriptor.getType().isEnum()));
            grid.setHTML(row, 13, descriptor.hasAttributes(FormatAttributes.class) ? descriptor.getAttributes(FormatAttributes.class).value() : "");
            row++;
        }
        main.add(grid);
        grid.getElement().getStyle().setMarginBottom(40, Style.Unit.PX);
        final Person person = beanInfo.newInstance();
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Label("Person's Name: "));
        final TextBox nameBox = new TextBox();
        hp.add(nameBox);
        Button setNameButton = new Button("Set Name", new ClickHandler() {

            public void onClick(ClickEvent event) {
                String name = nameBox.getText();
                beanInfo.getPropertyDescriptor("name").setValue(person, name);
            }
        });
        hp.add(setNameButton);
        Button getNameButton = new Button("Get Name", new ClickHandler() {

            public void onClick(ClickEvent event) {
                String name = (String) beanInfo.getPropertyDescriptor("name").getValue(person);
                Window.alert(name);
            }
        });
        hp.add(getNameButton);
        final FlowPanel messages = new FlowPanel();
        Button validateButton = new Button("Validate", new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                messages.clear();
                Set<ConstraintViolation<Person>> constraints = beanInfo.getValidator().validate(person, CompleteGroup.class);
                for (ConstraintViolation<Person> constraint : constraints) {
                    String message = "<b>ItemName:</b> " + constraint.getPropertyPath().toString() + ", <b>Message:</b> " + constraint.getMessage() + ", <b>Property Path:</b> " + constraint.getPropertyPath();
                    messages.add(new HTML(message));
                }
            }
        });
        hp.add(validateButton);
        main.add(hp);
        hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
        hp.add(new Label("Person Email: "));
        final TextBox emailBox = new TextBox();
        emailBox.setTitle("Email");
        hp.add(emailBox);
        Button setEmailButton = new Button("Set Email", new ClickHandler() {

            public void onClick(ClickEvent event) {
                beanInfo.getPathValueProxy("email").setValue(person, emailBox.getText());
            }
        });
        hp.add(setEmailButton);
        Button getEmailButton = new Button("Get Email", new ClickHandler() {

            public void onClick(ClickEvent event) {
                Window.alert((String) beanInfo.getPathValueProxy("email").getValue(person));
            }
        });
        hp.add(getEmailButton);
        main.add(hp);
        hp = new HorizontalPanel();
        hp.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
        hp.add(new Label("Person Main Adress Street: "));
        final TextBox streetBox = new TextBox();
        streetBox.setTitle("Street Box");
        hp.add(streetBox);
        Button setStreeButton = new Button("Set Street", new ClickHandler() {

            public void onClick(ClickEvent event) {
                beanInfo.getPathValueProxy("mainAddress.street").setValue(person, streetBox.getText());
            }
        });
        hp.add(setStreeButton);
        Button getStreetButton = new Button("Get Street", new ClickHandler() {

            public void onClick(ClickEvent event) {
                Window.alert((String) beanInfo.getPathValueProxy("mainAddress.street").getValue(person));
            }
        });
        hp.add(getStreetButton);
        main.add(hp);
        main.add(messages);
        RootPanel.get().add(main);
    }
}
