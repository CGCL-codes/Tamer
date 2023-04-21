package jmemorize.gui.swing;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import jmemorize.core.Card;
import jmemorize.core.Category;
import jmemorize.core.CategoryObserver;
import jmemorize.gui.Localization;
import jmemorize.util.EscapableFrame;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The window that is used to edit cards. Note this is a singleton class. The
 * same window will be reused for all editting.
 * 
 * @author djemili
 */
public class EditCardFrame extends EscapableFrame implements CategoryObserver {

    private class NextCardAction extends AbstractAction {

        public NextCardAction() {
            putValue(NAME, Localization.getString("EditCard.NEXT_CARD"));
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resource/icons/card_next.gif")));
            putValue(SHORT_DESCRIPTION, Localization.getString("EditCard.NEXT_CARD_DESC"));
            putValue(MNEMONIC_KEY, new Integer(1));
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (confirmCardSides()) {
                showNext();
            }
        }
    }

    private class PreviousCardAction extends AbstractAction {

        public PreviousCardAction() {
            putValue(NAME, Localization.getString("EditCard.PREV_CARD"));
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resource/icons/card_prev.gif")));
            putValue(SHORT_DESCRIPTION, Localization.getString("EditCard.PREV_CARD_DESC"));
            putValue(MNEMONIC_KEY, new Integer(1));
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (confirmCardSides()) {
                showPrevious();
            }
        }
    }

    private class RemoveCardAction extends AbstractAction {

        public RemoveCardAction() {
            putValue(NAME, Localization.getString("EditCard.REMOVE_CARD"));
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resource/icons/remove.gif")));
            putValue(SHORT_DESCRIPTION, Localization.getString("EditCard.REMOVE_CARD_DESC"));
            putValue(MNEMONIC_KEY, new Integer(3));
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            int n = JOptionPane.showConfirmDialog(EditCardFrame.this, Localization.getString("EditCard.REMOVE_CARD_WARN"), Localization.getString("EditCard.REMOVE_CARD_WARN_TITLE"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (n == JOptionPane.OK_OPTION) {
                Card deleteCard = m_currentCard;
                if (hasNext()) {
                    showNext();
                } else if (hasPrevious()) {
                    showPrevious();
                }
                deleteCard.getCategory().removeCard(deleteCard);
            }
        }
    }

    private class ResetCardAction extends AbstractAction {

        public ResetCardAction() {
            putValue(NAME, Localization.getString("EditCard.RESET_CARD"));
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resource/icons/card_reset.gif")));
            putValue(SHORT_DESCRIPTION, Localization.getString("EditCard.RESET_CARD_DESC"));
            putValue(MNEMONIC_KEY, new Integer(3));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            int n = JOptionPane.showConfirmDialog(EditCardFrame.this, Localization.getString("EditCard.RESET_CARD_WARN"), Localization.getString("EditCard.RESET_CARD"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (n == JOptionPane.OK_OPTION) {
                m_category.resetCard(m_currentCard);
            }
        }
    }

    private static final int MAX_TITLE_LENGTH = 80;

    private Action m_nextCardAction = new NextCardAction();

    private Action m_previousCardAction = new PreviousCardAction();

    private Action m_removeCardAction = new RemoveCardAction();

    private Action m_resetCardAction = new ResetCardAction();

    private Card m_currentCard;

    private int m_currentCardIndex;

    private ArrayList m_cards;

    private Category m_category;

    private JButton m_applyButton = new JButton(Localization.getString("General.APPLY"));

    private CardSidesPanel m_cardPanel = new CardSidesPanel();

    private JTextField m_categoryField = new JTextField();

    private JTextField m_createdField = new JTextField();

    private JTextField m_testedField = new JTextField();

    private JTextField m_expiresField = new JTextField();

    private JTextField m_ratioField = new JTextField();

    private static EditCardFrame m_instance;

    private boolean m_searchCase;

    private int m_searchSide;

    private String m_searchText;

    /**
     * @return The singleton instance.
     */
    public static EditCardFrame getInstance() {
        if (m_instance == null) {
            m_instance = new EditCardFrame();
        }
        return m_instance;
    }

    /**
     * Show the card so that the user can edit it.
     * 
     * @param card The card that is to be shown.
     */
    public void showCard(Card card) {
        List cards = new ArrayList(1);
        cards.add(card);
        showCard(card, cards, card.getCategory());
    }

    public void showCard(Card card, List cards, Category category) {
        showCard(card, cards, category, null, 0, true);
    }

    public void showCard(Card card, List cards, Category category, String searchText, int side, boolean ignoreCase) {
        if (isVisible() && !confirmCardSides()) {
            return;
        }
        m_searchText = searchText;
        m_searchSide = side;
        m_searchCase = ignoreCase;
        m_currentCard = card;
        m_currentCardIndex = cards.indexOf(card);
        m_cards = new ArrayList(cards);
        if (m_category != null) {
            m_category.removeObserver(this);
        }
        m_category = category;
        if (m_category != null) {
            category.addObserver(this);
        }
        updatePanel();
        setVisible(true);
    }

    /**
     * @return True if window was closed. False if this was prevented by user option.
     */
    public boolean close() {
        if (confirmCardSides()) {
            setVisible(false);
            return true;
        }
        return false;
    }

    public void onCategoryEvent(int type, Category category) {
        if (type == REMOVED_EVENT) {
            if (category.contains(m_currentCard.getCategory())) {
                setVisible(false);
            }
            for (Iterator it = m_cards.iterator(); it.hasNext(); ) {
                Card card = (Card) it.next();
                if (category.contains(card.getCategory())) {
                    m_cards.remove(card);
                }
            }
            m_currentCardIndex = m_cards.indexOf(m_currentCard);
            updateActions();
        } else if (type == EDITED_EVENT) {
            fillFields();
        }
    }

    public void onCardEvent(int type, Card card, int deck) {
        if (type == DECK_EVENT && m_currentCard == card) {
            fillFields();
        }
        if (type == REMOVED_EVENT) {
            if (m_currentCard == card) {
                setVisible(false);
            }
            if (m_cards.remove(card)) {
                m_currentCardIndex = m_cards.indexOf(m_currentCard);
                updateActions();
            }
        }
    }

    /**
     * If the content of the text panes differ from the currently saved card
     * entries, this will bring up a dialog that asks if the user wants to save 
     * the changes. If yes is selected the card sides are saved.
     * 
     * This should be called everytime there is the chance of losing card 
     * informations.
     * 
     * @return True if operation wasnt aborted by user.
     */
    private boolean confirmCardSides() {
        if (!m_cardPanel.getFrontside().equals(m_currentCard.getFrontSide()) || !m_cardPanel.getBackside().equals(m_currentCard.getBackSide())) {
            int n = JOptionPane.showConfirmDialog(this, Localization.getString("EditCard.MODIFIED_WARN") + "", Localization.getString("EditCard.MODIFIED_WARN_TITLE"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (n == JOptionPane.CANCEL_OPTION) {
                return false;
            }
            if (n == JOptionPane.YES_OPTION) {
                return saveSides();
            }
        }
        return true;
    }

    /** 
     * Creates new form EditCardFrame 
     */
    private EditCardFrame() {
        initComponents();
        m_cardPanel.addTextListener(new CardSidesPanel.TextChangeListener() {

            public void onTextChange() {
                if (!m_applyButton.isEnabled()) {
                    m_applyButton.setEnabled(true);
                }
            }
        });
        setLocationRelativeTo(null);
    }

    private void updatePanel() {
        updateTitle();
        m_cardPanel.setSides(m_currentCard.getFrontSide(), m_currentCard.getBackSide());
        updateActions();
        fillFields();
        m_applyButton.setEnabled(false);
    }

    /**
     * Update the title of this frame.
     */
    private void updateTitle() {
        String title = m_currentCard.getFrontSide();
        title = title.replace('\n', ' ');
        if (title.length() > MAX_TITLE_LENGTH) {
            title = title.substring(0, MAX_TITLE_LENGTH) + "...";
        }
        setTitle(title);
    }

    /**
     * Updates the actions of this EditCardFrame i.e. enabling/disabling certain
     * buttons.
     */
    private void updateActions() {
        if (m_cards == null) {
            m_nextCardAction.setEnabled(false);
            m_previousCardAction.setEnabled(false);
        } else {
            m_previousCardAction.setEnabled(hasPrevious());
            m_nextCardAction.setEnabled(hasNext());
        }
    }

    /**
     * @return <code>true</code> if there is another card left after this one.
     */
    private boolean hasNext() {
        return m_currentCardIndex < m_cards.size() - 1;
    }

    /**
     * @return <code>true</code> if there is a another card before this one.
     */
    private boolean hasPrevious() {
        return m_currentCardIndex > 0;
    }

    /**
     * Show the next card of the card list of this EditCardFrame.
     */
    private void showNext() {
        m_currentCard = (Card) m_cards.get(++m_currentCardIndex);
        updatePanel();
    }

    /**
     * Show the previous card of the card list of this EditCardFrame.
     */
    private void showPrevious() {
        m_currentCard = (Card) m_cards.get(--m_currentCardIndex);
        updatePanel();
    }

    /**
     * Fill the fields on the properties tab.
     */
    private void fillFields() {
        m_categoryField.setText(m_currentCard.getCategory().getPath() + " (" + m_currentCard.getLevel() + ")");
        Date date = m_currentCard.getDateCreated();
        m_createdField.setText(Localization.LONG_DATE_FORMATER.format(date));
        date = m_currentCard.getDateTested();
        m_testedField.setText(date != null ? Localization.LONG_DATE_FORMATER.format(date) : "-");
        date = m_currentCard.getDateExpired();
        m_expiresField.setText(date != null ? Localization.LONG_DATE_FORMATER.format(date) : "-");
        if (m_currentCard.getTestsTotal() > 0) {
            Object[] args = { new Integer(m_currentCard.getPassRatio()), new Integer(m_currentCard.getTestsPassed()), new Integer(m_currentCard.getTestsTotal()) };
            MessageFormat form = new MessageFormat("{0}%    ({1}/{2})");
            m_ratioField.setText(form.format(args));
        } else {
            m_ratioField.setText("-");
        }
    }

    private boolean saveSides() {
        try {
            m_currentCard.setSides(m_cardPanel.getFrontside(), m_cardPanel.getBackside());
            updateTitle();
            m_applyButton.setEnabled(false);
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, Localization.getString("General.EMPTY_SIDES_ALERT"), Localization.getString("General.EMPTY_SIDES_ALERT_TITLE"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void initComponents() {
        m_cardPanel.setBorder(Borders.DIALOG_BORDER);
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(new JButton(m_previousCardAction));
        toolBar.add(new JButton(m_nextCardAction));
        toolBar.add(new JButton(m_removeCardAction));
        toolBar.add(new JButton(m_resetCardAction));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new javax.swing.border.EtchedBorder());
        tabbedPane.setMinimumSize(new java.awt.Dimension(380, 138));
        tabbedPane.setPreferredSize(new java.awt.Dimension(400, 426));
        tabbedPane.addTab(Localization.getString("EditCard.TAB_SIDES"), m_cardPanel);
        tabbedPane.addTab(Localization.getString("EditCard.TAB_PROPERTIES"), buildPropertiesPanel());
        getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);
        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);
        getContentPane().add(buildBottomButtonBar(), java.awt.BorderLayout.SOUTH);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icons/card_edit.gif")));
        pack();
    }

    private JPanel buildBottomButtonBar() {
        JButton okayButton = new JButton(Localization.getString("General.OKAY"));
        okayButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSides();
                close();
            }
        });
        JButton cancelButton = new JButton(Localization.getString("General.CANCEL"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });
        m_applyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSides();
            }
        });
        JPanel buttonPanel = ButtonBarFactory.buildOKCancelApplyBar(okayButton, cancelButton, m_applyButton);
        buttonPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        getRootPane().setDefaultButton(okayButton);
        return buttonPanel;
    }

    private JPanel buildPropertiesPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, d:grow", "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p");
        CellConstraints cc = new CellConstraints();
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        m_categoryField.setEditable(false);
        m_createdField.setEditable(false);
        m_testedField.setEditable(false);
        m_expiresField.setEditable(false);
        m_ratioField.setEditable(false);
        builder.addSeparator(Localization.getString("General.GENERAL"), cc.xyw(1, 1, 3));
        builder.addLabel(Localization.getString("General.CATEGORY"), cc.xy(1, 3));
        builder.add(m_categoryField, cc.xy(3, 3));
        builder.addLabel(Localization.getString("General.CREATED"), cc.xy(1, 5));
        builder.add(m_createdField, cc.xy(3, 5));
        builder.addLabel(Localization.getString("General.LAST_TEST"), cc.xy(1, 7));
        builder.add(m_testedField, cc.xy(3, 7));
        builder.addLabel(Localization.getString("General.EXPIRES"), cc.xy(1, 9));
        builder.add(m_expiresField, cc.xy(3, 9));
        builder.addSeparator(Localization.getString("EditCard.DETAILS_HISTORY"), cc.xyw(1, 11, 3));
        builder.addLabel(Localization.getString("EditCard.DETAILS_RATIO"), cc.xy(1, 13));
        builder.add(m_ratioField, cc.xy(3, 13));
        return builder.getPanel();
    }
}
