package org.equanda.tapestry.pages.userAdmin.adminBorderConfig;

import org.equanda.tapestry.navigation.NavigationablePageImpl;
import org.equanda.tapestry.parser.border.Parser;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * Page to configure visibility of border items, for any role
 *
 * @author Florin
 */
public abstract class AdminBorderConfig extends NavigationablePageImpl {

    private void addNames(Parser.ItemDescription item, ArrayList<String> names) {
        names.add(item.getUserAdminId());
        for (Parser.ItemDescription child : item.getItems()) {
            addNames(child, names);
        }
    }

    private void addLabels(Parser.ItemDescription item, ArrayList<String> labels) {
        labels.add(getMessages().getMessage(item.getKey()));
        for (Parser.ItemDescription child : item.getItems()) {
            addLabels(child, labels);
        }
    }

    public ArrayList getNames() {
        List<Parser.ItemDescription> items = getVisitObject().getBorderItems();
        ArrayList<String> names = new ArrayList<String>();
        for (Parser.ItemDescription item : items) {
            addNames(item, names);
        }
        return names;
    }

    public ArrayList getLabels() {
        List<Parser.ItemDescription> items = getVisitObject().getBorderItems();
        ArrayList<String> labels = new ArrayList<String>();
        for (Parser.ItemDescription item : items) {
            addLabels(item, labels);
        }
        return labels;
    }

    public IPropertySelectionModel getSelectionModel() {
        return new org.equanda.tapestry.selectionModel.RoleSelectionModel(getPage());
    }
}
