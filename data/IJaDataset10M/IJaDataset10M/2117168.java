package no.ugland.utransprod.dao;

import java.util.List;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.model.ProductionUnit;

public interface ProductionUnitDAO extends DAO<ProductionUnit> {

    List<ProductionUnit> findByArticleTypeProductAreaGroup(ArticleType articleType, String productAreaGroupName);

    ProductionUnit findByName(String name);
}
