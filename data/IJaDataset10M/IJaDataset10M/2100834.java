package ossobook2010.gui.components.content.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.Content;
import ossobook2010.gui.frameworks.GridBagHelper;
import ossobook2010.gui.frameworks.StackLayout;
import ossobook2010.gui.stylesheet.Sizes;

/**
 * A screen where the user can login to a global or local database.
 * In general this screen is the start screen when OssoBook is started.
 * 
 * The user can enter a user name and the correct password. Furthermore
 * he can select if he want to connect to the global database or to the
 * local database.
 * 
 * @author Daniel Kaltenthaler
 */
public class NewUser extends JPanel implements ActionListener, KeyListener {

    /** The neccessary serial version ID. */
    private static final long serialVersionUID = -2438685890047460875L;

    /** The basic MainFrame object */
    private MainFrame mainFrame;

    /** The input object for the user name input. */
    private JTextField inputUser;

    /** The input object for the password input. */
    private JPasswordField inputPassword;

    /** The button object for the login. */
    private JButton buttonLogin;

    /** The button object for reset the textFields. */
    private JButton buttonCancel;

    /**
	 * Constructor of the Login class.
	 * Initializes the screen.
	 *
	 * @param mainFrame
	 *		The basic MainFrame object.
	 */
    public NewUser(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    /**
	 * Initializes the screen.
	 */
    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(50, 0, 0, 0));
        JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        JPanel basicContent = new JPanel(new StackLayout());
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        GridBagHelper.setConstraints(c, 0, 0);
        JLabel labelUser = new JLabel(Messages.getString("USER"));
        form.add(labelUser, c);
        GridBagHelper.setConstraints(c, 0, 1);
        inputUser = new JTextField();
        inputUser.setPreferredSize(new Dimension(300, Sizes.DEFAULT_BUTTON_HEIGHT));
        inputUser.addKeyListener(this);
        form.add(inputUser, c);
        GridBagHelper.setConstraints(c, 1, 0);
        form.add(new JLabel(Messages.getString("PASSWORD")), c);
        GridBagHelper.setConstraints(c, 1, 1);
        inputPassword = new JPasswordField();
        inputPassword.setPreferredSize(new Dimension(300, Sizes.DEFAULT_BUTTON_HEIGHT));
        inputPassword.addKeyListener(this);
        form.add(inputPassword, c);
        GridBagHelper.setConstraints(c, 3, 1);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        buttonLogin = new JButton(Messages.getString("ADD"));
        buttonLogin.addActionListener(this);
        buttonLogin.setPreferredSize(new Dimension(120, 40));
        p.add(buttonLogin);
        buttonCancel = new JButton(Messages.getString("CANCEL"));
        buttonCancel.addActionListener(this);
        buttonCancel.setPreferredSize(new Dimension(120, 40));
        p.add(buttonCancel);
        form.add(p, c);
        basicContent.add(BorderLayout.CENTER, form);
        sp.setViewportView(basicContent);
        add(BorderLayout.CENTER, sp);
    }

    private void addUser() {
        mainFrame.getController().addLocalUser(inputUser.getText(), String.valueOf(inputPassword.getPassword()));
        mainFrame.reloadGui(Content.Id.LOGIN);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonLogin) {
            addUser();
        } else if (event.getSource() == buttonCancel) {
            mainFrame.reloadGui(Content.Id.LOGIN);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            addUser();
        }
    }

    @Deprecated
    public void keyPressed(KeyEvent e) {
    }

    @Deprecated
    public void keyReleased(KeyEvent e) {
    }
}
