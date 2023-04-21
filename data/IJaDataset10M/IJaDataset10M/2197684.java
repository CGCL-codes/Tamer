package tim;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MapInfoFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField ver = new JTextField(3);

    private JTextField name = new JTextField(11);

    private JTextField author = new JTextField(11);

    private NumberField bomb = new NumberField(3);

    private JButton jButton = new JButton("确定");

    MapPanel mapPanel = null;

    MapInfoFrame(MapPanel panel) {
        setTitle("地图信息");
        setSize(290, 120);
        mapPanel = panel;
        setLocationRelativeTo(null);
        setResizable(false);
        init();
    }

    private void init() {
        setLayout(null);
        JPanel panel[] = new JPanel[3];
        panel[0] = new JPanel(new FlowLayout());
        panel[0].add(new JLabel("版本："));
        panel[0].add(ver);
        panel[0].add(new JLabel("炸弹："));
        panel[0].add(bomb);
        panel[1] = new JPanel(new FlowLayout());
        panel[1].add(new JLabel("名称："));
        panel[1].add(name);
        panel[2] = new JPanel(new FlowLayout());
        panel[2].add(new JLabel("作者："));
        panel[2].add(author);
        panel[0].setBounds(5, 5, 200, 30);
        panel[1].setBounds(5, 30, 200, 30);
        panel[2].setBounds(5, 55, 200, 30);
        for (int i = 0; i < 3; i++) add(panel[i]);
        add(jButton);
        jButton.setBounds(210, 25, 60, 40);
        jButton.addActionListener(this);
        validate();
    }

    public void setMapInfo(MapInfo mi) {
        ver.setText(mi.ver.trim());
        bomb.setText(String.valueOf(mi.bomb));
        name.setText(mi.name.trim());
        author.setText(mi.author.trim());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButton) {
            try {
                mapPanel.setMapInfo(ver.getText().trim(), Short.parseShort(bomb.getText().trim()), name.getText().trim(), author.getText().trim());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "哇，炸弹那里输入的不是整数啦！", "推箱子", JOptionPane.ERROR_MESSAGE);
            }
            setVisible(false);
        }
    }
}
