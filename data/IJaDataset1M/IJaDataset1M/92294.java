package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginGUI extends JFrame {

    /**
	 * Klasse fuer den Login<br>
	 * wird nach dem Screensplash aufgrufen<br>
	 * nutzt den SHA1 Hash fuer Passw&ouml;rter<br>
	 * <br>
	 */
    private JPanel jpLogin = new JPanel(new GridLayout(0, 2));

    private JPanel mainPanel = new JPanel(new BorderLayout());

    private JPanel buttonPanel = new JPanel();

    private JPanel buttonLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JPanel buttonRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    private JPanel pwPanel = new JPanel(new BorderLayout());

    private JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private JPanel textfeldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JLabel jlAcc = new JLabel(" Benutzername:");

    private JLabel jlPW = new JLabel(" Passwort:");

    private JLabel lInfo = new JLabel("Bitte geben Sie ihr neues Passwort ein! ");

    private JLabel lPasswort = new JLabel("Neues Passwort");

    private JTextField jtAcc = new JTextField();

    private JPasswordField jtPW = new JPasswordField();

    private JPasswordField pwField = new JPasswordField(16);

    private JButton jbLogin = new JButton("Login");

    private JButton jbAbbrechen = new JButton("Abrechen");

    private JButton jbchpw = new JButton("Passwort aendern");

    private int w, h, x, y;

    private static java.net.URL iconURL = Adressverwaltung.class.getResource("Buddy.png");

    public LoginGUI() {
        setTitle("Benutzer Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        checkiftableexists();
        add(mainPanel);
        mainPanel.add(jpLogin, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new FlowLayout());
        jpLogin.add(jlAcc);
        jpLogin.add(jtAcc);
        jpLogin.add(jlPW);
        jpLogin.add(jtPW);
        buttonPanel.add(buttonLeft);
        buttonPanel.add(buttonRight);
        buttonLeft.add(jbchpw);
        buttonRight.add(jbLogin);
        buttonRight.add(jbAbbrechen);
        assignIcon();
        pack();
        w = this.getWidth();
        h = this.getHeight();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        x = (d.width - w) / 2;
        y = (d.height - h) / 2;
        setLocation(x, y);
        setResizable(false);
        setVisible(true);
        jbLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loginProzedur();
            }
        });
        jbchpw.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                changepwDialog();
            }
        });
        jbAbbrechen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        jtPW.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loginProzedur();
            }
        });
        if (!checkUserexist()) {
            JOptionPane.showMessageDialog(null, "Keine User vorhanden,\n" + "Bitte geben sie ihre favorisierten Logindaten ein" + "\nund klicken sie auf Login.");
        }
    }

    /** Die Methode assignIcon ersetzt das Standard-Java Icon in den JFrames durch ein selber erstelltes Icon */
    public void assignIcon() {
        Image img = getToolkit().getImage(iconURL);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 0);
        setIconImage(img);
    }

    /** Methode ueberprueft mittels der Methode checkUserexist ob bereits
	 * ein User in der Datenbaktabelle Login befindet und oeffnet je nach Lage
	 * die verschiedenen JOptionPane*/
    public void loginProzedur() {
        if (!checkUserexist()) {
            JOptionPane.showMessageDialog(null, "User wird angelegt!");
            createloginuser(jtAcc.getText(), jtPW.getPassword());
        }
        if (checkLogin(jtAcc.getText(), jtPW.getPassword())) {
            new Adressverwaltung();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Falscher Account/Passwort!");
        }
    }

    /** Methode generiert die GUI fuer Passwort aendern */
    public void changepwDialog() {
        if (jtAcc.getText().length() < 1 || jtPW.getPassword().length < 1) {
            JOptionPane.showMessageDialog(null, "Keine Logindaten vorhanden");
        } else {
            lInfo.setFont(new Font("Arial", Font.BOLD, 16));
            pwPanel.add(infoPanel, BorderLayout.NORTH);
            pwPanel.add(textfeldPanel, BorderLayout.CENTER);
            infoPanel.add(lInfo);
            textfeldPanel.add(lPasswort);
            textfeldPanel.add(pwField);
            if (JOptionPane.showConfirmDialog(null, pwPanel, "Aendern?", 2) == 0 && new String(pwField.getPassword()).length() > 5) {
                if (!changepw(jtAcc.getText(), jtPW.getPassword(), pwField.getPassword())) {
                    JOptionPane.showMessageDialog(null, "Ihre alten Login-Daten waren falsch (Account oder Passwort).");
                }
            } else {
                if (new String(pwField.getPassword()).length() < 6) {
                    JOptionPane.showMessageDialog(null, "Zu kurzes Passwort. Mindestens 6 Zeichen");
                }
            }
        }
    }

    /** Methode gibt true oder false fuer falsches bzw richtiges Passwort zurueck
	 * (Empfang aus Passwortfeld als char-Array!) */
    public boolean changepw(String sACC, char[] cs, char[] ncs) {
        String nPW = new String(ncs);
        if (checkLogin(jtAcc.getText(), cs)) {
            String sqlStatement = "UPDATE login SET password='" + Mbt.verfiyforsql(getSHA1(nPW)) + "' WHERE login='" + sACC + "';";
            try {
                DataManager.getInstance().execute(sqlStatement);
            } catch (SQLException e) {
            }
            return true;
        } else {
            return false;
        }
    }

    /** Methode ueberprueft die Logindaten aus der Datenbank mit den
	 * vom Benuter eingebenen Logindaten */
    public boolean checkLogin(String sACC, char[] cs) {
        String sPW = new String(cs);
        sPW = getSHA1(sPW);
        String sSQLPW = "";
        String SQLString = "Select * from login WHERE login='" + Mbt.verfiyforsql(sACC) + "';";
        try {
            ResultSet rset = DataManager.getInstance().select(SQLString);
            while (rset.next()) {
                sSQLPW = rset.getString("password");
            }
            rset.close();
        } catch (SQLException e) {
        }
        if (sSQLPW.equals(sPW) && sSQLPW.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /** Methode legt eine neue Tabelle Login in der Datenbank an */
    public void checkiftableexists() {
        try {
            DataManager.getInstance().execute("CREATE TABLE IF NOT EXISTS [login] ([ID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, [login] TEXT  NULL,[password] TEXT  NULL);");
        } catch (SQLException e) {
        }
    }

    /** Methode liefert den SHA-1 String als Hash zurueck */
    public String getSHA1(String sString) {
        if (sString.length() > 0) {
            try {
                sString = SHA1class.SHA1(sString);
            } catch (NoSuchAlgorithmException e) {
            } catch (UnsupportedEncodingException e) {
            }
        } else {
            sString = "";
        }
        return sString;
    }

    /** Methode ueberprueft ob bereits ein User als Benutzer in der 
	 * Login-Tabelle eingetragen ist und liefert booleschen Wert zurueck */
    public boolean checkUserexist() {
        int iusercount = -1;
        String SQLString = "Select COUNT(*) AS userzahl  from login";
        try {
            ResultSet rset = DataManager.getInstance().select(SQLString);
            while (rset.next()) {
                iusercount = rset.getInt("userzahl");
            }
            rset.close();
        } catch (SQLException e) {
        }
        if (iusercount < 1) {
            return false;
        } else {
            return true;
        }
    }

    /** Methode schreibt den neuen Benutzer mit seinen Logindaten
	 * in die Datenbank */
    public void createloginuser(String sACC, char[] cs) {
        String sPW = new String(cs);
        String sPPW = new String(cs);
        sPW = getSHA1(sPW);
        if (sACC.length() > 1 && sPPW.length() > 1) {
            String sqlStatement = "INSERT INTO login (login,password) VALUES (" + "'" + Mbt.verfiyforsql(sACC) + "'," + "'" + Mbt.verfiyforsql(sPW) + "'" + ");";
            try {
                DataManager.getInstance().execute(sqlStatement);
            } catch (SQLException e) {
            }
        }
    }
}
