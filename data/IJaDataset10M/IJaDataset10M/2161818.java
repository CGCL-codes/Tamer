package net.shopxx.service;

import java.util.List;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;

/**
 * Service接口 - 商品分类
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX9A058EDC3D9C0E5B105452AB83A4DF2C
 * ============================================================================
 */
public interface ProductCategoryService extends BaseService<ProductCategory, String> {

    /**
	 * 获取所有顶级商品分类集合;
	 * 
	 * @return 所有顶级商品分类集合
	 * 
	 */
    public List<ProductCategory> getRootProductCategoryList();

    /**
	 * 根据ProductCategory对象获取所有父类集合，若无父类则返回null;
	 * 
	 * @return 父类集合
	 * 
	 */
    public List<ProductCategory> getParentProductCategoryList(ProductCategory productCategory);

    /**
	 * 根据Product对象获取所有父类集合，若无父类则返回null;
	 * 
	 * @return 父类集合
	 * 
	 */
    public List<ProductCategory> getParentProductCategoryList(Product product);

    /**
	 * 根据ProductCategory对象获取路径集合;
	 * 
	 * @return ProductCategory集合
	 * 
	 */
    public List<ProductCategory> getProductCategoryPathList(ProductCategory productCategory);

    /**
	 * 根据Product对象获取路径集合;
	 * 
	 * @return ProductCategory集合
	 * 
	 */
    public List<ProductCategory> getProductCategoryPathList(Product product);

    /**
	 * 根据ProductCategory对象获取所有子类集合，若无子类则返回null;
	 * 
	 * @return 子类集合
	 * 
	 */
    public List<ProductCategory> getChildrenProductCategoryList(ProductCategory productCategory);

    /**
	 * 根据Product对象获取所有子类集合，若无子类则返回null;
	 * 
	 * @return 子类集合
	 * 
	 */
    public List<ProductCategory> getChildrenProductCategoryList(Product product);

    /**
	 * 获取商品分类树集合;
	 * 
	 * @return 商品分类树集合
	 * 
	 */
    public List<ProductCategory> getProductCategoryTreeList();
}
