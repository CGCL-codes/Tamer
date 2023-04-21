package net.sf.cplab.headtracker.ui;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import net.sf.cplab.headtracker.ui.swing.MessageWindowImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jtse
 *
 */
public class SwingApplicationImpl implements SwingApplication {

    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(SwingApplicationImpl.class);

    private JFrame applicationFrame;

    private MessageWindowImpl messageWindow;

    private boolean isDisposed;

    public SwingApplicationImpl(String title) {
        this(title, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    }

    public SwingApplicationImpl(String title, GraphicsConfiguration gc) {
        applicationFrame = new JFrame(gc);
        applicationFrame.setTitle(title);
        applicationFrame.setLocationRelativeTo(null);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        isDisposed = false;
        messageWindow = new MessageWindowImpl(new JDialog(applicationFrame));
        messageWindow.getComponent().setLocationRelativeTo(applicationFrame);
    }

    public JTextField getTextFieldInstance(String name, String value) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        JTextField field = new JTextField(value);
        field.setName(name);
        return field;
    }

    public JFrame getFrameInstance(String title) {
        JFrame frame = new JFrame(title, applicationFrame.getGraphicsConfiguration());
        frame.setTitle(title);
        return frame;
    }

    public JFrame getFrame() {
        return applicationFrame;
    }

    public SwingApplication dispose() {
        applicationFrame.dispose();
        isDisposed = true;
        return this;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public JLabel getLabelInstance(String name, String value) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        JLabel label = new JLabel(value);
        label.setName(name);
        return label;
    }

    public JDialog getDialogInstance(String title, boolean isModal) {
        JDialog dialog = new JDialog(applicationFrame);
        dialog.setTitle(title);
        dialog.setModal(isModal);
        return dialog;
    }

    public MessageWindow getMessageWindow() {
        return messageWindow;
    }

    public JWindow getWindowInstance() {
        return new JWindow(applicationFrame);
    }
}
