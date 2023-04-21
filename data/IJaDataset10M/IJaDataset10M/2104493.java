package org.domain.analyticalcrm.session;

import org.domain.analyticalcrm.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("postalCodeHome")
public class PostalCodeHome extends EntityHome<PostalCode> {

    @In(create = true)
    TownHome townHome;

    public void setPostalCodeIdPostalCode(Integer id) {
        setId(id);
    }

    public Integer getPostalCodeIdPostalCode() {
        return (Integer) getId();
    }

    @Override
    protected PostalCode createInstance() {
        PostalCode postalCode = new PostalCode();
        return postalCode;
    }

    public void wire() {
        getInstance();
        Town town = townHome.getDefinedInstance();
        if (town != null) {
            getInstance().setTown(town);
        }
    }

    public boolean isWired() {
        return true;
    }

    public PostalCode getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
