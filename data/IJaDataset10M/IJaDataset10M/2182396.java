package net.sf.mmm.ui.toolkit.api.view.window;

/**
 * This is the interface of the workbench window. This is the master window for
 * an application that can contain {@link UiFrame}s within its content-area.<br/>
 * In swing this is called {@link javax.swing.JDesktopPane}, in web applications
 * this will be the browser window itself and for SWT this might be the eclipse
 * RCP platform.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface UiWorkbench extends UiFrame {

    /** the type of this object */
    String TYPE = "Workbench";
}
