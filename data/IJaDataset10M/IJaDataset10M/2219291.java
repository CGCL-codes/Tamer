package com.oat.explorer.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import com.oat.Algorithm;
import com.oat.AlgorithmEpochCompleteListener;
import com.oat.AutomaticallyConfigurableAlgorithm;
import com.oat.Domain;
import com.oat.DomainUtils;
import com.oat.Problem;
import com.oat.explorer.gui.AlgorithmChangedListener;
import com.oat.explorer.gui.AlgorithmRunStateChangedListener;
import com.oat.gui.BeanConfigurationFrame;
import com.oat.gui.GenericTextDialog;
import com.oat.gui.GenericTreeChooser;

/**
 * Type: AlgorithmPanel<br/>
 * Date: 24/11/2006<br/>
 * <br/>
 * Description:
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 06/07/2007   JBrownlee   Enable or disable the configuration button whether or
 *                          not the algorithm has configuration
 * 24/07/2007   JBrownlee   Added GUI support for automatic configuration, algorithm instances are set to use it if available
 * 11/09/2007	JBrownlee	Removed abstract methods, resolves largest common algorithm base automatically
 * 
 * </pre>
 */
public class AlgorithmPanel extends JPanel implements ActionListener, AlgorithmRunStateChangedListener, AlgorithmChangedListener, TreeSelectionListener {

    protected final LinkedList<AlgorithmChangedListener> algorithmChangedListeners;

    protected final Domain domain;

    protected Algorithm[] algorithmInstanceList;

    public GenericTreeChooser chooser;

    protected Algorithm selected;

    protected JTextField aField;

    protected JButton configureButton;

    protected JButton chooseButton;

    protected JButton aboutButton;

    protected JCheckBox automaticConfig;

    public AlgorithmPanel(Domain aDomain) {
        domain = aDomain;
        algorithmChangedListeners = new LinkedList<AlgorithmChangedListener>();
        setName("Algorithm Details");
        prepareGUI();
    }

    protected void prepareGUI() {
        registerAlgorithmChangedListener(this);
        loadAlgorithmInstances();
        String commonBase = null;
        if (domain.canOrganizeAlgorithsByClassname()) {
            commonBase = DomainUtils.locateLargestCommonBase(algorithmInstanceList);
        }
        configureButton = new JButton("Config");
        configureButton.addActionListener(this);
        aboutButton = new JButton("About");
        aboutButton.addActionListener(this);
        chooseButton = new JButton("Choose");
        chooseButton.addActionListener(this);
        chooser = new GenericTreeChooser(algorithmInstanceList, commonBase);
        chooser.getTree().addTreeSelectionListener(this);
        aField = new JTextField("", 30);
        aField.setEditable(false);
        aField.setBackground(Color.WHITE);
        automaticConfig = new JCheckBox("Automatically Configure Algorithm");
        automaticConfig.setEnabled(false);
        automaticConfig.setSelected(false);
        automaticConfig.addActionListener(this);
        chooseButton.setToolTipText("Select the algorithm for the next run");
        aField.setToolTipText("Algorithm human-readable name");
        configureButton.setToolTipText("Configure the selected algorithm (if algorithm has parameters)");
        aboutButton.setToolTipText("Information about the currently selected algorithm");
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        JPanel p = new JPanel(gbl);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        gbl.setConstraints(chooseButton, c);
        p.add(chooseButton, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(aField, c);
        p.add(aField, c);
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        gbl.setConstraints(configureButton, c);
        p.add(configureButton, c);
        c.gridx = 5;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        gbl.setConstraints(aboutButton, c);
        p.add(aboutButton, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(automaticConfig, c);
        p.add(automaticConfig, c);
        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), getName()));
    }

    public void setRandomAlgorithmInstance(Random r) {
        if (algorithmInstanceList != null) {
            chooser.setSelection(algorithmInstanceList[r.nextInt(algorithmInstanceList.length)]);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == configureButton) {
            Algorithm a = getCurrentlySelectedAlgorithm();
            if (a.isUserConfigurable()) {
                BeanConfigurationFrame f = new BeanConfigurationFrame(null, a, "Algorithm Configuration", null);
                f.setVisible(true);
            }
        } else if (src == chooseButton) {
            chooser.show(chooseButton, chooseButton.getX(), chooseButton.getY());
        } else if (src == aboutButton) {
            new GenericTextDialog(getCurrentlySelectedAlgorithm().getDetails()).setVisible(true);
        }
        if (src == automaticConfig) {
            if (automaticConfig.isSelected()) {
                configureButton.setEnabled(false);
            } else if (selected != null) {
                configureButton.setEnabled(selected.isUserConfigurable());
            }
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) chooser.tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            GenericTreeChooser.Entry entry = (GenericTreeChooser.Entry) node.getUserObject();
            selected = (Algorithm) entry.object;
            algorithmChanged(selected);
            chooser.setVisible(false);
        }
    }

    public void loadAlgorithmInstances() {
        String errorMsg = "";
        try {
            algorithmInstanceList = domain.loadAlgorithmList();
            System.err.println(getClass().getName() + " > loaded " + algorithmInstanceList.length + " algorithms.");
        } catch (Exception e) {
            errorMsg = e.getMessage();
            throw new RuntimeException("There was an error preparing algorithm instances.", e);
        } finally {
            if (algorithmInstanceList == null || algorithmInstanceList.length == 0) {
                algorithmInstanceList = null;
                JOptionPane.showMessageDialog(this, "There was an error preparing algorithm instances.\n" + errorMsg, "Error Preparing Algorithms", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void registerAlgorithmChangedListener(AlgorithmChangedListener a) {
        algorithmChangedListeners.add(a);
    }

    public void algorithmChanged(Algorithm algo) {
        for (AlgorithmChangedListener a : algorithmChangedListeners) {
            a.algorithmChangedEvent(algo);
        }
    }

    public void algorithmChangedEvent(Algorithm algo) {
        aField.setText(algo.getName());
        if (selected instanceof AutomaticallyConfigurableAlgorithm) {
            automaticConfig.setEnabled(true);
            automaticConfig.setSelected(true);
            configureButton.setEnabled(false);
        } else {
            automaticConfig.setEnabled(false);
            automaticConfig.setSelected(false);
            configureButton.setEnabled(algo.isUserConfigurable());
        }
    }

    public Algorithm getCurrentlySelectedAlgorithm() {
        if (algorithmInstanceList == null) {
            return null;
        }
        return selected;
    }

    public void algorithmFinishNotify(Problem p, Algorithm a) {
        configureButton.setEnabled(true);
        chooseButton.setEnabled(true);
        if (a instanceof AutomaticallyConfigurableAlgorithm) {
            automaticConfig.setEnabled(true);
            if (automaticConfig.isSelected()) {
                configureButton.setEnabled(false);
            } else {
                configureButton.setEnabled(a.isUserConfigurable());
            }
        } else {
            configureButton.setEnabled(a.isUserConfigurable());
        }
    }

    public void algorithmStartNotify(Problem p, Algorithm a) {
        configureButton.setEnabled(false);
        chooseButton.setEnabled(false);
        automaticConfig.setEnabled(false);
    }

    public void registerAlgorithmIterationCompleteListener(AlgorithmEpochCompleteListener l) {
        for (Algorithm a : algorithmInstanceList) {
            a.addAlgorithmIterationCompleteListener(l);
        }
    }

    public boolean shoudAutomaticallyConfigure() {
        return automaticConfig.isEnabled() && automaticConfig.isSelected();
    }
}
