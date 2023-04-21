package com.amidasoft.lincat.session;

import com.amidasoft.lincat.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("formacionsHome")
public class FormacionsHome extends EntityHome<Formacions> {

    public void setFormacionsId(Integer id) {
        setId(id);
    }

    public Integer getFormacionsId() {
        return (Integer) getId();
    }

    @Override
    protected Formacions createInstance() {
        Formacions formacions = new Formacions();
        return formacions;
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Formacions getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<EmpresesFormacions> getEmpresesFormacionses() {
        return getInstance() == null ? null : new ArrayList<EmpresesFormacions>(getInstance().getEmpresesFormacionses());
    }
}
