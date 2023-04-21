package gameditor.ui.tripleaGUI;

import java.awt.*;
import javax.swing.*;

public class ProductionPanel extends JPanel {

    /**
	   Default constructor.
	   Perpares the panel's GUI elements and layout manager.
	*/
    public ProductionPanel() {
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        add(new JLabel("Production Panel"), BorderLayout.CENTER);
    }
}
