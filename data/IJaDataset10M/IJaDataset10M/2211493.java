package com.andrewj.parachute.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import com.andrewj.parachute.core.*;

/**
 *
 * @author Andrew Jobson (andyjobson85@gmail.com)
 * @author Paul Kenny (abgorn@gmail.com)
 */
public class AboutDialog extends JDialog implements HyperlinkListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private final String RSC_LOC = "/com/andrewj/parachute/rsc/";

    private JPanel jPanel = null;

    private JLabel jLabel = null;

    private JScrollPane jScrollPane = null;

    private JEditorPane jEditorPane = null;

    private JButton butClose = null;

    private JButton butCheckVers = null;

    private JPanel pnlButtons = null;

    public AboutDialog(Frame parent) {
        super(parent, true);
        initialize();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setSize(new Dimension(544, 424));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("About");
        this.setContentPane(getJPanel());
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.anchor = GridBagConstraints.EAST;
            gridBagConstraints3.insets = new Insets(5, 0, 5, 5);
            gridBagConstraints3.gridy = 2;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.BOTH;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;
            gridBagConstraints4.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints4.gridx = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints.gridy = 1;
            jLabel = new JLabel();
            jLabel.setText("");
            jLabel.setIcon(new ImageIcon(getClass().getResource(RSC_LOC + "parachute.png")));
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(jLabel, gridBagConstraints);
            jPanel.add(getJScrollPane(), gridBagConstraints4);
            jPanel.add(getPnlButtons(), gridBagConstraints3);
        }
        return jPanel;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJEditorPane());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
    private JEditorPane getJEditorPane() {
        if (jEditorPane == null) {
            try {
                Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getClass().getResourceAsStream(RSC_LOC + "about.html"));
                XPath xPath = XPathFactory.newInstance().newXPath();
                Element e = (Element) (xPath.evaluate("/html/head/h2", d, XPathConstants.NODE));
                e.setTextContent("Version: " + new VersionChecker().getCurrentVersion());
                StringWriter stringWriter = new StringWriter();
                Result result = new StreamResult(stringWriter);
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(d), result);
                jEditorPane = new JEditorPane("text/html", stringWriter.getBuffer().toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jEditorPane.addHyperlinkListener(this);
                jEditorPane.setEditable(false);
                jEditorPane.setCaretPosition(0);
            }
        }
        return jEditorPane;
    }

    /**
	 * This method initializes butClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        if (butClose == null) {
            butClose = new JButton();
            butClose.setText("Close");
            butClose.addActionListener(this);
        }
        return butClose;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == butClose) {
            this.dispose();
        }
        if (e.getSource() == butCheckVers) {
            try {
                new VersionChecker().checkVersionWithDialog(this, false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error checking for latest version: " + ex.getMessage(), this.getTitle(), JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.BROWSE)) {
                try {
                    d.browse(e.getURL().toURI());
                } catch (Exception e1) {
                }
            }
        }
    }

    /**
	 * This method initializes butCheckVers	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getButCheckVers() {
        if (butCheckVers == null) {
            butCheckVers = new JButton();
            butCheckVers.setText("Check for new version");
            butCheckVers.addActionListener(this);
        }
        return butCheckVers;
    }

    /**
	 * This method initializes pnlButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlButtons() {
        if (pnlButtons == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = -1;
            gridBagConstraints2.insets = new Insets(0, 0, 0, 5);
            gridBagConstraints2.gridy = -1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.anchor = GridBagConstraints.CENTER;
            gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints1.gridx = -1;
            gridBagConstraints1.gridy = -1;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            pnlButtons = new JPanel();
            pnlButtons.setLayout(new GridBagLayout());
            pnlButtons.add(getButCheckVers(), gridBagConstraints2);
            pnlButtons.add(getJButton(), gridBagConstraints1);
        }
        return pnlButtons;
    }
}
