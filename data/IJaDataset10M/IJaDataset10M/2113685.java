package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;

/**
 * 
 * @author amit bhayani
 * 
 */
public class Asp implements XMLSerializable {

    protected static final String NAME = "name";

    protected String name;

    /**
	 * Local FSM is such that it sends ASP_UP to other side
	 **/
    protected FSM localFSM;

    /**
	 * Peer FSM is such that it receives ASP_UP from other side
	 **/
    protected FSM peerFSM;

    protected AspFactory aspFactory;

    protected As as;

    protected ASPIdentifier aSPIdentifier;

    private MessageFactory messageFactory = new MessageFactoryImpl();

    public Asp() {
    }

    public Asp(String name, AspFactory aspFactroy) {
        this.name = name;
        this.aspFactory = aspFactroy;
        this.init();
    }

    private void init() {
        switch(this.aspFactory.functionality) {
            case IPSP:
                if (this.aspFactory.exchangeType == ExchangeType.SE) {
                    if (this.aspFactory.ipspType == IPSPType.CLIENT) {
                        this.initLocalFSM();
                    } else {
                        this.initPeerFSM();
                    }
                } else {
                    this.initPeerFSM();
                    this.initLocalFSM();
                }
                break;
            case AS:
                if (this.aspFactory.exchangeType == ExchangeType.SE) {
                    this.initLocalFSM();
                } else {
                    this.initPeerFSM();
                    this.initLocalFSM();
                }
                break;
            case SGW:
                if (this.aspFactory.exchangeType == ExchangeType.SE) {
                    this.initPeerFSM();
                } else {
                    this.initPeerFSM();
                    this.initLocalFSM();
                }
                break;
        }
    }

    private void initLocalFSM() {
        this.localFSM = new FSM(this.name + "_LOCAL");
        this.localFSM.createState(AspState.DOWN_SENT.toString());
        this.localFSM.createState(AspState.DOWN.toString());
        this.localFSM.createState(AspState.UP_SENT.toString());
        this.localFSM.createState(AspState.INACTIVE.toString());
        this.localFSM.createState(AspState.ACTIVE_SENT.toString());
        this.localFSM.createState(AspState.ACTIVE.toString());
        this.localFSM.createState(AspState.INACTIVE_SENT.toString());
        this.localFSM.setStart(AspState.DOWN.toString());
        this.localFSM.setEnd(AspState.DOWN.toString());
        this.localFSM.createTransition(TransitionState.COMM_UP, AspState.DOWN.toString(), AspState.UP_SENT.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.localFSM.createTimeoutTransition(AspState.UP_SENT.toString(), AspState.UP_SENT.toString(), 2000).setHandler(new THLocalAspDwnToAspUpSnt(this, this.localFSM));
        this.localFSM.createTransition(TransitionState.ASP_INACTIVE, AspState.UP_SENT.toString(), AspState.INACTIVE.toString());
        this.localFSM.createTransition(TransitionState.ASP_ACTIVE_SENT, AspState.UP_SENT.toString(), AspState.ACTIVE_SENT.toString());
        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.UP_SENT.toString(), AspState.DOWN_SENT.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.UP_SENT.toString(), AspState.DOWN.toString());
        this.localFSM.createTimeoutTransition(AspState.ACTIVE_SENT.toString(), AspState.ACTIVE_SENT.toString(), 2000);
        this.localFSM.createTransition(TransitionState.ASP_ACTIVE_ACK, AspState.ACTIVE_SENT.toString(), AspState.ACTIVE.toString());
        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.ACTIVE_SENT.toString(), AspState.DOWN_SENT.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE_SENT.toString(), AspState.DOWN.toString());
        this.localFSM.createTransition(TransitionState.ASP_INACTIVE_SENT, AspState.ACTIVE.toString(), AspState.INACTIVE_SENT.toString());
        this.localFSM.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AspState.ACTIVE.toString(), AspState.INACTIVE.toString());
        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.ACTIVE.toString(), AspState.DOWN_SENT.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());
        this.localFSM.createTransition(TransitionState.ASP_ACTIVE_SENT, AspState.INACTIVE.toString(), AspState.ACTIVE_SENT.toString());
        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.INACTIVE.toString(), AspState.DOWN_SENT.toString());
        this.localFSM.createTimeoutTransition(AspState.INACTIVE_SENT.toString(), AspState.INACTIVE_SENT.toString(), 2000);
        this.localFSM.createTransition(TransitionState.ASP_INACTIVE_ACK, AspState.INACTIVE_SENT.toString(), AspState.INACTIVE.toString());
        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.INACTIVE_SENT.toString(), AspState.DOWN_SENT.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE_SENT.toString(), AspState.DOWN.toString());
        this.localFSM.createTransition(TransitionState.ASP_DOWN_ACK, AspState.DOWN_SENT.toString(), AspState.DOWN.toString());
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.DOWN_SENT.toString(), AspState.DOWN.toString());
    }

    private void initPeerFSM() {
        this.peerFSM = new FSM(this.name + "_PEER");
        this.peerFSM.createState(AspState.DOWN.toString());
        this.peerFSM.createState(AspState.ACTIVE.toString());
        this.peerFSM.createState(AspState.INACTIVE.toString());
        this.peerFSM.setStart(AspState.DOWN.toString());
        this.peerFSM.setEnd(AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.COMM_UP, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.COMM_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_UP, AspState.DOWN.toString(), AspState.INACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.DAUD, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_ACTIVE, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_INACTIVE, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.PAYLOAD, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_UP, AspState.INACTIVE.toString(), AspState.INACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.ASP_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_ACTIVE, AspState.INACTIVE.toString(), AspState.ACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.PAYLOAD, AspState.INACTIVE.toString(), AspState.INACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_UP, AspState.ACTIVE.toString(), AspState.INACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.ASP_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_INACTIVE, AspState.ACTIVE.toString(), AspState.INACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.PAYLOAD, AspState.ACTIVE.toString(), AspState.ACTIVE.toString());
        this.peerFSM.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AspState.ACTIVE.toString(), AspState.INACTIVE.toString());
    }

    public String getName() {
        return this.name;
    }

    public FSM getLocalFSM() {
        return localFSM;
    }

    public FSM getPeerFSM() {
        return peerFSM;
    }

    public As getAs() {
        return as;
    }

    public void setAs(As as) {
        this.as = as;
    }

    public AspFactory getAspFactory() {
        return this.aspFactory;
    }

    public ASPIdentifier getASPIdentifier() {
        return aSPIdentifier;
    }

    public void setASPIdentifier(ASPIdentifier identifier) {
        aSPIdentifier = identifier;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    /**
	 * XML Serialization/Deserialization
	 */
    protected static final XMLFormat<Asp> ASP_XML = new XMLFormat<Asp>(Asp.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, Asp asp) throws XMLStreamException {
            asp.name = xml.getAttribute(NAME, "");
        }

        @Override
        public void write(Asp asp, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(NAME, asp.name);
        }
    };
}
