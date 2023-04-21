package edu.java.homework.hw09.exercises.salads.components;

import edu.java.homework.hw09.exercises.salads.products.Vegetable;

public class VegetableComponent {

    private Vegetable vegetable = null;

    private double weight = 0.0d;

    private static final String PRODUCT_ARG_EXCEPTION = "Vegetable object is null-pointed.";

    private static final String WEIGHT_ARG_EXCEPTION = "The weight of the product could NOT be negative number.";

    public VegetableComponent(Vegetable vegetable) {
        this(vegetable, 0);
    }

    public VegetableComponent(Vegetable vegetable, double weight) {
        if (vegetable == null) {
            throw new IllegalArgumentException(PRODUCT_ARG_EXCEPTION);
        }
        if (weight < 0) {
            throw new IllegalArgumentException(WEIGHT_ARG_EXCEPTION);
        }
        this.vegetable = vegetable;
        this.weight = weight;
    }

    public Vegetable getVegetable() {
        return vegetable;
    }

    public void setVegetable(Vegetable vegetable) {
        if (vegetable == null) {
            throw new IllegalArgumentException(PRODUCT_ARG_EXCEPTION);
        }
        this.vegetable = vegetable;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException(WEIGHT_ARG_EXCEPTION);
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        return getVegetable().toString() + ", price: " + getVegetable().getPrice() + ", weight: " + getWeight();
    }
}
