package client.tabs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import client.update.Updateable;

public class KonsoleJPanel extends JPanel implements Updateable {

    GridBagConstraints c = new GridBagConstraints();

    protected JPanel nestedframe;

    protected Updateable updateable;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public KonsoleJPanel(JPanel jpanel, Konsole konsole) {
        JPanel Matrix = new JPanel();
        JLayeredPane layered = new JLayeredPane();
        nestedframe = jpanel;
        setLayout(new GridBagLayout());
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0;
        c.weightx = 0;
        c.insets = new Insets(10, 10, 10, 10);
        add(jpanel, c);
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        konsole.setLocation(0, 0);
        konsole.setSize(konsole.getSize());
        add(konsole, c);
        add(layered);
        if (nestedframe instanceof Updateable) {
            updateable = (Updateable) nestedframe;
        }
    }

    public void update(int beamernumber, String name) {
        if (updateable != null) {
            updateable.update(beamernumber, name);
        }
    }
}
