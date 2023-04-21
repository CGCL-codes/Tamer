package pku.edu.tutor.macro;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Node;
import pku.edu.tutor.commands.MacroCommandShell;
import pku.edu.tutor.core.IPlayable;
import pku.edu.tutor.core.IWritable;
import pku.edu.tutor.core.WidgetIdentifier;

/**
 * Models a macro
 * 
 * 
 * @author Zhang Ying
 */
public class Macro implements IWritable, IPlayable {

    private static final String SYNTAX_VERSION = "1.0";

    public static final int WORKBENCH_PART_CLOSED = -1;

    private String name;

    private ArrayList shells;

    private Stack shellStack;

    /**
	 * stack present x-coordinates, arraylist presents y_coordinates
	 */
    public Macro() {
        shells = new ArrayList();
    }

    public Macro(String name) {
        this();
        this.name = name;
    }

    public void addShell(Node node, Hashtable lineTable) throws CoreException {
        MacroCommandShell shell = new MacroCommandShell(null, null, null);
        shell.load(node, lineTable);
        shells.add(shell);
    }

    public void initializeForRecording(Display display) {
        shellStack = new Stack();
        shells.clear();
        Shell currentShell = display.getActiveShell();
        if (currentShell == null) {
            return;
        }
        Object data = currentShell.getData(MacroManager.IGNORE);
        if (data != null && data instanceof Boolean && ((Boolean) data).booleanValue()) return;
        pushStack(createCommandShell(currentShell));
    }

    private MacroCommandShell createCommandShell(Shell shell) {
        WidgetIdentifier wi = MacroUtil.getWidgetIdentifier(shell);
        if (wi == null) return null;
        return new MacroCommandShell(null, shell, wi);
    }

    private boolean isCurrent(Shell shell) {
        if (shellStack.isEmpty()) return false;
        MacroCommandShell cshell = (MacroCommandShell) shellStack.peek();
        return cshell.tracks(shell);
    }

    public void stopRecording() {
        reset();
    }

    /**
	 * associate the given event which occurred on a shell with this shell
	 * 
	 * @param event
	 * @return
	 * @throws Exception
	 */
    public boolean addEvent(Event event) throws Exception {
        if (MacroUtil.isIgnorableEvent(event)) return false;
        try {
            if (event.widget instanceof Shell) {
                switch(event.type) {
                    case SWT.Activate:
                        activateShell((Shell) event.widget);
                        break;
                    case SWT.Close:
                        boolean stop = closeShell((Shell) event.widget);
                        if (stop) return true;
                        break;
                }
            } else {
                MacroCommandShell topCommandShell = getTopShell();
                if (topCommandShell != null) {
                    Shell topShell = topCommandShell.getShell();
                    Shell widgetShell = (!(event.widget instanceof Control) ? null : ((Control) event.widget).getShell());
                    if (widgetShell == null || topShell == widgetShell) {
                        topCommandShell.addEvent(event);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    public void addPause() {
        MacroCommandShell topShell = getTopShell();
        if (topShell != null) topShell.addPause();
    }

    public MacroCommandShell getTopShell() {
        if (shellStack.isEmpty()) return null;
        return (MacroCommandShell) shellStack.peek();
    }

    private void activateShell(Shell shell) {
        if (shell.isDisposed()) return;
        Object data = shell.getData();
        if (data instanceof Dialog) {
            if (!isCurrent(shell)) {
                MacroCommandShell parentOfCurrentShell = null;
                if (shellStack.size() - 2 >= 0) {
                    parentOfCurrentShell = (MacroCommandShell) shellStack.get(shellStack.size() - 2);
                }
                if (parentOfCurrentShell != null && parentOfCurrentShell.tracks(shell)) {
                    popStack();
                } else {
                    MacroCommandShell commandShell = createCommandShell(shell);
                    if (getTopShell() == null) {
                        pushStack(commandShell);
                    } else {
                        getTopShell().addCommandShell(commandShell);
                        shellStack.push(commandShell);
                    }
                }
            }
        } else if (data instanceof Window) {
            updateStack();
            if (!isCurrent(shell)) {
                popStack();
                MacroCommandShell newCommandShell = createCommandShell(shell);
                MacroCommandShell topCommandShell = getTopShell();
                if (topCommandShell == null || !topCommandShell.getWidgetIdentifier().getObjectId().equals(newCommandShell.getWidgetIdentifier().getObjectId())) {
                    pushStack(newCommandShell);
                }
            }
        }
    }

    private void popStack() {
        if (shellStack.isEmpty()) return;
        MacroCommandShell top = (MacroCommandShell) shellStack.pop();
        top.extractExpectedReturnCode();
    }

    private void pushStack(MacroCommandShell commandShell) {
        shellStack.push(commandShell);
        shells.add(commandShell);
    }

    private boolean closeShell(Shell shell) {
        if (shellStack.isEmpty()) return false;
        MacroCommandShell top = (MacroCommandShell) shellStack.peek();
        if (top.tracks(shell)) popStack();
        return shellStack.isEmpty();
    }

    private void updateStack() {
        while (shellStack.size() > 0) {
            MacroCommandShell top = getTopShell();
            if (top.isDisposed()) popStack(); else break;
        }
    }

    /**
	 * actually playback the commands associate with these commondshells
	 */
    public boolean playback(Display display, Composite parent, IProgressMonitor monitor) throws CoreException {
        reset();
        for (int i = 0; i < shells.size(); i++) {
            MacroCommandShell shell = (MacroCommandShell) shells.get(i);
            final Shell[] sh = new Shell[1];
            display.syncExec(new Runnable() {

                public void run() {
                    sh[0] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                }
            });
            try {
                boolean result = shell.playback(display, sh[0], new SubProgressMonitor(monitor, 1));
                if (!result) return false;
            } catch (CoreException e) {
                throw e;
            }
        }
        return true;
    }

    private void reset() {
        shellStack = null;
    }

    public void write(int indent, PrintWriter writer) {
        StringBuffer sb = new StringBuffer();
        write(indent, sb);
        writer.write(sb.toString());
    }

    public void write(int indent, StringBuffer sb) {
        MacroUtil.addElement(sb, indent, MacroConstants.MACRO_ELEMENT, false, false);
        MacroUtil.addAttribute(sb, new String[] { MacroConstants.VERSION_ATTRIBUTE }, new String[] { SYNTAX_VERSION }, false, true);
        int cindent = 1;
        for (int i = 0; i < shells.size(); i++) {
            MacroCommandShell cshell = (MacroCommandShell) shells.get(i);
            cshell.write(cindent, sb);
        }
        MacroUtil.addElement(sb, indent, MacroConstants.MACRO_ELEMENT, true, true);
    }

    public String getName() {
        return name;
    }

    public void writeStart(int indent, StringBuffer writer) {
    }

    public void writeFinish(int indent, StringBuffer writer) {
    }

    public int getShellStackSize() {
        return shellStack.size();
    }
}
