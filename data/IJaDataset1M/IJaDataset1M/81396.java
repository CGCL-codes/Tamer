package gui;

import java.awt.Color;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class DatabaseTab extends JPanel {

    JTextField wc3location;

    JTextField replaylocation;

    JTextField replaypattern;

    JPanel replaypanel;

    JTextField sslocation;

    JTextField sspattern;

    JPanel sspanel;

    JCheckBox movereplays;

    JCheckBox alwaysmove;

    JButton Browse1;

    JButton Browse2;

    GridBagConstraints c = new GridBagConstraints();

    public DatabaseTab() {
        Action browse1Action = new AbstractAction("Browse") {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser pick = new JFileChooser();
                pick.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                pick.setApproveButtonText("Select");
                int returnVal = pick.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    replaylocation.setText(pick.getSelectedFile().getPath());
                }
            }
        };
        Browse1 = new JButton(browse1Action);
        Action browse2Action = new AbstractAction("Browse") {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser pick = new JFileChooser();
                pick.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                pick.setApproveButtonText("Select");
                int returnVal = pick.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    wc3location.setText(pick.getSelectedFile().getPath());
                }
            }
        };
        Browse2 = new JButton(browse2Action);
        this.setLayout(new GridBagLayout());
        c.weighty = 0.1;
        sspanel = new JPanel(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();
        sspanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Screenshot database"));
        sslocation = new JTextField("", 40);
        sspattern = new JTextField("", 40);
        a.anchor = GridBagConstraints.LINE_START;
        a.ipadx = 5;
        a.gridy = 0;
        a.weighty = 0.5;
        a.weightx = 0.1;
        a.gridx = 0;
        sspanel.add(new JLabel("Location : "), a);
        a.gridx = 1;
        sspanel.add(sslocation, a);
        a.gridy = 1;
        a.gridx = 0;
        sspanel.add(new JLabel("Filename Pattern : "), a);
        a.gridx = 1;
        sspanel.add(sspattern, a);
        c.gridy = 0;
        c.ipady = 20;
        c.ipadx = 20;
        this.add(sspanel, c);
        replaypanel = new JPanel(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        replaypanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Replay database"));
        replaypattern = new JTextField("", 40);
        replaylocation = new JTextField("", 40);
        wc3location = new JTextField("", 40);
        movereplays = new JCheckBox("Move and rename replays to database on game-over", true);
        alwaysmove = new JCheckBox("Move only when someone was banned that game", false);
        b.weighty = 0.5;
        b.weightx = 0.1;
        b.gridy = 0;
        b.anchor = GridBagConstraints.LINE_START;
        b.gridwidth = 2;
        b.ipadx = 5;
        replaypanel.add(movereplays, b);
        b.gridy = 1;
        replaypanel.add(alwaysmove, b);
        b.gridx = 0;
        b.gridy = 2;
        b.gridwidth = 1;
        replaypanel.add(new JLabel("Database Location : "), b);
        b.gridx = 1;
        replaypanel.add(replaylocation, b);
        b.gridx = 2;
        replaypanel.add(Browse1, b);
        b.gridy = 3;
        b.gridx = 0;
        replaypanel.add(new JLabel("Filename Pattern : "), b);
        b.gridx = 1;
        replaypanel.add(replaypattern, b);
        b.gridy = 4;
        b.gridx = 0;
        replaypanel.add(new JLabel("WC3 Replay Folder : "), b);
        b.gridx = 1;
        replaypanel.add(wc3location, b);
        b.gridx = 2;
        replaypanel.add(Browse2, b);
        c.gridy = 1;
        c.ipady = 20;
        c.ipadx = 20;
        this.add(replaypanel, c);
        loaddefaults();
    }

    private void loaddefaults() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config.txt"));
        } catch (FileNotFoundException e) {
            File config = new File("config.txt");
            try {
                config.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!p.getProperty("sslocation").trim().equalsIgnoreCase("")) {
                sslocation.setText(p.getProperty("sslocation"));
            } else {
                sslocation.setText(System.getProperty("user.dir") + "\\" + "Screenshots");
            }
        } catch (NullPointerException e) {
            sslocation.setText(System.getProperty("user.dir") + "\\" + "Screenshots");
        }
        try {
            if (!p.getProperty("sspattern").trim().equalsIgnoreCase("")) {
                sspattern.setText(p.getProperty("sspattern"));
            } else {
                sspattern.setText("$year$-$month$-$day$ $undertime$");
            }
        } catch (NullPointerException e) {
            sspattern.setText("$year$-$month$-$day$ $undertime$");
        }
        try {
            if (!p.getProperty("replaylocation").trim().equalsIgnoreCase("")) {
                replaylocation.setText(p.getProperty("replaylocation"));
            } else {
                replaylocation.setText(System.getProperty("user.dir") + "\\Replays");
            }
        } catch (NullPointerException e) {
            replaylocation.setText(System.getProperty("user.dir") + "\\Replays");
        }
        try {
            if (!p.getProperty("replaypattern").trim().equalsIgnoreCase("")) {
                replaypattern.setText(p.getProperty("replaypattern"));
            } else {
                replaypattern.setText("$year$-$month$-$day$ $undertime$ $bannedlist$");
            }
        } catch (NullPointerException e) {
            replaypattern.setText("$year$-$month$-$day$ $undertime$ $bannedlist$");
        }
        try {
            if (!p.getProperty("wc3replaylocation").trim().equalsIgnoreCase("")) {
                wc3location.setText(p.getProperty("wc3replaylocation"));
            } else {
                wc3location.setText("Enter the path of your WC3 replay folder here");
                wc3location.setForeground(Color.red);
            }
        } catch (NullPointerException e) {
            wc3location.setText("Enter the path of your WC3 replay folder here");
            wc3location.setForeground(Color.red);
        }
        try {
            if (!p.getProperty("movereplays").trim().equalsIgnoreCase("")) {
                if (p.getProperty("movereplays").equals("true")) movereplays.setEnabled(true); else movereplays.setEnabled(false);
            } else {
            }
        } catch (NullPointerException e) {
        }
        try {
            if (!p.getProperty("alwaysmove").trim().equalsIgnoreCase("")) {
                if (p.getProperty("alwaysmove").equals("true")) alwaysmove.setEnabled(true); else alwaysmove.setEnabled(false);
            } else {
            }
        } catch (NullPointerException e) {
        }
        general.MiscTools.sssavepath = sslocation.getText();
        general.MiscTools.sspattern = sspattern.getText();
        general.MiscTools.replaysavepath = replaylocation.getText();
        general.MiscTools.replaypattern = replaypattern.getText();
        general.MiscTools.wc3replaypath = wc3location.getText();
        general.MiscTools.movereplays = movereplays.isEnabled();
        general.MiscTools.alwaysmove = alwaysmove.isEnabled();
    }

    public void apply() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setProperty("sslocation", sslocation.getText());
        p.setProperty("sspattern", sspattern.getText());
        p.setProperty("replaylocation", replaylocation.getText());
        p.setProperty("replaypattern", replaypattern.getText());
        p.setProperty("wc3replaylocation", wc3location.getText());
        if (movereplays.isEnabled()) p.setProperty("movereplays", "true"); else p.setProperty("movereplays", "false");
        if (alwaysmove.isEnabled()) p.setProperty("alwaysmove", "true"); else p.setProperty("alwaysmove", "false");
        general.MiscTools.sssavepath = sslocation.getText();
        general.MiscTools.sspattern = sspattern.getText();
        general.MiscTools.replaysavepath = replaylocation.getText();
        general.MiscTools.replaypattern = replaypattern.getText();
        general.MiscTools.wc3replaypath = wc3location.getText();
        general.MiscTools.movereplays = movereplays.isEnabled();
        general.MiscTools.alwaysmove = alwaysmove.isEnabled();
        try {
            p.store(new FileOutputStream(new File("config.txt")), "Configuration");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
