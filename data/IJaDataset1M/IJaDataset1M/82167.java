package net.sourceforge.liftoff.installer.awt;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;
import net.sourceforge.liftoff.installer.*;

/**
 * This is an toplevel window for the installer.
 * <p>
 * This window contains an image at the left side, a button panel
 * at the bottom side and a panel with an Card-Layout in the center.
 * <p>
 * This Class manages the switching between the installer cards.
 *
 *
 * @author Andreas Hofmeister
 * @version $Revision: 1.2 $
 * 
 */
public class InstallerFrame extends Frame implements ActionListener, WindowListener {

    private Button nextButton;

    private Button prevButton;

    private Panel buttonPanel;

    private Panel contentPanel;

    private Label e1;

    private Label e2;

    private Vector cards = new Vector();

    private Hashtable cardNames = new Hashtable();

    private InstallerCard currentCard = null;

    private CardLayout cardLayout;

    private boolean nextEnabled = true;

    private ImageCanvas image;

    public InstallerFrame() {
        super("install");
        setSize(640, 420);
        setResizable(true);
        setBackground(Color.lightGray);
        Info.getFont().getName();
        setFont(Info.getFont());
        cardLayout = new CardLayout();
        contentPanel = new Panel();
        contentPanel.setLayout(cardLayout);
        add(contentPanel, "Center");
        image = new ImageCanvas(Info.installerProps.getProperty("image.side", "net/sourceforge/liftoff/installer/images/SideImage.gif"));
        add(image, "West");
        buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        prevButton = new Button("<<< Zur�ck");
        prevButton.setActionCommand("prev");
        prevButton.addActionListener(this);
        buttonPanel.add(prevButton);
        nextButton = new Button("Weiter >>>");
        nextButton.setActionCommand("next");
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);
        add(buttonPanel, "South");
        addWindowListener(this);
    }

    /**
     * This method will be called to re-initialize the GUI when the 
     * language was changed.
     *
     * @param loc new locale to use.
     */
    public void setLanguage(Locale loc) {
        Info.setLanguage(loc);
        Enumeration en = cards.elements();
        while (en.hasMoreElements()) {
            ((InstallerCard) en.nextElement()).setupGUI();
        }
        String text = currentCard.getNextCardLabel();
        nextButton.setLabel(text);
        text = currentCard.getPrevCardLabel();
        prevButton.setLabel(text);
        validate();
    }

    /**
     * This can be called from an installer card to enable or disable 
     * the "next" button.
     *
     * @param flag true to enable the next button, false to disable it.
     */
    public void setNextEnabled(boolean flag) {
        nextButton.setEnabled(flag);
        nextEnabled = flag;
    }

    /**
     * This can be called from an installer card to enable or disable 
     * the "prev" button.
     *
     * @param flag true to enable the next button, false to disable it.
     */
    public void setPrevEnabled(boolean flag) {
        prevButton.setEnabled(flag);
    }

    /**
     * add a card to the frame. The new card will be added as the last card.
     *
     */
    public void addCard(InstallerCard card) {
        cards.addElement(card);
        cardNames.put(card.getCardName(), card);
        contentPanel.add(card, card.getCardName());
    }

    /**
     * Show the first installer and make this frame visible.
     */
    public void showCards() {
        if (cards.size() == 0) {
            System.err.println("no cards to show !");
            return;
        }
        showCard((InstallerCard) cards.elementAt(0));
        pack();
        setVisible(true);
    }

    /**
     * Show a given installer card.
     */
    private void showCard(InstallerCard card) {
        currentCard = card;
        cardLayout.show(contentPanel, currentCard.getCardName());
        String text = currentCard.getNextCardLabel();
        if (text != null) {
            nextButton.setLabel(text);
            nextButton.setVisible(true);
            nextButton.setEnabled(true);
        } else {
            nextButton.setVisible(false);
            nextButton.setEnabled(false);
        }
        text = currentCard.getPrevCardLabel();
        if (text != null) {
            prevButton.setLabel(text);
            prevButton.setVisible(true);
            prevButton.setEnabled(true);
        } else {
            prevButton.setVisible(false);
            prevButton.setEnabled(false);
        }
    }

    /**
     * display the next card.
     *
     * This method will be called from the "next" button, but could also be
     * called from an installer card.
     */
    public void nextCard() {
        if (currentCard == null) return;
        String nextName = currentCard.getNextCardName();
        InstallerCard nextCard = null;
        if ((nextName != null) && (!("".equals(nextName)))) {
            if (nextName.equals("exit")) {
                System.exit(0);
            }
            nextCard = (InstallerCard) cardNames.get(nextName);
        } else {
            int nowIdx = cards.indexOf(currentCard);
            if (nowIdx >= 0) {
                if (nowIdx < cards.size() - 1) {
                    nextCard = (InstallerCard) cards.elementAt(nowIdx + 1);
                }
            }
        }
        if (nextCard != null) {
            nextEnabled = true;
            nextCard.enterCard(currentCard.getCardName());
            showCard(nextCard);
            nextButton.setEnabled(nextEnabled);
        }
    }

    /**
     * Go to the previous card.
     *
     * This method will be called from the "back" button but you can also
     * use it from an installer card.
     */
    public void prevCard() {
        if (currentCard == null) return;
        String prevName = currentCard.getPrevCardName();
        InstallerCard prevCard = null;
        if (prevName != null) {
            prevCard = (InstallerCard) cardNames.get(prevName);
        }
        if (prevCard != null) showCard(prevCard);
    }

    /********************************
     * Action-Listener ...
     */
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("prev")) {
            prevCard();
            return;
        } else if (ev.getActionCommand().equals("next")) {
            nextCard();
            return;
        }
    }

    /**
     * ... ActionListener
     *********************************
     * WindowListener ...
     */
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
