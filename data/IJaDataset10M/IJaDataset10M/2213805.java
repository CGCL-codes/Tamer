package forms;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import components.skroltext.skrolday.SkrolDay;

public class RemindersForm extends JFrame {

    private final JTable table;

    private final JTable table_1;

    public RemindersForm() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(RemindersForm.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
        getContentPane().setLayout(new BorderLayout(0, 0));
        SkrolDay skrolDay = new SkrolDay();
        getContentPane().add(skrolDay, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow") }, new RowSpec[] { RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow") }));
        JPanel panel_2 = new JPanel();
        panel.add(panel_2, "1, 1, fill, fill");
        panel_2.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC }));
        table = new JTable();
        panel_2.add(table, "2, 2, fill, fill");
        JSeparator separator = new JSeparator();
        panel.add(separator, "1, 2");
        JPanel panel_1 = new JPanel();
        panel.add(panel_1, "1, 3, fill, fill");
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC }));
        table_1 = new JTable();
        panel_1.add(table_1, "2, 2, fill, fill");
    }
}
