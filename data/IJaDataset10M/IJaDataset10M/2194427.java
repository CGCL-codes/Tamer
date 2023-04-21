package fhj.itm05.seminarswe.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import fhj.itm05.seminarswe.domain.Booking;

public class BookingDAOHsqldb implements BookingDAO {

    private static BookingDAOHsqldb instance;

    public static final String QUERIES_BUNDLE_PATH = "fhj.itm05.seminarswe.database.queries";

    /**
	 * Returns an instance of singleton DatabaseLayerImpl
	 * @return instance of DatabseLayerImpl
	 */
    public static BookingDAOHsqldb getInstance() {
        if (instance == null) {
            instance = new BookingDAOHsqldb();
        }
        return instance;
    }

    private BookingDAOHsqldb() {
    }

    /**
	 * returns a list of bookings sorted by a certain column
	 * @param user name from current session
	 * @param column for sorting
	 * @return list of bookings or empty list
	 */
    @Override
    public List<Booking> listBookingBy(String user, String column, String start, String end) {
        final List<Booking> bookingList = new ArrayList<Booking>();
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("list.bookingby") + " " + column);
            query.setString(1, user);
            query.setString(2, start);
            query.setString(3, end);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                final Booking booking = new Booking(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getFloat(5));
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    /**
	 * returns a list of bookings sorted by a certain column and filtered by a certain category
	 * @param user name from current session
	 * @param category for filtering
	 * @param column for sorting
	 * @return list of bookings or empty list
	 */
    @Override
    public List<Booking> listBookingByFilter(String user, String filter, String column) {
        final List<Booking> bookingList = new ArrayList<Booking>();
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("list.bookingbyfilter") + " " + column);
            query.setString(1, filter);
            query.setString(2, user);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                final Booking booking = new Booking(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getFloat(5));
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    /**
	 * returns a single booking defined by id
	 * @param booking id
	 * @return single booking or null
	 */
    @Override
    public Booking getBooking(int id) {
        Booking booking = null;
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("get.booking"));
            query.setInt(1, id);
            final ResultSet rs = query.executeQuery();
            if (rs.next()) {
                booking = new Booking(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getFloat(5));
                booking.setCategoryId(rs.getInt(6));
            } else {
                System.out.println("ERROR: booking not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    /**
	 * inserts a new single booking into the database
	 * @param booking
	 * @return state
	 */
    @Override
    public boolean insertBooking(Booking booking) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("insert.booking"));
            if (booking.getId() == -1) query.setString(1, null); else {
                query.setInt(1, booking.getId());
            }
            query.setDate(2, new Date(booking.getDate().getTime()));
            query.setInt(3, booking.getCategoryId());
            query.setString(4, booking.getBookingDescription());
            query.setFloat(5, booking.getAmount());
            query.setString(6, booking.getUser());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * updates an existing booking
	 * @param updated booking
	 * @return state
	 */
    @Override
    public boolean editBooking(Booking booking) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("edit.booking"));
            query.setDate(1, new Date(booking.getDate().getTime()));
            query.setInt(2, booking.getCategoryId());
            query.setString(3, booking.getBookingDescription());
            query.setFloat(4, booking.getAmount());
            query.setString(5, booking.getUser());
            query.setInt(6, booking.getId());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * deletes an existing booking
	 * @param booking id
	 * @return state
	 */
    @Override
    public boolean deleteBooking(int id) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("delete.booking"));
            query.setInt(1, id);
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int deleteUserBookings(String username) {
        try {
            PreparedStatement deleteStatement = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("deleteUserBooking"));
            deleteStatement.setString(1, username);
            return deleteStatement.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println("Failed to delete the bookings from " + username);
            System.out.println(sqle.getErrorCode() + " : " + sqle.getMessage());
            return -1;
        }
    }

    @Override
    public List<Booking> listBookingByDate(String user, String category, String column, String start, String end) {
        final List<Booking> bookingList = new ArrayList<Booking>();
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(BookingDAOHsqldb.QUERIES_BUNDLE_PATH).getString("list.bookingbydate") + " " + column);
            query.setString(1, category);
            query.setString(2, user);
            query.setString(3, start);
            query.setString(4, end);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                final Booking booking = new Booking(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getFloat(5));
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    @Override
    public boolean checkBookingId(Integer id) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("checkBookingId"));
            query.setInt(1, id);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean checkBookingData(Booking booking) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(booking.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String stringdate = sdf.format(cal.getTime());
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("checkBookingData"));
            query.setString(1, stringdate);
            query.setString(2, booking.getBookingDescription());
            query.setFloat(3, booking.getAmount());
            final ResultSet rs = query.executeQuery();
            while (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
