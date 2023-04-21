package com.jdiv;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import com.jdiv.extensions.JFnt;
import com.jdiv.extensions.JFpg;
import com.jdiv.extensions.JFpgImage;
import com.jdiv.input.JMouse;
import com.jdiv.input.JWrite;
import com.jdiv.util.JNumber;
import com.jdiv.util.MaskColorImage;

/**
 * @author  Joyal
 */
public class JDiv implements JConst {

    /**
	 * @uml.property  name="core"
	 * @uml.associationEnd  
	 */
    private static JCore core = new JCore();

    public static ArrayList<JProcess> process = core.getProcess();

    public static ArrayList<JFpg> fpgs = core.getFpgs();

    public static ArrayList<JFpgImage> images = core.getImages();

    public static ArrayList<JFnt> fnts = core.getFnts();

    public static ArrayList<JWrite> writes = core.getWrites();

    public static boolean keyStatus[] = core.getKeyStatus();

    /**
	 * @uml.property  name="program"
	 * @uml.associationEnd  
	 */
    public static JProgram program;

    public static BufferStrategy strategy = core.getBStrategy();

    public static int scale = 0;

    /**
	 * @uml.property  name="mouse"
	 * @uml.associationEnd  
	 */
    public static JMouse mouse = core.mouse;

    /**
	 * @uml.property  name="scroll"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    public static JScroll scroll[] = core.scroll;

    public static void define_region(int id, int x0, int y0, int x1, int y1) {
    }

    public static void delay(int speed) {
        core.delay(speed);
    }

    public static void delay(long speed) {
        core.delay(speed);
    }

    public static void sleep(int speed) {
        core.delay(speed);
    }

    public static void delete_text(int text) {
    }

    public static void exit(String message, int arg) {
        System.exit(arg);
    }

    public static void exit(int arg) {
        System.exit(arg);
    }

    public static void exit() {
        System.exit(0);
    }

    public static void addKeyListener(KeyListener kl) {
        core.addKeyListener(kl);
    }

    public static void setMaskColor(Color maskColor) {
        MaskColorImage.setMaskColor(maskColor);
    }

    public static BufferStrategy getBStrategy() {
        return core.getBStrategy();
    }

    public static Canvas getCanvas() {
        return core.getCanvas();
    }

    public static Graphics2D getGraphics() {
        return core.getJDivGraphics();
    }

    public static int getHeight() {
        return core.getCanvasHeight();
    }

    public static int getWidth() {
        return core.getCanvasWidth();
    }

    public static void init(int res) {
        core.appletInit(res);
    }

    public static boolean key(int KeyCode) {
        return keyStatus[KeyCode];
    }

    public static int load_fnt(String archivo) {
        return core.load_fnt(archivo);
    }

    public static int load_fnt(URL archivo) {
        return core.load_fnt(archivo);
    }

    public static int load_fpg(String archivo) {
        return core.load_fpg(archivo);
    }

    public static int load_image(String archivo) {
        return core.load_image(archivo);
    }

    public static int load_image(URL archivo) {
        return core.load_image(archivo);
    }

    public static int load_fpg(URL url) {
        return core.load_fpg(url);
    }

    public static int map_get_pixel(int fpg, int graph, int x, int y) {
        return core.map_get_pixel(fpg, graph, x, y);
    }

    public static boolean out_region(JProcess pro, int region) {
        return core.out_region(pro, region);
    }

    public static void paint(Graphics g) {
        core.paint(g);
    }

    public static void put_screen(int file, int graph) {
        core.put_screen(file, graph);
    }

    public static void set_mode(int resolution) {
        core.set_mode(resolution);
    }

    public static void set_mode(int width, int height) {
        core.set_mode(width, height);
    }

    public static void scale_mode(int scale) {
        JDiv.scale = scale;
        core.scale_mode(scale);
        if (core.window != null) {
            if (scale > 0) core.window.setSize(new Dimension(core.getCanvasWidth() * scale, core.getCanvasHeight() * scale)); else core.window.setSize(new Dimension(core.getCanvasWidth(), core.getCanvasHeight()));
        }
    }

    public static void set_title(String title) {
        core.set_title(title);
    }

    public static void signal(JProcess pro, int signal) {
        core.signal(pro, signal);
    }

    public static void fullScreen(boolean fullscreen) {
        core.fullScreen(fullscreen);
    }

    public static void start_scroll(int id, int fpg, int graph, int background, int region, int flags) {
        core.addScroll(id, fpg, graph, background, region, flags);
    }

    public static synchronized void update() {
        core.update();
    }

    public static synchronized void update(Graphics2D g) {
        core.update(g);
    }

    public static void write(int fnt, int x, int y, int align, JNumber num) {
        core.write(fnt, x, y, align, num);
    }

    public static void write(int fnt, int x, int y, int align, int text) {
        core.write(fnt, x, y, align, "" + text);
    }

    public static void write(int fnt, int x, int y, int align, String text) {
        core.write(fnt, x, y, align, text);
    }

    public static int rand(int n1, int n2) {
        return core.rand(n1, n2);
    }

    public static int get_pixel(int x, int y) {
        return core.get_pixel(x, y);
    }

    public static void appletInit(int resolution) {
        core.appletInit(resolution);
    }

    public static void appletInit(int width, int height) {
        core.appletInit(width, height);
    }

    public static void setMouseListener(MouseListener mouse) {
        core.setMouseListener(mouse);
    }

    public static void setMouseMotionListener(MouseMotionListener mouse) {
        core.setMouseMotionListener(mouse);
    }

    public static void replaceColor(Color color, Color newColor) {
        MaskColorImage.replaceColor(color, newColor);
    }

    public static int fget_angle(int x1, int y1, int x2, int y2) {
        return core.fget_angle(x1, y1, x2, y2);
    }

    public static void close() {
        core.close();
    }

    public JDiv() {
    }

    public static Panel getPanel() {
        return core.getPanel();
    }

    public static void screenShot() {
        core.screenShot();
    }

    public static long getElapsedTime() {
        return core.elapsedTime;
    }
}
