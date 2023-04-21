package org.hfbk.vis.visnode;

import java.awt.event.MouseEvent;
import java.io.File;
import org.dronus.gl.GLFont;
import org.dronus.gl.GLPrimitives;
import org.dronus.gl.GLTextPanel;
import org.dronus.gl.GLUtil;
import org.dronus.graph.Edge;
import org.dronus.graph.Graph;
import org.dronus.graph.Node;
import org.hfbk.util.Counter;
import org.hfbk.util.Sleeper;
import org.hfbk.vis.Allesfresser;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * a profiler that samples currently active methods inside 
 * the running vis/client. thus vis/client is able to show
 * something about it's inner state of mind :-)  
 * 
 * methods and threads are visualised as interconnected balls. 
 *  
 * 
 * @author paul
 *
 */
public class VisFilesys extends VisSet {

    /** milliseconds between thread samples*/
    final int DELAY = 1000;

    /** the abstract profiled call graph*/
    Graph profile = new Graph();

    /** counts running method samples */
    Counter<Integer> callCounter = new Counter<Integer>();

    Counter<String> sizeAdder = new Counter<String>();

    boolean dirty = true;

    /** a hooverable sphere */
    class VisCallBall extends VisNodeDraggable {

        int dl;

        GLTextPanel helpText;

        Node node;

        boolean isTriggered = true;

        VisCallBall(Node n, Vector3f position) {
            super(n, position);
            this.node = n;
            w = h = radius = 1.6f;
            String[] b = n.text.split("#");
            n.text = b[0];
            float f = 1.6f;
            if (b.length > 1) {
                f = Float.valueOf(b[1]).floatValue();
                w = h = radius = f / 10000;
                w = h = radius = ((float) Math.pow(f, (double) (1.0 / 3)) / 10f);
            }
            int cutoff = n.text.lastIndexOf("/");
            String caption = n.text;
            if (cutoff > 0) caption = n.text.substring(n.text.lastIndexOf("/") + 1);
            helpText = new GLTextPanel(caption, 0, 0);
        }

        String cleanPath(String path) {
            path = path.replace('\\', '/');
            path = path.replace("file://", "");
            if (path.matches("file:/.*?\\:.*?")) {
                path = path.replace("file:/", "");
            }
            return path.trim();
        }

        void handleEvent(VisMouseEvent evt) {
            super.handleEvent(evt);
            VisRoot root = getRoot();
            if (evt.getID() == MouseEvent.MOUSE_CLICKED) {
                if (node.text.toLowerCase().matches(".*?\\.(" + Allesfresser.suffixesImage + "|" + Allesfresser.suffixesVideo + ")(.|\\s)*")) {
                    System.out.println(cleanPath(node.text));
                    Vector3f posi = position;
                    posi.y += 1;
                    add(new VisImage(new Node(cleanPath(node.text)), new Vector3f(0, 1, 0)));
                } else if (node.text.toLowerCase().matches(".*?\\.(obj)(.|\\s)*")) {
                    System.out.println(cleanPath(node.text));
                    root.client.root.create("obj", cleanPath(node.text));
                }
            }
        }

        void renderSelf() {
            boolean isOur = false;
            Integer count = callCounter.get(node.id);
            if (count != null) {
            }
            if (isHoovered || isTriggered) GL11.glColor3f(1, 1, 1); else if (node.type.equals("File")) {
                if (node.text.toLowerCase().matches(".*?\\.(" + Allesfresser.suffixesImage + "|" + Allesfresser.suffixesVideo + ")(.|\\s)*")) {
                    GL11.glColor4f(1f, 1f, .1f, .5f);
                } else if (node.text.toLowerCase().matches(".*?\\.(java)(.|\\s)*")) {
                    GL11.glColor3f(0f, .8f, 1f);
                } else if (node.text.toLowerCase().matches(".*?\\.(obj|srt|coord|coords)(.|\\s)*")) {
                    GL11.glColor3f(.1f, 1f, .1f);
                } else GL11.glColor3f(.8f, .5f, .01f);
            } else if (node.type.equals("Dir")) {
                isOur = true;
                GL11.glColor4f(1, 0, 0, (isOur ? 1 : .5f));
            }
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GLPrimitives.renderBox(radius, radius, radius);
            if (isHoovered || isOur) {
                GL11.glTranslatef(radius, radius, 0);
                GLUtil.billboardCylinder();
                GL11.glScalef(1, 2, 1);
                GLFont.getDefault().render();
                helpText.render();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            isTriggered = false;
        }
    }

    /** override node factory for graphing, now only producing balls.*/
    VisNode create(Node node, Vector3f pos) {
        return new VisCallBall(node, pos);
    }

    /** set up this scanner by starting the sampling thread*/
    public VisFilesys(Node n, Vector3f position) {
        super(null, position);
        if (n.text.length() == 0) n.text = ".";
        final String start = n.text;
        profile.addNode(new Node(0, "Vis/Client", "Set"));
        Thread scanner = new Thread() {

            public void run() {
                setName("VisProfiler");
                while (true) {
                    synchronized (VisFilesys.this) {
                        scandir(start, null);
                    }
                    ;
                    Sleeper.sleep(DELAY);
                }
            }
        };
        scanner.start();
    }

    /** called by scanner thread to do one thread sample.
	 * recursively samples filesystem 
	 * and adds samples to filesystem graph.
	 */
    void scandir(String startatdir, Node parentnode) {
        Node rootset = profile.getRoot();
        if (startatdir.equals("")) {
            startatdir = ".";
            parentnode = rootset;
        }
        if (parentnode == null) parentnode = rootset;
        File f = null;
        f = new File(startatdir);
        String path = f.getAbsolutePath();
        for (File f2 : f.listFiles()) {
            String fn1 = f2.getName();
            int size = (int) f2.length();
            boolean isdir = f2.isDirectory();
            char c = fn1.charAt(0);
            if ((c != '.') && (!fn1.endsWith("~")) && (!fn1.endsWith("strip") && (!fn1.endsWith(".class")))) {
                int fileId = fn1.hashCode();
                callCounter.add(fileId);
                sizeAdder.add(startatdir, size);
                Node fileNode = profile.nodes.get(fileId);
                if (fileNode == null) {
                    if (isdir) {
                        fileNode = new Node(fileId, fn1, "Dir");
                    } else {
                        fileNode = new Node(fileId, path + "/" + fn1 + "#" + size, "File");
                    }
                    profile.addNode(fileNode);
                    dirty = true;
                    profile.addEdge(new Edge(fileNode, rootset, null, "in", 1f));
                    profile.addEdge(new Edge(fileNode, parentnode, null, "", .05f));
                    if (isdir) {
                        scandir(startatdir + "/" + fn1, fileNode);
                    }
                }
            }
        }
    }

    void renderSelf() {
        super.renderSelf();
        if (dirty) synchronized (this) {
            Node root = profile.getRoot();
            update(root);
            dirty = false;
        }
    }
}
