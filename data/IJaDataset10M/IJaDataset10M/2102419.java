package de.gstpl.gui.dialog;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import de.gstpl.util.SwingHelper;
import de.gstpl.util.server.ServerProperties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Peter Karich
 */
public class ConnectionPropertySupport extends AbstractPropertyEditor {

    private DefaultCellRenderer label;

    private JButton button;

    private ServerProperties serverP;

    /** Creates a new instance of ConnectionPropertySupport */
    public ConnectionPropertySupport() {
        editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
        ((JPanel) editor).add("*", label = new DefaultCellRenderer());
        label.setOpaque(false);
        ((JPanel) editor).add(button = ComponentFactory.Helper.getFactory().createMiniButton());
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ConnectionPropDialog cp = new ConnectionPropDialog(null, true);
                SwingHelper.center(cp);
                cp.setVisible(true);
                if (cp.getReturnValue() == cp.SAVE) {
                    ServerProperties oldP = serverP;
                    serverP = cp.getServer();
                    label.setValue(serverP);
                    firePropertyChange(oldP, serverP);
                }
            }
        });
        ((JPanel) editor).setOpaque(false);
    }

    public Object getValue() {
        return serverP;
    }

    public void setValue(Object value) {
        label.setValue((ServerProperties) value);
    }
}
