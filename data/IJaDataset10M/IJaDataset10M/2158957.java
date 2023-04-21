package at.vartmp.jschnellen.gui.parts.centerPanel;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import at.vartmp.jschnellen.core.util.logger.SchnellnLogFactory;
import at.vartmp.jschnellen.core.util.logger.SchnellnLogger;
import at.vartmp.jschnellen.gui.helper.GuiBeanManager;
import pagelayout.Column;
import pagelayout.Row;

/**
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 * 
 */
public class QuickStartPanel extends JPanel {

    private static final long serialVersionUID = 5883307082579820770L;

    private static SchnellnLogger logger = SchnellnLogFactory.getLog(BlindPanel.class);

    private GuiBeanManager beanManager;

    /**
	 * Instantiates a Panel for announcing blind
	 */
    public QuickStartPanel() {
        super();
        this.beanManager = GuiBeanManager.getInstance();
        this.init();
        this.validate();
        this.repaint();
    }

    private void init() {
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.darkGray));
        Column topLevel = new Column(Column.CENTER, Column.CENTER);
        logger.debug("Initializing QuickStartPanel...");
        AbstractButton[] buttons = new AbstractButton[3];
        ActionListener listener = beanManager.getActionListener("quickStartListener");
        buttons[0] = beanManager.getButton("quickStart.newGame.4");
        buttons[1] = beanManager.getButton("quickStart.newGame.3");
        buttons[2] = beanManager.getButton("quickStart.newGame.2");
        Column top = new Column(Column.CENTER, Column.CENTER);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(listener);
            top.newRow(Row.CENTER, Row.CENTER).add(buttons[i]);
        }
        JPanel myJPanel = new JPanel();
        Dimension size2 = new Dimension(40, 40);
        myJPanel.setPreferredSize(size2);
        topLevel.add(myJPanel);
        top.createLayout(myJPanel);
        topLevel.createLayout(this);
    }
}
