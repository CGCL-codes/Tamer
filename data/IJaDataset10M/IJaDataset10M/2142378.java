package org.in4ama.editor.datadesigner.spreadsheet;

import net.xoetrope.xui.XProjectManager;
import org.in4ama.editor.datadesigner.XuiDataSourceDesigner;
import org.in4ama.editor.datadesigner.XuiDataSourcePage;

/** SpreadsheetDataSourceDesigner, managed by Spring */
public class SpreadsheetDataSourceDesigner extends XuiDataSourceDesigner {

    public static final String NAME = "datasource.designer.spreadsheet";

    /** Gets the page providing components for the
     * JDBC data source configuration */
    @Override
    protected XuiDataSourcePage createPage() {
        return (XuiDataSourcePage) XProjectManager.getCurrentProject().getPageManager().loadPage("spreadsheetdatasourcedesigner");
    }

    /** Returns the name of this plugin */
    public String getName() {
        return NAME;
    }
}
