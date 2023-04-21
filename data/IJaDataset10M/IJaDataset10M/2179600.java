package eva.gui.tools;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.*;

public class JExceptionDialog extends JDialog {

    private static final Icon icon = UIManager.getIcon("OptionPane.errorIcon");

    ;

    private JLabel messageText = new JLabel();

    private JScrollPane scrollPane = new JScrollPane();

    private JTextArea stacktrace = new JTextArea();

    private JButton detailsButton = new JButton();

    private JButton okButton = new JButton();

    private GridBagLayout layout = new GridBagLayout();

    private static final boolean PRINT_TO_STDERR = true;

    public JExceptionDialog() {
        initGUI();
    }

    public JExceptionDialog(Throwable t) {
        setException(t);
        initGUI();
    }

    public void setException(Throwable t) {
        messageText.setText(t.getLocalizedMessage());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(bout));
        String str = bout.toString();
        if (str.length() == 0) {
            str = "(kein Stacktrace vorhanden)";
        }
        stacktrace.setText(str);
        updateLayout();
        pack();
        if (PRINT_TO_STDERR) {
            t.printStackTrace(System.err);
        }
    }

    public static void showExceptionDialog(Throwable t) {
        JExceptionDialog dlg = new JExceptionDialog(t);
        dlg.setVisible(true);
    }

    private void initGUI() {
        detailsButton.setAction(new AbstractAction("Details") {

            public void actionPerformed(ActionEvent e) {
                stacktrace.setVisible(!stacktrace.isVisible());
                scrollPane.setVisible(stacktrace.isVisible());
                updateLayout();
                pack();
            }
        });
        okButton.setAction(new AbstractAction("OK") {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        stacktrace.setVisible(false);
        stacktrace.setEditable(false);
        scrollPane.setViewportView(stacktrace);
        scrollPane.setVisible(false);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.getContentPane().setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridheight = 1;
        gbc.insets = new Insets(22, 12, 11, 17);
        addComponent(new JLabel(icon), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(12, 0, 0, 11);
        addComponent(messageText, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(12, 0, 11, 5);
        addComponent(okButton, gbc);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(12, 0, 11, 11);
        addComponent(detailsButton, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(6, 11, 11, 11);
        addComponent(scrollPane, gbc);
        int buttonWidth = detailsButton.getPreferredSize().width;
        int buttonHeight = detailsButton.getPreferredSize().height;
        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);
        okButton.setPreferredSize(buttonSize);
        detailsButton.setPreferredSize(buttonSize);
        this.setTitle("Fehler");
        this.setModal(true);
        this.updateLayout();
        this.pack();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addComponent(JComponent comp, GridBagConstraints gbc) {
        this.getContentPane().add(comp, gbc);
    }

    private void updateLayout() {
        Dimension scrollPaneDim = scrollPane.getPreferredSize();
        Dimension messageDim = messageText.getPreferredSize();
        Dimension dialogDim = getSize();
        Insets scrollPaneInsets = layout.getConstraints(scrollPane).insets;
        setMinimumSize(new Dimension(scrollPaneDim.width + scrollPaneInsets.left + scrollPaneInsets.right + 11, messageDim.height));
        if (scrollPane.isVisible()) {
            scrollPane.setPreferredSize(new Dimension(scrollPaneDim.width, dialogDim.height * 3));
        }
    }
}
