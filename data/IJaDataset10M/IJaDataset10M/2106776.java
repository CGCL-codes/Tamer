package pogvue.color_wheel;

import pogvue.io.FileParse;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public final class ColorWheel extends JPanel {

    private Hashtable reds;

    private Hashtable greens;

    private Hashtable blues;

    private Hashtable colors;

    private final String rgbfile = "/usr/X11/lib/X11/rgb";

    private final int height = 15;

    private final int width = 300;

    private int fullheight;

    public ColorWheel() {
        read_rgb_file();
    }

    private void read_rgb_file() {
        reds = new Hashtable();
        greens = new Hashtable();
        blues = new Hashtable();
        colors = new Hashtable();
        try {
            FileParse file = new FileParse(rgbfile, "File");
            String line;
            while ((line = file.nextLine()) != null) {
                if (line.indexOf("!") != 0) {
                    StringTokenizer str = new StringTokenizer(line);
                    int red = Integer.parseInt(str.nextToken());
                    int green = Integer.parseInt(str.nextToken());
                    int blue = Integer.parseInt(str.nextToken());
                    String name = str.nextToken();
                    while (str.hasMoreTokens()) {
                        name = name + " ";
                        name = name + str.nextToken();
                    }
                    reds.put(name, red);
                    greens.put(name, green);
                    blues.put(name, blue);
                    Color c = new Color(red, green, blue);
                    colors.put(name, c);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: Exception reading rgb file " + e);
        }
        fullheight = height * reds.size();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, size().width, size().height);
        Enumeration keys = reds.keys();
        setSize(width, height * reds.size());
        int i = 0;
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            int red = (Integer) reds.get(key);
            int green = (Integer) greens.get(key);
            int blue = (Integer) blues.get(key);
            Color c = (Color) colors.get(key);
            g.setColor(c);
            g.fillRect(0, i * height, width, height);
            g.setColor(Color.black);
            g.drawString(key, 10, (1 + i) * height - 2);
            i++;
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, fullheight);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        JPanel tp = new JPanel();
        ColorWheel cw = new ColorWheel();
        JScrollPane sp = new JScrollPane();
        tp.setLayout(new BorderLayout());
        tp.add(sp, BorderLayout.CENTER);
        f.getContentPane().add(sp);
        f.setSize(300, 600);
        sp.getViewport().add(cw);
        f.setVisible(true);
    }
}
