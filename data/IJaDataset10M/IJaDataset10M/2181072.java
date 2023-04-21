package com.presentation;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import com.dao.ServiceProvider;
import com.model.Flight;
import com.model.FlightLeg;
import com.model.Passenger;
import java.awt.Font;

public class FlightReservedFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButtonHome = null;

    private JLabel jLabel = null;

    private JTextArea jTextFieldFlightDetails = null;

    private JTextArea jTextFieldPassengerDetails = null;

    private JLabel jLabel1 = null;

    private ServiceProvider dao;

    private Passenger passenger;

    /**
	 * This is the default constructor
	 * @param dao 
	 */
    public FlightReservedFrame(ServiceProvider dao, String flightNumber, Passenger passenger, int reservationNumber) {
        super();
        this.dao = dao;
        this.passenger = passenger;
        initialize();
        jLabel1.setText("IMPORTANT: your reservation number is " + reservationNumber);
        jTextFieldPassengerDetails.setText("Passenger info\n\n\tName: " + passenger.getName() + "\n\tPersonal Number: " + passenger.getPersonalInfo() + "\n\tEmail: " + passenger.getEmail());
        String output = "Your flight " + flightNumber + " consists of the following flight legs:";
        Flight flight = dao.getFlight(flightNumber);
        int num = 1;
        for (FlightLeg flightLeg : flight.getFlightLegs()) {
            output += num + ": " + flightLeg.getDepAirport().getCity() + " -> " + flightLeg.getArrAirport().getCity() + "\n";
            num++;
        }
        if (num == 1) {
            output = "Could not find any suitible FLIGHT LEGS!";
        }
        jTextFieldFlightDetails.setText(output);
    }

    /**
	 * This method initializes this
	 *
	 * @return void
	 */
    private void initialize() {
        this.setSize(646, 507);
        this.setTitle("Flight Reserved");
        this.setContentPane(getJContentPane());
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent e) {
                System.out.println("windowOpened()");
            }
        });
        this.setTitle("Flight Reserved");
    }

    /**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(29, 49, 501, 27));
            jLabel1.setFont(new Font("Verdana", Font.BOLD, 14));
            jLabel1.setText("IMPORTANT: your reservation number is RESNUM");
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(30, 16, 484, 26));
            jLabel.setText("You have successfully reserved a ticket on Chalmers Airline");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonHome(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(getJTextFieldFlightDetails(), null);
            jContentPane.add(getJTextFieldPassengerDetails(), null);
            jContentPane.add(jLabel1, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jButtonHome
	 *
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonHome() {
        if (jButtonHome == null) {
            jButtonHome = new JButton();
            jButtonHome.setBounds(new Rectangle(428, 338, 101, 33));
            jButtonHome.setText("Home");
            jButtonHome.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                    SearchFrame.getInstance().setVisible(true);
                }
            });
        }
        return jButtonHome;
    }

    private JTextArea getJTextFieldFlightDetails() {
        if (jTextFieldFlightDetails == null) {
            jTextFieldFlightDetails = new JTextArea();
            jTextFieldFlightDetails.setBounds(new Rectangle(31, 175, 499, 157));
            jTextFieldFlightDetails.setBackground(new Color(181, 211, 249));
            jTextFieldFlightDetails.setEditable(false);
            jTextFieldFlightDetails.setText("passenger details here");
        }
        return jTextFieldFlightDetails;
    }

    /**
	 * This method initializes jTextFieldPassengerDetails
	 *
	 * @return javax.swing.JTextField
	 */
    private JTextArea getJTextFieldPassengerDetails() {
        if (jTextFieldPassengerDetails == null) {
            jTextFieldPassengerDetails = new JTextArea();
            jTextFieldPassengerDetails.setBounds(new Rectangle(32, 78, 496, 93));
            jTextFieldPassengerDetails.setBackground(new Color(181, 211, 249));
            jTextFieldPassengerDetails.setEditable(false);
            jTextFieldPassengerDetails.setText("passenger details here");
        }
        return jTextFieldPassengerDetails;
    }
}
