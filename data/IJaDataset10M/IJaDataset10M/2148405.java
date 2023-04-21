package org.nakedobjects.examples;

import java.awt.Image;
import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.util.TitleBuffer;

/**
 * Example location class.
 */
public class Location extends AbstractDomainObject {

    public static String descriptionNewBooking(final Location location) {
        return "Giving one location to another location creates a new booking going from the given location to the receiving location.";
    }

    public static String[] namesNewBooking() {
        return new String[] { "Pick Up" };
    }

    Image img;

    public Image getImage() {
        return this.img;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public void setupTime() {
    }

    String knownAs;

    String streetAddress;

    public String toString() {
        TitleBuffer title = new TitleBuffer();
        if (TitleBuffer.isEmpty(knownAs)) {
            title.append(streetAddress);
        } else {
            title.append(knownAs);
        }
        return title.toString();
    }

    private static final Logger LOG = Logger.getLogger(Location.class);

    public void created() {
        LOG.info("lifecycle created()");
    }

    public void saving() {
        LOG.info("lifecycle persisting()");
    }

    public void saved() {
        LOG.info("lifecycle persisted()");
    }

    public String validate() {
        return knownAs == null || knownAs.equals("") ? "Can't save this until know as set. Full stop" : null;
    }
}
