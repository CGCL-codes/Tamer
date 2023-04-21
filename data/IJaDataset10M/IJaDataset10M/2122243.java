package com.hibernate.dao.product;

import java.util.List;
import java.util.Map;
import com.hibernate.pojo.Product;
import com.struts.form.ProductForm;

public interface ProductDao {

    public Map findProducts(int page, int productleibieID);

    public Long saveProduct(Product product);

    public List findProduct(String queryString);

    public boolean updateProductAudit(ProductForm productform);

    public boolean deleteProduct(Long id);

    public List updateProductView(Long id);

    public List updateProduct(ProductForm productform, String newfilename);
}
