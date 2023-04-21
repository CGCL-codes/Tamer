package com.example.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import consumerest.VvffServiceREST;
import it.polimi.miaria.socialemis.partner.db.Vvff;

@SuppressWarnings("serial")
public class ListVvffSingleSelect extends VerticalLayout implements Property.ValueChangeListener {

    protected static String globalSelectedVvff;

    public ListVvffSingleSelect() {
        setSpacing(true);
        Vvff[] vvffs = VvffServiceREST.getAllVvffs();
        ListSelect vvffSelect = new ListSelect("1.Choose the vvff in the following list:");
        for (int i = 0; i < vvffs.length; i++) {
            vvffSelect.addItem(vvffs[i].getIdStation());
        }
        vvffSelect.setWidth("300px");
        vvffSelect.setRows(7);
        vvffSelect.setNullSelectionAllowed(false);
        vvffSelect.setImmediate(true);
        vvffSelect.addListener(this);
        addComponent(vvffSelect);
    }

    public void valueChange(ValueChangeEvent event) {
        globalSelectedVvff = event.getProperty().toString();
        TabsheetComponent.getCamionServiceButton.setEnabled(true);
        TabsheetComponent.getFiremanServiceButton.setEnabled(true);
        MyApplication.saveMarkerVvffButton.setEnabled(true);
    }
}
