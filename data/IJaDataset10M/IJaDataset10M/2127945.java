package se.umu.cs.pvtht10.p4.g5.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import com.google.appengine.api.datastore.Key;
import se.umu.cs.pvtht10.p4.g5.persistence.CategoryService;
import se.umu.cs.pvtht10.p4.g5.persistence.EMF;

@Entity
public class ConditionXinY extends ConditionBase {

    private int amount;

    private Key category;

    public ConditionXinY(int amount, CategoryEntity category) {
        setAmount(amount);
        setCategory(category);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setCategoryKey(Key category) {
        this.category = category;
    }

    public Key getCategoryKey() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category.getKey();
    }

    public CategoryEntity getCategory() {
        EntityManager em = EMF.get().createEntityManager();
        CategoryService cServe = new CategoryService(em);
        return cServe.find(category);
    }

    @Override
    public boolean validate(CustomerEntity customer) {
        if (!super.validate(customer)) {
            return false;
        }
        int count = 0;
        if (customer.getBuyEntities() == null) {
            return false;
        }
        for (BuyInfoEntity info : customer.getBuyEntities()) {
            ProductEntity product = info.getProductEntity();
            if (product.getCategoryEntity().equals(getCategory())) {
                count++;
            }
        }
        return (count >= getAmount());
    }
}
