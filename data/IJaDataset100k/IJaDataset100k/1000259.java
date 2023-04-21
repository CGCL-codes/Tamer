package com.mirovicjovan.carmachine.tapestry.pages;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import com.mirovicjovan.carmachine.core.tapestry.SpringBean;
import com.mirovicjovan.carmachine.model.Manufacturer;
import com.mirovicjovan.carmachine.model.User;
import com.mirovicjovan.carmachine.model.beans.ManufacturerBean;
import com.mirovicjovan.carmachine.service.admin.impl.WriteCommand;

public class AddManufacturer {

    @ApplicationState
    private User user;

    private boolean userExists;

    Object onActivate() {
        if (!userExists) return Index.class; else {
            if (!user.isAdmin()) return Index.class;
        }
        return null;
    }

    @Property
    @Persist("flash")
    private Manufacturer manufacturer;

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Inject
    @SpringBean("com.mirovicjovan.carmachine.service.admin.impl.WriteCommand")
    private WriteCommand<Manufacturer> writer;

    @OnEvent(value = "submit", component = "manufacturerCreationForm")
    void onFormSubmit() {
        manufacturer = new ManufacturerBean();
        manufacturer.setName(getName());
        manufacturer.setDescription(getDescription());
        writer.setInput(manufacturer);
        writer.execute();
        System.out.println("\n\n\n\n\n" + manufacturer.getName() + " " + manufacturer.getDescription() + "\n\n\n\n\n");
    }
}
