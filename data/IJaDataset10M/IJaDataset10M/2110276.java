package cn.ekuma.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.openbravo.data.basic.BasicException;
import com.openbravo.editor.JEditorKeys;
import com.openbravo.editor.JEditorNumber;

public class JEditorNumberDialog extends AbstractKeyBoardJDialog {

    private final JPanel contentPanel = new JPanel();

    JEditorNumber number;

    private JEditorKeys editorKeys;

    /**
	 * @wbp.parser.constructor
	 */
    public JEditorNumberDialog(Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public JEditorNumberDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public static JEditorNumberDialog newJDialog(Component superComp, JEditorNumber number) {
        Window window = getWindow(superComp);
        JEditorNumberDialog mydialog;
        if (window instanceof Frame) {
            mydialog = new JEditorNumberDialog((Frame) window, true);
        } else {
            mydialog = new JEditorNumberDialog((Dialog) window, true);
        }
        mydialog.init(number);
        return mydialog;
    }

    private void init(JEditorNumber number2) {
        number = number2;
        contentPanel.add(number, BorderLayout.NORTH);
        number.addEditorKeys(editorKeys);
    }

    /**
	 * Create the dialog.
	 */
    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        enterKeyProc();
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        escapeKeyProc();
                    }
                });
                buttonPane.add(cancelButton);
            }
            editorKeys = new JEditorKeys();
            contentPanel.add(editorKeys, BorderLayout.CENTER);
        }
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 309) / 2, (screenSize.height - 382) / 2, 309, 382);
    }

    public void setDoubleValue(Double dvalue) {
        number.setDoubleValue(dvalue);
        number.activate();
    }

    public Double getDoubleValue() {
        return number.getDoubleValue();
    }

    public void setValueInteger(int ivalue) {
        number.setValueInteger(ivalue);
        number.activate();
    }

    public int getValueInteger() throws BasicException {
        return number.getValueInteger();
    }

    @Override
    protected void enterKeyProc() {
        dispose();
    }

    @Override
    protected void escapeKeyProc() {
        dispose();
    }

    @Override
    protected void upKeyProc() {
    }

    @Override
    protected void downKeyProc() {
    }
}
