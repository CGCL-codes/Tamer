package petcatalog.ajax;

public class Composer {

    private String id;

    private String firstName;

    private String lastName;

    private String category;

    public Composer(String id, String firstName, String lastName, String category) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCategory() {
        return category;
    }
}
