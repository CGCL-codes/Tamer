package view;

import genetic.GeneticNodeObj;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;

/**
 * @author   Fabrizio Pastore [ fabrizio.pastore@gmail.com ]
 */
public class GeneticNodeGroupPane extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4461671805780136603L;

    private PopInfoTab tabParent = null;

    public GeneticNodeGroupPane(PopInfoTab tab) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.tabParent = tab;
        JPanel title = new JPanel(new GridLayout(1, 2));
        JLabel nomeL = new JLabel("Nome");
        title.add(nomeL);
        JLabel arietaL = new JLabel("Arieta");
        title.add(arietaL);
        this.add(title);
    }

    public void add(GeneticNodeObj obj) {
        GeneticNodePane gp = new GeneticNodePane(this.tabParent, obj);
        this.add(gp);
    }
}
