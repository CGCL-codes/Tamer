package first;

@SuppressWarnings("serial")
public class Product extends Entity<Product> {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void output() {
        System.out.println("--- Product ---");
        super.output();
        System.out.println("name: " + getName());
        System.out.println("---------------");
    }
}
