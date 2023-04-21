package OpenStreetPM;

import java.awt.Toolkit;

public class RunApp extends javax.swing.JFrame {

    /** Creates new form RunApp */
    public RunApp() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        MenuBar_Main = new javax.swing.JMenuBar();
        Menu_File = new javax.swing.JMenu();
        Menu_File_Open = new javax.swing.JMenuItem();
        Separator_Menu_File_Separator1 = new javax.swing.JSeparator();
        Menu_File_Save = new javax.swing.JMenuItem();
        Menu_File_SaveAs = new javax.swing.JMenuItem();
        Separator_Menu_File_Separator2 = new javax.swing.JSeparator();
        Menu_File_Exit = new javax.swing.JMenuItem();
        Menu_Edit = new javax.swing.JMenu();
        Menu_Account = new javax.swing.JMenu();
        Menu_Account_Login = new javax.swing.JMenuItem();
        Menu_Account_Logout = new javax.swing.JMenuItem();
        Separator_Menu_Account_Separator1 = new javax.swing.JSeparator();
        Menu_Account_Manage = new javax.swing.JMenuItem();
        Menu_Account_Open = new javax.swing.JMenuItem();
        Menu_Help = new javax.swing.JMenu();
        Menu_Help_HelpContents = new javax.swing.JMenuItem();
        Separator_Menu_Help_Separator1 = new javax.swing.JSeparator();
        Menu_Help_Privacy = new javax.swing.JMenuItem();
        Separator_Menu_Help_Separator2 = new javax.swing.JSeparator();
        Menu_Help_About = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OpenStreet Portfolio Manager");
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Images/OpenStreetIcon.gif")));
        Menu_File.setText("File");
        Menu_File.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_File_Open.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_File_Open.setText("Open");
        Menu_File.add(Menu_File_Open);
        Menu_File.add(Separator_Menu_File_Separator1);
        Menu_File_Save.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_File_Save.setText("Save");
        Menu_File.add(Menu_File_Save);
        Menu_File_SaveAs.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_File_SaveAs.setText("Save As");
        Menu_File.add(Menu_File_SaveAs);
        Menu_File.add(Separator_Menu_File_Separator2);
        Menu_File_Exit.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_File_Exit.setText("Exit");
        Menu_File_Exit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_File_ExitActionPerformed(evt);
            }
        });
        Menu_File.add(Menu_File_Exit);
        MenuBar_Main.add(Menu_File);
        Menu_Edit.setText("Edit");
        Menu_Edit.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        MenuBar_Main.add(Menu_Edit);
        Menu_Account.setText("Account");
        Menu_Account.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Account_Login.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Account_Login.setText("Login");
        Menu_Account.add(Menu_Account_Login);
        Menu_Account_Logout.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Account_Logout.setText("Logout");
        Menu_Account.add(Menu_Account_Logout);
        Menu_Account.add(Separator_Menu_Account_Separator1);
        Menu_Account_Manage.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Account_Manage.setText("Manage");
        Menu_Account.add(Menu_Account_Manage);
        Menu_Account_Open.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Account_Open.setText("Open");
        Menu_Account_Open.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Account_OpenActionPerformed(evt);
            }
        });
        Menu_Account.add(Menu_Account_Open);
        MenuBar_Main.add(Menu_Account);
        Menu_Help.setText("Help");
        Menu_Help.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Help_HelpContents.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Help_HelpContents.setText("Help Contents");
        Menu_Help_HelpContents.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Help_HelpContentsActionPerformed(evt);
            }
        });
        Menu_Help.add(Menu_Help_HelpContents);
        Menu_Help.add(Separator_Menu_Help_Separator1);
        Menu_Help_Privacy.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Help_Privacy.setText("Privacy");
        Menu_Help.add(Menu_Help_Privacy);
        Menu_Help.add(Separator_Menu_Help_Separator2);
        Menu_Help_About.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        Menu_Help_About.setText("About");
        Menu_Help_About.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Help_AboutActionPerformed(evt);
            }
        });
        Menu_Help.add(Menu_Help_About);
        MenuBar_Main.add(Menu_Help);
        setJMenuBar(MenuBar_Main);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 911, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 510, Short.MAX_VALUE));
        getAccessibleContext().setAccessibleParent(this);
        setBounds(100, 100, 921, 559);
    }

    private void Menu_File_ExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void Menu_Help_AboutActionPerformed(java.awt.event.ActionEvent evt) {
        new AboutBox(this, true).setVisible(true);
    }

    private void Menu_Help_HelpContentsActionPerformed(java.awt.event.ActionEvent evt) {
        BareBonesBrowserLaunch.openURL("http://openstreet.org");
    }

    private void Menu_Account_OpenActionPerformed(java.awt.event.ActionEvent evt) {
        new OpenAccount(this, true).setVisible(true);
        if (OpenAccount.ContinueAccountOpen) {
            new OpenAccountBasicInfo(this, true).setVisible(true);
            while (OpenAccountBasicInfo.EmptyData & OpenAccountBasicInfo.ContinueAccountOpen) {
                new MissingData(this, true).setVisible(true);
                new OpenAccountBasicInfo(this, true).setVisible(true);
            }
            if (OpenAccountBasicInfo.ContinueAccountOpen) {
                new OpenAccountPassword(this, true).setVisible(true);
                while (!OpenAccountPassword.Match & OpenAccountPassword.ContinueAccountOpen) {
                    new PasswordsNoMatch(this, true).setVisible(true);
                    new OpenAccountPassword(this, true).setVisible(true);
                }
                if (OpenAccountPassword.ContinueAccountOpen) {
                    new OpenAccountGetConfirm(this, true).setVisible(true);
                }
                if (OpenAccountGetConfirm.ContinueAccountOpen) {
                }
            }
        }
    }

    /** Provides main function
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RunApp();
            }
        });
    }

    private javax.swing.JMenuBar MenuBar_Main;

    private javax.swing.JMenu Menu_Account;

    private javax.swing.JMenuItem Menu_Account_Login;

    private javax.swing.JMenuItem Menu_Account_Logout;

    private javax.swing.JMenuItem Menu_Account_Manage;

    private javax.swing.JMenuItem Menu_Account_Open;

    private javax.swing.JMenu Menu_Edit;

    private javax.swing.JMenu Menu_File;

    private javax.swing.JMenuItem Menu_File_Exit;

    private javax.swing.JMenuItem Menu_File_Open;

    private javax.swing.JMenuItem Menu_File_Save;

    private javax.swing.JMenuItem Menu_File_SaveAs;

    private javax.swing.JMenu Menu_Help;

    private javax.swing.JMenuItem Menu_Help_About;

    private javax.swing.JMenuItem Menu_Help_HelpContents;

    private javax.swing.JMenuItem Menu_Help_Privacy;

    private javax.swing.JSeparator Separator_Menu_Account_Separator1;

    private javax.swing.JSeparator Separator_Menu_File_Separator1;

    private javax.swing.JSeparator Separator_Menu_File_Separator2;

    private javax.swing.JSeparator Separator_Menu_Help_Separator1;

    private javax.swing.JSeparator Separator_Menu_Help_Separator2;
}
