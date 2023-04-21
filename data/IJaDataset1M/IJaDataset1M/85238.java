package org.yournamehere.client.person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.*;

/**
 * Example class using the PersonService service.
 *
 * @author Mufasa
 */
public class PersonForm extends Panel {

    static final TextField txtName = new TextField("Фамилия", "name", 190);

    static final TextField txtSurname = new TextField("Имя", "surname", 190);

    static final TextField txtPatronymic = new TextField("Отчество", "patronymic", 190);

    static final TextField txtEmail = new TextField("Email", "email", 190);

    static final TextField txtAge = new TextField("Возвраст", "age", 190);

    public PersonForm() {
        final AsyncCallback<Person> callback = new AsyncCallback<Person>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Извините, произошла какая-то ошибка!");
            }

            public void onSuccess(Person result) {
                txtName.setValue(result.getName());
                txtSurname.setValue(result.getSurname());
                txtPatronymic.setValue(result.getPatronymic());
                txtAge.setValue(String.valueOf(result.getAge()));
                txtEmail.setValue(result.getEmail());
            }
        };
        setBorder(false);
        setPaddings(15);
        final FormPanel formPanel = new FormPanel(Position.CENTER);
        formPanel.setFrame(true);
        formPanel.setTitle("Тестовая формочка");
        formPanel.setWidth(500);
        formPanel.setLabelWidth(100);
        FieldSet fieldSet = new FieldSet("Человек");
        fieldSet.add(txtName);
        fieldSet.add(txtSurname);
        fieldSet.add(txtPatronymic);
        fieldSet.add(txtEmail);
        fieldSet.add(txtAge);
        txtEmail.setVtype(VType.EMAIL);
        txtAge.setVtype(VType.ALPHANUM);
        fieldSet.setWidth(formPanel.getWidth() - 20);
        final Button btnLoad = new Button("Загрузить", new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                getService().loadPerson(createSomePerson(), callback);
            }
        });
        formPanel.add(fieldSet);
        formPanel.addButton(btnLoad);
        add(formPanel);
    }

    public static PersonServiceAsync getService() {
        PersonServiceAsync service = (PersonServiceAsync) GWT.create(PersonService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "personservice";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        return service;
    }

    public static Person createSomePerson() {
        Person p = new Person();
        p.setAge(21);
        p.setEmail("bac1ca@mail.ru");
        p.setName("bac1ca");
        p.setPatronymic("sofe");
        p.setSurname("sasassasasas");
        return p;
    }
}
