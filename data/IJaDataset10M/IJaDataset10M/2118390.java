package funiture.infrastructure.persistence;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import funiture.domains.model.bom.BarCode;
import funiture.domains.model.bom.MaterialObject;
import funiture.domains.model.bom.MaterialObjectModel;
import funiture.domains.model.bom.MaterialObjectRespository;
import furniture.core.dao.QueryObjectProxyFactory;
import furniture.core.dao.RepositoryFacade;
import furniture.util.Util;

@Repository("materialObjectRespository")
public class MaterialObjectRespositoryImpl implements MaterialObjectRespository {

    @Autowired
    private RepositoryFacade repositoryFacade;

    @Override
    public void store(MaterialObjectModel materialObjectModel) {
        repositoryFacade.create(materialObjectModel);
    }

    @Override
    public void store(MaterialObject materialObject) {
        repositoryFacade.create(materialObject);
    }

    @Override
    public MaterialObject findBy(BarCode barCode) {
        DetachedCriteria detachedCriteria = QueryObjectProxyFactory.getProxy(MaterialObject.class).eq("barCode", barCode).getDetachedCriteria();
        List<MaterialObject> findBy = repositoryFacade.findBy(detachedCriteria);
        return Util.getFirst(findBy);
    }
}
