package com.plato.etohum.client;

import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.plato.etohum.client.System.Season;

public class AddSeasonPanel {

    public AddSeasonPanel(VerticalPanel loadPanel) {
        FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Yeni Sezon Ekle");
        formPanel.setFrame(true);
        final TextField<String> firstName = new TextField<String>();
        firstName.setFieldLabel("Sezon Adı");
        firstName.setAllowBlank(false);
        MyMessages myMessages = new MyMessages();
        formPanel.add(firstName);
        final DateField startDate = new DateField();
        startDate.setFieldLabel("Başlangıç Tarihi");
        formPanel.add(startDate);
        final DateField finishDate = new DateField();
        finishDate.setFieldLabel("Bitiş Tarihi");
        formPanel.add(finishDate);
        Button saveButton = new Button("Kaydet", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Season seasonbean = new Season();
                seasonbean.setEndDate(finishDate.getValue());
                seasonbean.setStartDate(startDate.getValue());
                seasonbean.setSessionName(firstName.getValue());
            }
        });
        Button cancelButton = new Button("İptal");
        formPanel.add(saveButton);
        formPanel.add(cancelButton);
        loadPanel.add(formPanel);
        formPanel.setWidth("600");
    }
}
