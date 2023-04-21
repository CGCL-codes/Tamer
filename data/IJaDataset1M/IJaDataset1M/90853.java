package com.sush.webstore.store.domain.beans;

import java.util.HashSet;
import java.util.Set;
import com.sush.webstore.store.domain.IImages;
import com.sush.webstore.store.domain.IProduct;

public class Product implements IProduct {

    private long id;

    private String name;

    private String description;

    private Double cost;

    private Set<Item> items = new HashSet<Item>();

    private Images images = new Images();

    private Long units;

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *            the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the id
	 */
    public long getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    private void setId(long id) {
        this.id = id;
    }

    /**
	 * @return the items
	 */
    public Set<Item> getItems() {
        return items;
    }

    /**
	 * @param items
	 *            the items to set
	 */
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    /**
	 * @return the images
	 */
    public IImages getIImages() {
        return images;
    }

    /**
	 * @param images
	 *            the images to set
	 */
    public void setImages(Images images) {
        this.images = images;
    }

    /**
	 * @return the images
	 */
    public Images getImages() {
        return images;
    }

    /**
	 * @return the cost
	 */
    public Double getCost() {
        return cost;
    }

    /**
	 * @param cost
	 *            the cost to set
	 */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
	 * @return the units
	 */
    public Long getUnits() {
        return units;
    }

    /**
	 * @param units
	 *            the units to set
	 */
    public void setUnits(Long units) {
        this.units = units;
    }
}
