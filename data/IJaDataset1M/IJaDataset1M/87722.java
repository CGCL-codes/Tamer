package uk.org.beton.ftpsync.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import uk.org.beton.ftpsync.model.Account;
import uk.org.beton.ftpsync.model.AccountStore;
import uk.org.beton.ftpsync.model.Fileset;

/**
 * This is the accounts frame.
 *
 * @author Rick Beton
 * @version $Id: Gui.java 651 2007-03-26 20:40:42Z rick $
 */
final class FilesetFrame extends JFrame implements PropertyChangeListener {

    private static final long serialVersionUID = 4452519907053943651L;

    private final MainFrame gui;

    private final AccountStore accountStore;

    private final DefaultListModel accountListModel = new DefaultListModel();

    private final JList accountList = new JList(accountListModel);

    private final FilesetForm form = new FilesetForm(this);

    private final JButton addButton = new JButton();

    private final JButton delButton = new JButton();

    private final JButton upButton = new JButton();

    private final JButton dnButton = new JButton();

    private boolean userMadeChanges = false;

    public FilesetFrame(MainFrame gui, AccountStore store) {
        this.gui = gui;
        this.accountStore = store;
        accountStore.addPropertyChangeListener(this);
        setTitle(Gui.getResourceString("FilesetFrame.title"));
        layoutControls();
        Gui.configureButton(addButton, "addButton");
        Gui.configureButton(delButton, "delButton");
        Gui.configureButton(upButton, "upButton");
        Gui.configureButton(dnButton, "dnButton");
        delButton.setEnabled(false);
        upButton.setEnabled(false);
        dnButton.setEnabled(false);
        populateFilesetNameList(store);
        form.setFilesetNames(store.getFilesetNames());
        form.setFileset(store.getFileset(AccountStore.DEFAULT_ACCOUNT_NAME));
        addCallbacks();
        pack();
        setLocationRelativeTo(gui);
        setVisible(true);
    }

    private void populateFilesetNameList(AccountStore store) {
        final Iterator<String> it = store.getFilesetNames().iterator();
        while (it.hasNext()) {
            accountListModel.addElement(it.next());
        }
        accountList.setSelectedIndex(0);
    }

    private void layoutControls() {
        final JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new FormLayout("15dlu,3dlu,100dlu,3dlu,pref", "15dlu,3dlu,15dlu,3dlu,15dlu,3dlu,15dlu,3dlu,max(160dlu;pref):grow"));
        final CellConstraints cc = new CellConstraints();
        contentPane.add(addButton, cc.xy(1, 1));
        contentPane.add(delButton, cc.xy(1, 3));
        contentPane.add(upButton, cc.xy(1, 5));
        contentPane.add(dnButton, cc.xy(1, 7));
        contentPane.add(new JScrollPane(accountList), cc.xywh(3, 1, 1, 9));
        contentPane.add(form, cc.xywh(5, 1, 1, 9));
    }

    private void addCallbacks() {
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Fileset newFileset = new Fileset();
                int n = accountListModel.getSize();
                String newName = "new" + n;
                while (accountStore.containsFileset(newName)) {
                    n++;
                    newName = "new" + n;
                }
                newFileset.setName(newName);
                accountStore.addFileset(newFileset);
                accountListModel.addElement(newFileset.getName());
                accountList.setSelectedIndex(accountListModel.getSize() - 1);
                setUserMadeChanges(true);
            }
        });
        delButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final int[] sel = accountList.getSelectedIndices();
                if (sel.length > 0) {
                    accountList.getSelectionModel().clearSelection();
                    final Set<String> doomed = new HashSet<String>();
                    for (int i = sel.length - 1; i >= 0; i--) {
                        final int index = sel[i];
                        final String name = (String) accountListModel.remove(index);
                        doomed.add(name);
                    }
                    deleteFilesets(doomed);
                    setUserMadeChanges(true);
                }
            }
        });
        upButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final int[] sel = accountList.getSelectedIndices();
                if (sel.length == 1) {
                    int index = sel[0];
                    final String name = (String) accountListModel.get(index);
                    accountStore.promoteFileset(name);
                    accountListModel.set(index, accountStore.getFilesetName(index));
                    index--;
                    accountListModel.set(index, accountStore.getFilesetName(index));
                    accountList.setSelectedIndex(index);
                    setUserMadeChanges(true);
                }
            }
        });
        dnButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final int[] sel = accountList.getSelectedIndices();
                if (sel.length == 1) {
                    int index = sel[0];
                    final String name = (String) accountListModel.get(index);
                    accountStore.demoteFileset(name);
                    accountListModel.set(index, accountStore.getFilesetName(index));
                    index++;
                    accountListModel.set(index, accountStore.getFilesetName(index));
                    accountList.setSelectedIndex(index);
                    setUserMadeChanges(true);
                }
            }
        });
        accountList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    final int[] sel = accountList.getSelectedIndices();
                    if (sel.length > 0) {
                        boolean canDelete = true;
                        for (int i = 0; i < sel.length && canDelete; i++) {
                            if (accountListModel.get(sel[i]).equals(AccountStore.DEFAULT_ACCOUNT_NAME)) {
                                canDelete = false;
                            }
                        }
                        delButton.setEnabled(canDelete);
                    }
                    if (sel.length == 1) {
                        final int index = sel[0];
                        final String name = (String) accountListModel.get(index);
                        form.setFileset(accountStore.getFileset(name));
                        dnButton.setEnabled(0 < index && index < (accountListModel.getSize() - 1));
                        upButton.setEnabled(index > 1);
                    } else {
                        form.setFileset(null);
                        dnButton.setEnabled(false);
                        upButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void deleteFilesets(Collection<String> doomed) {
        final Fileset defaultFileset = accountStore.getFileset(AccountStore.DEFAULT_ACCOUNT_NAME);
        final Iterator<Fileset> it = accountStore.filesetIterator();
        while (it.hasNext()) {
            final Fileset account = it.next();
            if (doomed.contains(account.getName())) {
                it.remove();
            } else if (account.getParent() != null && doomed.contains(account.getParent().getName())) {
                account.setParent(defaultFileset);
            }
        }
    }

    /**
     * Gets the accountStore property.
     *
     * @return the accountStore
     */
    public AccountStore getAccountStore() {
        return accountStore;
    }

    /**
     * Gets the userMadeChanges property.
     *
     * @return the userMadeChanges
     */
    public boolean hasUserMadeChanges() {
        return userMadeChanges;
    }

    /**
     * Sets the userMadeChanges property.
     *
     * @param userMadeChanges the userMadeChanges to set
     */
    public void setUserMadeChanges(boolean userMadeChanges) {
        this.userMadeChanges = userMadeChanges;
        final String suffix = userMadeChanges ? " *" : "";
        setTitle(Gui.getResourceString("FilesetFrame.title") + suffix);
        gui.setUserMadeChanges(userMadeChanges);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        setUserMadeChanges(true);
        final String pName = evt.getPropertyName();
        if (pName.equals(Account.NAME_PROPERTY)) {
            final String oldName = (String) evt.getOldValue();
            final String newName = (String) evt.getNewValue();
            final int index = accountListModel.indexOf(oldName);
            accountListModel.set(index, newName);
        }
    }
}
