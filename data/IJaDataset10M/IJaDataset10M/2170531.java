package test.endtoend;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.das2.DasApplication;
import org.das2.util.filesystem.FileSystemSettings;
import org.virbo.autoplot.AutoplotApplet;
import org.virbo.autoplot.state.StatePersistence;

/**
 * Andrew's server
 * @author jbf
 */
public class TestApplet001 {

    private static void doTest(final String testId, final Map<String, String> params, boolean headless, String... args) {
        AppletStub stub = new AppletStub() {

            public boolean isActive() {
                return true;
            }

            public URL getDocumentBase() {
                return null;
            }

            public URL getCodeBase() {
                return null;
            }

            public String getParameter(String name) {
                return params.get(name);
            }

            public AppletContext getAppletContext() {
                return new AppletContext() {

                    public AudioClip getAudioClip(URL url) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Image getImage(URL url) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Applet getApplet(String name) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Enumeration<Applet> getApplets() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void showDocument(URL url) {
                        System.err.println("showDocument: " + url);
                    }

                    public void showDocument(URL url, String target) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void showStatus(String status) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void setStream(String key, InputStream stream) throws IOException {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public InputStream getStream(String key) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public Iterator<String> getStreamKeys() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }

            public void appletResize(int width, int height) {
            }
        };
        JFrame frame = null;
        if (!headless) {
            frame = new JFrame("autoplot applet");
        }
        AutoplotApplet applet = new AutoplotApplet();
        applet.setStub(stub);
        int width = 400;
        int height = 300;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("height")) {
                height = Integer.parseInt(args[i + 1]);
                i++;
            } else if (args[i].equals("width")) {
                width = Integer.parseInt(args[i + 1]);
                i++;
            } else {
                throw new IllegalArgumentException("bad param: " + args[i]);
            }
        }
        Dimension size = new Dimension(width, height);
        applet.setPreferredSize(size);
        if (!headless) {
            frame.getContentPane().add(applet);
            frame.pack();
            frame.setVisible(true);
        }
        applet.init();
        applet.start();
        applet.getDom().getController().waitUntilIdle();
        try {
            StatePersistence.saveState(new File("test.applet." + testId + ".vap"), applet.getDom());
        } catch (IOException ex) {
            Logger.getLogger(TestApplet001.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            applet.getDom().getController().getCanvas().getController().getDasCanvas().writeToPng("test.applet." + testId + ".png");
        } catch (IOException ex) {
            Logger.getLogger(AutoplotApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        boolean headless = true;
        String test = "test001";
        if (args.length > 0) test = args[0];
        Map<String, String> params = new HashMap<String, String>();
        int height = 200;
        if (test.equals("test001")) {
            params.put("dataSetURL", "vap+tsds:http://timeseries.org/get.cgi?StartDate=20030101&EndDate=20080831&ext=bin&out=tsml&ppd=1440&param1=OMNI_OMNIHR-26-v0");
            params.put("column", "5em,100%-10em");
            params.put("font", "sans-italic-10");
            params.put("row", "3em,100%-3em");
            params.put("renderType", "fillToZero");
            params.put("color", "#0000ff");
            params.put("fillColor", "#aaaaff");
            params.put("foregroundColor", "#ffffff");
            params.put("backgroundColor", "#000000");
            params.put("clickCallback", "onClick,label=Show Coordinates");
            params.put("codebase_lookup", "false");
            params.put("java_arguments", "-Djnlp.packEnabled=true");
        } else if (test.equals("test002")) {
            params.put("vap", "http://autoplot.org/data/autoplot-applet.vap");
            params.put("clickCallback", "onClick,label=Show Coordinates");
            params.put("codebase_lookup", "false");
            params.put("java_arguments", "-Djnlp.packEnabled=true");
            height = 400;
        } else if (test.equals("test003")) {
            params.put("dataSetURL", "http://www.sarahandjeremy.net/jeremy/1wire/data/$Y/0B000800408DD710.$Y$m$d.d2s?timerange=2009-03-14");
            params.put("column", "5em,100%-10em");
            params.put("font", "sans-italic-10");
            params.put("row", "3em,100%-3em");
            params.put("renderType", "fillToZero");
            params.put("color", "#0000ff");
            params.put("fillColor", "#aaaaff");
            params.put("foregroundColor", "#ffffff");
            params.put("backgroundColor", "#000000");
            params.put("clickCallback", "onClick,label=Show Coordinates");
            params.put("codebase_lookup", "false");
            params.put("java_arguments", "-Djnlp.packEnabled=true");
        }
        DasApplication.setRestrictPermission(true);
        FileSystemSettings.setRestrictPermission(true);
        try {
            doTest(test, params, headless, "height", String.valueOf(height), "width", "600");
        } catch (Exception ex) {
            ex.printStackTrace();
            if (headless) System.exit(1);
        }
        if (headless) System.exit(0);
    }
}
