package com.sshtools.j2ssh.authentication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import com.sshtools.common.ui.IconWrapperPanel;
import com.sshtools.common.ui.ResourceIcon;
import com.sshtools.common.ui.UIUtil;

public class MyProxyPrompt {

    class MyProxyDialog extends JDialog {

        String getPassword() {
            if (cancelled || !myproxy) return null; else return new String(password.getPassword());
        }

        String getCPassword() {
            if (cancelled || myproxy || browser) return null; else return new String(cpassword.getPassword());
        }

        String getHost() {
            return (!cancelled && myproxy) ? host.getText() : null;
        }

        String getAccountName() {
            return (!cancelled && myproxy) ? name.getText() : null;
        }

        String getCFile() {
            return (!cancelled && !myproxy && !browser) ? cfile.getText() : null;
        }

        boolean getCanceled() {
            return cancelled;
        }

        void setFile(File f) {
            cfile.setText(f.getAbsolutePath());
        }

        void init(boolean use) {
            setDefaultCloseOperation(2);
            JPanel pMyproxy = new JPanel(new GridBagLayout());
            GridBagConstraints gridbagconstraints = new GridBagConstraints();
            gridbagconstraints.insets = new Insets(0, 0, 2, 2);
            gridbagconstraints.anchor = 17;
            gridbagconstraints.fill = 2;
            gridbagconstraints.weightx = 0.0D;
            UIUtil.jGridBagAdd(pMyproxy, new JLabel("Host: "), gridbagconstraints, -1);
            gridbagconstraints.weightx = 1.0D;
            UIUtil.jGridBagAdd(pMyproxy, host, gridbagconstraints, 0);
            gridbagconstraints.weightx = 0.0D;
            UIUtil.jGridBagAdd(pMyproxy, new JLabel("Account Name: "), gridbagconstraints, -1);
            gridbagconstraints.weightx = 1.0D;
            UIUtil.jGridBagAdd(pMyproxy, name, gridbagconstraints, 0);
            gridbagconstraints.weightx = 0.0D;
            UIUtil.jGridBagAdd(pMyproxy, new JLabel("Passphrase: "), gridbagconstraints, -1);
            gridbagconstraints.weightx = 1.0D;
            UIUtil.jGridBagAdd(pMyproxy, password, gridbagconstraints, 0);
            promptLabel.setHorizontalAlignment(0);
            JPanel pOuterMP = new JPanel(new BorderLayout());
            pOuterMP.add(promptLabel, "North");
            pOuterMP.add(pMyproxy, "Center");
            JButton jbutton = new JButton("Use MyProxy");
            jbutton.setMnemonic('m');
            jbutton.setDefaultCapable(true);
            jbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    cancelled = false;
                    browser = false;
                    myproxy = true;
                    cancelled = false;
                    hide();
                }
            });
            getRootPane().setDefaultButton(jbutton);
            JPanel jpanel2 = new JPanel(new FlowLayout(2, 0, 0));
            jpanel2.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
            jpanel2.add(jbutton);
            pOuterMP.add(jpanel2, "South");
            IconWrapperPanel iconwrapperpanel = new IconWrapperPanel(new ResourceIcon("largelock.png"), pOuterMP);
            iconwrapperpanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JButton jbuttonB = new JButton("Browse...");
            jbuttonB.setMnemonic('b');
            jbuttonB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    JFileChooser chooser = new JFileChooser();
                    ExampleFileFilter filter = new ExampleFileFilter();
                    filter.addExtension("pfx");
                    filter.addExtension("p12");
                    filter.setDescription("pfx and p12 files");
                    chooser.setFileFilter(filter);
                    chooser.setFileHidingEnabled(false);
                    chooser.setDialogTitle("Select Certificate File For Authentication");
                    if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                        setFile(chooser.getSelectedFile());
                    }
                }
            });
            cpassword.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    int k = e.getKeyCode();
                    if (k == e.VK_ENTER) {
                        cancelled = false;
                        browser = false;
                        myproxy = false;
                        hide();
                        e.consume();
                    }
                }
            });
            JPanel filepan = new JPanel(new BorderLayout());
            filepan.add(cfile, BorderLayout.CENTER);
            filepan.add(jbuttonB, BorderLayout.EAST);
            JPanel jpanelC = new JPanel(new GridBagLayout());
            gridbagconstraints = new GridBagConstraints();
            gridbagconstraints.insets = new Insets(0, 0, 2, 2);
            gridbagconstraints.anchor = 17;
            gridbagconstraints.fill = 2;
            gridbagconstraints.weightx = 0.0D;
            UIUtil.jGridBagAdd(jpanelC, new JLabel("Filename: "), gridbagconstraints, -1);
            gridbagconstraints.weightx = 1.0D;
            UIUtil.jGridBagAdd(jpanelC, filepan, gridbagconstraints, 0);
            gridbagconstraints.weightx = 0.0D;
            UIUtil.jGridBagAdd(jpanelC, new JLabel("Passphrase: "), gridbagconstraints, -1);
            gridbagconstraints.weightx = 1.0D;
            UIUtil.jGridBagAdd(jpanelC, cpassword, gridbagconstraints, 0);
            JLabel promptLabelC = new JLabel("Use a Grid certificate in pkcs12 format:");
            promptLabelC.setHorizontalAlignment(0);
            JButton jbuttonC = new JButton("Use Certificate");
            jbuttonC.setMnemonic('t');
            jbuttonC.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    cancelled = false;
                    browser = false;
                    myproxy = false;
                    hide();
                }
            });
            JPanel jpanelC2 = new JPanel(new BorderLayout());
            jpanelC2.add(jbuttonC, BorderLayout.EAST);
            JPanel jpanelC1 = new JPanel(new BorderLayout());
            jpanelC1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jpanelC1.add(promptLabelC, "North");
            jpanelC1.add(jpanelC, "Center");
            jpanelC1.add(jpanelC2, "South");
            IconWrapperPanel iconwrapperpanelC = new IconWrapperPanel(new ResourceIcon("/com/sshtools/common/authentication/largepassphrase.png"), jpanelC1);
            iconwrapperpanelC.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JButton jbuttonB2 = new JButton("Use Certificate from Browser");
            jbuttonB2.setMnemonic('b');
            jbuttonB2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    cancelled = false;
                    myproxy = false;
                    browser = true;
                    hide();
                }
            });
            JPanel jpanelB = new JPanel(new BorderLayout());
            JLabel info = new JLabel("Search for certificates in Internet Explorer or Firefox:");
            JPanel jpanelB2 = new JPanel(new BorderLayout());
            JPanel jpanelB3 = new JPanel(new BorderLayout());
            jpanelB3.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 0));
            jpanelB3.add(jbuttonB2, BorderLayout.NORTH);
            jpanelB2.add(jpanelB3, BorderLayout.EAST);
            jpanelB2.add(info, BorderLayout.NORTH);
            jpanelB.add(jpanelB2, BorderLayout.NORTH);
            IconWrapperPanel iconwrapperpanelB = new IconWrapperPanel(new ResourceIcon("/com/sshtools/common/ui/proxy.png"), jpanelB);
            iconwrapperpanelB.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JLabel warn = new JLabel(lastError);
            warn.setForeground(Color.red);
            warn.setHorizontalAlignment(JLabel.CENTER);
            JButton jbutton1 = new JButton("Cancel");
            jbutton1.setMnemonic('c');
            jbutton1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    cancelled = true;
                    browser = false;
                    myproxy = false;
                    hide();
                }
            });
            JPanel jpanelEND = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            jpanelEND.add(jbutton1);
            jpanelEND.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JPanel jpanelW = new JPanel(new BorderLayout());
            jpanelW.add(warn, BorderLayout.NORTH);
            getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            getContentPane().add(jpanelW);
            getContentPane().add(iconwrapperpanel);
            getContentPane().add(iconwrapperpanelC);
            getContentPane().add(iconwrapperpanelB);
            getContentPane().add(jpanelEND);
            pack();
            setResizable(false);
            toFront();
            UIUtil.positionComponent(0, this);
            setVisible(true);
            password.requestFocus();
        }

        JLabel promptLabel;

        JTextField host;

        JTextField name;

        JTextField cfile;

        JPasswordField password;

        JPasswordField cpassword;

        boolean cancelled = true;

        boolean myproxy = false;

        boolean browser = false;

        boolean getKeybased() {
            return !myproxy;
        }

        boolean getBrowser() {
            return browser;
        }

        MyProxyDialog(boolean use) {
            super((Frame) null, "Grid Certificate/Proxy needed for Authentication", true);
            promptLabel = new JLabel(title);
            host = new JTextField(15);
            name = new JTextField(15);
            host.setText(default_host);
            name.setText(default_name);
            password = new JPasswordField(15);
            cpassword = new JPasswordField(15);
            cfile = new JTextField(10);
            cfile.setText(lastFILE);
            init(use);
        }

        MyProxyDialog(Frame frame, boolean use) {
            super(frame, "Grid Certificate/Proxy needed for Authentication", true);
            promptLabel = new JLabel(title);
            host = new JTextField(15);
            name = new JTextField(15);
            host.setText(default_host);
            name.setText(default_name);
            password = new JPasswordField(15);
            cpassword = new JPasswordField(15);
            cfile = new JTextField(10);
            cfile.setText(lastFILE);
            cfile = new JTextField(10);
            init(use);
        }

        MyProxyDialog(Dialog dialog, boolean use) {
            super(dialog, "Grid Certificate/Proxy needed for Authentication", true);
            promptLabel = new JLabel(title);
            host = new JTextField(15);
            name = new JTextField(15);
            host.setText(default_host);
            name.setText(default_name);
            password = new JPasswordField(15);
            cpassword = new JPasswordField(15);
            cfile = new JTextField(10);
            cfile.setText(lastFILE);
            init(use);
        }
    }

    private MyProxyPrompt() {
        title = "Retrieve Credentials from MyProxy:";
    }

    public void setParentComponent(Component component) {
        parent = component;
    }

    public boolean doGet(Component component, StringBuffer stringbuffer, StringBuffer stringbuffer1, StringBuffer stringbuffer2, boolean use) {
        if (component == null) component = parent;
        Window window = component != null ? (Window) SwingUtilities.getAncestorOfClass(java.awt.Window.class, component) : null;
        MyProxyDialog myproxydialog = null;
        if (window instanceof Frame) myproxydialog = new MyProxyDialog((Frame) window, use); else if (window instanceof Dialog) myproxydialog = new MyProxyDialog((Dialog) window, use); else myproxydialog = new MyProxyDialog(use);
        stringbuffer.append(myproxydialog.getHost());
        stringbuffer1.append(myproxydialog.getAccountName());
        stringbuffer2.append(myproxydialog.getPassword());
        last = myproxydialog;
        lastFILE = last.getCFile();
        return myproxydialog.getCanceled();
    }

    public boolean keyBased(StringBuffer bufferf, StringBuffer bufferp) {
        bufferp.append(last.getCPassword());
        bufferf.append(last.getCFile());
        lastFILE = last.getCFile();
        return last.getKeybased();
    }

    public boolean getBrowser() {
        return last.getBrowser();
    }

    public static MyProxyPrompt getInstance() {
        if (instance == null) instance = new MyProxyPrompt();
        return instance;
    }

    public void setTitle(String s) {
        title = s;
    }

    public static final String PASSWORD_ICON = "/com/sshtools/common/authentication/largepassword.png";

    private static MyProxyPrompt instance;

    private Component parent;

    private String title;

    private String default_name;

    private String default_host;

    private String lastError = "";

    private MyProxyDialog last;

    private String lastFILE = "";

    public void setHost(String newhost) {
        default_host = newhost;
    }

    public void setAccountName(String newname) {
        default_name = newname;
    }

    public void setError(String s) {
        lastError = s;
    }
}
