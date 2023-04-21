package util;

import java.awt.*;
import javax.swing.JFileChooser;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

public class IO {

    protected static File file;

    public static final char s = File.separatorChar;

    public static final boolean os = (s == '/');

    public static final String app = System.getProperty("user.dir") + s;

    public static final String home = (os) ? (System.getProperty("user.home") + s + ".eugenemccabe" + s) : (app + s + "configuration" + s);

    public static final String appURI = URI(app);

    public static final String homeURI = URI(home);

    public static File app(final String dir) {
        return new File(app + dir);
    }

    public static File home(final String dir) {
        return new File(home + s + dir);
    }

    public static String URI(final String dir) {
        return "file://" + dir.replace("", "%20");
    }

    public static void copy(String a, String b) throws Exception {
        InputStream is = new IO().getClass().getResourceAsStream(a);
        OutputStream os = new FileOutputStream(b);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.close();
        is.close();
    }

    public static Image loadImage(final String folder) {
        try {
            return ImageIO.read(new IO().getClass().getResource((folder.startsWith("/") ? "" : "/") + folder));
        } catch (final java.io.IOException e) {
            System.out.println("\n**** Image : " + folder + " cannot be opened");
        }
        return null;
    }

    public static File getFile(String buttonText, File startDir, String suffix) {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileFilter(suffix));
        fc.setCurrentDirectory(startDir);
        if (fc.showDialog(game.GameFrame.reference, buttonText) == JFileChooser.CANCEL_OPTION) return null;
        File selected = fc.getSelectedFile();
        String name = selected.getName();
        if (!(name.length() > suffix.length() && name.substring(name.length() - suffix.length()).equals(suffix))) selected = new File(selected.getAbsolutePath().concat("." + suffix));
        return selected;
    }

    public static Object loadFile(String loc) {
        Object file = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(loc));
            file = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void saveFile(String loc, Object file) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(loc));
            out.writeObject(file);
            out.close();
        } catch (IOException e) {
        }
    }

    public static String[] splitString(Graphics g, String s, int width) {
        FontMetrics fm = g.getFontMetrics();
        ArrayList<String> lines = new ArrayList<String>();
        String current = "";
        String[] words = s.split(" ");
        for (String word : words) {
            String tmp = current + " " + word;
            if (fm.stringWidth(tmp) >= width) {
                lines.add(current);
                current = word;
            } else current = tmp;
        }
        if (!current.equals("")) lines.add(current);
        return lines.toArray(new String[lines.size()]);
    }
}

class FileFilter extends javax.swing.filechooser.FileFilter {

    private String suffix;

    public FileFilter(String suffix) {
        this.suffix = suffix;
    }

    public boolean accept(File pathname) {
        if (suffix == null) return true;
        if (pathname.isDirectory()) return true;
        String name = pathname.getName();
        return (name.length() > suffix.length() && name.substring(name.length() - suffix.length()).equals(suffix));
    }

    public String getDescription() {
        return suffix;
    }
}
