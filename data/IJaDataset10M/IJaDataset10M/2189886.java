package smptracer.boundary;

import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * Frame that informs the user about the clicked event flag
 * @author Marco Battarra
 *
 */
public class EventFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new EventFrame
	 * @param main main frame
	 * @param descriptions list of the descriptions of the related events
	 */
    public EventFrame(MainFrame main, ArrayList<String> descriptions) {
        super("INFORMAZIONI SUGLI EVENTI");
        setResizable(false);
        int x = main.getX() + 250;
        int y = main.getY() + 200;
        setBounds(x, y, 300, 400);
        setAlwaysOnTop(true);
        setVisible(true);
        addWindowListener(main);
        main.setEnabled(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Container c = getContentPane();
        EventPanel panel = new EventPanel(main, this, descriptions);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        c.add(scrollPane, BorderLayout.CENTER);
        pack();
    }
}
