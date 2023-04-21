package com.ttdev.wicketpagetest.sample.spring;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class Page1 extends WebPage {

    @SpringBean
    private MyService service;

    private String input;

    private String result;

    public Page1() {
        input = service.getDefaultInput();
        Form<Page1> form = new Form<Page1>("form", new CompoundPropertyModel<Page1>(this)) {

            @Override
            protected void onSubmit() {
                result = service.getResult(input);
            }
        };
        add(form);
        form.add(new TextField<String>("input"));
        add(new Label("result", new PropertyModel<String>(this, "result")));
    }
}
