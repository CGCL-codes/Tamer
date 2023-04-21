package com.google.gdt.eclipse.designer.ie;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;
import org.eclipse.wb.internal.core.utils.ui.UiUtils;
import com.google.gdt.eclipse.designer.ie.jsni.ModuleSpaceIE;
import com.google.gdt.eclipse.designer.ie.util.Utils;
import com.google.gwt.dev.shell.designtime.JsValue;
import com.google.gwt.dev.shell.designtime.ModuleSpace;

/**
 * Browser shell implementation for Windows.
 * 
 * @author mitin_aa
 */
public final class BrowserShellIE extends com.google.gdt.eclipse.designer.hosted.tdt.BrowserShell {

    private final Shell m_shell;

    private final Browser m_browser;

    private final IEInitializer m_helper;

    public BrowserShellIE() {
        m_shell = new Shell(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        m_shell.setLayout(new FillLayout());
        m_shell.setLocation(-10000, -10000);
        m_shell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(ShellEvent e) {
                e.doit = false;
                Shell shell = (Shell) e.widget;
                shell.setMinimized(false);
                shell.setMaximized(false);
                shell.setVisible(false);
            }
        });
        m_browser = new Browser(m_shell, SWT.NONE);
        Utils.ensureProxyBypassLocal();
        m_helper = new IEInitializer(m_browser);
        m_shell.layout();
        m_shell.setEnabled(false);
    }

    public Rectangle computeTrim(int x, int y, int width, int height) {
        return m_shell.computeTrim(x, y, width, height);
    }

    @Override
    public void dispose() {
        m_shell.dispose();
        try {
            ModuleSpace space = getModuleSpace();
            if (space != null) {
                space.dispose();
            }
        } catch (Throwable e) {
            ReflectionUtils.propagate(e);
        }
        System.gc();
        System.runFinalization();
        JsValue.mainThreadCleanup();
        super.dispose();
    }

    public boolean isDisposed() {
        return m_shell == null || m_shell.isDisposed();
    }

    public void setBounds(Rectangle bounds) {
        m_shell.setBounds(bounds);
    }

    public void setLocation(int x, int y) {
        m_shell.setLocation(x, y);
    }

    public void setSize(int width, int height) {
        m_shell.setSize(width, height);
    }

    public void setUrl(String url, String moduleName, int timeout, Runnable messageProcessor) throws Exception {
        try {
            m_browser.setUrl(url);
            m_helper.wait(timeout, messageProcessor);
            this.moduleSpace = new ModuleSpaceIE(getHost(), m_helper.m_window, moduleName);
        } finally {
            m_helper.dispose();
        }
        getModuleSpace().onLoad();
    }

    public void setVisible(boolean visible) {
        try {
            m_shell.setVisible(visible);
        } catch (SWTException e) {
        } catch (IllegalArgumentException e) {
        }
    }

    public void prepare() {
        setLocation(-10000, -10000);
    }

    public Image createBrowserScreenshot() throws Exception {
        return Utils.makeShot(m_browser);
    }

    public String getUserAgentString() {
        return "ie8";
    }

    public void showAsPreview() {
        centerShellInDisplay();
        openShellAsModal();
    }

    private void centerShellInDisplay() {
        try {
            Rectangle containerBounds = m_shell.getDisplay().getClientArea();
            Rectangle shellBounds = m_shell.getBounds();
            int x = Math.max(0, containerBounds.x + (containerBounds.width - shellBounds.width) / 2);
            int y = Math.max(0, containerBounds.y + (containerBounds.height - shellBounds.height) / 3);
            m_shell.setSize(shellBounds.width, shellBounds.height);
            m_shell.setLocation(x, y);
        } catch (SWTException e) {
        } catch (IllegalArgumentException e) {
        }
    }

    private void openShellAsModal() {
        m_shell.setEnabled(true);
        try {
            UiUtils.runModalShell(m_shell);
        } finally {
            m_shell.setEnabled(false);
        }
    }
}
