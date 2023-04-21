package org.crypthing.signthing.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import org.crypthing.things.config.Bundle;

/**
 * PKI Options tab
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class PKIOptions extends JPanel implements Configurable {

    private static final long serialVersionUID = 5405218813933843242L;

    private JComboBox jcbPKI = null;

    private JCheckBox jcbVerify = null;

    private JRadioButton jrbAllways = null;

    private JRadioButton jrbOnce = null;

    private JCheckBox jcbVerifyRevocation = null;

    private JRadioButton jrbCheckAllways = null;

    private JRadioButton jrbCheckOnce = null;

    private boolean verifyCompliance;

    private boolean verifyAllways;

    private boolean checkRevocation;

    private boolean checkAllways;

    public PKIOptions() {
        super();
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(600, 400));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(getSelectedPKI());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(getCompliancePanel());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(getRevocationPanel());
    }

    private JPanel getSelectedPKI() {
        JPanel jpPKI = new JPanel();
        jpPKI.setLayout(new BoxLayout(jpPKI, BoxLayout.LINE_AXIS));
        jpPKI.setAlignmentX(LEFT_ALIGNMENT);
        jpPKI.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jpPKI.setBackground(Color.white);
        jcbPKI = new JComboBox();
        jcbPKI.setPreferredSize(new Dimension(120, 20));
        jcbPKI.setMaximumSize(new Dimension(2000, 20));
        jcbPKI.setAlignmentX(LEFT_ALIGNMENT);
        jpPKI.add(new JLabel(Bundle.getInstance().getResourceString(this, "POP_LBL_PKI")));
        jpPKI.add(Box.createRigidArea(new Dimension(10, 0)));
        jpPKI.add(jcbPKI);
        return jpPKI;
    }

    private JPanel getCompliancePanel() {
        JPanel jpCompliance = new JPanel();
        jpCompliance.setBorder(new TitledBorder(Bundle.getInstance().getResourceString(this, "POP_PAN_COMPLIANCE")));
        jpCompliance.setLayout(new BoxLayout(jpCompliance, BoxLayout.PAGE_AXIS));
        jpCompliance.setBackground(Color.white);
        jpCompliance.setPreferredSize(new Dimension(100, 120));
        jpCompliance.setMaximumSize(new Dimension(2000, 120));
        jpCompliance.add(getJcbVerifyCompliance());
        jpCompliance.add(Box.createRigidArea(new Dimension(0, 10)));
        jpCompliance.add(getVerifyAllways());
        return jpCompliance;
    }

    private JCheckBox getJcbVerifyCompliance() {
        jcbVerify = new JCheckBox(Bundle.getInstance().getResourceString(this, "POP_CHB_CHECK_COMPLIANCE"));
        jcbVerify.setAlignmentX(LEFT_ALIGNMENT);
        jcbVerify.setBackground(Color.white);
        jcbVerify.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                verifyCompliance = (e.getStateChange() == ItemEvent.SELECTED);
                setVerifyComplienceEnabled(verifyCompliance);
            }
        });
        return jcbVerify;
    }

    private void setVerifyComplienceEnabled(boolean enabled) {
        jrbAllways.setEnabled(enabled);
        jrbOnce.setEnabled(enabled);
    }

    private JPanel getVerifyAllways() {
        JPanel jpVerifyCompliance = new JPanel();
        jpVerifyCompliance.setLayout(new BoxLayout(jpVerifyCompliance, BoxLayout.PAGE_AXIS));
        jpVerifyCompliance.setAlignmentX(LEFT_ALIGNMENT);
        jpVerifyCompliance.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 10));
        jpVerifyCompliance.setBackground(Color.white);
        ButtonGroup group = new ButtonGroup();
        group.add(getJrbAllways());
        group.add(getJrbOnce());
        jpVerifyCompliance.add(jrbAllways);
        jpVerifyCompliance.add(jrbOnce);
        return jpVerifyCompliance;
    }

    private JRadioButton getJrbAllways() {
        jrbAllways = new JRadioButton(Bundle.getInstance().getResourceString(this, "POP_RDO_ALLWAYS_CHECK_COMPL"));
        jrbAllways.setAlignmentX(LEFT_ALIGNMENT);
        jrbAllways.setBackground(Color.white);
        jrbAllways.setActionCommand("cmdVerifyAllways");
        jrbAllways.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                verifyAllways = (e.getActionCommand().contentEquals("cmdVerifyAllways"));
            }
        });
        return jrbAllways;
    }

    private JRadioButton getJrbOnce() {
        jrbOnce = new JRadioButton(Bundle.getInstance().getResourceString(this, "POP_RDO_SESSION_CHECK_COMPL"));
        jrbOnce.setAlignmentX(LEFT_ALIGNMENT);
        jrbOnce.setBackground(Color.white);
        jrbOnce.setActionCommand("cmdVerifyOnce");
        jrbOnce.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                verifyAllways = !(e.getActionCommand().contentEquals("cmdVerifyOnce"));
            }
        });
        return jrbOnce;
    }

    private JPanel getRevocationPanel() {
        JPanel jpRevocation = new JPanel();
        jpRevocation.setBorder(new TitledBorder(Bundle.getInstance().getResourceString(this, "POP_PAN_REVOCATION")));
        jpRevocation.setLayout(new BoxLayout(jpRevocation, BoxLayout.PAGE_AXIS));
        jpRevocation.setBackground(Color.white);
        jpRevocation.setPreferredSize(new Dimension(100, 120));
        jpRevocation.setMaximumSize(new Dimension(2000, 120));
        jpRevocation.add(getJcbVerifyRevocation());
        jpRevocation.add(Box.createRigidArea(new Dimension(0, 10)));
        jpRevocation.add(getCheckRevocationPanel());
        return jpRevocation;
    }

    private JCheckBox getJcbVerifyRevocation() {
        jcbVerifyRevocation = new JCheckBox(Bundle.getInstance().getResourceString(this, "POP_CHB_CHECK_REVOCATION"));
        jcbVerifyRevocation.setAlignmentX(LEFT_ALIGNMENT);
        jcbVerifyRevocation.setBackground(Color.white);
        jcbVerifyRevocation.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                checkRevocation = (e.getStateChange() == ItemEvent.SELECTED);
                setVerifyRevocationEnabled(checkRevocation);
            }
        });
        return jcbVerifyRevocation;
    }

    private void setVerifyRevocationEnabled(boolean enabled) {
        jrbCheckAllways.setEnabled(enabled);
        jrbCheckOnce.setEnabled(enabled);
    }

    private JPanel getCheckRevocationPanel() {
        JPanel jpCheckRevocation = new JPanel();
        jpCheckRevocation.setLayout(new BoxLayout(jpCheckRevocation, BoxLayout.PAGE_AXIS));
        jpCheckRevocation.setAlignmentX(LEFT_ALIGNMENT);
        jpCheckRevocation.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 10));
        jpCheckRevocation.setBackground(Color.white);
        ButtonGroup group = new ButtonGroup();
        group.add(getJrbCheckRevocationAllways());
        group.add(getJrbCheckRevocationOnce());
        jpCheckRevocation.add(jrbCheckAllways);
        jpCheckRevocation.add(jrbCheckOnce);
        return jpCheckRevocation;
    }

    private JRadioButton getJrbCheckRevocationAllways() {
        jrbCheckAllways = new JRadioButton(Bundle.getInstance().getResourceString(this, "POP_RDO_ALLWAYS_CHECK_REVOKE"));
        jrbCheckAllways.setAlignmentX(LEFT_ALIGNMENT);
        jrbCheckAllways.setBackground(Color.white);
        jrbCheckAllways.setActionCommand("cmdCheckAllways");
        jrbCheckAllways.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkAllways = (e.getActionCommand().contentEquals("cmdCheckAllways"));
            }
        });
        return jrbCheckAllways;
    }

    private JRadioButton getJrbCheckRevocationOnce() {
        jrbCheckOnce = new JRadioButton(Bundle.getInstance().getResourceString(this, "POP_RDO_SESSION_CHECK_REVOKE"));
        jrbCheckOnce.setAlignmentX(LEFT_ALIGNMENT);
        jrbCheckOnce.setBackground(Color.white);
        jrbCheckOnce.setActionCommand("cmdCheckOnce");
        jrbCheckOnce.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkAllways = !(e.getActionCommand().contentEquals("cmdCheckOnce"));
            }
        });
        return jrbCheckOnce;
    }

    @Override
    public void getConfiguration(Properties cfg) {
        cfg.setProperty("DEFAULT-PKI", (String) jcbPKI.getSelectedItem());
        cfg.setProperty("VERIFY-COMPLIANCE", Boolean.toString(verifyCompliance));
        cfg.setProperty("VERIFY-COMPLIANCE-ALLWAYS", Boolean.toString(verifyAllways));
        cfg.setProperty("VERIFY-REVOCATION", Boolean.toString(checkRevocation));
        cfg.setProperty("VERIFY-REVOCATION-ALLWAYS", Boolean.toString(checkAllways));
    }

    @Override
    public void setConfiguration(Properties cfg) {
        jcbPKI.setModel(new DefaultComboBoxModel(getInstalledPKIs(cfg)));
        jcbPKI.setSelectedItem(cfg.getProperty("DEFAULT-PKI"));
        verifyCompliance = Boolean.parseBoolean(cfg.getProperty("VERIFY-COMPLIANCE"));
        jcbVerify.setSelected(verifyCompliance);
        setVerifyComplienceEnabled(verifyCompliance);
        verifyAllways = Boolean.parseBoolean(cfg.getProperty("VERIFY-COMPLIANCE-ALLWAYS"));
        if (verifyAllways) jrbAllways.setSelected(true); else jrbOnce.setSelected(true);
        checkRevocation = Boolean.parseBoolean(cfg.getProperty("VERIFY-REVOCATION"));
        jcbVerifyRevocation.setSelected(checkRevocation);
        setVerifyRevocationEnabled(checkRevocation);
        checkAllways = Boolean.parseBoolean(cfg.getProperty("VERIFY-REVOCATION-ALLWAYS"));
        if (checkAllways) jrbCheckAllways.setSelected(true); else jrbCheckOnce.setSelected(true);
    }

    private String[] getInstalledPKIs(Properties cfg) {
        String[] ret = new String[0];
        try {
            int installed = Integer.parseInt(cfg.getProperty("INSTALLED-PKIS"));
            ret = new String[installed];
            for (int i = 1; i <= installed; i++) ret[i - 1] = cfg.getProperty("PKI-NAME-" + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
