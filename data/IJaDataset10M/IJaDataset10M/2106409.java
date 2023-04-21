package org.moltools.apps.probemaker.swingui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.moltools.apps.probemaker.SwingUIMessages;
import org.moltools.apps.probemaker.ProbeMakerPlugIn;

public class ProbeMaker_AboutBox extends JDialog implements ActionListener {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    JPanel panel1 = new JPanel();

    JPanel mainPanel = new JPanel();

    JPanel buttonPanel = new JPanel();

    JPanel textPanel1 = new JPanel();

    JPanel textPanel2 = new JPanel();

    JPanel textPanel3 = new JPanel();

    JPanel textPanel4 = new JPanel();

    JButton okButton = new JButton();

    JButton viewButton = new JButton();

    Component view;

    JLabel label1 = new JLabel();

    JLabel label2 = new JLabel();

    JLabel label3 = new JLabel();

    JLabel label4 = new JLabel();

    JLabel label5 = new JLabel();

    JLabel label6 = new JLabel();

    JLabel label7 = new JLabel();

    JLabel label8 = new JLabel();

    JLabel label9 = new JLabel();

    JLabel label10 = new JLabel();

    JLabel label11 = new JLabel();

    String product = "ProbeMaker";

    String version = "Version " + ProbeMakerPlugIn.versionNumber + "." + ProbeMakerPlugIn.subVersionNumber;

    String copyright = SwingUIMessages.getString("AboutBox.COPYRIGHT");

    String comments = "Johan Stenberg";

    String cite = SwingUIMessages.getString("AboutBox.CITATION_INFO_HEADER");

    String authors = SwingUIMessages.getString("AboutBox.CITATION_INFO_AUTHORS");

    String title = SwingUIMessages.getString("AboutBox.CITATION_INFO_LINE1");

    String journal = SwingUIMessages.getString("AboutBox.CITATION_INFO_LINE2");

    String contact = SwingUIMessages.getString("AboutBox.COMMENTS_LINE1");

    String address = SwingUIMessages.getString("AboutBox.COMMENTS_LINE2");

    String license = SwingUIMessages.getString("AboutBox.LICENSE_HEADER");

    public ProbeMaker_AboutBox(Frame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ProbeMaker_AboutBox() {
        this(null);
    }

    /**Component initialization*/
    private void jbInit() throws Exception {
        setTitle(SwingUIMessages.getString("AboutBox.ABOUT_HEADER"));
        label1.setText(product);
        label2.setText(version);
        label3.setText(copyright);
        label4.setText(comments);
        label5.setText(cite);
        label6.setText(authors);
        label7.setText(title);
        label8.setText(journal);
        label9.setText(contact);
        label10.setText(address);
        label11.setText(license);
        JEditorPane licArea = new JEditorPane("text/html", "<html><body>" + SwingUIMessages.getString("AboutBox.LICENSE_P1") + SwingUIMessages.getString("AboutBox.LICENSE_P2") + SwingUIMessages.getString("AboutBox.LICENSE_P3"));
        licArea.setEditable(false);
        licArea.setCaretPosition(0);
        licArea.setPreferredSize(new Dimension(400, 400));
        view = new JScrollPane(licArea);
        okButton.setText(SwingUIMessages.getString("Common.CAPTION_CLOSE"));
        okButton.addActionListener(this);
        viewButton.setText("View license");
        viewButton.addActionListener(this);
        label5.setFont(label5.getFont().deriveFont(Font.BOLD));
        label1.setFont(label5.getFont().deriveFont((float) (label1.getFont().getSize() + 8)));
        label9.setFont(label5.getFont());
        label11.setFont(label5.getFont());
        textPanel1.setLayout(new GridLayout(4, 1));
        textPanel1.add(label2, null);
        textPanel1.add(label3, null);
        textPanel1.add(label4, null);
        textPanel2.setLayout(new GridLayout(4, 1));
        textPanel2.add(label5, null);
        textPanel2.add(label6, null);
        textPanel2.add(label7, null);
        textPanel2.add(label8, null);
        textPanel3.setLayout(new GridLayout(2, 1));
        textPanel3.add(label9, null);
        textPanel3.add(label10, null);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0);
        mainPanel.add(label1, con);
        con.gridy++;
        con.insets = new Insets(10, 20, 2, 2);
        mainPanel.add(textPanel1, con);
        con.gridy++;
        con.insets = new Insets(10, 2, 2, 2);
        mainPanel.add(textPanel2, con);
        con.gridy++;
        mainPanel.add(textPanel3, con);
        con.gridy++;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(viewButton);
        buttonPanel.add(okButton);
        panel1.setLayout(new BorderLayout());
        panel1.add(mainPanel, BorderLayout.CENTER);
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        JPanel cp = (JPanel) getContentPane();
        cp.add(panel1);
        cp.setPreferredSize(new Dimension(textPanel2.getPreferredSize().width + 40, cp.getPreferredSize().height));
        pack();
    }

    /**Overridden so we can exit when window is closed*/
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    /**Close the dialog*/
    void cancel() {
        dispose();
    }

    /**Close the dialog on a button event*/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            cancel();
        } else if (e.getSource() == viewButton) {
            JOptionPane.showMessageDialog(this, view, license, JOptionPane.PLAIN_MESSAGE);
        }
    }
}
