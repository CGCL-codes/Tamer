package net.sourceforge.ipp;

import java.awt.*;
import java.util.Hashtable;

public class PlayerDisplay extends Panel {

    private static final long serialVersionUID = 6589121667735498734L;

    private transient Player player;

    private Font normalFont;

    private Font smallFont;

    private Label nameLabel;

    private Label cashLabel;

    private Panel cardPanel;

    private Label msgLabel;

    private static int fontsize = 12;

    private Hashtable<String, CardDisplay> displays = new Hashtable<String, CardDisplay>();

    public static void initialize(String size) {
        if (size.equalsIgnoreCase("small")) {
            fontsize = 9;
        }
    }

    public PlayerDisplay(Player p, int maxcards) {
        player = p;
        normalFont = new Font("SansSerif", Font.PLAIN, fontsize);
        smallFont = new Font("SansSerif", Font.PLAIN, fontsize - 2);
        setFont(normalFont);
        int width = CardDisplay.CARD_WIDTH * (1 + (maxcards - 1) / 2);
        int height = fontsize + 2;
        setSize(width, 2 * fontsize + CardDisplay.CARD_HEIGHT + 4);
        setLayout(null);
        nameLabel = new Label(player.getName(), Label.LEFT);
        add(nameLabel);
        nameLabel.setBounds(0, 0, width, height);
        cashLabel = new Label("$" + player.getCash(), Label.LEFT);
        add(cashLabel);
        cashLabel.setBounds(0, fontsize + 2, width, height);
        msgLabel = new Label("", Label.CENTER);
        add(msgLabel);
        msgLabel.setBounds(5, 2 * height + CardDisplay.CARD_HEIGHT / 2 - height / 2, width - 10, height);
        msgLabel.setForeground(Color.black);
        msgLabel.setBackground(new Color(255, 255, 144));
        msgLabel.setVisible(false);
        cardPanel = new Panel(new CardPanelLayout());
        add(cardPanel);
        cardPanel.setBounds(0, 2 * height, width, CardDisplay.CARD_HEIGHT + 4);
    }

    public void addCard(Card c) {
        CardDisplay disp = new CardDisplay(c);
        cardPanel.add(disp);
        displays.put(c.toString(), disp);
        cardPanel.validate();
    }

    public void removeCard(Card c) {
        CardDisplay disp = (CardDisplay) displays.get(c.toString());
        if (disp != null) {
            cardPanel.remove(disp);
            cardPanel.validate();
        } else {
            System.err.println("yow! bogus card to remove: " + c);
        }
    }

    public void removeAllCards() {
        cardPanel.removeAll();
        cardPanel.validate();
    }

    public void setCash(int amt) {
        cashLabel.setText("$" + amt);
    }

    public void setLastMsg(String msg) {
        if (msg == null || msg.equals("")) {
            msgLabel.setVisible(false);
        } else {
            if (msg.length() > 10) {
                msgLabel.setFont(smallFont);
            } else {
                msgLabel.setFont(normalFont);
            }
            msgLabel.setText(msg);
            msgLabel.setVisible(true);
        }
    }

    public void setFolded(boolean b) {
        if (b) {
            cardPanel.removeAll();
        }
    }
}

class CardPanelLayout implements LayoutManager {

    private int minWidth = 0;

    private int minHeight = 0;

    private int preferredWidth = 0;

    private int preferredHeight = 0;

    private int maxComponentWidth = 0;

    private int maxComponentHeight = 0;

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        setSizes(parent);
        Insets insets = parent.getInsets();
        dim.width = preferredWidth + insets.left + insets.right;
        dim.height = preferredHeight + insets.top + insets.bottom;
        return dim;
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        setSizes(parent);
        Insets insets = parent.getInsets();
        dim.width = minWidth + insets.left + insets.right;
        dim.height = minHeight + insets.top + insets.bottom;
        return dim;
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int n = parent.getComponentCount();
        for (int i = 0; i < n; i++) {
            Component c = parent.getComponent(i);
            if (c.isVisible()) {
                int x = insets.left + i * (CardDisplay.CARD_WIDTH / 2);
                int y = insets.top;
                int w = (i == n - 1) ? CardDisplay.CARD_WIDTH : (CardDisplay.CARD_WIDTH / 2);
                int h = CardDisplay.CARD_HEIGHT;
                c.setBounds(x, y, w, h);
            }
        }
    }

    public void setSizes(Container parent) {
        for (Component c : parent.getComponents()) {
            if (c.isVisible()) {
                Dimension d = c.getPreferredSize();
                maxComponentWidth = Math.max(maxComponentWidth, d.width);
                maxComponentHeight = Math.max(maxComponentHeight, d.height);
            }
        }
        minWidth = preferredWidth = maxComponentWidth;
        minHeight = preferredHeight = maxComponentHeight;
    }
}
