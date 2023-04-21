package Windows;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.net.ftp.*;
import Main.Config;
import Main.SqlQuerry;
import Main.Window;

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
public class User extends javax.swing.JInternalFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JTextField AnzeigeName;

    private JTextField Nachname;

    private JLabel GeburtstagText;

    private JLabel NachnameText;

    private JButton Refresh;

    private JButton Upload;

    private JLabel Pic;

    private JPanel Bild;

    private JTextField Wohnort;

    private JLabel WohnortText;

    public static JLabel UserStatus;

    private JButton Update;

    private JLabel VornameText;

    private JTextField Year;

    private JComboBox Month;

    private JComboBox Day;

    private JTextField Vorname;

    private JLabel AnzeigeNameText;

    private JPanel Profil;

    private JTabbedPane jTabbedPane1;

    static SqlQuerry userDetails;

    static Icon bild;

    {
        userDetails = new SqlQuerry("getUser", "SELECT * FROM applet.user WHERE id = " + Config.id);
    }

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	* Auto-generated main method to display this 
	* JInternalFrame inside a new JFrame.
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        User inst = new User();
        JDesktopPane jdp = new JDesktopPane();
        jdp.add(inst);
        jdp.setPreferredSize(inst.getPreferredSize());
        frame.setContentPane(jdp);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public User() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            {
                Upload = new JButton();
                Upload.setText("hochladen");
                Upload.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        UploadActionPerformed(evt);
                    }
                });
            }
            setPreferredSize(new Dimension(400, 300));
            this.setBounds(0, 0, 400, 300);
            setVisible(true);
            this.setFrameIcon(new ImageIcon(getClass().getClassLoader().getResource("img/user.png")));
            this.setTitle("User");
            this.setResizable(true);
            this.setName("User");
            this.setIconifiable(true);
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setMaximizable(true);
            {
                jTabbedPane1 = new JTabbedPane();
                {
                    Profil = new JPanel();
                    GroupLayout ProfilLayout = new GroupLayout((JComponent) Profil);
                    Profil.setLayout(ProfilLayout);
                    jTabbedPane1.addTab("Profil", null, Profil, null);
                    Profil.setBackground(new java.awt.Color(255, 255, 255));
                    Profil.setPreferredSize(new java.awt.Dimension(385, 204));
                    {
                        AnzeigeNameText = new JLabel();
                        AnzeigeNameText.setText("Anzeigename:");
                    }
                    {
                        WohnortText = new JLabel();
                        WohnortText.setText("Wohnort:");
                    }
                    {
                        Wohnort = new JTextField();
                    }
                    {
                        AnzeigeName = new JTextField();
                    }
                    {
                        Vorname = new JTextField();
                    }
                    {
                        Nachname = new JTextField();
                    }
                    {
                        ComboBoxModel DayModel = new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" });
                        Day = new JComboBox();
                        Day.setModel(DayModel);
                    }
                    {
                        ComboBoxModel MonthModel = new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
                        Month = new JComboBox();
                        Month.setModel(MonthModel);
                    }
                    {
                        Year = new JTextField();
                    }
                    {
                        VornameText = new JLabel();
                        VornameText.setText("Vorname:");
                    }
                    {
                        NachnameText = new JLabel();
                        NachnameText.setText("Nachname:");
                    }
                    {
                        GeburtstagText = new JLabel();
                        GeburtstagText.setText("Geburtstag:");
                    }
                    {
                        AnzeigeName.setText(userDetails.anzeigeName);
                        Vorname.setText(userDetails.vorname);
                        Nachname.setText(userDetails.nachname);
                        Day.setSelectedItem(userDetails.day);
                        Month.setSelectedItem(userDetails.month);
                        Year.setText(userDetails.year);
                        Wohnort.setText(userDetails.wohnort);
                    }
                    ProfilLayout.setHorizontalGroup(ProfilLayout.createSequentialGroup().addContainerGap().addGroup(ProfilLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, ProfilLayout.createSequentialGroup().addComponent(GeburtstagText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(10)).addGroup(GroupLayout.Alignment.LEADING, ProfilLayout.createSequentialGroup().addComponent(NachnameText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(14)).addGroup(GroupLayout.Alignment.LEADING, ProfilLayout.createSequentialGroup().addComponent(VornameText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(22)).addComponent(AnzeigeNameText, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(WohnortText, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(ProfilLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, ProfilLayout.createSequentialGroup().addComponent(Day, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(Month, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(Year, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE).addGap(0, 85, Short.MAX_VALUE)).addComponent(AnzeigeName, GroupLayout.Alignment.LEADING, 0, 293, Short.MAX_VALUE).addComponent(Vorname, GroupLayout.Alignment.LEADING, 0, 293, Short.MAX_VALUE).addComponent(Nachname, GroupLayout.Alignment.LEADING, 0, 293, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, ProfilLayout.createSequentialGroup().addComponent(Wohnort, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE).addGap(0, 85, Short.MAX_VALUE))).addContainerGap());
                    ProfilLayout.setVerticalGroup(ProfilLayout.createSequentialGroup().addContainerGap().addGroup(ProfilLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(AnzeigeName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(AnzeigeNameText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(ProfilLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(Vorname, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(VornameText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(ProfilLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(Nachname, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(NachnameText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(ProfilLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(Day, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(Month, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(Year, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE).addComponent(GeburtstagText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(ProfilLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(Wohnort, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(WohnortText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(47, 47));
                }
                {
                    Bild = new JPanel();
                    GroupLayout BildLayout = new GroupLayout((JComponent) Bild);
                    Bild.setLayout(BildLayout);
                    jTabbedPane1.addTab("Bild", null, Bild, null);
                    Bild.setBackground(new java.awt.Color(255, 255, 255));
                    {
                        Pic = new JLabel();
                        Pic.setIcon(new ImageIcon(new URL("http://showus.de/Applet/user/" + Config.id + ".jpg")));
                        Pic.setText("kein Pic");
                        Pic.setSize(70, 95);
                    }
                    {
                        Refresh = new JButton();
                        Refresh.setText("aktuallisieren");
                        Refresh.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                RefreshActionPerformed(evt);
                            }
                        });
                    }
                    BildLayout.setHorizontalGroup(BildLayout.createSequentialGroup().addContainerGap().addComponent(Pic, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(Upload, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(Refresh, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE).addContainerGap(106, Short.MAX_VALUE));
                    BildLayout.setVerticalGroup(BildLayout.createSequentialGroup().addContainerGap().addGroup(BildLayout.createParallelGroup().addGroup(BildLayout.createSequentialGroup().addComponent(Pic, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, BildLayout.createSequentialGroup().addComponent(Upload, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(0, 52, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, BildLayout.createSequentialGroup().addComponent(Refresh, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addGap(0, 52, Short.MAX_VALUE))).addContainerGap(111, 111));
                }
            }
            {
                Update = new JButton();
                Update.setText("update");
                Update.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        UpdateActionPerformed(evt);
                    }
                });
            }
            {
                UserStatus = new JLabel();
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addComponent(jTabbedPane1, 0, 225, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(thisLayout.createParallelGroup().addComponent(Update, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(UserStatus, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(Update, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(UserStatus, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE).addGap(0, 18, Short.MAX_VALUE)).addComponent(jTabbedPane1, GroupLayout.Alignment.LEADING, 0, 390, Short.MAX_VALUE));
            this.addInternalFrameListener(new InternalFrameAdapter() {

                public void internalFrameClosing(InternalFrameEvent evt) {
                    thisInternalFrameClosing(evt);
                }

                public void internalFrameClosed(InternalFrameEvent evt) {
                    thisInternalFrameClosed(evt);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void thisInternalFrameClosed(InternalFrameEvent evt) {
        Window.userStat = false;
    }

    private void thisInternalFrameClosing(InternalFrameEvent evt) {
        Window.userStat = false;
    }

    private void UpdateActionPerformed(ActionEvent evt) {
        @SuppressWarnings("unused") SqlQuerry userUpdate = new SqlQuerry("setUser", "UPDATE applet.user SET vorname='" + Vorname.getText() + "', name='" + AnzeigeName.getText() + "', nachname = '" + Nachname.getText() + "' , geburtstag = '" + Day.getSelectedItem() + Month.getSelectedItem() + Year.getText() + "' , wohnort = '" + Wohnort.getText() + "' WHERE id = " + Config.id);
    }

    private void UploadActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpg", "jpg");
        chooser.setFileFilter(filter);
        File dir = new File(System.getProperty("user.home"));
        chooser.setCurrentDirectory(dir);
        Component parent = null;
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            UserStatus.setText("Bitte warten");
        }
        try {
            Pic.setVisible(false);
            FTPClient client = new FTPClient();
            client.connect("showus.de");
            client.login("web2", "kcinnay88");
            client.enterLocalActiveMode();
            client.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            int reply = client.getReplyCode();
            System.out.println("Connect returned: " + reply);
            FileInputStream in = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("Uploading File");
            client.storeFile("/html/Applet/user/" + Config.id + ".jpg", in);
            client.logout();
            in.close();
            System.out.println("done");
            UserStatus.setText("Upload fertig, Bild wird aktuallisiert");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            UserStatus.setText("Fehler beim Upload");
            e.printStackTrace();
        }
    }

    private void RefreshActionPerformed(ActionEvent evt) {
        try {
            Pic.setIcon(new ImageIcon(new URL("http://showus.de/Applet/user/" + Config.id + ".jpg")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Pic.repaint();
        Pic.setVisible(true);
        this.repaint();
    }
}
