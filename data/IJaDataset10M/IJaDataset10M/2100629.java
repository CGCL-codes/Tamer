package org.personalsmartspace.onm.servmsg.test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkManager;
import org.junit.After;
import org.junit.Before;
import org.personalsmartspace.onm.api.pss3p.PSSAdvertisement;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;
import org.personalsmartspace.onm.servmsg.MessageQueue;

/**
 * @author Christopher Viana Lima
 *
 */
public class UTestMessageQueue {

    private static final String PIPEIDSTR = "urn:jxta:uuid-59616261646162614E50472050325033C0C1DE89719B456691A596B983BA0E1004";

    private NetworkManager manager;

    private PeerGroup netPeerGroup = null;

    private PipeService pipeService;

    private MessageQueue msgQueue;

    private PipeID pipeID;

    private String pipeAdv;

    private StubAdvertisementMgr advMgr;

    private StubPeerGroupMgr stubPgrp;

    private PSSAdvertisement targetPSS;

    private String sourceServiceID = "sourceServiceID";

    private String targetServiceID = "targetServiceID";

    private String destinationID = "destinationID";

    private boolean targetIsOtherPSS = false;

    private String targetOperation = "targetOperation";

    private boolean operationIsAsync = false;

    private String[] inputParametersasXML = { "parm1", "parm2" };

    private String[] parameterTypes = { "type1", "type2" };

    private ServiceMessage message = new ServiceMessage(sourceServiceID, targetServiceID, destinationID, targetIsOtherPSS, targetOperation, operationIsAsync, inputParametersasXML, parameterTypes);

    @Before
    public void setUp() throws Exception {
        advMgr = new StubAdvertisementMgr();
        stubPgrp = new StubPeerGroupMgr();
        msgQueue = new MessageQueue(advMgr);
        targetPSS = new PSSAdvertisement();
        targetPSS.setPssName("pssName");
        targetPSS.setPeerGroupID("urn:jxta:uuid-A0A5C386B13E4E97AC94D9156A75E40702");
        manager = null;
        try {
            manager = new net.jxta.platform.NetworkManager(NetworkManager.ConfigMode.EDGE, "ServerTest", new File(new File(".cache"), "ServerTest").toURI());
            manager.startNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pipeID = (PipeID) IDFactory.fromURI(new URI(PIPEIDSTR));
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
        netPeerGroup = manager.getNetPeerGroup();
        pipeService = netPeerGroup.getPipeService();
    }

    @After
    public void tearDown() throws Exception {
        manager.stopNetwork();
    }
}
