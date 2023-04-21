package sample;

import org.doframework.*;
import org.doframework.annotation.*;

@TargetReferencedClasses({ Manu.class })
@TargetClass(Prod.class)
public class ProdDeletionHelper implements ReferenceBuilder, DeletionHelper {

    public Object fetch() {
        String queryText = "select prod from Product as prod where prod.name = ?1 ";
        Object[] args = new Object[] { getPrimaryKey() };
        return fetchObject(queryText, args);
    }

    public Object getPrimaryKey() {
        return "Orange Juice";
    }

    public Object create() {
        Prod product = new Prod();
        product.setName((String) getPrimaryKey());
        product.setPrice(4);
        Manu manufacturer = (Manu) DOF.require(new ManuDeletionHelper());
        product.setManufacturerByManufacturerId(manufacturer);
        persist(product);
        return product;
    }

    public ReferenceBuilder[] getReferenceJavaDependencies() {
        return new ReferenceBuilder[] { new ManuDeletionHelper() };
    }

    public boolean okToDelete(Object object) {
        return true;
    }

    public Object extractPrimaryKey(Object object) {
        return ((Prod) object).getName();
    }

    public Object[] getReferencedObjects(Object object) {
        return new Object[] { ((Prod) object).getManufacturerByManufacturerId() };
    }

    public Class[] getReferencedClasses() {
        return new Class[] { Manu.class };
    }

    /**
     * Product in the simple example has no dependencies.
     * @return
     */
    protected boolean checkDependencies(Object objectToDelete) {
        return true;
    }

    public boolean delete(final Object objectToDelete) {
        return true;
    }

    protected void persist(final Object objectToPersist) {
    }

    protected Object fetchObject(String queryText, Object[] args) {
        return null;
    }
}
