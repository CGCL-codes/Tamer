package artem.finance.gui;

import artem.finance.gui.Dogovor2;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author Burtsev Ivan
 *
 */
public class DogTableCellRenderer2 extends JPanel implements TableCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JPanel panel;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int raw, int column) {
        if (object instanceof Dogovor2) {
            Dogovor2 dog = (Dogovor2) object;
            Rectangle rect = new Rectangle();
            JTextField regNumtf = new JTextField();
            regNumtf.setEditable(true);
            rect = new Rectangle(2, 2, 80, 24);
            regNumtf.setBounds(rect);
            regNumtf.setText(dog.getRegistrazionniyNomer());
            JFormattedTextField regDateftf = new JFormattedTextField();
            rect = new Rectangle(84, 2, 80, 24);
            regDateftf.setBounds(rect);
            regDateftf.setText(String.valueOf(dog.getDataRegistrazii()));
            JTextField numDogtf = new JTextField();
            rect = new Rectangle(166, 2, 80, 24);
            numDogtf.setBounds(rect);
            numDogtf.setText(dog.getNaimenovanieUslugi());
            JFormattedTextField dateFromftf = new JFormattedTextField();
            rect = new Rectangle(248, 2, 80, 24);
            dateFromftf.setBounds(rect);
            dateFromftf.setText(String.valueOf(dog.getDataNachala()));
            JFormattedTextField dateToftf = new JFormattedTextField();
            rect = new Rectangle(330, 2, 80, 24);
            dateToftf.setBounds(rect);
            dateToftf.setText(String.valueOf(dog.getDataOkonchanija()));
            JFormattedTextField sumftf = new JFormattedTextField();
            rect = new Rectangle(412, 2, 80, 24);
            sumftf.setBounds(rect);
            sumftf.setText(String.valueOf(dog.getSummaDogovora()));
            JTextField periodtf = new JTextField();
            rect = new Rectangle(494, 2, 80, 24);
            periodtf.setBounds(rect);
            periodtf.setText(dog.getPeriod());
            JComboBox organizazijacb = new JComboBox();
            rect = new Rectangle(576, 2, 120, 24);
            organizazijacb.setBounds(rect);
            JComboBox cluzhbacb = new JComboBox();
            rect = new Rectangle(698, 2, 120, 24);
            cluzhbacb.setBounds(rect);
            JTextField otvetstvennijtf = new JTextField();
            rect = new Rectangle(820, 2, 100, 24);
            otvetstvennijtf.setBounds(rect);
            otvetstvennijtf.setText(dog.getOtvetstvennij());
            JTextField valutatf = new JTextField();
            rect = new Rectangle(84, 27, 80, 24);
            valutatf.setBounds(rect);
            valutatf.setText(dog.getValuta());
            JComboBox naznachenie = new JComboBox();
            rect = new Rectangle(166, 27, 530, 24);
            naznachenie.setBounds(rect);
            JComboBox podsluzhbacb = new JComboBox();
            rect = new Rectangle(698, 27, 120, 24);
            podsluzhbacb.setBounds(rect);
            JTextField teltf = new JTextField();
            rect = new Rectangle(820, 27, 80, 24);
            teltf.setBounds(rect);
            teltf.setText(dog.getTelefon());
            JLabel sostojanieDogl = new JLabel("Sostojanie dogovora:");
            rect = new Rectangle(500, 53, 140, 24);
            sostojanieDogl.setBounds(rect);
            JCheckBox sostojanieDogchb = new JCheckBox();
            rect = new Rectangle(650, 55, 20, 20);
            sostojanieDogchb.setBounds(rect);
            JLabel zakazl = new JLabel("Zakaz:");
            rect = new Rectangle(680, 53, 70, 24);
            zakazl.setBounds(rect);
            JTextField zakaztf = new JTextField();
            rect = new Rectangle(754, 53, 120, 24);
            zakaztf.setBounds(rect);
            zakaztf.setText(dog.getZakaz());
            panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(200, 200, 200));
            panel.add(regNumtf, null);
            panel.add(regDateftf, null);
            panel.add(numDogtf, null);
            panel.add(dateFromftf, null);
            panel.add(dateToftf, null);
            panel.add(sumftf, null);
            panel.add(periodtf, null);
            panel.add(organizazijacb, null);
            panel.add(cluzhbacb, null);
            panel.add(otvetstvennijtf, null);
            panel.add(valutatf, null);
            panel.add(naznachenie, null);
            panel.add(podsluzhbacb, null);
            panel.add(teltf, null);
            panel.add(sostojanieDogl, null);
            panel.add(sostojanieDogchb, null);
            panel.add(zakazl, null);
            panel.add(zakaztf, null);
        }
        return panel;
    }
}
