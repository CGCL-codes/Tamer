package net.sf.worldsaver.names;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.sf.worldsaver.util.*;
import java.lang.reflect.*;

/**
 * This dialog displays buttons for the functionality provided by
 * the Names class and used by the Namer class/application.
 * @version 15 Mar 2001
 * @author Andreas Schmitz
 */
public class ActionDialog extends JDialog {

    private JButton okayButton;

    private JButton readInputfile;

    private JButton readConfigfile;

    private JButton readFilterfile;

    private JButton createConfigvalues;

    private JButton createFiltervalues;

    private JButton cleanFilters;

    private JButton saveConfigfile;

    private JButton saveFilterfile;

    private JButton launchGeneralOptions;

    private ActionDialog itsme;

    private Names names;

    private Timer timer;

    private static Localizer lang = Names.getLocalizer();

    /**
     * Almost a standard constructor, but requires a Names object to
     * let the actions take place somehow.
     *@param parent the owner frame
     *@param modal whether this dialog is modal
     *@param names The Names object.
     */
    public ActionDialog(Frame parent, boolean modal, Names names) {
        super(parent, modal);
        this.names = names;
        itsme = this;
        initComponents();
    }

    private void initComponents() {
        getContentPane().setLayout(new GridBagLayout());
        setTitle(lang.get("paction"));
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                closeDialog();
            }
        });
        okayButton = new JButton(lang.get("ok"));
        okayButton.setToolTipText(lang.get("exitdialog"));
        okayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                closeDialog();
            }
        });
        GridBagConstraints gb;
        gb = new GridBagConstraints();
        gb.fill = gb.HORIZONTAL;
        gb.gridx = 0;
        gb.gridy = 10;
        gb.insets = new Insets(2, 5, 5, 5);
        getContentPane().add(okayButton, gb);
        readInputfile = new JButton(lang.get("xread", new String[] { lang.get("input") }));
        readInputfile.setToolTipText(lang.get("reads", new String[] { lang.get("input") }));
        readInputfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                loadInputfileAction();
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 1;
        gb.insets = new Insets(5, 5, 2, 5);
        getContentPane().add(readInputfile, gb);
        readConfigfile = new JButton(lang.get("xread", new String[] { lang.get("config") }));
        readConfigfile.setToolTipText(lang.get("reads", new String[] { lang.get("config") }));
        readConfigfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    names.readConfigfile();
                    if (names.hasInput()) createFiltervalues.setEnabled(true);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(itsme, e.getLocalizedMessage(), lang.get("error"), JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(itsme, lang.get("wformat"), lang.get("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.insets = new Insets(2, 5, 2, 5);
        gb.gridy = 2;
        getContentPane().add(readConfigfile, gb);
        readFilterfile = new JButton(lang.get("xread", new String[] { lang.get("filter") }));
        readFilterfile.setToolTipText(lang.get("reads", new String[] { lang.get("filter") }));
        readFilterfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    names.readFilterfile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(itsme, e.getLocalizedMessage(), lang.get("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 3;
        getContentPane().add(readFilterfile, gb);
        createConfigvalues = new JButton(lang.get("createcv"));
        if (!names.hasInput()) createConfigvalues.setEnabled(false);
        createConfigvalues.setToolTipText(lang.get("createcvi"));
        createConfigvalues.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                createConfigvaluesAction();
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 4;
        getContentPane().add(createConfigvalues, gb);
        createFiltervalues = new JButton(lang.get("createfv"));
        createFiltervalues.setToolTipText(lang.get("createcvi"));
        if (!names.hasInput()) createFiltervalues.setEnabled(false);
        if (!names.hasConfig()) createFiltervalues.setEnabled(false);
        createFiltervalues.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                createFiltervaluesAction();
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 5;
        getContentPane().add(createFiltervalues, gb);
        cleanFilters = new JButton(lang.get("clean"));
        cleanFilters.setToolTipText(lang.get("cleani"));
        cleanFilters.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                names.cleanFilters();
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 6;
        getContentPane().add(cleanFilters, gb);
        saveConfigfile = new JButton(lang.get("save", new String[] { lang.get("config") }));
        saveConfigfile.setToolTipText(lang.get("savei", new String[] { lang.get("config") }));
        saveConfigfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    names.saveConfigfile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(itsme, e.getLocalizedMessage(), lang.get("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 7;
        getContentPane().add(saveConfigfile, gb);
        saveFilterfile = new JButton(lang.get("save", new String[] { lang.get("filter") }));
        saveFilterfile.setToolTipText(lang.get("savei", new String[] { lang.get("filter") }));
        saveFilterfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    names.saveFilterfile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(itsme, e.getLocalizedMessage(), lang.get("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 8;
        getContentPane().add(saveFilterfile, gb);
        launchGeneralOptions = new JButton(lang.get("launchg"));
        launchGeneralOptions.setToolTipText(lang.get("launchgi"));
        launchGeneralOptions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GeneralOptions dlg = new GeneralOptions(itsme, true, names);
                dlg.show();
            }
        });
        gb = (GridBagConstraints) gb.clone();
        gb.gridy = 9;
        getContentPane().add(launchGeneralOptions, gb);
        pack();
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = getSize();
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
    }

    private void createConfigvaluesAction() {
        names.createConfigvalues();
        final ProgressMonitor p = new ProgressMonitor(this, lang.get("creatingcd"), names.getMessage(), 0, names.getLengthOfTask());
        timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (names.isReady()) {
                    timer.stop();
                    p.close();
                } else {
                    p.setNote(names.getMessage());
                    p.setProgress(names.getStatus());
                }
            }
        });
        timer.start();
        createFiltervalues.setEnabled(true);
    }

    private void createFiltervaluesAction() {
        names.createFiltervalues();
        final ProgressMonitor p = new ProgressMonitor(this, lang.get("creatingf"), names.getMessage(), 0, names.getLengthOfTask());
        timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (names.isReady()) {
                    timer.stop();
                    p.close();
                } else {
                    p.setNote(names.getMessage());
                    p.setProgress(names.getStatus());
                }
            }
        });
        timer.start();
    }

    private void loadInputfileAction() {
        try {
            names.readInputfile();
            final ProgressMonitor p = new ProgressMonitor(this, lang.get("read", new String[] { lang.get("input") }), names.getMessage(), 0, names.getLengthOfTask());
            timer = new Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (names.isReady()) {
                        timer.stop();
                        p.close();
                    } else {
                        p.setNote(names.getMessage());
                        p.setProgress(names.getStatus());
                    }
                }
            });
            timer.start();
            if (names.hasConfig()) createFiltervalues.setEnabled(true);
            createConfigvalues.setEnabled(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(itsme, lang.get("errread", new String[] { lang.get("input"), e.getLocalizedMessage() }), lang.get("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }
}
