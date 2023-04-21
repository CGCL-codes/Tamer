package mta.connect.four.ui.gui;

import mta.connect.four.game.Command;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * @author Yoav Aharoni
 */
public class ButtonsPanel extends JPanel {

    private static final String[] HELP_MESSAGES = new String[] { "Connect-Four: The Game", "Connect-Four is a game of immense challenge, that will take you to your intellectual limits.", "The game is played in turns by two players, each dropping a single disc in her turn.", "The player can choose which column to drop the disc in - and the discs pile in a grid, above each other.", "The purpose of the game is to create a series of four 'occupied' board positions - ", "in any direction (e.g. vertical, horiztonal or diagonal). The first player who succeeds", "in creating such a series, wins the game.", "In order to play, find a friend and decide who's 'Yellow' and who's 'Red'. Then, each of you", "should choose the column number in which she wants to drop the disc in her turn.", "Don't worry - I'll be sure to tell you when somebody wins. Enjoy!" };

    private GameBoardView gameBoardView;

    public ButtonsPanel(GameBoardView gameBoardView1) {
        this.gameBoardView = gameBoardView1;
        setPreferredSize(new Dimension(300, 70));
        add(new MyButton(new AbstractAction(Command.UNDO.name()) {

            public void actionPerformed(ActionEvent e) {
                gameBoardView.getGameManager().undoLastMove();
            }
        }));
        add(new MyButton(new AbstractAction(Command.REDO.name()) {

            public void actionPerformed(ActionEvent e) {
                gameBoardView.getGameManager().redoLastMove();
            }
        }));
        add(new JButton(new AbstractAction(Command.START.name()) {

            public void actionPerformed(ActionEvent e) {
                gameBoardView.startGame();
            }
        }));
        add(new JButton(new AbstractAction(Command.HELP.name()) {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gameBoardView, HELP_MESSAGES);
            }
        }));
        add(new JButton(new LoadAction()));
        add(new JButton(new SaveAction()));
        add(new JButton(new HighScoreAction()));
    }

    private class MyButton extends JButton {

        private MyButton(Action a) {
            super(a);
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && gameBoardView.isGameActive();
        }
    }

    private class SaveAction extends AbstractAction {

        private SaveAction() {
            super(Command.SAVE.name());
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Connect4 Saved Game", "c4d");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(gameBoardView);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().getAbsolutePath();
                if (!fileName.contains(".")) {
                    fileName += ".c4d";
                }
                try {
                    gameBoardView.getGameManager().saveGame(fileName);
                    JOptionPane.showMessageDialog(getParent(), "Game saved to '" + fileName + "'");
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(getParent(), "Error saving game - game not saved. I guess you must continue playing!");
                }
            }
        }
    }

    private class HighScoreAction extends AbstractAction {

        private HighScoreAction() {
            super(Command.HIGHSCORE.name());
        }

        public void actionPerformed(ActionEvent e) {
            String highScoreMessage = gameBoardView.getGameManager().getHighscore().toString();
            JOptionPane.showMessageDialog(getParent(), highScoreMessage);
        }
    }

    private class LoadAction extends AbstractAction {

        private LoadAction() {
            super(Command.LOAD.name());
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Connect4 Saved Game", "c4d");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(gameBoardView);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().getAbsolutePath();
                boolean againstComputer = gameBoardView.getGameType(false).equals(GameType.OneVsComp);
                try {
                    gameBoardView.getGameManager().loadGame(fileName, againstComputer);
                    JOptionPane.showMessageDialog(getParent(), "Game loaded from '" + fileName + "'.\n" + "Go ahead and play " + gameBoardView.getGameManager().getTurn() + " player.");
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(getParent(), "Error loading game - game not loaded.");
                }
            }
        }
    }
}
