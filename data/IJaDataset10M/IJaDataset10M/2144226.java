package jwsf.exitwindows;

/**
 *  ExitWindows performs several shutdown operations on a windows machine. Operations include
 *  shutdown, reboot, logoff, and locking a workstation. All shutdown operations return as soon as 
 *  the operation it entails has been initiated. Then the initiated operation proceeds asynchronously. 
 *  
 *  The shutdown, reboot, and logoff, operations stop all process in the caller's session. To that end, these operations 
 *  are likely to fail, if the caller is not an interative user, or if the caller does not have the shutdown privilege. 
 *  A return value of true from these operations does not mean the operations was or will be successful. 
 *  It only indicates the operation has been initiated.
 *  
 *  Shutdown, reboot, and logoff operations can be stoped by applications, services, or even the system. 
 *  A return value of true means the system accepted the shutdown request. The caller, can specify whether 
 *  or not applications with unsaved changes should be forcibly closed.
 *  If the caller chooses not to force these applications to close and an application with
 *  unsaved changes is running on the console session, the shutdown will remain in progress until the user logged 
 *  into the console session aborts the shutdown, saves changes, closes the application, or 
 *  forces the application to close. During this period, the shutdown may not be aborted except by the console user, 
 *  and another shutdown may not be initiated.
 *   
 *  Passing the<code>setForce</code>  method a true value avoids this situation. However doing so can result in loss of data.
 *  During a shutdown or log-off operation, applications that are shut down are allowed a specific amount 
 *  of time to respond to the shutdown request. If the time expires, the system displays a dialog box that allows
 *  the user to forcibly shut down the application, to retry the shutdown, or to cancel the shutdown request. 
 *  If <code>setForce</code>  method is passed a value of true, the system always forces applications to close and does not display the dialog box.
 * 
 *  You can also specify that hung applications be forcefully closed. This can be done by passing a true value to setForceIfHung().
 *  If <code>setForceIfHung</code> is true,the system forces hung applications to close and does not display the dialog box. For a given reboot
 *  or shutdown opeation only one of these
 *  
 * 
 *  The ExitWindows class can also log the reason for initiating the shutdown in the windows event log. 
 *  ExitWindows provides two reasons namely unplanned and planned. Planned reason means that the shutdown was planned. 
 *  The system generates a System State Data (SSD) file. This file contains system state information such as the processes, 
 *  threads, memory usage, and configuration. Unplanned reason means the shutdown was unplanned. Unplanned is
 *  set as the default. However the reason can be changed using the isPlanned() method. In addition, this class does not
 *  log any reasons on Windows 2000/NT and Windows Me/98/95. 
 * 
 *  
 *  Below is a valid description of the two reasons logged on windows:
 *  Unplanned ( "Other (Unplanned)" An unplanned shutdown or restart.)
 *  Planned ( "Other (Planned)" A planned shutdown or restart.)
 *  
 * 
 *  There can be only one instance of this class in a given JVM instance. 
 * 
 *  Note: ExitWindows is only compatable with the following windows versions: 
 *  Windows Vista, Windows XP, Windows 2000 Professional, 
 *  NT Workstation, Windows Me, Windows 98, or Windows 95,
 *  Windows Server "Longhorn", Windows Server 2003, Windows 2000 Server, or Windows NT Server.
 * 
 *  
 *
 * @author David Alenkhe
 * @version 1.0, Jan 14, 2009
 */
public class ExitWindows {

    static {
        System.loadLibrary("lib/JWSF");
    }

    private static ExitWindows uniqueInstance;

    private ExitWindows() {
    }

    public static synchronized ExitWindows getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ExitWindows();
        }
        return uniqueInstance;
    }

    private native boolean Logoff();

    private native boolean LockWorkstation();

    private native boolean Shutdown();

    private native boolean Reboot();

    private native void SetForce(boolean bforce);

    private native void SetForceIfHung(boolean forceIfHung);

    private native void IsPlanned(boolean isPlanned);

    public boolean logOff() {
        return Logoff();
    }

    public boolean lockWorkstation() {
        return LockWorkstation();
    }

    public boolean shutdown() {
        return Shutdown();
    }

    public boolean reboot() {
        return Reboot();
    }

    public void setForce(boolean bforce) {
        SetForce(bforce);
    }

    public void setForceIfHung(boolean forceIfHung) {
        SetForceIfHung(forceIfHung);
    }

    public void isPlanned(boolean isPlanned) {
        IsPlanned(isPlanned);
    }

    public static void main(String[] args) {
        ExitWindows eWin = getInstance();
        System.out.println(eWin.lockWorkstation());
    }
}
