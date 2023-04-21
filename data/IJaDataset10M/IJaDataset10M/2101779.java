package com.amidasoft.lincat.session;

import com.amidasoft.lincat.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("formulesHome")
public class FormulesHome extends EntityHome<Formules> {

    public void setFormulesId(Integer id) {
        setId(id);
    }

    public Integer getFormulesId() {
        return (Integer) getId();
    }

    @Override
    protected Formules createInstance() {
        Formules formules = new Formules();
        return formules;
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Formules getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
