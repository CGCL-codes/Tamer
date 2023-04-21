package cwi;

/**
 * A product is a combination of manufacturer and make.
 * @author Mathis Dirksen-Thedens <zephyrsoft@users.sourceforge.net>
 * License GPL 2
 * @version $Id: Product.java,v 1.13 2007/07/12 12:30:11 dirkhillbrecht Exp $
 */
public class Product implements Comparable {

    private String name;

    private Type type;

    public Product(Type type, String name) {
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int compareTo(Object other) {
        if (other instanceof Product) {
            return name.compareTo(((Product) other).getName());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
