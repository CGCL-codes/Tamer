package com.aelitis.azureus.ui.swt.views.skin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Shell;
import org.gudy.azureus2.core3.util.AERunnable;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.ui.swt.Utils;
import org.gudy.azureus2.ui.swt.components.shell.ShellFactory;
import com.aelitis.azureus.ui.swt.UIFunctionsManagerSWT;
import com.aelitis.azureus.ui.swt.skin.SWTSkin;
import com.aelitis.azureus.ui.swt.skin.SWTSkinFactory;

/**
 * @author TuxPaper
 * @created Dec 23, 2008
 *
 */
public class SkinnedDialog {

    private final String shellSkinObjectID;

    private Shell shell;

    private SWTSkin skin;

    private List<SkinnedDialogClosedListener> closeListeners = new CopyOnWriteArrayList<SkinnedDialogClosedListener>();

    private Shell mainShell;

    protected boolean disposed;

    public SkinnedDialog(String skinFile, String shellSkinObjectID) {
        this(skinFile, shellSkinObjectID, SWT.DIALOG_TRIM | SWT.RESIZE);
    }

    public SkinnedDialog(String skinFile, String shellSkinObjectID, int style) {
        this.shellSkinObjectID = shellSkinObjectID;
        mainShell = UIFunctionsManagerSWT.getUIFunctionsSWT().getMainShell();
        shell = ShellFactory.createShell(mainShell, style);
        Utils.setShellIcon(shell);
        SWTSkin skin = SWTSkinFactory.getNonPersistentInstance(SkinnedDialog.class.getClassLoader(), "com/aelitis/azureus/ui/skin/", skinFile + ".properties");
        setSkin(skin);
        skin.initialize(shell, shellSkinObjectID);
        shell.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    shell.close();
                }
            }
        });
        shell.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                disposed = true;
                Utils.execSWTThreadLater(0, new AERunnable() {

                    public void runSupport() {
                        for (SkinnedDialogClosedListener l : closeListeners) {
                            try {
                                l.skinDialogClosed(SkinnedDialog.this);
                            } catch (Exception e2) {
                                Debug.out(e2);
                            }
                        }
                    }
                });
            }
        });
        disposed = false;
    }

    protected void setSkin(SWTSkin _skin) {
        skin = _skin;
    }

    public void open() {
        if (disposed) {
            Debug.out("can't opened disposed skinnedialog");
            return;
        }
        skin.layout();
        Utils.centerWindowRelativeTo(shell, mainShell);
        shell.open();
    }

    public SWTSkin getSkin() {
        return skin;
    }

    /**
	 * 
	 *
	 * @since 4.0.0.5
	 */
    public void close() {
        Utils.execSWTThread(new AERunnable() {

            public void runSupport() {
                if (disposed) {
                    return;
                }
                if (shell != null && !shell.isDisposed()) {
                    shell.close();
                }
            }
        });
    }

    public void addCloseListener(SkinnedDialogClosedListener l) {
        closeListeners.add(l);
    }

    public interface SkinnedDialogClosedListener {

        public void skinDialogClosed(SkinnedDialog dialog);
    }

    /**
	 * @param string
	 *
	 * @since 4.0.0.5
	 */
    public void setTitle(String string) {
        if (!disposed && shell != null && !shell.isDisposed()) {
            shell.setText(string);
        }
    }

    /**
	 * @return the shell
	 */
    public Shell getShell() {
        return shell;
    }

    public boolean isDisposed() {
        return disposed || shell == null || shell.isDisposed();
    }
}
