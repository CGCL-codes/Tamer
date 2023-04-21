package de.uni_mannheim.swt.pm_7.fdh.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * The Class NewGameDialog.
 */
public class NewGameDialog extends JFrame implements ActionListener, InputMethodListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2880824120804425314L;

    /** The click new game_. */
    private JButton clickNewGame_;

    /** The clicked new game listener_. */
    private ActionListener clickedNewGameListener_;

    /** The listr of player names_. */
    private ArrayList<JTextField> listrOfPlayerNames_ = new ArrayList<JTextField>();

    /** The computer player check box_. */
    private ArrayList<JCheckBox> computerPlayerCheckBox_ = new ArrayList<JCheckBox>();

    /** The plyer color list_. */
    private ArrayList<ColorpickDialog> plyerColorList_ = new ArrayList<ColorpickDialog>();

    /** The list of player element names_. */
    private ArrayList<JLabel> listOfPlayerElementNames_ = new ArrayList<JLabel>();

    /** The list of players_. */
    private JList listOfPlayers_;

    /** The list model. */
    private DefaultListModel listModel;

    /** The start game. */
    private JButton startGame;

    /** The Start new game action_. */
    private ActionListener StartNewGameAction_;

    /** The mouse listener_. */
    private MouseListener mouseListener_;

    /** The analyse game button_. */
    private JButton analyseGameButton_;

    /** The clicked analyse_. */
    private ActionListener clickedAnalyse_;

    /** The counting_. */
    private int counting_;

    /** The choose replay game_. */
    private JFileChooser chooseReplayGame_;

    /** The directory_. */
    private Object directory_;

    /** The reset game_. */
    private JButton resetGame_;

    /** The reset game act_. */
    private ActionListener resetGameAct_;

    public static void main(String... args) {
        NewGameDialog game = new NewGameDialog();
    }

    /**
	 * Instantiates a new new game dialog.
	 */
    public NewGameDialog() {
        super();
        this.setSize(1000, 400);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.BLACK);
        this.setEnabled(true);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.BLACK);
        this.setLocationByPlatform(true);
        this.initButton();
        this.getContentPane().add(this.clickNewGame_);
        this.setResizable(false);
        this.setVisible(true);
        for (int i = 0; i < 4; i++) {
            this.listrOfPlayerNames_.add(new JTextField());
            this.computerPlayerCheckBox_.add(new JCheckBox());
            this.plyerColorList_.add(new ColorpickDialog());
            this.listOfPlayerElementNames_.add(new JLabel());
        }
        this.initList();
        this.getContentPane().add(this.listOfPlayers_);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    }

    @Override
    public void caretPositionChanged(InputMethodEvent arg0) {
    }

    /**
	 * Inits the button.
	 */
    public void initButton() {
        int width = 200;
        int height = 100;
        this.clickNewGame_ = new JButton(Messages.getString("NewGameDialog.0"));
        this.clickNewGame_.setBounds(this.getWidth() / 2 - width / 2, this.getHeight() / 2 - height / 2 - 100, width, height);
        this.clickNewGame_.setForeground(Color.WHITE);
        this.clickNewGame_.setBackground(Color.BLACK);
        this.clickNewGame_.setVisible(true);
        this.clickedNewGameListener_ = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameDialog.this.counting_ = NewGameDialog.this.listOfPlayers_.getSelectedIndices()[0];
                NewGameDialog.this.openPlayerDialog();
            }
        };
        this.clickNewGame_.addActionListener(this.clickedNewGameListener_);
        this.analyseGameButton_ = new JButton(Messages.getString("NewGameDialog.1"));
        this.analyseGameButton_.setBounds(this.getWidth() / 4 - width / 2, this.getHeight() / 2 - height / 2 - 100, width, height);
        this.analyseGameButton_.setForeground(Color.WHITE);
        this.analyseGameButton_.setBackground(Color.BLACK);
        this.analyseGameButton_.setVisible(true);
        this.getContentPane().add(this.analyseGameButton_);
        this.clickedAnalyse_ = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameDialog.this.openAnalyseDialog();
            }
        };
        this.analyseGameButton_.addActionListener(this.clickedAnalyse_);
        this.resetGame_ = new JButton(Messages.getString("NewGameDialog.10"));
        this.resetGame_.setBounds((int) (this.getWidth() / 1.33 - width / 2), this.getHeight() / 2 - height / 2 - 100, width, height);
        this.resetGame_.setForeground(Color.WHITE);
        this.resetGame_.setBackground(Color.BLACK);
        this.resetGame_.setVisible(true);
        this.getContentPane().add(this.resetGame_);
        this.resetGameAct_ = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameDialog.this.openLastGame();
            }
        };
        this.resetGame_.addActionListener(this.resetGameAct_);
    }

    /**
	 * Inits the list.
	 */
    public void initList() {
        int width = 200;
        int height = 200;
        this.listModel = new DefaultListModel();
        this.listModel.addElement(Messages.getString("NewGameDialog.2"));
        this.listModel.addElement(Messages.getString("NewGameDialog.3"));
        this.listModel.addElement(Messages.getString("NewGameDialog.4"));
        this.listOfPlayers_ = new JList(this.listModel);
        this.listOfPlayers_.setSelectedIndex(1);
        this.listOfPlayers_.setBounds(this.getWidth() / 2 - width / 2, this.getHeight() / 2 - height / 2 + 150, width, height);
        this.listOfPlayers_.setForeground(Color.WHITE);
        this.listOfPlayers_.setBackground(Color.BLACK);
        this.listOfPlayers_.setVisible(true);
    }

    @Override
    public void inputMethodTextChanged(InputMethodEvent arg0) {
    }

    /**
	 * Open analyse dialog.
	 */
    protected void openAnalyseDialog() {
        this.chooseReplayGame_ = new JFileChooser();
        String property = Messages.getString("NewGameDialog.5");
        this.directory_ = System.getProperty(property);
        File file = new File(this.directory_ + Messages.getString("NewGameDialog.6"));
        file.mkdir();
        this.chooseReplayGame_.setCurrentDirectory(file);
        int state = this.chooseReplayGame_.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            File chosenFile = this.chooseReplayGame_.getSelectedFile();
            System.out.println(chosenFile.getAbsolutePath());
            this.openReplayMode(chosenFile);
        }
        this.chooseReplayGame_.setVisible(true);
    }

    /**
	 * Open last game.
	 */
    protected void openLastGame() {
        String property = Messages.getString("NewGameDialog.5");
        this.directory_ = System.getProperty(property);
        File file = new File(this.directory_ + Messages.getString("NewGameDialog.6"));
        file.mkdir();
        File tempfile = file.listFiles()[0];
        for (File f : file.listFiles()) {
            if (f.lastModified() > tempfile.lastModified()) {
                tempfile = f;
            }
        }
        this.openResetGame(tempfile);
    }

    /**
	 * Open player dialog.
	 */
    private void openPlayerDialog() {
        int num = this.listOfPlayers_.getLeadSelectionIndex() + 2;
        this.mouseListener_ = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                for (ColorpickDialog c : NewGameDialog.this.plyerColorList_) {
                    if (c.getColor().equals(((ColorpickDialog) e.getSource()).getColor())) {
                        c.setUnselected();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (ColorpickDialog c : NewGameDialog.this.plyerColorList_) {
                    if (c.getColor().equals(((ColorpickDialog) e.getSource()).getColor())) {
                        c.setUnselected();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        };
        this.getContentPane().removeAll();
        for (int i = 0; i < num; i++) {
            this.listOfPlayerElementNames_.get(i).setBounds(50, (80 * i) + 30, 100, 30);
            this.listOfPlayerElementNames_.get(i).setText(Messages.getString("NewGameDialog.7") + (i + 1));
            this.listOfPlayerElementNames_.get(i).setForeground(Color.WHITE);
            this.listOfPlayerElementNames_.get(i).setBackground(Color.BLACK);
            this.listOfPlayerElementNames_.get(i).setVisible(true);
            this.listrOfPlayerNames_.get(i).setBounds(200, (80 * i) + 30, 100, 30);
            this.listrOfPlayerNames_.get(i).setVisible(true);
            this.listrOfPlayerNames_.get(i).setForeground(Color.WHITE);
            this.listrOfPlayerNames_.get(i).setBackground(Color.BLACK);
            this.listrOfPlayerNames_.get(i).addInputMethodListener(this);
            this.listrOfPlayerNames_.get(i).setText(this.listOfPlayerElementNames_.get(i).getText());
            this.computerPlayerCheckBox_.get(i).setBounds(400, (80 * i) + 30, 100, 30);
            this.computerPlayerCheckBox_.get(i).setText(Messages.getString("NewGameDialog.8"));
            this.computerPlayerCheckBox_.get(i).setVisible(true);
            this.computerPlayerCheckBox_.get(i).setForeground(Color.WHITE);
            this.computerPlayerCheckBox_.get(i).setBackground(Color.BLACK);
            this.plyerColorList_.get(i).setLocation(600, (80 * i) + 30);
            this.plyerColorList_.get(i).addMouseListener(this.mouseListener_);
            this.plyerColorList_.get(i).setVisible(true);
        }
        for (JTextField f : this.listrOfPlayerNames_) {
            this.getContentPane().add(f);
        }
        for (JCheckBox f : this.computerPlayerCheckBox_) {
            this.getContentPane().add(f);
        }
        for (JLabel f : this.listOfPlayerElementNames_) {
            this.getContentPane().add(f);
        }
        for (ColorpickDialog f : this.plyerColorList_) {
            this.getContentPane().add(f);
        }
        this.startGame = new JButton(Messages.getString("NewGameDialog.9"));
        this.startGame.setBounds(this.getWidth() - 230, this.getHeight() - 70, 200, 40);
        this.startGame.setForeground(Color.WHITE);
        this.startGame.setBackground(Color.BLACK);
        this.startGame.setVisible(true);
        this.StartNewGameAction_ = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameDialog.this.startNewGame();
            }
        };
        this.startGame.addActionListener(this.StartNewGameAction_);
        this.getContentPane().add(this.startGame);
        this.validate();
        this.repaint();
    }

    /**
	 * Open replay mode.
	 *
	 * @param chosenFile the chosen file
	 */
    private void openReplayMode(File chosenFile) {
        try {
            this.setVisible(false);
            FDHMainView game = new FDHMainView();
            game.setEnabled(true);
            game.repaint();
            game.validate();
            game.initReplayMode(chosenFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Open reset game.
	 *
	 * @param tempfile the tempfile
	 */
    private void openResetGame(File tempfile) {
        try {
            this.setVisible(false);
            FDHMainView game = new FDHMainView();
            game.setEnabled(true);
            game.repaint();
            game.validate();
            game.initResetMode(tempfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Start new game.
	 */
    protected void startNewGame() {
        this.setVisible(false);
        FDHMainView game = new FDHMainView();
        game.setEnabled(true);
        game.repaint();
        game.validate();
        this.counting_ = this.listOfPlayers_.getAnchorSelectionIndex() + 2;
        System.out.print(this.counting_);
        String[] names = new String[this.counting_];
        Color[] color = new Color[this.counting_];
        boolean[] computer = new boolean[this.counting_];
        try {
            int i = 0;
            while (i < this.counting_) {
                color[i] = this.plyerColorList_.get(i).getColor();
                names[i] = this.listrOfPlayerNames_.get(i).getText();
                computer[i] = this.computerPlayerCheckBox_.get(i).isSelected();
                i++;
            }
            game.initGame(names, color, computer);
            game.displayPlayers();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        this.dispose();
    }
}
