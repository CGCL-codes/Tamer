package org.fudaa.fudaa.sig;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuLabel;
import org.fudaa.ctulu.CtuluLib;

/**
 * @author fred deniger
 * @version $Id: FSigWizardDefaultPanel.java,v 1.2 2006-08-17 16:35:16 deniger Exp $
 */
public class FSigWizardDefaultPanel extends JPanel {

    private final BuLabel lbFilesError_;

    JRootPane dialog_;

    public FSigWizardDefaultPanel() {
        super(new BuBorderLayout());
        lbFilesError_ = new BuLabel();
        lbFilesError_.setForeground(Color.RED);
        add(lbFilesError_, BuBorderLayout.NORTH);
        setErrorDefaultText();
    }

    protected void setErrorDefaultText() {
        lbFilesError_.setText("<html><body><br></body></html>");
    }

    public JRootPane getDialog() {
        return dialog_;
    }

    public final void setParentRootPane(final JRootPane _dialog) {
        dialog_ = _dialog;
    }

    public String valideAndGetError() {
        return null;
    }

    public final boolean valideData() {
        final String error = valideAndGetError();
        lbFilesError_.setForeground(error == null ? CtuluLib.getDefaultLabelForegroundColor() : Color.RED);
        if (error == null) {
            setErrorDefaultText();
        } else {
            lbFilesError_.setText("<html><body>" + error + "</body></html>");
        }
        return error == null;
    }
}
