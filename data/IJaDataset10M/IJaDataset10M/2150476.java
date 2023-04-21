package net.sf.tacos.seam.booking.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

@Name("HotelService")
@Scope(ScopeType.SESSION)
public class HotelService {

    @In
    private Session bookingDatabase;

    @Out
    private User user;

    @SuppressWarnings("unchecked")
    public List<Hotel> findHotels(String input) {
        input = '%' + input.toLowerCase().replace('*', '%') + '%';
        Query query = bookingDatabase.createQuery("select h from Hotel h where lower(h.name) like :input or lower(h.city) like :input or lower(h.zip) like :input or lower(h.address) like :input");
        query.setString("input", input);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public Hotel findHotelById(Long id) {
        return (Hotel) bookingDatabase.createCriteria(Hotel.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public User findUserByUsername(String username) {
        return (User) bookingDatabase.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Booking> findBookingsByUserId(Long id) {
        return bookingDatabase.createCriteria(Booking.class).createCriteria("user").add(Restrictions.eq("id", id)).list();
    }

    public void save(Object obj) {
        bookingDatabase.save(obj);
    }

    @SuppressWarnings("unchecked")
    public void createDemoData() {
        List<Hotel> list = bookingDatabase.createCriteria(Hotel.class).list();
        if (!list.isEmpty()) return;
        Hotel[] hotels = new Hotel[] { new Hotel("Marriott Courtyard", "Tower Place, Buckhead", "Atlanta", "GA", "30305", "USA", new BigDecimal(120)), new Hotel("Doubletree", "Tower Place, Buckhead", "Atlanta", "GA", "30305", "USA", new BigDecimal(180)), new Hotel("W Hotel", "Union Square, Manhattan", "NY", "NY", "10011", "USA", new BigDecimal(450)), new Hotel("W Hotel", "Lexington Ave, Manhattan", "NY", "NY", "10011", "USA", new BigDecimal(450)), new Hotel("Hotel Rouge", "1315 16th Street NW", "Washington", "DC", "20036", "USA", new BigDecimal(250)), new Hotel("70 Park Avenue Hotel", "70 Park Avenue", "NY", "NY", "10011", "USA", new BigDecimal(300)), new Hotel("Conrad Miami", "1395 Brickell Ave", "Miami", "FL", "33131", "USA", new BigDecimal(300)), new Hotel("Sea Horse Inn", "2106 N Clairemont Ave", "Eau Claire", "WI", "54703", "USA", new BigDecimal(80)), new Hotel("Super 8 Eau Claire Campus Area", "1151 W Macarthur Ave", "Eau Claire", "WI", "54701", "USA", new BigDecimal(90)), new Hotel("Marriot Downtown", "55 Fourth Street", "San Francisco", "CA", "94103", "USA", new BigDecimal(160)) };
        for (Hotel h : hotels) {
            Long id = (Long) bookingDatabase.save(h);
            System.out.println("saved: " + id);
        }
        Query query = bookingDatabase.createQuery("from Hotel");
        System.out.println("found: " + query.list().size());
        user = new User("Tapestry", "Secret", "TestUser");
        Serializable s = bookingDatabase.save(user);
        System.out.println("user: " + s);
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar tomorrow = new GregorianCalendar();
        tomorrow.setTimeInMillis(today.getTimeInMillis() + 3 * 1000 * 60 * 60 * 24);
        Booking booking = new Booking(hotels[0], user);
        booking.setCheckinDate(today.getTime());
        booking.setCheckoutDate(tomorrow.getTime());
        booking.setCreditCard("1234567891234567");
        booking.setCreditCardName("Visa");
        booking.setCreditCardExpiryMonth(6);
        booking.setCreditCardExpiryYear(2020);
        bookingDatabase.save(booking);
    }
}
