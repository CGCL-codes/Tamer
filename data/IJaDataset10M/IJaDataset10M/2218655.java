package sudoku.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.JToolBar.Separator;
import sudoku.commands.*;
import sudoku.core.Difficulty;
import sudoku.core.Grid;
import jguic.Command;
import jguic.Mediator;
import jguic.MediatorExtension;
import jguic.util.DesactivateRedo;
import jguic.util.DesactivateUndo;
import jguic.util.Redo;
import jguic.util.Undo;
import jguic.util.UndoableCommand;

/**
 * Impl�mente le menu du jeu
 * 
 * @author Romain HUET
 * @author Nicolas RAYNAUD
 */
public class GUIMenu extends MediatorExtension {

    /** Menu */
    private JMenuBar swing_menu;

    /** Bouton sauvegarder partie */
    private JMenuItem swing_saveGame;

    /** Bouton charger partie */
    private JMenuItem swing_loadGame;

    /** Bouton recommencer grille */
    private JMenuItem swing_resetGame;

    /** Bouton publier grille */
    private JMenuItem swing_publishGrid;

    /** Bouton sauvegarder grille */
    private JMenuItem swing_saveGrid;

    /** Bouton pause / play */
    private JMenuItem swing_playpause;

    /** Bouton annuler */
    private JMenuItem swing_undo;

    /** Bouton refaire */
    private JMenuItem swing_redo;

    /** Bouton v�rifier la solution */
    private JMenuItem swing_checkSolution;

    /** Bouton voir la solution (joueur) */
    private JMenuItem swing_showSolutionP;

    /** Bouton trouver une solution */
    private JMenuItem swing_findSolution;

    /** Bouton voir la solution (cr�ateur) */
    private JMenuItem swing_showSolutionC;

    /** Bouton valider */
    private JMenuItem swing_validate;

    /** Bouton rubrique d'aide */
    private JMenuItem swing_help;

    /** Bouton � propos */
    private JMenuItem swing_about;

    /** Menu avec bouton radio pour les possibilit�s avec chiffres */
    private JRadioButtonMenuItem possibilitiesWithFigures;

    /** Menu avec bouton radio pour les possibilit�s avec cercles */
    private JRadioButtonMenuItem possibilitiesWithCircles;

    /** Ic�ne de la fen�tre */
    private static Image swing_var_iconImage = new ImageIcon("images/sudokuJ.png").getImage();

    /** Image d'arri�re-plan "Aide" */
    private static Image swing_var_helpImage = new ImageIcon("images/help.png").getImage();

    /** Image d'arri�re-plan "� propos" */
    private static Image swing_var_aboutImage = new ImageIcon("images/about.png").getImage();

    /**
	 * Construit le menu
	 * 
	 * @param parent
	 *            M�diateur parent
	 */
    public GUIMenu(Mediator parent) {
        super(parent);
        swing_menu = new JMenuBar();
        constructFileMenu();
        constructActionsMenu();
        constructOptionsMenu();
        constructHelpMenu();
    }

    /**
	 * Impl�mente la rubrique d'aide sur SudokuJ
	 * 
	 * @author Romain HUET
	 * @author Nicolas RAYNAUD
	 */
    public class HelpScreen {

        /** Fen�tre "� propos" */
        private JFrame swing_helpFrame = null;

        /** Panel contenant l'image */
        private JPanel swing_helpPanel = null;

        public HelpScreen() {
            swing_helpFrame = new JFrame();
            swing_helpFrame.setIconImage(swing_var_iconImage);
            swing_helpFrame.setSize(500, 400);
            swing_helpFrame.setResizable(false);
            swing_helpFrame.setTitle("Rubrique d'aide de SudokuJ");
            Dimension screen = swing_helpFrame.getToolkit().getScreenSize();
            swing_helpFrame.setLocation((int) (screen.getWidth() - swing_helpFrame.getWidth()) / 2, (int) (screen.getHeight() - swing_helpFrame.getHeight()) / 2);
            swing_helpPanel = new JPanel() {

                public void paintComponent(Graphics g) {
                    if (swing_var_aboutImage.getWidth(null) > 0) g.drawImage(swing_var_helpImage, 0, 0, this);
                }
            };
            swing_helpPanel.setSize(500, 400);
            JTextArea rules = new JTextArea("Une grille de SudokuJ est constitu�e d'un carr� de " + "neuf cases de c�t�, subdivis� en autant de carr�s " + "identiques, �galement appel�s r�gions.\n" + "La r�gle du jeu est la suivante : chaque ligne, " + "colonne et r�gion ne doit contenir qu'une seule " + "fois tous les chiffres de 1 � 9. Autrement dit, " + "chacun de ces ensembles doit contenir tous les " + "chiffres de 1 � 9.\n\n" + "Pour indiquer la valeur d'une case, vous devez tout " + "d'abord s�lectionner le chiffre souhait� dans le panneau de " + "contr�le situ� au-dessus de la grille afin de le rendre actif. " + "Pour cela, vous pouvez utiliser la molette de la souris " + "ou bien effectuer un clic gauche sur l'un des chiffres. " + "Ensuite, il suffit d'un clic gauche dans une case de la grille pour " + "y affecter la valeur active. Un clic droit permet de noter " + "ce chiffre comme une possibilit� pour cette case.\n" + "Pour vous aider dans la r�solution de la grille, vous pouvez �galement " + "mettre en �vidence les cases ayant la m�me valeur gr�ce � un " + "gestionnaire de couleurs. Un clic droit sur un chiffre du " + "panneau de contr�le permet de surligner toute les cases identiques " + "dans la grille.\n" + "Enfin, notez que SudokuJ poss�de des fonctions de mise en pause, " + "de sauvegarde et de chargement de parties. Et si vous vous en " + "sentez le courage, vous pouvez m�me cr�er vos propres grilles " + "dans le mode Cr�ateur !");
            rules.setMaximumSize(new Dimension(460, 300));
            rules.setFont(new Font("Tahoma", Font.PLAIN, 11));
            rules.setOpaque(false);
            rules.setWrapStyleWord(true);
            rules.setLineWrap(true);
            rules.setEditable(false);
            swing_helpPanel.add(rules);
            swing_helpPanel.setVisible(true);
            BoxLayout layout = new BoxLayout(swing_helpPanel, BoxLayout.PAGE_AXIS);
            swing_helpPanel.setLayout(layout);
            swing_helpPanel.add(Box.createRigidArea(new Dimension(0, 110)));
            swing_helpPanel.add(rules);
            swing_helpFrame.add(swing_helpPanel);
            swing_helpFrame.setVisible(true);
            swing_helpFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    /**
	 * Impl�mente la fen�tre d'informations sur SudokuJ
	 * 
	 * @author Romain HUET
	 * @author Nicolas RAYNAUD
	 */
    public class AboutScreen {

        /** Fen�tre "� propos" */
        private JFrame swing_aboutFrame = null;

        /** Panel contenant l'image */
        private JPanel swing_aboutPanel = null;

        public AboutScreen() {
            swing_aboutFrame = new JFrame();
            swing_aboutFrame.setIconImage(swing_var_iconImage);
            swing_aboutFrame.setSize(506, 392);
            swing_aboutFrame.setResizable(false);
            swing_aboutFrame.setTitle("� propos de SudokuJ...");
            swing_aboutPanel = new JPanel() {

                public void paintComponent(Graphics g) {
                    if (swing_var_aboutImage.getWidth(null) > 0) g.drawImage(swing_var_aboutImage, 0, 0, this);
                }
            };
            swing_aboutPanel.setSize(500, 360);
            swing_aboutPanel.setVisible(true);
            Dimension screen = swing_aboutFrame.getToolkit().getScreenSize();
            swing_aboutFrame.setLocation((int) (screen.getWidth() - swing_aboutFrame.getWidth()) / 2, (int) (screen.getHeight() - swing_aboutFrame.getHeight()) / 2);
            JTextArea about = new JTextArea("SudokuJ a �t� cr�� en 2007 dans le cadre du projet " + "CPOO de 4e ann�e Informatique de l'INSA de Rennes. " + "Il est distribu� sous licence GNU GPL 2.0.\n\n" + "Merci � nos professeurs, en particulier �ric A., " + "Pascal G., Mikl�s M. et Solen Q. pour leur apport. " + "Merci �galement � Michel pour son aide lors des tests.\n\n");
            about.setMaximumSize(new Dimension(370, 140));
            about.setFont(new Font("Tahoma", Font.PLAIN, 11));
            about.setOpaque(false);
            about.setWrapStyleWord(true);
            about.setLineWrap(true);
            about.setEditable(false);
            swing_aboutPanel.setLayout(new BoxLayout(swing_aboutPanel, BoxLayout.PAGE_AXIS));
            swing_aboutPanel.add(Box.createRigidArea(new Dimension(0, 250)));
            swing_aboutPanel.add(about);
            swing_aboutFrame.add(swing_aboutPanel);
            swing_aboutFrame.setVisible(true);
            swing_aboutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    /**
	 * Construit le menu "Fichier"
	 */
    private void constructFileMenu() {
        JMenu swing_file = new JMenu("Fichier");
        JMenu swing_newGrid = new JMenu("Nouvelle grille");
        JMenuItem swing_newGridEasy = new JMenuItem("Facile");
        swing_newGridEasy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                generateGrid(Difficulty.EASY);
            }
        });
        JMenuItem swing_newGridMedium = new JMenuItem("Moyen");
        swing_newGridMedium.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                generateGrid(Difficulty.MEDIUM);
            }
        });
        JMenuItem swing_newGridHard = new JMenuItem("Difficile");
        swing_newGridHard.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                generateGrid(Difficulty.HARD);
            }
        });
        JMenuItem swing_newGridLoad = new JMenuItem("Importer une grille...");
        swing_newGridLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        swing_newGridLoad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new LoadCommand(UserData.PLAYER_MODE));
            }
        });
        swing_newGrid.add(swing_newGridEasy);
        swing_newGrid.add(swing_newGridMedium);
        swing_newGrid.add(swing_newGridHard);
        swing_newGrid.addSeparator();
        swing_newGrid.add(swing_newGridLoad);
        swing_file.add(swing_newGrid);
        swing_resetGame = new JMenuItem("Recommencer");
        swing_resetGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new ResetCommand());
            }
        });
        swing_saveGame = new JMenuItem("Sauvegarder la partie");
        swing_saveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        swing_saveGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new SaveCommand(UserData.PLAYER_MODE));
            }
        });
        swing_loadGame = new JMenuItem("Charger la partie");
        swing_loadGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        swing_loadGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new LoadCommand(UserData.PLAYER_MODE));
            }
        });
        swing_file.add(swing_resetGame);
        swing_file.add(swing_saveGame);
        swing_file.add(swing_loadGame);
        swing_file.add(new Separator());
        JMenuItem swing_newGridCreator = new JMenuItem("Nouvelle grille cr�ateur");
        swing_newGridCreator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        swing_newGridCreator.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new NewGridCommand(new Grid(), UserData.CREATOR_MODE));
            }
        });
        swing_publishGrid = new JMenuItem("Publier la grille");
        swing_publishGrid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        swing_publishGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new PublishCommand());
            }
        });
        swing_saveGrid = new JMenuItem("Enregistrer la grille");
        swing_saveGrid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        swing_saveGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new SaveCommand(UserData.CREATOR_MODE));
            }
        });
        JMenuItem swing_loadGrid = new JMenuItem("Charger une grille");
        swing_loadGrid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        swing_loadGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new LoadCommand(UserData.CREATOR_MODE));
            }
        });
        swing_file.add(swing_newGridCreator);
        swing_file.add(swing_publishGrid);
        swing_file.add(swing_saveGrid);
        swing_file.add(swing_loadGrid);
        swing_file.add(new Separator());
        JMenuItem swing_exit = new JMenuItem("Quitter");
        swing_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        swing_exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new ExitCommand());
            }
        });
        swing_file.add(swing_exit);
        swing_menu.add(swing_file);
    }

    /**
	 * Construit le menu "Actions"
	 */
    private void constructActionsMenu() {
        JMenu actions = new JMenu("Actions");
        swing_showSolutionP = new JMenuItem("Afficher la solution");
        swing_showSolutionP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        swing_showSolutionP.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new ShowSolutionCommand());
            }
        });
        actions.add(swing_showSolutionP);
        swing_checkSolution = new JMenuItem("V�rifier la solution");
        swing_checkSolution.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        swing_checkSolution.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new CheckSolutionCommand());
            }
        });
        actions.add(swing_checkSolution);
        actions.add(new Separator());
        swing_findSolution = new JMenuItem("Trouver une solution");
        swing_findSolution.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        swing_findSolution.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new FindSolutionCommand());
            }
        });
        actions.add(swing_findSolution);
        swing_showSolutionC = new JMenuItem("Afficher la solution");
        swing_showSolutionC.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        swing_showSolutionC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new ShowSolutionCommand());
            }
        });
        actions.add(swing_showSolutionC);
        swing_validate = new JMenuItem("Valider la grille");
        swing_validate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        swing_validate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new ValidateGridCommand());
            }
        });
        actions.add(swing_validate);
        actions.add(new Separator());
        swing_playpause = new JMenuItem("Pause");
        swing_playpause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        swing_playpause.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new PlayPauseCommand());
            }
        });
        actions.add(swing_playpause);
        actions.add(new Separator());
        swing_undo = new JMenuItem("Annuler");
        swing_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        swing_undo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new Undo());
            }
        });
        actions.add(swing_undo);
        swing_redo = new JMenuItem("Refaire");
        swing_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        swing_redo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new Redo());
            }
        });
        actions.add(swing_redo);
        swing_menu.add(actions);
    }

    /**
	 * Contruit le menu "Options"
	 */
    public void constructOptionsMenu() {
        JMenu options = new JMenu("Options");
        possibilitiesWithFigures = new JRadioButtonMenuItem("Afficher les possibilit�s avec des chiffres");
        possibilitiesWithFigures.setSelected(true);
        possibilitiesWithFigures.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new SetCirclesCommand(false));
            }
        });
        possibilitiesWithCircles = new JRadioButtonMenuItem("Afficher les possibilit�s avec des disques");
        possibilitiesWithCircles.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new SetCirclesCommand(true));
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(possibilitiesWithFigures);
        group.add(possibilitiesWithCircles);
        options.add(possibilitiesWithFigures);
        options.add(possibilitiesWithCircles);
        options.add(new Separator());
        JMenuItem swing_backgroundImage = new JMenuItem("Choisir l'image d'arri�re-plan...");
        swing_backgroundImage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handle(new SetBackgroundImageCommand());
            }
        });
        options.add(swing_backgroundImage);
        swing_menu.add(options);
    }

    /**
	 * Contruit le menu "Aide"
	 */
    public void constructHelpMenu() {
        JMenu help = new JMenu("?");
        swing_help = new JMenuItem("Rubrique d'aide");
        swing_help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        help.add(swing_help);
        help.add(new Separator());
        swing_about = new JMenuItem("� propos de SudokuJ...");
        help.add(swing_about);
        swing_menu.add(help);
        swing_help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new HelpScreen();
            }
        });
        swing_about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AboutScreen();
            }
        });
    }

    /**
	 * Sp�cifie si le jeu est en pause ou pas
	 * 
	 * @param pause
	 *            Vrai si le jeu est en pause
	 */
    private void setPaused(boolean pause) {
        if (pause) {
            swing_playpause.setText("Reprendre");
        } else {
            swing_playpause.setText("Pause");
        }
    }

    /**
	 * Retourne la barre de menu Swing
	 * 
	 * @return Barre de menu Swing
	 */
    public JMenuBar getJMenuBar() {
        return swing_menu;
    }

    /** {@inheritDoc} */
    public void receiveCommand(Mediator mediator, Command command) {
        if (command instanceof DesactivateUndo) {
            swing_undo.setEnabled(false);
        } else if (command instanceof DesactivateRedo) {
            swing_redo.setEnabled(false);
        } else if (command instanceof UndoableCommand) {
            swing_undo.setEnabled(true);
            swing_redo.setEnabled(true);
        }
        if (command instanceof SetUserDataCommand) {
            SetUserDataCommand c = (SetUserDataCommand) command;
            if (c.getUserData().getMode() == UserData.PLAYER_MODE) {
                swing_saveGame.setEnabled(true);
                swing_resetGame.setEnabled(true);
                swing_checkSolution.setEnabled(true);
                swing_showSolutionP.setEnabled(true);
                swing_playpause.setEnabled(true);
                swing_publishGrid.setEnabled(false);
                swing_saveGrid.setEnabled(false);
                swing_findSolution.setEnabled(false);
                swing_showSolutionC.setEnabled(false);
                swing_validate.setEnabled(false);
            } else {
                swing_saveGame.setEnabled(false);
                swing_resetGame.setEnabled(false);
                swing_checkSolution.setEnabled(false);
                swing_showSolutionP.setEnabled(false);
                swing_playpause.setEnabled(false);
                swing_publishGrid.setEnabled(true);
                swing_saveGrid.setEnabled(true);
                swing_findSolution.setEnabled(true);
                swing_showSolutionC.setEnabled(true);
                swing_validate.setEnabled(true);
            }
            setPaused(c.getUserData().isPaused());
        } else if (command instanceof PlayPauseCommand) {
            setPaused(((PlayPauseCommand) command).isPaused());
        } else if (command instanceof SetCirclesCommand) {
            boolean circles = ((SetCirclesCommand) command).isCircles();
            possibilitiesWithFigures.setSelected(!circles);
            possibilitiesWithCircles.setSelected(circles);
        }
        super.receiveCommand(mediator, command);
    }

    /**
	 * G�n�re une grille
	 * 
	 * @param level
	 *            Niveau de difficult�
	 */
    private void generateGrid(Difficulty level) {
        NewGridCommand c = new NewGridCommand();
        Grid g = new Grid();
        g.generate(level);
        c.setGrid(g);
        c.setMode(UserData.PLAYER_MODE);
        c.setDifficulty(level);
        handle(c);
    }
}
