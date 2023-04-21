package org.equanda.tapestry.pages.userAdmin.tablePreferencesPage;

import org.equanda.tapestry.components.userAdmin.preferences.tablePreferences.TablePreferences;
import org.equanda.tapestry.navigation.NavigationablePageImpl;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.components.Block;

/**
 * General page for table rights.
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
public abstract class TablePreferencesPage extends NavigationablePageImpl<TablePreferencesPageParameters> {

    /**
     * Component which handles the rendering of the pages.
     */
    @Component(type = "TablePreferences", id = "tablePreferences", bindings = { "tableDescription = PageParameters.tableDescription" })
    public abstract TablePreferences getTablePreferencesComponent();

    @Component(type = "PreferencesInputBlock", id = "preferencesInputBlock")
    public abstract Block getPreferencesInputBlockComponent();

    public Block getInputFieldBlock() {
        return getPreferencesInputBlockComponent();
    }

    @Override
    public TablePreferencesPageParameters buildNewPageParameters() {
        return new TablePreferencesPageParameters();
    }

    public String getTitle() {
        return getMessages().getMessage("title") + ' ' + getMessages().getMessage("table." + getPageParameters().getTableName() + ".label");
    }
}
