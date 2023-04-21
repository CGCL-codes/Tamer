package onekin.WSL.config;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import onekin.WSL.WSL_Util;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Original TextInputDemo.java
 * @author Gorka Puente García
 */
public class WSL_MediaWikiConfig extends JPanel implements ActionListener, FocusListener {

    private JTextField wikiName, email, host, dbName, dbUser, installationDir;

    private JPasswordField passwordField, confirmPasswordField;

    private JFileChooser chooser;

    private String chooserTitle;

    private JLabel addressDisplay;

    private static JFrame frame;

    static final int GAP = 10;

    private static WSL_ExistMWConfig existingMW;

    private String curDir = System.getProperty("user.dir");

    private WSL_Util wsl_util;

    private JButton button, installationDirButton;

    public WSL_MediaWikiConfig() {
        wsl_util = new WSL_Util();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        JPanel leftHalf = new JPanel() {

            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.PAGE_AXIS));
        leftHalf.add(createEntryFields());
        leftHalf.add(createButtons());
        add(leftHalf);
        add(createAddressDisplay());
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        button = new JButton("Download and install MW");
        button.addActionListener(this);
        panel.add(button);
        button = new JButton("Clear fields");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);
        installationDirButton = new JButton("Select dir");
        installationDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle(chooserTitle);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    installationDir.setText(chooser.getSelectedFile().toString());
                } else {
                    System.out.println("No Selection ");
                }
            }
        });
        panel.add(installationDirButton);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP - 5, GAP - 5));
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            button.setEnabled(false);
            installationDirButton.setEnabled(false);
            if ("clear".equals(e.getActionCommand())) {
                wikiName.setText("");
                email.setText("");
                host.setText("");
                dbName.setText("");
                dbUser.setText("");
                installationDir.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
            } else {
                if (checkFields()) {
                    addressDisplay.setText("<html><p align=center>Everything is OK</html></p>");
                    downloadMW();
                    installMW();
                    existingMW.setLocalSettings();
                    destroy();
                }
            }
        } finally {
            button.setEnabled(true);
            installationDirButton.setEnabled(true);
        }
    }

    public void destroy() {
        if (frame != null) {
            frame.dispose();
        }
    }

    private void installMW() {
        try {
            File file = new File(installationDir.getText() + "/" + wikiName.getText() + "/LocalSettings.php");
            wsl_util.copyFile(new File(curDir + "/plugins/WSL/resources/LocalSettings.php"), file);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", newText = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains("$wgScriptPath       =")) {
                    line = "$wgScriptPath = \"/" + wikiName.getText() + "\";";
                } else if (line.contains("$wgSitename         =")) {
                    line = "$wgSitename = \"" + wikiName.getText() + "\";";
                } else if (line.contains("$wgEmergencyContact =")) {
                    line = "$wgEmergencyContact = \"" + email.getText() + "\";";
                } else if (line.contains("$wgPasswordSender =")) {
                    line = "$wgPasswordSender = \"" + email.getText() + "\";";
                } else if (line.contains("$wgDBserver         =")) {
                    line = "$wgDBserver         = \"" + host.getText() + "\";";
                } else if (line.contains("$wgDBname           =")) {
                    line = "$wgDBname           = \"" + dbName.getText() + "\";";
                } else if (line.contains("$wgDBuser           =")) {
                    line = "$wgDBuser           = \"" + dbUser.getText() + "\";";
                } else if (line.contains("$wgDBpassword       =")) {
                    line = "$wgDBpassword       = \"" + new String(passwordField.getPassword()) + "\";";
                } else if (line.contains("$wgLogo             = \"$wgStylePath/common/images/wiki.png\";")) {
                    line = "#$wgLogo             = \"$wgStylePath/common/images/wiki.png\";";
                } else if (line.contains("$wgDefaultSkin = 'monobook';")) {
                    line = "#$wgDefaultSkin = 'monobook';";
                }
                newText = newText + line + "\n";
            }
            reader.close();
            FileWriter writer = new FileWriter(installationDir.getText() + "/" + wikiName.getText() + "/LocalSettings.php");
            writer.write(newText);
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        createDB();
    }

    /**
    * This method creates a new database in mysql, with MediaWiki tables
    * and with an admin user "WikiSysop"
    */
    private void createDB() {
        String command;
        Process child;
        int exitVal1 = -1, exitVal2 = -1, exitVal3 = -1;
        try {
            command = "mysql --user=" + dbUser.getText() + " --password=" + new String(passwordField.getPassword()) + " --host=" + host.getText() + " --execute=\"CREATE DATABASE IF NOT EXISTS " + dbName.getText() + "\"";
            child = Runtime.getRuntime().exec(command);
            exitVal1 = child.waitFor();
            command = "mysql " + dbName.getText() + " --user=" + dbUser.getText() + " --password=" + new String(passwordField.getPassword()) + " --host=" + host.getText() + " -e \"SOURCE " + curDir + "/plugins/WSL/resources/tables.sql\"";
            child = Runtime.getRuntime().exec(command);
            exitVal2 = child.waitFor();
            command = "mysql " + dbName.getText() + " --user=" + dbUser.getText() + " --password=" + new String(passwordField.getPassword()) + " --host=" + host.getText() + " -e \"SOURCE " + curDir + "/plugins/WSL/resources/WikiSysop.sql\"";
            child = Runtime.getRuntime().exec(command);
            exitVal3 = child.waitFor();
        } catch (IOException e) {
            System.out.println("Exit codes => Create database: " + exitVal1 + " Create tables: " + exitVal2 + " Create WikiSysop user:" + exitVal3);
            System.out.println(e.toString());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Exit codes => Create database: " + exitVal1 + " Create tables: " + exitVal2 + " Create WikiSysop user:" + exitVal3);
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * This methods downloads mediaWiki 1.16.1 from
     * a Onekin repository
     */
    private void downloadMW() {
        BufferedInputStream in;
        try {
            String mediaWikiFileName = "mediawiki-1.16.1.tar.gz";
            in = new BufferedInputStream(new java.net.URL("http://www.onekin.org/wsl/downloads/" + mediaWikiFileName).openStream());
            FileOutputStream fos = new FileOutputStream(mediaWikiFileName);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
            fos.close();
            uncompressMW(mediaWikiFileName, new File(installationDir.getText()));
            deleteFile(mediaWikiFileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * This methods uncompresses tar.gz files
	 * @param mediaWikiFileName
	 * Uncompress tar.gz
	 */
    private void uncompressMW(String mediaWikiFileName, File dest) {
        try {
            dest.mkdir();
            TarInputStream tin = new TarInputStream(new GZIPInputStream(new FileInputStream(new File(mediaWikiFileName))));
            TarEntry tarEntry = tin.getNextEntry();
            while (tarEntry != null) {
                File destPath = new File(dest.toString() + File.separatorChar + tarEntry.getName());
                if (tarEntry.isDirectory()) {
                    destPath.mkdir();
                } else {
                    FileOutputStream fout = new FileOutputStream(destPath);
                    tin.copyEntryContents(fout);
                    fout.close();
                }
                tarEntry = tin.getNextEntry();
            }
            tin.close();
            File oldMWDir = new File(installationDir.getText() + File.separatorChar + mediaWikiFileName.substring(0, mediaWikiFileName.length() - 7));
            oldMWDir.renameTo(new File(installationDir.getText() + File.separatorChar + wikiName.getText()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * This methods deletes a file
	 * @param mediaWikiFileName
	 */
    private void deleteFile(String fileToDelete) {
        File file = new File(fileToDelete);
        if (!file.exists()) {
            throw new IllegalArgumentException("Delete: no such file or directory: " + fileToDelete);
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("Delete: write protected: " + fileToDelete);
        }
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files.length > 0) throw new IllegalArgumentException("Delete: directory not empty: " + fileToDelete);
        }
        boolean success = file.delete();
        if (!success) {
            throw new IllegalArgumentException("Delete: deletion failed");
        }
    }

    /**
	 * This methods checks if all the fields are correct
	 * @return boolean
	 */
    private boolean checkFields() {
        boolean everythingOK = true;
        StringBuffer warning = new StringBuffer();
        warning.append("<html><p align=center>");
        if (!validateEmail(email.getText())) {
            everythingOK = false;
            warning.append("Invalid email: " + email.getText() + " <BR> ");
        }
        if (!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
            everythingOK = false;
            warning.append("Passwords do not match <BR> ");
        }
        if (wikiName.getText().equals("")) {
            everythingOK = false;
            warning.append("Wiki name is empty <BR> ");
        }
        if (email.getText().equals("")) {
            everythingOK = false;
            warning.append("Email is empty <BR> ");
        }
        if (host.getText().equals("")) {
            everythingOK = false;
            warning.append("Host is empty <BR> ");
        }
        if (dbName.getText().equals("")) {
            everythingOK = false;
            warning.append("DB name is empty <BR> ");
        }
        if (dbUser.getText().equals("")) {
            everythingOK = false;
            warning.append("DB user is empty <BR> ");
        }
        if (installationDir.getText().equals("")) {
            everythingOK = false;
            warning.append("Installation dir is empty <BR> ");
        }
        if (passwordField.getPassword().equals("")) {
            everythingOK = false;
            warning.append("Password is empty <BR> ");
        }
        if (confirmPasswordField.getPassword().equals("")) {
            everythingOK = false;
            warning.append("Confirmation password is empty <BR> ");
        }
        warning.append("</p></html>");
        addressDisplay.setText(warning.toString());
        return everythingOK;
    }

    /**
	 * 
	 * @param text
	 * @return Boolean
	 * Email validator, extracted from 
	 * www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression
	 */
    private Boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected void updateDisplays() {
        addressDisplay.setText("<html><p align=center>Complete the following data to <BR>configure MediaWiki installation!<BR>" + "Be patient during MediaWiki configuration  <BR>and installation please, it takes a minute <BR>" + "After this process you may select the new LocalSettings.php</html></p>");
    }

    protected JComponent createAddressDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        addressDisplay = new JLabel();
        addressDisplay.setHorizontalAlignment(JLabel.CENTER);
        updateDisplays();
        panel.setBorder(BorderFactory.createEmptyBorder(GAP / 2, 0, GAP / 2, 0));
        panel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
        panel.add(addressDisplay, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));
        return panel;
    }

    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField) c).selectAll();
        }
    }

    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField) c;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    public void focusLost(FocusEvent e) {
    }

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());
        String[] labelStrings = { "Wiki name (e.g., WSL)", "Contact email (e.g., admin@host.com)", "DB Host (e.g., localhost)", "DB name (e.g., WSL)", "DB username (e.g., root)", "DB password", "Confirm DB password", "Select dir to install MediaWiki" };
        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;
        wikiName = new JTextField();
        wikiName.setColumns(30);
        fields[fieldNum++] = wikiName;
        email = new JTextField();
        email.setColumns(30);
        fields[fieldNum++] = email;
        host = new JTextField();
        host.setColumns(30);
        fields[fieldNum++] = host;
        dbName = new JTextField();
        dbName.setColumns(30);
        fields[fieldNum++] = dbName;
        dbUser = new JTextField();
        dbUser.setColumns(30);
        fields[fieldNum++] = dbUser;
        passwordField = new JPasswordField();
        passwordField.setColumns(30);
        fields[fieldNum++] = passwordField;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setColumns(30);
        fields[fieldNum++] = confirmPasswordField;
        installationDir = new JTextField();
        installationDir.setColumns(20);
        fields[fieldNum++] = installationDir;
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner) fields[i]);
            } else {
                tf = (JTextField) fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, GAP, GAP, GAP, GAP / 2);
        return panel;
    }

    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: " + spinner.getEditor().getClass() + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    static void createAndShowGUI() {
        frame = new JFrame("Configure your MediaWiki Installation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new WSL_MediaWikiConfig());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setSize(width / 2, height / 2);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        launch();
    }

    static void launch(WSL_ExistMWConfig existMW) {
        existingMW = existMW;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    static void launch() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
