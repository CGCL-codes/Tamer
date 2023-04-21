package org.rendezvous;

import org.datashare.objects.DataShareConnectionDescriptor;
import org.datashare.objects.ActivateConnectionObject;
import org.datashare.objects.DataShareObject;
import org.datashare.objects.ChannelDescription;
import org.datashare.objects.RequestHistory;
import org.datashare.objects.HistoryFinishedObject;
import org.datashare.client.ClientDataReceiverInterface;
import org.datashare.client.DataShareConnection;
import org.datashare.SessionUtilities;
import org.rendezvous.ppkgui.ParentOfWrapper;
import javax.swing.JTree;

/**
 * This class will provide the methods necessary to take a DataShareConnectionDescriptor,
 * establish the network connection, activate it, and provide input and output methods necessary to
 * share data over the described connection.
 */
public abstract class FunctionAdapter implements ClientDataReceiverInterface {

    protected DataShareConnectionDescriptor myConnectionDescription = null;

    protected DataShareConnection myConnection = null;

    protected ChannelLostInterface cli = null;

    private boolean useThreads = false;

    public String myKeyValue;

    public boolean isActivated = false;

    public boolean functionSuccessfullyLoaded = false;

    private HistoryFinishedInterface historyFinishedInterface;

    /**
    * Constructor, creates connection and activates it on the server
    */
    public FunctionAdapter() {
    }

    /**
    * called to initialize the function, subclasses should call this (with super)
    */
    public void initialize(String clientKey, DataShareConnectionDescriptor myConnectionDescription, ParentOfWrapper pclass, ChannelLostInterface cli) {
        System.out.println("FunctionAdapter.initialize() called: client-> " + clientKey + ", serverIP-> " + myConnectionDescription.serverIP.getHostAddress() + ", serverPort-> " + myConnectionDescription.serverPort);
        this.cli = cli;
        this.myConnectionDescription = myConnectionDescription;
        this.myConnectionDescription.clientKeyValue = clientKey;
        try {
            myConnection = new DataShareConnection(myConnectionDescription, this);
            myKeyValue = clientKey + "-" + myConnection.keyValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Activates our data channel on the server, we will not receive data until this is called!
    */
    protected void activateDataChannel() {
        System.out.println("Sending an ActivateConnectionObject for " + myConnection.keyValue);
        myConnection.sendToAll(new ActivateConnectionObject(myConnectionDescription.clientKeyValue), true);
        isActivated = true;
    }

    /**
    * This method must be supplied, and is called automatically whenever new
    * data is received from the server in this Channel.
    */
    public abstract void newDataReceived(DataShareObject dataShareObject);

    /**
    * This is called by PpKClient everytime the tree for this function has changed,
    * if this function has registered for tree updates by calling
    * the cli.registerForTreeChanges() method.  Note that this should be overidden for it to be any use.
    */
    public void newTreeArrived(JTree newTree) {
    }

    /**
    * call this method to get the latest tree from PpKClient
    */
    public JTree getTree() {
        return cli.getTree(this);
    }

    /**
    * FuntionAdapters call this to be notified (via newTreeAvailable()) when a new tree is available,
    * If registered and the tree changes, a call to newTreeArrived(JTree) in FunctionAdapter
    * is made when an updated tree is available.  Note that newTreeArrived() must be overidden in function
    * before registering will work.
    * @param addMe true if you are registering, false if you wish to be removed
    */
    public void registerForTreeChanges(boolean addMe) {
        System.out.println("FunctionAdapter.registerForTreeChanges() called by " + this.myKeyValue + " with value of " + addMe);
        cli.registerForTreeChanges(this, addMe);
    }

    /**
    * function must supply code for this method that will close all GUIs and stop all threads, etc.
    */
    protected abstract void shutDown();

    /**
    * this method is called when the data connection has been lost,
    * this will be used to notify anybody that cares... must be overidden,
    * must call cli.channelConnectionLost(this)
    */
    public void connectionLost(DataShareConnection dsc) {
        System.out.println("connectionLost() called in functionAdapter...");
        cli.channelConnectionLost(this);
    }

    /**
    * this method is called by the parent when this instance is supposed to clean up, shutdown,
    * and prepare to exit.
    */
    public void tryToShutDown() {
        System.out.println("FunctionAdapter().tryToShutDown()");
        shutDown();
        myConnection.closeAll();
    }

    public DataShareConnectionDescriptor getMyDataShareConnectionDescriptor() {
        return this.myConnectionDescription;
    }

    /**
    * Use this method to send an object to everybody in a Channel (including our instance)
    */
    protected void sendDataToAll(Object object) {
        myConnection.sendToAll(object);
    }

    /**
    * Use this method to send an object to everybody in a Channel (excluding our instance)
    */
    protected void sendDataToOthers(Object object) {
        myConnection.sendToOthers(object);
    }

    /**
    * Use this method to send an object to one client in a Channel
    */
    protected void sendDataToClient(Object object, String destinationClientKey) {
        myConnection.sendToClient(object, destinationClientKey);
    }

    /**
    * This method should not be overidden and is called automatically when data
    * is received.  This method takes care of ensuring that any objects we receive
    * that may cause a GUI update are called from the event thread to help us be
    * thread safe.
    */
    public void dataReceived(DataShareObject dataShareObject) {
        boolean forwardable = true;
        try {
            HistoryFinishedObject hfo = (HistoryFinishedObject) SessionUtilities.retrieveObject(dataShareObject.objectBytes);
            forwardable = false;
            if (historyFinishedInterface != null) historyFinishedInterface.historyFinished(this); else System.out.println(" *** received HistoryFinishedObject but our interface callback is null!");
            historyFinished();
        } catch (ClassCastException cce) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (forwardable) {
            if (useThreads) {
                final DataShareObject thisObject = dataShareObject;
                final Runnable sendTheObject = new Runnable() {

                    public void run() {
                        newDataReceived(thisObject);
                    }
                };
                javax.swing.SwingUtilities.invokeLater(sendTheObject);
            } else {
                newDataReceived(dataShareObject);
            }
        }
    }

    /**
    * send object to server to request the history of this channel be sent, note
    * that this request is sent in the Data channel of the function
    */
    public void requestHistory(HistoryFinishedInterface hfi) {
        historyFinishedInterface = hfi;
        RequestHistory rh = new RequestHistory(this.myConnectionDescription.clientKeyValue, RequestHistory.DATA, this.myConnectionDescription.sessionName, this.getMyDataShareConnectionDescriptor().databaseID, this.myConnectionDescription.channelDescription.channelName);
        this.myConnection.sendToAll(rh, true);
    }

    /**
    * send object to server to cancel the history request of this channel, note
    * that this request is sent in the Data channel of the function
    */
    public void cancelHistory() {
        RequestHistory rh = new RequestHistory(this.myConnectionDescription.clientKeyValue, RequestHistory.CANCEL, this.myConnectionDescription.sessionName, this.getMyDataShareConnectionDescriptor().databaseID, this.myConnectionDescription.channelDescription.channelName);
        this.myConnection.sendToAll(rh, true);
    }

    /**
    * any function can supply this method if it is desired to know when the historyFinished 
    * object has been received (no more historical data is coming).
    */
    public void historyFinished() {
    }

    /**
    * The function should call this to ask the server for the Token for this Channel.
    * Only one client at a time can have the token.
    * Our method tokenReceived() will be called if/when we get the Token.
    */
    public void tokenRequest() {
        if (SessionUtilities.getVerbose()) System.out.println("FunctionAdapter.tokenRequest() called");
        cli.tokenRequest(this.myConnectionDescription.sessionName, this.myConnectionDescription.channelDescription.channelName, this.myConnectionDescription.tokenKey);
    }

    /**
    * The function should call this if the Token has been requested, and is no longer needed.
    * This method can be called before the Token has been received (i.e. before tokenReceived() has
    * been called) in order to cancel the request, or it can be called after the token has been
    * received in order to give the Token back to the server.
    */
    public void tokenCancel() {
        if (SessionUtilities.getVerbose()) System.out.println("FunctionAdapter.tokenCancel() called");
        cli.tokenCancel(this.myConnectionDescription.tokenKey);
    }

    /**
    * Automatically called when the server has given us the token for this channel,
    * should be overidden by any functions that wish to implement tokens.
    * If this method is called with tokenAvailable true, the function can assume it
    * has the Token.   If this method is called with tokenAvailable false, the function
    * should immediately stop using whatever resource is controlled by the Token (the
    * server has revoked the Token and probably given it to some other client)
    *
    * @parma tokenAvailable true if we have the token, false if we do not
    */
    public void tokenReceived(boolean tokenAvailable) {
        System.out.println("FunctionAdapter.tokenReceived() DEFAULT method called (must be a mistake!)");
    }

    protected void finalize() throws Throwable {
        System.out.println("Finalizing FunctionAdapter for " + this.myKeyValue);
    }
}
