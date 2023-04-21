package simis.appLayer.economic.negotiation.bargaining;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;
import simis.appLayer.economic.AgentTypes;
import simis.appLayer.economic.EconomicAgent;
import simis.appLayer.economic.onto.Service;
import simis.messaging.BooleanContent;
import simis.messaging.Element;
import simis.messaging.HeaderElements;
import simis.messaging.IntegerContent;
import simis.messaging.Message;
import simis.messaging.MessageTypes;
import simis.messaging.StringContent;

public class AlternateOffersSellPayBefore extends AlternateOffers {

    protected HashMap<String, Integer> interestedAgents;

    public AlternateOffersSellPayBefore(EconomicAgent agent) {
        super(agent);
        this.logger = Logger.getLogger(this.getClass().toString());
        this.logger.setParent(super.rootlogger);
    }

    @Override
    public void currentAuctionStep() {
        if (this.auctionState == IDLE) {
            return;
        } else if (this.auctionState == WAITINGFORBIDS) {
            if (this.auctionTimeOut == 0) {
                String bestAgent = null;
                if (this.isProvider == true) {
                    bestAgent = this.getBestAgent(this.interestedAgents);
                }
                if (bestAgent == null) {
                    this.auctionState = IDLE;
                    this.myAgent.addCanceledNegotiation();
                } else {
                    if (this.isProvider) {
                        this.myPrice = myPrice - this.priceStep;
                    } else {
                        this.myPrice = myPrice + this.priceStep;
                    }
                    Message msg = new Message();
                    msg.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
                    msg.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.BID)));
                    msg.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
                    msg.getBody().setChild(new Element("Price", new IntegerContent(this.myPrice)));
                    this.auctionTimeOut = this.auctionTimeOutDefault;
                    this.myAgent.getSite().sendMessage(msg, bestAgent);
                    this.logger.info(this.myAgent.getAgentId() + " is proposing a new price " + this.myPrice + " to " + bestAgent);
                    this.auctionTimeOut = this.auctionTimeOutDefault;
                    this.auctionState = BIDDING;
                }
            } else {
                this.auctionTimeOut--;
            }
        } else if (this.auctionState == BIDDING) {
            if (this.auctionTimeOut == 0) {
                this.auctionState = IDLE;
                this.myAgent.addCanceledNegotiation();
            } else {
                this.auctionTimeOut--;
            }
        } else if (this.auctionState == AUCTIONFINISHED) {
            if (this.auctionTimeOut == 0) {
                this.reactOnFailure(this.winner);
                this.auctionState = IDLE;
            }
            this.auctionTimeOut--;
        }
    }

    @Override
    public void sellProduct(Service service) {
        if (this.auctionState == IDLE) {
            this.isProvider = true;
            this.myPrice = this.estimatedMarketPrice * 5 / 4;
            this.interestedAgents = new HashMap<String, Integer>();
            this.currentService = service.getItemType();
            this.logger.info(this.myAgent.getAgentId() + " is selling " + service.getItemType());
            Message msg = new Message();
            String targetGroup;
            targetGroup = AgentTypes.CSA;
            msg.getHeader().setChild(new Element(HeaderElements.TARGETGROUP, new StringContent(targetGroup)));
            msg.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
            msg.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.CALLFORBIDS)));
            msg.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
            msg.getBody().setChild(new Element("ServiceType", new StringContent(this.currentService)));
            msg.getBody().setChild(new Element("IsDemand", new BooleanContent(false)));
            msg.getBody().setChild(new Element("Price", new IntegerContent(this.myPrice)));
            this.auctionState = WAITINGFORBIDS;
            this.auctionTimeOut = auctionTimeOutDefault;
            myAgent.getSite().sendBroadcast(msg, this.hopCount);
        } else if (service == null) {
            this.logger.warning(this.myAgent.getAgentId() + " has a problem.");
        }
    }

    protected void interpretInterest(Message message) {
        Vector<Element> body = message.getBody().getChildren();
        String agentID = this.getSender(message).getValue();
        Integer price = null;
        for (Element elem : body) {
            if (elem.getName().equals("Price")) {
                price = ((IntegerContent) elem.getContent()).getValue();
            }
        }
        this.interestedAgents.put(agentID, price);
    }

    @Override
    public void interpretCallForBids(Message msg) {
        throw new RuntimeException("This method is not implemented in a selling strategy");
    }

    @Override
    public void interpretLost(Message msg) {
        this.logger.info("Service has already been sold to another agent");
        this.auctionState = IDLE;
        this.myAgent.addCanceledNegotiation();
    }

    @Override
    protected void reactOnMyAcceptance(String opponent) {
        this.auctionState = AUCTIONFINISHED;
        this.auctionTimeOut = this.auctionTimeOutDefault;
    }

    @Override
    public void interpretServiceInvocation(Message message) {
        if (this.auctionState == AUCTIONFINISHED) {
            this.logger.info(this.myAgent.getAgentId() + " has been invoked by agent " + this.getSender(message).getValue());
            int price = 0;
            Vector<Element> body = message.getBody().getChildren();
            for (Element elem : body) {
                if (elem.getName().equals(HeaderElements.PRICE)) {
                    price = ((IntegerContent) elem.getContent()).getValue();
                }
            }
            this.myAgent.modifyBudget(price);
            if (this.decideIfExecuting()) {
                this.myAgent.payForServiceInvocation();
                Message msg = new Message();
                msg.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
                msg.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.SUCCESSFULINVOCATION)));
                msg.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
                this.myAgent.getSite().sendMessage(msg, this.getSender(message).getValue());
            }
            this.reactOnSuccess(this.getSender(message).getValue());
            this.auctionState = IDLE;
        } else if (this.auctionState == AUCTIONFINISHED && !this.decideIfExecuting()) {
            this.logger.info("Resource agent " + this.myAgent.getAgentId() + " will not fulfill the service");
            this.auctionState = IDLE;
        } else {
            this.logger.warning("Agent " + " has been invoked, but is in state " + this.auctionState);
        }
    }

    @Override
    public void interpretSuccessfulInvocation(Message msg) {
        this.logger.warning("This method is not implemented in this AlternateOffers " + " selling strategy.");
    }

    @Override
    public void interpretPay(String sender, int price) {
        this.logger.warning("This method is not implemented in this AlternateOffers " + " selling strategy using a pay before model.");
    }

    @Override
    protected boolean decideIfAccept(String sender, int opponentPrice) {
        if (this.auctionState == BIDDING && opponentPrice >= this.myPrice) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void interpretSold(Message message) {
        if (this.auctionState == BIDDING || this.auctionState == WAITINGFORBIDS) {
            Vector<Element> body = message.getBody().getChildren();
            for (Element elem : body) {
                if (elem.getName().equals("Price")) {
                    this.opponentPrice = ((IntegerContent) elem.getContent()).getValue();
                }
            }
            this.myAgent.setCurrentPrice(this.opponentPrice);
            this.auctionState = AUCTIONFINISHED;
            this.auctionTimeOut = this.auctionTimeOutDefault;
            this.winner = this.getSender(message).getValue();
        } else {
            this.logger.info("Service has already been sold by agent " + this.myAgent.getAgentId());
            this.notifyLooser(this.getSender(message).getValue());
        }
    }

    @Override
    public void interpretBid(Message message) {
        if (auctionState == WAITINGFORBIDS) {
            this.interpretInterest(message);
        } else if (auctionState == BIDDING) {
            Vector<Element> body = message.getBody().getChildren();
            for (Element elem : body) {
                if (elem.getName().equals("Price")) {
                    this.opponentPrice = ((IntegerContent) elem.getContent()).getValue();
                }
            }
            if (this.decideIfAccept(this.getSender(message).getValue(), this.opponentPrice)) {
                this.logger.info("Agent " + this.myAgent.getAgentId() + " accepts proposal from " + this.getSender(message).getValue() + " for price " + this.opponentPrice);
                this.myAgent.getSite().sendMessage(this.sendAcceptanceNotification(), this.getSender(message).getValue());
                this.interpretSold(message);
            } else {
                if (new Random().nextDouble() >= this.acquisitiviness) {
                    this.myAgent.getSite().sendMessage(this.getCounterOffer(this.myPrice, this.opponentPrice), this.getSender(message).getValue());
                    this.auctionTimeOut = this.auctionTimeOutDefault;
                    this.logger.info(this.myAgent.getAgentId() + " has sent a Bid message with price " + this.myPrice + " to " + this.getSender(message).getValue());
                } else {
                    this.logger.info(this.myAgent.getAgentId() + " will leave the bargaining process.");
                }
            }
        } else {
            return;
        }
    }

    @Override
    public void interpretIsOppOk(String sender, Message msg) {
        throw new RuntimeException("not implemented here...");
    }
}
