package com.coderdream.chapter04.factorymethod.framework;

public abstract class Factory {

    public final Product create(String owner) {
        Product product = this.createProduct(owner);
        this.registerProduct(product);
        return product;
    }

    protected abstract Product createProduct(String owner);

    protected abstract void registerProduct(Product product);
}
