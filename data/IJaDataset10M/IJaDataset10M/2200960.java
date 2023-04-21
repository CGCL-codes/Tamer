package example.gui.util.resizable;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ResizableCodeBoxExampleFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -15386982010998710L;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel mainPanel;

    private JScrollPane codeScrollPane;

    private JTextField classPathAndNameTextField;

    private JButton changeClassButton;

    private JPanel classPanel;

    private JTextField lineWrapperOptionsPathAndNameTextField;

    private JButton changeLineWrapperOptionsButton;

    private JPanel lineWrapperOptionsPanel;

    private JTextArea codeTextArea;

    public ResizableCodeBoxExampleFrame() {
        super();
        initGUI();
        DefaultCaret caret = (DefaultCaret) codeTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    }

    private void initGUI() {
        try {
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setTitle("Resizable Code Box Example");
            {
                mainPanel = new JPanel();
                GridBagLayout mainPanelLayout = new GridBagLayout();
                getContentPane().add(mainPanel, BorderLayout.CENTER);
                mainPanelLayout.rowWeights = new double[] { 0.0, 0.0, 1.0 };
                mainPanelLayout.rowHeights = new int[] { 7, 7, 7 };
                mainPanelLayout.columnWeights = new double[] { 0.1 };
                mainPanelLayout.columnWidths = new int[] { 7 };
                mainPanel.setLayout(mainPanelLayout);
                {
                    classPanel = new JPanel();
                    mainPanel.add(classPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                    GridBagLayout classPanelLayout = new GridBagLayout();
                    classPanelLayout.columnWidths = new int[] { 7, 7 };
                    classPanelLayout.rowHeights = new int[] { 7 };
                    classPanelLayout.columnWeights = new double[] { 1.0, 0.0 };
                    classPanelLayout.rowWeights = new double[] { 0.1 };
                    classPanel.setBorder(BorderFactory.createTitledBorder("Class"));
                    classPanel.setLayout(classPanelLayout);
                    {
                        changeClassButton = new JButton();
                        classPanel.add(changeClassButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
                        changeClassButton.setText("Change Class");
                    }
                    {
                        classPathAndNameTextField = new JTextField();
                        classPanel.add(classPathAndNameTextField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
                        classPathAndNameTextField.setEditable(false);
                    }
                }
                {
                    lineWrapperOptionsPanel = new JPanel();
                    mainPanel.add(lineWrapperOptionsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                    GridBagLayout jPanel1Layout = new GridBagLayout();
                    jPanel1Layout.columnWidths = new int[] { 7, 7 };
                    jPanel1Layout.rowHeights = new int[] { 7 };
                    jPanel1Layout.columnWeights = new double[] { 1.0, 0.0 };
                    jPanel1Layout.rowWeights = new double[] { 0.1 };
                    lineWrapperOptionsPanel.setBorder(BorderFactory.createTitledBorder("Line Wrapper"));
                    lineWrapperOptionsPanel.setLayout(jPanel1Layout);
                    {
                        changeLineWrapperOptionsButton = new JButton();
                        lineWrapperOptionsPanel.add(changeLineWrapperOptionsButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
                        changeLineWrapperOptionsButton.setText("Change Options");
                    }
                    {
                        lineWrapperOptionsPathAndNameTextField = new JTextField();
                        lineWrapperOptionsPanel.add(lineWrapperOptionsPathAndNameTextField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
                        lineWrapperOptionsPathAndNameTextField.setEditable(false);
                    }
                }
                {
                    codeScrollPane = new JScrollPane();
                    mainPanel.add(codeScrollPane, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                    codeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    codeScrollPane.getHorizontalScrollBar().setEnabled(false);
                    {
                        codeTextArea = new JTextArea();
                        codeScrollPane.setViewportView(getCodeTextArea());
                        codeTextArea.setEditable(false);
                    }
                }
            }
            pack();
            this.setSize(450, 650);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JTextArea getCodeTextArea() {
        return codeTextArea;
    }

    public JScrollPane getCodeScrollPane() {
        return codeScrollPane;
    }

    public JTextField getLineWrapperOptionsPathAndNameTextField() {
        return lineWrapperOptionsPathAndNameTextField;
    }

    public JButton getChangeLineWrapperOptionsButton() {
        return changeLineWrapperOptionsButton;
    }

    public JTextField getClassPathAndNameTextField() {
        return classPathAndNameTextField;
    }

    public JButton getChangeClassButton() {
        return changeClassButton;
    }
}
