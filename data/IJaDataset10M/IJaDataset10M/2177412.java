package com.sistemask.sc.yapk.gui.listener;

import com.sistemask.sc.yapk.gui.main.Okno;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import com.sistemask.sc.yapk.core.control.FileNameExtFilter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ToolBoxListener implements ActionListener {

    private Okno o;

    public ToolBoxListener(Okno o) {
        this.o = o;
    }

    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) (e.getSource());
        JFileChooser jfc = new JFileChooser();
        if (source == o.colorChooser) {
            o.currentColor = (JColorChooser.showDialog(new JPanel(), "Choose..", Color.white));
            o.imageChooser.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, o.currentColor));
            o.colorChooser.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, o.currentColor));
            o.setCursor(o.currentCursor);
        }
        if (source == o.imageChooser) {
            jfc.setAcceptAllFileFilterUsed(false);
            FileFilter filter = new FileNameExtFilter("Image file (.jpg, .jpeg, .gif, .png)", new String[] { "jpg", "jpeg", "gif", "png" });
            jfc.addChoosableFileFilter(filter);
            int returnVal2 = jfc.showOpenDialog(o);
            if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                File file2 = jfc.getSelectedFile();
                ImageIcon tlo = new ImageIcon(file2.getAbsolutePath());
                o.img = tlo.getImage();
                o.images.add(tlo.getImage());
            }
            o.tool = 11;
        }
        if (source == o.brush) {
            o.tool = 1;
            o.infos.setText("B R U S H");
        } else if (source == o.airbrush) {
            o.tool = 2;
            o.infos.setText("A I R B R U S H");
        } else if (source == o.line) {
            o.tool = 3;
            o.infos.setText("L I N E");
        } else if (source == o.eyedropper) {
            o.tool = 4;
            o.infos.setText("E Y E  D R O P P E R");
        } else if (source == o.ellipse) {
            o.tool = 5;
            o.infos.setText("E L L I P S E");
        } else if (source == o.ellipse_fill) {
            o.tool = 6;
            o.infos.setText("F I L L E D  E L L I P S E");
        } else if (source == o.rectangle) {
            o.tool = 7;
            o.infos.setText("R E C T A N G L E");
        } else if (source == o.rectangle_fill) {
            o.tool = 8;
            o.infos.setText("F I L L E D  R E C T A N G L E");
        } else if (source == o.eraser) {
            o.tool = 9;
            o.infos.setText("E R A S E R");
        } else if (source == o.text) {
            o.tool = 10;
            o.infos.setText("T E X T");
            o.textString = JOptionPane.showInputDialog("Text");
        }
    }
}
