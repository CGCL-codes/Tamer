package fr.irisa.asap.debug.debugGUI;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import fr.irisa.asap.debug.manager.IManager;
import fr.irisa.asap.debug.manager.Manager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.List;
import java.io.File;

/**
 * The window of the debugger where you can choice the stations. <br />
 * 
 * @author DebugProject_Fabien-Loisel
 * created : 12/03/2007
 * @version $Id: StationChoice.java 369 2007-06-14 20:30:19Z fabien.loisel $ 
 */
public class StationChoice extends JFrame implements IDebugWindow {

    private static final long serialVersionUID = -7464484322697678205L;

    private JFileChooser jc;

    private JLabel fileChoosenMessage;

    private JLabel fileChoosenContent;

    private ButtonGroup bGroup;

    private JRadioButton jrOne2One, jrAll;

    private List adds;

    private JTextField adresse;

    public static final int fromPlay = 1;

    public static final int fromReplay = 2;

    private final int fromWhere;

    private IManager manager = Manager.instance();

    private static final int NUM_GET_LIST_B = 1;

    private static final int NUM_ADD = 2;

    private static final int NUM_OK = 3;

    private static final int NUM_CANCEL = 4;

    private static final int NUM_RM = 5;

    /**
	 * The constructor, set up the frame
	 */
    public StationChoice(int fromWhere) {
        super();
        this.fromWhere = fromWhere;
        jc = new JFileChooser();
        jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        setupFrame();
    }

    private void setupFrame() {
        String maintText = "";
        if (fromWhere == fromPlay) maintText = Messages.getString("StationChoiceMainTextPlay"); else if (fromWhere == fromReplay) maintText = Messages.getString("StationChoiceMainTextReplay");
        JLabel jlMain = new JLabel(maintText, JLabel.CENTER);
        jlMain.setFont(FontFactory.getMainFont());
        jlMain.setForeground(FontFactory.getMainFontColor());
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(new JLabel());
        header.add(jlMain);
        JPanel center = new JPanel(new GridLayout(1, 3));
        center.setOpaque(false);
        JPanel centerLeft = new JPanel(new GridLayout(5, 1));
        centerLeft.setOpaque(false);
        JPanel centerCenter = new JPanel(new GridLayout(5, 1));
        centerCenter.setOpaque(false);
        bGroup = new ButtonGroup();
        JPanel one2one = new JPanel(new GridLayout(4, 1));
        one2one.setOpaque(false);
        jrOne2One = new JRadioButton(Messages.getString("StationChoiceText1"));
        jrOne2One.setOpaque(false);
        jrOne2One.setMnemonic(1);
        bGroup.add(jrOne2One);
        one2one.add(jrOne2One);
        adresse = new JTextField();
        one2one.add(adresse);
        JLabel syntaxe = new JLabel(Messages.getString("StationChoiceSynt"));
        syntaxe.setForeground(new Color(200, 200, 200));
        JLabel exemple = new JLabel(Messages.getString("StationChoiceEx"));
        exemple.setForeground(new Color(200, 200, 200));
        one2one.add(syntaxe);
        one2one.add(exemple);
        jrAll = new JRadioButton(Messages.getString("StationChoiceText2"));
        jrAll.setOpaque(false);
        jrAll.setMnemonic(2);
        bGroup.add(jrAll);
        centerLeft.add(new JLabel());
        centerLeft.add(one2one);
        centerLeft.add(new JLabel());
        centerLeft.add(jrAll);
        JPanel rmAddPanel = new JPanel(new GridLayout(3, 1));
        rmAddPanel.setOpaque(false);
        DebugButton addButton = new DebugButton(">>", this, NUM_ADD);
        DebugButton rmButton = new DebugButton(Messages.getString("StationChoiceRm"), this, NUM_RM);
        rmAddPanel.add(addButton);
        rmAddPanel.add(new JLabel());
        rmAddPanel.add(rmButton);
        JPanel sel = new JPanel(new GridLayout(3, 1));
        sel.setOpaque(false);
        DebugButton getListB = new DebugButton(Messages.getString("StationChoiceGetListB"), this, NUM_GET_LIST_B);
        fileChoosenContent = new JLabel("", JLabel.CENTER);
        fileChoosenMessage = new JLabel("", JLabel.CENTER);
        sel.add(getListB);
        sel.add(fileChoosenMessage);
        sel.add(fileChoosenContent);
        centerCenter.add(new JLabel());
        centerCenter.add(rmAddPanel);
        centerCenter.add(new JLabel());
        centerCenter.add(sel);
        JPanel rightList = new JPanel(new GridLayout(2, 1));
        rightList.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        rightList.setOpaque(false);
        adds = new List();
        rightList.add(adds);
        center.add(centerLeft);
        center.add(centerCenter);
        center.add(rightList);
        JPanel footer = new JPanel(new GridLayout(1, 3));
        footer.setOpaque(false);
        footer.add(new DebugButton(Messages.getString("StationChoiceBackButton"), this, NUM_CANCEL));
        footer.add(new JLabel());
        String OKB = "";
        if (fromWhere == fromPlay) OKB = Messages.getString("StationChoiceDeploymentButton"); else if (fromWhere == fromReplay) OKB = Messages.getString("StationChoiceContinueButton");
        footer.add(new DebugButton(OKB, this, NUM_OK));
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
        int returnVal;
        switch(id) {
            case NUM_OK:
                if (bGroup.getSelection() != null) {
                    if (bGroup.getSelection().getMnemonic() == jrOne2One.getMnemonic()) {
                        if (adds.getItemCount() == 0) new DebugPopup(this, Messages.getString("StationChoicePopTitle"), Messages.getString("StationChoicePopLEmptyM1"), Messages.getString("StationChoicePopLEmptyM2")); else {
                            if (fromWhere == fromPlay) new AppliChoice(fromWhere).setVisible(true); else if (fromWhere == fromReplay) new ModeChoice().setVisible(true);
                            this.setVisible(false);
                            this.setEnabled(false);
                        }
                    } else {
                        if (jc.getSelectedFile() != null) {
                            try {
                                manager.setPcListFromFile(jc.getSelectedFile().getAbsolutePath());
                                if (fromWhere == fromPlay) new AppliChoice(fromWhere).setVisible(true); else if (fromWhere == fromReplay) new ModeChoice().setVisible(true);
                                this.setVisible(false);
                                this.setEnabled(false);
                            } catch (Exception e) {
                                new DebugPopup(this, Messages.getString("StationChoicePopTitle"), Messages.getString("StationChoicePopFileNotAvailable1"), Messages.getString("StationChoicePopFileNotAvailable2"));
                            }
                        } else {
                            new DebugPopup(this, Messages.getString("StationChoicePopTitle"), Messages.getString("StationChoicePopNoFileM1"), Messages.getString("StationChoicePopNoFileM2"));
                        }
                    }
                } else {
                    new DebugPopup(this, Messages.getString("StationChoicePopTitle"), Messages.getString("StationChoicePopNoRadM1"), Messages.getString("StationChoicePopNoRadM2"));
                }
                ;
                break;
            case NUM_CANCEL:
                manager.eraseCurrentScenario();
                new MainWindow().setVisible(true);
                this.setVisible(false);
                this.setEnabled(false);
                ;
                break;
            case NUM_GET_LIST_B:
                returnVal = jc.showDialog(this, Messages.getString("FileChooserButton"));
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = jc.getSelectedFile();
                    fileChoosenMessage.setText(Messages.getString("FileAttachOpened"));
                    fileChoosenContent.setText(file.getName());
                    jrAll.setSelected(true);
                } else {
                    fileChoosenMessage.setText(Messages.getString("FileAttachCancelled"));
                    fileChoosenContent.setText("");
                }
                ;
                break;
            case NUM_ADD:
                if (adresse.getDocument().getLength() != 0) {
                    jrOne2One.setSelected(true);
                    if (adresse.getText().matches(".*\\:\\d*(,\\d*)*")) {
                        adds.add(adresse.getText());
                        try {
                            manager.addPcList(adresse.getText());
                        } catch (Exception e) {
                            System.err.println("Format rentré incorrect");
                            e.printStackTrace();
                        }
                        adresse.setText("");
                    }
                }
                ;
                break;
            case NUM_RM:
                if (adds.getSelectedIndex() != -1) {
                    manager.removePcFromList(adds.getName());
                    adds.remove(adds.getSelectedIndex());
                }
                ;
                break;
        }
    }

    public static void main(String arg[]) {
        new StationChoice(fromPlay);
    }
}
