package uk.org.beton.ftpsync.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import uk.org.beton.ftpsync.FtpSync;
import uk.org.beton.ftpsync.model.Account;
import uk.org.beton.ftpsync.model.AccountStore;
import uk.org.beton.ftpsync.model.Fileset;
import uk.org.beton.ftpsync.model.Message;
import uk.org.beton.ftpsync.model.UserOptions;
import uk.org.beton.util.gui.LoginDialog;

/**
 * This is the GUI main frame.
 * 
 * @author Rick Beton
 * @version $Id: MainFrame.java 109 2008-09-24 23:52:43Z rickbeton $
 */
final class MainFrame extends JFrame {

    protected static final String ACCOUNTS_FILE = "accounts.file";

    protected static final String SELECTED_ACCOUNT = "selected.account";

    protected static final String ROOT_DIR = ".root.dir";

    protected static final String BASE_DIR = ".base.dir";

    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("uk.org.beton.ftpsync.gui.Gui");

    private static final long serialVersionUID = 4452519907053943651L;

    private final Properties rcProperties;

    private final JMenuBar menuBar = new JMenuBar();

    private final JMenu fileMenu = new JMenu();

    private final JMenu editMenu = new JMenu();

    private final JMenuItem newItem = new JMenuItem();

    private final JMenuItem openItem = new JMenuItem();

    private final JMenuItem saveItem = new JMenuItem();

    private final JMenuItem saveAsItem = new JMenuItem();

    private final JMenuItem exitItem = new JMenuItem();

    private final JMenuItem accountsItem = new JMenuItem();

    private final JFileChooser fileChooser = new JFileChooser();

    private final OptionForm optionForm = new OptionForm(this);

    private final CredentialCache credentialCache = new CredentialCache(new LoginDialog(this));

    private AccountFrame accountFrame;

    private AccountStore accountStore;

    public MainFrame(Properties rcProperties) {
        this.rcProperties = rcProperties;
        setUserMadeChanges(false);
        setJMenuBar(menuBar);
        optionForm.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(optionForm, BorderLayout.CENTER);
        createMenus();
        addCallbacks();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {
        if (rcProperties.containsKey(ACCOUNTS_FILE)) {
            final File file = new File(rcProperties.getProperty(ACCOUNTS_FILE));
            fileChooser.setSelectedFile(file);
            if (file.exists()) {
                openFile(file);
            }
        } else {
            fileChooser.setSelectedFile(new File("ftp-accounts.properties"));
        }
    }

    private void createMenus() {
        Gui.configureButton(fileMenu, "fileMenu");
        Gui.configureButton(editMenu, "editMenu");
        Gui.configureButton(newItem, "newItem");
        Gui.configureButton(openItem, "openItem");
        Gui.configureButton(saveItem, "saveItem");
        Gui.configureButton(saveAsItem, "saveAsItem");
        Gui.configureButton(accountsItem, "accountsItem");
        Gui.configureButton(exitItem, "exitItem");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        saveItem.setEnabled(false);
        saveAsItem.setEnabled(false);
        editMenu.add(accountsItem);
        editMenu.setEnabled(false);
    }

    private void addCallbacks() {
        newItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final File file = new File(System.getProperty("user.home") + "/ftp-accounts.properties");
                fileChooser.setSelectedFile(file);
                accountStore = new AccountStore();
                optionForm.setAccountStore(file, accountStore);
                editMenu.setEnabled(true);
                accountFrame = new AccountFrame(MainFrame.this, accountStore);
                saveItem.setEnabled(true);
                saveAsItem.setEnabled(true);
            }
        });
        openItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    openSelectedFile();
                }
            }
        });
        saveItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final File file = fileChooser.getSelectedFile();
                saveFile(file);
            }
        });
        saveAsItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    final File file = fileChooser.getSelectedFile();
                    saveFile(file);
                }
            }
        });
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        accountsItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showAccountFrame();
            }
        });
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Gui.saveRC(rcProperties);
            }
        });
    }

    void showAccountFrame() {
        if (accountStore != null) {
            if (accountFrame == null) {
                accountFrame = new AccountFrame(this, accountStore);
            }
            accountFrame.setVisible(true);
        }
    }

    private void openSelectedFile() {
        final File file = fileChooser.getSelectedFile();
        openFile(file);
    }

    private void openFile(final File file) {
        try {
            accountStore = new AccountStore(file);
            accountFrame = null;
            rcProperties.put(ACCOUNTS_FILE, file.getPath().replace('\\', '/'));
            optionForm.setAccountStore(file, accountStore);
            editMenu.setEnabled(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read " + file, "File IO Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void saveFile(final File file) {
        try {
            accountStore.save(file);
            accountFrame.setUserMadeChanges(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read " + file, "File IO Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Gets the rcProperties property.
     * 
     * @return the rcProperties
     */
    public Properties getRcProperties() {
        return rcProperties;
    }

    /**
     * Gets the accountStore property.
     * 
     * @return the accountStore
     */
    public AccountStore getAccountStore() {
        return accountStore;
    }

    public void sync(String selectedAccountName, String selectedFilesetName, UserOptions opts) {
        final Account account = accountStore.getAccount(selectedAccountName);
        final Fileset fileset = accountStore.getFileset(selectedFilesetName);
        if (account != null && fileset != null) {
            opts.setAccount(account);
            opts.setFileset(fileset);
            final String baseDirStr = determineBaseDir(account);
            if (baseDirStr == null) {
                return;
            }
            credentialCache.getCredentials(opts);
            if (credentialCache.getLoginDialog().isCancelled()) {
                return;
            }
            opts.setBaseDir(baseDirStr);
            opts.setThreads(1);
            opts.setInfoLevel(Message.VERBOSE);
            rcProperties.put(SELECTED_ACCOUNT, selectedAccountName);
            rcProperties.put(selectedAccountName + BASE_DIR, opts.getBaseDir());
            final OutputFrame output = new OutputFrame(this, opts);
            new FtpSync(opts, output.getReporter());
        }
    }

    public String determineBaseDir(final Account account) {
        String baseDirStr = rcProperties.getProperty(account.getName() + ROOT_DIR);
        File baseDir = baseDirStr != null ? new File(baseDirStr) : null;
        while (baseDir == null || !baseDir.exists()) {
            final JFileChooser fc = new JFileChooser();
            final String prompt = Gui.getResourceFormat("chooseAcctDir.prompt", new Object[] { account.getName() });
            JOptionPane.showMessageDialog(this, Gui.getResourceString("chooseAcctDir.info"), prompt, JOptionPane.INFORMATION_MESSAGE);
            fc.setDialogTitle(prompt);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            final int r = fc.showOpenDialog(this);
            {
                if (r == JFileChooser.APPROVE_OPTION) {
                    baseDir = fc.getSelectedFile();
                    baseDirStr = baseDir.getPath().replace('\\', '/');
                    rcProperties.setProperty(account.getName() + ROOT_DIR, baseDirStr);
                } else if (r == JFileChooser.CANCEL_OPTION) {
                    return null;
                }
            }
        }
        return baseDirStr + '/';
    }

    /**
     * Sets the userMadeChanges property.
     * 
     * @param userMadeChanges the userMadeChanges to set
     */
    public void setUserMadeChanges(boolean userMadeChanges) {
        final String suffix = userMadeChanges ? " *" : "";
        setTitle(RESOURCES.getString("Gui.title") + " v" + FtpSync.VERSION + suffix);
        saveItem.setEnabled(userMadeChanges);
        saveAsItem.setEnabled(userMadeChanges);
    }
}
