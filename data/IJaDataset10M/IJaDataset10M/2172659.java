package fr.irisa.asap.debug.debugGUI;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import fr.irisa.asap.debug.manager.IManager;
import fr.irisa.asap.debug.manager.Manager;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * The window of the debugger where you are asked if you want to use the current scenario. <br />
 * 
 * @author DebugProject_Alexandrine-Andrieu
 * created : 18/03/2007
 * @version $Id: Record.java 317 2007-06-07 15:43:36Z fabien.loisel $ 
 */
public class UseCurrent extends JFrame implements IDebugWindow {

    public static final int REPLAY = 7;

    public static final int CHANGE = 8;

    public static final int OBS = 9;

    private int _type;

    private static final long serialVersionUID = -7464484322697678205L;

    private JFileChooser jc;

    private ButtonGroup bGroup;

    private JRadioButton jrYes, jrNo;

    private IManager manager = Manager.instance();

    private static final int NUM_NEXT = 2;

    private static final int NUM_BACK = 3;

    /**
	 * The constructor, set up the frame
	 */
    public UseCurrent(int type) {
        super();
        _type = type;
        jc = new JFileChooser();
        jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setupFrame();
    }

    private void setupFrame() {
        JLabel jlMain = new JLabel(Messages.getString("RecordMainText"), JLabel.CENTER);
        jlMain.setFont(FontFactory.getMainFont());
        jlMain.setForeground(FontFactory.getMainFontColor());
        JPanel header = new JPanel(new GridLayout(3, 1));
        header.setOpaque(false);
        header.add(new JLabel());
        header.add(new JLabel());
        header.add(jlMain);
        JPanel center = new JPanel(new GridLayout(1, 3));
        center.setOpaque(false);
        JPanel centermiddle = new JPanel(new GridLayout(4, 1));
        centermiddle.setOpaque(false);
        bGroup = new ButtonGroup();
        JPanel yes = new JPanel(new GridLayout(1, 2));
        yes.setOpaque(false);
        jrYes = new JRadioButton(Messages.getString("RecordYes"));
        jrYes.setOpaque(false);
        bGroup.add(jrYes);
        yes.add(jrYes);
        JPanel no = new JPanel(new GridLayout(1, 1));
        no.setOpaque(false);
        jrNo = new JRadioButton(Messages.getString("RecordNo"));
        jrNo.setOpaque(false);
        bGroup.add(jrNo);
        no.add(jrNo);
        centermiddle.add(new JLabel());
        centermiddle.add(yes);
        centermiddle.add(no);
        center.add(new JLabel());
        center.add(centermiddle);
        JPanel footer = new JPanel(new GridLayout(1, 3));
        footer.setOpaque(false);
        footer.add(new DebugButton(Messages.getString("ModeChoiceBackButton"), this, NUM_BACK));
        footer.add(new JLabel());
        footer.add(new DebugButton(Messages.getString("RecordNext"), this, NUM_NEXT));
        WindowConfigurator wc = new WindowConfigurator(this, true);
        JPanel foregroundPanel = wc.getPanel();
        foregroundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        foregroundPanel.setLayout(new BorderLayout());
        foregroundPanel.add(header, "North");
        foregroundPanel.add(center, "Center");
        foregroundPanel.add(footer, "South");
        wc.buildWindow();
    }

    /**
	 * @see IDebugWindow#actionOnClick(int)
	 */
    public void actionOnClick(int id) {
        switch(id) {
            case NUM_NEXT:
                if (bGroup.getSelection() != null) {
                    if (jrNo.isSelected()) {
                        manager.eraseCurrentScenario();
                    }
                    if (CHANGE == _type) {
                        if (jrYes.isSelected()) new StationChoiceVerification(manager.getCurrentScenario().getLogLink(), StationChoiceVerification.fromVerif).setVisible(true); else new Verification().setVisible(true);
                    } else if (OBS == _type) {
                        if (jrYes.isSelected()) new Comparison(Comparison.SECOND).setVisible(true); else new Comparison(Comparison.FIRST).setVisible(true);
                    } else if (REPLAY == _type) {
                        if (jrYes.isSelected()) {
                            new StationChoice(StationChoice.fromReplay).setVisible(true);
                            manager.setReferenceScenario(manager.getCurrentScenario());
                            manager.initScenario();
                        } else new ScenarioChoice().setVisible(true);
                    }
                    this.setVisible(false);
                    this.setEnabled(false);
                } else {
                    new DebugPopup(this, Messages.getString("RecordPopTitle"), Messages.getString("RecordPopT1"), Messages.getString("RecordPopT2"));
                }
                ;
                break;
            case NUM_BACK:
                new MainWindow().setVisible(true);
                this.setVisible(false);
                this.setEnabled(false);
        }
    }

    public static void main(String arg[]) {
        new UseCurrent(7);
    }
}
