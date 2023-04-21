package gui.extras;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import buttons.PosObject;
import rcObjects.RepObject;
import constants.PuzzAppConstants;
import gui.MagicPuzzle;

/**
  * @author Holger R�sch, Manuel Scholz
 * 
 */
public class ShowConflictTree extends Frame implements ActionListener, ItemListener {

    private static final long serialVersionUID = 1L;

    private Panel p1;

    private Panel p2;

    private Panel p3;

    private List list;

    private int selected = 0;

    private MagicPuzzle magicP = null;

    private TextField textField;

    private TextArea textArea;

    private int deletedNodes = 0;

    /**
	 * 
	 * @param mp
	 */
    public ShowConflictTree(MagicPuzzle mp) {
        super("KonfliktBaum Anzeige");
        PuzzAppConstants.ShowConflictTreeLogger.info("Konstruktor");
        setMagicPuzzle(mp);
        init();
    }

    private void init() {
        PuzzAppConstants.ShowConflictTreeLogger.debug("init");
        setSize(250, 320);
        setLayout(new GridLayout(3, 1));
        addWindowCloser();
        p1 = new Panel();
        p2 = new Panel();
        p3 = new Panel();
        createPanelOne();
        createPanelTwo();
        createPanelThree();
        this.setVisible(true);
    }

    /**
	 * 
	 */
    private void createPanelThree() {
        PuzzAppConstants.ShowConflictTreeLogger.debug("createPanelThree");
        p3.setLayout(new GridLayout(3, 1));
        Button semantik = new Button("Konfliktbaum Anzeigen (Semantik)");
        semantik.addActionListener(this);
        semantik.setActionCommand("semantik");
        p3.add(semantik);
        Button time = new Button("Konfliktbaum anzeigen (Zeit)");
        time.addActionListener(this);
        time.setActionCommand("time");
        p3.add(time);
        Button back = new Button("zur�ck");
        back.addActionListener(this);
        back.setActionCommand("back");
        p3.add(back);
        add(p3);
    }

    /**
	 * 
	 */
    private void createPanelTwo() {
        PuzzAppConstants.ShowConflictTreeLogger.debug("createPanelTwo");
        p2.setLayout(new GridLayout(1, 1));
        textArea = new TextArea(9, 30);
        p2.add(textArea);
        add(p2);
    }

    /**
	 * 
	 */
    private void createPanelOne() {
        PuzzAppConstants.ShowConflictTreeLogger.debug("createPanelOne");
        Label l1 = new Label("Geben Sie das Puzzleteil ein, f�r das");
        Label l2 = new Label("der Konfliktbaum ausgegeben werden soll");
        textField = new TextField(10);
        p1.add(l1);
        p1.add(l2);
        p1.add(textField);
        add(p1);
    }

    /**
	 * To be able to close the Window
	 */
    private void addWindowCloser() {
        PuzzAppConstants.ShowConflictTreeLogger.debug("addWindowCloser");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
	 * Der �bergebene Konfliktbaum (als Object[])wird ausgegeben
	 * 
	 * @param roa -
	 *            Object[] - der Konfliktbaum (sematische oder zeitl. Ordnung)
	 * @param puzz -
	 *            Puzzleteil-Nummer
	 * @param t -
	 *            wenn 1, dann Semantischer Baum, bei 2 Zeitlich geordneter Baum
	 */
    private void addToPanel(Object[][] roa, int puzz, int t) {
        PuzzAppConstants.ShowConflictTreeLogger.debug("addToPanel");
        RepObject ro;
        boolean deleted = false;
        SimpleDateFormat sdf = new SimpleDateFormat("H:m:s:S");
        textArea.append("Konfliktbaum f�r Puzzleteil " + puzz + "\n");
        if (t == 1) {
            textArea.append("in semantischer Ordnung (inOrder) \n\n");
        } else if (t == 2) {
            textArea.append("in zeitlicher Ordnung \n\n");
        }
        PuzzAppConstants.ShowConflictTreeLogger.debug("Anzahl der Knoten: " + roa.length);
        for (int i = 0; i < roa.length; i++) {
            PuzzAppConstants.ShowConflictTreeLogger.debug("hole RepObject an Stelle " + i);
            ro = (RepObject) roa[i][0];
            if (((Integer) roa[i][1]).intValue() == 0) {
                deleted = false;
            } else {
                deleted = true;
                deletedNodes++;
            }
            PuzzAppConstants.ShowConflictTreeLogger.debug("RepObject " + ro.getObjectID() + " empfangen");
            int cID = ro.getCreatorNodeID();
            PosObject po = (PosObject) ro.getObjectValue();
            if (deleted) {
                textArea.append("folgender Knoten ist gel�scht:\n");
            }
            textArea.append("ID: " + ro.getUpdateID() + "\n");
            textArea.append("  ID: " + ro.getDependencyID() + "\n");
            textArea.append("  neue Position: X: " + po.getPosX() + " Y: " + po.getPosY() + "\n");
            textArea.append("   Erz. lokal um: " + sdf.format(ro.getLocalCreationTime()) + "\n\n");
        }
        textArea.append("Anzahl Knoten im Baum: " + roa.length + "\n");
        textArea.append("Anzahl gel�schter Knoten: " + deletedNodes + "\n");
    }

    public void actionPerformed(ActionEvent arg0) {
        PuzzAppConstants.ShowConflictTreeLogger.info("actionPerformed");
        String ac = arg0.getActionCommand();
        deletedNodes = 0;
        if (ac.equals("semantik")) {
            PuzzAppConstants.ShowConflictTreeLogger.info("gew�nschte Ausgabe: Semantischer Baum");
            textArea.setText("");
            String text = textField.getText();
            try {
                Integer i = new Integer(text);
                int selectedCT = i.intValue();
                if (selectedCT > 0 && selectedCT <= magicP.getNumberOfPuzzlePeaces()) {
                } else {
                    textArea.setText("Dieses Puzzleteil existiert nicht!");
                }
            } catch (NumberFormatException nfe) {
                textField.setText("");
                textArea.append("Es muss eine Zahl eingegeben werden!");
            }
        }
        if (ac.equals("time")) {
            PuzzAppConstants.ShowConflictTreeLogger.info("gew�nschte Ausgabe: zeitlicher Baum");
            textArea.setText("");
            String text = textField.getText();
            try {
                Integer i = new Integer(text);
                int selectedCT = i.intValue();
                if (selectedCT > 0 && selectedCT <= magicP.getNumberOfPuzzlePeaces()) {
                } else {
                    textArea.setText("Dieses Puzzleteil existiert nicht!");
                }
            } catch (NumberFormatException nfe) {
                textField.setText("");
                textArea.append("Es muss eine Zahl eingegeben werden!");
            }
        }
        if (ac.equals("back")) {
            PuzzAppConstants.ShowConflictTreeLogger.info("ok");
            this.setVisible(false);
            this.dispose();
        }
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        PuzzAppConstants.ShowConflictTreeLogger.info("itemStateChanged");
        list = (List) itemEvent.getItemSelectable();
        selected = list.getSelectedIndex();
        PuzzAppConstants.ShowConflictTreeLogger.debug("selektiert: " + selected);
    }

    /**
	 * @param magicPuzzle
	 */
    public void setMagicPuzzle(MagicPuzzle magicPuzzle) {
        PuzzAppConstants.ShowConflictTreeLogger.debug("setMagicPuzzle");
        magicP = magicPuzzle;
    }
}
