package com.safi.workshop.sqlexplorer.sqleditor.actions;

import java.sql.SQLException;
import java.util.LinkedList;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import com.safi.workshop.sqlexplorer.dbproduct.MetaDataSession;
import com.safi.workshop.sqlexplorer.dbproduct.Session;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;
import com.safi.workshop.sqlexplorer.plugin.editors.SQLEditor;
import com.safi.workshop.sqlexplorer.util.ImageUtil;
import com.safi.workshop.sqlexplorer.util.TextUtil;

/**
 * SQLEditorToolBar controls the toolbar displayed in the editor.
 * 
 * @modified John Spackman
 */
public class SQLEditorToolBar {

    private SQLEditorCatalogSwitcher _catalogSwitcher;

    private ToolBarManager _catalogToolBarMgr;

    private CoolBar _coolBar;

    private CoolBarManager _coolBarMgr;

    private ToolBarManager _defaultToolBarMgr;

    private SQLEditor _editor;

    private ToolBarManager _extensionToolBarMgr;

    private SQLEditorSessionSwitcher _sessionSwitcher;

    private SQLLimitRowsControl _limitRows;

    private ToolBarManager _sessionToolBarMgr;

    private LinkedList<AbstractEditorAction> actions = new LinkedList<AbstractEditorAction>();

    /**
   * Create a new toolbar on the given composite.
   * 
   * @param parent
   *          composite to draw toolbar on.
   * @param editor
   *          parent editor for this toolbar.
   */
    public SQLEditorToolBar(Composite parent, SQLEditor editor) {
        _editor = editor;
        _coolBar = new CoolBar(parent, SWT.FLAT);
        _coolBarMgr = new CoolBarManager(_coolBar);
        GridData gid = new GridData();
        gid.horizontalAlignment = GridData.FILL;
        _coolBar.setLayoutData(gid);
        _defaultToolBarMgr = new ToolBarManager(SWT.FLAT);
        actions.add(new ExecSQLAction(_editor));
        actions.add(new CommitAction(_editor));
        actions.add(new RollbackAction(_editor));
        actions.add(new OpenFileAction(_editor));
        actions.add(new SaveEditorAction(_editor));
        actions.add(new AddQueryAction(_editor));
        actions.add(new ClearTextAction(_editor));
        actions.add(new OptionsDropDownAction(_editor, parent));
        for (AbstractEditorAction action : actions) {
            action.setEnabled(!action.isDisabled());
            _defaultToolBarMgr.add(action);
        }
        _extensionToolBarMgr = new ToolBarManager(SWT.FLAT);
        _sessionToolBarMgr = new ToolBarManager(SWT.FLAT);
        _sessionSwitcher = new SQLEditorSessionSwitcher(editor);
        _sessionToolBarMgr.add(_sessionSwitcher);
        _limitRows = new SQLLimitRowsControl(editor);
        _sessionToolBarMgr.add(_limitRows);
        _catalogToolBarMgr = new ToolBarManager(SWT.FLAT);
        _coolBarMgr.add(new ToolBarContributionItem(_defaultToolBarMgr));
        _coolBarMgr.add(new ToolBarContributionItem(_extensionToolBarMgr));
        _coolBarMgr.add(new ToolBarContributionItem(_sessionToolBarMgr));
        _coolBarMgr.add(new ToolBarContributionItem(_catalogToolBarMgr));
        _coolBarMgr.update(true);
    }

    public void addResizeListener(ControlListener listener) {
        _coolBar.addControlListener(listener);
    }

    /**
   * Updates the default actions to reflect their enabled-ness
   * 
   */
    private void updateDefaultActions() {
        for (AbstractEditorAction action : actions) action.setEnabled(!action.isDisabled());
        _defaultToolBarMgr.update(true);
    }

    /**
   * Called to notify that the editor's session has changed
   * 
   * @param session
   *          The new session (can be null)
   */
    public void onEditorSessionChanged(final Session session) {
        if (_editor.getSite() != null && _editor.getSite().getShell() != null && _editor.getSite().getShell().getDisplay() != null) _editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                if (_coolBar.isDisposed()) return;
                _extensionToolBarMgr.removeAll();
                _catalogToolBarMgr.removeAll();
                _catalogSwitcher = null;
                if (session != null) doOnEditorSessionChanged(session);
                updateDefaultActions();
                _extensionToolBarMgr.update(true);
                _coolBarMgr.update(true);
                _coolBar.update();
            }
        });
    }

    public String getCurrentCatalog() {
        if (_catalogSwitcher == null) return null;
        return _catalogSwitcher.getSelectedCatalog();
    }

    /**
   * Implementation for onEditorSessionChanged; only called if the new session is non-null
   * 
   * @param session
   *          The new session (cannot be null)
   */
    private void doOnEditorSessionChanged(Session session) {
        String databaseProductName = null;
        MetaDataSession metaDataSession = session.getUser().getMetaDataSession();
        try {
            if (session.getUser() == null) return;
            if (metaDataSession != null) databaseProductName = metaDataSession.getDatabaseProductName().toLowerCase().trim(); else databaseProductName = "offline db";
        } catch (SQLException e) {
            SQLExplorerPlugin.error(e);
            MessageDialog.openError(_editor.getSite().getShell(), "Cannot connect", e.getMessage());
        }
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint("AsteriskSafletDesigner.diagram", "editorAction");
        IExtension[] extensions = point.getExtensions();
        for (IExtension e : extensions) {
            IConfigurationElement[] ces = e.getConfigurationElements();
            for (IConfigurationElement ce : ces) {
                try {
                    boolean isValidProduct = false;
                    String[] validProducts = ce.getAttribute("database-product-name").split(",");
                    String imagePath = ce.getAttribute("icon");
                    String id = ce.getAttribute("id");
                    for (String validProduct : validProducts) {
                        String product = validProduct.toLowerCase().trim();
                        if (product.length() == 0) {
                            continue;
                        }
                        if (product.equals("*")) {
                            isValidProduct = true;
                            break;
                        }
                        String regex = TextUtil.replaceChar(product, '*', ".*");
                        if (databaseProductName != null && databaseProductName.matches(regex)) {
                            isValidProduct = true;
                            break;
                        }
                    }
                    if (!isValidProduct) {
                        continue;
                    }
                    AbstractEditorAction action = (AbstractEditorAction) ce.createExecutableExtension("class");
                    action.setEditor(_editor);
                    String fragmentId = id.substring(0, id.indexOf('.', 28));
                    if (imagePath != null && imagePath.trim().length() != 0) {
                        action.setImageDescriptor(ImageUtil.getFragmentDescriptor(fragmentId, imagePath));
                    }
                    _extensionToolBarMgr.add(action);
                } catch (Throwable ex) {
                    SQLExplorerPlugin.error("Could not create editor action", ex);
                }
            }
        }
        _catalogToolBarMgr.removeAll();
        if (metaDataSession != null && metaDataSession.getCatalogs() != null) {
            _catalogSwitcher = new SQLEditorCatalogSwitcher(_editor);
            _catalogToolBarMgr.add(_catalogSwitcher);
        }
    }

    /**
   * Refresh actions availability on the toolbar.
   */
    public void refresh() {
        if (_editor.getSite() != null && _editor.getSite().getShell() != null && _editor.getSite().getShell().getDisplay() != null) _editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                if (_coolBar.isDisposed()) return;
                updateDefaultActions();
                _sessionToolBarMgr.update(true);
                _coolBarMgr.update(true);
                _coolBar.update();
            }
        });
    }

    /**
   * Returns the control
   * 
   * @return the _coolBar
   */
    public CoolBar getToolbarControl() {
        return _coolBar;
    }

    /**
   * Returns whether to limit the results and if so by how much.
   * 
   * @return the maximum number of rows to retrieve, 0 for unlimited, or null if it cannot
   *         be interpretted
   */
    public Integer getLimitResults() {
        return _limitRows.getLimitResults();
    }
}
