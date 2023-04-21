package net.sf.edevtools.tools.logviewer.core.tests.handlers;

import net.sf.edevtools.tools.logviewer.core.preferences.ILogViewerPrefs;
import net.sf.edevtools.tools.logviewer.core.tests.EDvTLogViewTestMain;
import net.sf.edevtools.tools.logviewer.core.tests.socket.TestSocketClient;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartLogEmitter extends AbstractHandler {

    /**
	 * The constructor.
	 */
    public StartLogEmitter() {
    }

    /**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        InputDialog lDlg = new InputDialog(null, "Start Test Log Emitter", "Select a port to start a test emitter on.", Integer.toString(ILogViewerPrefs.INSTANCE.getDefaultPort()), new IInputValidator() {

            public String isValid(String aNewText) {
                try {
                    Integer.parseInt(aNewText);
                } catch (Throwable ex) {
                    return "This is not a valid port number";
                }
                return null;
            }
        });
        lDlg.setBlockOnOpen(true);
        if (lDlg.open() == Window.OK) {
            int lPort = Integer.parseInt(lDlg.getValue());
            System.out.println("Starting client " + lPort);
            TestSocketClient lClient = EDvTLogViewTestMain.getInstance().createTestSocketClient(lPort);
            if (lClient != null) {
                System.out.println("Starting client " + lPort);
                Thread lThread = new Thread(lClient);
                lThread.start();
            }
        }
        return null;
    }
}
