package org.xfc.dialog;

import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.xfc.XApp;
import org.xfc.components.XMultilineLabel;
import org.xfc.util.XUtils;
import com.jgoodies.forms.layout.FormLayout;

/**
 *
 *
 * @author Devon Carew
 */
public class XErrorDialog extends XDialog {

    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;

    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;

    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;

    private static ImageIcon ERROR_ICON = XApp.getApp().getResources().getIcon("/firefox/skin/classic/global/icons/Error.png");

    private static ImageIcon WARNING_ICON = XApp.getApp().getResources().getIcon("/firefox/skin/classic/global/icons/Warning.png");

    private static ImageIcon INFO_ICON = XApp.getApp().getResources().getIcon("/firefox/skin/classic/global/icons/Question.png");

    private static final int MIN_DIALOG_WIDTH = 300;

    private JLabel iconLabel;

    private XMultilineLabel messageLabel;

    private XMultilineLabel subMessageLabel;

    private int dialogType = ERROR_MESSAGE;

    private JComponent detailsComponent;

    private Action showExceptionAction = new AbstractAction("Details...") {

        public void actionPerformed(ActionEvent event) {
            detailsComponent.setVisible(true);
            pack();
            center();
            showExceptionAction.setEnabled(false);
        }
    };

    /**
	 * 
	 */
    public XErrorDialog() {
        this(null, null, null);
    }

    public XErrorDialog(String title, String message) {
        this(title, message, null);
    }

    public XErrorDialog(String title, String message, Throwable exception) {
        super(XApp.getApp().getActiveAppFrame());
        setResizable(false);
        setModal(true);
        setOKAction(createDefaultCloseAction());
        iconLabel = new JLabel(ERROR_ICON);
        messageLabel = new XMultilineLabel();
        messageLabel.setPreferredWidth(400);
        subMessageLabel = new XMultilineLabel();
        subMessageLabel.setPreferredWidth(400);
        JPanel contents = new JPanel(new FormLayout("pref 12px 12px pref:grow", "pref 6px pref:grow pref"));
        contents.add(iconLabel, "1, 1, 1, 3, l, t");
        contents.add(messageLabel, "3, 1, 2, 1");
        contents.add(subMessageLabel, "4, 3");
        if (exception != null) {
            addAction(showExceptionAction, XDialog.HELP_POSITION);
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setRows(6);
            textArea.setColumns(80);
            textArea.setText(getStackTrace(exception));
            textArea.select(0, 0);
            JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVisible(false);
            contents.add(scrollPane, "1, 4, 4, 1, f, f");
            detailsComponent = scrollPane;
        }
        setContents(contents);
        construct();
        if (title != null) setTitle(title);
        if (message != null) setMessage(message);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setSubMessage(String subMessage) {
        subMessageLabel.setText(subMessage);
    }

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        if (dialogType != ERROR_MESSAGE && dialogType != WARNING_MESSAGE && dialogType != INFORMATION_MESSAGE) throw new IllegalArgumentException("invalid value for dialogType");
        this.dialogType = dialogType;
        if (dialogType == ERROR_MESSAGE) iconLabel.setIcon(ERROR_ICON); else if (dialogType == WARNING_MESSAGE) iconLabel.setIcon(WARNING_ICON); else iconLabel.setIcon(INFO_ICON);
    }

    public void pack() {
        super.pack();
        if (getWidth() < MIN_DIALOG_WIDTH) setSize(MIN_DIALOG_WIDTH, getHeight());
    }

    public static void showError(String title, String message) {
        showDlog(title, message, null, ERROR_MESSAGE);
    }

    public static void showError(String title, String message, String subMessage) {
        showDlog(title, message, subMessage, ERROR_MESSAGE);
    }

    public static void showError(Throwable exception) {
        showError(XUtils.getShortName(exception.getClass()), exception);
    }

    public static void showError(String title, Throwable exception) {
        XErrorDialog dialog = new XErrorDialog(title, exception.toString(), exception);
        dialog.show();
    }

    public static void showWarning(String title, String message) {
        showDlog(title, message, null, WARNING_MESSAGE);
    }

    public static void showWarning(String title, String message, String subMessage) {
        showDlog(title, message, subMessage, WARNING_MESSAGE);
    }

    public static void showInfo(String title, String message) {
        showDlog(title, message, null, INFORMATION_MESSAGE);
    }

    public static void showInfo(String title, String message, String subMessage) {
        showDlog(title, message, subMessage, INFORMATION_MESSAGE);
    }

    private static void showDlog(String title, String message, String subMessage, int type) {
        XErrorDialog dialog = new XErrorDialog(title, message);
        if (subMessage != null) dialog.setSubMessage(subMessage);
        dialog.setDialogType(type);
        dialog.show();
    }

    private String getStackTrace(Throwable t) {
        StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));
        return out.toString();
    }
}
