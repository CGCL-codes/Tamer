package org.wcb.autohome.util;

import com.jpeterson.x10.event.AddressListener;
import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.FunctionListener;
import com.jpeterson.x10.event.FunctionEvent;
import org.wcb.autohome.AutoHomeAdminSession;
import org.wcb.autohome.exceptions.HomeException;
import org.wcb.autohome.implementations.RunEvent;
import org.wcb.autohome.interfaces.IX10MonitorEvent;
import org.wcb.autohome.interfaces.IRunEvent;
import org.wcb.autohome.model.MonitorTableModel;
import java.io.IOException;

/**
 * AliceX10AddressListener.java
 *
 * Created on December 12, 2003, 7:59 PM
 *
 * @author  wbogaardt
 *
 *  Project:   Home Automation Interface
 *  Filename:  $Id: AliceX10AddressListener.java,v 1.9 2004/07/22 03:17:55 wbogaardt Exp $
 *  Abstract:  Listens and monitors CM11A device for events occuring over power line and
 *             logs the results.
 *
 *  $Log: AliceX10AddressListener.java,v $
 *  Revision 1.9  2004/07/22 03:17:55  wbogaardt
 *  removed deprecated methods
 *
 *  Revision 1.8  2004/02/02 23:41:31  wbogaardt
 *  refactored email so that properties are maintained in bean object and added allowing username and password authentication for email servers that require it.
 *
 *  Revision 1.7  2004/01/21 05:29:43  wbogaardt
 *  fixed bug saving file format and added disable monitoring
 *
 *  Revision 1.6  2004/01/20 19:36:00  wbogaardt
 *  fixed adding new objects to check for redundancy
 *
 *  Revision 1.5  2004/01/20 05:31:05  wbogaardt
 *  added speech event for runing
 *
 *  Revision 1.4  2004/01/20 00:20:58  wbogaardt
 *  allow email service to send status code back if email succeds or fails
 *
 *  Revision 1.3  2004/01/19 22:35:38  wbogaardt
 *  added fixes to runing events and emails so they work and added a testing of a monitored event through the table popup on a right mouse click.
 *
 *  Revision 1.2  2004/01/17 08:15:46  wbogaardt
 *  Have an initial working monitoring frame that actually shows the date and time an event takes place
 *
 *  Revision 1.1  2003/12/13 04:30:59  wbogaardt
 *  new CM11A monitoring listeners.
 *
 */
public class AliceX10AddressListener implements AddressListener, FunctionListener {

    private MonitorTableModel mModel;

    private EmailService eMailService;

    private IX10MonitorEvent iMainEvent;

    private static int deviceID = 0;

    /** Creates a new instance of AliceX10AddressListener */
    public AliceX10AddressListener(MonitorTableModel mModel) {
        this.mModel = mModel;
        eMailService = new EmailService(AutoHomeAdminSession.getInstance().getEmailInformation());
    }

    public void address(AddressEvent addressEvent) {
        deviceID = addressEvent.getDeviceCode();
    }

    /**
     * Run the event from the detected item.
     * @param event The main event that the action was detected.
     */
    public void runAction(IX10MonitorEvent event) {
        IRunEvent[] runEvents = event.getRunEvent();
        switch(runEvents[0].getRunType()) {
            case RunEvent.RUN_APPLICATION:
                this.runApplication(event);
                break;
            case RunEvent.RUN_MODULE:
                this.runModuleAction(event);
                break;
            case RunEvent.SEND_EMAIL:
                this.sendEmailNotification(event);
                break;
            case RunEvent.SPEAK_COMMAND:
                this.speakSentence(event);
                break;
        }
    }

    /**
     * This runs the user specified application if the module detects some event.
     *
     * @param iMonitor The monitoring module
     */
    private void runApplication(IX10MonitorEvent iMonitor) {
        IRunEvent[] runEvents = iMonitor.getRunEvent();
        String[] cmd = new String[1];
        if (!isAllArgumentsNull(runEvents[0].getArguments())) {
            cmd = new String[1 + runEvents[0].getArguments().length];
            for (int iCount = 0; iCount < runEvents[0].getArguments().length; iCount++) {
                if (runEvents[0].getArguments()[iCount] != null) {
                    cmd[1 + iCount] = runEvents[0].getArguments()[iCount];
                } else {
                    cmd[1 + iCount] = "";
                }
            }
        }
        cmd[0] = runEvents[0].getCommand();
        if (cmd[0] != null) {
            try {
                Runtime r = Runtime.getRuntime();
                r.exec(cmd);
            } catch (IOException ioe) {
                System.err.println("Unable to run command or application " + ioe);
            }
        }
    }

    /**
     * Tests to see if all the arguments are null if they are then
     * there were no arguments to pass to the runtime.
     * @param args the argumentts array to check
     * @return true has no arguments false has arguments
     */
    private boolean isAllArgumentsNull(String[] args) {
        for (int iCount = 0; iCount < args.length; iCount++) {
            if (args[iCount] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * This sends of an email message to the user of the monitor that noticed an action.
     * @param iMonitor The monitoring X10 device.
     */
    private void sendEmailNotification(IX10MonitorEvent iMonitor) {
        this.eMailService.sendEventMessage(iMonitor);
    }

    /**
     * This Runs the user specified action on an x10 device, which the monitoring x10 device has
     * been assigned to.
     * @param iMonitor The monitoring X10 device.
     */
    private void runModuleAction(IX10MonitorEvent iMonitor) {
        IRunEvent[] runEvents = iMonitor.getRunEvent();
        try {
            System.out.println(runEvents[0].getX10Module().getFullDeviceCode() + " running " + runEvents[0].getModuleCommand());
            AutoHomeAdminSession.getInstance().sendCommandToX10Module(runEvents[0].getX10Module(), runEvents[0].getModuleCommand());
        } catch (HomeException he) {
            System.err.println("Home exception " + he);
        }
    }

    private void speakSentence(IX10MonitorEvent iMonitor) {
        IRunEvent[] runEvents = iMonitor.getRunEvent();
        AutoHomeAdminSession.getInstance().printMessage(runEvents[0].getSentence());
    }

    /**
     * This function detects all lights off function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionAllLightsOff(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "All lights off");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    /**
     * This function detects all lights on function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionAllLightsOn(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "All lights on");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    /**
     * This function detects all units off function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionAllUnitsOff(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "All Units off");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    /**
     * This function detects a Brighten function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionBright(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "Brighten");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    /**
     * This function detects a dim function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionDim(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "Dim light");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    public void functionHailAcknowledge(FunctionEvent functionEvent) {
    }

    public void functionHailRequest(FunctionEvent functionEvent) {
    }

    /**
     * This function detects an off function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionOff(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "Off");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    /**
     * This function detects an on function on the House.  So this listener must first get the
     * device code information from the AddressListener, which should fire first. Then this should fire
     * as the next event to get the status type. The address listener gives the house and device id.
     * @param functionEvent The function or action that was fired on a house code.
     */
    public void functionOn(FunctionEvent functionEvent) {
        iMainEvent = this.mModel.getModuleOfEventDetected(functionEvent.getHouseCode(), deviceID, "On");
        if (iMainEvent != null) {
            this.runAction(iMainEvent);
        }
    }

    public void functionPresetDim1(FunctionEvent functionEvent) {
    }

    public void functionPresetDim2(FunctionEvent functionEvent) {
    }

    public void functionStatusOff(FunctionEvent functionEvent) {
        AutoHomeAdminSession.getInstance().printMessage("Detected status off module at " + functionEvent.getHouseCode());
    }

    public void functionStatusOn(FunctionEvent functionEvent) {
        AutoHomeAdminSession.getInstance().printMessage("Detected status on module at " + functionEvent.getHouseCode());
    }

    public void functionStatusRequest(FunctionEvent functionEvent) {
    }
}
