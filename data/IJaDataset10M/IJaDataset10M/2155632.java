package com.amidasoft.lincat.session;

import com.amidasoft.lincat.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("editorialsContactesHome")
public class EditorialsContactesHome extends EntityHome<EditorialsContactes> {

    public void setEditorialsContactesId(Integer id) {
        setId(id);
    }

    public Integer getEditorialsContactesId() {
        return (Integer) getId();
    }

    @Override
    protected EditorialsContactes createInstance() {
        EditorialsContactes editorialsContactes = new EditorialsContactes();
        return editorialsContactes;
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public EditorialsContactes getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
