package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.List;
import no.ugland.utransprod.dao.ProductionUnitDAO;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.model.ProductionUnit;
import no.ugland.utransprod.service.ProductionUnitManager;

public class ProductionUnitManagerImpl extends ManagerImpl<ProductionUnit> implements ProductionUnitManager {

    @Override
    protected Serializable getObjectId(ProductionUnit object) {
        return object.getProductionUnitId();
    }

    public List<ProductionUnit> findByArticleTypeProductAreaGroup(ArticleType articleType, String productAreaGroupName) {
        return ((ProductionUnitDAO) dao).findByArticleTypeProductAreaGroup(articleType, productAreaGroupName);
    }

    public ProductionUnit findByName(String name) {
        return ((ProductionUnitDAO) dao).findByName(name);
    }
}
