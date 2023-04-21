package de.psychomatic.mp3db.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Dialog extended with "Yes to all" and "No to all"
 * @author Kykal
 */
public class MultiDialog extends JDialog implements ActionListener {

    /**
     * Creates the dialog with a dialog as parent
     * @param parent Parentdialog
     * @param type Type of this dialog
     */
    public MultiDialog(Dialog parent, String type) {
        super(parent);
        setResizable(false);
        setModal(true);
        buildGui(type);
    }

    /**
     * Creates the dialog with a frame as parent
     * @param parent Parentframe
     * @param type Type of this dialog
     */
    public MultiDialog(Frame parent, String type) {
        super(parent);
        setResizable(false);
        setModal(true);
        buildGui(type);
    }

    /**
     * Creates the dialog
     * @param type Type of this dialog
     */
    private void buildGui(String type) {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 10));
        _textLabel = new JLabel();
        if (type != null) {
            Object icon = UIManager.getDefaults().get(type);
            if (icon != null && icon instanceof Icon) {
                p1.add(new JLabel((Icon) icon));
                _textLabel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
            }
        }
        p1.add(_textLabel);
        c.add(p1, BorderLayout.NORTH);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 5));
        JButton yes = new JButton("Ja");
        yes.setActionCommand("YES");
        yes.addActionListener(this);
        JButton yesToAll = new JButton("Ja f�r alle");
        yesToAll.setActionCommand("YESTOALL");
        yesToAll.addActionListener(this);
        JButton no = new JButton("Nein");
        no.setActionCommand("NO");
        no.addActionListener(this);
        JButton noToAll = new JButton("Nein f�r alle");
        noToAll.setActionCommand("NOTOALL");
        noToAll.addActionListener(this);
        JButton cancel = new JButton("Abbrechen");
        cancel.setActionCommand("CANCEL");
        cancel.addActionListener(this);
        buttons.add(yes);
        buttons.add(yesToAll);
        buttons.add(no);
        buttons.add(noToAll);
        buttons.add(cancel);
        c.add(buttons, BorderLayout.SOUTH);
        pack();
    }

    /**
     * Shows the dialog and returns the selected value. If chosen "yes to all" or "no to all", it returns
     * next time only "yes" or "no" without showing the dialog. In some cases the variable of the dialog
     * has to be static, because this prevents the dialog forgetting the choose
     * @param text Text in the dialog
     * @return int YES/NO/CANCEL
     */
    public int showDialog(String text) {
        if (_result == YESTOALL) return YES;
        if (_result == NOTOALL) return NO;
        _result = CANCEL;
        setLocationRelativeTo(getOwner());
        _textLabel.setText(text);
        pack();
        setVisible(true);
        if (_result == YESTOALL) return YES;
        if (_result == NOTOALL) return NO;
        return _result;
    }

    /**
     * Sets the return value
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("YES")) {
            _result = YES;
        } else if (e.getActionCommand().equals("YESTOALL")) {
            _result = YESTOALL;
        } else if (e.getActionCommand().equals("NO")) {
            _result = NO;
        } else if (e.getActionCommand().equals("NOTOALL")) {
            _result = NOTOALL;
        }
        setVisible(false);
        dispose();
    }

    /**
     * Static method for a single dialog. Creates the dialog static and throws an IllegalArgumentException
     * if the owner is neither type Dialog nor type Frame
     * @param owner The ownercomponent
     * @param type Messagetype
     */
    public static void setNewDialog(Component owner, String type) {
        if (owner instanceof Frame || owner == null) {
            _dialog = new MultiDialog((Frame) owner, type);
        } else if (owner instanceof Dialog) {
            _dialog = new MultiDialog((Dialog) owner, type);
        } else {
            throw new IllegalArgumentException("Illegal owner");
        }
    }

    /**
     * Displays the dialog. Throws an IllegalArgumentException if <code>setNewDialog</code> wasn't
     * called before
     * @param text Text in the dialog
     * @return int YES/NO/CANCEL
     */
    public static int showStaticDialog(String text) {
        if (_dialog == null) throw new IllegalArgumentException("No dialog found. Use setNewDialog");
        return _dialog.showDialog(text);
    }

    /**
     * Type of question dialog
     */
    public static final String QUESTION_DIALOG = "OptionPane.questionIcon";

    /**
     * Type of error dialog
     */
    public static final String ERROR_DIALOG = "OptionPane.errorIcon";

    /**
     * Type of information dialog
     */
    public static final String INFORMATION_DIALOG = "OptionPane.informationIcon";

    /**
     * Type of warning dialog
     */
    public static final String WARNING_DIALOG = "OptionPane.warningIcon";

    /**
     * Cancel-constant
     */
    public static final int CANCEL = 0;

    /**
     * Yes-constant
     */
    public static final int YES = 1;

    /**
     * No-constant
     */
    public static final int NO = 2;

    /**
     * No-to-all-constant
     */
    private static final int NOTOALL = 3;

    /**
     * Yes-to-all-constant
     */
    private static final int YESTOALL = 4;

    /**
     * Label to show text
     */
    private JLabel _textLabel;

    /**
     * Resultvariable
     */
    private int _result;

    /**
     * Static dialog
     */
    private static MultiDialog _dialog;
}
