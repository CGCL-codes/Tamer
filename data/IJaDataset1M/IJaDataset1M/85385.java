package Views;

import UCM.UCDisplayEditManager;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Joshua King
 */
public class ViewDisplayEditManager extends JPanel {

    protected JButton editManagerButton = new JButton("Add Promotion");

    private final UCDisplayEditManager UCDisplayEditManager = new UCDisplayEditManager(this);

    public ViewDisplayEditManager() {
        setLayout(new FlowLayout());
        add(editManagerButton);
        editManagerButton.addActionListener(new AdapterUCCToAL(UCDisplayEditManager));
    }

    public void setEditManagerVisible() {
        ViewMain.getInstance().updateEditManager();
    }
}
