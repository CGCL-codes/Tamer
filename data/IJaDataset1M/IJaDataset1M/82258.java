package hermes.browser.dialog;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

/**
 * A simple dialog that shows the properties of a bean - only deals with basic types.
 * 
 * @author colincrist@hermesjms.com
 * @version $Id: MapPropertyDialog.java,v 1.4 2006/02/08 09:17:08 colincrist Exp $
 */
public class MapPropertyDialog extends StandardDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2201822635263036312L;

    private MapPropertyPanel mapPanel;

    /**
     * @throws java.awt.HeadlessException
     */
    public MapPropertyDialog(Frame owner, String headerText, String comment, Map map, boolean editable) throws HeadlessException {
        super(owner, headerText, true);
        mapPanel = new MapPropertyPanel(comment, map, editable);
        mapPanel.init();
        pack();
        setLocationRelativeTo(null);
    }

    public JComponent createBannerPanel() {
        return null;
    }

    public JComponent createContentPanel() {
        return mapPanel;
    }

    public ButtonPanel createButtonPanel() {
        final ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.RIGHT);
        AbstractAction okButtonAction = new AbstractAction(UIManager.getString("OptionPane.okButtonText")) {

            private static final long serialVersionUID = -7972988497933837263L;

            public void actionPerformed(ActionEvent arg0) {
                mapPanel.doOK();
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                dispose();
            }
        };
        AbstractAction cancelButtonAction = new AbstractAction(UIManager.getString("OptionPane.cancelButtonText")) {

            private static final long serialVersionUID = -4199961023918150328L;

            public void actionPerformed(ActionEvent arg0) {
                mapPanel.doCancel();
                setDialogResult(RESULT_CANCELLED);
                setVisible(false);
                dispose();
            }
        };
        JButton okButton = new JButton(okButtonAction);
        JButton cancelButton = new JButton(cancelButtonAction);
        buttonPanel.addButton(okButton);
        buttonPanel.addButton(cancelButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cancelButton.setEnabled(mapPanel.isEditable());
        return buttonPanel;
    }

    public void addOKAction(Runnable r) {
        mapPanel.addOKAction(r);
    }
}
