package de.beas.explicanto.client.rcp.pageedit2.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datastructures.CStyleSheet;
import de.bea.services.vidya.client.datastructures.CTableComponent;
import de.beas.explicanto.client.rcp.components.html.TableFormatDlg;
import de.beas.explicanto.client.rcp.pageedit2.PageComponent;
import de.beas.explicanto.client.rcp.pageedit2.PageRegion;

/**
 * 
 * AddTextComponentAction
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class AddTableComponentAction extends PageEditorAction {

    protected TableFormatDlg dlg;

    protected void performAction(PageRegion region, IAction action) {
        log.debug("inserting table component");
        dlg = new TableFormatDlg(window.getShell(), VidyaDataTree.getDefault().getRootCustomer().getStyleSheets());
        if (dlg.open() != Dialog.OK) return;
        region.addComp(getNewTextComponent(region));
    }

    protected void performAction(PageComponent component, IAction action) {
        dlg = new TableFormatDlg(window.getShell(), VidyaDataTree.getDefault().getRootCustomer().getStyleSheets());
        if (dlg.open() != Dialog.OK) return;
        component.getPageRegion().addComp(getNewTextComponent(component.getPageRegion()));
    }

    protected CTableComponent getNewTextComponent(PageRegion region) {
        int cols = dlg.getColsCount();
        int rows = dlg.getRowsCount();
        int type = dlg.getType();
        CStyleSheet css = dlg.getCss();
        CTableComponent table = new CTableComponent(region.getRegion().getBoundingBox().getWidth(), 100, rows, cols, type, css.getStyleSheetType(), region.getPage().getNextUidIncr(), region.getPage());
        return table;
    }

    protected String getTranslatedName() {
        return translate("pageEditor.menu.insert.table");
    }

    protected String getTranslatedToolTip() {
        return translate("pageEditor.menu.insert.table");
    }
}
