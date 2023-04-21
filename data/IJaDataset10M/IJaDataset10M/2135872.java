package org.one.stone.soup.xapp.swing.components;

import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.one.stone.soup.xapp.components.XappTextArea;

public class XappSwingTextArea extends JPanel implements XappTextArea {

    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private JScrollPane scrollPane;

    private JTextArea text;

    public XappSwingTextArea() {
        text = new JTextArea();
    }

    public String getData() {
        return text.getText();
    }

    public void setData(String value) {
        text.setText(value);
    }

    public void addActionListener(ActionListener actionListener) {
    }

    public void setTip(String toolTip) {
        text.setToolTipText(toolTip);
    }

    public void setStyle(String style) {
        if (style == null || style.equals("framed")) {
            JScrollPane scrollPane = new JScrollPane(text);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane);
        } else if (style.equals("flow")) {
            add(text);
            setSize(text.getSize());
        } else if (style.equals("plain")) {
            add(text);
            text.setBackground(MetalLookAndFeel.getDesktopColor());
            text.setForeground(MetalLookAndFeel.getWhite());
            setSize(text.getSize());
        }
    }

    public void setColumns(int columns) {
        text.setColumns(columns);
    }

    public void setRows(int rows) {
        text.setRows(rows);
    }

    public void setEnabled(boolean state) {
        setEditable(state);
    }

    public void setEditable(boolean state) {
        text.setEditable(state);
    }

    public void append(String newData) {
        text.append(newData);
    }
}
