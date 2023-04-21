package clientside;

import gamelogic.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * tavolo di gioco - grafica
 * @author nicola
 */
public class TableCanvas extends JPanel {

    private LocalPlayerStatus currentStatus;

    private int[] tournamentScores;

    private String[] playerNames;

    private int gameType;

    private boolean tableCleared;

    private String cardsType = "trevisane";

    boolean newMsgsPresent;

    private int msgsNotificationCountDown;

    private Client client;

    private Image background;

    private Image[] cards;

    private Image back;

    private Image backR;

    private static final int IMAGE_WIDTH = 75;

    private static final int IMAGE_HEIGTH = 150;

    public TableCanvas(int gameType, Client client) {
        this.gameType = gameType;
        currentStatus = null;
        tournamentScores = null;
        cards = new Image[40];
        for (int i = 0; i < cards.length; i++) cards[i] = null;
        tableCleared = true;
        this.client = client;
        back = null;
        backR = null;
        newMsgsPresent = false;
        msgsNotificationCountDown = 0;
    }

    /** setta nomi giocatori */
    public void setPlayerNames(String[] names) {
        playerNames = names;
    }

    /** nuovo stato locale la funzione � chiamata da Client */
    public void updateStatus(LocalPlayerStatus status) {
        currentStatus = status;
        if (status.table[status.table.length - 1] != null) tableCleared = false;
        repaint();
    }

    /** metodo paint - si occupa di tutta la gestione della grafica; 
     * NOTA - ogni volta che fa vedere un immagine
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        printTournamentScores(g2d);
        if (currentStatus == null) return;
        int turn = drawTurnRect(g2d);
        drawCardBacks(turn, g2d);
        drawDeck(g2d);
        drawTable(g2d);
        drawHand(g2d);
        drawPlayerNames(g2d);
        drawStatusMsgsNotification(g2d);
    }

    private void drawHand(final Graphics2D g2d) throws HeadlessException {
        for (int i = 0; i < currentStatus.hand.length; i++) {
            if (currentStatus.hand[i] != null) {
                Card c = currentStatus.hand[i];
                if (cards[c.getIndex()] == null) try {
                    ClassLoader cldr = this.getClass().getClassLoader();
                    URL cardUrl = cldr.getResource("clientside/images/" + cardsType + "/" + c.toFileString());
                    cards[c.getIndex()] = ImageIO.read(cardUrl);
                } catch (IOException ex) {
                    System.out.println("Errore nella" + "lettura del file " + c.toFileString());
                    ex.printStackTrace();
                }
                g2d.drawImage(cards[c.getIndex()], 275 + 25 * 7 / 2 * i, 400, IMAGE_WIDTH, IMAGE_HEIGTH, null);
            }
        }
    }

    private void drawTable(final Graphics2D g2d) throws HeadlessException {
        if (currentStatus.table[currentStatus.table.length - 1] == null || (currentStatus.table[currentStatus.table.length - 1] != null && !tableCleared)) {
            if (!tableCleared) tableCleared = true;
            for (int i = 0; i < currentStatus.table.length; i++) {
                Card c = currentStatus.table[i];
                if (c == null) break;
                if (cards[c.getIndex()] == null) try {
                    ClassLoader cldr = this.getClass().getClassLoader();
                    URL cardUrl = cldr.getResource("clientside/images/" + cardsType + "/" + c.toFileString());
                    cards[c.getIndex()] = ImageIO.read(cardUrl);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Errore nella" + "lettura del file " + c.toFileString());
                    ex.printStackTrace();
                }
                g2d.drawImage(cards[c.getIndex()], 275 + 25 * 5 / 2 * i, 212, IMAGE_WIDTH, IMAGE_HEIGTH, null);
            }
        }
    }

    private void drawDeck(final Graphics2D g2d) throws HeadlessException {
        Card briscola = currentStatus.briscola;
        if (cards[briscola.getIndex()] == null) try {
            ClassLoader cldr = this.getClass().getClassLoader();
            URL cardUrl = cldr.getResource("clientside/images/" + cardsType + "/" + briscola.toFileString());
            cards[briscola.getIndex()] = ImageIO.read(cardUrl);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nella" + "lettura del file");
            ex.printStackTrace();
        }
        boolean drawDeck = true;
        if (gameType == Tournament.TYPE_BRISCOLA2) {
            if (client.getHand() > 16) drawDeck = false;
        } else if (gameType == Tournament.TYPE_BRISCOLA3) {
            if (client.getHand() > 9) drawDeck = false;
        } else if (gameType == Tournament.TYPE_BRISCOLA4) {
            if (client.getHand() > 6) drawDeck = false;
        }
        if (drawDeck) {
            g2d.drawImage(cards[briscola.getIndex()], 662, 400, 75, 150, null);
            int deckSize = 0;
            if (gameType == Tournament.TYPE_BRISCOLA2) deckSize = 34 - client.getHand() * 2; else if (gameType == Tournament.TYPE_BRISCOLA3) deckSize = 30 - client.getHand() * 3; else if (gameType == Tournament.TYPE_BRISCOLA4) deckSize = 28 - client.getHand() * 4;
            for (int i = 0; i < deckSize; i += 8) {
                g2d.drawImage(backR, 625 + i / 4, 475 + i / 4, 150, 75, null);
            }
        }
    }

    private void drawStatusMsgsNotification(final Graphics2D g2d) {
        if (newMsgsPresent) {
            msgsNotificationCountDown--;
            g2d.setColor(new Color(255, 0, 0));
            g2d.drawString("controllare la", 25, 500);
            g2d.drawString("finestra di stato", 25, 520);
            g2d.setColor(new Color(0, 0, 0));
        }
        if (msgsNotificationCountDown < 1) newMsgsPresent = false;
    }

    private void drawCardBacks(final int turn, final Graphics2D g2d) throws HeadlessException {
        if (back == null) {
            try {
                ClassLoader cldr = this.getClass().getClassLoader();
                URL backUrl = cldr.getResource("clientside/images/" + cardsType + "/" + "Dorso.png");
                back = ImageIO.read(backUrl);
                URL backRUrl = cldr.getResource("clientside/images/" + cardsType + "/" + "DorsoR.png");
                backR = ImageIO.read(backRUrl);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Errore nella" + "lettura del file Dorso.png");
                ex.printStackTrace();
            }
        }
        g2d.setColor(new Color(255, 0, 0));
        if (gameType == Tournament.TYPE_BRISCOLA2) {
            if (client.getHand() == 16) g2d.drawString("ULTIMA PESCATA", 25, 25);
            int opponentCards = currentStatus.hands[(client.getId() + 1) % 2];
            for (int i = 0; i < opponentCards; i++) g2d.drawImage(back, 275 + 87 * i, 25, 75, 150, null);
        } else if (gameType == Tournament.TYPE_BRISCOLA3) {
            int[] opponentCards = new int[2];
            for (int i = 0; i < opponentCards.length; i++) opponentCards[i] = currentStatus.hands[(client.getId() + 1 + i) % 3];
            for (int i = 0; i < opponentCards[0]; i++) g2d.drawImage(backR, 625, 87 + 87 * i, 150, 75, null);
            for (int i = 0; i < opponentCards[1]; i++) g2d.drawImage(back, 275 + 87 * i, 25, 75, 150, null);
        } else if (gameType == Tournament.TYPE_BRISCOLA4) {
            int[] opponentCards = new int[3];
            for (int i = 0; i < opponentCards.length; i++) opponentCards[i] = currentStatus.hands[(client.getId() + 1 + i) % 4];
            for (int i = 0; i < opponentCards[0]; i++) g2d.drawImage(backR, 625, 87 + 87 * i, 150, 75, null);
            for (int i = 0; i < opponentCards[1]; i++) g2d.drawImage(back, 275 + 87 * i, 25, 75, 150, null);
            for (int i = 0; i < opponentCards[2]; i++) g2d.drawImage(backR, 25, 87 + 87 * i, 150, 75, null);
        }
    }

    private int drawTurnRect(final Graphics2D g2d) {
        int turn = currentStatus.turn;
        g2d.setColor(new Color(255, 0, 0));
        if (turn == 0) g2d.drawRect(275 - 5, 400 - 5, 260, 160);
        if (gameType == Tournament.TYPE_BRISCOLA2) {
            if (turn == 1) g2d.drawRect(275 - 5, 25 - 5, 260, 160);
        } else if (gameType == Tournament.TYPE_BRISCOLA3) {
            if (turn == 1) g2d.drawRect(275 - 5, 25 - 5, 260, 160);
            if (turn == 2) g2d.drawRect(625 - 5, 87 - 5, 160, 260);
        } else if (gameType == Tournament.TYPE_BRISCOLA4) {
            if (turn == 3) g2d.drawRect(625 - 5, 87 - 5, 160, 260);
            if (turn == 2) g2d.drawRect(275 - 5, 25 - 5, 260, 160);
            if (turn == 1) g2d.drawRect(25 - 5, 87 - 5, 160, 260);
        }
        return turn;
    }

    private void drawBackground(final Graphics2D g2d) {
        if (background == null) {
            try {
                ClassLoader cldr = this.getClass().getClassLoader();
                URL bgUrl = cldr.getResource("clientside/images/background.jpg");
                background = ImageIO.read(bgUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        g2d.drawImage(background, 0, 0, 800, 600, new Color(0, 0, 0), null);
    }

    private void printTournamentScores(final Graphics2D g2d) {
        g2d.drawString("Situazione del torneo:", 25, 400);
        int h = 400;
        if (gameType == Tournament.TYPE_BRISCOLA2 || gameType == Tournament.TYPE_BRISCOLA3 || gameType == Tournament.TYPE_BRISCOLA5) {
            for (int i = 0; i < playerNames.length; i++) {
                h += 25;
                g2d.drawString("  " + playerNames[i] + ": " + tournamentScores[i], 25, h);
            }
        } else if (gameType == Tournament.TYPE_BRISCOLA4) {
            h += 25;
            g2d.drawString("  Squadra 1 - " + playerNames[0] + " e " + playerNames[2] + ": " + tournamentScores[0], 25, h);
            h += 25;
            g2d.drawString("  Squadra 2 - " + playerNames[1] + " e " + playerNames[3] + ": " + tournamentScores[1], 25, h);
        }
    }

    /** nuovo punteggio del torneo */
    public void updateTournamentScores(int[] scores) {
        tournamentScores = scores;
        repaint();
    }

    /** il metodo è chiamato dal listener */
    public void mouseClick(MouseEvent e) {
        if (currentStatus == null) return;
        repaint();
        if (currentStatus.turn != 0) return;
        Point p = e.getPoint();
        if (p.y >= 400 && p.y <= 400 + IMAGE_HEIGTH) {
            if (p.x >= 275 && p.x <= 275 + IMAGE_WIDTH) client.playCard(currentStatus.hand[0]); else if (p.x >= 362 && p.x <= 362 + IMAGE_WIDTH) client.playCard(currentStatus.hand[1]); else if (p.x >= 450 && p.x <= 450 + IMAGE_WIDTH) client.playCard(currentStatus.hand[2]);
        }
    }

    private void drawPlayerNames(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0));
        if (currentStatus.turn == 0) g2d.setColor(new Color(255, 0, 0));
        g2d.drawString(playerNames[client.getId()], 275, 565);
        g2d.setColor(new Color(0, 0, 0));
        if (gameType == Tournament.TYPE_BRISCOLA2) {
            if (currentStatus.turn == 1) g2d.setColor(new Color(255, 0, 0));
            g2d.drawString(playerNames[(client.getId() + 1) % 2], 275, 190);
        } else if (gameType == Tournament.TYPE_BRISCOLA3) {
            if (currentStatus.turn == 2) g2d.setColor(new Color(255, 0, 0));
            g2d.drawString(playerNames[(client.getId() + 1) % 3], 625, 352);
            g2d.setColor(new Color(0, 0, 0));
            if (currentStatus.turn == 1) g2d.setColor(new Color(255, 0, 0));
            g2d.drawString(playerNames[(client.getId() + 2) % 3], 275, 190);
        } else if (gameType == Tournament.TYPE_BRISCOLA4) {
            if (currentStatus.turn == 3) g2d.setColor(new Color(255, 0, 0));
            g2d.drawString(playerNames[(client.getId() + 1) % 4], 625, 352);
            g2d.setColor(new Color(0, 0, 0));
            if (currentStatus.turn == 2) g2d.setColor(new Color(255, 0, 0));
            g2d.drawString(playerNames[(client.getId() + 2) % 4], 275, 190);
            g2d.setColor(new Color(0, 0, 0));
            if (currentStatus.turn == 1) g2d.setColor(new Color(255, 0, 0));
            g2d.drawString(playerNames[(client.getId() + 3) % 4], 25, 352);
        }
        g2d.setColor(new Color(0, 0, 0));
    }

    /** usato dal client per stampare un pallino rosso quando arrivano nuovi
     * messaggi di stato */
    public void notifyNewStatusMsgsArePresent() {
        newMsgsPresent = true;
        msgsNotificationCountDown = 2;
    }
}
