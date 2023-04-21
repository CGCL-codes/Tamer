package mou.gui.tradescreen;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import mou.Main;
import mou.core.civilization.Civilization;
import mou.gui.GUI;
import mou.gui.diplomacy.FremdeCivTable.FremdeCivTabelle;

/**
 * @author pb
 */
public class CivilizationsTableDialog extends JDialog {

    private FremdeCivTabelle civTable = new FremdeCivTabelle();

    /**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
    public CivilizationsTableDialog() throws HeadlessException {
        super(Main.instance().getGUI().getMainFrame(), true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(civTable, BorderLayout.CENTER);
        setAlwaysOnTop(isAlwaysOnTop());
        civTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        civTable.getActionMap().put("Escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        civTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setVisible(false);
                }
            }
        });
    }

    public Civilization showDialog() {
        pack();
        GUI.centreWindow(Main.instance().getGUI().getMainFrame(), this);
        civTable.deselectAll();
        setVisible(true);
        return civTable.getSelectedCivilization();
    }
}
