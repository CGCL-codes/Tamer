package org.matsim.utils.vis.otfivs.opengl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.matsim.gbl.Gbl;
import org.matsim.utils.vis.otfivs.data.OTFClientQuad;
import org.matsim.utils.vis.otfivs.data.OTFConnectionManager;
import org.matsim.utils.vis.otfivs.gui.OTFHostControlBar;
import org.matsim.utils.vis.otfivs.gui.OTFVisConfig;
import org.matsim.utils.vis.otfivs.gui.PreferencesDialog;
import org.matsim.utils.vis.otfivs.handler.OTFAgentsListHandler;
import org.matsim.utils.vis.otfivs.handler.OTFDefaultLinkHandler;
import org.matsim.utils.vis.otfivs.handler.OTFDefaultNodeHandler;
import org.matsim.utils.vis.otfivs.handler.OTFLinkAgentsHandler;
import org.matsim.utils.vis.otfivs.handler.OTFLinkAgentsNoParkingHandler;
import org.matsim.utils.vis.otfivs.interfaces.OTFDrawer;
import org.matsim.utils.vis.otfivs.opengl.drawer.OTFOGLDrawer;
import org.matsim.utils.vis.otfivs.opengl.layer.ColoredStaticNetLayer;
import org.matsim.utils.vis.otfivs.opengl.layer.OGLAgentPointLayer;
import org.matsim.utils.vis.otfivs.opengl.layer.SimpleStaticNetLayer;
import org.matsim.utils.vis.otfivs.opengl.layer.OGLAgentPointLayer.AgentPointDrawer;

public class OnTheFlyClientFileQuad extends Thread {

    protected OTFHostControlBar hostControl = null;

    private final String filename;

    private boolean splitLayout = true;

    OTFConnectionManager connect = new OTFConnectionManager();

    public OTFDrawer getLeftDrawerComponent(JFrame frame) throws RemoteException {
        OTFConnectionManager connectL = this.connect.clone();
        connectL.remove(OTFLinkAgentsHandler.class);
        connectL.add(OTFLinkAgentsHandler.class, ColoredStaticNetLayer.QuadDrawer.class);
        connectL.add(ColoredStaticNetLayer.QuadDrawer.class, ColoredStaticNetLayer.class);
        OTFClientQuad clientQ = this.hostControl.createNewView(null, null, connectL);
        OTFDrawer drawer = new OTFOGLDrawer(frame, clientQ);
        return drawer;
    }

    public OTFDrawer getRightDrawerComponent(JFrame frame) throws RemoteException {
        OTFConnectionManager connectR = this.connect.clone();
        connectR.remove(OTFLinkAgentsHandler.class);
        connectR.add(OTFLinkAgentsHandler.class, SimpleStaticNetLayer.SimpleQuadDrawer.class);
        connectR.add(SimpleStaticNetLayer.SimpleQuadDrawer.class, SimpleStaticNetLayer.class);
        connectR.add(OTFLinkAgentsHandler.class, AgentPointDrawer.class);
        connectR.add(OGLAgentPointLayer.AgentPointDrawer.class, OGLAgentPointLayer.class);
        OTFClientQuad clientQ2 = this.hostControl.createNewView(null, null, connectR);
        OTFDrawer drawer2 = new OTFOGLDrawer(frame, clientQ2);
        return drawer2;
    }

    @Override
    public void run() {
        System.setProperty("javax.net.ssl.keyStore", "input/keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "vspVSP");
        System.setProperty("javax.net.ssl.trustStore", "input/truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "vspVSP");
        boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
        if (isMac) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        try {
            OTFVisConfig visconf = new OTFVisConfig();
            if (Gbl.getConfig() == null) Gbl.createConfig(null);
            Gbl.getConfig().addModule(OTFVisConfig.GROUP_NAME, visconf);
            System.out.println("Loading file " + this.filename + " ....");
            this.hostControl = new OTFHostControlBar("file:" + this.filename, this.getClass());
            JFrame frame = new JFrame("MATSim OTFVis");
            if (isMac) {
                frame.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
            }
            hostControl.frame = frame;
            frame.getContentPane().add(this.hostControl, BorderLayout.NORTH);
            JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            pane.setContinuousLayout(true);
            pane.setOneTouchExpandable(true);
            frame.getContentPane().add(pane);
            PreferencesDialog.buildMenu(frame, visconf, this.hostControl);
            if (this.splitLayout) {
                OTFDrawer drawer = getLeftDrawerComponent(frame);
                drawer.invalidate(0);
                this.hostControl.addHandler("test", drawer);
                pane.setLeftComponent(drawer.getComponent());
            }
            OTFDrawer drawer2 = getRightDrawerComponent(frame);
            pane.setRightComponent(drawer2.getComponent());
            this.hostControl.addHandler("test2", drawer2);
            drawer2.invalidate(0);
            System.out.println("Finished init");
            pane.setDividerLocation(0.5);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width / 2, screenSize.height / 2);
            frame.setVisible(true);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filename;
        if (args.length == 1) {
            filename = args[0];
        } else {
            filename = "/TU Berlin/workspace/MatsimJ/output/OTFQuadfileNoParking10p_wip.mvi";
        }
        OnTheFlyClientFileQuad client = new OnTheFlyClientFileQuad(filename);
        client.run();
    }

    public OnTheFlyClientFileQuad(String filename) {
        super();
        this.filename = filename;
        this.connect.add(OTFDefaultLinkHandler.Writer.class, OTFDefaultLinkHandler.class);
        this.connect.add(OTFLinkAgentsHandler.Writer.class, OTFLinkAgentsHandler.class);
        this.connect.add(OTFLinkAgentsNoParkingHandler.Writer.class, OTFLinkAgentsHandler.class);
        this.connect.add(OTFDefaultNodeHandler.Writer.class, OTFDefaultNodeHandler.class);
        this.connect.add(OTFAgentsListHandler.Writer.class, OTFAgentsListHandler.class);
        this.connect.add(OTFAgentsListHandler.class, OGLAgentPointLayer.AgentPointDrawer.class);
        this.connect.add(AgentPointDrawer.class, OGLAgentPointLayer.class);
    }

    public OnTheFlyClientFileQuad(String filename2, OTFConnectionManager connect) {
        this(filename2);
        this.connect = connect;
    }

    public OnTheFlyClientFileQuad(String filename2, OTFConnectionManager connect, boolean split) {
        this(filename2);
        this.connect = connect;
        this.splitLayout = split;
    }
}
