package org.infoeng.ictp.broker;

import javax.sql.DataSource;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activeio.packet.ByteSequence;
import org.apache.activemq.util.BrokerSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.jms.Destination;
import javax.jms.Topic;
import javax.jms.TextMessage;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.infoeng.ictp.documents.TradeOffer;
import org.infoeng.ictp.documents.TradeRequest;
import org.infoeng.icws.documents.InformationCurrencyUnit;
import org.infoeng.icws.util.Utils;

public class ICTPBroker extends BrokerFilter {

    private int IC_VERIFIED = 0x01;

    private int TRADEOFFER_FILLED = 0x02;

    private DataSource dataSource;

    private Connection conn;

    private Hashtable clientKeyIds;

    private Random myRandom;

    private String tradeRequestTopicId = "TradeRequests";

    private String tradeOfferTopicId = "TradeOffers";

    private static final Log log = LogFactory.getLog(ICTPInterceptorBroker.class);

    private String trCreate = " create table tradeRequest ( tradeRequestId int primary key, tradeRequestDigest varchar(40), offeredSeriesId blob, " + " offeredSeriesQuantity int, requestedSeriesId blob, requestedSeriesQuantity int, tradeRequestBlob blob ) ";

    private String trInsert = " insert into tradeRequest ( tradeRequestId, tradeRequestDigest, offeredSeriesId, " + " offeredSeriesQuantity, requestedSeriesId, requestedSeriesQuantity, tradeRequestBlob ) " + " values ( ?, ?, ?, ?, ?, ?, ? ) ";

    private String toCreate = " create table tradeOffer ( tradeOfferId int primary key, " + " receivedTime timestamp, status smallint, " + " clientIdentifier int references clientTable(clientIdentifier), " + " tradeOfferDigest varchar(40), escrowedIC blob, " + " offeredSeriesId varchar(512), " + " offeredSeriesQuantity int, requestedSeriesId varchar(512), " + " requestedSeriesQuantity int, tradeOfferBlob blob ) ";

    private String toInsert = " insert into tradeOffer ( tradeOfferId, clientIdentifier, " + " tradeOfferDigest, escrowedIC, offeredSeriesId, " + " offeredSeriesQuantity, requestedSeriesId, " + " requestedSeriesQuantity, tradeOfferBlob, receivedTime, status ) " + " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    private String escrowToUpdate = "update tradeOffer set escrowedIC=?, status=? where tradeOfferId=?";

    private String orderMatch = " select tradeOfferBlob, escrowedIC, " + " tradeOfferId, publicKeyString, tradeOfferDigest " + " from tradeOffer, clientTable " + " where status=? and " + " requestedSeriesId=? and offeredSeriesId=? and " + " clientTable.clientIdentifier=tradeOffer.clientIdentifier " + " order by requestedSeriesQuantity, receivedTime ";

    private String matchedOrderUpdate = " update tradeOffer set status=? where tradeOfferId=? or tradeOfferId=? ";

    private String clientTableCreate = " create table clientTable ( clientIdentifier int primary key, " + " publicKeyString varchar(8192) ) ";

    private String clientTableInsert = " insert into clientTable (clientIdentifier, publicKeyString) values ( ?, ? ) ";

    private String clientTableFind = " select clientIdentifier from clientTable where publicKeyString=? ";

    public ICTPBroker(Broker brkr) {
        super(brkr);
        clientKeyIds = new Hashtable();
        myRandom = new Random();
        System.setProperty("javax.net.ssl.trustStore", "/n/home/jpb/code/infoeng/trunk/icws-3/conf/localhost-keystore.jks");
    }

    public void setDataSource(DataSource ds) {
        initializeDB(ds);
    }

    public void send(ConnectionContext ctxt, Message message) throws Exception {
        Destination dest = ((javax.jms.Message) message).getJMSDestination();
        if (dest instanceof Topic && tradeOfferTopicId.equals(((Topic) dest).getTopicName())) {
            TextMessage txtMsg = (TextMessage) message;
            processTradeOffer(ctxt, txtMsg);
        } else if (dest instanceof Topic && tradeRequestTopicId.equals(((Topic) dest).getTopicName())) {
            TextMessage txtMsg = (TextMessage) message;
            processTradeRequest(ctxt, txtMsg);
        } else {
            next.send(ctxt, (org.apache.activemq.command.Message) message);
        }
    }

    private void processTradeRequest(ConnectionContext ctxt, TextMessage msg) throws Exception {
        String messageText = msg.getText();
        if (messageText == null || !(messageText.startsWith("<TradeRequest"))) throw new Exception("Message does not contain trade request.");
        TradeRequest tReq = new TradeRequest(new ByteArrayInputStream(messageText.getBytes()));
        PublicKey pubKey = tReq.getSignaturePublicKey();
        Integer clientId = findClient("" + ((RSAPublicKey) pubKey).getModulus() + "");
        if (clientId == null) {
            throw new Exception("Did not find / insert the client id.");
        }
        storeTradeRequest(tReq, clientId);
        next.send(ctxt, (org.apache.activemq.command.Message) msg);
    }

    private boolean storeTradeRequest(TradeRequest tmpTr, Integer clientInt) throws Exception {
        PreparedStatement trPS = conn.prepareStatement(trInsert);
        trPS.setInt(1, myRandom.nextInt(Integer.MAX_VALUE));
        trPS.setString(2, tmpTr.getDigestValue());
        String tmpStr = tmpTr.getOfferedSeriesID();
        if (tmpStr != null) trPS.setBlob(3, new SerialBlob(tmpStr.getBytes())); else trPS.setBlob(3, null);
        int tmpInt = tmpTr.getOfferedQuantity();
        trPS.setInt(4, tmpInt);
        tmpStr = tmpTr.getRequestedSeriesID();
        if (tmpStr != null) trPS.setBlob(5, new SerialBlob(tmpStr.getBytes())); else trPS.setBlob(5, null);
        tmpInt = tmpTr.getRequestedQuantity();
        trPS.setInt(6, tmpInt);
        trPS.setBlob(7, new SerialBlob(tmpTr.toString().getBytes()));
        int retNum = trPS.executeUpdate();
        if (retNum == 1) return true; else return false;
    }

    private void processTradeOffer(ConnectionContext ctxt, TextMessage msg) throws Exception {
        String messageText = msg.getText();
        if (messageText == null || !(messageText.startsWith("<TradeOffer"))) throw new Exception("Message does not contain trade offer.");
        TradeOffer tOffer = new TradeOffer(new ByteArrayInputStream(messageText.getBytes()));
        PublicKey pubKey = tOffer.getSignaturePublicKey();
        Integer clientId = findClient("" + ((RSAPublicKey) pubKey).getModulus() + "");
        if (clientId == null) {
            throw new Exception("Did not find / insert the client id.");
        }
        InformationCurrencyUnit[] icuArray = tOffer.getOfferedAssetIC();
        if (icuArray == null || icuArray.length == 0) {
            throw new Exception("Did not find IC in a trade offer.");
        }
        boolean invalidIc = false;
        ArrayList arrayList = new ArrayList();
        for (int x = 0; x < icuArray.length; x++) {
            InformationCurrencyUnit retIcu = icuArray[x].exchange();
            if (retIcu == null || retIcu.equals(icuArray[x])) {
                invalidIc = true;
                break;
            }
            arrayList.add(retIcu);
        }
        if (invalidIc) {
            String returnIcString = "";
            for (int x = 0; x < icuArray.length; x++) {
                returnIcString = returnIcString + icuArray[x].toString() + "";
            }
            int numItems = arrayList.size();
            for (int m = 0; m < numItems; m++) {
                InformationCurrencyUnit thisIcu = (InformationCurrencyUnit) arrayList.get(m);
                returnIcString = returnIcString + thisIcu.toString() + "";
            }
        } else {
            storeTradeOffer(tOffer, arrayList, clientId);
            ictpMatchOffers(ctxt, (TextMessage) msg, tOffer, arrayList, clientId);
        }
    }

    private boolean storeTradeOffer(TradeOffer tmpTo, List arrayList, Integer clientInt) throws Exception {
        int arraySize = arrayList.size();
        String icuString = "";
        for (int x = 0; x < arraySize; x++) {
            InformationCurrencyUnit icu = (InformationCurrencyUnit) arrayList.get(x);
            icuString = icuString + icu.toString() + "";
        }
        PreparedStatement toPS = conn.prepareStatement(toInsert);
        toPS.setInt(2, clientInt.intValue());
        String toDV = tmpTo.getDigestValue();
        toPS.setString(3, toDV);
        toPS.setBlob(4, new SerialBlob(icuString.getBytes()));
        toPS.setString(5, tmpTo.getOfferedSeriesID());
        toPS.setInt(6, tmpTo.getOfferedQuantity());
        toPS.setString(7, tmpTo.getRequestedSeriesID());
        toPS.setInt(8, tmpTo.getRequestedQuantity());
        toPS.setBlob(9, new SerialBlob(tmpTo.toString().getBytes()));
        toPS.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));
        toPS.setInt(11, IC_VERIFIED);
        int loopNum = 0;
        boolean updateWorked = false;
        while (true) {
            toPS.setInt(1, myRandom.nextInt(Integer.MAX_VALUE));
            int resNum = toPS.executeUpdate();
            loopNum++;
            if (resNum == 1) {
                updateWorked = true;
                break;
            } else {
                if (loopNum > 20) break;
            }
        }
        if (!updateWorked) {
            toPS.close();
            return false;
        }
        toPS.close();
        return true;
    }

    private boolean ictpMatchOffers(ConnectionContext ctxt, TextMessage txtMsg, TradeOffer tOffer, List arrayList, Integer clientInt) throws Exception {
        TradeOffer matchTo = null;
        InformationCurrencyUnit[] escrowedICArray = null;
        int matchOfferId = -1;
        String matchPubKeyString = null;
        String matchDigestValue = null;
        int reqQuant = tOffer.getRequestedQuantity();
        String reqSrs = tOffer.getRequestedSeriesID();
        int offerQuant = tOffer.getOfferedQuantity();
        String offerSrs = tOffer.getOfferedSeriesID();
        PreparedStatement offerMatchPS = conn.prepareStatement(orderMatch);
        offerMatchPS.setInt(1, IC_VERIFIED);
        offerMatchPS.setString(2, offerSrs);
        offerMatchPS.setString(3, reqSrs);
        ResultSet offerMatchRS = offerMatchPS.executeQuery();
        if (offerMatchRS != null) {
            if (offerMatchRS.next()) {
                Blob toBlob = offerMatchRS.getBlob("tradeOfferBlob");
                Blob icBlob = offerMatchRS.getBlob("escrowedIC");
                matchOfferId = offerMatchRS.getInt("tradeOfferId");
                matchPubKeyString = offerMatchRS.getString("publicKeyString");
                matchDigestValue = offerMatchRS.getString("tradeOfferDigest");
                matchTo = new TradeOffer(toBlob.getBinaryStream());
                escrowedICArray = InformationCurrencyUnit.parseArray(icBlob.getBinaryStream());
            }
        }
        offerMatchRS.close();
        offerMatchPS.close();
        if (matchTo != null && escrowedICArray != null && escrowedICArray.length > 0) {
            TradeOffer toDocOne = new TradeOffer();
            if (tOffer.getRequestedQuantity() > 0) toDocOne.setRequestedQuantity(tOffer.getRequestedQuantity());
            toDocOne.setRequestedSeriesID(tOffer.getRequestedSeriesID());
            toDocOne.setOfferedQuantity(tOffer.getOfferedQuantity());
            toDocOne.setOfferedSeriesID(tOffer.getOfferedSeriesID());
            toDocOne.setStatusText("filledOrder - " + tOffer.getDigestValue());
            toDocOne.addOfferedAsset(escrowedICArray);
            log.info(" trader series: " + escrowedICArray[0].getSeriesID() + ".");
            TradeOffer toDocTwo = new TradeOffer();
            toDocTwo.setRequestedQuantity(tOffer.getOfferedQuantity());
            toDocTwo.setRequestedSeriesID(tOffer.getOfferedSeriesID());
            if (tOffer.getRequestedQuantity() > 0) toDocTwo.setOfferedQuantity(tOffer.getRequestedQuantity());
            toDocTwo.setOfferedSeriesID(tOffer.getRequestedSeriesID());
            toDocTwo.setStatusText("filledOrder - " + matchDigestValue);
            int numIcUnits = arrayList.size();
            InformationCurrencyUnit[] icuArray = new InformationCurrencyUnit[numIcUnits];
            arrayList.toArray(icuArray);
            toDocTwo.addOfferedAsset(icuArray);
            log.info(" counterparty series: " + icuArray[0].getSeriesID() + ".");
            String counterPartyTopicString = "PUBLICKEY." + matchPubKeyString + "";
            ActiveMQTopic counterPartyTopic = new ActiveMQTopic(counterPartyTopicString);
            ActiveMQTextMessage counterPartyMessage = new ActiveMQTextMessage();
            counterPartyMessage.setText(toDocTwo.toString());
            counterPartyMessage.setDestination(counterPartyTopic);
            counterPartyMessage.setMessageId(new MessageId(UUID.randomUUID().toString()));
            BigInteger modInt = ((RSAPublicKey) tOffer.getSignaturePublicKey()).getModulus();
            String responseTopicString = "PUBLICKEY." + modInt + "";
            ActiveMQTopic responseTopic = new ActiveMQTopic(responseTopicString);
            ActiveMQTextMessage responseMsg = new ActiveMQTextMessage();
            responseMsg.setText(toDocOne.toString());
            responseMsg.setDestination(responseTopic);
            responseMsg.setMessageId(new MessageId(UUID.randomUUID().toString()));
            next.send(ctxt, (org.apache.activemq.command.Message) counterPartyMessage);
            next.send(ctxt, (org.apache.activemq.command.Message) responseMsg);
            return true;
        } else {
            tOffer.removeOfferedAsset();
            txtMsg.setText(tOffer.toString());
            next.send(ctxt, (org.apache.activemq.command.Message) txtMsg);
            return false;
        }
    }

    private void initializeDB(DataSource ds) {
        try {
            dataSource = ds;
            if (ds == null) return;
            String tmpTxt = "";
            if (ds != null) tmpTxt = " dataSource is not null ";
            log.info("setDataSource()" + tmpTxt + "!");
            conn = ds.getConnection();
            if (conn == null) return;
            Statement st = conn.createStatement();
            try {
                st.execute(clientTableCreate);
            } catch (Exception e) {
                String blah = Utils.getStackTrace(e);
                log.info(blah);
            }
            try {
                st.execute(trCreate);
            } catch (Exception ex) {
                String blah = Utils.getStackTrace(ex);
                log.info(blah);
            }
            try {
                st.execute(toCreate);
            } catch (Exception e) {
                String blah = Utils.getStackTrace(e);
                log.info(blah);
            }
            st.close();
        } catch (Exception e) {
            String blah = Utils.getStackTrace(e);
            log.info(blah);
        }
    }

    /**
     *  <p> Cache the client id int's, fetching them when 
     *      necessary. </p>
     *
     */
    private Integer findClient(String publicKeyString) throws Exception {
        int clientId = -1;
        Integer idInt = (Integer) clientKeyIds.get(publicKeyString);
        if (idInt == null) {
            PreparedStatement ps = conn.prepareStatement(clientTableFind);
            ps.setString(1, publicKeyString);
            ResultSet clientIdRS = ps.executeQuery();
            if (clientIdRS != null) {
                if (clientIdRS.next()) {
                    clientId = clientIdRS.getInt("clientIdentifier");
                    if (clientId != -1) clientKeyIds.put(publicKeyString, new Integer(clientId));
                }
            }
            clientIdRS.close();
            ps.close();
            if (clientId == -1) {
                int loopNum = 0;
                PreparedStatement clientIdInsPS = conn.prepareStatement(clientTableInsert);
                clientIdInsPS.setString(2, publicKeyString);
                while (true) {
                    clientId = myRandom.nextInt(Integer.MAX_VALUE);
                    clientIdInsPS.setInt(1, clientId);
                    int resNum = clientIdInsPS.executeUpdate();
                    loopNum++;
                    if (resNum == 1) break; else {
                        clientId = -1;
                        if (loopNum > 20) break;
                    }
                }
            }
            if (clientId != -1) return new Integer(clientId);
        } else return idInt;
        return null;
    }
}
