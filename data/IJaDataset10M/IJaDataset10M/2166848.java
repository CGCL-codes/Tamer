package tetranoid.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tetranoid.field.Orientation;
import tetranoid.field.sprites.Sprite;

public class ScoreBoard extends JPanel {

    public ScoreBoard() {
        setPreferredSize(new Dimension(200, 600));
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(10, 0, 10, 0);
        scoreLabel = new JLabel("SCORE");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 26));
        scoreLabel.setVisible(false);
        add(scoreLabel, c);
        c.weighty = 1;
        c.gridy = 5;
        add(new JLabel(), c);
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
    }

    public void addPlayer(String name, Orientation position) {
        PlayerPane playerPane = new PlayerPane(name, position);
        add(playerPane, c);
        playerPanes.add(playerPane);
        scoreLabel.setVisible(true);
        validateTree();
        c.gridy++;
    }

    public PlayerPane getPlayer(String nick) {
        for (PlayerPane player : playerPanes) {
            if (player.getNick().equals(nick)) return player;
        }
        return null;
    }

    public void reset() {
        setVisible(false);
        for (PlayerPane player : playerPanes) {
            remove(player);
        }
        playerPanes.clear();
        scoreLabel.setVisible(false);
        setVisible(true);
        validate();
        c.gridx = 0;
        c.gridy = 1;
    }

    public class PlayerPane extends JPanel {

        public PlayerPane(String name, Orientation orientation) {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            nick = new JLabel(name);
            nick.setFont(new Font("Arial", Font.BOLD, 24));
            nick.setForeground(orientation.color);
            add(nick, c);
            Sprite sprite = null;
            if (orientation.color == Color.RED) {
                sprite = Sprite.BALL_RED;
            } else if (orientation.color == Color.BLUE) {
                sprite = Sprite.BALL_BLUE;
            } else if (orientation.color == Color.YELLOW) {
                sprite = Sprite.BALL_YELLOW;
            } else if (orientation.color == Color.GREEN) {
                sprite = Sprite.BALL_GREEN;
            }
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            lives = new JPanel(new FlowLayout(FlowLayout.LEFT));
            for (int i = 0; i < MAX_LIVES; i++) {
                liveSymbols[i] = new JLabel(new ImageIcon(sprite.img));
                lives.add(liveSymbols[i]);
            }
            lives.setPreferredSize(new Dimension(90, 24));
            add(lives, c);
            c.gridx = 1;
            c.gridy = 1;
            score = new JLabel("0");
            score.setHorizontalAlignment(JLabel.RIGHT);
            score.setPreferredSize(new Dimension(90, 24));
            score.setFont(new Font("Arial", Font.BOLD, 18));
            add(score, c);
        }

        public String getNick() {
            return nick.getText();
        }

        public int getLives() {
            return liveCount;
        }

        public int getScore() {
            return Integer.parseInt(score.getText());
        }

        public void setScore(int score) {
            this.score.setText(Integer.toString(score));
        }

        public void setLives(int lives) {
            for (int i = 0; i < MAX_LIVES; i++) {
                if (i < lives) {
                    liveSymbols[i].setVisible(true);
                } else {
                    liveSymbols[i].setVisible(false);
                }
            }
            validate();
            this.liveCount = lives;
        }

        public final int MAX_LIVES = 5;

        private JLabel nick;

        private JPanel lives;

        private JLabel[] liveSymbols = new JLabel[MAX_LIVES];

        private int liveCount;

        private JLabel score;
    }

    GridBagConstraints c;

    JLabel scoreLabel;

    List<PlayerPane> playerPanes = new ArrayList<PlayerPane>();
}
