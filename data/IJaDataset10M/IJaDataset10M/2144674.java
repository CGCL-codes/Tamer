package com.cell.loader.app;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.cell.classloader.jcl.CC;
import com.cell.classloader.jcl.JarClassLoader;
import com.cell.loader.LoadTask;
import com.cell.loader.MD5;
import com.cell.loader.PaintTask;
import com.cell.loader.LoadTask.LoadTaskListener;

/**
 * 
[main]
update_path			=http://game.lordol.com/lordol_xc_test/update.val
ignore_list			=loader.jar,lordol_res.jar,lordol_ressk.jar,lordol_j2se_ui_sk.jar
l_font				=宋体

[image]
img_bg				=bg.png
img_loading_f		=loading_f.png
img_loading_s 		=loading_s.png
img_loading_b 		=loading_b.png

[text]
l_text_loading		=更新文件中...
l_text_initializing	=初始化中...
l_text_error		=更新错误!
l_text_check		=检查更新中...
l_text_complete		=完成

[net]
load_retry_count	=5
load_timeout		=10000
*/
@SuppressWarnings("serial")
public abstract class LoaderFrame extends JFrame implements WindowListener, LoadTask.LoadTaskListener {

    private static final long serialVersionUID = 1L;

    private String update_path;

    private String ignore_list;

    private String l_font;

    private String img_bg;

    private String img_loading_f;

    private String img_loading_s;

    private String img_loading_b;

    protected String l_text_loading;

    protected String l_text_initializing;

    protected String l_text_error;

    protected String l_text_check;

    protected String l_text_complete;

    private int load_retry_count = 5;

    private int load_timeout = 10000;

    private String root_remote;

    private File root_dir = new File(".");

    private PaintTask paint_task;

    private LoadTask load_task;

    private Canvas paint_canvas;

    private LinkedHashMap<String, byte[]> local_jar_files = new LinkedHashMap<String, byte[]>();

    private LinkedHashMap<String, String> ignore_jar_files = new LinkedHashMap<String, String>();

    private boolean is_complete = false;

    protected Image bg;

    protected Image loading_f;

    protected Image loading_s;

    protected Image loading_b;

    protected Font font;

    public LoaderFrame(String update_ini_file) {
        this.addWindowListener(this);
        this.setSize(640, 480);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getHeight() / 2);
        try {
            InputStream is = getClass().getResourceAsStream("/com/cell/loader/app/icon.png");
            this.setIconImage(ImageIO.read(is));
        } catch (Exception err) {
        }
        this.paint_canvas = new PaintCanvas();
        this.add(paint_canvas);
        this.setTitle("check for update");
        this.setResizable(false);
        try {
            paint_task = new PaintTask(paint_canvas);
            paint_task.start();
            LinkedHashMap<String, String> update_ini = new LinkedHashMap<String, String>();
            {
                System.out.println("loading : " + update_ini_file);
                FileInputStream fis = new FileInputStream(update_ini_file);
                byte[] data = new byte[fis.available()];
                fis.read(data);
                fis.close();
                String text = new String(data, "UTF-8");
                text = text.replaceAll("^\n", "");
                text = text.replaceAll("^\r\n", "");
                String[] lines = text.split("\n");
                for (String line : lines) {
                    try {
                        String[] kv = line.split("=");
                        update_ini.put(kv[0].trim(), kv[1].trim());
                        System.out.println("\t" + kv[0].trim() + "\t=" + kv[1].trim());
                    } catch (Exception e) {
                    }
                }
                {
                    update_path = update_ini.get("update_path");
                    ignore_list = update_ini.get("ignore_list");
                    l_font = update_ini.get("l_font");
                    img_bg = update_ini.get("img_bg");
                    img_loading_f = update_ini.get("img_loading_f");
                    img_loading_s = update_ini.get("img_loading_s");
                    img_loading_b = update_ini.get("img_loading_b");
                    l_text_loading = update_ini.get("l_text_loading");
                    l_text_initializing = update_ini.get("l_text_initializing");
                    l_text_error = update_ini.get("l_text_error");
                    l_text_check = update_ini.get("l_text_check");
                    l_text_complete = update_ini.get("l_text_complete");
                    try {
                        load_retry_count = Integer.parseInt(update_ini.get("load_retry_count"));
                        load_timeout = Integer.parseInt(update_ini.get("load_timeout"));
                    } catch (Exception e) {
                        load_retry_count = 5;
                        load_timeout = 10000;
                    }
                    onTaskInit(update_ini);
                    LoadTask.LoadRetryTime = load_retry_count;
                    LoadTask.LoadTimeOut = load_timeout;
                    this.setTitle(l_text_check);
                }
                if (ignore_list != null) {
                    String[] ignores = ignore_list.split(",");
                    for (String ignore : ignores) {
                        while (ignore.startsWith(".")) {
                            ignore = ignore.substring(1);
                        }
                        while (ignore.contains("\\")) {
                            ignore = ignore.replace('\\', '/');
                        }
                        ignore_jar_files.put(ignore.trim(), ignore.trim());
                    }
                }
                try {
                    System.out.println("loading images !");
                    bg = ImageIO.read(new File(img_bg));
                    loading_f = ImageIO.read(new File(img_loading_f));
                    loading_s = ImageIO.read(new File(img_loading_s));
                    loading_b = ImageIO.read(new File(img_loading_b));
                    font = new Font(l_font, Font.PLAIN, 14);
                    paint_task.setPaintUnit(bg, loading_f, loading_s, loading_b, font);
                    paint_canvas.repaint();
                    int frameWidth = bg.getWidth(null) + (getInsets().left + getInsets().right);
                    int frameHeight = bg.getHeight(null) + (getInsets().top + getInsets().bottom);
                    this.setSize(frameWidth, frameHeight);
                    if (font != null) {
                        if (getFont() != null) {
                            this.setFont(new Font(l_font, getFont().getStyle(), getFont().getSize()));
                        } else {
                            this.setFont(font);
                        }
                    }
                    this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getHeight() / 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.setVisible(true);
            }
            this.root_remote = update_path.substring(0, update_path.lastIndexOf("/"));
            System.out.println("root_remote = " + root_remote);
            System.out.println("root_local  = " + root_dir.getCanonicalPath());
            LinkedHashMap<String, String> update_val = loadUpdateVal();
            this.local_jar_files = loadLocalFiles(update_val);
            if (!update_val.isEmpty()) {
                ArrayList<String> remote_files = new ArrayList<String>();
                for (String remote_file : update_val.keySet()) {
                    if (!isIgnore(remote_file)) {
                        System.out.print("redist : " + remote_file);
                        remote_file = root_remote + "/" + remote_file;
                        System.out.println("\t -> " + remote_file);
                        remote_files.add(remote_file);
                    } else {
                        System.out.println("ignore : " + remote_file);
                    }
                }
                load_task = new LoadTask(this, remote_files.toArray(new String[remote_files.size()]));
            } else {
                loadAllComplete(new Vector<byte[]>(local_jar_files.size()));
            }
        } catch (Throwable err) {
            this.setVisible(true);
            err.printStackTrace();
            paint_task.setState(l_text_error + " : " + err.getMessage());
        }
    }

    private LinkedHashMap<String, String> loadUpdateVal() {
        LinkedHashMap<String, String> update_val = new LinkedHashMap<String, String>();
        try {
            System.out.println("loading : " + update_path);
            URLConnection update_path_c = LoadTask.openURL(new URL(update_path));
            byte[] data = LoadTask.loadURL(update_path_c);
            String text = new String(data, "UTF-8");
            String[] lines = text.split("\n");
            for (String line : lines) {
                try {
                    String[] kv = line.split(":");
                    String jar_name = kv[1].trim();
                    while (jar_name.startsWith(".")) {
                        jar_name = jar_name.substring(1);
                    }
                    update_val.put(jar_name, kv[0].trim());
                    System.out.println(line);
                } catch (Exception e) {
                }
            }
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return update_val;
    }

    private LinkedHashMap<String, byte[]> loadLocalFiles(LinkedHashMap<String, String> update_val) {
        LinkedHashMap<String, byte[]> local_jar_files = new LinkedHashMap<String, byte[]>(update_val.size());
        for (String remote_file : new ArrayList<String>(update_val.keySet())) {
            Thread.yield();
            if (isIgnore(remote_file)) {
                System.out.println("ignore local file : \t\"" + remote_file + "\" ");
            } else {
                System.out.println("get local file : \t\"" + remote_file + "\" ");
                try {
                    File file = new File(root_dir, remote_file);
                    if (file.exists()) {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] data = new byte[fis.available()];
                        fis.read(data);
                        fis.close();
                        String remote_md5 = update_val.get(remote_file);
                        String local_md5 = MD5.getMD5(data);
                        if (remote_md5.equals(local_md5)) {
                            update_val.remove(remote_file);
                            if (remote_file.endsWith(".jar")) {
                                local_jar_files.put(file.getName(), data);
                            }
                        } else {
                            System.out.println("\t\tvalidate changed, prepare to update !" + "\n\t" + remote_md5 + "\n\t" + local_md5);
                        }
                    } else {
                        System.out.println("\t(not exist) ");
                    }
                } catch (Exception e) {
                    System.err.println(remote_file);
                    e.printStackTrace();
                }
            }
        }
        return local_jar_files;
    }

    protected abstract void onTaskOver(Vector<byte[]> datas) throws Exception;

    protected abstract void onTaskInit(Map<String, String> config_file);

    public File getRootDir() {
        return root_dir;
    }

    public Map<String, byte[]> getLocalJarFiles() {
        return local_jar_files;
    }

    public Map<String, String> getIgnoreJarFiles() {
        return ignore_jar_files;
    }

    public final boolean isIgnore(String file_name) {
        String suffix = file_name;
        while (suffix.contains("\\")) {
            suffix = suffix.replace('\\', '/');
        }
        for (String ig : ignore_jar_files.keySet()) {
            if (suffix.endsWith(ig)) {
                return true;
            }
        }
        return false;
    }

    public final void loadBegin(URL[] files) {
        paint_task.setState(l_text_loading);
    }

    public final void loadJarBegin(URL file, int current, int total) {
        paint_task.setState(l_text_loading + "(" + (current) + "/" + total + ")");
        paint_task.reset();
    }

    public final void loadJarStart(URL file, int current, int total, int length) {
        paint_task.setMax(length);
    }

    public final void loadJarProgress(URL file, int current, int total, int actual) {
        paint_task.acc(actual);
    }

    public final void loadJarComplete(URL file, int current, int total, byte[] data) {
        String file_name = file.getFile();
        if (file_name.contains("/")) {
            file_name = file_name.substring(file_name.lastIndexOf("/") + 1);
        }
        if (!isIgnore(file_name)) {
            try {
                File local_file = new File(root_dir, file_name);
                if (local_file.getParentFile() != null) {
                    if (!local_file.getParentFile().exists()) {
                        local_file.getParentFile().mkdirs();
                    }
                }
                FileOutputStream fos = new FileOutputStream(local_file);
                fos.write(data);
                fos.flush();
                fos.close();
                System.out.println("\t - save to local file : " + local_file.getCanonicalPath());
            } catch (Exception e) {
                e.printStackTrace();
                paint_task.setState(l_text_error + " : " + e.getMessage());
            } finally {
                local_jar_files.put(file_name, data);
            }
        } else {
            System.out.println("\t - ignore save to local file : " + file_name);
        }
    }

    public final void loadAllComplete(Vector<byte[]> datas) {
        System.out.println("loadCompleted !");
        paint_task.setState(l_text_initializing);
        paint_task.exit();
        this.validate();
        try {
            this.remove(paint_canvas);
            onTaskOver(datas);
            is_complete = true;
            paint_task = null;
        } catch (Throwable e) {
            e.printStackTrace();
            paint_task.setState(l_text_error + " : " + e.getMessage());
        }
        this.validate();
    }

    public void loadError(Throwable cause) {
        System.out.println("loadError !");
        paint_task.setState(l_text_error + " : " + cause.getMessage());
    }

    private class PaintCanvas extends Canvas {

        @Override
        public void update(Graphics g) {
            paint(g);
        }

        public void paint(Graphics g) {
            if (!is_complete) {
                if (paint_task != null) {
                    Image buffer = paint_task.repaint(((Graphics2D) g).getDeviceConfiguration());
                    if (buffer != null) {
                        g.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
                    }
                }
            }
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(1);
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
