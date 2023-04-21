package tr.swing.editorpane;

import javax.swing.JPanel;

/**
 * Panel for URL entry.
 *
 * @author Jeremy Moore (jimoore@netscape.net.au)
 */
final class URLDialogPanel extends JPanel {

    /** Creates new form URLPanel */
    public URLDialogPanel() {
        initComponents();
    }

    private void initComponents() {
        urlLabel = new javax.swing.JLabel();
        urlTextField = new javax.swing.JTextField();
        urlHintLabel = new javax.swing.JLabel();
        textLabel = new javax.swing.JLabel();
        textHintLabel = new javax.swing.JLabel();
        textTextField = new javax.swing.JTextField();
        setPreferredSize(new java.awt.Dimension(500, 130));
        urlLabel.setText(org.openide.util.NbBundle.getMessage(URLDialogPanel.class, "url"));
        urlHintLabel.setForeground(javax.swing.UIManager.getDefaults().getColor("controlDkShadow"));
        urlHintLabel.setText(org.openide.util.NbBundle.getMessage(URLDialogPanel.class, "url.hint"));
        textLabel.setText(org.openide.util.NbBundle.getMessage(URLDialogPanel.class, "text"));
        textHintLabel.setForeground(javax.swing.UIManager.getDefaults().getColor("controlDkShadow"));
        textHintLabel.setText(org.openide.util.NbBundle.getMessage(URLDialogPanel.class, "text.hint"));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(urlLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(urlTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(textLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(7, 7, 7).add(textTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(urlHintLabel).add(textHintLabel)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(urlLabel).add(urlTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(2, 2, 2).add(urlHintLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(textLabel).add(textTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(2, 2, 2).add(textHintLabel).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    public void initView() {
        urlTextField.requestFocusInWindow();
        urlTextField.setText(DEFAULT_START);
        urlTextField.setCaretPosition(DEFAULT_START.length());
    }

    public String getURL() {
        return urlTextField.getText();
    }

    public String getLinkText() {
        return textTextField.getText();
    }

    private javax.swing.JLabel textHintLabel;

    private javax.swing.JLabel textLabel;

    private javax.swing.JTextField textTextField;

    private javax.swing.JLabel urlHintLabel;

    private javax.swing.JLabel urlLabel;

    private javax.swing.JTextField urlTextField;

    public static final String DEFAULT_START = "http://www.";
}
