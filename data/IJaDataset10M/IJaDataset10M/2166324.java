package com.scs.base.dao.part;

import java.util.List;
import com.scs.base.model.part.PartFamily;

public interface PartFamilyDAO {

    public List getPartFamilies();

    public PartFamily getPartFamily(Long partFamilyId);

    public void savePartFamily(PartFamily partFamily);

    public void removePartFamily(Long partFamilyId);

    public void saveAllPartFamilies(List partFamilyList);

    public Long getPartFamilyId(String partFamilyName);

    public List getPartFamiliesByName(String partFamilyName);
}
