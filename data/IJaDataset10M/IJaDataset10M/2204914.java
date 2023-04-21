package de.andreavicentini.teatralia.pages.contact;

import org.magiclabs.htmlx.Area;
import org.magiclabs.htmlx.PropertiesTextStructure;
import org.magiclabs.htmlx.TextStructure;
import de.andreavicentini.teatralia.generate.Contact;
import de.andreavicentini.teatralia.pages.TeatraliaPage;

public class ContactPage extends TeatraliaPage {

    public enum Keys implements TextStructure.TextKey {

        title, description
    }

    public static final TextStructure TEXT = new PropertiesTextStructure(ContactPage.class, "contact");

    public ContactPage(Area area) {
        super(new Contact(), Keys.title, Keys.description, area.getLayout(), area, TEXT);
    }

    public String getMail() {
        return "mail@teatralia.de";
    }

    @Override
    public boolean hasFooter() {
        return false;
    }
}
