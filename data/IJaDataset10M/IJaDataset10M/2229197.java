package com.hy.erp.inventory.service.interfaces;

import java.util.List;
import javax.ejb.Local;
import com.hy.enterprise.framework.service.business.IBusinessService;
import com.hy.erp.inventory.pojo.interfaces.IFamily;
import com.hy.erp.inventory.service.FamilyService;
import com.hy.framework.lang.annotation.ImplementsBy;

@Local
@ImplementsBy(FamilyService.class)
public interface IFamilyService extends IBusinessService<IFamily> {

    public String[] addFamily(Object[] family);

    public boolean modifyFamily(IFamily family);

    public String[] modifyFamily(Object[] family);

    public Integer removeFamily(String[] familyIds);

    public List<IFamily> getFamilyById(String[] Id);

    public List<IFamily> getAllFamily();

    public List<IFamily> getFamilyByQueryName(String queryName, Object[] args);
}
