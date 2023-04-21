package ch.intertec.storybook.view.table.renderer;

import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import ch.intertec.storybook.SbConstants.ClientPropertyName;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.entity.Strand;
import ch.intertec.storybook.toolkit.swing.label.CleverLabel;
import ch.intertec.storybook.view.MainFrame;

@SuppressWarnings("serial")
public class StrandsTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        MainFrame mainFrame = (MainFrame) table.getClientProperty(ClientPropertyName.MAIN_FRAME.toString());
        JLabel lbText = (JLabel) super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        JPanel panel = new JPanel(new MigLayout("insets 2"));
        panel.setOpaque(true);
        panel.setBackground(lbText.getBackground());
        if (value instanceof String) {
            panel.add(new JLabel());
        } else {
            try {
                DocumentModel model = mainFrame.getDocumentModel();
                Session session = model.beginTransaction();
                @SuppressWarnings("unchecked") List<Strand> list = (List<Strand>) value;
                for (Strand strand : list) {
                    session.refresh(strand);
                    CleverLabel lb = new CleverLabel(strand.getAbbr());
                    lb.setBackground(strand.getJColor());
                    panel.add(lb);
                }
                model.commit();
            } catch (ClassCastException e) {
                return panel;
            }
        }
        return panel;
    }
}
