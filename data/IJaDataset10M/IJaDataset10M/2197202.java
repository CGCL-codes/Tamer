package bom;

import org.nakedobjects.application.BusinessObjectContainer;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.control.ActionAbout;
import org.nakedobjects.application.valueholder.Option;
import org.nakedobjects.application.valueholder.TextString;
import java.util.Vector;

public class CopyOfLocation implements Common {

    private final TextString streetAddress;

    private final TextString knownAs;

    private City city;

    private Customer customer;

    private boolean isDirty;

    private transient BusinessObjectContainer container;

    private Option type = new Option(new String[] { "One", "Two", "Threee" });

    public Option getType() {
        return type;
    }

    public CopyOfLocation() {
        streetAddress = new TextString();
        knownAs = new TextString();
    }

    public void aboutActionNewBooking(ActionAbout about, CopyOfLocation location) {
        about.setDescription("Giving one location to another location creates a new booking going from the given location to the recieving location.");
        about.unusableOnCondition(equals(location), "Two different locations are required");
        boolean sameCity = getCity() != null && location != null && getCity().equals(location.getCity());
        about.unusableOnCondition(!sameCity, "Locations must be in the same city");
    }

    public void explorationActionExplorationMethod() {
        Vector instances = container.allInstances(City.class);
        if (instances.size() > 0) {
            city = (City) instances.elementAt(0);
        }
        instances = container.allInstances(Customer.class);
        if (instances.size() > 0) {
            customer = (Customer) instances.elementAt(0);
        }
    }

    public void debugActionDebugMethod() {
        System.out.println(this);
        System.out.println("  " + knownAs.titleString());
        System.out.println("  " + streetAddress.titleString());
        System.out.println("  " + city.title());
        System.out.println("  " + customer.title());
    }

    public void setContainer(BusinessObjectContainer container) {
        this.container = container;
    }

    public City getCity() {
        container.resolve(this, city);
        return city;
    }

    public Customer getCustomer() {
        container.resolve(this, customer);
        return customer;
    }

    public final TextString getStreetAddress() {
        return streetAddress;
    }

    public final TextString getKnownAs() {
        return knownAs;
    }

    public void setCity(City newCity) {
        city = newCity;
        isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void clearDirty() {
        isDirty = false;
    }

    public void markDirty() {
        isDirty = true;
    }

    public void associateCustomer(Customer newCustomer) {
        newCustomer.addToLocations(null);
    }

    public void dissociateCustomer(Customer newCustomer) {
        newCustomer.removeFromLocations(null);
    }

    public void setCustomer(Customer newCustomer) {
        customer = newCustomer;
        isDirty = true;
    }

    public Title title() {
        if (knownAs.isEmpty()) {
            return streetAddress.title().append(",", getCity());
        } else {
            return knownAs.title().append(",", getCity());
        }
    }
}
