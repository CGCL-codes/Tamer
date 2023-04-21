package vavi.awt;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Run an Applet as an application.
 * <p>
 * Using this class you can add a trivial main program to any Applet
 * and run it directly, as well as from a browser or the appletviewer.
 * And unlike some versions of this concept, AppletFrame implements
 * images, not sound.
 * <p>
 * Sample main program:
 * <pre>
 * public static void main(String[] args) {
 *     new vavi.awt.AppletFrame(new ThisApplet(), args, 400, 400);
 * }
 * </pre>
 * The only methods you need to know about are the constructors.
 * <p>
 * You can specify Applet parameters on the command line, as name=value.
 * For instance, the equivalent of:
 * <pre>
 * &lt;PARAM NAME="vavi.awt.AppletFrame.parameter.pause" VALUE="200"&gt;
 * </pre>
 * would just be:
 * <pre>
 * pause=200
 * </pre>
 * You can also specify three special parameters:
 * <pre>
 * vavi.awt.AppletFrame.parameter.width=N          Width of the Applet.
 * vavi.awt.AppletFrame.parameter.height=N         Height of the Applet.
 * vavi.awt.AppletFrame.parameter.barebones=true   Leave off the menu bar and status area.
 * </pre>
 *
 * @caution	JDK 1.1 �p
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	010901	nsano	port from Acme.MainFrame <br>
 *		0.01	020415	nsano	un swinging <br>
 *		0.02	020613	nsano	fix size around <br>
 *		0.03	020620	nsano	refine parameter around <br>
 */
public class AppletFrame extends Frame implements Runnable, AppletStub, AppletContext {

    private static final String VERSION = "0.01";

    private static final String VENDOR = "Vavisoft";

    private static final String VENDOR_URL = "http://www.vavisoft.com/";

    /** ���j���[�o�[�ƃX�e�[�^�X�o�[��\�����Ȃ����ǂ��� */
    private boolean barebones = false;

    /** �X�e�[�^�X�o�[ */
    private Label label = null;

    /** �A�v���b�g�̃p�����[�^ */
    private String[] args = null;

    private static int instances = 0;

    /** �A�v���b�g�̃N���X�� */
    private String name;

    private Applet applet;

    private Dimension appletSize;

    private boolean active = false;

    private static final String PARAM_PROP_PREFIX = "vavi.awt.AppletFrame.parameter.";

    /** Constructor with everything specified. */
    public AppletFrame(Applet applet, String[] args, int width, int height) {
        build(applet, args, width, height);
    }

    /** Constructor with no default width/height. */
    public AppletFrame(Applet applet, String[] args) {
        build(applet, args, -1, -1);
    }

    /** Constructor with no arg parsing. */
    public AppletFrame(Applet applet, int width, int height) {
        build(applet, null, width, height);
    }

    private void build(Applet applet, String[] args, int width, int height) {
        ++instances;
        this.applet = applet;
        this.args = args;
        applet.setStub(this);
        name = applet.getClass().getName();
        setTitle(name);
        Properties props = System.getProperties();
        props.put("browser", getClass().getName());
        props.put("browser.version", VERSION);
        props.put("browser.vendor", VENDOR);
        props.put("browser.vendor.url", VENDOR_URL);
        if (args != null) parseArgs(args, props);
        String widthStr = getParameter(PARAM_PROP_PREFIX + "width");
        if (widthStr != null) width = Integer.parseInt(widthStr);
        String heightStr = getParameter(PARAM_PROP_PREFIX + "height");
        if (heightStr != null) height = Integer.parseInt(heightStr);
        if (width == -1 || height == -1) {
            System.err.println("Width and height must be specified.");
            return;
        }
        String bonesStr = getParameter(PARAM_PROP_PREFIX + "barebones");
        if (bonesStr != null && bonesStr.equals("true")) barebones = true;
        if (!barebones) {
            MenuBar mb = new MenuBar();
            Menu m = new Menu("Applet");
            MenuItem mi = new MenuItem("Restart");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ev) {
                    AppletFrame.this.applet.stop();
                    AppletFrame.this.applet.destroy();
                    Thread thread = new Thread(AppletFrame.this);
                    thread.start();
                }
            });
            m.add(mi);
            mi = new MenuItem("Clone");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ev) {
                    try {
                        new AppletFrame((Applet) AppletFrame.this.applet.getClass().newInstance(), AppletFrame.this.args, appletSize.width, appletSize.height);
                    } catch (IllegalAccessException e) {
                        showStatus(e.getMessage());
                    } catch (InstantiationException e) {
                        showStatus(e.getMessage());
                    }
                }
            });
            m.add(mi);
            mi = new MenuItem("Close");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ev) {
                    setVisible(false);
                    remove(AppletFrame.this.applet);
                    AppletFrame.this.applet.stop();
                    AppletFrame.this.applet.destroy();
                    if (label != null) remove(label);
                    dispose();
                    --instances;
                    if (instances == 0) System.exit(0);
                }
            });
            m.add(mi);
            mi = new MenuItem("Quit");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ev) {
                    AppletFrame.this.dispatchEvent(new WindowEvent(AppletFrame.this, WindowEvent.WINDOW_CLOSING));
                }
            });
            m.add(mi);
            mb.add(m);
            setMenuBar(mb);
        }
        addWindowListener(windowAdapter);
        setLayout(new BorderLayout());
        add(applet);
        if (!barebones) {
            Panel borderPanel = new Panel();
            borderPanel.setLayout(new BorderLayout());
            label = new Label(name);
            borderPanel.add(BorderLayout.CENTER, label);
            add(BorderLayout.SOUTH, borderPanel);
        }
        pack();
        validate();
        appletSize = applet.getSize();
        applet.setSize(width, height);
        show();
        (new Thread(this)).start();
    }

    /**
     * Turn command-line arguments into Applet parameters, by way of the
     * properties list.
     */
    private static void parseArgs(String[] args, Properties props) {
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            int ind = arg.indexOf('=');
            if (ind == -1) props.put(PARAM_PROP_PREFIX + arg.toLowerCase(), ""); else props.put(PARAM_PROP_PREFIX + arg.substring(0, ind).toLowerCase(), arg.substring(ind + 1));
        }
    }

    public void showStatus(String status) {
        if (label != null) label.setText(status);
    }

    /** Separate thread to call the applet's init() and start() methods. */
    public void run() {
        showStatus(name + " initializing...");
        applet.init();
        validate();
        showStatus(name + " starting...");
        applet.start();
        validate();
        showStatus(name + " running...");
    }

    public boolean isActive() {
        return active;
    }

    /** Returns the current directory. */
    public URL getDocumentBase() {
        String dir = System.getProperty("user.dir");
        String urlDir = dir.replace(File.separatorChar, '/');
        try {
            return new URL("file:" + urlDir + "/");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Hack: loop through each item in CLASSPATH, checking if
     * the appropriately named .class file exists there.  But
     * this doesn't account for .zip files.
     */
    public URL getCodeBase() {
        String path = System.getProperty("java.class.path");
        Enumeration st = new StringTokenizer(path, ":");
        while (st.hasMoreElements()) {
            String dir = (String) st.nextElement();
            String filename = dir + File.separatorChar + name + ".class";
            File file = new File(filename);
            if (file.exists()) {
                String urlDir = dir.replace(File.separatorChar, '/');
                try {
                    return new URL("file:" + urlDir + "/");
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /** Return a parameter via the munged names in the properties list. */
    public String getParameter(String name) {
        return System.getProperty(PARAM_PROP_PREFIX + name.toLowerCase());
    }

    /**
     * Change the frame's size by the same amount that the applet's
     * size is changing.
     */
    public void appletResize(int width, int height) {
        Dimension dimension = getSize();
        dimension.width += width - appletSize.width;
        dimension.height += height - appletSize.height;
        setSize(dimension);
        appletSize = applet.getSize();
    }

    public AppletContext getAppletContext() {
        return this;
    }

    /**
     * This is an internal undocumented routine.  However, it
     * also provides needed functionality not otherwise available.
     * I suspect that in a future release, JavaSoft will add an
     * audio content handler which encapsulates this, and then
     * we can just do a getContent just like for images.
     */
    public AudioClip getAudioClip(URL url) {
        return null;
    }

    public Image getImage(URL url) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        try {
            ImageProducer prod = (ImageProducer) url.getContent();
            return tk.createImage(prod);
        } catch (IOException e) {
            return null;
        }
    }

    /** Returns this Applet or nothing. */
    public Applet getApplet(String name) {
        if (name.equals(this.name)) return applet;
        return null;
    }

    /** Just yields this applet. */
    public Enumeration getApplets() {
        Vector v = new Vector();
        v.addElement(applet);
        return v.elements();
    }

    /** Ignore. */
    public void showDocument(URL url) {
    }

    /** Ignore. */
    public void showDocument(URL url, String target) {
    }

    /** */
    private WindowAdapter windowAdapter = new WindowAdapter() {

        public synchronized void windowOpened(WindowEvent ev) {
            applet.init();
            applet.start();
            active = true;
        }

        public synchronized void windowIconified(WindowEvent ev) {
            if (active) {
                active = false;
                applet.stop();
            }
        }

        public synchronized void windowDeiconified(WindowEvent ev) {
            if (!active) {
                applet.start();
                active = true;
            }
        }

        public synchronized void windowClosing(WindowEvent ev) {
            if (active) {
                active = false;
                applet.stop();
            }
            dispose();
        }

        public synchronized void windowClosed(WindowEvent ev) {
            applet.destroy();
            System.exit(0);
        }
    };

    /** @retroweave 1.1 */
    public void setStream(String string, InputStream is) {
    }

    /** @retroweave 1.1 */
    public InputStream getStream(String string) {
        return null;
    }

    /** @retroweave 1.1 */
    public Iterator getStreamKeys() {
        return null;
    }
}
