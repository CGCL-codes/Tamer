package net.sf.juife.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import static net.sf.juife.JuifeI18n.i18n;

/**
 * This class can be used to simplify the creation of dialogs with 'OK' and 'Cancel' buttons.
 * To create a dialog you only need to specify a <code>Container</code> that will be used as
 * a main pane in the dialog and to implement the
 * {@link EnhancedDialog#onOk} and {@link EnhancedDialog#onCancel} methods.
 * The main pane can be set either by using one of
 * the following constructors to create the dialog
 * <ul>
 *  <li>{@link #OkCancelDialog(Frame owner, Container mainPane)}
 *  <li>{@link #OkCancelDialog(Frame owner, String title, Container mainPane)}
 *  <li>{@link #OkCancelDialog(Frame owner, String title, Container mainPane, boolean modal)}
 *  <li>{@link #OkCancelDialog(Dialog owner, Container mainPane)}
 *  <li>{@link #OkCancelDialog(Dialog owner, String title, Container mainPane)}
 *  <li>{@link #OkCancelDialog(Dialog owner, String title, Container mainPane, boolean modal)}
 * </ul>
 * or by using the {@link #setMainPane} method.
 *
 * @author Grigor Iliev
 */
public abstract class OkCancelDialog extends EnhancedDialog {

    private JPanel pane = new JPanel(new BorderLayout());

    protected final JButton btnOk = new JButton(i18n.getButtonLabel("ok"));

    protected final JButton btnCancel = new JButton(i18n.getButtonLabel("cancel"));

    /**
	 * Creates a modal dialog without a title and with the specified
	 * <code>Frame</code> as its owner.
	 * @param owner Specifies the <code>Frame</code> from which this dialog is displayed.
	 */
    public OkCancelDialog(Frame owner) {
        this(owner, new Container());
    }

    /**
	 * Creates a modal dialog with the specified title and owner <code>Frame</code>.
	 * @param owner Specifies the <code>Frame</code> from which this dialog is displayed.
	 * @param title The text to be displayed in the dialog's title bar.
	 */
    public OkCancelDialog(Frame owner, String title) {
        this(owner, title, new Container());
    }

    /**
	 * Creates a modal dialog without a title and with the specified owner and main pane.
	 * @param owner Specifies the <code>Frame</code> from which this dialog is displayed.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public OkCancelDialog(Frame owner, Container mainPane) {
        this(owner, "", mainPane);
    }

    /**
	 * Creates a modal dialog with the specified title, owner and main pane.
	 * @param owner Specifies the <code>Frame</code> from which this dialog is displayed.
	 * @param title The text to be displayed in the dialog's title bar.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public OkCancelDialog(Frame owner, String title, Container mainPane) {
        this(owner, title, mainPane, true);
    }

    /**
	 * Creates a modal or non-modal dialog with the specified title, owner and main pane.
	 * @param owner Specifies the <code>Frame</code> from which this dialog is displayed.
	 * @param title The text to be displayed in the dialog's title bar.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @param modal Specifies whether this dialog should be modal or not.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public OkCancelDialog(Frame owner, String title, Container mainPane, boolean modal) {
        super(owner, title, modal);
        initOkCancelDialog();
        setMainPane(mainPane);
    }

    /**
	 * Creates a modal dialog without a title and with the specified
	 * <code>Dialog</code> as its owner.
	 * @param owner Specifies the <code>Dialog</code> from which this dialog is displayed.
	 */
    public OkCancelDialog(Dialog owner) {
        this(owner, new Container());
    }

    /**
	 * Creates a modal dialog with the specified title and owner <code>Dialog</code>.
	 * @param owner Specifies the <code>Dialog</code> from which this dialog is displayed.
	 * @param title The text to be displayed in the dialog's title bar.
	 */
    public OkCancelDialog(Dialog owner, String title) {
        this(owner, title, new Container());
    }

    /**
	 * Creates a modal dialog without a title and with the specified owner and main pane.
	 * @param owner Specifies the <code>Dialog</code> from which this dialog is displayed.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public OkCancelDialog(Dialog owner, Container mainPane) {
        this(owner, "", mainPane);
    }

    /**
	 * Creates a modal dialog with the specified title, owner and main pane.
	 * @param owner Specifies the <code>Dialog</code> from which this dialog is displayed.
	 * @param title The text to be displayed in the dialog's title bar.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public OkCancelDialog(Dialog owner, String title, Container mainPane) {
        this(owner, title, mainPane, true);
    }

    /**
	 * Creates a modal or non-modal dialog with the specified title, owner and main pane.
	 * @param owner Specifies the <code>Dialog</code> from which this dialog is displayed.
	 * @param title The text to be displayed in the dialog's title bar.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @param modal Specifies whether this dialog should be modal or not.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public OkCancelDialog(Dialog owner, String title, Container mainPane, boolean modal) {
        super(owner, title, modal);
        initOkCancelDialog();
        setMainPane(mainPane);
    }

    /** Used for initial initialization of the dialog */
    private void initOkCancelDialog() {
        pane.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(pane, BorderLayout.CENTER);
        Dimension d = JuifeUtils.getUnionSize(btnOk, btnCancel);
        btnOk.setPreferredSize(d);
        btnOk.setMaximumSize(d);
        btnCancel.setPreferredSize(d);
        btnCancel.setMaximumSize(d);
        JPanel btnPane = new JPanel();
        btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.X_AXIS));
        btnPane.add(Box.createGlue());
        btnPane.add(btnOk);
        btnPane.add(Box.createRigidArea(new Dimension(5, 0)));
        btnPane.add(btnCancel);
        btnPane.setBorder(BorderFactory.createEmptyBorder(17, 0, 0, 0));
        pane.add(btnPane, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocation(JuifeUtils.centerLocation(this, getOwner()));
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setCancelled(true);
                onCancel();
            }
        });
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setCancelled(false);
                onOk();
            }
        });
    }

    /**
	 * Sets the main pane of this dialog and centers the dialog relatvely to its owner.
	 * @param mainPane A non-null <code>Container</code> instance that will be used
	 * as a main pane for this dialog.
	 * @throws IllegalArgumentException If <code>mainPane</code> is <code>null</code>.
	 */
    public void setMainPane(Container mainPane) {
        if (mainPane == null) throw new IllegalArgumentException("mainPane must be non null");
        pane.add(mainPane, BorderLayout.CENTER);
        pack();
        setLocation(JuifeUtils.centerLocation(this, getOwner()));
    }
}
