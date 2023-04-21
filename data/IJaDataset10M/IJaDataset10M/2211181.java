package gmgen.gui;

import gmgen.util.LogUtilities;
import pcgen.core.SettingsHandler;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * LogWindow is the top-level frame for the logging window.
 * It contains a panel that implements all of the interface elements.
 *
 * @author Tod Milam
 */
public class LogWindow extends JFrame implements ActionListener {

    private JMenuBar menu;

    private JMenuItem clear;

    private List dbgList = new ArrayList();

    private LogPanel panel;

    /**
	 * Handles the creation of its children and initial display duties.
	 *
	 * @deprecated Unused
	 */
    public LogWindow() {
        getContentPane().setLayout(new BorderLayout());
        panel = new LogPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('E');
        menu.add(edit);
        clear = new JMenuItem("Clear", 'C');
        edit.add(clear);
        clear.addActionListener(this);
        JMenu debugLevel = new JMenu("Debug Level");
        debugLevel.setMnemonic('D');
        edit.add(debugLevel);
        int currDebugLvl = SettingsHandler.getGMGenOption("Logging.DebugLevel", 2);
        boolean isSelected;
        ButtonGroup bgroup = new ButtonGroup();
        String[] debugStrs = { "Minimum Debug Messages", "Error Messages", "Exceptions", "Plugin Communication", "Data Structures", "Standard Messages", "Major Function Entry/Exit", "All Function Entry/Exit", "Everything" };
        for (int i = 1; i < 10; i++) {
            isSelected = currDebugLvl == i;
            JRadioButtonMenuItem dbg = new JRadioButtonMenuItem(i + " " + debugStrs[i - 1], isSelected);
            bgroup.add(dbg);
            dbg.addActionListener(this);
            dbgList.add(dbg);
            debugLevel.add(dbg);
        }
        int iWinX = SettingsHandler.getGMGenOption("Logging.WindowX", 0);
        int iWinY = SettingsHandler.getGMGenOption("Logging.WindowY", 0);
        int iWinWidth = SettingsHandler.getGMGenOption("Logging.WindowWidth", 440);
        int iWinHeight = SettingsHandler.getGMGenOption("Logging.WindowHeight", 230);
        panel.setPreferredSize(new Dimension(iWinWidth - 5, iWinHeight - 5));
        pack();
        setTitle("GMGen Log Console");
        setLocation(iWinX, iWinY);
        setSize(iWinWidth, iWinHeight);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.getImage(getClass().getResource("/pcgen/gui/resource/gmgen_icon.png"));
        setIconImage(img);
        setVisible(true);
    }

    /**
	 * Handle menu items.
	 * @param e
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == null) {
            setVisible(false);
        } else if (e.getSource() == clear) {
            panel.clearCurrentTab();
            LogUtilities.inst().logMessage("LogWindow", "Clearing current tab");
        }
    }
}
