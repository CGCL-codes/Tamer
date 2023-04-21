package registerUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import cashRegister.CashRegister;
import cashRegister.Customer;
import cashRegister.Final;
import cashRegister.Item;

@SuppressWarnings("serial")
public class InventoryReportCard extends GUIMode {

    private JTextArea textArea;

    private JScrollPane textScroller;

    private JButton backButton;

    private CashRegister register;

    private RootGUI parent;

    public InventoryReportCard(CashRegister register, RootGUI gui) {
        this.register = register;
        parent = gui;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        backButton = new JButton("Back");
        textArea = new JTextArea();
        textArea.setFont(new Font("Courier", textArea.getFont().getStyle(), textArea.getFont().getSize() + 3));
        textArea.addKeyListener(parent);
        textArea.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        textScroller = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textScroller.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textScroller.addKeyListener(parent);
        backButton.addKeyListener(parent);
        backButton.addActionListener(this);
        add(textScroller);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(backButton, BorderLayout.PAGE_END);
        add(p);
        validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            try {
                Robot r = new Robot();
                r.keyPress(KeyEvent.VK_ESCAPE);
                r.keyRelease(KeyEvent.VK_ESCAPE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void reload() {
        textArea.setText("");
        List<Item> li = register.getItemList();
        Collections.sort(li);
        for (Item i : li) {
            String buf = formatWithLeadingZeroes(i.getID());
            buf += " " + formatWithTrailingSpaces(i.getName());
            buf += "x" + i.getCount() + "\n";
            textArea.append(buf);
        }
    }

    private String formatWithLeadingZeroes(long i) {
        String s = Long.toString(i);
        while (s.length() < Final.MAX_ID_LENGTH) s = " " + s;
        return s;
    }

    private String formatWithTrailingSpaces(String s) {
        while (s.length() < 64) s += " ";
        return s;
    }
}
