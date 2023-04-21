package org.parosproxy.paros.extension;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.utils.FontHelper;

/**
 * Abstract base class for all dialog box.
 */
public abstract class AbstractDialog extends JDialog {

    private static final long serialVersionUID = -3951504408180103696L;

    protected AbstractDialog thisDialog = null;

    /**
	 * @throws HeadlessException
	 */
    public AbstractDialog() throws HeadlessException {
        super();
        initialize();
    }

    /**
	 * @param arg0
	 * @param arg1
	 * @throws HeadlessException
	 */
    public AbstractDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setVisible(false);
        this.setFont(FontHelper.getBaseFont());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(300, 200);
        this.setTitle(Constant.PROGRAM_NAME);
        addEscapeListener(this);
    }

    /**
	 * Centre this frame.
	 * 
	 */
    public void centreDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public void setVisible(boolean show) {
        if (show) {
            centreDialog();
        }
        super.setVisible(show);
    }

    /**
	 * Add an Escape Listener to the JDialog
	 * 
	 * @param	dialog
	 * @return	void
	 */
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };
        dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
