package saf.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import saf.ErrorHandler;
import saf.bot.GameBot;
import saf.bot.EnumTypes.AttackType;
import saf.bot.EnumTypes.MoveType;
import saf.bot.EnumTypes.Orientation;

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;

    private Color skyColor = new Color(135, 206, 250);

    private Color grassColor = new Color(50, 205, 50);

    private final int WINDOW_WIDTH = 1280;

    private final int WINDOW_HEIGHT = 400;

    private final int GRASS_HEIGHT = 100;

    private final int DEAD_ADJUSTMENT = 120;

    private double scaleFactor = 50;

    private int bot1X;

    private int bot1Y = WINDOW_HEIGHT - GRASS_HEIGHT - 200;

    private boolean bot1IsDead;

    private AttackType bot1Attack;

    private MoveType bot1Move;

    private Orientation bot1Orientation;

    private int bot2X;

    private int bot2Y = WINDOW_HEIGHT - GRASS_HEIGHT - 200;

    private boolean bot2IsDead;

    private AttackType bot2Attack;

    private MoveType bot2Move;

    private Orientation bot2Orientation;

    public Window(double gameWidth, GameBot bot1, GameBot bot2) {
        super("SAF game");
        scaleFactor = ((double) WINDOW_WIDTH - 160.) / gameWidth;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setLocation(500, 250);
        this.setBounds(30, 30, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setBackground(skyColor);
        this.setResizable(false);
        setBotStates(bot1, bot2);
        GameScreen canvas = new GameScreen();
        this.getContentPane().add(canvas);
        this.setVisible(true);
    }

    /**
	 * Store the bot states into this instance
	 * @param bot1
	 * @param bot2
	 */
    public void setBotStates(GameBot bot1, GameBot bot2) {
        bot1X = (int) (scaleFactor * bot1.getCurrentPosition());
        if (bot1.isDead()) {
            bot1Y = WINDOW_HEIGHT - GRASS_HEIGHT - 200 + DEAD_ADJUSTMENT;
            bot1IsDead = true;
        } else {
            bot1IsDead = false;
        }
        bot1Attack = bot1.getCurrentAttack();
        bot1Move = bot1.getCurrentMove();
        bot1Orientation = bot2.orientationFromOther(bot1);
        bot2X = (int) (scaleFactor * bot2.getCurrentPosition());
        if (bot2.isDead()) {
            bot2Y = WINDOW_HEIGHT - GRASS_HEIGHT - 200 + DEAD_ADJUSTMENT;
            bot2IsDead = true;
        } else {
            bot2IsDead = false;
        }
        bot2Attack = bot2.getCurrentAttack();
        bot2Move = bot2.getCurrentMove();
        bot2Orientation = bot1.orientationFromOther(bot2);
    }

    public void updateScreen() {
        getContentPane().getComponent(0).repaint();
    }

    private class GameScreen extends JComponent {

        private static final long serialVersionUID = 2;

        /**
		 * Paint both the background and the fighters with current states
		 */
        public void paint(Graphics g) {
            g.setColor(grassColor);
            g.fillRect(0, WINDOW_HEIGHT - GRASS_HEIGHT, WINDOW_WIDTH, GRASS_HEIGHT);
            Image bot1 = loadFighterImage(bot1IsDead, bot1Attack, bot1Move, bot1Orientation);
            g.drawImage(bot1, bot1X, bot1Y, bot1.getWidth(this), bot1.getHeight(this), this);
            Image bot2 = loadFighterImage(bot2IsDead, bot2Attack, bot2Move, bot2Orientation);
            g.drawImage(bot2, bot2X, bot2Y, bot2.getWidth(this), bot2.getHeight(this), this);
        }

        private Image loadFighterImage(boolean isDead, AttackType attackType, MoveType moveType, Orientation orientation) {
            String filePath = "src/saf/figures/";
            if (isDead) {
                filePath += "dead";
            } else if (moveType == MoveType.JUMP) {
                filePath += "jump/";
                switch(attackType) {
                    case PUNCH_HIGH:
                    case PUNCH_LOW:
                        filePath += "punch";
                        break;
                    case KICK_HIGH:
                    case KICK_LOW:
                        filePath += "kick";
                        break;
                    case BLOCK_HIGH:
                    case BLOCK_LOW:
                        filePath += "block";
                        break;
                    default:
                        filePath += "idle";
                        break;
                }
            } else if (moveType == MoveType.CROUCH) {
                filePath += "crouch/";
                switch(attackType) {
                    case PUNCH_HIGH:
                    case PUNCH_LOW:
                        filePath += "punch";
                        break;
                    case KICK_HIGH:
                    case KICK_LOW:
                        filePath += "kick";
                        break;
                    case BLOCK_HIGH:
                    case BLOCK_LOW:
                        filePath += "block";
                        break;
                    default:
                        filePath += "idle";
                        break;
                }
            } else {
                filePath += "stand/";
                if (attackType == AttackType.IDLE) {
                    filePath += "idle";
                } else if (attackType == AttackType.PUNCH_LOW) {
                    filePath += "punch_low";
                } else if (attackType == AttackType.PUNCH_HIGH) {
                    filePath += "punch_high";
                } else if (attackType == AttackType.KICK_LOW) {
                    filePath += "kick_low";
                } else if (attackType == AttackType.KICK_HIGH) {
                    filePath += "kick_high";
                } else if (attackType == AttackType.BLOCK_LOW) {
                    filePath += "block_low";
                } else if (attackType == AttackType.BLOCK_HIGH) {
                    filePath += "block_high";
                } else {
                    filePath += "idle";
                }
            }
            if (orientation == Orientation.RIGHT) {
                filePath += "_right.png";
            } else {
                filePath += "_left.png";
            }
            File sourceFile = new File(filePath);
            try {
                return ImageIO.read(sourceFile);
            } catch (IOException e) {
                ErrorHandler.exitWithString("Can't read file " + sourceFile + "!");
                return null;
            }
        }
    }
}
