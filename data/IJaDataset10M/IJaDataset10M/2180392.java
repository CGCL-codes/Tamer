package client.view.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import client.model.table.Card;
import client.view.player.CardPanel;
import client.view.player.PlayerPanel;

public class TablePanel extends JPanel {

    private static final long serialVersionUID = 4939381612775239038L;

    private ImageIcon table;

    private CardPanel communityCards;

    private PlayerPanel dealer;

    private PlayerPanel currentPlayer;

    private JLabel potSize;

    private JLabel blindsLabel;

    private Map<String, PlayerPanel> playerPanels;

    private Map<Integer, JPanel> playerBoxes;

    private Map<Integer, Integer> positionsToSeats;

    private static final int PLAYER_X = 345;

    private static final int PLAYER_Y = 360;

    private static final int OPP_1X = 95;

    private static final int OPP_1Y = 390;

    private static final int OPP_2X = 10;

    private static final int OPP_2Y = 250;

    private static final int OPP_3X = 40;

    private static final int OPP_3Y = 105;

    private static final int OPP_4X = 180;

    private static final int OPP_4Y = 15;

    private static final int OPP_5X = 345;

    private static final int OPP_5Y = 2;

    private static final int OPP_6X = 510;

    private static final int OPP_6Y = 15;

    private static final int OPP_7X = 650;

    private static final int OPP_7Y = 105;

    private static final int OPP_8X = 680;

    private static final int OPP_8Y = 250;

    private static final int OPP_9X = 595;

    private static final int OPP_9Y = 390;

    private static final int PLAYER_WIDTH = 110;

    private static final int PLAYER_HEIGHT = 135;

    private static final int COMMUNITY_CARD_X = 0;

    private static final int COMMUNITY_CARD_Y = 250;

    private static final int COMMUNITY_CARD_WIDTH = 42;

    private static final int COMMUNITY_CARD_HEIGHT = 65;

    private static final int POTSIZE_LABEL_X = 0;

    private static final int POTSIZE_LABEL_Y = 325;

    private static final int BLINDS_LABEL_X = 5;

    private static final int BLINDS_LABEL_Y = 0;

    public TablePanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(0, 0, 0, 0));
        setTableImage();
        dealer = null;
        currentPlayer = null;
        playerPanels = new HashMap<String, PlayerPanel>(10);
        playerBoxes = new HashMap<Integer, JPanel>(10);
        positionsToSeats = new HashMap<Integer, Integer>(10);
        createPlayerBoxes();
        communityCards = new CardPanel();
        communityCards.setBounds(COMMUNITY_CARD_X, COMMUNITY_CARD_Y, width, 70);
        potSize = new JLabel("$0", SwingConstants.CENTER);
        potSize.setForeground(Color.WHITE);
        potSize.setFont(potSize.getFont().deriveFont(15.0f));
        potSize.setFont(potSize.getFont().deriveFont(Font.BOLD));
        potSize.setBounds(POTSIZE_LABEL_X, POTSIZE_LABEL_Y, width, 20);
        potSize.setVisible(false);
        blindsLabel = new JLabel();
        blindsLabel.setForeground(Color.WHITE);
        blindsLabel.setFont(blindsLabel.getFont().deriveFont(10.0f));
        blindsLabel.setFont(blindsLabel.getFont().deriveFont(Font.BOLD));
        blindsLabel.setBounds(BLINDS_LABEL_X, BLINDS_LABEL_Y, 100, 20);
        this.add(communityCards);
        this.add(potSize);
        this.add(blindsLabel);
    }

    public void displayDealerToken(String playerName) {
        if (dealer != null) {
            dealer.removeDealerToken();
        }
        dealer = playerPanels.get(playerName);
        dealer.drawDealerToken();
    }

    public void addCommunityCards(List<Card> cards) {
        communityCards.removeCards();
        for (Card card : cards) {
            communityCards.addCard(card, COMMUNITY_CARD_WIDTH, COMMUNITY_CARD_HEIGHT);
        }
    }

    public void addPlayer(final String name, final int position) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    if (name != null && (position >= 0 && position < 10)) {
                        PlayerPanel playerPanel = new PlayerPanel(name, PLAYER_WIDTH, PLAYER_HEIGHT);
                        JPanel playerBox = playerBoxes.get(positionsToSeats.get(position));
                        playerPanels.put(name, playerPanel);
                        playerBox.removeAll();
                        playerBox.add(playerPanel);
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void clearPlayers() {
        playerPanels.clear();
        for (JPanel playerBox : playerBoxes.values()) {
            playerBox.removeAll();
        }
        this.revalidate();
        this.repaint();
    }

    public void addPlayerCards(String playerName, List<Card> cards) {
        PlayerPanel player = getPlayerPanel(playerName);
        player.removeCards();
        for (Card card : cards) {
            player.addCard(card);
        }
    }

    private void createPlayerBoxes() {
        Color noColour = new Color(0, 0, 0, 0);
        JPanel panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(0, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_1X, OPP_1Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(1, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_2X, OPP_2Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(2, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_3X, OPP_3Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(3, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_4X, OPP_4Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(4, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_5X, OPP_5Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(5, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_6X, OPP_6Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(6, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_7X, OPP_7Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(7, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_8X, OPP_8Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(8, panel);
        this.add(panel);
        panel = new JPanel();
        panel.setBackground(noColour);
        panel.setBounds(OPP_9X, OPP_9Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBoxes.put(9, panel);
        this.add(panel);
    }

    public void updateMyPosition(int myPosition) {
        System.out.println("SPREE Client: my position = " + myPosition);
        positionsToSeats.put(myPosition, 0);
        positionsToSeats.put((myPosition + 1) % 10, 1);
        positionsToSeats.put((myPosition + 2) % 10, 2);
        positionsToSeats.put((myPosition + 3) % 10, 3);
        positionsToSeats.put((myPosition + 4) % 10, 4);
        positionsToSeats.put((myPosition + 5) % 10, 5);
        positionsToSeats.put((myPosition + 6) % 10, 6);
        positionsToSeats.put((myPosition + 7) % 10, 7);
        positionsToSeats.put((myPosition + 8) % 10, 8);
        positionsToSeats.put((myPosition + 9) % 10, 9);
    }

    public void updatePlayerStack(String playerName, int stack) {
        getPlayerPanel(playerName).updateStack(stack);
    }

    public void updatePlayerAction(String playerName, String lastAction) {
        getPlayerPanel(playerName).updateAction(lastAction);
    }

    public void displayCurrentPlayer(final String playerName) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (currentPlayer != null) {
                    currentPlayer.setCurrentPlayer(false);
                }
                currentPlayer = playerPanels.get(playerName);
                currentPlayer.setCurrentPlayer(true);
            }
        });
    }

    public void updatePot(int amount) {
        if (amount != 0) {
            potSize.setVisible(true);
            potSize.setText("$" + amount);
        } else {
            potSize.setVisible(false);
        }
    }

    public void setBlinds(String blinds) {
        blindsLabel.setText("Blinds: $" + blinds);
    }

    private PlayerPanel getPlayerPanel(String playerName) {
        return playerPanels.get(playerName);
    }

    private void setTableImage() {
        URL imageURL = getClass().getResource("/client/view/resources/pokerTable.png");
        if (imageURL != null) {
            table = new ImageIcon(imageURL);
        } else {
            System.out.println("Couldn't find background image");
        }
    }

    protected void paintComponent(Graphics g) {
        if (table != null) {
            g.drawImage(table.getImage(), 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }

    public void clear() {
        communityCards.removeCards();
        dealer = null;
        potSize.setText("");
        potSize.setVisible(false);
        blindsLabel.setText("");
        potSize.setVisible(false);
        positionsToSeats.clear();
        clearPlayers();
    }
}
