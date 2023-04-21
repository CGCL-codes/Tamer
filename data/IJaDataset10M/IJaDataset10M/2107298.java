package hermes.browser.dialog;

import hermes.Domain;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JTable;
import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.grid.EditorContext;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: DomainCellEditor.java,v 1.4 2005/02/25 08:41:49 colincrist Exp $
 */
public class DomainCellEditor extends ContextSensitiveCellEditor {

    private static final long serialVersionUID = 2889094946400633095L;

    private static Object options[] = { Domain.UNKNOWN, Domain.QUEUE, Domain.TOPIC };

    public static final EditorContext CONTEXT = new EditorContext("JMSDomain");

    public Domain selection = Domain.QUEUE;

    public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
        final JComboBox combo = new JComboBox(options);
        combo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                selection = (Domain) combo.getSelectedItem();
            }
        });
        return combo;
    }

    public Object getCellEditorValue() {
        return selection;
    }
}
