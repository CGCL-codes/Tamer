package net.sourceforge.configured.examples.ui.vo;

public class ProductVO extends BaseValueObject {

    protected CategoryVO category = new CategoryVO();

    protected String price;

    protected String name;

    public CategoryVO getCategory() {
        return category;
    }

    public void setCategory(CategoryVO category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
