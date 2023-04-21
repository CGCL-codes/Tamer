package com.habook.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import com.habook.data.CString;
import com.habook.image.ImageHelper;
import com.habook.util.Property;

public class AddressBookPanel extends JPanel {

    private static final long serialVersionUID = -4479991916447167635L;

    private AddrTextField filterField = new AddrTextField("搜索");

    private JComboBox orderbyChoose = new JComboBox(new String[] { "排序", "a", "b" });

    private JList contactList = new JList();

    private JTextArea notesArea = new JTextArea();

    private JTextField nameField = new JTextField();

    private JTextField[] infoFields = new JTextField[13];

    private JTextField addrPathField = new JTextField(Property.getAbsLocalNotePath());

    private JMenuItem syncMailItem = new JMenuItem("Sync mail", ImageHelper.ICON_MAIL_SEND);

    private JMenuItem configMailItem = new JMenuItem("Config mail");

    private JMenuItem usageItem = new JMenuItem("Usage");

    private JMenuItem aboutItem = new JMenuItem("About");

    class MyCellRenderer extends JLabel implements ListCellRenderer {

        public MyCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(isSelected ? Color.red : Color.white);
            setForeground(isSelected ? Color.white : Color.black);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setText(value.toString());
            return this;
        }
    }

    public AddressBookPanel() {
        addrPathField.setEditable(false);
        addrPathField.setBorder(BorderFactory.createEmptyBorder());
        addrPathField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        file.add(syncMailItem);
        JMenu settings = new JMenu("Settings");
        settings.add(configMailItem);
        JMenu help = new JMenu("Help");
        help.setMnemonic('H');
        help.add(usageItem);
        help.add(aboutItem);
        JMenuBar menubar = new JMenuBar();
        menubar.add(file);
        menubar.add(addrPathField);
        menubar.add(settings);
        menubar.add(help);
        JPanel tPanel = new JPanel(new BorderLayout(0, 0));
        filterField.setPreferredSize(new Dimension(65, 25));
        orderbyChoose.setPreferredSize(new Dimension(65, 25));
        orderbyChoose.setRenderer(new MyCellRenderer());
        tPanel.add(filterField, BorderLayout.CENTER);
        orderbyChoose.setBorder(BorderFactory.createEmptyBorder());
        tPanel.add(orderbyChoose, BorderLayout.EAST);
        notesArea.setPreferredSize(new Dimension(-1, 80));
        JPanel wPanel = new JPanel(new BorderLayout(5, 5));
        wPanel.add(tPanel, BorderLayout.NORTH);
        wPanel.add(new JScrollPane(contactList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        wPanel.add(new JScrollPane(notesArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.SOUTH);
        wPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        wPanel.setPreferredSize(new Dimension(200, -1));
        InfoPanel infoPanel = new InfoPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        setLayout(new BorderLayout());
        add(menubar, BorderLayout.NORTH);
        add(wPanel, BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
    }
}

class InfoPanel extends JPanel {

    private static final long serialVersionUID = -2588763425591993699L;

    private String[][] info = { { "姓名", "昵称", "性别" }, { "生日", "纪念日" }, { "QQ", "MSN" }, { "手机", "飞信" }, { "工作电话", "家庭电话" }, { "电子邮箱" }, { "个人主页" }, { "联系周期", "最后联系时间" }, { "公司名称", "部门职务" }, { "隶属行业" }, { "公司地址" }, { "隶属分组" } };

    public InfoPanel() {
        JPanel cp = new JPanel(new GridLayout(info.length, 1, 2, 3));
        for (int i = 0; i < info.length; i++) {
            JPanel tp = new JPanel(new GridLayout(1, info[i].length, 2, 3));
            for (int j = 0; j < info[i].length; j++) tp.add(new AddrTextField(info[i][j]));
            cp.add(tp);
        }
        setLayout(new BorderLayout(5, 5));
        JTextField k = new JTextField();
        k.setEditable(false);
        add(cp, BorderLayout.CENTER);
    }
}
