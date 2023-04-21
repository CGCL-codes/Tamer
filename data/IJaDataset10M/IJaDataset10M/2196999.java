package org.matsim.vis.otfvis;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import org.matsim.core.config.groups.OTFVisConfigGroup;
import org.matsim.vis.otfvis.data.OTFClientQuadTree;
import org.matsim.vis.otfvis.data.OTFConnectionManager;
import org.matsim.vis.otfvis.data.OTFServerQuadTree;
import org.matsim.vis.otfvis.data.fileio.OTFFileReader;
import org.matsim.vis.otfvis.data.fileio.SettingsSaver;
import org.matsim.vis.otfvis.gui.OTFHostControl;
import org.matsim.vis.otfvis.gui.OTFHostControlBar;
import org.matsim.vis.otfvis.gui.OTFTimeLine;
import org.matsim.vis.otfvis.handler.OTFAgentsListHandler;
import org.matsim.vis.otfvis.handler.OTFLinkAgentsHandler;
import org.matsim.vis.otfvis.opengl.drawer.OTFOGLDrawer;
import org.matsim.vis.otfvis.opengl.layer.AgentPointDrawer;
import org.matsim.vis.otfvis.opengl.layer.OGLAgentPointLayer;
import org.matsim.vis.otfvis.opengl.layer.OGLSimpleQuadDrawer;
import org.matsim.vis.otfvis.opengl.layer.OGLSimpleStaticNetLayer;

/**
 * This file starts OTFVis using a .mvi file.
 * 
 * @author dstrippgen
 * @author dgrether
 */
public class OTFClientFile implements Runnable {

    private final String url;

    public OTFClientFile(String filename) {
        super();
        this.url = filename;
    }

    @Override
    public final void run() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createDrawer();
            }
        });
    }

    private void createDrawer() {
        OTFClient otfClient = new OTFClient();
        OTFFileReader otfServer = new OTFFileReader(url);
        otfClient.setServer(otfServer);
        OTFVisConfigGroup otfVisConfig = otfServer.getOTFVisConfig();
        OTFConnectionManager connect = new OTFConnectionManager();
        connect.connectWriterToReader(OTFLinkAgentsHandler.Writer.class, OTFLinkAgentsHandler.class);
        connect.connectWriterToReader(OTFAgentsListHandler.Writer.class, OTFAgentsListHandler.class);
        connect.connectReaderToReceiver(OTFAgentsListHandler.class, AgentPointDrawer.class);
        connect.connectReaderToReceiver(OTFLinkAgentsHandler.class, OGLSimpleQuadDrawer.class);
        connect.connectReceiverToLayer(OGLSimpleQuadDrawer.class, OGLSimpleStaticNetLayer.class);
        connect.connectReceiverToLayer(AgentPointDrawer.class, OGLAgentPointLayer.class);
        OTFHostControlBar hostControlBar = otfClient.getHostControlBar();
        OTFHostControl otfHostControl = hostControlBar.getOTFHostControl();
        OTFTimeLine timeLine = new OTFTimeLine("time", otfHostControl);
        otfClient.getFrame().getContentPane().add(timeLine, BorderLayout.SOUTH);
        OTFServerQuadTree servQ = otfServer.getQuad(connect);
        OTFClientQuadTree clientQ = servQ.convertToClient(otfServer, connect);
        clientQ.setConnectionManager(connect);
        clientQ.getConstData();
        OTFClientQuadTree clientQuadTree = clientQ;
        OTFOGLDrawer mainDrawer = new OTFOGLDrawer(clientQuadTree, hostControlBar, otfVisConfig);
        otfClient.addDrawerAndInitialize(mainDrawer, new SettingsSaver(url));
        otfClient.show();
    }
}
