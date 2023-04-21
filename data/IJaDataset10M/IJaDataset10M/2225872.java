package pl.vdl.azbest.mremote.gui.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import pl.vdl.azbest.log.LoggingTheGathering;
import pl.vdl.azbest.log.gui.composite.CompositeSystemConsole;
import pl.vdl.azbest.mremote.Conf;
import pl.vdl.azbest.mremote.gui.SWTElement;

/**
 * 
 * CompositeSystemConsole Wrapper
 * @author azbest
 * */
public class SystemConsole extends SWTElement {

    /**
	 * This method initializes sShell
	 */
    @Override
    protected void createSShell() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        sShell = new Shell();
        sShell.setText("System Console");
        sShell.setSize(new Point(484, 290));
        sShell.setLayout(new GridLayout());
        sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {

            public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
                Conf.getInstance().consoleOpen = false;
            }
        });
        CompositeSystemConsole csc = new CompositeSystemConsole(sShell, SWT.NONE);
        csc.setLayoutData(gridData);
        Conf.getInstance().consoleOpen = true;
    }

    {
        LoggingTheGathering.addPath(getClass().getName());
    }
}
