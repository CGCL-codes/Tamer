package chsec.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import chsec.domain.CashCount;
import chsec.domain.CashDonation;
import chsec.domain.CreditCard;
import chsec.domain.Donation;
import chsec.domain.DonationCat;
import chsec.domain.Parishioner;
import chsec.domain.Person;
import chsec.util.RptDesc;
import chsec.util.UIResources;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DonationsEditorImp extends JFrame implements DonationsEditor, DonEditorMenuCont, CashCountListener {

    private static final long serialVersionUID = 1L;

    private static final String _ADD_ROW_ICON = "img/table_row_insert.png";

    private static final String _REM_ROW_ICON = "img/table_row_delete.png";

    private static final String _UPD_ROW_ICON = "img/table_edit.png";

    private static final String _SAVE_ICON = "img/accept.png";

    private static final String _EXIT_ICON = "img/arrow_out.png";

    private static final String _CLEAR_ICON = "img/page_white.png";

    private class _catListModel extends AbstractListModel implements ComboBoxModel {

        Object selCat;

        public Object getSelectedItem() {
            return selCat;
        }

        public void setSelectedItem(Object selI) {
            Object newSelCat = selI;
            if (newSelCat != null) {
                int selIdx = baseCatL.indexOf(newSelCat);
                if (selIdx >= 0 && canExit()) {
                    DonationCat donCat = baseCatL.get(selIdx);
                    currPane = getCenterPane().getComponent(selIdx);
                    getCardLayout().show(getCenterPane(), donCat.toString());
                    getCatLongNmL().setText(donCat.getDescr());
                } else {
                    return;
                }
            }
            selCat = newSelCat;
            fireContentsChanged(this, 0, baseCatL.size() - 1);
        }

        public Object getElementAt(int idx) {
            return baseCatL.get(idx);
        }

        public int getSize() {
            return baseCatL.size();
        }

        public void addItem(DonationCat donCat) {
            int prevSz = baseCatL.size();
            baseCatL.add(donCat);
            fireIntervalAdded(this, prevSz, prevSz);
            DonPane donPan = (DonPane) DonationsEditorImp.this.donPaneFry.createDonPane(donCat, DonationsEditorImp.this, control, parnrL, ccL);
            getCenterPane().add(donCat.getCatNm(), (Component) donPan);
            setSelectedItem(donCat);
        }

        public boolean removeItem(DonationCat donCat) {
            int idx = baseCatL.indexOf(donCat);
            if (idx >= 0) {
                doRemCatPan(idx);
                return true;
            }
            return false;
        }

        private void doRemCatPan(int idx) {
            Component pane = getCenterPane().getComponent(idx);
            getCenterPane().remove(idx);
            baseCatL.remove(idx);
            fireIntervalRemoved(this, idx, idx);
            if (currPane == pane) {
                currPane = null;
                if (!baseCatL.isEmpty()) {
                    setSelectedItem(baseCatL.get(0));
                }
            }
        }

        public void clearItems() {
            for (int i = 0; i < baseCatL.size(); i++) {
                doRemCatPan(i);
            }
        }

        public boolean updateItem(DonationCat donCat) {
            int idx = baseCatL.indexOf(donCat);
            if (idx >= 0) {
                baseCatL.set(idx, donCat);
                fireContentsChanged(this, idx, idx);
                DonPane pane = (DonPane) getCenterPane().getComponent(idx);
                pane.setDonCat(donCat);
                return true;
            }
            return false;
        }
    }

    private JPanel jContentPane = null;

    private JMenuBar myMenuBar = null;

    private DonationsEditorCtrl control;

    private Action exitA;

    private Action saveA;

    private Action addRecA;

    private Action delRecA;

    private Action editSubCatA;

    private Action updRecA;

    private Action clearRecA;

    private DonPaneFry donPaneFry;

    private List<RptDesc> reports;

    private boolean initialized;

    private UIResources uiResources;

    private List<CreditCard> ccL;

    private JPanel centerPane;

    private CardLayout cardLayout;

    private JPanel northPane;

    private JComboBox donCatCB;

    private _catListModel donCatListModel;

    private Component currPane;

    private Date collDate = new Date();

    private List<DonationCat> baseCatL = new ArrayList<DonationCat>();

    private List<Parishioner> parnrL;

    private JLabel catLongNmL = null;

    private JPanel centerCtxPane = null;

    private JMenu printMn;

    public void setDonPaneFry(DonPaneFry donPaneFry) {
        this.donPaneFry = donPaneFry;
    }

    public void setUiResources(UIResources uiResources) {
        this.uiResources = uiResources;
    }

    DonationsEditorCtrl getControl() {
        return control;
    }

    public void setControl(DonationsEditorCtrl control) {
        this.control = control;
        initialize();
    }

    public void setReports(List<RptDesc> reports) {
        this.reports = reports;
    }

    public void deleteDonation() {
        if (currPane != null) {
            ((PersonDonPane) currPane).delDon();
            getSaveAction().setEnabled(true);
            printMn.setEnabled(false);
        }
    }

    public void donSelected(Donation selDon) {
        getUpdRecAction().setEnabled(true);
        if (selDon instanceof CashDonation) {
            CashDonation cash = (CashDonation) selDon;
            ((AnonymDonPane) currPane).setCashCounts(cash.getCounts().values());
        } else {
            ((AnonymDonPane) currPane).setCashCounts(null);
        }
    }

    public void payMethodSelected(PaymentMethod pm) {
        if (pm == PaymentMethod.Cash) {
            ((AnonymDonPane) currPane).setCashCounts(new ArrayList<CashCount>());
        } else {
            ((AnonymDonPane) currPane).setCashCounts(null);
        }
    }

    public void addNewDon(Donation newDon) {
        if (currPane != null) {
            ((PersonDonPane) currPane).addDon(newDon);
            getSaveAction().setEnabled(true);
            printMn.setEnabled(false);
            ((AnonymDonPane) currPane).setCashCounts(new ArrayList<CashCount>());
        }
    }

    public void showError(String errMsg) {
        JOptionPane.showMessageDialog(this, errMsg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public DonationCat getChangedDonationCat() {
        if (currPane != null) {
            return ((DonPane) currPane).getDonCat();
        }
        return null;
    }

    public Date getCollectionDate() {
        return collDate;
    }

    public String getNewCheckNo() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).getNewCheckNo();
        }
        return null;
    }

    public Person getNewDonPayer() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).getNewDonPayer();
        }
        return null;
    }

    public float getNewDonSum() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).getNewDonSum();
        }
        return 0;
    }

    public boolean isNewDonTaxDed() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).isNewDonTaxDed();
        }
        return false;
    }

    public int getSelDonRow() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).getSelDonRow();
        }
        return 0;
    }

    public void updateDonation(Donation don, int idx) {
        if (currPane != null) {
            ((PersonDonPane) currPane).updDon(don, idx);
            getSaveAction().setEnabled(true);
            printMn.setEnabled(false);
        }
    }

    public void updateCashTotal(float total) {
        if (currPane != null) {
            ((PersonDonPane) currPane).updateCashTotal(total);
            getSaveAction().setEnabled(true);
            printMn.setEnabled(false);
        }
    }

    public Collection<CashCount> getCashCounts() {
        if (currPane != null) {
            return ((AnonymDonPane) currPane).getCashCounts();
        }
        return null;
    }

    public void clearDonCat() {
        donCatListModel.clearItems();
    }

    public void cashCountChanged(CashCount cc) {
        control.cashCountChanged(cc);
    }

    /**
	 * This is the default constructor
	 */
    public DonationsEditorImp() {
        super();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        if (!initialized) {
            this.setSize(500, 600);
            this.setJMenuBar(getMyMenuBar());
            this.setContentPane(getJContentPane());
            this.setTitle("Donations Editor");
            getJContentPane().add(getCenterCtxPane(), BorderLayout.CENTER);
            getJContentPane().add(getNorthPane(), BorderLayout.NORTH);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent ev) {
                    exitA.actionPerformed(null);
                }
            });
            if (reports != null && reports.size() > 0) {
                for (RptDesc desc : reports) {
                    printMn.add(new JMenuItem(new _rptMenuAction(desc)));
                }
            }
            setLocationRelativeTo(null);
            initialized = true;
        }
    }

    public void run() {
        if (!baseCatL.isEmpty()) {
            getDonCatCB().setSelectedIndex(0);
        }
        setVisible(true);
    }

    public List<Parishioner> getParnrL() {
        return parnrL;
    }

    public void setParnrL(List<Parishioner> parnrL) {
        this.parnrL = parnrL;
    }

    public void clearDon() {
        if (currPane != null) {
            ((PersonDonPane) currPane).clearDon();
            ((AnonymDonPane) currPane).setCashCounts(null);
        }
    }

    public CreditCard getNewDonCC() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).getCC();
        }
        return null;
    }

    public PaymentMethod getNewDonPayMeth() {
        if (currPane != null) {
            return ((PersonDonPane) currPane).getPayMethod();
        }
        return null;
    }

    public void setCreditCardL(List<CreditCard> ccL) {
        this.ccL = ccL;
    }

    public void addDonCat(DonationCat donCat, CashCountListener lnr) {
        donCatListModel.addItem(donCat);
    }

    public void deleteDonCat(DonationCat donCat) {
        donCatListModel.removeItem(donCat);
    }

    public void donationsSaved() {
        getSaveAction().setEnabled(false);
        printMn.setEnabled(true);
    }

    public void enableEditCategories(boolean flg) {
        getEditSubCatAction().setEnabled(flg);
    }

    public void updateDonCat(DonationCat donCat) {
        donCatListModel.updateItem(donCat);
    }

    public void setCollectionDate(Date dt) {
        for (int idx = 0; idx < baseCatL.size(); idx++) {
            DonPane pane = (DonPane) getCenterPane().getComponent(idx);
            pane.setCollectionDate(dt);
        }
        collDate = dt;
    }

    public void setDonations(DonationCat donCat, List<Donation> donL) {
        int idx = baseCatL.indexOf(donCat);
        if (idx >= 0) {
            DonPane pane = (DonPane) getCenterPane().getComponent(idx);
            pane.setDonations(donL);
        }
    }

    private CardLayout getCardLayout() {
        if (cardLayout == null) {
            cardLayout = new CardLayout();
        }
        return cardLayout;
    }

    private JPanel getCenterPane() {
        if (centerPane == null) {
            centerPane = new JPanel(getCardLayout());
        }
        return centerPane;
    }

    private JPanel getNorthPane() {
        if (northPane == null) {
            northPane = new JPanel();
            northPane.setLayout(new BorderLayout());
            JPanel pan = new JPanel();
            pan.add(new JLabel("Donation Category "));
            pan.add(getDonCatCB());
            northPane.add(getMyToolBar(), BorderLayout.NORTH);
            northPane.add(pan, BorderLayout.CENTER);
        }
        return northPane;
    }

    JComboBox getDonCatCB() {
        if (donCatCB == null) {
            donCatCB = new JComboBox(donCatListModel = new _catListModel());
        }
        return donCatCB;
    }

    public Action getAddRecAction() {
        if (addRecA == null) {
            addRecA = new _addRecAction(uiResources.getIcon(_ADD_ROW_ICON));
        }
        return addRecA;
    }

    Action getUpdRecAction() {
        if (updRecA == null) {
            updRecA = new _updRecAction(uiResources.getIcon(_UPD_ROW_ICON));
        }
        return updRecA;
    }

    public Action getEditSubCatAction() {
        if (editSubCatA == null) {
            editSubCatA = new _editSubCatAction(null);
            editSubCatA.setEnabled(false);
        }
        return editSubCatA;
    }

    public Action getSaveAction() {
        if (saveA == null) {
            saveA = new _saveAction(uiResources.getIcon(_SAVE_ICON));
            saveA.setEnabled(false);
        }
        return saveA;
    }

    public Action getClearAction() {
        if (clearRecA == null) {
            clearRecA = new _clrRecAction(uiResources.getIcon(_CLEAR_ICON));
        }
        return clearRecA;
    }

    public Action getExitAction() {
        if (exitA == null) {
            exitA = new _exitAction(uiResources.getIcon(_EXIT_ICON));
        }
        return exitA;
    }

    public Action getDelRecAction() {
        if (delRecA == null) {
            delRecA = new _delRecAction(uiResources.getIcon(_REM_ROW_ICON));
        }
        return delRecA;
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
        }
        return jContentPane;
    }

    /**
	 * This method initializes myMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
    private JMenuBar getMyMenuBar() {
        if (myMenuBar == null) {
            myMenuBar = new JMenuBar();
            JMenu fileMn = new JMenu("File");
            myMenuBar.add(fileMn);
            JMenuItem saveMI = new JMenuItem(getSaveAction());
            fileMn.add(saveMI);
            fileMn.addSeparator();
            fileMn.add(new JMenuItem(getExitAction()));
            JMenu editMn = new JMenu("Edit");
            myMenuBar.add(editMn);
            editMn.add(new JMenuItem(getClearAction()));
            editMn.add(new JMenuItem(getAddRecAction()));
            editMn.add(new JMenuItem(getUpdRecAction()));
            editMn.add(new JMenuItem(getDelRecAction()));
            editMn.add(new JMenuItem(getEditSubCatAction()));
            printMn = new JMenu("Reports");
            printMn.add(new JMenuItem(new _rptMenuAction(new RptDesc("Control Sums", "_control_totals"))));
            myMenuBar.add(printMn);
        }
        return myMenuBar;
    }

    private JToolBar getMyToolBar() {
        JToolBar result = new JToolBar();
        result.setFloatable(false);
        result.add(getExitAction());
        result.add(getSaveAction());
        result.addSeparator();
        result.add(getClearAction());
        result.add(getAddRecAction());
        result.add(getDelRecAction());
        result.add(getUpdRecAction());
        return result;
    }

    private class _exitAction extends AbstractAction {

        public _exitAction(Icon icon) {
            super("eXit", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Exit Editor");
        }

        public void actionPerformed(ActionEvent arg0) {
            if (!canExit()) {
                return;
            }
            DonationsEditorImp.this.setVisible(false);
            DonationsEditorImp.this.control.closeNotify();
        }
    }

    private class _saveAction extends AbstractAction {

        public _saveAction(Icon icon) {
            super("Save", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Save Changes");
        }

        public void actionPerformed(ActionEvent arg0) {
            control.storeDonations();
        }
    }

    private class _editSubCatAction extends AbstractAction {

        public _editSubCatAction(Icon icon) {
            super("edit Categories ..", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }

        public void actionPerformed(ActionEvent arg0) {
            control.editDonCategories();
        }
    }

    private class _addRecAction extends AbstractAction {

        public _addRecAction(Icon icon) {
            super("Add record", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Add Record");
        }

        public void actionPerformed(ActionEvent arg0) {
            control.addPersDonation();
        }
    }

    private class _delRecAction extends AbstractAction {

        public _delRecAction(Icon icon) {
            super("Delete record", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Delete Selected Record");
        }

        public void actionPerformed(ActionEvent arg0) {
            control.deleteDonation();
        }
    }

    private class _updRecAction extends AbstractAction {

        public _updRecAction(Icon icon) {
            super("Update record", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Update Selected Record");
        }

        public void actionPerformed(ActionEvent e) {
            control.updateDonation();
        }
    }

    private class _clrRecAction extends AbstractAction {

        public _clrRecAction(Icon icon) {
            super("Clear record", icon);
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Clear Edit Record Fields");
        }

        public void actionPerformed(ActionEvent e) {
            clearDon();
            getUpdRecAction().setEnabled(false);
        }
    }

    private class _rptMenuAction extends AbstractAction {

        private RptDesc rptDesc;

        public _rptMenuAction(RptDesc desc) {
            super(desc.getMenuRptNm());
            rptDesc = desc;
        }

        public void actionPerformed(ActionEvent e) {
            control.printReport(rptDesc.getSrcRptFile());
        }
    }

    JLabel getCatLongNmL() {
        if (catLongNmL == null) {
            catLongNmL = new JLabel();
            catLongNmL.setLayout(null);
            catLongNmL.setText("Category Name");
            catLongNmL.setVisible(true);
            catLongNmL.setPreferredSize(new java.awt.Dimension(110, 14));
        }
        return catLongNmL;
    }

    /**
	 * This method initializes centerCtxPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getCenterCtxPane() {
        if (centerCtxPane == null) {
            centerCtxPane = new JPanel();
            centerCtxPane.setLayout(new BorderLayout());
            centerCtxPane.setSize(new Dimension(119, 79));
            centerCtxPane.add(getCatLongNmL(), BorderLayout.NORTH);
            centerCtxPane.add(getCenterPane(), BorderLayout.CENTER);
        }
        return centerCtxPane;
    }

    private boolean canExit() {
        boolean canExit = true;
        if (getSaveAction().isEnabled()) {
            int resp = JOptionPane.showConfirmDialog(DonationsEditorImp.this, "You have unsaved data. Select \"Yes\" to confirm you want to exit current view", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (resp == JOptionPane.NO_OPTION) {
                canExit = false;
            }
        }
        return canExit;
    }
}
