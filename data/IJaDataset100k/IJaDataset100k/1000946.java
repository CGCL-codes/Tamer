package net.sf.brightside.overlord.web.pages;

import net.sf.brightside.overlord.domain.DomainObject;
import net.sf.brightside.overlord.system.OverLordFactory;
import net.sf.brightside.overlord.system.OverLordPersister;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;

public class DeleteProduct {

    private OverLordPersister persister = (OverLordPersister) OverLordFactory.getBean("overlordPersister");

    @Persist
    private String status;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @OnEvent(value = "submit", component = "userInputForm")
    void onFormSubmit() {
        DomainObject object = (DomainObject) OverLordFactory.getBean("product");
        try {
            object = persister.load(object, "name", name);
            persister.delete(object);
        } catch (Exception e) {
            setStatus("Delete operation failure!");
            return;
        }
        setStatus("Delete operation success!");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
