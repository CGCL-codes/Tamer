package mapEditor;

import mapEditor.utils.MapLocation;
import javax.swing.*;

/**
* @author  �ystein Myhre
*/
public class StatusLine extends JPanel {

    static final long serialVersionUID = 0;

    private LoweredLabel description;

    private LoweredLabel scale;

    private LoweredLabel mode;

    private LoweredLabel locationX, locationY;

    private LoweredLabel positionX, positionY;

    public StatusLine() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(description = new LoweredLabel());
        add(locationX = new LoweredLabel());
        add(locationY = new LoweredLabel());
        add(positionX = new LoweredLabel());
        add(positionY = new LoweredLabel());
        add(scale = new LoweredLabel());
        add(mode = new LoweredLabel());
        setDescription("");
        setScale("1:500");
        setMode("");
    }

    public void setDescription(String text) {
        description.setText(" " + text + ' ');
    }

    public void setMode(String text) {
        mode.setText(" Mouse: " + text + ' ');
    }

    public void setScale(String text) {
        scale.setText(" Scale: " + text + ' ');
    }

    public void setMousePosition(MapLocation location) {
        positionX.setText(" X: " + ((location == null) ? "unknown" : location.getX()) + ' ');
        positionY.setText(" Y: " + ((location == null) ? "unknown" : location.getY()) + ' ');
        locationX.setText("UTM32 X: " + ((location == null) ? "unknown" : location.getEast()) + " E ");
        locationY.setText("UTM32 Y: " + ((location == null) ? "unknown" : location.getNorth()) + " N ");
    }
}

class LoweredLabel extends JLabel {

    static final long serialVersionUID = 0;

    public LoweredLabel() {
        setBorder(BorderFactory.createLoweredBevelBorder());
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}
