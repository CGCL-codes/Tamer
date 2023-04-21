package mpower_hibernate;

/**
 * 
 *
 * @hibernate.class
 *     table="INGREDIENT"
 *
 */
public class Ingredient {

    private String Name;

    /**
  *   @hibernate.property
  */
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    private String Quantity;

    /**
  *   @hibernate.property
  */
    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    private String Indicator;

    /**
  *   @hibernate.property
  */
    public String getIndicator() {
        return Indicator;
    }

    public void setIndicator(String Indicator) {
        this.Indicator = Indicator;
    }

    private String Description;

    /**
  *   @hibernate.property length="2000"
  */
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    private long id;

    /**
  *   @hibernate.id
  *     generator-class="sequence"
  */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
