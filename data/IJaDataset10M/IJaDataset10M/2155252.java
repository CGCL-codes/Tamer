package jgnash.ui.register.invest;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.event.EventListenerList;
import jgnash.engine.Account;
import jgnash.engine.EngineResource;
import jgnash.engine.InvestmentTransaction;
import jgnash.engine.Transaction;
import jgnash.engine.TransactionType;
import jgnash.engine.event.WeakObservable;
import jgnash.engine.event.WeakObserver;
import jgnash.engine.event.jgnashEvent;
import jgnash.ui.components.ShadowBorder;
import jgnash.ui.register.RegisterEvent;
import jgnash.ui.register.RegisterListener;
import jgnash.ui.util.UIResource;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Panel that uses a cardlayout to display the investment transaction forms.
 *
 * @author Craig Cavanaugh
 * @author Don Brown
 * 
 * $Id: InvestmentTransactionPanel.java 675 2008-06-17 01:36:01Z ccavanaugh $
 */
public class InvestmentTransactionPanel extends JPanel implements WeakObserver, ActionListener {

    private Account account;

    private Transaction modTrans;

    private int currentCard;

    private CardLayout cardLayout;

    private JButton enterButton;

    private JButton cancelButton;

    private JPanel cardPanel;

    private JComboBox actionCombo;

    private AbstractInvTransactionPanel[] cards = new AbstractInvTransactionPanel[0];

    private static UIResource rb = (UIResource) UIResource.get();

    private static String[] actions;

    private EventListenerList listenerList = new EventListenerList();

    static {
        EngineResource eRb = (EngineResource) EngineResource.get();
        actions = new String[] { eRb.getString("buyShare"), eRb.getString("sellShare"), eRb.getString("transferIn"), eRb.getString("transferOut"), eRb.getString("addShare"), eRb.getString("removeShare"), eRb.getString("reinvestDiv"), eRb.getString("dividend"), eRb.getString("splitShare"), eRb.getString("mergeShare") };
    }

    public InvestmentTransactionPanel(Account account) {
        this.account = account;
        actionCombo = new JComboBox(new DefaultComboBoxModel(actions));
        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout());
        enterButton = new JButton(rb.getString("Button.Enter"));
        cancelButton = new JButton(rb.getString("Button.Cancel"));
        layoutMainPanel();
        cancelButton.addActionListener(this);
        enterButton.addActionListener(this);
        actionCombo.addActionListener(this);
        cardLayout = (CardLayout) cardPanel.getLayout();
        loadCards();
        engine.addTransactionObserver(this);
        engine.addAccountObserver(this);
        enterButton.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterAction();
                }
            }
        });
    }

    private void layoutMainPanel() {
        FormLayout layout = new FormLayout("d, 4dlu, m:g, 4dlu, m", "f:d, 5dlu, f:d, 5dlu, f:d");
        CellConstraints cc = new CellConstraints();
        setBorder(new CompoundBorder(new ShadowBorder(), Borders.DIALOG_BORDER));
        setLayout(layout);
        add(cardPanel, cc.xyw(1, 1, 5));
        add(new JSeparator(), cc.xyw(1, 3, 5));
        add(new JLabel(rb.getString("Label.Action")), cc.xy(1, 5));
        add(actionCombo, cc.xy(3, 5));
        add(ButtonBarFactory.buildOKCancelBar(enterButton, cancelButton), cc.xy(5, 5));
    }

    public void addRegisterListener(RegisterListener l) {
        listenerList.add(RegisterListener.class, l);
    }

    public void removeRegisterListener(RegisterListener l) {
        listenerList.remove(RegisterListener.class, l);
    }

    /** Notify all listeners of a cancel action
     */
    protected void fireCancelAction() {
        RegisterEvent e = null;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RegisterListener.class) {
                if (e == null) {
                    e = new RegisterEvent(this, RegisterEvent.CANCEL_ACTION);
                }
                ((RegisterListener) listeners[i + 1]).registerEvent(e);
            }
        }
    }

    /** Notify all listeners of an OK action
     */
    protected void fireOkAction() {
        RegisterEvent e = null;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RegisterListener.class) {
                if (e == null) {
                    e = new RegisterEvent(this, RegisterEvent.OK_ACTION);
                }
                ((RegisterListener) listeners[i + 1]).registerEvent(e);
            }
        }
    }

    protected void cancelAction() {
        cards[currentCard].clearForm();
        modTrans = null;
        fireCancelAction();
    }

    void enterAction() {
        cards[currentCard].enterAction();
        fireOkAction();
    }

    private void actionAction() {
        activateCard(actionCombo.getSelectedIndex());
    }

    public void modifyTransaction(Transaction t) {
        modTrans = t;
        if (t instanceof InvestmentTransaction) {
            InvestmentTransaction _t = (InvestmentTransaction) t;
            TransactionType type = _t.getType();
            if (type == TransactionType.BUYSHARE) {
                actionCombo.setSelectedItem(actions[0]);
            } else if (type == TransactionType.SELLSHARE) {
                actionCombo.setSelectedItem(actions[1]);
            } else if (type == TransactionType.ADDSHARE) {
                actionCombo.setSelectedItem(actions[4]);
            } else if (type == TransactionType.REMOVESHARE) {
                actionCombo.setSelectedItem(actions[5]);
            } else if (type == TransactionType.REINVESTDIV) {
                actionCombo.setSelectedItem(actions[6]);
            } else if (type == TransactionType.DIVIDEND) {
                actionCombo.setSelectedItem(actions[7]);
            } else if (type == TransactionType.SPLITSHARE) {
                actionCombo.setSelectedItem(actions[8]);
            } else if (type == TransactionType.MERGESHARE) {
                actionCombo.setSelectedItem(actions[9]);
            } else {
                return;
            }
        } else {
            if (t.getAmount(account).signum() >= 0) {
                actionCombo.setSelectedItem(actions[2]);
            } else {
                actionCombo.setSelectedItem(actions[3]);
            }
        }
        cards[currentCard].modifyTransaction(t);
    }

    private void loadCards() {
        cards = new AbstractInvTransactionPanel[10];
        cards[0] = new SellSharePanel(account, TransactionType.BUYSHARE);
        cards[1] = new SellSharePanel(account, TransactionType.SELLSHARE);
        cards[2] = new TransferPanel(account, TransferPanel.CREDIT);
        cards[3] = new TransferPanel(account, TransferPanel.DEBIT);
        cards[4] = new AddRemoveSharePanel(account, TransactionType.ADDSHARE);
        cards[5] = new AddRemoveSharePanel(account, TransactionType.REMOVESHARE);
        cards[6] = new ReinvestDividendPanel(account);
        cards[7] = new DividendPanel(account);
        cards[8] = new SplitMergeSharePanel(account, TransactionType.SPLITSHARE);
        cards[9] = new SplitMergeSharePanel(account, TransactionType.MERGESHARE);
        for (int i = 0; i < actions.length; i++) {
            cardPanel.add(cards[i], actions[i]);
        }
    }

    private void activateCard(int index) {
        cards[currentCard].clearForm();
        cardLayout.show(cardPanel, actions[index]);
        currentCard = index;
    }

    public void update(final WeakObservable o, final jgnashEvent event) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (event.messageId == jgnashEvent.TRANSACTION_REMOVE) {
                    if (modTrans == event.transaction) {
                        cancelAction();
                    }
                }
            }
        });
    }

    /** Invoked when an action occurs. */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            cancelAction();
        } else if (e.getSource() == enterButton) {
            enterAction();
        } else if (e.getSource() == actionCombo) {
            actionAction();
        }
    }
}
