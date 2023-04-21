package org.rendezvous.appshare3;

import org.rendezvous.FunctionAdapter;
import org.rendezvous.ChannelLostInterface;
import org.datashare.SessionUtilities;
import org.datashare.client.DataShareConnection;
import org.rendezvous.AboutBox;
import org.datashare.objects.DataShareConnectionDescriptor;
import org.datashare.objects.DataShareObject;
import org.datashare.objects.ChannelDescription;
import org.rendezvous.ppkgui.ParentOfWrapper;
import org.rendezvous.ppkgui.WrapJInternalFrame;
import org.rendezvous.ppkgui.WrapperInterface;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBoxMenuItem;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * This class uses all-java techniques to allow a user to share a region of his
 * desktop with other users.  The sharer may also allow other users, one at a time,
 * to perform mouse-functions, clipboard-operations, and keystrokes in the shared
 * region of the desktop.
 * The sharer will be able to specify who can control, stop any controller, specify
 * a rate at which screen images are sent...
 * The AppSharePanel is where the sharer can preview what is sent, and the sharee
 * sees the desktop of the sharer.
 * The AppSharePacket is sent between the sharer<->sharee and will contain one of
 * the following: screenImage info, clipBoard, mouse events, keyStrokes
 * Who is sharer and who is a controlling sharee will probably be implemented using
 * two Tokens, that way only one sharer and one sharee can exist at any time.  How
 * we will allow transfer of the Tokens is TBD.
 */
public class AppShareMain extends FunctionAdapter implements Runnable {

    /**
    * used when the user requests the about box for this instance
    *
    */
    protected AboutBox aboutBox = null;

    /**
    * 'About' under 'Help'
    *
    */
    public JMenuItem helpAboutMenuItem = new JMenuItem();

    /**
    * the 'Help' menu
    *
    */
    public JMenu helpMenu = new JMenu();

    JMenuBar menuBar = new JMenuBar();

    JCheckBoxMenuItem sendOnlyDifferencesCBox = new JCheckBoxMenuItem();

    JMenu editMenu = new JMenu();

    ParentOfWrapper pclass;

    WrapperInterface wrapped;

    AppSharePanel appSharePanel;

    String myClientKey;

    JScrollPane myScrollPane = new JScrollPane();

    int myIDinMyPclass;

    Object ack = new Object();

    DifferenceFactory df = new DifferenceFactory();

    boolean usingDifferences = false;

    /**
    * used if we are a sharer and we have allowed another client to control us,
    * null otherwise;
    */
    String controllingClient = "";

    /**
    * set to true if we, as an ACTIVE_SHARER, are going to allow a user to control us
    */
    boolean allowControl = false;

    /**
    * will be set to the ACTIVE_SHARER (where images come from), if we are not ACTIVE_SHARER,
    * note that this is only set if we see command that tells us there is an ACTIVE_SHARER.  We
    * could also get this info from who is sending us the images, but I don't think that would be wise.
    */
    String activeSharer = "";

    static final int UNKNOWN = 0;

    static final String sharerStateStrings[] = { "UNKNOWN", "ACTIVE_SHARER", "ENABLED_SHARER", "ENABLED_SHAREE", "DISABLED_SHAREE", "CONTROLLER", "VIEWING_HISTORY" };

    /**
    * these are the valid values for the sharerState attribute
    */
    static final int ACTIVE_SHARER = 1;

    static final int ENABLED_SHARER = 2;

    static final int ENABLED_SHAREE = 3;

    static final int DISABLED_SHAREE = 4;

    static final int CONTROLLER = 5;

    static final int VIEWING_HISTORY = 6;

    /**
    * indicates what the state of this instance
    */
    private int sharerState;

    static final String sharerStateCommandStrings[] = { "UNKNOWN", "DEACTIVATE_SHARER", "ACTIVATE_SHARER", "ENABLE_SHARER", "DISABLE_SHARER", "CONTROL", "END_CONTROL", "ENABLE_SHAREE", "DISABLE_SHAREE", "VIEW_HISTORY" };

    /**
    * these are the valid command values for changing the sharer state attribute, used
    * as inputs to the setSharer method.
    * State can go from ENABLED_SHARER to ACTIVE_SHARER via an ACTIVATE_SHARER command,
    *              from ACTIVE_SHARER to ENABLED_SHARER via a DEACTIVATE_SHARER command,
    *              from ENABLED_SHARER to ENABLED_SHAREE via a DISABLE_SHARER command,
    *              from ENABLED_SHAREE to ENABLED_SHARER via an ENABLE_SHARER command,
    *              from ENABLED_SHAREE to CONTROLLER via a CONTROL command,
    *              from CONTROLLER to ENABLED_SHAREE via an END_CONTROL command,
    *              from ENABLED_SHAREE to DISABLED_SHAREE via a DISABLE_SHAREE command,
    *              from DISABLED_SHAREE to ENABLED_SHAREE via an ENABLE_SHAREE
    */
    static final int DEACTIVATE_SHARER = 1;

    static final int ACTIVATE_SHARER = 2;

    static final int ENABLE_SHARER = 3;

    static final int DISABLE_SHARER = 4;

    static final int CONTROL = 5;

    static final int END_CONTROL = 6;

    static final int ENABLE_SHAREE = 7;

    static final int DISABLE_SHAREE = 8;

    static final int VIEW_HISTORY = 9;

    /**
    * set to true while the sharer is actually sharing desktop images, note that
    * an instance can be a sharer that is not sharing.
    */
    boolean sendingImages = false;

    /**
    * used to determine what part of the desktop to share if we are the sharer,
    * can be changed while sharing to adjust what is shared dynamically
    */
    Rectangle sharingRegion = new Rectangle(0, 0, 420, 200);

    /**
    * provides most of the functionality of AppShare, used to generate native system
    * input events where control of the mouse and keyboard is needed, also used
    * to provide the screen image for sharing.
    */
    private Robot robot;

    /**
    * used to determine how long the sharer should delay between sending desktop images,
    * units are mSec (1000 is 1 second), can be changed while sharing to adjust bandwidth
    */
    int delayTime = 2000;

    /**
    * set to true if we should display what we send, only applies if we are already sending
    */
    boolean previewing;

    /**
    * used to set the jpeg image quality, values should be between 0 and 100 are percent values
    */
    int quality = 40;

    /**
    * the empty constructor, initialize must be called for anything interesting to happen.
    */
    public AppShareMain() {
    }

    /**
    * gets sharer value, indicates our sharer state
    */
    int getSharerState() {
        return sharerState;
    }

    /**
    * this method handles state changes for AppSharing.  The state changes can
    * be generated by GUI events and commands from other clients.
    */
    public synchronized void setSharerState(int sharerCommand) {
        if (SessionUtilities.getVerbose()) System.out.println("AppShareMain.setSharerState() called with command of " + sharerStateCommandStrings[sharerCommand]);
        switch(sharerCommand) {
            case ACTIVATE_SHARER:
                if (sharerState == ENABLED_SHARER) {
                    sharerState = ACTIVE_SHARER;
                    if (SessionUtilities.getVerbose()) System.out.println("we just became ACTIVE_SHARER, so sending SHARER_DISABLE to others...");
                    sendDataToOthers(new AppSharePacket(myClientKey, AppSharePacket.SHARER_DISABLE));
                    controllingClient = "";
                    sendingImages = false;
                    previewing = false;
                    allowControl = false;
                    startSharing();
                } else System.out.println("AppShareMain.setSharerState mode wrong, activate_sharer command when not enabled_sharer");
                break;
            case DEACTIVATE_SHARER:
                if (sharerState == ACTIVE_SHARER) {
                    allowControl = false;
                    sendingImages = false;
                    sharerState = ENABLED_SHARER;
                    forceACK();
                    if (!controllingClient.equals("")) {
                        if (SessionUtilities.getVerbose()) System.out.println("We were an ACTIVE_SHARER and had a controlling client, disable the controller...");
                        stopTheController();
                    }
                    if (SessionUtilities.getVerbose()) System.out.println("we just became ENABLED_SHARER, so sending SHARER_ENABLE to others...");
                    sendDataToOthers(new AppSharePacket(myClientKey, AppSharePacket.SHARER_ENABLE));
                    if (SessionUtilities.getVerbose()) System.out.println("forcing an ACK because we just stopped sending images and don't want it to hang in send loop");
                } else System.out.println("AppShareMain.setSharerState mode wrong, deactivate_sharer command when not active_sharer");
                break;
            case ENABLE_SHARER:
                if (sharerState == UNKNOWN || sharerState == ENABLED_SHAREE || sharerState == VIEWING_HISTORY) sharerState = ENABLED_SHARER; else System.out.println("AppShareMain.setSharerState mode wrong, enable_sharer command when not enabled_sharee");
                break;
            case DISABLE_SHARER:
                if (sharerState == ENABLED_SHARER) sharerState = ENABLED_SHAREE; else System.out.println("AppShareMain.setSharerState mode wrong, disable_sharer command when not enabled_sharer");
                break;
            case CONTROL:
                if (sharerState == ENABLED_SHAREE) {
                    sharerState = CONTROLLER;
                    appSharePanel.startControlling();
                } else System.out.println("AppShareMain.setSharerState mode wrong, control command when not enabled_sharee");
                break;
            case END_CONTROL:
                if (sharerState == CONTROLLER) {
                    sharerState = ENABLED_SHAREE;
                    appSharePanel.stopControlling();
                } else System.out.println("AppShareMain.setSharerState mode wrong, end_control command when not controller");
                break;
            case ENABLE_SHAREE:
                if (sharerState == DISABLED_SHAREE || sharerState == ENABLED_SHAREE) sharerState = ENABLED_SHAREE; else System.out.println("AppShareMain.setSharerState mode wrong, enable_sharee command when not disabled_sharee");
                break;
            case DISABLE_SHAREE:
                if (sharerState == ENABLED_SHAREE) sharerState = DISABLED_SHAREE; else System.out.println("AppShareMain.setSharerState mode wrong, disable_sharee command when not enabled_sharee");
                break;
            case VIEW_HISTORY:
                sharerState = VIEWING_HISTORY;
                break;
        }
        appSharePanel.setGUIState(sharerState);
    }

    public void initialize(String clientKey, DataShareConnectionDescriptor myConnectionDescription, ParentOfWrapper pclass, ChannelLostInterface cli) {
        super.initialize(clientKey, myConnectionDescription, pclass, cli);
        try {
            this.pclass = pclass;
            myClientKey = clientKey;
            appSharePanel = new AppSharePanel(this, robot);
            robot = new Robot();
            wrapped = new WrapJInternalFrame(pclass, myConnectionDescription.name + " (" + ChannelDescription.validTypes[myConnectionDescription.channelDescription.type] + ")", true, true, false, true, true);
            editMenu.setText("Edit");
            editMenu.add(sendOnlyDifferencesCBox);
            sendOnlyDifferencesCBox.setText("send only image changes");
            sendOnlyDifferencesCBox.setState(usingDifferences);
            sendOnlyDifferencesCBox.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    usingDifferences = sendOnlyDifferencesCBox.getState();
                }
            });
            helpMenu.setText("Help");
            helpAboutMenuItem.setText("About");
            menuBar.add(editMenu);
            menuBar.add(helpMenu);
            String[] comments = { "AppShare allows you to share a region of your desktop,", "let other(s) control it, or view any other sharer's desktop." };
            addHelpAbout("AppSharing", "Version 3.0, 1300 February 19, 2002", comments);
            pclass.add((Component) wrapped);
            myIDinMyPclass = pclass.newChild(wrapped);
            wrapped.getContentPane().add(appSharePanel);
            wrapped.setGoneExternalMessage("AppSharing Function is on external window.");
            wrapped.setFunctionAdapter(this);
            wrapped.setMyScrollPane(myScrollPane);
            SessionUtilities.scrollPaneDroppingsFixer(myScrollPane);
            wrapped.setJMenuBar(menuBar);
            wrapped.setSize(new Dimension(450, 400));
            wrapped.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        functionSuccessfullyLoaded = true;
    }

    /**
    * code for this method that will close all GUIs and stop all threads, etc.
    */
    protected void shutDown() {
        System.out.println("AppShareMain().shutdown()");
        switch(sharerState) {
            case ACTIVE_SHARER:
                if (!controllingClient.equals("")) {
                    stopTheController();
                    sendDataToOthers(new AppSharePacket(this.myClientKey, AppSharePacket.CONTROL_ENABLED));
                    forceACK();
                }
                sendDataToOthers(new AppSharePacket(this.myClientKey, AppSharePacket.SHARER_ENABLE));
                break;
            case ENABLED_SHARER:
            case ENABLED_SHAREE:
            case DISABLED_SHAREE:
            case VIEWING_HISTORY:
                break;
            case CONTROLLER:
                sendControlCommand(new AppSharePacket(this.myClientKey, AppSharePacket.CONTROL_STOPPING));
                if (appSharePanel != null) appSharePanel.stopControlling();
                break;
        }
        this.registerForTreeChanges(false);
        JTextArea jta = new JTextArea("Lost Data Connection to Server");
        WrapperInterface wi = (WrapperInterface) pclass.getAllChildren().elementAt(this.myIDinMyPclass);
        wi.getContentPane().removeAll();
        appSharePanel = null;
        wi.getContentPane().add(jta);
    }

    /**
    * causes this sharer to start a new thread that will share it's desktop image with sharee(s).
    * Note that the images are not sent until sendImages is set to true.  The thread
    * is not stopped until we leave the ACTIVE_SHARER state.
    * 
    */
    public void startSharing() {
        if (SessionUtilities.getVerbose()) System.out.println("AppShareMain.startSharing() called");
        Thread mySharingThread = new Thread(this, "AppShareMain.sendImages for " + myClientKey);
        mySharingThread.start();
        Thread.currentThread().yield();
    }

    /**
    * Activates our data channel on the server, we will not receive data until this is called!
    */
    protected void activateDataChannel() {
        super.activateDataChannel();
        setSharerState(ENABLE_SHARER);
    }

    /**
    * automatically called when the historyFinished 
    * object has been received (no more historical data is coming).
    */
    public void historyFinished() {
        setSharerState(ENABLE_SHARER);
    }

    /**
    * used if we are the ACTIVE_SHARER and we want to stop a CONTROLLER
    */
    void stopTheController() {
        sendDataToClient(new AppSharePacket(myClientKey, AppSharePacket.CONTROL_STOPPING), controllingClient);
        controllingClient = "";
    }

    /**
    * this is how the AppSharePanel GUI is able to request to be the Controller
    * @param value true means we are requesting to be the controller,
    * false means we no longer want to control
    */
    void requestControl(boolean value) {
        if (SessionUtilities.getVerbose()) System.out.println("AppShareMain.requestControl called with a value of " + value);
        if (value) sendControlCommand(new AppSharePacket(this.myClientKey, AppSharePacket.CONTROL_REQUEST)); else {
            sendControlCommand(new AppSharePacket(this.myClientKey, AppSharePacket.CONTROL_STOPPING));
            setSharerState(END_CONTROL);
        }
    }

    /**
    * called only by the sharer, used to send the contents of the clipboard to a controlling sharee,
    * I don't think this really needs to go to the sharee for appSharing to work, but it
    * is a nice feature that lets you 'get' some objects from the sharer that you would
    * otherwise have a hard time getting.
    */
    public void sendClipboard(AppSharePacket packet) {
        if (controllingClient != null) this.sendDataToClient(packet, controllingClient); else System.out.println("Trying to send a Clipboard to a non-Controller (" + controllingClient + "), or without a Paste type");
    }

    /**
    * called by the AppSharePanel, used to send control commands from a CONTROLLER to 
    * the ACTIVE_SHARER.
    */
    void sendControlCommand(AppSharePacket packet) {
        if (sharerState == CONTROLLER || sharerState == ENABLED_SHAREE) {
            sendDataToClient(packet, activeSharer);
        }
    }

    /**
    * adds an About Dialog to the end of the Help menu, with the Strings displayed
    * along with the Ball logo
    *
    * @param frame2 the frame to be used to position the help box
    * @param productName2 the product/function name
    * @param versionAndDate2 the version/date for the function
    * @param comments2 any function specific text to be displayed
    */
    public void addHelpAbout(String productName2, String versionAndDate2, String[] comments2) {
        final String productName = productName2;
        final String versionAndDate = versionAndDate2;
        final String[] comments = comments2;
        helpAboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            /**
              * called when the Help/About menu is selected
              *
              * @param e the event that caused this method to be called
              */
            public void actionPerformed(ActionEvent e) {
                try {
                    Frame possibleFrame = SessionUtilities.getParentFrame((Container) pclass.getAllChildren().elementAt(myIDinMyPclass));
                    helpAboutMenuItem_actionPerformed(possibleFrame, productName, versionAndDate, comments);
                } catch (Exception ee) {
                }
            }
        });
        helpMenu.add(helpAboutMenuItem);
    }

    /**
    * called when the Help/About menu item is selected
    *
    * @param frame the frame used to position the help box
    * @param productName the function name
    * @param versionAndDate the version and date of the function code
    * @param comments any function specific comments to be displayed
    */
    public void helpAboutMenuItem_actionPerformed(Frame frame, String productName, String versionAndDate, String[] comments) {
        aboutBox = new AboutBox(frame, productName, versionAndDate, comments);
        Dimension d = frame.getSize();
        Point p = frame.getLocationOnScreen();
        aboutBox.setLocation(((d.width - aboutBox.getSize().width) / 2) + p.x, ((d.height - aboutBox.getSize().height) / 2) + p.y);
        aboutBox.setVisible(true);
        aboutBox.setEnabled(true);
    }

    /**
    * this method is called when the data connection has been lost,
    * this will be used to notify anybody that cares...
    */
    public void connectionLost(DataShareConnection dsc) {
        System.out.println("connectionLost() called in TestMain...");
        cli.channelConnectionLost(this);
    }

    /**
    * this is where both the sharer and sharee receive data.
    * Note that some command cause a change in state (the logic for those is
    * handled in teh setSharerState method), and some cause other things...
    */
    public synchronized void newDataReceived(DataShareObject dataShareObject) {
        boolean dataIsFromHistory = dataShareObject.isFromHistory;
        pclass.getMyMainOfWrapper().showActivityInSession(myConnectionDescription.sessionName);
        try {
            Object o = SessionUtilities.retrieveObject(dataShareObject.objectBytes);
            AppSharePacket packet = (AppSharePacket) o;
            if (SessionUtilities.getVerbose()) System.out.println("AppShareMain.newDataRecieved with packet type of " + packet.typeString[packet.type] + (dataIsFromHistory ? "(from history)" : ""));
            if (dataIsFromHistory) {
                setSharerState(VIEW_HISTORY);
                if (packet.type == AppSharePacket.IMAGE || packet.type == AppSharePacket.DIMAGE) {
                    appSharePanel.setDisplayImageTitle(packet.clientKey + " (" + packet.length + " bytes in image)");
                    appSharePanel.displayImage(packet);
                }
            } else {
                switch(packet.type) {
                    case AppSharePacket.KEY_PRESSED:
                    case AppSharePacket.KEY_RELEASED:
                    case AppSharePacket.MOVED:
                    case AppSharePacket.RELEASED:
                    case AppSharePacket.PRESSED:
                    case AppSharePacket.CLIPBOARD_PASTE:
                    case AppSharePacket.CLIPBOARD_COPY:
                        if (sharerState == ACTIVE_SHARER && packet.clientKey.equals(controllingClient)) {
                            packet.apply(robot, this);
                        } else System.out.println("AppShareMain recieved control cmd, but we are not sharer or it came from wrong client");
                        break;
                    case AppSharePacket.IMAGE:
                    case AppSharePacket.DIMAGE:
                        appSharePanel.setDisplayImageTitle(packet.clientKey + " (" + packet.length + " bytes in image)");
                        appSharePanel.displayImage(packet);
                        if (getSharerState() == ENABLED_SHARER) {
                            activeSharer = packet.clientKey;
                            setSharerState(DISABLE_SHARER);
                        } else if (getSharerState() == CONTROLLER) {
                            if (SessionUtilities.getVerbose()) System.out.println("sending an ACK because we just displayed an image and we are controller");
                            sendDataToClient(new AppSharePacket(myClientKey, AppSharePacket.ACK), packet.clientKey);
                        }
                        break;
                    case AppSharePacket.CONTROL_REQUEST:
                        if (sharerState == ACTIVE_SHARER && controllingClient.equals("") && allowControl) {
                            int allow = JOptionPane.showConfirmDialog(appSharePanel, "Will you allow " + packet.clientKey + " to remotely\ncontrol your desktop?", "Confirm Remote Control", JOptionPane.YES_NO_OPTION);
                            if (allow == JOptionPane.YES_OPTION) {
                                controllingClient = packet.clientKey;
                                if (SessionUtilities.getVerbose()) System.out.println("AppShareMain.newData allowing " + packet.clientKey + " to control us");
                                sendDataToClient(new AppSharePacket(myClientKey, AppSharePacket.CONTROL_ALLOWED), packet.clientKey);
                                if (SessionUtilities.getVerbose()) System.out.println("AppShareMain.newData telling other clients that they cannot control");
                                sendDataToOthers(new AppSharePacket(myClientKey, AppSharePacket.CONTROL_DISABLED));
                                appSharePanel.setGUIState(getSharerState());
                            } else sendDataToClient(new AppSharePacket(myClientKey, AppSharePacket.CONTROL_ENABLED), packet.clientKey);
                        } else {
                            String reason = "I'm stubborn";
                            if (sharerState != ACTIVE_SHARER) reason = "we are not the ACTIVE_SHARER";
                            if (!controllingClient.equals("")) reason = controllingClient + " is already controlling us";
                            if (!allowControl) reason = "we are not allowing control at this time";
                            System.out.println("Not allowing " + packet.clientKey + " to control us because " + reason);
                            sendDataToClient(new AppSharePacket(myClientKey, AppSharePacket.CONTROL_ENABLED), packet.clientKey);
                        }
                        break;
                    case AppSharePacket.CONTROL_ALLOWED:
                        setSharerState(CONTROL);
                        if (SessionUtilities.getVerbose()) System.out.println("Sending an ACK because we just became the controller");
                        sendDataToClient(new AppSharePacket(myClientKey, AppSharePacket.ACK), packet.clientKey);
                        break;
                    case AppSharePacket.SHARER_DISABLE:
                        activeSharer = packet.clientKey;
                        setSharerState(DISABLE_SHARER);
                        break;
                    case AppSharePacket.SHARER_ENABLE:
                        setSharerState(ENABLE_SHARER);
                        break;
                    case AppSharePacket.ADJUST_SHARED_REGION:
                        break;
                    case AppSharePacket.SEND_IMAGES_SLOWER:
                        break;
                    case AppSharePacket.SEND_IMAGES_FASTER:
                        break;
                    case AppSharePacket.CONTROL_DISABLED:
                        if (sharerState != CONTROLLER) setSharerState(DISABLE_SHAREE);
                        break;
                    case AppSharePacket.CONTROL_ENABLED:
                        setSharerState(ENABLE_SHAREE);
                        break;
                    case AppSharePacket.CONTROL_STOPPING:
                        if (sharerState == CONTROLLER) setSharerState(END_CONTROL); else if (sharerState == ACTIVE_SHARER) {
                            controllingClient = "";
                            appSharePanel.setGUIState(ACTIVE_SHARER);
                        }
                        break;
                    case AppSharePacket.ACK:
                        if (sharerState == ACTIVE_SHARER && controllingClient.equals(packet.clientKey)) {
                            synchronized (ack) {
                                ack.notifyAll();
                            }
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * used when the sharer is actually sendingImages the desktop, send desktop images to the DataShare server
    */
    public void run() {
        df.resetImage();
        boolean lastCaptureWasDifferences = usingDifferences;
        while (sharerState == ACTIVE_SHARER) {
            if (sendingImages) {
                try {
                    AppSharePacket packet = null;
                    BufferedImage buff = robot.createScreenCapture(sharingRegion);
                    if (usingDifferences) {
                        if (!lastCaptureWasDifferences) {
                            df.resetImage();
                            lastCaptureWasDifferences = true;
                        }
                        Vector differences = df.createDifference(buff);
                        if (differences.size() > 0) packet = new AppSharePacket(differences, myClientKey);
                    } else {
                        lastCaptureWasDifferences = false;
                        if (!isDifferent(buff)) {
                            buff = null;
                        }
                        packet = new AppSharePacket(buff, myClientKey, quality);
                    }
                    if (packet != null) {
                        sendDataToOthers(packet);
                        if (previewing) {
                            if (appSharePanel != null) {
                                appSharePanel.setDisplayImageTitle("Previewing... sent " + packet.length + " bytes in image");
                                appSharePanel.displayImage(packet);
                                appSharePanel.imageLabel.setText(null);
                            }
                        } else {
                            if (appSharePanel != null) {
                                appSharePanel.setDisplayImageTitle("not Previewing...");
                                appSharePanel.displayImage(null);
                                appSharePanel.imageLabel.setText("sent " + packet.length + " bytes in image");
                            }
                        }
                        if (!controllingClient.equals("")) {
                            if (SessionUtilities.getVerbose()) System.out.println("Waiting for ACK from controller...");
                            waitForControllerToAck();
                            if (SessionUtilities.getVerbose()) System.out.println("Received ACK");
                        }
                    }
                } catch (java.awt.image.RasterFormatException rfe) {
                }
            }
            try {
                Thread.sleep(delayTime);
            } catch (Exception e) {
            }
        }
    }

    int[] lastPixelArrayReceived;

    int lastWidth, lastHeight;

    boolean isDifferent(BufferedImage image) {
        boolean different = true;
        if (image != null) {
            different = false;
            Raster r = image.getData();
            int[] pixelArray1 = r.getPixels(0, 0, r.getWidth(), r.getHeight(), (int[]) null);
            if (lastPixelArrayReceived != null) {
                if (pixelArray1.length == lastPixelArrayReceived.length && lastWidth == r.getWidth() && lastHeight == r.getHeight()) {
                    for (int i = 0; i < pixelArray1.length; i++) {
                        if (pixelArray1[i] != lastPixelArrayReceived[i]) {
                            different = true;
                            break;
                        }
                    }
                } else different = true;
            } else different = true;
            lastPixelArrayReceived = new int[pixelArray1.length];
            lastWidth = r.getWidth();
            lastHeight = r.getHeight();
            System.arraycopy(pixelArray1, 0, lastPixelArrayReceived, 0, pixelArray1.length);
        }
        return different;
    }

    /**
    * Only used by the ACTIVE_SHARER that is controlled,
    * waits until the controller sends an acknowledgement packet that it has received the last image,
    * or waits until we are notified that we are no longer controlled.
    */
    void waitForControllerToAck() {
        synchronized (ack) {
            try {
                ack.wait(15000);
            } catch (Exception e) {
            }
        }
    }

    /**
    * used by the server when we have disconnected the controller and therefore no longer want to 
    * wait for an ACK from them.
    */
    void forceACK() {
        synchronized (ack) {
            if (SessionUtilities.getVerbose()) System.out.println("We are internally forcing an ACK");
            ack.notifyAll();
        }
    }
}
