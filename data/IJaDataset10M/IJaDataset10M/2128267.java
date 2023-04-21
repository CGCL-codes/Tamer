package ch.mastermapframework.basicGUIs;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BasicGUIPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public BasicGUIPanel() {
        super();
    }

    public void constructPanel() {
        this.layoutPanel();
    }

    private void layoutPanel() {
        this.setVisible(true);
    }

    public void addBorderTitle(String IN) {
        this.setBorder(BorderFactory.createTitledBorder(IN));
    }

    public void setGridLayout(int row, int col) {
        this.setLayout(new GridLayout(row, col));
    }
}
