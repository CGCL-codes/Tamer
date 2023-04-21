package gui.panels;

import gui.MagicPuzzle;
import java.awt.*;
import java.awt.event.*;
import constants.PuzzAppConstants;
import puzzles.*;
import wrapper.symore.ReplicationSymoreWrapper;

/**
  * @author Holger R�sch, Manuel Scholz
 * 
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu
 * �ndern: Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und
 * Kommentare
 */
public class PuzzleList extends Frame implements ItemListener, ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String selectedItem;

    private List list;

    private boolean m_bDistributed = false;

    /**
	 * Symore wrapper
	 */
    private ReplicationSymoreWrapper m_oSymoreWrapper = null;

    /**
	 * Username
	 */
    private String m_sUsername = null;

    public PuzzleList(ReplicationSymoreWrapper oSymoreWrapper, String sUsername) {
        super("Puzzleauswahl");
        this.m_oSymoreWrapper = oSymoreWrapper;
        this.m_sUsername = sUsername;
        PuzzAppConstants.PuzzleListLogger.info("Konstruktor 1");
        initPanels();
    }

    public PuzzleList(int i, ReplicationSymoreWrapper oSymoreWrapper, String sUsername) {
        super("Remote Puzzleauswahl");
        this.m_oSymoreWrapper = oSymoreWrapper;
        this.m_sUsername = sUsername;
        PuzzAppConstants.PuzzleListLogger.info("Konstruktor 2");
        this.m_bDistributed = true;
        initPanels();
    }

    public void initPanels() {
        PuzzAppConstants.PuzzleListLogger.debug("initPanels");
        setSize(250, 320);
        setLayout(new GridLayout(2, 1));
        addWindowCloser();
        Panel p1 = new Panel();
        Panel p2 = new Panel();
        Puzzle puz = new Puzzle();
        list = puz.getPuzzleList();
        list.select(0);
        list.addActionListener(this);
        list.addItemListener(this);
        list.setSize(300, 300);
        p1.add(list);
        Button ok = new Button("OK");
        ok.addActionListener(this);
        ok.setActionCommand("ok");
        Button cancel = new Button("Abbrechen");
        cancel.addActionListener(this);
        cancel.setActionCommand("cancel");
        p2.add(ok);
        p2.add(cancel);
        add(p1);
        add(p2);
        setVisible(true);
    }

    public void startPuzzle(String uName, String gName) {
        PuzzAppConstants.PuzzleListLogger.debug("startPuzzle");
        PuzzAppConstants.PuzzleListLogger.info("UserName: " + uName);
        PuzzAppConstants.PuzzleListLogger.info("GruppenName: " + gName);
        new MagicPuzzle(selectedItem, this.m_bDistributed, uName, gName, this.m_oSymoreWrapper);
        this.dispose();
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        PuzzAppConstants.PuzzleListLogger.info("itemStateChanged");
        list = (List) itemEvent.getItemSelectable();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        PuzzAppConstants.PuzzleListLogger.info("actionPerformed");
        String command = actionEvent.getActionCommand();
        if (command.equals("ok")) {
            selectedItem = list.getSelectedItem();
            this.setVisible(false);
            if (this.m_bDistributed) {
                new MagicPuzzle(selectedItem, true, this.m_sUsername, "Gruppe1", this.m_oSymoreWrapper);
            } else {
                startPuzzle(this.m_sUsername, "Gruppe1");
            }
        }
        if (command.equals("cancel")) {
            PuzzAppConstants.PuzzleListLogger.info("Beenden");
            System.exit(0);
        }
        if (command.equals("next")) {
            this.setVisible(false);
            this.dispose();
            new PuzzleList(this.m_oSymoreWrapper, this.m_sUsername);
        }
        Object obj = actionEvent.getSource();
        if (obj instanceof List) {
        } else if (obj instanceof Button) {
            if (actionEvent.getActionCommand().equals("Ende")) {
                System.exit(0);
            }
        }
    }

    /**
	 * To be able to close the Window
	 */
    private void addWindowCloser() {
        PuzzAppConstants.PuzzleListLogger.debug("addWindowCloser");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                PuzzAppConstants.PuzzleListLogger.info("windowClosing");
                System.exit(0);
            }
        });
    }
}
