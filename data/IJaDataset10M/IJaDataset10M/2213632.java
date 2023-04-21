package de.boardgamesonline.bgo2.alhambra.view2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JComponent;
import de.boardgamesonline.bgo2.alhambra.controller.GameController;
import de.boardgamesonline.bgo2.alhambra.model.Player;

/**
 * The class shows a screen which will become visible at the end of the game.
 * In the screen the winner and the final scores will be announced and you
 * can restart the game.
 * @author Meike
 *
 */
public class EndOfGameWindow extends JComponent {

    /**
	 * Serial number
	 */
    private static final long serialVersionUID = -1995427875875642785L;

    /**
	 * Variable for the gameGui
	 */
    private GameGui gameGui;

    /**
	 * the list of players to show
	 */
    private List<Player> players;

    /**
	 * width of the screen.
	 */
    private int width;

    /**
	 * ?????
	 */
    private int len;

    /**
	 * Constructor. Constructs the layout
	 * @param gameGui gameGui 
	 * @param gameController the gameController of the game
	 * @param width width of the window
	 * @param height height of the window
	 * @param imageLoader reference to the imageLoader
	 */
    public EndOfGameWindow(GameGui gameGui, GameController gameController, int width, int height, ImageLoader imageLoader) {
        this.gameGui = gameGui;
        players = gameController.ranking();
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.addMouseListener(new StartclickHandler());
        this.width = width;
    }

    /**
	 * Paints the layout
	 *@param g brush of the method
	 */
    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, Color.black, 500, 350, new Color(136, 120, 66), true);
        graphics.setPaint(gp);
        Rectangle bounds = graphics.getClipBounds();
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        graphics.setColor(new Color(167, 159, 53));
        graphics.setFont(new Font("Serif", Font.PLAIN, 45));
        FontMetrics fm = graphics.getFontMetrics();
        String str = "Ende des Spiels";
        int slen = fm.stringWidth(str);
        graphics.drawString(str, (width - slen) / 2, 70);
        graphics.setFont(new Font("Serif", Font.PLAIN, 36));
        FontMetrics fm2 = graphics.getFontMetrics();
        for (int i = 0; i < players.size(); i++) {
            graphics.drawString(players.get(i).getName(), width / 3, 130 + i * 40);
            int length = fm.stringWidth(String.valueOf(players.get(i).getScore()));
            graphics.drawString(String.valueOf(players.get(i).getScore()), (2 * width / 3) - length, 130 + i * 40);
        }
        String str2 = "Spiel neu starten";
        int slen2 = fm2.stringWidth(str2);
        len = slen2;
        graphics.setColor(new Color(167, 159, 24));
        graphics.fill3DRect(495 - slen2 / 2, 600, slen2 + 10, 50, true);
        graphics.setColor(Color.BLACK);
        graphics.drawString(str2, 500 - (slen2 / 2), 637);
    }

    /**
	 * This class handles the mouseclick of the start button.
	 * @author Meike
	 *
	 */
    private class StartclickHandler extends MouseAdapter {

        /**
		 * Action Listener for the start button
		 * @param mouseEvent mouseEvent
		 */
        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getX() >= 495 - len / 2 && mouseEvent.getX() < 505 + len && mouseEvent.getY() >= 600 && mouseEvent.getY() < 650) {
                gameGui.restartGame();
            }
        }
    }
}
