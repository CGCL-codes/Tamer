package net.sf.brightside.overlord.domain;

public interface Product extends DomainObject {

    long getId();

    String getName();

    String getPrice();

    String getCode();

    String getDescription();

    String getQuantity();

    void setName(String name);

    void setPrice(String price);

    void setCode(String code);

    void setDescription(String description);

    void setQuantity(String quantity);
}
