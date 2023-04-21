package asa;

import javax.swing.JApplet;
import javax.swing.JPanel;
import asa.view.components.UnderlineLabel;

/**
 * Just for some tests. NOT part of the applications.
 * 
 * @author Asamandra
 * 
 */
public class TestApp extends JApplet {

    private static final long serialVersionUID = 1L;

    public TestApp() {
        JPanel panel = new JPanel();
        UnderlineLabel l1 = new UnderlineLabel("test");
        panel.add(l1);
        this.add(panel);
    }

    @Override
    public void init() {
    }
}
