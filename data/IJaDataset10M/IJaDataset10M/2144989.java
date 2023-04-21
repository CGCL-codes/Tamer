package components.skroltext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import components.buton.Left.JButonLeft;
import components.buton.right.JButonRight;
import cor.ComponentTextData;

public abstract class SkrolText extends JComponent {

    private final JButonLeft btnlftTxt;

    private final JPanel panel;

    private final JSeparator separator_1;

    private final JPanel panel_3;

    private final JSeparator separator_2;

    private final JSeparator separator_3;

    private final JSeparator separator;

    private final JButonRight butonRight;

    private final JPanel panel_1;

    public final JMenuBar menuBar;

    public static ComponentTextData componentTextData = ComponentTextData.getInstance();

    private final JPanel panel_2;

    private final JPanel panel_4;

    private final JMenuBar menuBar_1;

    private final JMenuBar menuBar_2;

    public SkrolText() {
        setLayout();
        btnlftTxt = new JButonLeft();
        btnlftTxt.setIcon(new ImageIcon(SkrolText.class.getResource("/com/sun/java/swing/plaf/motif/icons/ScrollLeftArrowActive.gif")));
        btnlftTxt.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnlftTxt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                calendarDavn();
            }
        });
        add(btnlftTxt, "1, 2, 3, 1, fill, fill");
        panel = new JPanel();
        add(panel, "4, 2, fill, fill");
        panel.setLayout(new BorderLayout(0, 0));
        separator_1 = new JSeparator();
        panel.add(separator_1, BorderLayout.SOUTH);
        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("min:grow"), ColumnSpec.decode("center:max(40dlu;min)"), ColumnSpec.decode("min:grow") }, new RowSpec[] { RowSpec.decode("30px:grow") }));
        panel_2 = new JPanel();
        panel_3.add(panel_2, "1, 1, fill, fill");
        panel_2.setLayout(new GridLayout(0, 1, 0, 0));
        menuBar_1 = new JMenuBar();
        menuBar_1.setBorderPainted(false);
        panel_2.add(menuBar_1);
        panel_1 = new JPanel();
        panel_1.setLayout(new GridLayout(0, 1, 0, 0));
        panel_3.add(panel_1, "2, 1, fill, fill");
        menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        panel_1.add(menuBar);
        panel_4 = new JPanel();
        panel_3.add(panel_4, "3, 1, fill, fill");
        panel_4.setLayout(new GridLayout(0, 1, 0, 0));
        menuBar_2 = new JMenuBar();
        menuBar_2.setBorderPainted(false);
        panel_4.add(menuBar_2);
        separator_2 = new JSeparator();
        separator_2.setOrientation(SwingConstants.VERTICAL);
        panel.add(separator_2, BorderLayout.WEST);
        separator_3 = new JSeparator();
        separator_3.setOrientation(SwingConstants.VERTICAL);
        panel.add(separator_3, BorderLayout.EAST);
        separator = new JSeparator();
        panel.add(separator, BorderLayout.NORTH);
        butonRight = new JButonRight();
        butonRight.setFont(new Font("Tahoma", Font.BOLD, 12));
        butonRight.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                calendarUp();
            }
        });
        add(butonRight, "5, 2, 3, 1, fill, fill");
    }

    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void setLayout() {
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(26dlu;pref)"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("min:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(26dlu;pref)"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:34px:grow"), FormFactory.RELATED_GAP_ROWSPEC }));
    }

    public abstract Calendar calendarUp();

    public abstract Calendar calendarDavn();

    public abstract Calendar getCalendar();

    public abstract void setCalendar(Calendar calendar);
}
