package mw.client.dialogs;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.JButton;
import mw.client.gui.MWCardImpl;
import mw.client.managers.AudioManager;
import mw.client.managers.DialogManager;
import mw.client.managers.GameManager;
import mw.client.managers.ImageManager;
import mw.client.managers.ProfileManager;
import mw.client.managers.SettingsManager;
import mw.client.utils.dialogs.DialogContainer;
import mw.client.utils.dialogs.DlgParams;
import mw.client.utils.dialogs.IDialogPanel;
import mw.client.utils.gui.HoverButton;
import mw.client.utils.gui.ShadowLabel;
import mw.server.list.CardBeanList;
import mw.server.model.bean.CardBean;
import mw.server.pattern.Command;

public class GraveDialog extends IDialogPanel {

    private static final long serialVersionUID = 1L;

    private ShadowLabel jTitle = null;

    private HoverButton jButtonOK = null;

    private HoverButton jButtonPrevPage = null;

    private HoverButton jButtonNextPage = null;

    private JButton jButtonSort = null;

    private CardBeanList cards;

    private int playerID;

    private int page = 1;

    private int maxPages;

    private int in_a_row = 4;

    private int rows = 3;

    HashMap<CardBean, MWCardImpl> map = new HashMap<CardBean, MWCardImpl>();

    /**
     * This is the default constructor
     */
    public GraveDialog(DlgParams params) {
        super(params);
        cards = params.getCardList();
        playerID = params.getPlayerID();
        if (SettingsManager.getManager().getCardLook() == 2) {
            in_a_row = 5;
            rows = 2;
        }
        maxPages = cards.size() / (in_a_row * rows);
        if (cards.size() % (in_a_row * rows) != 0) {
            maxPages++;
        }
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        String title = "";
        if (playerID == ProfileManager.getMyId()) {
            title += "Your ";
        } else {
            title += "Opponent's ";
        }
        title += "graveyard: ";
        if (cards.size() == 0) {
            title += "no cards";
        } else if (cards.size() == 1) {
            title += "1 card";
        } else {
            title += cards.size() + " cards";
        }
        jTitle = new ShadowLabel(title, 14);
        jTitle.setBounds(new Rectangle(5, 3, 500, 16));
        jTitle.setFont(new Font("Dialog", Font.BOLD, 14));
        this.setLayout(null);
        this.add(jTitle, null);
        this.add(getJButtonOK(), null);
        this.add(getJButtonPrevPage(), null);
        this.add(getJButtonNextPage(), null);
        this.add(getJButtonSort(), null);
        makeTransparent();
        GameManager.getManager().resetChosenCards();
        displayCards();
    }

    private void displayCards() {
        if (cards.size() == 0) {
            return;
        }
        int offsetY = 50;
        if (SettingsManager.getManager().getCardLook() == 2) offsetY += 30;
        ArrayList<Component> toRemove = new ArrayList<Component>();
        for (int i = getComponentCount() - 1; i > 0; i--) {
            Component o = getComponent(i);
            if (o instanceof MWCardImpl) {
                toRemove.add(o);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            remove(toRemove.get(i));
        }
        int j = 0;
        int offset = (page - 1) * (in_a_row * rows);
        for (int i = 0; i + offset < cards.size() && i < in_a_row * rows; i++) {
            if (i > 0 && i % in_a_row == 0) {
                j++;
            }
            if (j == rows) {
                return;
            }
            final CardBean c = cards.get(offset + i);
            MWCardImpl mtgCard = null;
            if (map.containsKey(c)) {
                mtgCard = map.get(c);
                CardBean chosenCard = GameManager.getManager().getChosenCard();
                if (chosenCard != null && !chosenCard.equals(mtgCard.getCard())) {
                    mtgCard.setBordered(false);
                }
            } else {
                mtgCard = new MWCardImpl(c);
                mtgCard.setEnteredMouseListener();
                mtgCard.setDoubleClickPlay(this);
                map.put(c, mtgCard);
            }
            mtgCard.setBounds(70 + (i - j * in_a_row) * (SettingsManager.getManager().getCardSize().width + 10), offsetY + j * (SettingsManager.getManager().getCardSize().height + 10), SettingsManager.getManager().getCardSize().width, SettingsManager.getManager().getCardSize().height);
            add(mtgCard);
        }
        repaint();
    }

    private HoverButton getJButtonOK() {
        if (jButtonOK == null) {
            jButtonOK = new HoverButton("", ImageManager.getCancelButtonIcon(), ImageManager.getActiveCancelButtonIcon(), ImageManager.getCancelButtonIcon(), new Rectangle(60, 60));
            int w = getDlgParams().rect.width - 75;
            int h = getDlgParams().rect.height - 90;
            jButtonOK.setBounds(new Rectangle(w / 2 - 40, h - 50, 60, 60));
            jButtonOK.setObserver(new Command() {

                @Override
                public void execute() {
                    DialogManager.getManager().fadeOut((DialogContainer) getParent());
                }

                private static final long serialVersionUID = 1L;
            });
        }
        return jButtonOK;
    }

    private HoverButton getJButtonPrevPage() {
        if (jButtonPrevPage == null) {
            jButtonPrevPage = new HoverButton("", ImageManager.getDlgPrevButtonIcon(), ImageManager.getActiveDlgPrevButtonIcon(), ImageManager.getDlgPrevButtonIcon(), new Rectangle(60, 60));
            int w = getDlgParams().rect.width - 75;
            int h = getDlgParams().rect.height - 90;
            jButtonPrevPage.setBounds(new Rectangle(w / 2 - 125, h - 50, 60, 60));
            jButtonPrevPage.setVisible(false);
            jButtonPrevPage.setObserver(new Command() {

                public void execute() {
                    if (page == 1) {
                        return;
                    }
                    AudioManager.playPrevPage();
                    page--;
                    getJButtonPrevPage().setVisible(false);
                    getJButtonOK().setVisible(false);
                    getJButtonNextPage().setVisible(false);
                    revalidate();
                    displayCards();
                    if (page != 1) {
                        getJButtonPrevPage().setVisible(true);
                    }
                    getJButtonOK().setVisible(true);
                    getJButtonNextPage().setVisible(true);
                }

                private static final long serialVersionUID = 1L;
            });
        }
        return jButtonPrevPage;
    }

    private HoverButton getJButtonNextPage() {
        if (jButtonNextPage == null) {
            jButtonNextPage = new HoverButton("", ImageManager.getDlgNextButtonIcon(), ImageManager.getActiveDlgNextButtonIcon(), ImageManager.getDlgNextButtonIcon(), new Rectangle(60, 60));
            int w = getDlgParams().rect.width - 75;
            int h = getDlgParams().rect.height - 90;
            jButtonNextPage.setBounds(new Rectangle(w / 2 + 45, h - 50, 60, 60));
            if (maxPages > 1) {
                jButtonNextPage.setVisible(true);
            } else {
                jButtonNextPage.setVisible(false);
            }
            jButtonNextPage.setObserver(new Command() {

                public void execute() {
                    if (page == maxPages) {
                        return;
                    }
                    AudioManager.playNextPage();
                    page++;
                    getJButtonPrevPage().setVisible(false);
                    getJButtonOK().setVisible(false);
                    getJButtonNextPage().setVisible(false);
                    revalidate();
                    displayCards();
                    getJButtonPrevPage().setVisible(true);
                    getJButtonOK().setVisible(true);
                    if (page != maxPages) {
                        getJButtonNextPage().setVisible(true);
                    }
                }

                private static final long serialVersionUID = 1L;
            });
        }
        return jButtonNextPage;
    }

    /**
     * This method initializes jButtonNextPage  
     *  
     * @return javax.swing.JButton      
     */
    private JButton getJButtonSort() {
        if (jButtonSort == null) {
            jButtonSort = new JButton();
            int w = getDlgParams().rect.width - 75;
            int h = getDlgParams().rect.height - 75;
            jButtonSort.setBounds(new Rectangle(w / 2 + 150, h - 30, 78, 22));
            jButtonSort.setText("Sort");
            jButtonSort.setVisible(false);
            if (maxPages == 1) {
                jButtonSort.setVisible(false);
            }
            jButtonSort.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cards.sort(new Comparator<CardBean>() {

                        public int compare(CardBean c1, CardBean c2) {
                            return c1.toString().compareTo(c2.toString());
                        }
                    });
                    GameManager.getManager().resetChosenCards();
                    jButtonSort.setVisible(false);
                    if (page > 1) {
                        page = 2;
                        displayCards();
                    }
                    page = 1;
                    displayCards();
                }
            });
        }
        return jButtonSort;
    }

    public void turnCardBorderOff(CardBean card) {
        for (int i = 0; i < getComponentCount(); i++) {
            Object o = getComponent(i);
            if (o instanceof MWCardImpl) {
                CardBean c = ((MWCardImpl) o).getCard();
                if (c.equals(card)) {
                    ((MWCardImpl) o).setBordered(false);
                    return;
                }
            }
        }
    }

    public void changeTitle(String cardName) {
        jTitle.setText("Choose a card (by double-click), chosen card: " + cardName);
    }
}
