package Views;

import UCM.UCDisplayEditFlight;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Blah
 * @author Joshua King
 */
public class ViewDisplayEditFlight extends JPanel {

    protected JButton editFlightButton = new JButton("Edit Flight");

    private final UCDisplayEditFlight UCDisplayEditFlight = new UCDisplayEditFlight(this);

    public ViewDisplayEditFlight() {
        setLayout(new FlowLayout());
        add(editFlightButton);
        editFlightButton.addActionListener(new AdapterUCCToAL(UCDisplayEditFlight));
    }

    public void setEditFlightVisible() {
        ViewMain.getInstance().updateEditFlight();
    }
}
