package com.extjs.serverside.sample.form;

import com.extjs.serverside.form.Checkbox;
import com.extjs.serverside.form.DateField;
import com.extjs.serverside.form.Form;
import com.extjs.serverside.form.TextField;

public class DemoForm extends Form {

    public DemoForm() {
        super("demoform");
        addFieldSet("Personal information", new TextField("firstName", "First Name"), new TextField("lastName", "Last Name"), new DateField("birthDate", "Birth Date"), new Checkbox("validUser", "Valid User"));
        addInitScript("demoform.render('myForm');");
    }
}
