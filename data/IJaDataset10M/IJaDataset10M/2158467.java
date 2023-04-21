package blueprint4j.apps;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import blueprint4j.db.ConnectionPool;
import blueprint4j.db.DBTools;
import blueprint4j.db.Entity;
import blueprint4j.utils.Log;
import blueprint4j.utils.Settings;

public abstract class Application {

    private String name = null;

    private JFrame frame = null;

    private Authenticator authenticator = null;

    private AuthenticatedUser<? extends Entity> user = null;

    private void setLookAndFeel() {
        try {
            String lookandfeel = Settings.getString("gui." + name + ".look&feel", "Use System");
            if ("Default".equals(lookandfeel)) return;
            if ("Use System".equals(lookandfeel)) {
                Log.trace.out("Set Look and Feel", "System");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                Log.trace.out("Set Look and Feel", lookandfeel);
                UIManager.setLookAndFeel(lookandfeel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.critical.out(e);
            System.exit(-1);
        }
    }

    public AuthenticatedUser<? extends Entity> getUser() {
        return user;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public String getName() {
        return name;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void start(Image iconimage) {
        start(iconimage, JFrame.MAXIMIZED_BOTH);
    }

    public void start(Image iconimage, int frame_state) {
        try {
            name = this.getClass().getName();
            while (name.indexOf(".") > 0) {
                name = name.substring(name.indexOf(".") + 1);
            }
            if (!DBTools.hasDeafaultCP()) {
                DBTools.setConnectionPool(new ConnectionPool(ConnectionPool.DBTYPE_MYSQL, Settings.getString(name + ".db.host", "localhost"), Settings.getString(name + ".db.name", name), Settings.getString(name + ".db.path", "database"), Settings.getString(name + ".db.user", "sa"), Settings.getString(name + ".db.password", "")), DBTools.CONNECTIONPOOL_DEFAULT);
            }
            Log.trace.out("Set Look and Feel", "");
            setLookAndFeel();
            if (authenticator != null) {
                user = authenticate(authenticator);
                if (user == null) {
                    System.exit(0);
                }
            }
            frame = new JFrame(name);
            if (iconimage != null) {
                frame.setIconImage(iconimage);
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.getContentPane().setLayout(new BorderLayout());
            JPanel main = new JPanel();
            main.setLayout(new BorderLayout());
            main.setSize(new Dimension(1024, 1024));
            main.add(getNewInterface(frame));
            frame.getContentPane().add(main, BorderLayout.CENTER);
            if (frame_state == JFrame.NORMAL) {
                frame.pack();
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            JOptionPane.setRootFrame(frame);
        } catch (Exception e) {
            Log.critical.out(e);
        }
    }

    public abstract JComponent getNewInterface(JFrame frame);

    public static void touchJarFile(File file, Class super_class, Class cc[], Object co[]) throws Throwable {
        ZipFile zipfile = new ZipFile(file);
        java.util.Enumeration e = zipfile.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (entry.getName().endsWith(".class") && entry.getName().indexOf("$") == -1) {
                String classname = entry.getName().replaceAll("/", ".");
                Class cl = Class.forName(classname.substring(0, classname.length() - ".class".length()));
                if (cc != null && (super_class == null || (cl.getSuperclass() != null && cl.getSuperclass().getName().equals(super_class.getName())))) {
                    try {
                        cl.getConstructor(cc).newInstance(co);
                    } catch (Throwable nme) {
                        Log.trace.out("COULD NOT LOCATE CONSTRUCTOR", nme);
                    }
                }
            }
        }
    }

    /**
	 * This will load all jar files in the given directory
	 */
    public static void touchLibrary(Class super_class, String path) throws Throwable {
        File file = new File(path);
        File files[] = file.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.getName().endsWith(".jar");
            }
        });
        for (int i = 0; i < files.length; i++) {
            touchJarFile(files[i], super_class, null, null);
        }
    }

    private AuthenticatedUser<? extends Entity> authenticate(Authenticator authenticator) {
        LoginDialog dialog = new LoginDialog(authenticator);
        dialog.setVisible(true);
        return dialog.user;
    }

    class LoginDialog extends JDialog implements ActionListener {

        private static final long serialVersionUID = 1L;

        AuthenticatedUser<? extends Entity> user = null;

        Authenticator authenticator;

        JTextField username = new JTextField(15);

        JPasswordField password = new JPasswordField(15);

        JButton okButton = new JButton("Login");

        JButton cancelButton = new JButton("Cancel");

        LoginDialog(Authenticator authenticator) {
            this.authenticator = authenticator;
            setModal(true);
            build();
        }

        public void build() {
            JPanel namePassword = new JPanel(new GridBagLayout());
            namePassword.setBorder(BorderFactory.createTitledBorder("Credentials"));
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            g.anchor = GridBagConstraints.EAST;
            namePassword.add(new JLabel("Username"), g);
            g.gridy = 1;
            namePassword.add(new JLabel("Password"), g);
            g.gridx = 1;
            g.gridy = 0;
            g.anchor = GridBagConstraints.WEST;
            namePassword.add(username, g);
            g.gridy = 1;
            namePassword.add(password, g);
            JPanel buttons = new JPanel(new FlowLayout());
            buttons.add(cancelButton);
            buttons.add(okButton);
            cancelButton.addActionListener(this);
            okButton.addActionListener(this);
            okButton.setDefaultCapable(true);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(namePassword, BorderLayout.CENTER);
            panel.add(buttons, BorderLayout.SOUTH);
            getContentPane().add(panel);
            setTitle("Login");
            setResizable(false);
            pack();
            setLocationRelativeTo(null);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == cancelButton) {
                user = null;
                setVisible(false);
            } else {
                user = authenticator.authenticateUser(username.getText(), new String(password.getPassword()));
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Login failed", "Failed!", JOptionPane.INFORMATION_MESSAGE);
                    password.setText(null);
                } else {
                    setVisible(false);
                }
            }
        }
    }
}
