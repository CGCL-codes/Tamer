package com.example.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractSelect;
import com.example.vaadin.VaadinExampleUtils;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author fabio
 */
public class MarkerComboBox extends VerticalLayout implements Property.ValueChangeListener {

    public MarkerComboBox() {
        setSpacing(true);
        ComboBox l = new ComboBox("Please select your country", VaadinExampleUtils.getISO3166Container());
        l.setItemCaptionPropertyId(VaadinExampleUtils.iso3166_PROPERTY_NAME);
        l.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        l.setItemIconPropertyId(VaadinExampleUtils.iso3166_PROPERTY_FLAG);
        l.setItemIcon(this, new ThemeResource("../runo/icons/16/ok.png"));
        l.setWidth(350, UNITS_PIXELS);
        l.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
        l.setImmediate(true);
        l.addListener(this);
        l.setNullSelectionAllowed(false);
        addComponent(l);
    }

    public void valueChange(ValueChangeEvent event) {
        Property selected = VaadinExampleUtils.getISO3166Container().getContainerProperty(event.getProperty().toString(), "name");
        getWindow().showNotification("Selected country: " + selected);
    }
}
