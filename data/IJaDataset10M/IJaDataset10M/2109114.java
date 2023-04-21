package aml.ramava.data.entities;

public class InvitationDeliveryType {

    private int id;

    private String name;

    private String description;

    public InvitationDeliveryType() {
        this(null, null);
    }

    public InvitationDeliveryType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public InvitationDeliveryType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof InvitationDeliveryType) return ((InvitationDeliveryType) o).getId() == this.id;
        return false;
    }
}
