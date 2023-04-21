package com.ak.ib;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ak.order.OrderListener;
import com.ak.order.OrderManager;
import com.ak.utils.CommonUtils;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public class IBOrderManager extends OrderManager implements OrderListener, EWrapper {

    static Log log = LogFactory.getLog(IBOrderManager.class);

    private EClientSocket m_client = new EClientSocket(this);

    private Properties properties = new Properties();

    private boolean transmitt = false;

    private int triggerMethod = 0;

    private int client_id;

    private int nextOrderId;

    public IBOrderManager() {
        log.info("IBOrderManager starting ...");
    }

    private void startIBConnection(String _propFile) {
        try {
            InputStream _prop = IBTwsFactory.class.getClassLoader().getResourceAsStream(_propFile);
            if (_prop == null) {
                log.error("Properties file " + _propFile + "not found in classpath");
            }
            properties.load(_prop);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String host_ip = properties.getProperty("ib_host");
        int host_port = Integer.parseInt(properties.getProperty("ib_port"));
        client_id = Integer.parseInt(properties.getProperty("ib_clientid"));
        m_client.eConnect(host_ip, host_port, client_id);
        CommonUtils.sleep(1);
        if (m_client.isConnected()) {
            log.info("***** Connected to IB");
        } else {
            log.fatal(" *** Connection failure, please check  !");
            System.exit(1);
        }
    }

    public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
        log.info("QuoteListener:orderStatus-" + orderId + "," + status + "," + filled + "," + remaining + "," + avgFillPrice + "," + permId + "," + parentId + "," + lastFillPrice + "," + clientId + "," + whyHeld);
        try {
            updateOrderStatus(orderId, status);
            if (filled > 0) {
                fillOrderDetails(orderId, filled, lastFillPrice);
            }
        } catch (Exception e) {
            log.error("Order id : " + orderId + "status update fail !", e);
        }
    }

    @Override
    public void cancelAllPendingOrders() {
    }

    @Override
    public void closeAllPosition() {
    }

    @Override
    public int placeOrder(String contractString, String orderString) throws Exception {
        return 0;
    }

    @Override
    public void executionAlert() {
    }

    @Override
    public void systemAlert() {
    }

    @Override
    public void nextValidId(int orderId) {
        nextOrderId = orderId;
        log.info("Next valid order id : " + nextOrderId);
    }

    public int getNextValidId() {
        return nextOrderId;
    }

    @Override
    public void accountDownloadEnd(String accountName) {
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
    }

    @Override
    public void contractDetailsEnd(int reqId) {
    }

    @Override
    public void currentTime(long time) {
    }

    @Override
    public void deltaNeutralValidation(int reqId, UnderComp underComp) {
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
    }

    @Override
    public void execDetailsEnd(int reqId) {
    }

    @Override
    public void fundamentalData(int reqId, String data) {
    }

    @Override
    public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
    }

    @Override
    public void managedAccounts(String accountsList) {
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
    }

    @Override
    public void openOrderEnd() {
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
    }

    @Override
    public void receiveFA(int faDataType, String xml) {
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
    }

    @Override
    public void scannerDataEnd(int reqId) {
    }

    @Override
    public void scannerParameters(String xml) {
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double modelPrice, double pvDividend) {
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
    }

    @Override
    public void updateAccountTime(String timeStamp) {
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
    }

    @Override
    public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
    }

    @Override
    public void connectionClosed() {
    }

    @Override
    public void error(Exception e) {
    }

    @Override
    public void error(String str) {
    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
    }

    @Override
    public void setOrderManagerClientId(int clientId) {
    }
}
