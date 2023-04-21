package com.cell.j2se.awt;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.cell.CIO;

public class TrayFormListener extends Tray {

    final Frame focused_frame;

    public TrayFormListener(final Frame form, String imgPath) {
        super(form.getTitle(), Toolkit.getDefaultToolkit().createImage(CIO.loadData(imgPath)), new String[] { "show" }, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("show")) {
                    form.setVisible(true);
                }
            }
        });
        focused_frame = form;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            focused_frame.setVisible(true);
        }
    }
}
