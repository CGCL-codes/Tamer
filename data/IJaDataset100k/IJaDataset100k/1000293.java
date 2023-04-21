package Assignment5n6;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import Assignment5n6.gui.DinnerReservationListWindow;
import Assignment5n6.gui.PassengerBookingListWindow;
import Assignment5n6.gui.PassengerBookingWindow;
import Assignment5n6.gui.VehicleBookingListWindow;
import Assignment5n6.gui.VehicleBookingWindow;

/**
 * Main application of FerryTicketingSystem - GUI mode
 * 
 *  NGUYEN, Viet Quang - Student Number: s3045708
 */
public class FerryTicketingSystemGUI extends JFrame {

    /**
     * Auto-generated serial version UID.
     */
    private static final long serialVersionUID = 6937538226564109504L;

    public static void main(String argv[]) {
        FerryTicketingSystemGUI gui = new FerryTicketingSystemGUI();
        gui.setVisible(true);
    }

    private List<Booking> passengerBookings;

    private List<CarBooking> carBookings;

    public FerryTicketingSystemGUI() {
        super("Ferry Ticketing System");
        setSize(300, 400);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.passengerBookings = BookingUtils.readPassengerBookingList("passengers.txt");
        this.carBookings = BookingUtils.readVehicleBookingList("vehicles.txt");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
        JPanel menuPanel = new JPanel();
        menuPanel.add(createMenuItem(new JButton("A"), new JLabel("Make Passenger Booking"), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                makePassengerBooking();
            }
        }));
        menuPanel.add(createMenuItem(new JButton("B"), new JLabel("Add Vehicle Booking"), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                makeVehicleBooking();
            }
        }));
        menuPanel.add(createMenuItem(new JButton("C"), new JLabel("List all Passenger Bookings"), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayPassengerBookingList();
            }
        }));
        menuPanel.add(createMenuItem(new JButton("D"), new JLabel("List all Vehicle Bookings"), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayVehicleBookingList();
            }
        }));
        menuPanel.add(createMenuItem(new JButton("E"), new JLabel("Display Dinner Reservation List"), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayDinnerReservationList();
            }
        }));
        menuPanel.add(createMenuItem(new JButton("X"), new JLabel("Exit"), new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        }));
        getContentPane().add(menuPanel);
    }

    Component createMenuItem(JButton button, JLabel label, ActionListener actionListener) {
        button.addActionListener(actionListener);
        button.setAlignmentX(LEFT_ALIGNMENT);
        label.setPreferredSize(new Dimension(210, 15));
        label.setAlignmentX(LEFT_ALIGNMENT);
        JPanel menuItem = new JPanel();
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        menuItem.add(button);
        menuItem.add(Box.createRigidArea(new Dimension(5, 0)));
        menuItem.add(label);
        return menuItem;
    }

    void exitApplication() {
        BookingUtils.writePassengerBookingList(passengerBookings, "passengers.txt");
        BookingUtils.writeVehicleBookingList(carBookings, "vehicles.txt");
        this.dispose();
    }

    void makePassengerBooking() {
        JDialog gui = new PassengerBookingWindow(this, this.passengerBookings);
        gui.setVisible(true);
    }

    void makeVehicleBooking() {
        JDialog gui = new VehicleBookingWindow(this, this.passengerBookings, this.carBookings);
        gui.setVisible(true);
    }

    void displayPassengerBookingList() {
        JDialog gui = new PassengerBookingListWindow(this, this.passengerBookings);
        gui.setVisible(true);
    }

    void displayVehicleBookingList() {
        JDialog gui = new VehicleBookingListWindow(this, this.passengerBookings, this.carBookings);
        gui.setVisible(true);
    }

    void displayDinnerReservationList() {
        JDialog gui = new DinnerReservationListWindow(this, this.passengerBookings);
        gui.setVisible(true);
    }
}
