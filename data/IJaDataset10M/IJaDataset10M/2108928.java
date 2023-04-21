package yajdr.core;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import yajdr.gui.D20Gui;
import yajdr.gui.D20XpGui;
import yajdr.gui.LoadingScreen;
import yajdr.gui.ShadowrunGui;
import yajdr.gui.StarWarsXpGui;
import yajdr.gui.WorldOfDarknessGui;
import yajdr.interfaces.ThreadProgress;

/**
 * Title: Dice Roller Application Description: As the previous Die Roller, but this now has a GUI,
 * and thus is more versatile. As of 27/05/04 it also uses threads, to allow people to try and roll
 * huge amounts of dice. However, I'm still undecided as to whether I should keep the cutoff point
 * for new threads at 1000 dice, or to increase it. I also need to add in thread capability for the
 * d20 part of the program. Oh, and I've added a scrollpane for the WoD output area. Still need to
 * add it to d20.
 * 
 * @todo: Add JScrollPane to d20Output (DONE)
 * @todo: Thread d20RollDice (DONE)
 * @todo: Extend the program so that it will roll additional systems.
 * @todo: Add additional frames (DONE)
 * @todo: Add character sheet capabilities. Copyright: Copyright (c) 2004
 * @author Andrew Thorburn
 * @version 6.0
 *          <p>
 *          As of v 6.0, I have added in an experience point calculator, and put each part of the
 *          GUI into it's own frame, in order to clear this class up a bit, and make it easier for
 *          me to make changes.
 *          </p>
 */
public class DieRoller extends JFrame implements ActionListener, ChangeListener, ThreadProgress {

    private static final long serialVersionUID = 1640152470711610549L;

    private static final Log log = LogFactory.getLog(DieRoller.class);

    private JTabbedPane gameLine = new JTabbedPane();

    private JTabbedPane d20TabPane = new JTabbedPane();

    private JMenuBar bar = new JMenuBar();

    private JMenu file = new JMenu("File");

    private JMenu helpM = new JMenu("Help");

    private JMenuItem newF = new JMenuItem("New Die Roller Window");

    private JMenuItem exit = new JMenuItem("Exit all frames");

    private JMenuItem about = new JMenuItem("About");

    private JMenuItem helpMI = new JMenuItem("Help");

    private JProgressBar progBar = new JProgressBar(0, 100);

    private JLabel error = new JLabel("Done");

    /**
	 * <p>
	 * This creates a new frame, which beings by setting the layout to flow. Each tab of the frame
	 * is a seperate class, but they are instantiated here. The menu bar is also created in here.
	 * <p>
	 * 
	 * @param loadingScreen
	 *            ThreadProgress
	 * @see D20XpGui
	 * @see D20Gui
	 * @see ShadowrunGui
	 * @see WorldOfDarknessGui
	 */
    public DieRoller(ThreadProgress loadingScreen) {
        log.trace("Loading base die roller...");
        int numberPanels = 13;
        int panelsDone = 0;
        getContentPane().setLayout(new BorderLayout());
        setDefaultLookAndFeelDecorated(false);
        WorldOfDarknessGui g = new WorldOfDarknessGui();
        getContentPane().add(g);
        updateLoader(++panelsDone, numberPanels, "wodGUI initialised", loadingScreen);
        JPanel b = new JPanel(new GridLayout());
        b.add(error, BorderLayout.NORTH);
        error.setBorder(BorderFactory.createLoweredBevelBorder());
        b.add(progBar, BorderLayout.SOUTH);
        progBar.setBorder(BorderFactory.createLoweredBevelBorder());
        getContentPane().add(b, BorderLayout.SOUTH);
        updateLoader(++panelsDone, numberPanels, "utility initialised", loadingScreen);
        D20Gui tmp = new D20Gui();
        getContentPane().add(tmp);
        updateLoader(++panelsDone, numberPanels, "d20GUI initialised", loadingScreen);
        D20XpGui temp = new D20XpGui();
        getContentPane().add(temp);
        updateLoader(++panelsDone, numberPanels, "d20xpGUI initialised", loadingScreen);
        StarWarsXpGui sw = new StarWarsXpGui();
        getContentPane().add(sw);
        updateLoader(++panelsDone, numberPanels, "swXPGUI initialised", loadingScreen);
        d20TabPane.addTab("D20 Dice Roller", null, tmp, "Dice Roller for all d20 systems");
        d20TabPane.addTab("D20 Experience Calculator", null, temp, "Calculates Experience");
        d20TabPane.addTab("Star Wars Experience Calculator", null, sw, "Gets XP for Star Wars");
        updateLoader(++panelsDone, numberPanels, "tabbed pane initialised", loadingScreen);
        ShadowrunGui sg = new ShadowrunGui();
        getContentPane().add(sg);
        updateLoader(++panelsDone, numberPanels, "shadowrunGUI initialised", loadingScreen);
        GameNotes gn = new GameNotes();
        getContentPane().add(gn);
        updateLoader(++panelsDone, numberPanels, "gameNotes initialised", loadingScreen);
        RandomName rn = new RandomName();
        updateLoader(++panelsDone, numberPanels, "RandomName initialised", loadingScreen);
        gameLine.addTab("World of Darkness", null, g, "Dice Roller for WoD");
        gameLine.addTab("D20", null, d20TabPane, "Includes Die Roller and XP calculator");
        gameLine.addTab("Shadowrun", null, sg, "Dice Roller for SR system");
        gameLine.addTab("Notes", null, gn, "Facility for taking notes");
        gameLine.addTab("Random Name Generator", null, rn, "Generates random fantasy-ish names");
        updateLoader(++panelsDone, numberPanels, "tabbed pane initialised", loadingScreen);
        getContentPane().add(gameLine);
        gameLine.addChangeListener(this);
        setJMenuBar(bar);
        bar.add(file);
        bar.add(helpM);
        file.add(newF);
        file.add(exit);
        helpM.add(helpMI);
        helpM.add(about);
        updateLoader(++panelsDone, numberPanels, "menu initialised", loadingScreen);
        newF.addActionListener(this);
        exit.addActionListener(this);
        helpMI.addActionListener(this);
        about.addActionListener(this);
        g.setParent(this);
        sw.setParent(this);
        sg.setParent(this);
        tmp.setParent(this);
        updateLoader(++panelsDone, numberPanels, "Done", loadingScreen);
        setSize(585, 588);
        setLocation(DieRollerMain.centreOnScreen(getSize()));
    }

    private void updateLoader(int current, int total, String message, ThreadProgress loader) {
        loader.update(current, total);
        loader.update(message);
    }

    /**
	 * <p>
	 * This is called whenever a menu item is selected. There are no buttons in this class. All
	 * buttons are declared in their own classes, and have action listeners registered to those
	 * classes
	 * </p>
	 * 
	 * @param aE
	 *            ActionEvent
	 */
    public void actionPerformed(ActionEvent aE) {
        Object buttonPressed = aE.getSource();
        if (buttonPressed == newF) {
            DieRoller d = new DieRoller(new LoadingScreen());
            d.setSize(600, 600);
            d.setVisible(true);
            d.setTitle("Storyteller system Dice Roller");
            d.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } else if (buttonPressed == exit) System.exit(0); else if (buttonPressed == helpMI) Help(); else if (buttonPressed == about) JOptionPane.showMessageDialog(null, "This contains a variety of stuff - most of it should be fairly obvious" + " but if it's not, I'm contactable at ipsi@paradise.net.nz\n" + "Also, the actual page for this is at http://homepages.paradise.net.nz/ipsi\n" + "Nothing more to say. All useful info is on the website.", "About Die Roller", JOptionPane.INFORMATION_MESSAGE);
    }

    public void stateChanged(ChangeEvent ce) {
        update("Ready");
    }

    public void update(int current, int total) {
        progBar.setMaximum(total);
        progBar.setValue(current);
    }

    public void update(String message) {
        error.setText(message);
    }

    public void resetProgressBar() {
        progBar.setMaximum(100);
        progBar.setValue(0);
    }

    /**
	 * <p>
	 * This generates a message dialog, which is essentially the help menu, though, admittedly, not
	 * very helpful
	 * </p>
	 */
    private void Help() {
        JOptionPane.showMessageDialog(null, "Enter the target number in the difficulty box" + "\nThe reset button will reset everything to the defaults for Exalted." + "\nThe check boxes should be fairly obvious, and are built to cater to Exalted.");
    }
}
