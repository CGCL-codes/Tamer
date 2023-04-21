package com.mirovicjovan.carmachine.model;

import java.util.List;

public interface Model {

    public Manufacturer getManufacturer();

    public void setManufacturer(Manufacturer manufacturer);

    public Category getCategory();

    public void setCategory(Category category);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public List<Car> getCars();

    public void setCars(List<Car> cars);
}
