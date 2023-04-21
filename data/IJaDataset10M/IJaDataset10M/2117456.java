package com.tll.service.entity.product;

import javax.validation.ValidatorFactory;
import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ProductCategory;
import com.tll.service.entity.NamedEntityService;

/**
 * ProductCategoryService - {@link IProductCategoryService} impl
 * @author jpk
 */
public class ProductCategoryService extends NamedEntityService<ProductCategory> implements IProductCategoryService {

    /**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
    @Inject
    public ProductCategoryService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
        super(dao, entityAssembler, vfactory);
    }

    @Override
    public Class<ProductCategory> getEntityClass() {
        return ProductCategory.class;
    }
}
