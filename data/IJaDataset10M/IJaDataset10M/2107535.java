package Views;

import UCM.UCDisplayFlights;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Defines the view display class
 * @author Jason Sorbello
 */
public class ViewDisplayFlights extends JPanel {

    private JButton displayFlightsButton = new JButton("DisplayFlights");

    private final UCDisplayFlights UCDisplayFlights = new UCDisplayFlights(this);

    private MainUI view;

    /**
     * Construction the initualises the view
     * @param view
     */
    public ViewDisplayFlights(MainUI view) {
        this.view = view;
        this.setLayout(new FlowLayout());
        this.add(displayFlightsButton);
        displayFlightsButton.addActionListener(new AdapterUCCToAL(UCDisplayFlights));
        this.setVisible(true);
    }

    /**
     * sets the displays flights panel visible
     * @param flightsInfoPanel
     */
    public void setDisplayFlightsVisible(JPanel flightsInfoPanel) {
        view.removeCenterPanel();
        view.setFlightsPanel(flightsInfoPanel);
        view.add(view.getFlightsPanel(), BorderLayout.CENTER);
        view.pack();
    }
}
