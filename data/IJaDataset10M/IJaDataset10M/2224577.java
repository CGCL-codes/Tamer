package com.java.component;

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class QueryAccomPanel extends JPanel implements ActionListener, MouseListener {

    private static final long serialVersionUID = -5812163014772966869L;

    private JLabel sNo = new JLabel("学号：");

    private JLabel sName = new JLabel("姓名：");

    private JLabel aRoom = new JLabel("宿舍：");

    private JLabel aBed = new JLabel("床位：");

    private JTextField no = new JTextField("请输入学号", 10);

    private JTextField name = new JTextField("", 10);

    private JTextField room = new JTextField("", 10);

    private JTextField bed = new JTextField("", 10);

    private JButton query = new JButton("查询");

    private JButton reset = new JButton("重置");

    private JPanel JPcomon1 = new JPanel();

    private JPanel JPcomon2 = new JPanel();

    private JPanel JPcomon3 = new JPanel();

    private JPanel JPcomon4 = new JPanel();

    private JPanel JPbutton = new JPanel();

    public QueryAccomPanel() {
        sNo.setHorizontalAlignment(SwingConstants.CENTER);
        sName.setHorizontalAlignment(SwingConstants.CENTER);
        aRoom.setHorizontalAlignment(SwingConstants.CENTER);
        aBed.setHorizontalAlignment(SwingConstants.CENTER);
        JPcomon1.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 5));
        JPcomon1.add(sNo);
        JPcomon1.add(no);
        JPcomon2.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 5));
        JPcomon2.add(sName);
        JPcomon2.add(name);
        JPcomon3.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 5));
        JPcomon3.add(aRoom);
        JPcomon3.add(room);
        JPcomon4.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 5));
        JPcomon4.add(aBed);
        JPcomon4.add(bed);
        JPbutton.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 5));
        JPbutton.add(query);
        JPbutton.add(reset);
        this.setLayout(new GridLayout(10, 1));
        this.add(JPcomon1);
        this.add(JPcomon2);
        this.add(JPcomon3);
        this.add(JPcomon4);
        this.add(JPbutton);
        name.setEditable(false);
        room.setEditable(false);
        bed.setEditable(false);
        query.addActionListener(this);
        reset.addActionListener(this);
        no.addMouseListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("查询")) {
            QueryDataBase();
            no.setEditable(false);
        } else if (e.getActionCommand().equals("重置")) {
            no.setText("");
            no.setEditable(true);
        }
    }

    private void QueryDataBase() {
        ResultSet rs = null;
        try {
            BaseSQL accom = null;
            accom = new BaseSQL();
            String sql = "select * from accom where sno = '" + Integer.parseInt(no.getText()) + "'";
            rs = accom.executeSelect(sql);
            if (rs.next()) {
                if (Integer.parseInt(no.getText()) == rs.getInt("sno")) {
                    name.setText(rs.getString("sname"));
                    room.setText(rs.getString("room"));
                    bed.setText(rs.getString("bed"));
                }
            } else {
                JOptionPane.showMessageDialog(null, "不存在学号为：" + no.getText() + " 的住宿记录", "住宿查询", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException evt) {
            JOptionPane.showMessageDialog(this, evt, "系统错误", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException evt) {
            JOptionPane.showMessageDialog(this, evt, "系统错误", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException evt) {
            JOptionPane.showMessageDialog(this, evt, "系统错误", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (no.getText().equals("请输入学号")) {
            no.setText("");
            this.setVisible(true);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
