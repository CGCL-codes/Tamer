package nl.burgerweeshuis.util;

import nl.burgerweeshuis.dao.AbstractDAO;
import nl.burgerweeshuis.model.ActivityCategory;
import nl.burgerweeshuis.model.Address;
import nl.burgerweeshuis.model.Role;
import nl.burgerweeshuis.model.User;
import nl.burgerweeshuis.model.Venue;
import org.hibernate.Session;

/**
 * Provides default database content.
 */
public final class DefaultDatabaseContent extends AbstractDAO {

    /**
	 * Construct.
	 * 
	 * @param hibernateSession
	 *            hibernate sessie
	 */
    public DefaultDatabaseContent(Session hibernateSession) {
        super(hibernateSession);
    }

    /**
	 * Fills database with default filling.
	 */
    public void fill() {
        addVenuesAndUsers();
    }

    private void addVenuesAndUsers() {
        Address address;
        Venue burgerweeshuis = new Venue();
        burgerweeshuis.setName("Het Burgerweeshuis");
        address = new Address();
        address.setAddress("Bagijnenstraat 9");
        address.setPc("7411 PT");
        address.setCity("Deventer");
        address.setTelephone("+31(0)570619198");
        address.setFax("+31(0)570641013");
        address.setWebmasterEmail("webchef@burgerweeshuis.nl");
        burgerweeshuis.setAddress(address);
        create(address);
        create(burgerweeshuis);
        addCategories(burgerweeshuis);
        Venue hedon = new Venue();
        hedon.setName("Hedon");
        address = new Address();
        address.setAddress("Burgemeester Drijbersingel 7");
        address.setPc("8021 DA");
        address.setCity("Zwolle");
        address.setTelephone("+31 (0)38 452 72 29");
        address.setFax("+31 (0)38 452 60 67");
        address.setWebmasterEmail("info@hedon-zwolle.nl");
        hedon.setAddress(address);
        create(address);
        create(hedon);
        addCategories(hedon);
        Venue atak = new Venue();
        atak.setName("Atak");
        address = new Address();
        address.setAddress("Noorderhagen 12");
        address.setPc("7511 EL");
        address.setCity("Enschede");
        address.setTelephone("+31 (0)53 4 322 388");
        address.setFax("+31 (0)53 4 321 142");
        address.setWebmasterEmail("atak@atak.nl");
        atak.setAddress(address);
        create(address);
        create(atak);
        addCategories(atak);
        Venue metropool = new Venue();
        metropool.setName("Metropool");
        address = new Address();
        address.setAddress("Wemenstraat 38");
        address.setPc("7551 EZ");
        address.setCity("Hengelo");
        address.setTelephone("+31 (0)74 24 38 000");
        address.setFax("+31 (0)74 29 13 808");
        address.setWebmasterEmail("ronald.punt@mac.com");
        metropool.setAddress(address);
        create(address);
        create(metropool);
        addCategories(metropool);
        Role siteAdminRole = new Role("siteAdmin", "Site Onderhoud", "gebruikers met deze rol zijn gemachtigd om front-end content te onderhouden");
        create(siteAdminRole);
        Role activityAdminRole = new Role("activityAdmin", "Agenda Onderhoud", "gebruikers met deze rol mogen de agenda onderhouden.");
        create(activityAdminRole);
        Role listAdminRole = new Role("listAdmin", "Lijst Onderhoud", "gebruikers met deze rol mogen de email- en reserveringslijsten onderhouden.");
        create(listAdminRole);
        User testUser = new User();
        testUser.setFirstName("Fritz");
        testUser.setLastName("Fritzl");
        testUser.setUserName("test");
        testUser.setPassword("test");
        testUser.setRootUser(Boolean.TRUE);
        testUser.addRole(burgerweeshuis, siteAdminRole);
        testUser.addRole(burgerweeshuis, activityAdminRole);
        testUser.addRole(burgerweeshuis, listAdminRole);
        create(testUser);
    }

    private void addCategories(Venue venue) {
        ActivityCategory cat;
        cat = new ActivityCategory();
        cat.setName("pop/rock");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("metal/gothic");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("dance (hedendaags)");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("disco (retro/ funk ed)");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("roots (blues, wereldmuziek, folk)");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("punk/ hardcore");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("reggae (ook ska)");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("hiphop/ r&b");
        cat.setVenue(venue);
        create(cat);
        cat = new ActivityCategory();
        cat.setName("overig (stand ups/ singer songwriters etc)");
        cat.setVenue(venue);
        create(cat);
    }
}
