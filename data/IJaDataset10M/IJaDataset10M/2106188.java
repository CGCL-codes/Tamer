package artem.finance.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * This class is header renderer for table, witch shows Dogovora objects in "Search & Edit dog's" form.
 * @author bia
 *
 */
public class DogHeaderRenderer2 extends JPanel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private JPanel panel;

    private Properties properties;

    /**
	 * This method returns JPanel component with many labels with information.
	 */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int raw, int col) {
        panel = new JPanel();
        panel.setBounds(new Rectangle(0, 0, 900, 50));
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        Rectangle rect = new Rectangle();
        JLabel regNuml = new JLabel(properties.getProperty("regNum"));
        rect = new Rectangle(2, 2, 80, 24);
        regNuml.setBounds(rect);
        panel.add(regNuml, null);
        JLabel regDatel = new JLabel("Data reg.");
        rect = new Rectangle(84, 2, 80, 24);
        regDatel.setBounds(rect);
        panel.add(regDatel, null);
        JLabel dogNuml = new JLabel("Nomer dog.");
        rect = new Rectangle(166, 2, 80, 24);
        dogNuml.setBounds(rect);
        panel.add(dogNuml, null);
        JLabel srokl = new JLabel("Srok dogovora     s:   po:");
        rect = new Rectangle(248, 2, 162, 24);
        srokl.setBounds(rect);
        panel.add(srokl, null);
        JLabel sumDogl = new JLabel("Summa");
        rect = new Rectangle(412, 2, 80, 24);
        sumDogl.setBounds(rect);
        panel.add(sumDogl, null);
        JLabel periodl = new JLabel("Period");
        rect = new Rectangle(494, 2, 80, 24);
        periodl.setBounds(rect);
        panel.add(periodl, null);
        JLabel orgl = new JLabel("Organizazija");
        rect = new Rectangle(576, 2, 120, 24);
        orgl.setBounds(rect);
        panel.add(orgl, null);
        JLabel sluzhbal = new JLabel("Sluzhba");
        rect = new Rectangle(698, 2, 120, 24);
        sluzhbal.setBounds(rect);
        panel.add(sluzhbal, null);
        JLabel otvetstvennijl = new JLabel("Otvetstvennij");
        rect = new Rectangle(820, 2, 100, 24);
        otvetstvennijl.setBounds(rect);
        panel.add(otvetstvennijl, null);
        JLabel vall = new JLabel("Valuta");
        rect = new Rectangle(84, 27, 80, 24);
        vall.setBounds(rect);
        panel.add(vall, null);
        JLabel naznl = new JLabel("Naznachenie");
        rect = new Rectangle(166, 27, 530, 24);
        naznl.setBounds(rect);
        panel.add(naznl, null);
        JLabel podsluzhbal = new JLabel("Podsluzhba");
        rect = new Rectangle(698, 27, 120, 24);
        podsluzhbal.setBounds(rect);
        panel.add(podsluzhbal, null);
        JLabel tell = new JLabel("Telefon");
        rect = new Rectangle(820, 27, 80, 24);
        tell.setBounds(rect);
        panel.add(tell, null);
        return panel;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}
