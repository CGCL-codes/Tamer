package net.sf.peervibes.test.protocols.membership.global;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import org.apache.log4j.Logger;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.AppiaException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.EventQualifier;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.xml.interfaces.InitializableSession;
import net.sf.appia.xml.utils.SessionProperties;
import net.sf.peervibes.protocols.membership.events.GetPeersEvent;
import net.sf.peervibes.protocols.membership.events.InitMembershipEvent;
import net.sf.peervibes.protocols.p2p.events.P2PInitEvent;
import net.sf.peervibes.test.protocols.membership.global.events.InitMembershipTimeout;
import net.sf.peervibes.test.protocols.membership.global.events.JoinEvent;
import net.sf.peervibes.test.protocols.membership.global.events.JoinReplyEvent;
import net.sf.peervibes.test.protocols.membership.global.events.NewPeerEvent;
import net.sf.peervibes.test.protocols.membership.global.events.ShuffleEvent;
import net.sf.peervibes.test.protocols.membership.global.events.ShuffleTimer;
import net.sf.peervibes.utils.Peer;
import net.sf.peervibes.utils.RandomSamples;

/**
 * The <i>peer-to-peer communication</i> global membership session.
 * <br>
 * This session implements the basic operation of the global membership layer.
 * <br>
 *
 * @version 0.1
 * @author Joao Leitao
 */
public class GlobalMembershipSession extends Session implements InitializableSession {

    public static final int DEFAULT_PERIOD = 2000;

    private Channel channel;

    private Peer localNode;

    private Collection<Peer> globalView;

    private boolean initialized;

    private Random rand;

    private long shufflePeriod;

    private long timeout;

    private static Logger log = Logger.getLogger(GlobalMembershipSession.class);

    public GlobalMembershipSession(Layer layer) {
        super(layer);
        this.channel = null;
        this.localNode = null;
        this.globalView = new ArrayList<Peer>();
        this.initialized = false;
        this.rand = new Random();
        this.shufflePeriod = GlobalMembershipSession.DEFAULT_PERIOD;
    }

    public void init(SessionProperties parameters) {
        if (parameters.containsKey("period")) {
            this.shufflePeriod = parameters.getLong("period");
        }
        if (parameters.containsKey("timeout")) {
            this.timeout = parameters.getLong("timeout");
        }
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GetPeersEvent) {
            handleGetPeersEvent((GetPeersEvent) event);
        } else if (event instanceof ShuffleTimer) {
            handleShuffleTimer((ShuffleTimer) event);
        } else if (event instanceof ShuffleEvent) {
            handleShuffleEvent((ShuffleEvent) event);
        } else if (event instanceof JoinEvent) {
            handleJoinEvent((JoinEvent) event);
        } else if (event instanceof JoinReplyEvent) {
            handleJoinReplyEvent((JoinReplyEvent) event);
        } else if (event instanceof NewPeerEvent) {
            handleNewPeerEvent((NewPeerEvent) event);
        } else if (event instanceof P2PInitEvent) {
            handleP2PInitEvent((P2PInitEvent) event);
        } else if (event instanceof InitMembershipEvent) {
            handleInitMembershipEvent((InitMembershipEvent) event);
        } else if (event instanceof InitMembershipTimeout) {
            handleInitMembershipTimeout((InitMembershipTimeout) event);
        } else if (event instanceof ChannelInit) {
            handleChannelInit((ChannelInit) event);
        } else if (event instanceof ChannelClose) {
            handleChannelClose((ChannelClose) event);
        } else {
            log.warn("Unrequested event: \"" + event.getClass().getName() + "\".");
            try {
                event.go();
            } catch (AppiaEventException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleShuffleEvent(ShuffleEvent event) {
        for (Peer p : event.getView()) {
            if (!this.globalView.contains(p)) this.globalView.add(p);
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleShuffleTimer(ShuffleTimer event) {
        if (event.getDir() == Direction.UP && this.globalView.size() >= 2) {
            Peer[] c = this.globalView.toArray(new Peer[this.globalView.size()]);
            Peer d = c[this.rand.nextInt(c.length)];
            Collection<Peer> toSend = new ArrayList<Peer>(this.globalView);
            toSend.remove(d);
            toSend.add(this.localNode);
            try {
                ShuffleEvent se = new ShuffleEvent(toSend, this.channel, Direction.DOWN, this);
                se.dest = d.getAddress();
                se.source = this.localNode.getAddress();
                se.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleChannelClose(ChannelClose event) {
        this.channel = null;
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleChannelInit(ChannelInit event) {
        this.channel = event.getChannel();
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleInitMembershipEvent(InitMembershipEvent event) {
        if (!this.initialized) {
            try {
                JoinEvent je = new JoinEvent(this.localNode, this.channel, Direction.DOWN, this);
                je.dest = event.getContactNode().getAddress();
                je.source = this.localNode.getAddress();
                je.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
            try {
                ShuffleTimer st = new ShuffleTimer("GlobalMembershipShuffle", this.shufflePeriod, this.channel, Direction.DOWN, this, EventQualifier.ON);
                st.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            } catch (AppiaException e) {
                e.printStackTrace();
            }
            try {
                InitMembershipTimeout imt = new InitMembershipTimeout(((InitMembershipEvent) event.cloneEvent()), this.timeout, "GlobalMembershipInitMembershipTimeout", this.channel, Direction.DOWN, this, EventQualifier.ON);
                imt.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            } catch (AppiaException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            if (!this.globalView.contains(event.getContactNode())) {
                this.globalView.add(event.getContactNode());
                try {
                    NewPeerEvent npe = new NewPeerEvent(this.localNode, this.channel, Direction.DOWN, this);
                    npe.dest = event.getContactNode().getAddress();
                    npe.source = this.localNode.getAddress();
                    npe.go();
                } catch (AppiaEventException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleInitMembershipTimeout(InitMembershipTimeout event) {
        if (event.getQualifierMode() == EventQualifier.NOTIFY && !this.initialized) {
            InitMembershipEvent ime = event.getEvent();
            ime.setInitResult(this.initialized);
            ime.setDir(Direction.invert(ime.getDir()));
            ime.setSourceSession(this);
            try {
                ime.init();
                ime.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleP2PInitEvent(P2PInitEvent event) {
        this.localNode = event.getLocalPeer();
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleNewPeerEvent(NewPeerEvent event) {
        if (event.getDir() == Direction.UP) {
            if (!this.globalView.contains(event.getNewPeer())) this.globalView.add(event.getNewPeer());
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleJoinReplyEvent(JoinReplyEvent event) {
        if (event.getDir() == Direction.UP) {
            Collection<Peer> c = event.getView();
            for (Peer p : c) {
                if (!this.globalView.contains(p)) {
                    this.globalView.add(p);
                }
            }
            this.initialized = true;
            try {
                InitMembershipEvent ime = new InitMembershipEvent(this.channel, Direction.UP, this, true);
                ime.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleJoinEvent(JoinEvent event) {
        if (!this.globalView.contains(event.getNewPeer())) {
            try {
                ArrayList<Peer> list = new ArrayList<Peer>(this.globalView);
                list.add(this.localNode);
                JoinReplyEvent jre = new JoinReplyEvent(list, this.channel, Direction.DOWN, this);
                jre.dest = event.source;
                jre.source = this.localNode.getAddress();
                jre.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
            try {
                for (Peer p : this.globalView) {
                    NewPeerEvent npe = new NewPeerEvent(event.getNewPeer(), this.channel, Direction.DOWN, this);
                    npe.dest = p.getAddress();
                    npe.source = this.localNode.getAddress();
                    npe.go();
                }
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
            this.globalView.add(event.getNewPeer());
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    private void handleGetPeersEvent(GetPeersEvent event) {
        event.setAnswer(this.getSample(event.getN(), event.getException()));
        event.setDir(Direction.invert(event.getDir()));
        event.setSourceSession(this);
        try {
            event.init();
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<Peer> getSample(int n, Object exception) {
        ArrayList<Peer> ans = new ArrayList<Peer>();
        Collection<Peer> not = null;
        if (exception == null) {
            not = new ArrayList<Peer>();
        } else if (exception instanceof Peer) {
            not = new ArrayList<Peer>(1);
            not.add((Peer) exception);
        } else if (exception instanceof Collection) {
            not = (Collection<Peer>) exception;
        } else {
            not = new ArrayList<Peer>();
        }
        int[] uniformSample = RandomSamples.mkUniverse(this.globalView.size());
        RandomSamples.uniformSample(this.globalView.size(), uniformSample, this.rand);
        int inserted = 0;
        int i = 0;
        Peer[] view = this.globalView.toArray(new Peer[this.globalView.size()]);
        while (inserted < n && i < uniformSample.length) {
            if (!not.contains(view[uniformSample[i]])) {
                ans.add(view[uniformSample[i]]);
                inserted++;
            }
            i++;
        }
        return ans;
    }
}
