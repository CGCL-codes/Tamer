package com.sun.spot.spotworld.gridview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ImageBackground extends JComponent implements GVBackdrop, MouseListener, ActionListener {

    GridView view;

    Image image;

    Point origin;

    JPopupMenu menu;

    double zoomdelta = 0;

    int upper_x, upper_y;

    int lower_x, lower_y;

    double image_width = 640;

    double image_height = 480;

    public ImageBackground(GridView v, String filename) {
        this.view = v;
        if (filename == null) {
            filename = this.getFilename().getAbsolutePath();
        }
        setImage(filename);
        menu = new JPopupMenu();
        menu.add(new JLabel("backdrop options"));
        menu.addSeparator();
        JMenuItem item = new JMenuItem("select backdrop");
        menu.add(item);
        item.addActionListener(this);
        this.add(menu);
    }

    public void setImage(String filename) {
        double xoffset = view.getXOrgOffset();
        double yoffset = view.getYOrgOffset();
        image = Toolkit.getDefaultToolkit().getImage(filename);
        image_height = view.getHeight() * (1 / view.getZoom());
        image_width = view.getWidth() * (1 / view.getZoom());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Point origin = view.getLocationInView(0, 0);
        int half_height = (int) ((image_height / 2) * view.getZoom());
        int half_width = (int) ((image_width / 2) * view.getZoom());
        origin = view.getLocationInView(0, 0);
        boolean static_image = false;
        if (static_image) {
            g2.drawImage(image, 0, 0, view.getWidth(), view.getHeight(), null);
        } else {
            g2.drawImage(image, origin.x - half_width, origin.y - half_height, half_width * 2, half_height * 2, null);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
            popupMenu(mouseEvent);
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void actionPerformed(ActionEvent actionEvent) {
        menu.setVisible(false);
        String filename = this.getFilename().getAbsolutePath();
        setImage(filename);
    }

    public File getFilename() {
        JFileChooser fc = new JFileChooser("~");
        int returnVal = fc.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        } else return null;
    }

    public void popupMenu(MouseEvent mouseEvent) {
        menu.setLocation(mouseEvent.getX(), mouseEvent.getY());
        menu.setVisible(true);
    }
}
