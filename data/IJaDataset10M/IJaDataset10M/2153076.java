package pack;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class ToolTipComboBoxExample extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    String[] items = { "jw", "ja", "la" };

    String[] tooltips = { "Javanese ", "Japanese ", "Latin " };

    public ToolTipComboBoxExample() {
        super("ToolTip ComboBox Example");
        JComboBox combo = new JComboBox(items);
        combo.setRenderer(new MyComboBoxRenderer());
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(combo);
    }

    class MyComboBoxRenderer extends BasicComboBoxRenderer {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                if (-1 < index) {
                    list.setToolTipText(tooltips[index]);
                }
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception evt) {
        }
        ToolTipComboBoxExample frame = new ToolTipComboBoxExample();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(200, 140);
        frame.setVisible(true);
    }
}
