package org.xmdl.taslak.webapp.action;

import java.util.Collection;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.dao.DataIntegrityViolationException;
import org.xmdl.ida.lib.web.action.BaseAction;
import org.xmdl.taslak.model.Product;
import org.xmdl.taslak.model.search.ProductSearch;
import org.xmdl.taslak.service.ProductManager;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
public class ProductSupplierAction extends BaseAction implements Preparable {

    private ProductManager productManager;

    private Collection<Product> products;

    private Product product;

    private Long id;

    private Long idToCopy;

    private ProductSearch productSearch = new ProductSearch();

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            if (id != null) {
                product = productManager.get((long) id);
            } else {
                product = new Product();
            }
        }
    }

    @SkipValidation
    public String list() {
        if (log.isDebugEnabled()) log.debug("list() <-");
        products = productManager.search(productSearch);
        if (log.isDebugEnabled()) log.debug("listing items:" + products.size());
        if (log.isDebugEnabled()) log.debug("list() ->");
        return SUCCESS;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @SkipValidation
    public String delete() {
        if (log.isDebugEnabled()) log.debug("delete() <-");
        productManager.remove(product.getId());
        saveMessage(getText("product.deleted"));
        if (log.isDebugEnabled()) log.debug("deleting product: " + product);
        if (log.isDebugEnabled()) log.debug("delete() ->");
        return SUCCESS;
    }

    public String copy() {
        if (log.isDebugEnabled()) log.debug("copy() <-");
        if (idToCopy != null) {
            product = productManager.get(idToCopy);
        }
        if (log.isDebugEnabled()) log.debug("copying product: " + product);
        if (log.isDebugEnabled()) log.debug("copy() ->");
        product.setId(null);
        return SUCCESS;
    }

    public String edit() {
        if (log.isDebugEnabled()) log.debug("edit() <-");
        if (id != null) {
            product = productManager.get(id);
        } else {
            product = new Product();
        }
        if (log.isDebugEnabled()) log.debug("editing product: " + product);
        if (log.isDebugEnabled()) log.debug("edit() ->");
        return SUCCESS;
    }

    @SkipValidation
    public String deleteMass() throws Exception {
        if (log.isDebugEnabled()) log.debug("deleteMass() <-");
        boolean cannotDeleted = false;
        boolean anyDeleted = false;
        if (getDeleteId() != null) {
            for (String idStr : getDeleteId()) {
                try {
                    productManager.remove(new Long(idStr));
                    anyDeleted = true;
                    if (log.isDebugEnabled()) log.debug("deleting product with id: " + idStr);
                } catch (DataIntegrityViolationException e) {
                    e.printStackTrace();
                    cannotDeleted = true;
                    if (log.isDebugEnabled()) log.debug("could not delete product with id: " + idStr);
                }
            }
        }
        if (cannotDeleted) saveMessage(getText("Product.cannotBeDeleted"));
        if (anyDeleted) saveMessage(getText("Product.deleted"));
        products = productManager.search(productSearch);
        if (log.isDebugEnabled()) log.debug("deleteMass() ->");
        return SUCCESS;
    }

    public String save() throws Exception {
        if (log.isDebugEnabled()) log.debug("save() <-");
        if (cancel != null) {
            if (log.isDebugEnabled()) log.debug("save() ->");
            return "cancel";
        }
        if (delete != null) {
            if (log.isDebugEnabled()) log.debug("save() ->");
            return delete();
        }
        boolean isNew = (product.getId() == null);
        productManager.save(product);
        String key = (isNew) ? "product.added" : "product.updated";
        saveMessage(getText(key));
        String logMessage = (isNew) ? "adding product: " + product : "updating product: " + product;
        if (log.isDebugEnabled()) log.debug(logMessage);
        if (log.isDebugEnabled()) log.debug("save() ->");
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }

    public ProductSearch getProductSearch() {
        return productSearch;
    }

    public void setProductSearch(ProductSearch productSearch) {
        this.productSearch = productSearch;
    }

    public Long getIdToCopy() {
        return idToCopy;
    }

    public void setIdToCopy(Long idToCopy) {
        this.idToCopy = idToCopy;
    }

    public ProductManager getProductManager() {
        return productManager;
    }
}
