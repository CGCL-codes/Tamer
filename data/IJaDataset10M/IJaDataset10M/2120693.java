package com.global360.sketchpadbpmn.dialogs;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import com.global360.sketchpadbpmn.SketchpadBuildInfo;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class AboutDialogBox extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final int ICON_BORDER_SIZE = 8;

    private static final int TEXT_LABEL_BORDER_SIZE = 4;

    private static final int BUTTON_BORDER_SIZE = 8;

    JPanel buttonPanel = new JPanel();

    JPanel contentPanel = new JPanel();

    JButton okButton = new JButton();

    JLabel programIconLabel = makeLabel();

    JLabel productNameAndVersionLabel = makeLabel();

    JLabel buildIdLabel = makeLabel();

    JLabel copyrightLabel = makeLabel();

    ImageIcon largeProgramIcon = new ImageIcon();

    String product = SketchpadBuildInfo.getBuildName();

    String version = Messages.getString("MainFrame_AboutBox.vIsForVersion") + SketchpadBuildInfo.getMajorVersion() + "." + SketchpadBuildInfo.getMinorVersion();

    private static JLabel makeLabel() {
        JLabel result = DialogStyles.makeCenterLabel("");
        result.setBorder(DialogStyles.makeEmptyBorder(TEXT_LABEL_BORDER_SIZE));
        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        return result;
    }

    public AboutDialogBox(Frame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AboutDialogBox() {
        this(null);
    }

    private void jbInit() throws Exception {
        Container contentPane = this.getContentPane();
        contentPane.setBackground(SystemColor.window);
        contentPane.setLayout(new BorderLayout());
        this.setForeground(Color.black);
        this.setTitle(Messages.getString("MainFrame_AboutBox.About"));
        largeProgramIcon = getAboutBoxIcon();
        programIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        programIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        programIconLabel.setIcon(largeProgramIcon);
        programIconLabel.setBorder(DialogStyles.makeEmptyBorder(ICON_BORDER_SIZE));
        String productNameAndVersionText = getProgramNameAndVersion();
        productNameAndVersionLabel.setBackground(SystemColor.window);
        productNameAndVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productNameAndVersionLabel.setText(productNameAndVersionText);
        String buildId = getBuildId();
        buildIdLabel.setText(buildId);
        String copyrightText = getCopyrightText();
        copyrightLabel.setText(copyrightText);
        copyrightLabel.setBackground(Color.white);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        okButton.setText(Messages.getString("MainFrame_AboutBox.Ok"));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension preferredSize = okButton.getPreferredSize();
        preferredSize.width = largeProgramIcon.getIconWidth();
        okButton.setMaximumSize(preferredSize);
        okButton.addActionListener(this);
        this.getRootPane().setDefaultButton(okButton);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(this, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(okButton, BorderLayout.CENTER);
        buttonPanel.setBorder(DialogStyles.makeEmptyBorder(BUTTON_BORDER_SIZE));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(DialogStyles.makeEmptyBorder(BUTTON_BORDER_SIZE));
        contentPanel.add(programIconLabel);
        contentPanel.add(productNameAndVersionLabel);
        contentPanel.add(buildIdLabel);
        contentPanel.add(copyrightLabel);
        contentPanel.add(buttonPanel);
        contentPane.add(contentPanel, BorderLayout.CENTER);
        contentPane.add(contentPanel);
        setResizable(true);
    }

    protected String getBuildId() {
        return "Revision " + SketchpadBuildInfo.getBuildId();
    }

    protected String getCopyrightText() {
        return Messages.getString("MainFrame_AboutBox.Copyright") + " " + Messages.getString("MainFrame_AboutBox.CompanyName");
    }

    protected String getProgramNameAndVersion() {
        return Messages.getString("MainFrame_AboutBox.ProgramName") + ", " + product + " " + version;
    }

    protected ImageIcon getAboutBoxIcon() {
        ImageIcon result = com.global360.sketchpadbpmn.MainFrame.getImageIcon("Images/SketchpadBpmnIcon.png");
        return result;
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    void cancel() {
        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        cancel();
    }
}
