package plotting;

import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

class ImageFrame {

    public void showimage() throws IOException {
        try {
            JFrame f = new JFrame();
            f.setTitle("JComplexity - Time Complexity Plot");
            ImagePanel panel = new ImagePanel(getCurrentDir() + "/temp/chart.png");
            f.getContentPane().add(panel);
            f.setBounds(0, 0, 800, 640);
            f.setVisible(true);
            f.show();
        } catch (Exception e) {
            System.out.println("Please verify that you selected a valid image file");
        }
    }

    public String getCurrentDir() {
        File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        return file.getParent();
    }
}
