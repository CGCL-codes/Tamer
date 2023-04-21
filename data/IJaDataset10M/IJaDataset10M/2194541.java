package wicket.in.action.common;

import java.io.Serializable;

public final class Cheese implements Serializable {

    private String description;

    private String name;

    private double price;

    public Cheese() {
    }

    public Cheese(String name, String description, double price) {
        super();
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cheese) {
            Cheese that = (Cheese) obj;
            return Objects.equal(name, that.name);
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
